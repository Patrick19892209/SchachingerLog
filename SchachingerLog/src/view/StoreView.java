package view;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.primefaces.context.RequestContext;

import controller.StoreData;
import model.Delivery;


class TimeComparator implements Comparator<Delivery> {

	@Override
	public int compare(Delivery supp1, Delivery supp2) {		
		return supp1.getArrival().compareTo(supp2.getArrival());
	}
	
}

@ManagedBean
@ViewScoped
public class StoreView {

	private List<Delivery> openDeliveries;
	private List<Delivery> finishedDeliveries;
	private Date deliveryDate;
	private StoreData data;
    RequestContext context = RequestContext.getCurrentInstance();
    FacesMessage message = null;

	@PostConstruct
	public void init() {
		data = new StoreData();
		Calendar now = Calendar.getInstance();
		deliveryDate = now.getTime();
		initOpenDeliveries();
		initFinishedDeliveries();		
	}



	private void initFinishedDeliveries() {
		finishedDeliveries = data.finishedDeliveries(deliveryDate);
	}



	private void initOpenDeliveries() {
		openDeliveries = data.openDeliveries(deliveryDate);
		setAverageTime();
		Collections.sort(openDeliveries, new TimeComparator());
		
	}

	 public void confirm(ActionEvent actionEvent) {
		 
	 }
   

	private void setAverageTime() {
		data.setArrivals(openDeliveries);
	}



	public List<Delivery> getOpenDeliveries() {
		return openDeliveries;
	}


	public void setOpenDeliveries(List<Delivery> openDeliveries) {
		this.openDeliveries = openDeliveries;
	}


	public Date getDeliveryDate() {
		return deliveryDate;
	}


	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}


	public List<Delivery> getFinishedDeliveries() {
		return finishedDeliveries;
	}

	public void setFinishedDeliveries(List<Delivery> finishedDeliveries) {
		this.finishedDeliveries = finishedDeliveries;
	}
	
	
	
}
