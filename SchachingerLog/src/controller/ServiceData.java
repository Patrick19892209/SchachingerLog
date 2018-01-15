package controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import view.Service;

public class ServiceData extends Data {

	private String aviso;
	private int id;
	private String date;
	private String productNr;
	private String creator;
	private String amount;
	private String service;
	private int chatId;
	
	//Constructors
	public ServiceData(String aviso, String creator, String productNr, String to, String amount, String service) {
		super("controller.ServiceData");
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
		String getMaxIdService = "SELECT max(id) FROM Additional_Service WHERE aviso = '" + aviso + "'";
		int newId = getMaxId(getMaxIdService);	//get the new Id for the current Aviso (Avisos can have more than one complaint, thus they have an Id)
		if (newId < 0) {return false;}
		else {this.id=newId+1;}
		String getMaxChatId = "SELECT max(chatId) FROM Claim";
		int maxChatId = getMaxId(getMaxChatId);
		if(maxChatId<0) {return false;}
		else {this.chatId = maxChatId+1;}

		String insertService="INSERT INTO Additional_Service (aviso, id, date, creator, product_nr, service, amount, chatId) "
				+ "VALUES ('" + this.aviso + "', " +  this.id + ", " + "CURRENT_TIMESTAMP" + ", '" + this.creator + "', " 
				+ this.productNr + ", '" + this.service + "', " + this.amount + ", " + this.chatId +   ")";
		
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
	
	public List<Service> fetchClaims(String creator){
		
		if(this.chatId<1) return null;
		List<Service> serviceList = new ArrayList<>();
		String fetchServices = "SELECT * FROM Claim "
				+ "WHERE creator='" + creator + 
				"' OR assigned_to='" + creator + "' "
				+ "ORDER BY aviso DESC, id ASC";
		logger.info(fetchServices);
		try {
			this.con = dbc.openConnection();
			
			this.con.setAutoCommit(false);
			Statement stmt = null;
			try {
				stmt = this.con.createStatement();
				ResultSet rs = stmt.executeQuery(fetchServices);
				while(rs.next()) {
					Service service = new Service();
					service.setAviso(rs.getString("aviso"));
					service.setId(rs.getInt("id"));
					service.setDate(rs.getDate("entry_date").toString());
					//service.setDate(service.convertTsToDate(rs.getTimestamp("Timestamp")));
					service.setCreator(rs.getString("creator"));
					service.setProductNr(rs.getString("product_nr"));
					service.setAmount(rs.getString("amount"));
					service.setService(rs.getString("deficiency"));
					service.setChatId(rs.getInt("chatId"));
					//service.setDone(rs.getBoolean("done"));
					serviceList.add(service);
				}
				logger.info(fetchServices + " erfolgreich durchgeführt");
			} finally {
				try {
					stmt.close();
					this.con.commit();
					this.con.close();
				} catch (Exception ignore) {
					logger.warn("Connection couldn't be closed successfully");
				}
			}
		} catch (SQLException ex) {
			logger.warn("SQL ERROR: " + ex);
            try {
				this.con.rollback();
			} catch (Exception e) {
				logger.warn("Rollback didnt work");
				}
			} 
		return serviceList;	
	}

	public boolean update() {
		String updateClaim = "UPDATE Additional_Service SET "
				+ "aviso='" + this.aviso + "', id=" + this.id 
				+ ", entry_date='" + this.date + "', creator='" + this.creator 
				+ "', product_nr='" + this.productNr 
				+ "', amount=" + this.amount + ", service='" + this.service
				+ "' WHERE aviso='" + this.aviso + "' AND id=" + this.id;
		System.out.println(updateClaim);
		return update(updateClaim);
	}
	public boolean delete() {
		String delete="DELETE FROM Additional_Service WHERE aviso='" + this.aviso
				+ "' AND id=" + this.id;
		return update(delete);
	}

	
	//Getters and Setters

	public String getAviso() {
		return aviso;
	}

	public void setAviso(String aviso) {
		this.aviso = aviso;
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public void setChatId(int chatId) {
		this.chatId = chatId;
	}

	public String getDate() {
		return date;
	}

	public int getChatId() {
		return chatId;
	}

	public void setDate(String date) {
		this.date = date;
	}


	
	
}
