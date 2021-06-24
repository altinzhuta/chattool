package de.hrw.dsalab.distsys.chat.chat_back;

import com.google.gson.Gson;

import de.hrw.dsalab.distsys.chat_util.JsonUtil;
import spark.Request;
import static spark.Spark.port;
import static spark.Spark.post;

import java.io.IOException;
import java.util.HashMap;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

public class ChatRestApi {
	// clientList contains all logged in clients
	private HashMap<String, Integer> clientList;

	// ChatRestApi listen on {port} on route "/api/messages" for incoming chatMessages and on route "/api/login/:nick" for incoming logins
	public ChatRestApi(int port) {

		clientList = new HashMap<String, Integer>();
		port(port);
		post("/api/messages", (req, res) -> postMessage(req), JsonUtil.json());
		post("/api/login/:nick", (req, res) -> greetNick(req), JsonUtil.json());

	}

	// new message is created and broadCastet to all clients
	public Message postMessage(Request request) {
		Message message = new Gson().fromJson(request.body(), Message.class);
		broadCastUpdate(message, message.port);

		return message;
	}

	// handling of the initial calling (login) from a client
	public Message greetNick(Request request) {
		Message message = new Gson().fromJson(request.body(), Message.class);
		if (clientList.containsValue(message.port) == false
				&& message.chatMessage.endsWith(JsonUtil.getValueString("password"))) {
			clientList.put(message.nick, message.port);
		}
		message.chatMessage ="<"+ message.nick + "> just joined this Chat. Welcome "+message.nick+"!";
		return message;
	}
	// searching for all logged clients and sending each a POST request with the message 
	public void broadCastUpdate(Message message, int clientPort) {
		for (int port : clientList.values()) {
			if (port != clientPort) {
				try {
					HttpClient httpClient = HttpClientBuilder.create().build();
					HttpPost request = new HttpPost(String.format(JsonUtil.getValueString("frontAdress"), port));
					String body = new Gson().toJson(message);
					StringEntity entity = new StringEntity(body);
					request.setEntity(entity);
					httpClient.execute(request);

				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}

	}

}