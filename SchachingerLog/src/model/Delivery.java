package model;

import java.util.Calendar;
import java.util.Date;

public class Delivery {

	private String aviso;
	private String orderId;
	private String supplier;
	private Calendar arrival;
	private Calendar departure;
	private boolean claim;
	private boolean service;
	
	public Delivery(String supplier, Calendar arrival, Calendar departure, String aviso) {
		this.aviso = aviso;
		this.supplier = supplier;
		this.arrival = arrival;
		this.departure = departure;
	}

	public Delivery(String supplier, Calendar arrival, String aviso) {
		this.aviso = aviso;
		this.supplier = supplier;
		this.arrival = arrival;
		this.departure = Calendar.getInstance();
	}
	public String getAviso() {
		return aviso;
	}

	public void setAviso(String aviso) {
		this.aviso = aviso;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getSupplier() {
		if (this.supplier.length() > 15 ) return this.supplier.substring(0, 15);
		return this.supplier;
	}

	public String getFullSupplier() {
		return this.supplier;
	}
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public Calendar getArrival() {
		return arrival;
	}

	public void setArrival(Calendar arrival) {
		this.arrival = arrival;
	}

	public Calendar getDeparture() {
		return departure;
	}

	public void setDeparture(Calendar departure) {
		this.departure = departure;
	}

	public boolean isClaim() {
		return claim;
	}

	public void setClaim(boolean claim) {
		this.claim = claim;
	}

	public boolean isService() {
		return service;
	}

	public void setService(boolean service) {
		this.service = service;
	}

	public void setTimeDep(Date date,Calendar c) {
		this.departure.setTime(date);
		this.departure.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY));
		this.departure.set(Calendar.MINUTE, c.get(Calendar.MINUTE));
		this.departure.set(Calendar.SECOND, c.get(Calendar.SECOND));
	}
	
	public void setTimeArr(Date date,Calendar c) {
		this.arrival.setTime(date);
		this.arrival.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY));
		this.arrival.set(Calendar.MINUTE, c.get(Calendar.MINUTE));
		this.arrival.set(Calendar.SECOND, c.get(Calendar.SECOND));
	}
	
}
