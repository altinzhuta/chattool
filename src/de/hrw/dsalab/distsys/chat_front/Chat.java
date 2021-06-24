package de.hrw.dsalab.distsys.chat_front;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import de.hrw.dsalab.distsys.chat.chat_back.ChatRestApi;
import de.hrw.dsalab.distsys.chat_util.JsonUtil;

public class Chat extends JFrame {

	private static final long serialVersionUID = 1L;

	private InputListener inputListener;
	private NetworkListener networkListener;
	private String nick;
	private int clientport;

	public Chat(int port) {
		this.clientport = port;
		JPanel mainPanel;
		setTitle("Chat Tool v0.0.65.3");
		setSize(900, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		nick = retrieveNickName();
		mainPanel = setupChatView();
		getContentPane().add(mainPanel);
		getContentPane().getParent().invalidate();
		getContentPane().validate();
	}

	private JPanel setupChatView() {
		JPanel panel = new JPanel();
		JPanel southPanel = new JPanel();
		JTextArea textArea = new JTextArea();
		final JTextField textField = new JTextField();
		JButton sendButton = new JButton("Send");

		textField.setColumns(60);
		sendButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					inputListener.inputReceived(textField.getText());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				textField.setText("");
			}

		});

		textArea.setBackground(Color.LIGHT_GRAY);
		textArea.setEditable(false);

		southPanel.setLayout(new FlowLayout());
		southPanel.add(textField);
		southPanel.add(sendButton);

		panel.setLayout(new BorderLayout());
		panel.add(textArea, BorderLayout.CENTER);
		panel.add(southPanel, BorderLayout.SOUTH);

		// KeybooardListener sends text input as message to the backend
		inputListener = new KeyboardListener(textArea, nick, clientport);

		// ClientRestServer listening on clientport for messages
		networkListener = new RestListener(textArea, nick, clientport);
		return panel;
	}

	private String retrieveNickName() {
		return (String) JOptionPane.showInputDialog(this, "Enter your nickname please:", "Enter nickname",
				JOptionPane.QUESTION_MESSAGE);
	}

	public static void main(String[] args) {
		// starting backend and two clients with port parameters from config.json file
		
		// ports are--> backend : 4000 ; first client : 4001 ; second client : 4002
		
		// for more clients adjust config.json or simply add chat instances (in case of
		// error check maxThreadPool in the RestListener constructor and adjust
		// accordingly)

		ChatRestApi chatRestApi = new ChatRestApi(JsonUtil.geValueInt("chatBackendPort"));
		Chat chat1 = new Chat(JsonUtil.geValueInt("firstChatclientPort"));
		Chat chat2 = new Chat(JsonUtil.geValueInt("secondChatclientPort"));

	}
}
