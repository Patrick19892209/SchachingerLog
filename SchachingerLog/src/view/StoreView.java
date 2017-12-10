package view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import controller.StoreData;
import model.Delivery;


class TimeComparator implements Comparator<Delivery> {

	@Override
	public int compare(Delivery supp1, Delivery supp2) {
			if (supp1.getArrival().get(Calendar.HOUR_OF_DAY) == supp2.getArrival().get(Calendar.HOUR_OF_DAY)) {
               if(supp1.getArrival().get(Calendar.MINUTE) == supp2.getArrival().get(Calendar.MINUTE)) {
            	   if(supp1.getArrival().get(Calendar.SECOND) == supp2.getArrival().get(Calendar.SECOND)) {
            		   return 0;
            	   } else if (supp1.getArrival().get(Calendar.SECOND) < supp2.getArrival().get(Calendar.SECOND)) return -1;
            	   else return 1;      	  
               } else if (supp1.getArrival().get(Calendar.MINUTE) < supp2.getArrival().get(Calendar.MINUTE)) return -1;
               else	return 1;
		   } else if (supp1.getArrival().get(Calendar.HOUR_OF_DAY) < supp2.getArrival().get(Calendar.HOUR_OF_DAY)) return -1;
		   else return 1;
	}	
}

@ManagedBean
@ViewScoped
public class StoreView {

	private List<Delivery> openDeliveries;
	private List<Delivery> finishedDeliveries;
	private Date deliveryDate;
	private Delivery deliDone;
	private StoreData data;
	private List<String> gates;
	private int gate;
    RequestContext context = RequestContext.getCurrentInstance();
    FacesMessage message = null;
    
    @ManagedProperty(value="#{login}")
    Login login;
    
	@PostConstruct
	public void init() {
		data = new StoreData();
		Calendar now = Calendar.getInstance();
		deliveryDate = now.getTime();
		initOpenDeliveries();
		initFinishedDeliveries();
		int maxGates = data.getGates(login.getUser().getLocation());
		gates = new ArrayList<>();
		for (int i = 1; i <= maxGates; i++) {
			gates.add("Tor " + i);
		}
		this.gate = 1;
	}

    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary,  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void switchGate(String s) {
    	this.gate = Integer.parseInt(Character.toString(s.charAt(4)));
    }
	private void initFinishedDeliveries() {
		finishedDeliveries = data.finishedDeliveries(deliveryDate);
	}

	public void setDeliDone(Delivery deliDone) {
		this.deliDone = deliDone;
		Calendar c = Calendar.getInstance();
		deliDone.setTime2(deliveryDate, c);
		boolean result = data.setTables(deliDone, deliveryDate, this.gate, this.login.getUser().getAbbrevation());
		if (!result) { addMessage("Fehler bei der Verarbeitung"); return ; }
		initOpenDeliveries();
		initFinishedDeliveries();
        RequestContext.getCurrentInstance().update("lager:delivery");
		addMessage("Alles Gut");
	}

	public void cancel(Delivery deli) {
		boolean result = data.delDelivery(deli);
		if (!result) { addMessage("Fehler bei der Verarbeitung"); return ; }
		initOpenDeliveries();
		initFinishedDeliveries();
        RequestContext.getCurrentInstance().update("lager:delivery");
		addMessage("Alles Gut");
	}
	
	public void onDateSelect(SelectEvent event) {
	        FacesContext facesContext = FacesContext.getCurrentInstance();
	        Date dateNew = (Date) event.getObject();
	        if (dateNew.compareTo(deliveryDate) != 0) return;
	        setDeliveryDate(dateNew);
	        updateDeliveries();
	        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
	        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected", format.format(event.getObject())));
	        RequestContext.getCurrentInstance().update("lager:delivery");
	 }

	private void updateDeliveries() {
		openDeliveries = data.openDeliveries(getDeliveryDate());
		setAverageTime();
		Collections.sort(openDeliveries, new TimeComparator());
				
	}


	private void initOpenDeliveries() {
		openDeliveries = data.openDeliveries(deliveryDate);
		setAverageTime();
		Collections.sort(openDeliveries, new TimeComparator());
	}

	
	
	public Login getLogin() {
		return login;
	}

	public void setLogin(Login login) {
		this.login = login;
	}

	public List<String> getGates() {
		return gates;
	}

	public void setGates(List<String> gates) {
		this.gates = gates;
	}

	public int getGate() {
		return gate;
	}

	public void setGate(int gate) {
		this.gate = gate;
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


	
	public Delivery getDeliDone() {
		return deliDone;
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
