package base.data;

import java.sql.Connection;
import java.sql.DriverManager;

public abstract class Database {

	private static Connection conn;
	private static final String DATASOURCE = "jdbc:postgresql://localhost/tania?ssl=false";
	private static final String USERNAME = "postgres";
	private static final String PASSWORD = "lucas";

	/**
	 * Singleton de conexão JDBC com o banco de dados
	 * 
	 * @return
	 */
	public static Connection getConnection() {
		try {
			if (conn != null) {
				if (!conn.isClosed()) {
					return conn;
				}
			}

			conn = DriverManager.getConnection(DATASOURCE, USERNAME, PASSWORD);

			return conn;
		} catch (Exception e) {
			throw new RuntimeException("Erro ao se conectar ao banco de dados", e);
		}
	}
}
