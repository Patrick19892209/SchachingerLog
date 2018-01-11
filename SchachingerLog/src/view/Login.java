package view;


import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.context.RequestContext;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import controller.LoginData;
import model.User;


@ManagedBean
@SessionScoped
public class Login {
	private Logger logger = (Logger) LoggerFactory.getLogger("view.Login");
	private User user;
	private LoginData data;
    private boolean loggedIn = false;
    
	@PostConstruct
	public void init() {
		data = new LoginData();
		user = new User();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public String login() {
        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage message = null;
        String answer = null;
        if(user.getAbbrevation() != null && user.getPassword() != null){
        	switch (data.getRole(user)) {
    		case 1: //admin
    			loggedIn = true;
                logger.info(user.getName() + " logged in");
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome", user.getName());
                answer = "admin";
                user.setRole("admin");
                user.setHomepage("../admin/WelcomeAdmin.xhtml");
    			break;
    		case 2: //Lager
    			loggedIn = true;
                logger.info(user.getName() + " logged in");
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome", user.getName());
                answer = "lager";
                user.setRole("lager");
                user.setHomepage("../lager/WelcomeLager.xhtml");
    			break;
    		case 3: //buero
    			loggedIn = true;
                logger.info(user.getName() + " logged in");
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome", user.getName());
                answer = "buero";
                user.setRole("buero");
                user.setHomepage("../b�ro/WelcomeB�ro.xhtml");
    			break;
    		case -1:
    			message = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Keine Connection :( So ein Mist aber auch!",null);
    			answer ="Connection problem";
    			System.out.println("Login con prob");
    			break;
    		default:
    			loggedIn = false;
                logger.warn(user.getName() + " tried to log in and failed (wrong user or pw)");
                message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Loggin Error", "Invalid credentials");
                answer = "wrong login";
    			break;
    		}

        }
    
        FacesContext.getCurrentInstance().addMessage(null, message);
        context.addCallbackParam("loggedIn", loggedIn);
        context.addCallbackParam("username", user.getName());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("username", user.getName());
        return answer;

	}
	
	public String logout() {
		logger.info(user.getName() + " logging out");
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Logout", user.getName())
		);
		user.setName(null);
		user.setPassword(null);
		return "logout";
		
	}
	
	public String validate(){
		
		if (user.getName() == null || !this.loggedIn) return "logout";
		return "stay";
	}

	
}
