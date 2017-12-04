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
	
	public boolean exists(String query) {
		Connection con = null;
		boolean bool = false;
		try {
			con = this.dbc.openConnection();
			con.setAutoCommit(false);
			Statement stmt = null;
			try {
				stmt = con.createStatement();
				this.rs = stmt.executeQuery(query);
				if(this.rs.next()) bool=true;
				logger.info(query + " erfolgreich durchgeführt - Bool: " + bool);
			} finally {
				try {
					
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
		catch(NullPointerException n) {
			System.out.println("BUT WHY?!");
		}
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
