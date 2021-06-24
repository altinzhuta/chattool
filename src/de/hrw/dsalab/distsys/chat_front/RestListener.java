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
import spark.Service;

public class RestListener implements NetworkListener {

	private JTextArea textArea;

   // RestListener creates one spark server instance per client with a maximum threadpool
	public RestListener(JTextArea textArea, String nick, int clientPort) {
		this.textArea = textArea;
		Service client = Service.ignite().port(clientPort).threadPool(JsonUtil.geValueInt("sparkThreadPoolNum"));
		
		// only route ist POST to "localhost:{clientport}/client/message" and returns the message to be updated
		client.post("/client/message", (req, res) -> messageReceived(req.body()), JsonUtil.json());
		
		// greetBackend is calling the backend using Post and serves as a login
		greetBackend(nick, clientPort);
	}
	
	// converts jsonString to java <Message> class and appends the new message to the textArea
	@Override
	public Message messageReceived(String body) {
		Message message = new Gson().fromJson(body, Message.class);
		this.textArea.append(message.getChatMessage() + System.getProperty("line.separator"));
		return message;

	}
	// send post request with "password" body for evaluation
	public void greetBackend(String nick, int clientPort) {
		String greetMessage = "";
		try {
			Message message = new Message(nick, JsonUtil.getValueString("password"), clientPort);
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpPost greetRequest = new HttpPost(String.format(JsonUtil.getValueString("loginAdress"), nick));

			String body = new Gson().toJson(message);
			StringEntity entity = new StringEntity(body);
			greetRequest.setEntity(entity);
			HttpResponse response = httpClient.execute(greetRequest);
			greetMessage = EntityUtils.toString(response.getEntity());

		} catch (IOException e) {
			e.printStackTrace();
		}

		messageReceived(greetMessage);
	}

}
