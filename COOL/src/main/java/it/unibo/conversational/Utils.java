package it.unibo.conversational;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import edu.stanford.nlp.util.StringUtils;
import it.unibo.conversational.algorithms.MarriageProblem;
import it.unibo.conversational.database.Cube;
import it.unibo.conversational.database.QueryGenerator;
import it.unibo.conversational.datatypes.Entity;
import it.unibo.conversational.datatypes.Mapping;
import it.unibo.conversational.datatypes.Ngram;
import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.math.RoundingMode;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility class.
 */
public final class Utils {
    private Utils() {
    }

    /**
     * Decimal formatter.
     */
    public static final DecimalFormat DF = new DecimalFormat();

    static {
        final DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        otherSymbols.setDecimalSeparator('.');
        DF.setMinimumFractionDigits(0);
        DF.setMaximumFractionDigits(2);
        DF.setDecimalFormatSymbols(otherSymbols);
        DF.setRoundingMode(RoundingMode.HALF_DOWN);
        DF.setGroupingUsed(false);
    }

    /**
     * Data type handled by our system.
     */
    public enum DataType {
        /**
         * A number.
         */
        NUMERIC,
        /**
         * A string.
         */
        STRING,
        /**
         * A date.
         */
        DATE,
        /**
         * A MC / SC / GBC clause.
         */
        OTHER
    }

    /**
     * Convert string to DataType type.
     *
     * @param t type in string
     * @return datatype
     */
    public static DataType getDataType(final String t) {
        // mancano: BINARY, VARBINARY, LONGVARBINARY, NULL, OTHER, JAVA_OBJECT, DISTINCT, STRUCT, ARRAY,
        // BLOB, CLOB, REF, DATALINK, BOOLEAN, ROWID, NCLOB, SQLXML, REF_CURSOR, BIGINT
        if (t.equals(JDBCType.CHAR.toString()) || t.equals(JDBCType.LONGVARCHAR.toString()) || t.equals(JDBCType.VARCHAR.toString())
                || t.equals(JDBCType.NCHAR.toString()) || t.equals(JDBCType.NVARCHAR.toString()) || t.equals(JDBCType.LONGNVARCHAR.toString())) {
            return DataType.STRING;
        } else if (t.equals(JDBCType.BIT.toString()) || t.equals(JDBCType.TINYINT.toString()) || t.equals(JDBCType.SMALLINT.toString()) || t.equals(JDBCType.INTEGER.toString())
                || t.equals(JDBCType.FLOAT.toString()) || t.equals(JDBCType.REAL.toString()) || t.equals(JDBCType.DOUBLE.toString())
                || t.equals(JDBCType.NUMERIC.toString()) || t.equals(JDBCType.DECIMAL.toString())) {
            return DataType.NUMERIC;
        } else if (t.equals(JDBCType.DATE.toString()) || t.equals(JDBCType.TIME.toString()) || t.equals(JDBCType.TIMESTAMP.toString())
                || t.equals(JDBCType.TIME_WITH_TIMEZONE.toString()) || t.equals(JDBCType.TIMESTAMP_WITH_TIMEZONE.toString())) {
            return DataType.DATE;
        } else {
            // throw new IllegalArgumentException(t);
            return DataType.OTHER;
        }
    }

    /**
     * Convdert MySQL type to DataType type.
     *
     * @param t type in string
     * @return datatype
     */
    public static DataType getDataType(final JDBCType t) {
        // mancano: BINARY, VARBINARY, LONGVARBINARY, NULL, OTHER, JAVA_OBJECT, DISTINCT, STRUCT, ARRAY,
        // BLOB, CLOB, REF, DATALINK, BOOLEAN, ROWID, NCLOB, SQLXML, REF_CURSOR, BIGINT
        if (t.equals(JDBCType.CHAR) || t.equals(JDBCType.LONGVARCHAR) || t.equals(JDBCType.VARCHAR) || t.equals(JDBCType.NCHAR) || t.equals(JDBCType.NVARCHAR)
                || t.equals(JDBCType.LONGNVARCHAR)) {
            return DataType.STRING;
        } else if (t.equals(JDBCType.TINYINT) || t.equals(JDBCType.SMALLINT) || t.equals(JDBCType.INTEGER) || t.equals(JDBCType.FLOAT) || t.equals(JDBCType.REAL)
                || t.equals(JDBCType.DOUBLE) || t.equals(JDBCType.NUMERIC) || t.equals(JDBCType.DECIMAL)) {
            return DataType.NUMERIC;
        } else if (t.equals(JDBCType.DATE) || t.equals(JDBCType.TIME) || t.equals(JDBCType.TIMESTAMP) || t.equals(JDBCType.TIME_WITH_TIMEZONE)
                || t.equals(JDBCType.TIMESTAMP_WITH_TIMEZONE)) {
            return DataType.DATE;
        }
        return DataType.OTHER;
    }

    /**
     * Convert result set to json object
     *
     * @param rs result set
     * @return string of the json object
     * @throws SQLException in case of error
     */
    public static JSONObject resultSet2Json(final ResultSet rs) throws SQLException {
        final ResultSetMetaData meta = rs.getMetaData();
        final int columns = meta.getColumnCount();
        final List<String> columnNames = new ArrayList<>();
        final JSONObject obj = new JSONObject();
        for (int i = 1; i <= columns; i++) {
            columnNames.add(meta.getColumnName(i).toLowerCase());
        }
        obj.put("schema", columnNames);
        final List<List<String>> records = new ArrayList<>();
        while (rs.next()) { // convert each object to an human readable JSON object
            List<String> record = new ArrayList<>();
            for (int i = 1; i <= columns; i++) {
                // String key = columnNames.get(i - 1);
                record.add(rs.getString(i));
            }
            records.add(record);
        }
        obj.put("records", records);
        return obj;
    }

    public enum Type {
        DRILL, ROLLUP, SAD, ADD, DROP, REPLACE, ACCESSORY,
        /**
         * Measure clause.
         */
        MC,
        /**
         * Measure.
         */
        MEA,
        /**
         * Fact name.
         */
        FACT,
        /**
         * Hierarchy.
         */
        H,
        /**
         * Measure aggregation.
         */
        AGG,
        /**
         * Group by `by`.
         */
        BY,
        /**
         * Group by `where`.
         */
        WHERE,
        /**
         * Group by clause.
         */
        GC,
        /**
         * Level.
         */
        ATTR,
        /**
         * Comparison operator.
         */
        COP,
        /**
         * Selection clause.
         */
        SC,
        /**
         * Value.
         */
        VAL,
        /**
         * Between operator.
         */
        BETWEEN,
        /**
         * Logical and.
         */
        AND,
        /**
         * Logical or.
         */
        OR,
        /**
         * Logical not.
         */
        NOT,
        /**
         * `Select`.
         */
        SELECT,
        /**
         * Container for not mapped tokens.
         */
        BIN,
        /**
         * Query.
         */
        GPSJ, PARSEFOREST,
        /**
         * Count, count distinct.
         */
        COUNT,
        /**
         * Dummy container for Servlet purpose.
         */
        FOO;
    }

    public static String quote(final String s, final boolean returnOriginal) {
        if (returnOriginal) return s;
        else return quote(s);
    }

    /**
     * Double quote a string
     *
     * @param s string
     * @return double quoted string
     */
    private static String quote(final String s) {
        if (s.startsWith("\"") || s.endsWith("\"")) {
            throw new IllegalArgumentException("String already begins/ends with quotes " + s);
        }
        if (System.getProperty("os.name").startsWith("Windows")) {
            return "\"" + s + "\"";
        }
        return s;
    }

    public static String unquote(final String s, final boolean returnOriginal) {
        if (returnOriginal) return s;
        else return unquote(s);
    }

    /**
     * Remove external quotes in string
     *
     * @param s string
     * @return strign without double quotes
     */
    public static String unquote(final String s) {
        if (System.getProperty("os.name").startsWith("Windows")) {
            if (!s.startsWith("\"") || !s.endsWith("\"")) {
                throw new IllegalArgumentException("String does not begin/end with quotes " + s);
            }
            return s.substring(1, s.length() - 1);
        }
        return s;
    }

    public static String getFrom(final Cube c, final List<Entity> attributes) {
        String from = "";
        Set<String> tabIns = Sets.newHashSet();
        Pair<String, String> ftdet = QueryGenerator.getFactTable(c);
        from = " FROM " + ftdet.getRight() + " FT ";
        for (Entity mde : attributes) {
            final String idT = mde.refToOtherTable();
            if (!tabIns.contains(idT)) {
                Pair<String, String> detTab = QueryGenerator.getTabDetails(c, ftdet.getLeft(), idT);
                // TODO: note that this assume that the attributes name of pk and foreign keys are matching
                from += " INNER JOIN " + detTab.getLeft() + " ON " + detTab.getLeft() + "." + detTab.getRight() + " = FT." + detTab.getRight();
                tabIns.add(idT);
            }
        }
        return from;
    }

    private static String enrichDate(final Cube c, final String attr, final String val) {
        final boolean isDate = attr.toLowerCase().contains("month") || attr.toLowerCase().contains("year") || attr.toLowerCase().contains("date");
        return isDate ? toDate(c, attr, val) : val;
    }

    private static String jsonMeasureToString(final JSONObject mcClause, final boolean rename, final boolean ignoreQuotes) {
        final String s;
        if (mcClause.has(quote(Type.AGG.toString(), ignoreQuotes))) {
            s = unquote(mcClause.getString(quote(Type.AGG.toString(), ignoreQuotes)), ignoreQuotes) + "(" + unquote(mcClause.getString(quote(Type.MEA.toString(), ignoreQuotes)), ignoreQuotes) + ")";
        } else {
            s = unquote(mcClause.getString(quote(Type.MEA.toString(), ignoreQuotes)), ignoreQuotes);
        }
        return s + (rename && mcClause.has(quote("AS", ignoreQuotes)) ? " as " + unquote(mcClause.getString(quote("AS", ignoreQuotes)), ignoreQuotes) : "");
    }

    /**
     * JSON object to SQL query
     *
     * @param json object to convert
     * @return SQL query
     */
    public static String createQuery(final Cube c, final JSONObject json, final Boolean ignoreQuotes) {
        JSONArray mc = json.getJSONArray(quote(Type.MC.toString(), ignoreQuotes));
        final JSONArray union = new JSONArray();
        if (json.has(quote(Type.GC.toString(), ignoreQuotes))) {
            json.getJSONArray(quote(Type.GC.toString(), ignoreQuotes)).forEach(union::put);
        }
        if (json.has(quote("PROPERTIES", ignoreQuotes))) {
            json.getJSONArray(quote("PROPERTIES", ignoreQuotes)).forEach(union::put);
        }
        String select = "SELECT "; // SQL_NO_CACHE
        final Iterator<Object> mcIterator = mc.iterator();
        while (mcIterator.hasNext()) {
            JSONObject mcClause = (JSONObject) mcIterator.next();
            final String s = jsonMeasureToString(mcClause, true, ignoreQuotes);
            select += s + (mcIterator.hasNext() || !union.isEmpty() ? ", " : "");
        }

        String groupby = union.isEmpty() ? "" : " ";
        final Iterator<Object> gcIterator = union.iterator();
        final List<Entity> attributes = Lists.newArrayList();
        while (gcIterator.hasNext()) {
            final Entity attr = QueryGenerator.getLevel(c, unquote(gcIterator.next().toString(), ignoreQuotes));
            attributes.add(attr);
            groupby += attr.fullQualifier().toUpperCase() + (gcIterator.hasNext() ? ", " : "");
            select += attr.fullQualifier().toUpperCase() + (gcIterator.hasNext() ? ", " : "");
        }

        JSONArray sc = json.getJSONArray(quote(Type.SC.toString(), ignoreQuotes));
        String where = sc.isEmpty() ? "" : " WHERE ";
        final Iterator<Object> scIterator = sc.iterator();
        while (scIterator.hasNext()) {
            JSONObject scClause = (JSONObject) scIterator.next();
            final Entity attr = QueryGenerator.getLevel(c, unquote(scClause.getString(quote(Type.ATTR.toString(), ignoreQuotes)), ignoreQuotes));
            attributes.add(attr);
            final JSONArray values = scClause.getJSONArray(quote(Type.VAL.toString(), ignoreQuotes));
            final String value;
            final String attrToAdd = enrichDate(c, attr.nameInTable(), attr.fullQualifier());
            if (unquote(scClause.getString(quote(Type.COP.toString(), ignoreQuotes)).toLowerCase(), ignoreQuotes).equals("in")) {
                value = "(" + values.toList().stream().map(a -> unquote(a.toString(), ignoreQuotes)).reduce((a, b) -> a + "," + b).get() + ")";
            } else if (unquote(scClause.getString(quote(Type.COP.toString(), ignoreQuotes)).toLowerCase(), ignoreQuotes).equals("between")) {
                value = unquote(values.getString(0), ignoreQuotes) + " and " + unquote(values.getString(1), ignoreQuotes);
            } else {
                value = enrichDate(c, attr.nameInTable(), unquote(values.toList().get(0).toString(), ignoreQuotes));
            }
            where += attrToAdd + " " + unquote(scClause.getString(quote(Type.COP.toString(), ignoreQuotes)), ignoreQuotes) + " " + value + (scIterator.hasNext() ? " AND " : "");
        }
        String from = getFrom(c, attributes);
        return select
                + from
                + where
                + (groupby.isEmpty() ? "" : " GROUP BY" + groupby + (json.has(quote("HAVING", ignoreQuotes)) ? " HAVING " + Utils.unquote(json.getString(quote("HAVING", ignoreQuotes))) : "") + " ORDER BY" + groupby);
    }

    public static String toDate(final Cube cube, final String attribute, final String date) {
        final String newDate;
        switch (cube.getDbms()) {
            case "mysql":
                if (attribute.toLowerCase().contains("date")) {
                    newDate = date;
                } else if (attribute.toLowerCase().contains("month")) {
                    newDate = "STR_TO_DATE(concat(" + date + ",'-01'),'%Y-%m-%d')";
                } else {
                    newDate = "STR_TO_DATE(concat(\" + date + \",'-01-01'),'%Y-%m-%d')";
                }
                return newDate;
            case "oracle":
                if (attribute.toLowerCase().contains("date")) {
                    newDate = "TO_DATE(" + date + ",\"YYYY-MM-DD\")";
                } else if (attribute.toLowerCase().contains("month")) {
                    newDate = "TO_DATE(" + date + ",\"YYYY-MM\")";
                } else {
                    newDate = "TO_DATE(" + date + ",\"YYYY\")";
                }
                return newDate;
            default:
                throw new IllegalArgumentException(cube.getDbms() + " is not handled");
        }
    }

    public static String toInterval(final Cube cube, final String attribute, final String date, final int time) {
        switch (cube.getDbms()) {
            case "mysql":
                return "date_sub(" + toDate(cube, attribute, date) + ", INTERVAL " + time + " " + (attribute.contains("month") ? "MONTH" : attribute.contains("year") ? "YEAR" : "DAY") + ")";
            case "oracle":
                if (attribute.toLowerCase().contains("date")) {
                    return toDate(cube, attribute, date) + " - " + time;
                } else if (attribute.toLowerCase().contains("month")) {
                    return toDate(cube, attribute, date) + " - interval '" + time + "' MONTH";
                } else {
                    return toDate(cube, attribute, date) + " - interval '" + time + "' YEAR";
                }
            default:
                throw new IllegalArgumentException(cube.getDbms() + " is not handled");
        }
    }

    public static String toIf(final Cube cube, final String c, final String t) {
        switch (cube.getDbms()) {
            case "mysql":
                return "if(" + c + "," + t + ",null)";
            case "oracle":
                return "case when " + c + " then " + t + " else null end";
            default:
                throw new IllegalArgumentException(cube.getDbms() + " is not handled");
        }
    }

    /**
     * Word similarity based on levhenstein distance.
     *
     * @param first  a word
     * @param second another word
     * @return similarity
     */
    public static double tokenSimilarity(final String first, final String second) {
        double maxl = Math.max(first.length(), second.length());
        return 1 - (StringUtils.levenshteinDistance(first, second) / maxl);
    }

    /**
     * Similarity between two lists of tokens.
     *
     * @param aTokens a list
     * @param bTokens another list
     * @return similarity score
     */
    public static double tokenSimilarity(final String[] aTokens, final String[] bTokens) {
        return tokenSimilarity(Arrays.asList(aTokens), Arrays.asList(bTokens));
    }

    /**
     * Similarity between two lists of tokens.
     *
     * @param aTokens a list
     * @param bTokens another list
     * @return similarity score
     */
    public static double tokenSimilarity(final List<String> aTokens, final List<String> bTokens) {
        // La similarità tra due termini viene calcolando sommando la similarità di ogni coppia di parole associate moltiplicata per la lunghezza massima delle due parole
        // tutto fratto la lunghezza totale delle parole non matchate più la somma delle lunghezze massime
        final Map<Integer, Integer> matching = MarriageProblem.getBestMatch(aTokens, bTokens);
        double weightedMatch = 0.0, sumLen = 0.0;
        int unmatched = 0;
        for (final Map.Entry<Integer, Integer> e : matching.entrySet()) {
            String s1 = aTokens.get(e.getKey());
            String s2 = bTokens.get(e.getValue());
            final int maxl = Math.max(s1.length(), s2.length());
            weightedMatch += (tokenSimilarity(s1, s2) * maxl);
            sumLen += maxl;
        }
        for (int i = 0; i < aTokens.size() && aTokens.size() > bTokens.size(); i++) {
            if (!matching.containsKey(i)) {
                unmatched += aTokens.get(i).length();
            }
        }
        for (int i = 0; i < bTokens.size() && aTokens.size() < bTokens.size(); i++) {
            if (!matching.containsValue(i)) {
                unmatched += bTokens.get(i).length();
            }
        }
        return weightedMatch / (sumLen + unmatched);
    }

    /**
     * Write ngrams and mappings on file.
     *
     * @param folderName folder
     * @param fileName   file
     * @param ngrams     ngrams
     */
    public static void writeMappings(final String folderName, final String fileName, final List<Ngram> ngrams) {
        try {
            final String filePath = "resources/test/" + folderName + "/" + fileName.replace("?", "") + ".csv";
            final FileWriter csvWriter = new FileWriter(filePath);
            csvWriter.append("N-gram");
            csvWriter.append(";");
            csvWriter.append("Pos");
            csvWriter.append(";");
            csvWriter.append("Tabella");
            csvWriter.append(";");
            csvWriter.append("Similarità");
            csvWriter.append(";");
            csvWriter.append("\n");
            for (final Ngram res : ngrams) {
                csvWriter.append(res.tokens);
                csvWriter.append(";");
                csvWriter.append("[" + res.pos().getLeft() + "-" + res.pos().getRight() + "]");
                csvWriter.append(";");
                csvWriter.append(res.type + ": " + res.mde().nameInTable() + ": " + res.typeInDB());
                csvWriter.append(";");
                csvWriter.append(String.valueOf(res.similarity()));
                csvWriter.append("\n");
            }
            csvWriter.flush();
            csvWriter.close();
        } catch (IOException e) {
            // e.printStackTrace();
        }
    }

    /**
     * Write sentence on file.
     *
     * @param folderName folder
     * @param fileName   file
     * @param sentences  sentences
     */
    public static void writeParsing(final String folderName, final String fileName, final List<Mapping> sentences) {
        final Locale currentLocale = Locale.getDefault();
        final DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(currentLocale);
        otherSymbols.setDecimalSeparator(',');
        final DecimalFormat df = new DecimalFormat("#.########", otherSymbols);
        df.setRoundingMode(RoundingMode.HALF_DOWN);
        try {
            final String filePath = "resources/test/" + folderName + "/" + fileName.replace("?", "") + ".csv";
            final FileWriter csvWriter = new FileWriter(filePath);
            for (final Mapping set : sentences.stream().sorted((Mapping s1, Mapping s2) -> {
                final int s1f = s1.ngrams.stream().anyMatch(n -> n.type.equals(Type.GPSJ)) ? 1 : 0;
                final int s2f = s2.ngrams.stream().anyMatch(n -> n.type.equals(Type.GPSJ)) ? 1 : 0;
                final int c = -Integer.compare(s1f, s2f);
                if (c == 0) {
                    return -Integer.compare(s1.ngrams.size(), s2.ngrams.size());
                }
                return c;
            }).collect(Collectors.toList())) {
                csvWriter.append(set.toString());
                csvWriter.append("\n");
            }
            csvWriter.flush();
            csvWriter.close();
        } catch (final IOException e) {
            // e.printStackTrace();
        }
    }

    public static String ngram2string(final List<String> ngram) {
        return String.join(" ", ngram).toLowerCase();
    }

    public static List<String> string2ngram(final String token) {
        final List<String> res =
                Arrays.stream(
                        token
                                .toLowerCase()
                                .replace("the_", "")
                                .split(" |_")
                )
                        .filter(t -> !t.isEmpty())
                        .collect(Collectors.toList());
        return res;
    }
}
