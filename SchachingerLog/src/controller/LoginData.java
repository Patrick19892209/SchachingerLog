package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
		if (!dbc.closeConnection(con)) return -2;
		return result;
	}

	private int chkUser(User user) {
		int result = 0;
		String role ;
		try {
			con.setAutoCommit(false);
			Statement stmt = null;
			ResultSet rs = null;
			try {
				String sql = "Select * From User Where abbrevation = '" + user.getAbbrevation() + 
						"' and password = '" + user.getPassword() + "'";
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
            try {
				con.rollback();

			} catch (Exception e) {
				logger.warn("Rollback didnt work");
			}
     } 
		return result;
	}

	public boolean loadCSV() throws Exception {
		dbc = new DBConnector();
		con = dbc.openConnection();
		boolean result = true;
		try {
            con.setAutoCommit(false);
                        
            Statement stmt = null;
            ResultSet rs = null;
            String columns = null, table = null;
            		columns = "(aviso, delivery_nr,"
                		+ " @date, nr_of_items, supplier"
                		+ " ) "
                		+ "SET date = STR_TO_DATE(@date,'%Y%m%d')";
                	table = "Delivery";
             try {
            	 
            	String loadQuery = "LOAD DATA LOCAL INFILE '" + "C:/Users/bauer/Downloads/Aviso_METRO.CSV" + 
            			"' INTO TABLE " + table + " FIELDS TERMINATED BY ';'" + 
            			" LINES TERMINATED BY '\n' IGNORE 1 LINES " + columns;
            	stmt = con.createStatement();
            	System.out.println(loadQuery);
            	rs = stmt.executeQuery(loadQuery);
                try {
                    rs.close();
                } catch (Exception ignore) {
                }
                  
            } finally {
                try {
                    stmt.close();
                    con.commit();
                } catch (Exception ignore) {
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error executing transactions ..." + ex.getMessage());
            try {
                con.rollback();
                result = false;
            } catch (Exception ignore) {
                throw ignore;
            }
            throw ex;
        }
		return result;
    }

	
}



