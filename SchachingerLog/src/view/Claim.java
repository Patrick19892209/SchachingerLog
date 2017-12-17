package view;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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

import controller.ClaimData;

@ManagedBean(name="claim")
@ViewScoped
public class Claim {

	private String aviso;
	private int id;
	private String creator;
	private String productNr;
	private String to;
	private String amount;
	private String deficiency;
	private UIComponent claimButton;
	private String date;

	//private ClaimData redat;
	List <String> deficiencyList;
	private String redirect;
    @ManagedProperty(value="#{storeView}")
    StoreView store;
	
	@PostConstruct
	public void init() {
		if (store != null) {
			this.aviso = store.getDeliDone().getAviso();
			this.redirect = "lager";
		} else {
			this.redirect = "buero";
		}
		
	}

	public Claim() {

		//Dropdown opts
		deficiencyList = new ArrayList<>();
		deficiencyList.add("Artikel beschädigt");
		deficiencyList.add("Verpackung beschädigt");
		deficiencyList.add("Menge inkorrekt");
		this.creator="DP";
		
	}
	
	public Claim(String aviso, String creator, String date, String productNr, String to, String amount, String deficiency,
			Timestamp ts) {
		super();
		this.aviso = aviso;
		this.creator = creator;
		this.productNr = productNr;
		this.to = to;
		this.amount = amount;
		this.deficiency = deficiency;
		this.date = date;
		deficiencyList = new ArrayList<>();
		deficiencyList.add("Artikel beschädigt");
		deficiencyList.add("Verpackung beschädigt");
		deficiencyList.add("amount inkorrekt");
	}
	
	//inserts data from this Claim into Table Claim
	public String insertClaim() throws SQLException {
		
		FacesContext context = FacesContext.getCurrentInstance();
		String result;	//result message of the given query
		Severity sev;
		//if (this.creator!=null&&this.productNr!=null&&this.deficiency!=deficiencyList.get(0)&&this.amount!=null) {
			ClaimData redat = new ClaimData(this);
			if(redat.insert()) {
					result="Reklamationserfassung erfolgreich abgeschlossen";
					sev = FacesMessage.SEVERITY_INFO;
			}
			else {
					result = "Reklamationserfassung fehlgeschlagen!";
					sev = FacesMessage.SEVERITY_ERROR;
			}
		//}
		FacesMessage message = new FacesMessage(sev,result,null);
		context.addMessage(claimButton.getClientId(context), message);
		return result;
	}
	
	//converts Timestamp to String e.g. 2017-12-24
	public String convertTsToDate(Timestamp ts) {
		Date date = new Date(ts.getTime());
  	  	SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
  	  	return format1.format(date);	
	}
	
	//Getters and Setters
	
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

	public List<String> getDeficiencyList() {
		return deficiencyList;
	}

	public void setDeficiencyList(List<String> deficiencyList) {
		this.deficiencyList = deficiencyList;
	}	
}