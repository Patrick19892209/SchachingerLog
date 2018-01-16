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

import controller.ChatMsgData;
import controller.ClaimData;

@ManagedBean(name = "claim")
@ViewScoped
public class Claim {

	private String aviso;
	private int id;
	private String creator;
	private String productNr;
	private String to;
	private String amount;
	private String deficiency;
	private String deficiencyL;
	private UIComponent claimButton;
	private UIComponent inputDeficiency;
	private String date;
	private int chatId;
	private String text;
	private boolean done = false;
	private boolean showFileUpload = false;
	private boolean disableInput = false;
	private List<ChatMsgData> chatHistory;
	private List<String> deficiencyList;
	private static String user;
	@ManagedProperty(value = "#{login}")
	private Login login;
	private String redirect;
	@ManagedProperty(value = "#{storeView}")
	private StoreView store;

	@PostConstruct
	public void init() {
		if (store.isEntered() == true) {
			this.aviso = store.getDeliDone().getAviso();
			this.redirect = "lager";
		} else {
			this.redirect = "buero";
		}
		// this.creator = this.login.getUser().getAbbrevation();
		this.creator = login.getUser().getAbbrevation();
		Claim.user = login.getUser().getAbbrevation();
		// System.out.println("init: " + this.user);

	}

	public Claim() {

		// Dropdown opts
		deficiencyList = new ArrayList<>();
		deficiencyList.add("Artikel beschädigt");
		deficiencyList.add("Verpackung beschädigt");
		deficiencyList.add("Menge inkorrekt");
	}

	// inserts data from this Claim into Table Claim
	public String insertClaim() {

		FacesContext context = FacesContext.getCurrentInstance();
		Severity sev;

		if ((this.deficiency == "" || this.deficiency == null)
				&& (this.deficiencyL == "" || this.deficiencyL == null)) {
			sev = FacesMessage.SEVERITY_ERROR;
			FacesMessage msg = new FacesMessage(sev, "Mangel erfassen!", "");
			context.addMessage(this.inputDeficiency.getClientId(), msg);
			return "Fail";
		}

		String result; // result message of the given query
		if (this.deficiency == "" || this.deficiency == null)
			this.deficiency = this.deficiencyL;
		ClaimData claimDat = new ClaimData(this);
		this.chatId = claimDat.getChatId();
		if (claimDat.insert()) {
			result = "Reklamationserfassung erfolgreich abgeschlossen";
			sev = FacesMessage.SEVERITY_INFO;
			this.id = claimDat.getId();
			this.showFileUpload = true;
			this.disableInput = true;
		} else {
			result = "Reklamationserfassung fehlgeschlagen!";
			sev = FacesMessage.SEVERITY_ERROR;
		}
		FacesMessage message = new FacesMessage(sev, result, null);
		context.addMessage(claimButton.getClientId(context), message);
		return result;
	}

	public void fetchChatHistory() {
		ChatMsgData chatMsg = new ChatMsgData();
		this.chatHistory = chatMsg.fetchHistory(this.chatId);
	}

	public void insertChatMsg() {
		String result;
		Severity sev;
		if (this.text == null || this.text.length() < 1) {
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

	public void deleteClaim() {
		String result;
		Severity sev;
		ClaimData claimdat = new ClaimData(this);
		if (!claimdat.delete()) {
			result = "Löschen fehlgeschlagen!";
			sev = FacesMessage.SEVERITY_WARN;
		} else {
			result = "Eintrag gelöscht";
			sev = FacesMessage.SEVERITY_INFO;
		}

		FacesMessage msg = new FacesMessage(sev, result, "");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	// Message is written and value done from table Claim is refreshed
	public void addDoneMsg() {
		String msg = "Reklamation erledigt!";
		if (!this.done)
			msg = "Reklamation offen!";
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(msg));
		String query = "UPDATE Claim SET done=" + this.done + " WHERE aviso='" + this.aviso + "' AND id='" + this.id
				+ "'";

		// System.out.println(query);
		ClaimData claimdat = new ClaimData(this);
		claimdat.update(query);
	}

	// Getters and Setters

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

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		if (creator != null && to != "")
			creator = creator.toUpperCase();
		this.creator = creator;
	}

	public String getProductNr() {
		return productNr;
	}

	public void setProductNr(String productNr) {
		this.productNr = productNr;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		if (to != null && to != "")
			to = to.toUpperCase();
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

	public UIComponent getClaimButton() {
		return claimButton;
	}

	public void setClaimButton(UIComponent claimButton) {
		this.claimButton = claimButton;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getChatId() {
		return chatId;
	}

	public void setChatId(int chatId) {
		this.chatId = chatId;
	}

	public List<String> getDeficiencyList() {
		return deficiencyList;
	}

	public void setDeficiencyList(List<String> deficiencyList) {
		this.deficiencyList = deficiencyList;
	}

	public List<ChatMsgData> getChatHistory() {
		return chatHistory;
	}

	public void setChatHistory(List<ChatMsgData> chatHistory) {
		this.chatHistory = chatHistory;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isShowFileUpload() {
		return showFileUpload;
	}

	public void setShowFileUpload(boolean showFileUpload) {
		this.showFileUpload = showFileUpload;
	}

	public boolean isDisableInput() {
		return disableInput;
	}

	public void setDisableInput(boolean disableInput) {
		this.disableInput = disableInput;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {

		this.done = done;

	}

	public String getDeficiencyL() {
		return deficiencyL;
	}

	public void setDeficiencyL(String deficiencyL) {
		this.deficiencyL = deficiencyL;
	}

	public UIComponent getInputDeficiency() {
		return inputDeficiency;
	}

	public void setInputDeficiency(UIComponent inputDeficiency) {
		this.inputDeficiency = inputDeficiency;
	}

	public Login getLogin() {
		return login;
	}

	public void setLogin(Login login) {
		this.login = login;
	}

	public static String getUser() {
		return user;
	}

	public static void setUser(String user) {
		Claim.user = user;
	}

}