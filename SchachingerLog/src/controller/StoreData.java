package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import model.DBConnector;
import model.Delivery;
public class StoreData {

	private DBConnector dbc;
	private static Connection con;
	
	public List<Delivery> openDeliveries(Date day) {
		dbc = new DBConnector();
		con = dbc.openConnection();
		List <Delivery> list = new ArrayList<>();
	      
	    Statement query = null;
	    try {
	          query = con.createStatement();
        	  SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        	  String date = format1.format(day);
	 
	          String sql =
		                "SELECT Aviso,BestellNr,Datum,Lieferant,AnzahlPos,Komplexität"
		                + " FROM Lieferung Where Datum = '" + date + "' and Arrival_Date IS NULL";
	          ResultSet result = query.executeQuery(sql);
	 
	          while (result.next()) {
		          String aviso = result.getString("Aviso");
		          String supplier = result.getString("Lieferant");
		          Calendar c = Calendar.getInstance();
		          c.setTime(day);
		          list.add(new Delivery(supplier, c, aviso));	          
	          }
	      } catch (SQLException e) {
	        e.printStackTrace();
	      }
	    try {
			query.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	return list;
	}

	public void setArrivals(List<Delivery> openDeliveries) {
		dbc = new DBConnector();
		con = dbc.openConnection();
		Statement query = null;
	    try {
	          query = con.createStatement();
	          for (Delivery deli: openDeliveries) {
	    	      List<Time> list = new ArrayList<>();
	    	      Calendar c = Calendar.getInstance();
	              String supplier = deli.getFullSupplier();
	              long sum = 0L;
        		  String sql =
			      "SELECT *"
			      + " FROM Eingebucht Where Lieferant = '" + supplier + "'";
		          ResultSet result = query.executeQuery(sql);
		 
		          while (result.next()) {
		          Time time = result.getTime("LieferZeit");
		          list.add(time);
		          }

		          if (!list.isEmpty()) {int counter = 0;
		      	        for (Time t : list)
		      	            {
		      	                sum += t.getTime();
		      	                counter++;
		      	            }
		      	      c.setTimeInMillis((sum/counter));
		      	      deli.setArrival(c);
			       }
		          else {
		        	  Calendar temp = Calendar.getInstance();
		        	  temp.set(Calendar.HOUR_OF_DAY, 12);
		        	  temp.set(Calendar.MINUTE, 0);
		        	  temp.set(Calendar.SECOND, 0);
		        	  deli.setArrival(temp);
		          }
	       	 }
	      } catch (SQLException e) {
	        e.printStackTrace();
	      }
	    dbc.closeConnection(con);
	}

	public List<Delivery> finishedDeliveries(Date day) {
		dbc = new DBConnector();
		con = dbc.openConnection();
		List <Delivery> list = new ArrayList<>();
	      
	    Statement query = null;
	    try {
	          query = con.createStatement();
        	  SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        	  String date = format1.format(day);
	 
	          String sql =
		                "SELECT *"
		                + " FROM Eingebucht Where LieferDatum = '" + date + "'";
	          ResultSet result = query.executeQuery(sql);
	 
	          while (result.next()) {
		          String aviso = result.getString("Aviso");
		          String supplier = result.getString("Lieferant");
		          Calendar c = Calendar.getInstance();
		          c.setTime(day);
		          list.add(new Delivery(supplier, c, aviso));	          
	          }
	      } catch (SQLException e) {
	        e.printStackTrace();
			dbc.closeConnection(con);
	      }
	    try {
			query.close();
			dbc.closeConnection(con);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			dbc.closeConnection(con);
		}
	return list;
	}

	public boolean setTableLieferung(Delivery deli, Date deliveryDate) {
		boolean result = true;
		dbc = new DBConnector();
		con = dbc.openConnection();
		Calendar c = Calendar.getInstance();
		c.setTime(deliveryDate);
	    Statement query = null;
	    try {
	          query = con.createStatement();
	          String sql =
		                "UPDATE Lieferung"
		                + " Set Arrival_Date = '" + new java.sql.Date(deli.getDeparture().getTime().getTime()) + "' Where Aviso = '" + deli.getAviso() + 
		                "' AND Lieferant = '" + deli.getFullSupplier() + "'";
	  		query.executeUpdate(sql);
			 
	          
	      } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	      }
	    try {
			query.close();
			dbc.closeConnection(con);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			dbc.closeConnection(con);
			return false;
		}
	    return result;
	}

	public boolean setTableEingebucht(Delivery deli, Date deliveryDate) {
		boolean result = true;
		dbc = new DBConnector();
		con = dbc.openConnection();
		Calendar c = Calendar.getInstance();
		c.setTime(deliveryDate);
	    Statement query = null;
	    try {
	          query = con.createStatement();
	          
	          String sql =
		                "INSERT INTO Eingebucht (Aviso, Lieferant, Erfasser, LieferDatum, LieferZeit) VALUES ('"
		                + deli.getAviso() + "','" + deli.getFullSupplier() + "','" + "BM2','" +
		                new java.sql.Date(c.getTime().getTime()) + "','" + new java.sql.Time(deli.getDeparture().getTime().getTime()) + "')";
	          query.executeUpdate(sql);
			 
	          
	      } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	      }
	    try {
			query.close();
			dbc.closeConnection(con);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			dbc.closeConnection(con);
			return false;
		}
	    return result;
	}

	public int getGates(String location) {
		dbc = new DBConnector();
		con = dbc.openConnection();
		int result = 0 ;
	    Statement query = null;
	    try {
	          query = con.createStatement();
	 
	          String sql =
		                "SELECT Anzahl_Tore"
		                + " FROM Location Where Name = '" + location + "'";

	          ResultSet results = query.executeQuery(sql);
	          while (results.next()) {
	        	  result = results.getInt("Anzahl_Tore");
	          }
	      } catch (SQLException e) {
	        e.printStackTrace();
			dbc.closeConnection(con);
			result = -1;
	      }
	    try {
			query.close();
			dbc.closeConnection(con);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			dbc.closeConnection(con);
			result = -1;
		}
	return result;
	}		
}
