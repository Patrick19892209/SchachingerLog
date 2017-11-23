package sqldata;

import java.sql.*;

public class TestData {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	//static final String DB_IP = "140.78.42.62:3306";
	static final String DB_IP = "127.0.0.1:3333";
	// static final String SCHEME = "schachinger";
	static final String SCHEME = "schachinger_log";
	static final String DB_URL = "jdbc:mysql://" + DB_IP + "/" + SCHEME;

	// Database credentials
	static final String USER = "root";
	static final String PASS = "wkA7*ucE#wY#";
	//static final String PASS = "5pvNmKg0UeArOwH0" + "   ";
	public static void main(String[] args) {
		Connection conn = null;
		Statement stmt = null;
		try {
			// STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			System.out.println("Connecting to " + DB_URL);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			System.out.println("Connected database successfully...");

			// STEP 4: Execute a query
			System.out.println("Creating statement...");
			stmt = conn.createStatement();

			//String sql = "SELECT id, first, last, age FROM Registration";
			
			//Userdaten
			String user = "DP";
			//--Ende Userdaten
			
			//Rekladaten
			int artNr=13371337;
			String reason="Artikel kaputt";
			String aviso="A000037182";
			String actorA="DP";
			String actorB="EL";
			int amount=2;
			//--Ende Rekladaten
			
			//stmt.executeUpdate(insertRekla(artNr,reason,aviso,actorA,actorB,amount));
			ResultSet rs= stmt.executeQuery(getUser(user));
			// STEP 5: Extract data from result set
			
			while (rs.next()) {
				// Retrieve by column name
				printUser(rs);
			}
			rs.close();
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
				System.out.println("SQL failure!");
			} // do nothing
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			} // end finally try
		} // end try
		System.out.println("Goodbye!");
	}// end main

			
	private static String getUser(String user) {
		return "SELECT password, location FROM User WHERE abbrevation='" + user + "'";
	}
	private static void printUser(ResultSet rs) {
		try {
			System.out.println(rs.getString("password"));
			System.out.println(rs.getString("location"));
		}
		catch(SQLException se) {
			
		}
	}
	private static String insertRekla(int artnr, String reason, String aviso, String actorA, String actorB, int amount) {
		return "INSERT INTO Reklamation (Id, Aviso, Timestamp, Erfasser, ZugewiesenAn, ArtikelNr, Menge) VALUES ('12', 'A000037171', '2017-11-08 00:00:00', 'BM', 'DP', '1231', '1')";
	}
	private static void printRekla(ResultSet reklaRs) {
		try {
			System.out.println(reklaRs.getString("Id"));
			System.out.println(reklaRs.getString("ArtikelNr"));
			System.out.println(reklaRs.getString("Erfasser"));
		}
		catch(SQLException se){
		}
		}
}// end JDBCExample



