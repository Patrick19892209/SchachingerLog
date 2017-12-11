package controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ReklaData extends Data{

	private String aviso;
	private String erfasser;
	private String artNr;
	private String an = null;
	private String menge;
	private String mangel;
	private String ts = "CURRENT_TIMESTAMP";
	
	//Constructors
	public ReklaData(String aviso, String erfasser, String artNr, String an, String menge, String mangel) {
		super("controller.ReklaData");
		this.aviso = aviso;
		this.erfasser = erfasser;
		this.artNr = artNr;
		this.an = an;
		this.menge = menge;
		this.mangel = mangel;
	}
	public ReklaData(){	
		super("controller.ReklaData");
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
		if(exists(avisoExists)) return true;
		return false;
	}
	//inserts the complaint values from a complaint object to the db
	public boolean insert() {
		
		int newId = getMaxId(this.aviso) + 1;	//get the Id for the current Aviso (Avisos can have more than one complaint)
		if (newId == 0) return false; 
		String anString;	//if ZugewiesenAn is null NULL has to be passed as SQL parameter
		if(this.an == null || this.an == "") {
			anString = "NULL";	
		}
		else {
			anString = "'" + this.an + "'";
		}
		String insertRekla="INSERT INTO Reklamation (Aviso, Id, Timestamp, Erfasser, ZugewiesenAn, ArtikelNr, Menge, Mangel) "
				+ "VALUES ('" + this.aviso + "', " +  newId + ", " + this.ts + ", '" + this.erfasser + "', " + anString +  ", '" + this.artNr +"', '" + this.menge + "', '" + this.mangel + "')";
		System.out.println("Query: " + insertRekla);
		Connection con = null;
		boolean bool = false;
		try {
			con = dbc.openConnection();
			con.setAutoCommit(false);
			Statement stmt = null;
			try {
				stmt = con.createStatement();
				stmt.executeUpdate(insertRekla);
				bool = true;
				logger.info(insertRekla + " erfolgreich durchgeführt");
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
	//gets the highest Id of certain Aviso currently existing in the db
	public int getMaxId(String aviso) {
		
		String getMaxId = "SELECT max(Id) FROM Reklamation WHERE Aviso = '" + aviso + "'";
		int maxId = -1;
		try {
			con = dbc.openConnection();
			con.setAutoCommit(false);
			Statement stmt = null;
			try {
				stmt = con.createStatement();
				this.rs = stmt.executeQuery(getMaxId);
				if (rs.next()) maxId = this.rs.getInt(1);
				logger.info("MaxId: " + maxId);
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
		return maxId;
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

	public String getAn() {
		return an;
	}

	public void setAn(String an) {
		this.an = an;
	}

	public String getMenge() {
		return menge;
	}

	public void setMenge(String menge) {
		this.menge = menge;
	}

	public String getMangel() {
		return mangel;
	}

	public void setMangel(String mangel) {
		this.mangel = mangel;
	}
	//
	
}
