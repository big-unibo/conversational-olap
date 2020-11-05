package it.unibo.describe.web;

import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.AgeFileFilter;
import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONObject;

import com.google.common.collect.Maps;

import it.unibo.conversational.Utils;
import it.unibo.conversational.database.DBmanager;
import it.unibo.describe.Describe;
import it.unibo.describe.DescribeExecute;

/**
 * Servlet interface Describe operator.
 */
@WebServlet("/Describe")
public class DescribeServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;
  private static final int OK = 200;
  private static final int ERROR = 500;

  final Map<String, Describe> sessions = Maps.newLinkedHashMap();
  final static String PYTHON_PATH = Utils.credentialsFromFile()[7];
  private boolean emptySession(final String sessionid) {
    return sessionid == null || sessionid.isEmpty();
  }

  private boolean emptyValue(final String value) {
    return value == null || value.isEmpty();
  }

  private int id = 0;

  public synchronized int getId() {
    return id++;
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
    // System.out.println(PYTHON_PATH);
    response.addHeader("Access-Control-Allow-Origin", "*");
    response.addHeader("Access-Control-Allow-Methods", "GET, POST, PATCH, PUT, DELETE, OPTIONS");
    response.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, X-Auth-Token");
    response.setCharacterEncoding("UTF-8");
    final JSONObject error = new JSONObject();
    try {
      /* **********************************************************************
       * CLEAN OLD .CSV FILES
       ********************************************************************** */
      final Date oldestDate = DateUtils.addMinutes(new Date(), -30); // Remove file older than 30minutes
      final File targetDir = new File(getServletContext().getRealPath("WEB-INF/classes"));
      final Iterator<File> filesToDelete = FileUtils.iterateFiles(targetDir, new AgeFileFilter(oldestDate), null);
      while (filesToDelete.hasNext()) {
        final File toDelete = filesToDelete.next();
        if (toDelete.getName().endsWith(".csv")) {
          FileUtils.deleteQuietly(toDelete);
        }
      }

      final String sessionid = request.getParameter("sessionid");
      final String value;
      if (DBmanager.metaDb.equals("conversational")) {
        value = (request.getParameter("value").toString() + " ")//
            .replace(" customer ", " customer_id ")//
            .replace(" product ", " product_id ")//
            .replace(" date ", " the_date ")//
            .replace(" month ", " the_month ")//
            .replace(" year ", " the_year ")//
            .replace(" city ", " store_city ")//
            .replace(" country ", " store_country ")//
            .replace(" category ", " product_category ")//
            .replace(" type ", " product_subcategory ")//
            .replace(" quantity ", " unit_sales ")//
            .replace(" storeSales ", " store_sales ")//
            .replace(" storeCost ", " store_cost ")//
            .replace(" store ", " store_id ");
        error.put("sessionid", sessionid);
      } else {
        value = request.getParameter("value").toString();
      }
      error.put("value", value);

      final int status;
      final JSONObject result;

      if (!emptyValue(value)) {
        status = OK;
        final Describe d = DescribeExecute.parse(sessions.get(sessionid), value);
        result = DescribeExecute.execute(d, getServletContext().getRealPath("WEB-INF/classes/"), PYTHON_PATH);
        sessions.put(emptySession(sessionid) ? getId() + "" : sessionid, d);
      } else {
        status = ERROR;
        result = null;
        error.put("error", "Empty string");
      }
      response.setStatus(OK);
      response.getOutputStream().print(status == OK //
          ? result.toString()//
              .replace("customer_id", "customer")//
              .replace("product_id", "product")//
              .replace("store_id", "store")//
              .replace("the_date", "date")//
              .replace("the_month", "month")//
              .replace("the_year", "year")//
              .replace("store_city", "city")//
              .replace("store_country", "country")//
              .replace("product_subcategory", "type")//
              .replace("product_category", "category")//
              .replace("unit_sales", "quantity")//
              .replace("store_sales", "storeSales")//
              .replace("store_cost", "storeCost")//
          : error.toString());
    } catch (final Exception ex) {
      ex.printStackTrace();
      response.setStatus(OK);
      try {
        error.put("error", ex.getLocalizedMessage());
        response.getOutputStream().print(error.toString());
      } catch (final Exception e) {
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
