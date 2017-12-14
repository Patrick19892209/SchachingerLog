package view;


import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;

import controller.Data;
import controller.ClaimData;
import view.Claim;
 
@ManagedBean(name="dtClaims")
@ViewScoped
public class ClaimTable extends Data implements Serializable {
    
	private static final long serialVersionUID = 8725748769196865593L;

	private List<Claim> claims;
	private List<Claim> filteredClaims;
	   // @ManagedProperty("#{carService}")
	    private Claim claim;

		@PostConstruct
	    public void init() {
	        ClaimData claimdat = new ClaimData();
	    	this.claims = claimdat.fetchClaims();
	    }
	 	
		//Reklas aus Datenbank holen und ReklaListe füllen
	    public void onRowEdit(RowEditEvent event) {
	        String msgString1 = "Claim Edited"; 
	        String msgString2 = ((Claim) event.getObject()).getAviso() + "/" + ((Claim) event.getObject()).getId();
	    	Claim claim = (Claim) event.getObject();
	    	System.out.print(claim.getDate());
	    	ClaimData claimdata = new ClaimData(claim);
	        if(claimdata.update()) msgString1 = "Editing failed"; 
	        FacesMessage msg = new FacesMessage(msgString1, msgString2);
	        FacesContext.getCurrentInstance().addMessage(null, msg);
	    }
	     
	    public void onRowCancel(RowEditEvent event) {
	        FacesMessage msg = new FacesMessage("Edit Cancelled", ((Claim) event.getObject()).getAviso());
	        FacesContext.getCurrentInstance().addMessage(null, msg);
	    }
	     
	    public void onCellEdit(CellEditEvent event) {
	        Object oldValue = event.getOldValue();
	        Object newValue = event.getNewValue();
	         
	        if(newValue != null && !newValue.equals(oldValue)) {
	        	
	            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
	            FacesContext.getCurrentInstance().addMessage(null, msg);
	        }
	    }

		public Claim getClaim() {
			return claim;
		}

		public void setClaim(Claim claim) {
			this.claim = claim;
		}
		
	    public List<Claim> getClaims() {
			return claims;
		}
		
	    public void setClaims(List<Claim> claims) {
			this.claims = claims;
		}

		public List<Claim> getFilteredClaims() {
			return filteredClaims;
		}

		public void setFilteredClaims(List<Claim> filteredClaims) {
			this.filteredClaims = filteredClaims;
		}
	       
    
}