package Module.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.*;

public class SocketClient extends Thread {
	JTextPane obj;
	Socket client;
	BufferedReader reader;
	PrintWriter writer;

	public SocketClient(JTextPane obj) {
		this.obj = obj;
		try {
			client = new Socket("127.0.0.1", 1228);
			format("成功连接服务器");
			reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
			writer = new PrintWriter(client.getOutputStream(), true);

		} catch (IOException e) {
			format("无法连接服务器");
			format(e.toString());
			e.printStackTrace();
		}
		this.start();
	}

	public void run() {
		String messages = "";
		while (true) {
			try {
				messages = reader.readLine();
			} catch (IOException e) {
				format("失去连接");

				break;
			}
			if (messages != null && messages.trim() != "") {
				format(messages);
			}
		}
	}

	public void sendMessages(String content) {
		try {
			writer.println(content);
		} catch (Exception e) {
			format(e.toString());
		}
	}

	public void format(String s) {
		if (s != null) {
			//this.ui.taShow.setText(this.ui.taShow.getText() + s + "\n");
			//this.obj.setText(t);
			System.out.println(s + "\n");
		}
	}
	
	public void closeClient() {
		try {
			if (client != null){
				client.close();
			}
				
			if (reader != null){
				reader.close();
			}
				
			if (writer != null){
				writer.close();
			}

		} catch (IOException e) {
		}

	}
	
}