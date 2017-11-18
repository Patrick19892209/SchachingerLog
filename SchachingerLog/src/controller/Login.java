package controller;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class Login {
	
	private String user;
	private String pw;
	
	public Login() {
		
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}
	
}
