package etna_java.ahmed.etape2;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import etna_java.ahmed.etape1.FileMessage;
import etna_java.ahmed.etape1.Message;
import etna_java.ahmed.etape1.TextMessage;
import etna_java.ahmed.etape3.MessageHandler;
import etna_java.ahmed.etape3.ServerState;

public class Server implements MessageHandler {

	private Socket socket;
	private ServerSocket socketserver;
	private ServerState state;

	public static void main(String[] args) {
		Server server = new Server();
		server.clientConnected();
		server.recieveMessage(server.getSocket());
		// etape3
		server.recieveFile();
		//etape4
		server.recieveTextMessgae();
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

	public void recieveFile() {
		try {
			ServerSocket socketserver = new ServerSocket(2019);
			Socket socket = socketserver.accept();
			InputStream in = socket.getInputStream();
			OutputStream out = new FileOutputStream(
					"C:\\\\Users\\\\Utilisateur\\\\eclipse-workspace\\\\etna_java\\\\src\\\\etna_java\\\\file-recevied+"
							+ new Date().getTime() + ".txt");
			byte[] bytes = new byte[16 * 1024];
			int count;
			while ((count = in.read(bytes)) > 0) {
				out.write(bytes, 0, count);
			}
			out.close();
			in.close();
			socket.close();
			socketserver.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void recieveTextMessgae() {
		try {
			ServerSocket socketserver = new ServerSocket(2020);
			Socket socket = socketserver.accept();
			ObjectInputStream objIn = new ObjectInputStream(socket.getInputStream());
			Object recievedObject = objIn.readObject();
			if (recievedObject instanceof TextMessage) {
				TextMessage tm = (TextMessage)recievedObject;
				String content = tm.getContent();
				System.out.println(content);
				//here implement the logic based on the value of tm.getContent()
				//example :get_time
				if("get_time".equals(content)) {
					PrintWriter out = new PrintWriter(socket.getOutputStream());
					out.println("Server says : "+new Date());
					out.flush();
				}
			}
			objIn.close();
			//socket.close();
			//socketserver.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Socket getSocket() {
		return socket;
	}

	public ServerState getServerState(Message message) {
		if (message instanceof TextMessage) {
			return ServerState.TEXT_MESSAGE;
		}
		if (message instanceof FileMessage) {
			return ServerState.FILE_MESSGAE;
		}
		return ServerState.DOWN;
	}

	@Override
	public ServerState handleMessage(String message) {
		if ("shutdown".equalsIgnoreCase(message)) {
			try {
				socketserver.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			state = ServerState.DOWN;
		}
		if ("send_file".equalsIgnoreCase(message)) {
			state = ServerState.FILE_MESSGAE;
		}
		return state;
	}

}
