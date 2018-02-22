package etna_java.myclasses;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	private Socket socket;
	
	public static void main(String[] args) {
		Client client = new Client();
		client.sendMessage("my_first_message");
		client.recieveMessage(client.getSocket());
	}
	
	public Client() {
		try {
			socket = new Socket(InetAddress.getLocalHost(), 2018);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(String msg) {
		try {
			OutputStream outstream = socket.getOutputStream();
			PrintWriter out = new PrintWriter(outstream);
			out.println(msg);
			out.flush();
		}  catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void recieveMessage(Socket socket)  {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String messageReceived = in.readLine();
			System.out.println(messageReceived);
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Socket getSocket() {
		return socket;
	}

	
}
