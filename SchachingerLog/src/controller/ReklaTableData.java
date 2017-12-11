package controller;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;

import view.Rekla;
 
@ManagedBean(name="dtReklas")
@ViewScoped
public class ReklaTableData extends Data implements Serializable {
    
    /**
	 * 
	 */
	//private static final long serialVersionUID = 1L; ?

	private List<Rekla> reklas;
         
   // @ManagedProperty("#{carService}")
    private Rekla rekla;
    @PostConstruct
    public void init() {
        ReklaData rekdat = new ReklaData();
    	reklas = rekdat.fetchReklas();
    }
 	
	//Reklas aus Datenbank holen und ReklaListe füllen
    public List<Rekla> getReklas() {
		return reklas;
	}
	public void setReklas(List<Rekla> reklas) {
		this.reklas = reklas;
	}
	public Rekla getRekla() {
		return rekla;
	}
	public void setRekla(Rekla rekla) {
		this.rekla = rekla;
	}

    public void onRowEdit(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Rekla Edited", ((Rekla) event.getObject()).getAviso());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
     
    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled", ((Rekla) event.getObject()).getAviso());
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
}