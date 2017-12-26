package controller;

public class ChatMsgData extends Data {

	int id;
	int chatId;
	String text;
	String user;
	String date;
	String time;
	String to;
	//LocalDateTime time;

	public ChatMsgData(int chatId, String msg, String date, String time) {
		super();
		this.text = msg;
		this.user = "DP";	//TODO: userbean abgreifen!!
		this.date = date;
		this.time = time;

	}
	
	public ChatMsgData() {
		super();
	}
	//add new Message to Chat_Message
	public boolean insertMessage(int chatId) {
		if(!exists("SELECT * FROM Chat WHERE id='" + chatId + "'")) {
			logger.info("Chat mit chatId: " + chatId + " existiert nicht!");
			//return false;
		}
		String getMaxIdMsg = "SELECT max(id) FROM Chat_Message WHERE chatId = '" + chatId + "'";
		int maxMsgId = getMaxId(getMaxIdMsg);	//get the Id for the current Aviso (Avisos can have more than one complaint)
		if (maxMsgId < 0) {return false;}
		else {this.id=maxMsgId+1;}
		String insertClaim="INSERT INTO Chat_Message (chatId, id, text, user, date, time) "
				+ "VALUES (" + chatId + ", " +  this.id + ", '" 
				+ this.text + "', '" + this.user + "', '" 
				+ this.date +"', '" + this.time.toString() + "')";
				System.out.println("Query: " + insertClaim);
		return insert(insertClaim);
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

}
