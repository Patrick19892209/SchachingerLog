package model;

public class User {

	//Variables
	private String user;
	private String password;
	private String name;
	private String location;
	private String role;
	private String abbrevation;
	private String homepage;
	private String redirect;

	
	// Getters and Setters
	public String getAbbrevation() {
		return abbrevation;
	}
	
	public void setAbbrevation(String abbrevation) {
		this.abbrevation = abbrevation;
	}
	
	public String getUser() {
		return user;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public String getRedirect() {
		return redirect;
	}

	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}

	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String full_name) {
		this.name = full_name;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}

	
}
