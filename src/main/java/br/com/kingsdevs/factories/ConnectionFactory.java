package br.com.kingsdevs.factories;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactory {

	public static Connection getConnection() throws Exception {		
	
		String url = "jdbc:postgresql://localhost:5432/bdusuarios";		
		String user = "postgres";
		String password = "reis";
		String driver = "org.postgresql.Driver";
		
		Class.forName(driver);
		
		return DriverManager.getConnection(url, user, password);
		
		}
	
}
