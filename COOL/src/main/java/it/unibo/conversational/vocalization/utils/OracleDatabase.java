package it.unibo.conversational.vocalization.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import oracle.jdbc.OracleDriver;

public class OracleDatabase {

	private static final String URL_PREFIX = "jdbc:oracle:thin:@";
	private final Statement statement;

	public OracleDatabase(Statement statement) {
		this.statement = statement;
	}

	public static OracleDatabase connect(String host, String port, String sid, String user, String pw) throws Exception {
		String url = URL_PREFIX + host + ":" + port + ":" + sid;
		DriverManager.registerDriver(new OracleDriver());
		Connection connection = DriverManager.getConnection(url, user, pw);
		return new OracleDatabase(connection.createStatement());
	}

	public ResultSet query(String sql) throws Exception {
		return this.statement.executeQuery(sql);
	}

}
