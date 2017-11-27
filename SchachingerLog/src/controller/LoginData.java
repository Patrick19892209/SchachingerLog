package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import model.DBConnector;

public class LoginData {
	private String user;
	private String password;
	private String full_name;
	private DBConnector dbc;
	private static Connection con;
	private Logger logger = null;

	
	public LoginData(String user, String password) {
		this.user = user;
		this.password = password;
	}
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public int getRole(String user, String password) {
		dbc = new DBConnector();
		con = dbc.openConnection();
		logger = (Logger) LoggerFactory.getLogger("model.LoginData");
		int result = 0;

		
		switch (getUserClass(user)) {
		case 1:
			if(chkUser(user, password)) result = 1;
			break;
		case 2:
			if(chkUser(user, password)) result = 2;
			break;
		case 3:
			if(chkUser(user, password)) result = 3;
			break;
		default : 
			result = -1;
		}
		
		if (!dbc.closeConnection(con)) return -2;
		return result;
	}

	private boolean chkUser(String user, String password) {
		boolean result = false;
		try {
			con.setAutoCommit(false);
			Statement stmt = null;
			ResultSet rs = null;
			try {
				String sql = "Select * From User Where abbrevation = '" + user + 
						"' and password = '" + password + "'";
				stmt = con.createStatement();
				rs = stmt.executeQuery(sql);
				if (rs.next()) {
					result = true;
					logger.info("Correct Password");
				}
			} finally {
				try {
					stmt.close();
					rs.close();
					con.commit();
				} catch (Exception ignore) {
				}
			}
		} catch (SQLException ex) {
			logger.warn("SQL ERROR");
            try {
				con.rollback();
				result = false;
			} catch (Exception e) {
				logger.warn("Rollback didnt work");
			}
     } 
		return result;
	}

	public int getUserClass(String id) {
		int result = -1;
		try {
			con.setAutoCommit(false);
			Statement stmt = null;
			ResultSet rs = null;
			
			try {
				String sql = "Select * From User Where abbrevation = '" + id + "'";
				stmt = con.createStatement();
				rs = stmt.executeQuery(sql);
				String role;
				if (rs.next()) {
					role = rs.getString("role");
					full_name = rs.getString("name");
					if (role.trim().equalsIgnoreCase("Admin")) result = 1;
					if (role.trim().equalsIgnoreCase("Lager")) result = 2;
					if (role.trim().equalsIgnoreCase("Büro")) result = 3;
				}
			} finally {
				try {
					stmt.close();
					rs.close();
					con.commit();
				} catch (Exception ignore) {
				}
			}
		} catch (SQLException ex) {
			logger.warn("SQL ERROR");
			result = -1;
            try {
				con.rollback();
			} catch (SQLException e) {
				logger.warn("Rollback not work");
			}
     } 
		return result;
	}

	public String getName() {
		return this.full_name;
	}

	
}

