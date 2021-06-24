package de.hrw.dsalab.distsys.chat_front;

import de.hrw.dsalab.distsys.chat.chat_back.Message;

public interface NetworkListener {
	
	public Message messageReceived(String message);
}
