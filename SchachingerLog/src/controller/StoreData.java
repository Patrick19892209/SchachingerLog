package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.ParseException;
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
		                "SELECT *"
		                + " FROM Delivery Where date = '" + date + "' and arrival_date IS NULL";
	          ResultSet result = query.executeQuery(sql);
	 
	          while (result.next()) {
		          String aviso = result.getString("aviso");
		          String supplier = result.getString("supplier");
		          Calendar arrivalC = Calendar.getInstance();
		          list.add(new Delivery(supplier, arrivalC, aviso));}
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
			      + " FROM Registered Where supplier = '" + supplier + "'";
		          ResultSet result = query.executeQuery(sql);
		 
		          while (result.next()) {
		          Time time = result.getTime("delivery_time");
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
		                + " FROM Registered Where delivery_date = '" + date + "'";
	          ResultSet result = query.executeQuery(sql);
	 
	          while (result.next()) {
		          String aviso = result.getString("aviso");
		          String supplier = result.getString("supplier");
		          Date arrivalD = result.getDate("arrival_date");
		          Time arrivalT = result.getTime("arrival_time");
		          Date departureD = result.getDate("delivery_date");
		          Time departureT = result.getTime("delivery_time");
		          SimpleDateFormat sdfD = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		          String dateInStringD = arrivalD.toString().concat(" ").concat(arrivalT.toString());
		          SimpleDateFormat sdfT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		          String dateInStringT = departureD.toString().concat(" ").concat(departureT.toString());
		          
		          Calendar arrivalC = Calendar.getInstance();
		          Calendar departureC = Calendar.getInstance();
		          try {
					arrivalC.setTime(sdfD.parse(dateInStringD));
			        departureC.setTime(sdfT.parse(dateInStringT));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		          list.add(new Delivery(supplier, arrivalC, departureC, aviso));	          
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

	public boolean setTables(Delivery deli, Date deliveryDate, int gate, String name) {
		boolean result = true;
		dbc = new DBConnector();
		con = dbc.openConnection();
		Calendar c = Calendar.getInstance();
		c.setTime(deliveryDate);
	    Statement query = null;
	    try {
	          query = con.createStatement();
	          String sql =
		                "UPDATE Delivery"
		                + " Set arrival_date = '" + new java.sql.Date(deli.getDeparture().getTime().getTime()) + "' Where aviso = '" + deli.getAviso() + 
		                "' AND supplier = '" + deli.getFullSupplier() + "'";
	  		query.executeUpdate(sql);
			 
	  		query = con.createStatement();
	        sql =
		                "INSERT INTO Registered (aviso, supplier, creator, delivery_date, delivery_time, gate, arrival_date, arrival_time) VALUES ('"
		                + deli.getAviso() + "','" + deli.getFullSupplier() + "','" + name + "','" +
		                new java.sql.Date(c.getTime().getTime()) + "','" + new java.sql.Time(deli.getDeparture().getTime().getTime()) + "','" + gate + "','" +
		                new java.sql.Date(c.getTime().getTime()) + "','" + new java.sql.Time(deli.getArrival().getTime().getTime()) + "')";
	  		query.executeUpdate(sql);
			 
	          
	      } catch (SQLException e) {
	        e.printStackTrace();
	        try {
				con.rollback();
				dbc.closeConnection(con);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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
		                "SELECT nr_of_gates"
		                + " FROM Location Where name = '" + location + "'";

	          ResultSet results = query.executeQuery(sql);
	          while (results.next()) {
	        	  result = results.getInt("nr_of_gates");
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

	public boolean delDelivery(Delivery deli) {
		boolean result = true;
		dbc = new DBConnector();
		con = dbc.openConnection();
		Statement query = null;
	    try {
	          query = con.createStatement();
	          String sql =
		                "DELETE FROM Registered"
		                + " Where supplier = '" + deli.getFullSupplier() + "' and aviso = '" + deli.getAviso() + "'";
	  		query.executeUpdate(sql);
			 
	  		query = con.createStatement();
	        sql = "UPDATE Delivery Set arrival_date = CAST(NULL As Date)"
		                + " Where supplier = '" + deli.getFullSupplier() + "' and aviso = '" + deli.getAviso() + "'";
	  		query.executeUpdate(sql);
			 
	          
	      } catch (SQLException e) {
	        e.printStackTrace();
	        try {
				con.rollback();
				dbc.closeConnection(con);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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
}
