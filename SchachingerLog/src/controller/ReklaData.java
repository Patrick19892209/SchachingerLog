package controller;

import java.sql.SQLException;
import java.sql.Statement;

public class ReklaData extends Data{

	private String id;
	private String aviso;
	private String erfasser;
	private String artNr;
	private String an;
	private String menge;
	private String mangel;
	
	//Constructors
	public ReklaData(String erfasser, String artNr, String an, String menge, String mangel) {
		super("model.ReklaData");
		this.erfasser = erfasser;
		this.artNr = artNr;
		this.an = an;
		this.menge = menge;
		this.mangel = mangel;
	}
	public ReklaData(){	
		
	}
	
	private String insertString() {
		return "INSERT INTO Reklamation (Id, Aviso, Erfasser, ZugewiesenAn, ArtikelNr, Menge) "
				+ "VALUES ('" + this.id + "', '" + this.aviso + "', '" + this.erfasser + "', '" + this.an + "', '" + this.artNr +"', '" + this.menge + "')";
	}

	public void insertRekla() {
		String insertString=(insertString());
		try {
			this.con.setAutoCommit(false);
			Statement stmt = null;
			try {
				stmt = con.createStatement();
				
				stmt.executeUpdate(insertString);
			} finally {
				try {
					stmt.close();
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
		logger.info(insertString + "erfolgreich durchgeführt");
	}
	
	//Getters and Setters
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
