package de.hrw.dsalab.distsys.chat_front;

import java.io.IOException;
import javax.swing.JTextArea;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import com.google.gson.Gson;

import de.hrw.dsalab.distsys.chat.chat_back.Message;
import de.hrw.dsalab.distsys.chat_util.JsonUtil;

public class KeyboardListener implements InputListener {

	private JTextArea textArea;
	private String nick;
	private int port;

	public KeyboardListener(JTextArea textArea, String nick, int port) {
		this.textArea = textArea;
		this.nick = nick;
		this.port = port;

	}
	// sends message to the backend after user presses send button and appends the response to the textarea
	@Override
	public void inputReceived(String str) throws UnsupportedOperationException, IOException {
		String chatMessage = sendMessage(this.nick, str, JsonUtil.getValueString("backAdress"));
		textArea.append(chatMessage + System.getProperty("line.separator"));

	}
	// creating the message object, calling the backend and returning response message 
	public String sendMessage(String nick, String message, String adress) {
		Message newMessage = new Message(nick, message, this.port);
		String response = getPostResponse(adress, newMessage);
		Message messageResponse = new Gson().fromJson(response, Message.class);

		return messageResponse.getChatMessage();

	}
	// send a POST request with the client message to the backend 
	public String getPostResponse(String adress, Message newMessage) {
		try {
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpPost request = new HttpPost(adress);
			String body = new Gson().toJson(newMessage);
			StringEntity entity = new StringEntity(body);
			request.setEntity(entity);

			HttpResponse response = httpClient.execute(request);
			String jsonString = EntityUtils.toString(response.getEntity());

			return jsonString;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}