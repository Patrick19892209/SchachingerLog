package controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ServiceData extends Data {

	private String aviso;
	private String erfasser;
	private String artNr;
	private String menge;
	private String service;
	private String ts = "CURRENT_TIMESTAMP";
	
	//Constructors
	public ServiceData(String aviso, String erfasser, String artNr, String menge, String service) {
		super("controller.ServiceData");
		this.aviso = aviso;
		this.erfasser = erfasser;
		this.artNr = artNr;
		this.menge = menge;
		this.service = service;
	}
	public ServiceData(){	
		super("controller.ServiceData");
	}
	
	//checks whether abbrevation exists
	public boolean abbrevationExists(String abbr) {
		String abbrExists = "SELECT * FROM User WHERE Abbrevation = '" + abbr + "'";
		logger.info(abbrExists);
		if(exists(abbrExists)) return true;
		return false;
	}
	
	//checks whether certain Aviso exists in the db
	public boolean avisoExists(String aviso) {
		String avisoExists =  "SELECT * FROM Lieferung WHERE Aviso = '" + aviso + "'";
		logger.info(avisoExists);
		if(exists(avisoExists));
		return false;
	}
	//inserts the complaint values from a complaint object to the db
	public boolean insert() {

		ReklaData rd = new ReklaData();
		int newId = rd.getMaxId(this.aviso) + 1;	//get the new Id for the current Aviso (Avisos can have more than one complaint, thus they have an Id)
		if (newId == 0) return false; 
		String insertService="INSERT INTO Additional_Service (Aviso, Id, Timestamp, Erfasser, ArtikelNr, Service, Menge) "
				+ "VALUES ('" + this.aviso + "', " +  newId + ", " + this.ts + ", '" + this.erfasser + "', '" 
				+ this.artNr + "', '" + this.service + "', '" + this.menge +  "')";
		
		this.logger.info("Insert Query: " + insertService);
		Connection con = null;
		boolean bool = false;
		try {
			con = dbc.openConnection();
			con.setAutoCommit(false);
			Statement stmt = null;
			try {
				stmt = con.createStatement();
				stmt.executeUpdate(insertService);
				bool = true;
				logger.info(insertService + " erfolgreich durchgeführt");
			} finally {
				try {
					stmt.close();
					con.commit();
					con.close();
				} catch (Exception ignore) {
					logger.warn("Connection couldn't be closed successfully");
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
		
		return bool;
	}

	//Getters and Setters

	public String getTs() {
		return ts;
	}
	public void setTs(String ts) {
		this.ts = ts;
	}
	public String getAviso() {
		return aviso;
	}
	public void setAviso(String aviso) {
		this.aviso = aviso;
	}
	public String getErfasser() {
		return erfasser;
	}
	public void setErfasser(String erfasser) {
		this.erfasser = erfasser;
	}
	public String getArtNr() {
		return artNr;
	}

	public void setArtNr(String artNr) {
		this.artNr = artNr;
	}

	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public String getMenge() {
		return menge;
	}

	public void setMenge(String menge) {
		this.menge = menge;
	}
	//

	
}
