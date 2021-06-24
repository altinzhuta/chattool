package de.hrw.dsalab.distsys.chat.chat_back;

public class Message {
	String nick;
	String message;
	String chatMessage;
	int port;

	public Message(String nick, String message, int port) {
		this.nick = nick;
		this.message = message;
		this.chatMessage ="<"+nick +">  : " + message;
		this.port = port;

	}

	public String getChatMessage() {
		return this.chatMessage;
	}

}
