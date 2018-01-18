package controller;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import model.DBConnector;

public class Data {

	protected DBConnector dbc;
	protected Connection con;
	protected Logger logger;
	protected ResultSet rs;

	public Data() {
		this.dbc = new DBConnector();
		this.con = null;
		this.rs = null;
		this.logger = (Logger) LoggerFactory.getLogger("controller.noName");;
		//this.con = dbc.openConnection();
	}

	public Data(String logname) {
		this.logger = (Logger) LoggerFactory.getLogger(logname);
		this.dbc = new DBConnector();
		//this.con = dbc.openConnection();
	}
	
	public boolean update(String query) {
		boolean bool = false;
		logger.info(query);
		try {
			this.con = this.dbc.openConnection();
			this.con.setAutoCommit(false);
			Statement stmt = null;
			try {
				stmt = this.con.createStatement();
				stmt.executeUpdate(query);
				bool = true;
				logger.info(query + " erfolgreich durchgeführt");
			} finally {
				try {
					stmt.close();
					this.con.commit();
					this.con.close();
				} catch (Exception ignore) {
					//logger.warn("Connection couldn't be closed successfully");
				}
			}
		} catch (SQLException ex) {
			logger.warn("SQL ERROR: " + ex);
            try {
				this.con.rollback();
			} catch (Exception e) {
				//logger.warn("Rollback didnt work");
				}
			} 
		
		return bool;
	}
	
	public boolean exists(String query) {
		boolean bool = false;
		try {
			con = this.dbc.openConnection();
			con.setAutoCommit(false);
			Statement stmt = null;
			try {
				stmt = con.createStatement();
				this.rs = stmt.executeQuery(query);
				if(this.rs.next()) bool=true;
				//logger.info(query + " erfolgreich durchgeführt - Bool: " + bool);
			} finally {
				try {
					stmt.close();
					con.commit();
					this.dbc.closeConnection(con);
					
				} catch (Exception ignore) {
				}
			}
		} catch (SQLException ex) {
			//logger.warn("SQL ERROR: " + ex);
            try {
				con.rollback();
			} catch (Exception e) {
				//logger.warn("Rollback didnt work");
				}
			} 
		catch(NullPointerException n) {
			System.out.println("BUT WHY?!");
		}
		return bool;
	}

	//converts Timestamp to String e.g. 2017-12-24
	public String convertTsToDate(Timestamp ts) {
		Date date = new Date(ts.getTime());
  	  	SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
  	  	return format1.format(date);	
	}
	
	public String convertTime(LocalTime time) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.GERMANY);
		return formatter.format(time);
	}
	
	//Maximale Id von Table - Achtung nur möglich wenn Spalte der Id "id" heißt
	public int getMaxId(String query) {
		int maxId = -1;		
		//if(!query.contains("id")) return -1;
		try {
			con = dbc.openConnection();
			con.setAutoCommit(false);
			Statement stmt = null;
			try {
				stmt = con.createStatement();
				this.rs = stmt.executeQuery(query);
				if (rs.next()) maxId = this.rs.getInt(1);
				if(maxId<0) maxId=-1;
				//logger.info("MaxId: " + maxId);
			} finally {
				try {
					stmt.close();
					con.commit();
					con.close();
				} catch (Exception ignore) {
					//logger.warn("Connection couldn't be closed successfully");
				}
			}
		} catch (SQLException ex) {
			//logger.warn("SQL ERROR: " + ex);
            try {
				con.rollback();
			} catch (Exception e) {
				//logger.warn("Rollback didnt work");
				}
			} 
		//logger.info("return: "+maxId);
		return maxId;
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
