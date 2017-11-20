package view;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import controller.LoginData;


@ManagedBean(name="log")
@SessionScoped
public class Login {
	private Logger logger = (Logger) LoggerFactory.getLogger("view.Login");
    private String username;  
    private String password;
    private LoginData data;
    private String name;
    
    
    private boolean loggedIn = false;
    
	public boolean isLoggedIn() {
		return loggedIn;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getName() {
		return data.getName();
	}
	public void setName(String name) {
		this.name = username;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	

	public String login() {
        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage message = null;
        data = new LoginData(username, password);
        
        String answer = null;
        if(username != null && password != null){
        	switch (data.getRole(username, password)) {
    		case 1: //admin
    			loggedIn = true;
                logger.info(username + " logged in");
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome", username);
                answer = "admin";
    			break;
    		case 2: //Lager
    			loggedIn = true;
                logger.info(username + " logged in");
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome", username);
                answer = "lager";
    			break;
    		case 3: //Büro
    			loggedIn = true;
                logger.info(username + " logged in");
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome", username);
                answer = "büro";
    			break;

    		default:
    			loggedIn = false;
                logger.warn(username + " tried to log in and failed (wrong user or pw)");
                message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Loggin Error", "Invalid credentials");
                answer = "wrong login";
    			break;
    		}

        }
    
        FacesContext.getCurrentInstance().addMessage(null, message);
        context.addCallbackParam("loggedIn", loggedIn);
        context.addCallbackParam("username", username);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("username", username);
        System.out.println(answer);
        return answer;

	}
	
	public String logout() {
		logger.info(username + " logging out");
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Logout", username)
		);
		username = null;
		password = null;
		return "logout";
		
	}
	
	public String validate(){
		if (this.username == null || !this.loggedIn) return "logout";
		return "stay";
	}

	
	
}
