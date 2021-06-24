package de.hrw.dsalab.distsys.chat_front;

import java.io.IOException;


public interface InputListener {
	
	public void inputReceived(String str) throws  UnsupportedOperationException, IOException;

}
