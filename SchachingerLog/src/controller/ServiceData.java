package controller;


import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;

import view.Service;

public class ServiceData extends Data {

	private String aviso;
	private String id;
	private Date date;
	private String productNr;
	private String creator;
	private String amount;
	private String service;
	
	//Constructors
	public ServiceData(String aviso, String creator, String productNr, String to, String amount, String service) {
		super("controller.ClaimData");
		this.aviso = aviso;
		this.creator = creator;
		this.productNr = productNr;
		this.amount = amount;
		this.service = service;
	}
	
	public ServiceData(Service service) {	//since 
		this.aviso = service.getAviso();
		this.creator = service.getCreator();
		this.productNr = service.getProductNr();
		this.amount = service.getAmount();
		this.service = service.getService();

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
		String avisoExists =  "SELECT * FROM Delivery WHERE aviso = '" + aviso + "'";
		logger.info(avisoExists);
		if(exists(avisoExists));
		return false;
	}
	//inserts the complaint values from a complaint object to the db
	public boolean insert() {

		ClaimData rd = new ClaimData();
		int newId = rd.getMaxId(this.aviso) + 1;	//get the new Id for the current Aviso (Avisos can have more than one complaint, thus they have an Id)
		if (newId == 0) return false; 
		String insertService="INSERT INTO Additional_Service (aviso, id, date, creator, product_nr, service, amount) "
				+ "VALUES ('" + this.aviso + "', " +  newId + ", " + this.date + ", '" + this.creator + "', '" 
				+ this.productNr + "', '" + this.service + "', '" + this.amount +  "')";
		
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

	public String getAviso() {
		return aviso;
	}

	public void setAviso(String aviso) {
		this.aviso = aviso;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getProductNr() {
		return productNr;
	}

	public void setProductNr(String productNr) {
		this.productNr = productNr;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}


	
	
}
