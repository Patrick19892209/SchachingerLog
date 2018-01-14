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

@ManagedBean(name = "service")
@ViewScoped
public class Service {

	private String aviso;
	private int id;
	private String creator;
	private String productNr;
	private String amount;
	private String service;
	private String serviceL;
	private Date date;
	private UIComponent serviceButton;
	private UIComponent inputService;
	List<String> serviceList;
	private String redirect;
	@ManagedProperty(value = "#{storeView}")
	StoreView store;
	@ManagedProperty(value = "#{login}")
	private Login login;

	@PostConstruct
	public void init() {
		if (store.isEntered() == true) {
			this.aviso = store.getDeliDone().getAviso();
			this.redirect = "lager";
		} else {
			this.redirect = "buero";
		}
		this.creator = this.login.getUser().getAbbrevation();
	}

	public Service() {
		// Dropdown opts
		serviceList = new ArrayList<>();
		serviceList.add("Verpackung erneuert");
		serviceList.add("Artikel repariert");
		serviceList.add("Palette repariert");
		serviceList.add("Ware umsortiert");

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
		String result; // result message of the given query
		Severity sev;
		if ((this.service == "" || this.service == null) && (this.serviceL == "" || this.serviceL == null)) {
			sev = FacesMessage.SEVERITY_ERROR;
			FacesMessage msg = new FacesMessage(sev, "Mangel erfassen!", "");
			context.addMessage(this.inputService.getClientId(), msg);
			System.out.println("Hallo");
			return "Fail";
		}
		if(this.service==""||this.service==null) this.service=this.serviceL;
		ServiceData servdat = new ServiceData(this);
		if (servdat.insert()) {
			result = "Service-Leistung erfolgreich erfasst!";
			sev = FacesMessage.SEVERITY_INFO;
		} else {
			result = "Erfassung der Service-Leistung fehlgeschlagen!";
			sev = FacesMessage.SEVERITY_ERROR;
		}
		FacesMessage message = new FacesMessage(sev, result, null);
		context.addMessage(serviceButton.getClientId(context), message);
		return result;
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

	public String getServiceL() {
		return serviceL;
	}

	public void setServiceL(String serviceL) {
		this.serviceL = serviceL;
	}

	public UIComponent getInputService() {
		return inputService;
	}

	public void setInputService(UIComponent inputService) {
		this.inputService = inputService;
	}

	public Login getLogin() {
		return login;
	}

	public void setLogin(Login login) {
		this.login = login;
	}

}
