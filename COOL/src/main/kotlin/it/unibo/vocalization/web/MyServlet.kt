package it.unibo.vocalization.web

import com.google.common.base.Optional
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import com.google.common.collect.Lists
import it.unibo.conversational.Validator
import it.unibo.conversational.algorithms.Parser
import it.unibo.conversational.database.Config
import it.unibo.conversational.database.DBmanager
import it.unibo.conversational.database.QueryGenerator
import it.unibo.conversational.datatypes.Mapping
import it.unibo.conversational.datatypes.Ngram
import it.unibo.conversational.olap.Operator
import org.apache.commons.lang3.tuple.Triple
import org.json.JSONObject
import org.slf4j.LoggerFactory
import java.io.*
import java.util.concurrent.TimeUnit
import javax.servlet.ServletException
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


/**
 * Servlet interface to COOL (COnversational OLap).
 */
@WebServlet("/Conversational")
class MainServlet : HttpServlet() {

    val sessions: LoadingCache<String, Optional<Session>> = CacheBuilder.newBuilder()
            .maximumSize(10000)
            .expireAfterWrite(2, TimeUnit.HOURS)
            .build(
                    object : CacheLoader<String?, Optional<Session>>() {
                        @Throws(Exception::class)
                        override fun load(key: String?): Optional<Session> {
                            return Optional.absent()
                        }
                    })

    private fun isEmpty(value: String?): Boolean {
        return value == null || value.isEmpty()
    }

    private fun cloneMapping(mapping: Mapping): Mapping {
        val baos = ByteArrayOutputStream()
        val oos = ObjectOutputStream(baos)
        oos.writeObject(mapping)
        oos.flush()
        oos.close()
        val `is`: InputStream = ByteArrayInputStream(baos.toByteArray())
        val `in` = ObjectInputStream(`is`)
        val prevMapping = `in`.readObject() as Mapping
        `in`.close()
        return prevMapping
    }

    private fun navigate(result: Session, value: String, sessionid: String, annotationid: String?): Session {
        return if (value.toLowerCase().replace("\\s*".toRegex(), "").equals("tellmemore", true)) {
            result
        } else if (isEmpty(annotationid)) {
            val prevTree = result.mapping!!
            val prevMapping = cloneMapping(prevTree)
            val op = Validator.parseAndTranslate(cube, Operator::class.java, prevTree, tau, log, value).getNgrams()[0] as Operator
            if (op.countAnnotatedNodesInTree() == 0) {
                op.apply(prevTree.bestNgram)
            }
            val s = Session(cube, sessionid, mapping = prevTree, prevMapping = prevMapping, operator = op)
            sessions.put(sessionid, Optional.of(s))
            s
        } else if (!isEmpty(annotationid)) { // disambiguating an OLAP operator
            if (value == "drop") { // drop the OLAP operator
                val s = Session(cube, sessionid, result.mapping)
                sessions.put(sessionid, Optional.of(s))
                s
            } else { // modify the OLAP operator
                val prevTree = result.mapping
                val op = result.operator
                op!!.disambiguate(annotationid, value, log)
                if (op.countAnnotatedNodesInTree() == 0) {
                    op.apply(prevTree!!.bestNgram)
                }
                result
            }
        } else {
            result
        }
    }

    private fun setResponse(response: HttpServletResponse) {
        response.addHeader("Access-Control-Allow-Origin", "*")
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PATCH, PUT, DELETE, OPTIONS")
        response.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, X-Auth-Token")
        response.characterEncoding = "UTF-8"
        response.status = OK
    }

    /**
     * Given a sentence returns the string representing the parsing tree.
     *
     * @param request  request
     * @param response response
     * @throws ServletException in case of error
     * @see HttpServlet.doGet
     */
    @Throws(ServletException::class)
    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        log = Lists.newArrayList() // by emptying log every time, hints are disabled
        setResponse(response)
        val sessionid = request.getParameter("sessionid")
        val annotationid = request.getParameter("annotationid")
        var value = request.getParameter("value")
        val limit = request.getParameter("limit")
        val error = JSONObject()
        error.put("sessionid", sessionid)
        error.put("annotationid", annotationid)
        error.put("value", value)
        error.put("limit", limit)
        error.put("timestamp", System.currentTimeMillis())
        L.warn(error.toString())
        error.put("error", "An error occurred")
        try {
            if (request.getParameter("describe") != null) {
                response.outputStream.print(QueryGenerator.describeLevel2JSON(cube, request.getParameter("describe"), 5).toString())
            } else {
                var result: Session?
                var parseTime = Optional.absent<Long?>()
                if (!value.isNullOrBlank() && !sessionid.isNullOrBlank()) {
                    value = value.replace("\"", "'")
                    val optionalResult: Optional<Session> = sessions[sessionid]
                    if (value == "read" || value == "reset") {
                        QueryGenerator.saveSession(cube, sessionid, null, value, null, null,  //
                                if (value == "reset") if (optionalResult.isPresent) optionalResult.get().mapping else null else null,  //
                                if (value == "reset") if (optionalResult.isPresent) optionalResult.get().operator else null else null)
                        result = Session(cube)
                        sessions.invalidate(sessionid)
                    } else {
                        result = optionalResult.orNull()
                        if (result == null) { // SESSION ID does not exist, issuing a FULL QUERY
                            val startTime = System.currentTimeMillis()
                            result = Session(cube, sessionid, mapping = Validator.parseAndTranslate(cube, value, tau, log))
                            parseTime = Optional.of(System.currentTimeMillis() - startTime)
                            sessions.put(sessionid, Optional.of(result))
                            QueryGenerator.saveQuery(cube, value, "", "", "")
                            if (result.state == Mapping.State.NAVIGATE) {
                                QueryGenerator.saveSession(cube, sessionid, null, "navigate", null, null, result.mapping, result.operator)
                            }
                        } else { // SESSION ID is not null
                            when (result.state) {
                                Mapping.State.ENGAGE -> {
                                    if (!isEmpty(annotationid)) { // disambiguating a FULL QUERY
                                        result.mapping!!.disambiguate(annotationid, value, log)
                                    }
                                    if (result.state == Mapping.State.NAVIGATE) {
                                        QueryGenerator.saveSession(cube, sessionid, null, "navigate", null, null, result.mapping, result.operator)
                                    }
                                }
                                Mapping.State.ENGAGE_HINT -> {
                                    if (!isEmpty(annotationid)) { // disambiguating a FULL QUERY
                                        result.mapping!!.disambiguate(annotationid, value, log)
                                    } else {
                                        result = navigate(result, value, sessionid, annotationid)
                                    }
                                }
                                Mapping.State.NAVIGATE -> {
                                    result = navigate(result, value, sessionid, annotationid)
                                }
                            }
                        }
                    }
                    if (value != "read" && value != "reset") {
                        QueryGenerator.saveSession(cube, sessionid, annotationid, value, "foo", limit, result.mapping, result.operator)
                    }
                    val obj = result.toJSON(value, limit)
                    if (parseTime.isPresent) {
                        obj.put("parse_time", parseTime.get())
                    }
                    response.outputStream.print(obj.toString())
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            try {
                val sw = StringWriter()
                ex.printStackTrace(PrintWriter(sw))
                error.put("error", "Did not understand the query, can you repeat please?")
                error.put("error_debug", sw.toString())
                response.outputStream.print(error.toString())
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        DBmanager.closeAllConnections()
    }

    /**
     * Given a sentence returns the string representing the parsing tree.
     *
     * @param request  request
     * @param response response
     * @throws ServletException in case of error
     */
    @Throws(ServletException::class)
    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
        doGet(request, response)
    }

    companion object {
        private val L = LoggerFactory.getLogger(MainServlet::class.java)
        private val serialVersionUID = 1L
        private val OK = 200
        private val ERROR = 500
        private var log: List<Triple<Ngram.AnnotationType, Ngram, Ngram>> = Lists.newArrayList()
        private val tau = 0.5
        private val cube = Config.getCube("sales_fact_1997")
    }

    /**
     * Instantiate the servlet.
     */
    init {
        Parser.TEST = true
        if (Parser.TEST) {
            var prevTree = Validator.parseAndTranslate(cube, "store cost", tau, log)
            prevTree.disambiguate("i0", "max", log)
            prevTree = Validator.parseAndTranslate(cube, "store cost", tau, log)
            prevTree.disambiguate("i1", "max", log)
        }
    }
}