package view;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
//import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import controller.ServiceData;

@ManagedBean(name="service")
@ViewScoped
public class Service {

	private String aviso;
	private String erfasser;
	private String artNr;
	private String menge;
	private String service;
	private UIComponent serviceButton;
	//private ReklaData redat;
	List <String> serviceListe;
	
	public Service() {

		//Dropdown opts
		serviceListe = new ArrayList<>();
		serviceListe.add("Verpackung erneuert");
		serviceListe.add("Artikel repariert");
		serviceListe.add("Palette repariert");
		serviceListe.add("Ware umsortiert");

		this.erfasser="DP";
		
	}

	public List<String> getServiceListe() {
		return serviceListe;
	}

	public void setServiceListe(List<String> serviceListe) {
		this.serviceListe = serviceListe;
	}

	public List<String> getMaengelListe() {
		return serviceListe;
	}

	public void setMaengelListe(List<String> maengelListe) {
		this.serviceListe = maengelListe;
	}

	public String insertService() throws SQLException {
		
		FacesContext context = FacesContext.getCurrentInstance();
		String result;	//result message of the given query
		Severity sev;
		//if (this.erfasser!=null&&this.artNr!=null&&this.mangel!=maengelListe.get(0)&&this.menge!=null) {
			System.out.println(this.menge);
			ServiceData servdat = new ServiceData(this.aviso, this.erfasser, this.artNr, this.menge, this.service);
			if(servdat.insert()) {
					result="Service-Leistung erfolgreich erfasst!";
					sev = FacesMessage.SEVERITY_INFO;
			}
			else {
					result = "Erfassung der Service-Leistung fehlgeschlagen!";
					sev = FacesMessage.SEVERITY_ERROR;
			}
		//}
		FacesMessage message = new FacesMessage(sev,result,null);
		context.addMessage(serviceButton.getClientId(context), message);
		return result;
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

	public String getErfasser() {
		return erfasser;
	}

	public void setErfasser(String erfasser) {
		this.erfasser = erfasser;
	}

	public String getAviso() {
		return aviso;
	}

	public void setAviso(String aviso) {
		this.aviso = aviso;
	}

	public String getActorA() {
		return erfasser;
	}

	public void setActorA(String erfasser) {
		this.erfasser = erfasser;
	}

	public String getArtNr() {
		return artNr;
	}

	public void setArtNr(String artNr) {
		this.artNr = artNr;
	}

	public String getMenge() {
		return menge;
	}

	public void setMenge(String menge) {
		this.menge = menge;
	}
	
}
