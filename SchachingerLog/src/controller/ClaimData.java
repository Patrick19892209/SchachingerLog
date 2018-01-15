package controller;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedProperty;

import view.Claim;

public class ClaimData extends Data{

	private String aviso;
	private int id;
	private String date;
	private String productNr;
	private String creator;
	private String to = null;
	private String amount;
	private String deficiency;
	private int chatId;
	private boolean done;

	//Constructors
	public ClaimData(String aviso, String creator, String productNr, String to, String amount, String deficiency) {
		super("controller.ClaimData");
		this.aviso = aviso;
		this.creator = creator;
		this.productNr = productNr;
		this.to = to;
		this.amount = amount;
		this.deficiency = deficiency;
		String maxChatId = "SELECT max(id) FROM Claim WHERE chatId = '" + chatId + "'";
		this.chatId = getMaxId(maxChatId) + 1;
		}

	public ClaimData(Claim claim) {	
		super("controller.ClaimData");
		this.aviso = claim.getAviso();
		this.id = claim.getId();
		this.date = claim.getDate();
		this.creator = claim.getCreator();
		this.productNr = claim.getProductNr();
		this.to = claim.getTo();
		this.amount = claim.getAmount();
		this.deficiency = claim.getDeficiency();
		String maxChatId = "SELECT max(id) FROM Claim WHERE chatId = '" + chatId + "'";
		this.chatId = getMaxId(maxChatId) + 1;
		this.done = claim.isDone();
	}
	
	public ClaimData(){	
		super("controller.ClaimData");
		String maxChatId = "SELECT max(id) FROM Claim WHERE chatId = '" + chatId + "'";
		this.chatId = getMaxId(maxChatId) +1;
}
	
	/*
	String input = "2012/01/20 12:05:10.321";
	DateFormat inputFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
	Date date = inputFormatter.parse(input);

	DateFormat outputFormatter = new SimpleDateFormat("MM/dd/yyyy");
	String output = outputFormatter.format(date); // Output : 01/20/2012
	*/
	public static Date StringToDate(String sdate) throws ParseException {
		DateFormat inputFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
		Date date = inputFormatter.parse(sdate);
		return date;
	}
	
	public List<Claim> fetchClaims(String creator){
		
		if(this.chatId<1) return null;
		List<Claim> claimList = new ArrayList<>();
		String fetchClaims = "SELECT * FROM Claim "
				+ "WHERE creator='" + creator + 
				"' OR assigned_to='" + creator + "' "
				+ "ORDER BY aviso DESC, id ASC";
		//logger.info(fetchClaims);
		try {
			this.con = dbc.openConnection();
			
			this.con.setAutoCommit(false);
			Statement stmt = null;
			try {
				stmt = this.con.createStatement();
				ResultSet rs = stmt.executeQuery(fetchClaims);
				while(rs.next()) {
					Claim claim = new Claim();
					claim.setAviso(rs.getString("aviso"));
					claim.setId(rs.getInt("id"));
					claim.setDate(rs.getDate("entry_date").toString());
					//claim.setDate(claim.convertTsToDate(rs.getTimestamp("Timestamp")));
					claim.setCreator(rs.getString("creator"));
					claim.setTo(rs.getString("assigned_to"));
					claim.setProductNr(rs.getString("product_nr"));
					claim.setAmount(rs.getString("amount"));
					claim.setDeficiency(rs.getString("deficiency"));
					claim.setChatId(rs.getInt("chatId"));
					claim.setDone(rs.getBoolean("done"));
					// TEST System.out.println(rs.getString(1) + " " + rs.getInt(2) + " " + rs.getString(3) + " " + rs.getString(4) + " " + rs.getString(5)+ " " + rs.getString(6)+ " " + rs.getString(7)+ " " + rs.getString(8));
					//claim.setChatHistory(fetchHistory(this.chatId));
					claimList.add(claim);
				}
				//logger.info(fetchClaims + " erfolgreich durchgeführt");
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
			//logger.warn("SQL ERROR: " + ex);
            try {
				this.con.rollback();
			} catch (Exception e) {
				//logger.warn("Rollback didnt work");
				}
			} 
		return claimList;	
	}
	
	public boolean update() {
		String updateClaim = "UPDATE Claim SET "
				+ "aviso='" + this.aviso + "', id=" + this.id 
				+ ", entry_date='" + this.date + "', creator='" + this.creator 
				+ "', assigned_to='" + this.to + "', product_nr='" + this.productNr 
				+ "', amount=" + this.amount + ", deficiency='" + this.deficiency
				+ "' WHERE aviso='" + this.aviso + "' AND id=" + this.id;
		//System.out.println(updateClaim);
		return update(updateClaim);
	}
	
	//inserts the complaint values from a complaint object to the db
	public boolean insert() {
		
		String getMaxIdAviso = "SELECT max(id) FROM Claim WHERE aviso = '" + aviso + "'";
		int maxAvisoId = getMaxId(getMaxIdAviso);	//get the Id for the current Aviso (Avisos can have more than one complaint)
		if (maxAvisoId < 0) {return false;}
		else {this.id=maxAvisoId+1;}
		String nullString;	//if assigned_to is null, "NULL" has to be passed as SQL parameter
		String getMaxChatId = "SELECT max(chatId) FROM Claim";
		int maxChatId = getMaxId(getMaxChatId);
		if(maxChatId<0) {return false;}
		else {this.chatId = maxChatId+1;}
		if(this.to == null || this.to == "") {
			nullString = "NULL";	
		}
		else {
			nullString = "'" + this.to + "'";
		}
		String insertClaim="INSERT INTO Claim (aviso, id, entry_date, creator, assigned_to, product_nr, amount, deficiency, chatId, done) "
				+ "VALUES ('" + this.aviso + "', " +  this.id + ", " 
				+ "CURRENT_TIMESTAMP" + ", '" + this.creator + "', " 
				+ nullString +  ", '" + this.productNr +"', '" 
				+ this.amount + "', '" + this.deficiency + "', "
				+ this.chatId + ", " + done + ")";
		//System.out.println("Query: " + insertClaim);
		return update(insertClaim);
	}
	
	public boolean delete() {
		String delete="DELETE FROM Claim WHERE aviso='" + this.aviso
				+ "' AND id=" + this.id;
		return update(delete);
	}
	
	//checks whether abbrevation exists
	public boolean abbrevationExists(String abbr) {
		String abbrExists = "SELECT * FROM User WHERE abbrevation = '" + abbr + "'";
		if(exists(abbrExists)) return true;
		return false;
	}
	
	//checks whether certain Aviso exists in the db
	public boolean avisoExists(String aviso) {
		String avisoExists =  "SELECT * FROM Delivery WHERE aviso = '" + aviso + "'";
		if(exists(avisoExists)) return true;
		return false;
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
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
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getDeficiency() {
		return deficiency;
	}
	public void setDeficiency(String deficiency) {
		this.deficiency = deficiency;
	}

	public int getChatId() {
		return chatId;
	}

	public boolean isDone() {
		return done;
	}

	public void setChatId(int chatId) {
		this.chatId = chatId;
	}

	public void setDone(boolean done) {
		this.done = done;
	}	
}
