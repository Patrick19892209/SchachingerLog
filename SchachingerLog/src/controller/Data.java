package controller;

import java.sql.Connection;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import model.DBConnector;

public class Data {
	
	protected DBConnector dbc;
	protected Connection con;
	protected Logger logger = null;
	
	public Data() {
		this.dbc = new DBConnector();
		this.con = dbc.openConnection();
	}
	public Data(String logname) {
		this.logger = (Logger) LoggerFactory.getLogger("model." + logname);
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
