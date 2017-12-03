package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import model.DBConnector;

public class Data {

	protected DBConnector dbc;
	protected Connection con;
	protected Logger logger = null;
	protected ResultSet rs = null;

	public Data() {
		this.dbc = new DBConnector();
		//this.con = dbc.openConnection();
	}

	public Data(String logname) {
		this.logger = (Logger) LoggerFactory.getLogger("model." + logname);
		this.dbc = new DBConnector();
		//this.con = dbc.openConnection();
	}

	public ResultSet executeQuery(String query) {
		try {
			this.con = dbc.openConnection();
			this.con.setAutoCommit(false);
			Statement stmt = null;
			try {
				stmt = con.createStatement();
				rs = stmt.executeQuery(query); 
			} finally {
				try {
					stmt.close();
					con.commit();
					con.close();
					return rs;	
				} catch (Exception ignore) {
				}
			}
		} catch (SQLException ex) {
			logger.warn("SQL ERROR: " + ex);
            try {
				con.rollback();
			} catch (Exception e) {
				logger.warn("Rollback didnt work");
				}
			} 
		logger.info(query + "erfolgreich durchgeführt");
		return null;
	}
	
	public boolean executeUpdate(String query) {
		try {
			this.con.setAutoCommit(false);
			Statement stmt = null;
			try {
				System.out.println("63");
				stmt = con.createStatement();
				System.out.println("65");
				stmt.executeUpdate(query);
				
			} finally {
				try {
					stmt.close();
					con.commit();
				} catch (Exception ignore) {
				}
			}
		} catch (SQLException ex) {
			logger.warn("SQL ERROR: " + ex);
            try {
				con.rollback();
			} catch (Exception e) {
				logger.warn("Rollback didnt work");
				return false;
			}
            
		} 
		logger.info(query + "erfolgreich durchgeführt");
		return true;
	}

	// führt eine SQL-Abfrage durch type=q --> query sonst update
	public boolean execute(char type, String query) {
		boolean bool = false;
		try {
			this.con.setAutoCommit(false);
			Statement stmt = null;
			try {
				stmt = con.createStatement();
				if(type=='q'){ 
					stmt.executeQuery(query);
				}
				else {
					stmt.executeUpdate(query);
				}
				
				bool = true;
			} finally {
				try {
					stmt.close();
					con.commit();
				} catch (Exception ignore) {
				}
			}
		} catch (SQLException ex) {
			logger.warn("SQL ERROR: " + ex);
			try {
				con.rollback();
			} catch (Exception e) {
				logger.warn("Rollback didnt work");
			}
			return bool;
		}
		logger.info(query + "erfolgreich durchgeführt");
		return bool;
	}

	public DBConnector getDbc() {
		return dbc;
	}

	public void setDbc(DBConnector dbc) {
		this.dbc = dbc;
	}

	public Connection getCon() {
		return con;
	}

	public void setCon(Connection con) {
		this.con = con;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

}
