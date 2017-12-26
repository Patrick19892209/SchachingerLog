package view;

import java.time.LocalDateTime;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class Chat {

	int id;
	String history;
	String msg;
	String user;
	String to;
	LocalDateTime time;
	
	public Chat(int id, String history, String msg, String user, String to, String reklaAviso, String reklaId,
			LocalDateTime time) {
		super();
		this.id = id;
		this.history = history;
		this.msg = msg;
		this.user = user;
		this.to = to;
		this.time = time;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getHistory() {
		return history;
	}

	public void setHistory(String history) {
		this.history = history;
	}

	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public LocalDateTime getTime() {
		return time;
	}
	public void setTime(LocalDateTime time) {
		this.time = time;
	}
	
	
}
