package view;

import java.time.LocalDateTime;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.component.datatable.DataTable;

import controller.ChatMsgData;

@ManagedBean
@ViewScoped
public class Chat {

	private int id;
	private int chatId;
	private String text;
	private String user;
	private String date;
	private String time;
	private String to;
	private String history;
	private DataTable dt;
	
	public Chat() {
	}
	
	public Chat(int id, int chatId, String text, String user, String date, String time, String to, String history) {
		super();
		this.id = id;
		this.chatId = chatId;
		this.text = text;
		this.user = user;
		this.date = date;
		this.time = time;
		this.to = to;
		this.history = history;
	}
	
    public void insertChatMsg() {
    	String result;
    	Severity sev;
    	if(text==null||text.length()<1) {
				result="Keine Nachricht erfasst!";
				sev = FacesMessage.SEVERITY_INFO;
		}
		else {
				Claim claim = (Claim) this.dt.getRowData();
				ChatMsgData chatMsg = new ChatMsgData();
				chatMsg.setText(this.text);
				chatMsg.insertMessage(claim.getChatId());
				result = "Reklamationserfassung fehlgeschlagen!";
				sev = FacesMessage.SEVERITY_ERROR;
		}

        FacesMessage msg = new FacesMessage(sev,result,"");
        FacesContext.getCurrentInstance().addMessage(null, msg);

    }
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getChatId() {
		return chatId;
	}
	public void setChatId(int chatId) {
		this.chatId = chatId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getHistory() {
		return history;
	}
	public void setHistory(String history) {
		this.history = history;
	}
	public DataTable getDt() {
		return dt;
	}
	public void setDt(DataTable dt) {
		this.dt = dt;
	}
	
}