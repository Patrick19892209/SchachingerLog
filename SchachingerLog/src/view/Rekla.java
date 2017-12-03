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

import controller.ReklaData;

@ManagedBean(name="rekla")
@ViewScoped
public class Rekla {

	private String aviso;
	private String erfasser;
	private String artNr;
	private String an;
	private String menge;
	private String mangel;
	private UIComponent reklaButton;
	//private ReklaData redat;
	List <String> maengelListe;
	
	public Rekla() {

		//Dropdown opts
		maengelListe = new ArrayList<>();
		//maengelListe.add("Mangel erfassen...");	
		maengelListe.add("Artikel beschädigt");
		maengelListe.add("Verpackung beschädigt");
		maengelListe.add("Menge inkorrekt");
		this.erfasser="DP";
		
	}

	public List<String> getMaengelListe() {
		return maengelListe;
	}

	public void setMaengelListe(List<String> maengelListe) {
		this.maengelListe = maengelListe;
	}

	public String insertRekla() throws SQLException {
		
		FacesContext context = FacesContext.getCurrentInstance();
		String result;	//result message of the given query
		Severity sev;
		//if (this.erfasser!=null&&this.artNr!=null&&this.mangel!=maengelListe.get(0)&&this.menge!=null) {
			ReklaData redat = new ReklaData(this.aviso, this.erfasser, this.artNr, this.an, this.menge, this.mangel);
			if (redat.avisoExists(this.aviso)) {	//Complaint is only inserted if Aviso is existing in db
				if(redat.insert()) {
					result="Reklamationserfassung erfolgreich abgeschlossen";
					sev = FacesMessage.SEVERITY_INFO;
				}
				else {
					result = "Reklamationserfassung fehlgeschlagen!";
					sev = FacesMessage.SEVERITY_ERROR;
				}
				
			}
			else {
				result = "Kein gültiges Aviso!";
				sev = FacesMessage.SEVERITY_ERROR;
			}
		//}
		FacesMessage message = new FacesMessage(sev,result,null);
		context.addMessage(reklaButton.getClientId(context), message);
		return result;
	}
	public UIComponent getReklaButton() {
		return reklaButton;
	}

	public void setReklaButton(UIComponent reklaButton) {
		this.reklaButton = reklaButton;
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

	public String getActorB() {
		return an;
	}

	public void setActorB(String an) {
		this.an = an;
	}

	public String getArtNr() {
		return artNr;
	}

	public void setArtNr(String artNr) {
		this.artNr = artNr;
	}

	public String getAn() {
		return an;
	}

	public void setAn(String an) {
		this.an = an;
	}

	public String getMenge() {
		return menge;
	}

	public void setMenge(String menge) {
		this.menge = menge;
	}

	public String getMangel() {
		return mangel;
	}

	public void setMangel(String mangel) {
		this.mangel = mangel;
	};
	
	
	
}
