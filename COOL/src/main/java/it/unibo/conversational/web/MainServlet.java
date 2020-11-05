package it.unibo.conversational.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.tuple.Triple;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;

import it.unibo.conversational.Validator;
import it.unibo.conversational.algorithms.Parser;
import it.unibo.conversational.database.DBmanager;
import it.unibo.conversational.database.QueryGeneratorChecker;
import it.unibo.conversational.datatypes.Mapping;
import it.unibo.conversational.datatypes.Mapping.State;
import it.unibo.conversational.datatypes.Ngram;
import it.unibo.conversational.datatypes.Ngram.AnnotationType;
import it.unibo.conversational.olap.Operator;

/**
 * Servlet interface to COOL (COnversational OLap).
 */
@WebServlet("/Conversational")
public class MainServlet extends HttpServlet {
  private static final Logger L = LoggerFactory.getLogger(MainServlet.class);
  private static final long serialVersionUID = 1L;
  private static final int OK = 200;
  private static final int ERROR = 500;
  private static List<Triple<AnnotationType, Ngram, Ngram>> log = Lists.newArrayList();
  private static final double tau = 0.5;

  public class Session {
    public Session(final String uuid, final Mapping mapping, final Operator operator) {
      this.uuid = uuid == null ? (Parser.TEST ? "s0" : UUID.randomUUID().toString()) : uuid;
      this.mapping = mapping;
      this.operator = operator;
    }
    public Session(final String uuid, final Mapping mapping) {
      this (uuid, mapping, null);
    }
    public Session() {
      this(null, null, null);
    }
    public State getState() {
      return mapping.getState();
    }
    public JSONObject toJSON(final String value, final String limit) throws Exception {
      final JSONObject ret = new JSONObject();
      if (mapping != null) {
        ret.put("parseforest", mapping == null ? new JSONObject() : mapping.JSONobj(value, limit == null ? Optional.absent() : Optional.of(Long.parseLong(limit))));
        ret.put("operatorforest", operator == null || operator.countAnnotatedNodesInTree() == 0 ? new JSONObject() : operator.toJSON());
        ret.put("state", getState());
        ret.put("sessionid", uuid);
      }
      return ret;
    }
    public final Mapping mapping;
    public final Operator operator;
    public final String uuid;
  }

  /**
   * Instantiate the servlet.
   * @throws Exception in case of error
   */
  public MainServlet() throws Exception {
    Parser.TEST = true;
    if (Parser.TEST) {
      Mapping prevTree = Validator.parseAndTranslate("store cost", tau, log);
      prevTree.disambiguate("i0", "max", log);
      prevTree = Validator.parseAndTranslate("store cost", tau, log);
      prevTree.disambiguate("i1", "max", log);
    }
  }

  final LoadingCache<String, Optional<Session>> sessions = CacheBuilder.newBuilder()
      .maximumSize(10000)
      .expireAfterWrite(2, TimeUnit.HOURS)
      .build(
          new CacheLoader<String, Optional<Session>>() {
            @Override
            public Optional<Session> load(String key) throws Exception {
              return Optional.absent();
            }
          });
 
  private boolean isEmpty(final String value) {
    return value == null || value.isEmpty();
  }

  /**
   * Given a sentence returns the string representing the parsing tree.
   * 
   * @param request
   *          request
   * @param response
   *          response
   * @throws ServletException
   *           in case of error
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
    log = Lists.newArrayList(); // by emptying log every time, hints are disabled
    response.addHeader("Access-Control-Allow-Origin", "*");
    response.addHeader("Access-Control-Allow-Methods", "GET, POST, PATCH, PUT, DELETE, OPTIONS");
    response.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, X-Auth-Token");
    response.setCharacterEncoding("UTF-8");
    response.setStatus(OK);
    final String sessionid = request.getParameter("sessionid");
    final String annotationid = request.getParameter("annotationid");
    String value = request.getParameter("value");
    final String valueIta = request.getParameter("valueIta");
    final String limit = request.getParameter("limit");

    final JSONObject error = new JSONObject();
    error.put("sessionid", sessionid);
    error.put("annotationid", annotationid);
    error.put("value", value);
    error.put("valueIta", valueIta);
    error.put("limit", limit);
    error.put("timestamp", System.currentTimeMillis());
    L.warn(error.toString());
    error.put("error", "An error occurred");
    try {
      if (request.getParameter("describe") != null) {
        response.getOutputStream().print(QueryGeneratorChecker.describeLevel2JSON(request.getParameter("describe"), 5).toString());
      } else {
        
        Session result = null;
        Optional<Long> parseTime = Optional.absent();
        int status = OK;
        if (isEmpty(value) || isEmpty(sessionid)) {
          status = ERROR;
        } else {
          value = value.replace("\"", "'");
          Optional<Session> optionalResult = sessions.get(sessionid);
          if (value.equals("read") || value.equals("reset")) {
            QueryGeneratorChecker.saveSession(sessionid, null, value, null, null, //
                value.equals("reset") ? (optionalResult.isPresent() ? optionalResult.get().mapping : null) : null, //
                value.equals("reset") ? (optionalResult.isPresent() ? optionalResult.get().operator : null) : null);
            result = new Session();
            sessions.invalidate(sessionid);
          } else {
            result = optionalResult.orNull();
            if (result == null) { // SESSION ID does not exist, issuing a FULL QUERY
              final long startTime = System.currentTimeMillis();
              result = new Session(sessionid, Validator.parseAndTranslate(value, tau, log));
              parseTime = Optional.of(System.currentTimeMillis() - startTime);
              sessions.put(sessionid, Optional.of(result));
              QueryGeneratorChecker.saveQuery(value, "", "", "");
              if (result.getState().equals(State.NAVIGATE)) {
                QueryGeneratorChecker.saveSession(sessionid, null, "navigate", null, null, result.mapping, result.operator);
              }
            } else { // SESSION ID is not null
              switch (result.getState()) {
              case ENGAGE:
                if (!isEmpty(annotationid)) { // disambiguating a FULL QUERY
                  result.mapping.disambiguate(annotationid, value, log);
                }
                if (result.getState().equals(State.NAVIGATE)) {
                  QueryGeneratorChecker.saveSession(sessionid, null, "navigate", null, null, result.mapping, result.operator);
                }
                break;
              case ENGAGE_HINT:
                if (!isEmpty(annotationid)) { // disambiguating a FULL QUERY
                  result.mapping.disambiguate(annotationid, value, log);
                  break;
                }
              case NAVIGATE:
                if (isEmpty(annotationid)) { // issuing an OLAP OPERATOR
                  final Mapping prevTree = result.mapping;
                  final Operator op = (Operator) Validator.parseAndTranslate(Operator.class, prevTree, tau, log, value).getNgrams().get(0);
                  if (op.countAnnotatedNodesInTree() == 0) {
                    op.apply(prevTree.bestNgram);
                  }
                  result = new Session(sessionid, prevTree, op);
                  sessions.put(sessionid, Optional.of(result));
                } else if (!isEmpty(annotationid)) { // disambiguating an OLAP OPERATOR
                  if (value.equals("drop")) { // DROP dell'OLAP operator, non è da disambiguare
                    result = new Session(sessionid, result.mapping);
                    sessions.put(sessionid, Optional.of(result));
                  } else {
                    final Mapping prevTree = result.mapping;
                    final Operator op = result.operator;
                    op.disambiguate(annotationid, value, log);
                    if (op.countAnnotatedNodesInTree() == 0) {
                      op.apply(prevTree.bestNgram);
                    }
                  }
                }
              }
            }
          }

          if (!value.equals("read") && !value.equals("reset")) {
            QueryGeneratorChecker.saveSession(sessionid, annotationid, value, valueIta, limit, result.mapping, result.operator);
          }

          if (status == OK) {
            final JSONObject obj = result.toJSON(value, limit);
            if (parseTime.isPresent()) {
              obj.put("parse_time", parseTime.get());
            }
            response.getOutputStream().print(obj.toString());
          } else {
            response.getOutputStream().print(error.toString());
          }
        }
      }
    } catch (final Exception ex) {
      ex.printStackTrace();
      try {
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        error.put("error", "Did not understand the query, can you repeat please?");
        error.put("error_debug", sw.toString());
        response.getOutputStream().print(error.toString());
      } catch (final IOException e) {
        e.printStackTrace();
      }
    } finally {
      try {
        DBmanager.getMetaConnection().close();
        DBmanager.getDataConnection().close();
      } catch (final Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Given a sentence returns the string representing the parsing tree.
   * 
   * @param request
   *          request
   * @param response
   *          response
   * @throws ServletException
   *           in case of error
   */
  @Override
  protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
    doGet(request, response);
  }
}
