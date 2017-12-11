package controller;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import view.Claim;

public class ClaimData extends Data{

	private String aviso;
	private String id;
	private Date date;
	private String productNr;
	private String creator;
	private String to = null;
	private String amount;
	private String deficiency;
	private String ts = "CURRENT_TIMESTAMP";
	
	//Constructors
	public ClaimData(String aviso, String creator, String productNr, String to, String amount, String deficiency) {
		super("controller.ClaimData");
		this.aviso = aviso;
		this.creator = creator;
		this.productNr = productNr;
		this.to = to;
		this.amount = amount;
		this.deficiency = deficiency;
	}
	
	public ClaimData(Claim claim) {	//since 
		this.aviso=claim.getAviso();
		this.creator = claim.getCreator();
		this.productNr = claim.getProductNr();
		this.to = claim.getTo();
		this.amount = claim.getAmount();
		this.deficiency = claim.getDeficiency();
	}
	
	public ClaimData(){	
		super("controller.ClaimData");
	}
	
	public List<Claim> fetchClaims(){
		List<Claim> claimList = new ArrayList<>();
		String fetchClaims = "SELECT * FROM Claim";
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
					Date date = new Date(rs.getDate("entry_date").getTime());
					claim.setDate(date);
					//claim.setDate(claim.convertTsToDate(rs.getTimestamp("Timestamp")));
					claim.setCreator(rs.getString("creator"));
					claim.setTo(rs.getString("assigned_to"));
					claim.setProductNr(rs.getString("product_nr"));
					claim.setAmount(rs.getString("amount"));
					claim.setDeficiency(rs.getString("deficiency"));
					// TEST System.out.println(rs.getString(1) + " " + rs.getInt(2) + " " + rs.getString(3) + " " + rs.getString(4) + " " + rs.getString(5)+ " " + rs.getString(6)+ " " + rs.getString(7)+ " " + rs.getString(8));
					claimList.add(claim);
				}
				logger.info(fetchClaims + " erfolgreich durchgeführt");
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
		return claimList;	
	}
	
	public boolean update() {
		String updateClaim = "UPDATE Claim SET aviso=" + this.aviso + ", id=" + this.id 
				+ ", entry_date=" + this.date + ", creator=" + this.creator 
				+ ", assigned_to=" + this.to + ", product_nr=" + this.productNr 
				+ ", amount=" + this.amount + ", deficiency=" + this.deficiency
				+ "WHERE aviso=" + this.aviso + "AND id =" + this.id;
		boolean bool = false;
		try {
			this.con = this.dbc.openConnection();
			this.con.setAutoCommit(false);
			Statement stmt = null;
			try {
				stmt = this.con.createStatement();
				stmt.executeUpdate(updateClaim);
				bool = true;
				logger.info(updateClaim + " erfolgreich durchgeführt");
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
		
		return bool;
	}
	
	//inserts the complaint values from a complaint object to the db
	public boolean insert() {
		
		int newId = getMaxId(this.aviso) + 1;	//get the Id for the current Aviso (Avisos can have more than one complaint)
		if (newId == 0) return false; 
		String toString;	//if assigned_to is null, "NULL" has to be passed as SQL parameter
		if(this.to == null || this.to == "") {
			toString = "NULL";	
		}
		else {
			toString = "'" + this.to + "'";
		}
		String insertClaim="INSERT INTO Claim (aviso, id, entry_date, creator, assigned_to, product_nr, amount, deficiency) "
				+ "VALUES ('" + this.aviso + "', " +  newId + ", " + this.ts + ", '" + this.creator + "', " + toString +  ", '" + this.productNr +"', '" + this.amount + "', '" + this.deficiency + "')";
		System.out.println("Query: " + insertClaim);
		boolean bool = false;
		try {
			this.con = this.dbc.openConnection();
			this.con.setAutoCommit(false);
			Statement stmt = null;
			try {
				stmt = this.con.createStatement();
				stmt.executeUpdate(insertClaim);
				bool = true;
				logger.info(insertClaim + " erfolgreich durchgeführt");
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
		
		return bool;
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
	
	//gets the highest id of certain aviso currently existing in the db
	public int getMaxId(String aviso) {
		
		String getMaxId = "SELECT max(Id) FROM Claims WHERE Aviso = '" + aviso + "'";
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
	public String getTs() {
		return ts;
	}
	public void setTs(String ts) {
		this.ts = ts;
	}	
}
