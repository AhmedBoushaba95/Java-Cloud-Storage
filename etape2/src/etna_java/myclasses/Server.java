package etna_java.myclasses;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	private Socket socket;
	private ServerSocket socketserver;

	public static void main(String[] args) {
		Server server = new Server();
		server.clientConnected();
		server.recieveMessage(server.getSocket());
	}

	public Socket clientConnected() {
		try {
			socketserver = new ServerSocket(2018);
			socket = socketserver.accept();
			PrintWriter out = new PrintWriter(socket.getOutputStream());
			out.println("Server says : A client is connected !");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return socket;
	}

	public void recieveMessage(Socket socket) {
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
