package view;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
//import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import controller.ServiceData;

@ManagedBean(name="service")
@ViewScoped
public class Service {

	private String aviso;
	private int id;
	private String creator;
	private String productNr;
	private String amount;
	private String service;
	private Date date;
	private UIComponent serviceButton;
	//private ClaimData redat;
	List <String> serviceList;
	private String redirect;
    @ManagedProperty(value="#{storeView}")
    StoreView store;
    

    @PostConstruct
	public void init() {
		if (store.isEntered() == true) {
			this.aviso = store.getDeliDone().getAviso();
			this.redirect = "lager";
		} else {
			this.redirect = "buero";
		}		
	}

	public Service() {
		//Dropdown opts
		serviceList = new ArrayList<>();
		serviceList.add("Verpackung erneuert");
		serviceList.add("Artikel repariert");
		serviceList.add("Palette repariert");
		serviceList.add("Ware umsortiert");

		this.creator="DP";
	}	
	
	public Date getDate() {
		return date;
	}

	
	public String getRedirect() {
		return redirect;
	}

	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}

	public StoreView getStore() {
		return store;
	}

	public void setStore(StoreView store) {
		this.store = store;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String insertService() throws SQLException {
		
		FacesContext context = FacesContext.getCurrentInstance();
		String result2;	//result message of the given query
		Severity sev;
		//if (this.erfasser!=null&&this.artNr!=null&&this.mangel!=maengelListe.get(0)&&this.menge!=null) {
			ServiceData servdat = new ServiceData(this);
			if(servdat.insert()) {
					result2="Service-Leistung erfolgreich erfasst!";
					sev = FacesMessage.SEVERITY_INFO;
			}
			else {
					result2 = "Erfassung der Service-Leistung fehlgeschlagen!";
					sev = FacesMessage.SEVERITY_ERROR;
			}
		//}
		FacesMessage message = new FacesMessage(sev,result2,null);
		context.addMessage(serviceButton.getClientId(context), message);
		return result2;
	}

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

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getProductNr() {
		return productNr;
	}

	public void setProductNr(String productNr) {
		this.productNr = productNr;
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

	public UIComponent getServiceButton() {
		return serviceButton;
	}

	public void setServiceButton(UIComponent serviceButton) {
		this.serviceButton = serviceButton;
	}

	public List<String> getServiceList() {
		return serviceList;
	}

	public void setServiceList(List<String> serviceList) {
		this.serviceList = serviceList;
	}
	
	
}
