package view;

import java.util.ArrayList;
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

import model.ChatMsgData;
import model.ServiceData;

@ManagedBean(name = "service")
@ViewScoped
public class Service {

	private String aviso;
	private int id;
	private String creator;
	private String productNr;
	private String amount;
	private double price;
	private String service;
	private String serviceL;
	private String date;
	private int chatId;
	private String text;
	private boolean done;
	private UIComponent serviceButton;
	private UIComponent inputService;
	private List<ChatMsgData> chatHistory;
	private List<String> serviceList;
	private static String user;

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
		Service.user = login.getUser().getAbbrevation();
	}

	public Service() {
		// Dropdown opts
		serviceList = new ArrayList<>();
		serviceList.add("Verpackung erneuert");
		serviceList.add("Artikel repariert");
		serviceList.add("Palette repariert");
		serviceList.add("Ware umsortiert");

	}


	public String insertService() {

		FacesContext context = FacesContext.getCurrentInstance();
		Severity sev;
		if ((this.service == "" || this.service == null) && (this.serviceL == "" || this.serviceL == null)) {
			sev = FacesMessage.SEVERITY_ERROR;
			FacesMessage msg = new FacesMessage(sev, "Service erfassen!", "");
			context.addMessage(this.inputService.getClientId(), msg);
			return "Fail";
		}

		String result; // result message of the given query
		if (this.service == "" || this.service == null)
			this.service = this.serviceL;
		ServiceData servdat = new ServiceData(this);
		this.chatId = servdat.getChatId();
		if (servdat.insert()) {
			result = "Service-Leistung erfolgreich erfasst!";
			sev = FacesMessage.SEVERITY_INFO;
			this.id = servdat.getId();
		} else {
			result = "Erfassung der Service-Leistung fehlgeschlagen!";
			sev = FacesMessage.SEVERITY_ERROR;
		}
		FacesMessage message = new FacesMessage(sev, result, null);
		context.addMessage(serviceButton.getClientId(context), message);
		return result;
	}

	public void fetchChatHistory() {
		ChatMsgData chatMsg = new ChatMsgData();
		this.chatHistory = chatMsg.fetchHistory(this.chatId);
	}

	public void insertChatMsg() {
		String result;
		Severity sev;
		if (text == null || text.length() < 1) {
			result = "Keine Nachricht erfasst!";
			sev = FacesMessage.SEVERITY_WARN;
		} else {
			// System.out.println("Claim login: " + this.login.getUser().getAbbrevation());
			// // + "\nloginuser: " + this.store.login.getUser().getAbbrevation());
			ChatMsgData chatMsg = new ChatMsgData(this.chatId, this.text, this.user);
			if (chatMsg.insertMessage(this.chatId)) {
				result = "Nachricht erfasst!";
				sev = FacesMessage.SEVERITY_INFO;
				this.chatHistory.add(chatMsg);
			} else {
				result = "Nachricht konnte nicht erfasst!";
				sev = FacesMessage.SEVERITY_WARN;
			}
		}

		FacesMessage msg = new FacesMessage(sev, result, "");
		FacesContext.getCurrentInstance().addMessage(null, msg);

	}

	public void deleteService() {
		String result;
		Severity sev;
		ServiceData servicedat = new ServiceData(this);
		if (!servicedat.delete()) {
			result = "Löschen fehlgeschlagen!";
			sev = FacesMessage.SEVERITY_WARN;
		} else {
			result = "Eintrag gelöscht";
			sev = FacesMessage.SEVERITY_INFO;
		}

		FacesMessage msg = new FacesMessage(sev, result, "");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void addDoneMsg() {
		String msg = "Servicefall erledigt!";
		if (!this.done)
			msg = "Servicefall offen!";
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(msg));
		String query = "UPDATE Additional_Service SET done=" + this.done + " WHERE aviso='" + this.aviso + "' AND id='"
				+ this.id + "'";

		// System.out.println(query);
		ServiceData servdat = new ServiceData(this);
		servdat.update(query);
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

	public int getChatId() {
		return chatId;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDate() {
		return date;
	}

	public void setChatId(int chatId) {
		this.chatId = chatId;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public String getText() {
		return text;
	}

	public List<ChatMsgData> getChatHistory() {
		return chatHistory;
	}

	public static String getUser() {
		return user;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setChatHistory(List<ChatMsgData> chatHistory) {
		this.chatHistory = chatHistory;
	}

	public static void setUser(String user) {
		Service.user = user;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
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
}
