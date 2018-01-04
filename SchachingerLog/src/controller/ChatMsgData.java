package controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ChatMsgData extends Data {

	private int id;
	private int chatId;
	private String text;
	private String user;
	private String date;
	private String time;
	private String to;
	//LocalDateTime time;
	
	public ChatMsgData(int chatId, String text, String date, String time) {
		super();
		this.text = text;
		this.user = "DP";	//TODO: userbean abgreifen!!
		this.date = date;
		this.time = time;

	}
	
	public ChatMsgData(int chatId, String text, String user) {
		super();
		this.chatId = chatId;
		this.text = text;
		this.user = user;
	}

	public ChatMsgData() {
		super();
	}
	//add new Message to Chat_Message
	public boolean insertMessage(int chatId) {
		if(chatId<1) chatId=this.chatId;
		if(!exists("SELECT * FROM Chat WHERE id='" + chatId + "'")) {
			logger.info("Chat mit chatId: " + chatId + " existiert nicht!");
			return false;
		}
		String getMaxIdMsg = "SELECT max(id) FROM Chat_Message WHERE chatId = '" + chatId + "'";
		int maxMsgId = getMaxId(getMaxIdMsg);	//get the Id for the current Aviso (Avisos can have more than one complaint)
		if (maxMsgId < 0) {return false;}
		else {this.id=maxMsgId+1;}
		String date = LocalDate.now().toString();
		String time = convertTime(LocalTime.now());
		String insertMsg="INSERT INTO Chat_Message (chatId, id, text, user, date, time) "
				+ "VALUES (" + chatId + ", " +  this.id + ", '" 
				+ this.text + "', '" + this.user + "', '" 
				+ date +"', '" + time + "')";
		this.date = date;
		this.time = time;
		return update(insertMsg);
	}
	
	public List<ChatMsgData> fetchHistory(int chatId) {
		String fetchHistory = "SELECT * FROM Chat_Message " + "WHERE chatId='" + chatId 
				+ "' ORDER BY date ASC, time ASC";
		List<ChatMsgData> history = new ArrayList<>();
		
		try {
			this.con = dbc.openConnection();
			this.con.setAutoCommit(false);
			Statement stmt = null;
			try {
				stmt = this.con.createStatement();
				ResultSet rs = stmt.executeQuery(fetchHistory);
				while (rs.next()) {
					ChatMsgData chatMsg = new ChatMsgData(this.chatId,rs.getString("text"),rs.getString("date"),rs.getString("time"));
					logger.info(rs.getString("text"));
					history.add(chatMsg);
				}
				logger.info(fetchHistory + " erfolgreich durchgeführt");
			} finally {
				try {
					stmt.close();
					this.con.commit();
					this.con.close();
				} catch (Exception ignore) {
					logger.warn("Connection couldn't be closed successfully");
				}
			}
		} catch (SQLException ex) {
			logger.warn("SQL ERROR: " + ex);
			try {
				this.con.rollback();
			} catch (Exception e) {
				logger.warn("Rollback didnt work");
			}
		}
		return history;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
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

	public int getChatId() {
		return chatId;
	}

	public String getTime() {
		return time;
	}

	public void setChatId(int chatId) {
		this.chatId = chatId;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
