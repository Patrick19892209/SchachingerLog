package view;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;

import model.Data;
import model.ServiceData;

@ManagedBean(name = "dtServices")
@ViewScoped
public class ServiceTable extends Data implements Serializable {

	private static final long serialVersionUID = 4138649614383680255L;
	
	private List<Service> services;
	private List<Service> filteredServices;
	@ManagedProperty(value="#{login}") 
	private Login login;

	@PostConstruct
	public void init() {
		ServiceData servicedat = new ServiceData();
		this.services = servicedat.fetchServices(this.login.getUser().getAbbrevation());
	}

	// Reklas aus Datenbank holen und ReklaListe füllen
	public void onRowEdit(RowEditEvent event) {
		String msgString1 = "Service Edited";
		Service service = (Service) event.getObject();
		String msgString2 = service.getAviso() + "/" + service.getId();
		ServiceData servdat = new ServiceData(service);
		if (!servdat.update())
			msgString1 = "Editing failed";
		FacesMessage msg = new FacesMessage(msgString1, msgString2);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void onRowCancel(RowEditEvent event) {
		FacesMessage msg = new FacesMessage("Edit Cancelled", ((Service) event.getObject()).getAviso());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void onCellEdit(CellEditEvent event) {
		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();

		if (newValue != null && !newValue.equals(oldValue)) {

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed",
					"Old: " + oldValue + ", New:" + newValue);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public void deleteService(Service service) {
		String msgString1 = "Reklamation gelöscht";
		String msgString2 = service.getAviso() + "/" + service.getId();
		ServiceData servicedat = new ServiceData(service);
		if (!servicedat.delete()) {
			msgString1 = "Löschen fehlgeschlagen!";
		}
		else {
			services.remove(service);
		}
		FacesMessage msg = new FacesMessage(msgString1, msgString2);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}


	/*public Login getLogin() {
		return login;
	}

	public void setLogin(Login login) {
		this.login = login;
	}
	 */
	public List<Service> getServices() {
		return services;
	}

	public List<Service> getFilteredServices() {
		return filteredServices;
	}

	public void setServices(List<Service> services) {
		this.services = services;
	}

	public void setFilteredServices(List<Service> filteredServices) {
		this.filteredServices = filteredServices;
	}

	public Login getLogin() {
		return login;
	}

	public void setLogin(Login login) {
		this.login = login;
	}

}
