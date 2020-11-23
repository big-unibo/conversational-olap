package it.unibo.conversational.database

import kotlinx.cli.*
import java.text.Format
import java.sql.*;
import java.util.stream.IntStream

fun getConnString(dbms: String, ip: String, port: Int, db: String): String {
    return when (dbms) {
        "mysql" -> "jdbc:mysql://$ip:$port/$db?serverTimezone=UTC&autoReconnect=true"
        "oracle" -> "jdbc:oracle:thin:@$ip:$port/$db"
        else -> "";
    }
}

fun main(args: Array<String>) {
    val parser = ArgParser("Migrating data")
    val iip by parser.option(ArgType.String, shortName = "iip").default("137.204.74.2")
    val oip by parser.option(ArgType.String, shortName = "oip").default("137.204.74.10")
    val iport by parser.option(ArgType.Int, shortName = "iport").default(3307)
    val oport by parser.option(ArgType.Int, shortName = "oport").default(1521)
    val idbms by parser.option(ArgType.String, shortName = "idbms").default("mysql")
    val odbms by parser.option(ArgType.String, shortName = "odbms").default("oracle")
    val idb by parser.option(ArgType.String, shortName = "idb").default("conversational")
    val odb by parser.option(ArgType.String, shortName = "odb").default("research")
    val iuser by parser.option(ArgType.String, shortName = "iuser").default("mfrancia")
    val ouser by parser.option(ArgType.String, shortName = "ouser").default("foodmart")
    val ipwd by parser.option(ArgType.String, shortName = "ipwd").default("mfrancia")
    val opwd by parser.option(ArgType.String, shortName = "opwd").default("foodmart")
    var tables by parser.argument(ArgType.String, description = "tables").vararg()
    parser.parse(args)

    // Check that all the drivers exist
    Class.forName("com.mysql.cj.jdbc.Driver")
    Class.forName("oracle.jdbc.driver.OracleDriver")

    // Set incoming/outgoing connections
    val iurl = getConnString(idbms, iip, iport, idb)
    val ourl = getConnString(odbms, oip, oport, odb)
    val iconn = DriverManager.getConnection(iurl, iuser, ipwd);
    val oconn = DriverManager.getConnection(ourl, ouser, opwd);

    tables.forEach { table ->
        val select = "SELECT * FROM $table";
        val ist = iconn.createStatement();
        val rs = ist.executeQuery(select);

        val rsmd: ResultSetMetaData = rs.getMetaData()

        val cols = rsmd.columnCount
        val outquery = "INSERT INTO $table VALUES (${(1..cols).map { "?" }.reduce { a, b -> "$a,$b" }})"
        val ost = oconn.prepareStatement(outquery);

        while (rs.next()) {
            (1..cols).forEach { ost.setObject(it, rs.getObject(it)) }
            ost.addBatch()
        }
        ost.executeBatch()

        ost.close()
        ist.close();
    }
}