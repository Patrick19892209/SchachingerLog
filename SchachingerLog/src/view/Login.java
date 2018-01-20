package view;


import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import model.LoginData;
import model.User;


@ManagedBean
@SessionScoped
public class Login {

	//Variables
	private User user;
	private LoginData data;
    private boolean loggedIn = false;
    private final String out = "../login.xhtml";
    private RequestContext context = RequestContext.getCurrentInstance();
    private FacesMessage message = null;
    
    
	@PostConstruct
	public void init() {
		data = new LoginData();
		user = new User();
	}

	// Getters and Setters
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getOut() {
		return out;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	/**
	 * check user and password
	 * also the the other user variables will be set
	 * @return answer  // page to navigate to 
	 */
	public String login() {
        String answer = null;
        if(user.getAbbrevation() != null && user.getPassword() != null){
        	switch (data.getRole(user)) {
    		case 1: //admin
    			loggedIn = true;
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome", user.getName());
                answer = "admin";
                user.setRole("admin");
                user.setHomepage("../admin/WelcomeAdmin.xhtml");
    			break;
    		case 2: //Lager
    			loggedIn = true;
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome", user.getName());
                answer = "lager";
                user.setRole("lager");
                user.setHomepage("../lager/WelcomeLager.xhtml");
    			break;
    		case 3: //buero
    			loggedIn = true;
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome", user.getName());
                answer = "buero";
                user.setRole("buero");
                user.setHomepage("../buero/WelcomeBuero.xhtml");
    			break;
    		case -1:
    			message = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Keine Connection :( So ein Mist aber auch!",null);
    			answer ="Connection problem";
    			break;
    		default:
    			loggedIn = false;
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
	
	/**
	 * set user to null and return to login page
	 * @return loginpage
	 */
	public String logout() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Logout", user.getName())
		);
		user.setName(null);
		user.setPassword(null);
		return "../login.xhtml";
		
	}

	/**
	 * check if logged in
	 * @return logout/stay
	 */
	public String validate(){
		if (user.getName() == null || !this.loggedIn) return "logout";
		return "stay";
	}

	
}
