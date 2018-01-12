package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import model.DBConnector;
import model.User;

public class LoginData {
	private DBConnector dbc;
	private static Connection con;
	private Logger logger = null;


	public int getRole(User user) {
		dbc = new DBConnector();
		con = dbc.openConnection();
		logger = (Logger) LoggerFactory.getLogger("model.LoginData");
		int result = 0;

		result = chkUser(user);
		dbc.closeConnection(con);
		return result;
	}

	private int chkUser(User user) {
		int result = 0;
		String role ;
		Statement stmt = null;
		ResultSet rs = null;
			try {
				String sql = "Select * From User Where abbrevation = '" + user.getAbbrevation() + 
						"' and password = '" + user.getPassword() + "'";
				if(con==null) return -1;
				stmt = con.createStatement();
				rs = stmt.executeQuery(sql);
				if (rs.next()) {
					logger.info("Correct Password");
					role = rs.getString("role");
					user.setRole(role);
					user.setName(rs.getString("name"));
					user.setLocation(rs.getString("location"));
					if (role.trim().equalsIgnoreCase("Admin")) result = 1;
					if (role.trim().equalsIgnoreCase("Lager")) result = 2;
					if (role.trim().equalsIgnoreCase("Buero")) result = 3;
				}
				stmt.close();
				rs.close();
				con.close();
			}catch(SQLException se){
				      //Handle errors for JDBC
				      se.printStackTrace();
				   }catch(Exception e){
				      //Handle errors for Class.forName
				      e.printStackTrace();
			} finally {
				try {
				if (stmt != null) 
					stmt.close();
				}catch(SQLException e) {
				}
				try {
				if (con != null) 
					con.close();
				
				} catch (SQLException ignore) {
				}
			}
		
		return result;
	}

}



