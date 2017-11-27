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
		                + " FROM Lieferung Where Datum = '" + date + "'";
	          ResultSet result = query.executeQuery(sql);
	 
	          while (result.next()) {
	          String aviso = result.getString("Aviso");
	          String supplier = result.getString("Lieferant");
	          
	          list.add(new Delivery(supplier, day, aviso));
	          
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
	              String supplier = deli.getSupplier();
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
		      	      deli.setArrival(c.getTime());
			       }
		          else {
		        	  Calendar temp = Calendar.getInstance();
		        	  temp.set(Calendar.HOUR_OF_DAY, 12);
		        	  temp.set(Calendar.MINUTE, 0);
		        	  temp.set(Calendar.SECOND, 0);
		        	  deli.setArrival(temp.getTime());
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
	          
	          list.add(new Delivery(supplier, day, aviso));
	          
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
}
