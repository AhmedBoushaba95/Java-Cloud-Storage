package etna_java.ahmed.etape2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import etna_java.ahmed.etape1.TextMessage;

public class Client {

	private Socket socket;
	private Socket socket2020;

	public static void main(String[] args) {
		Client client = new Client();
		client.sendMessage("I am Ahmed Boushaba the BOSS !");
		client.recieveMessage(client.getSocket());
		//etape3
		client.sendFile();
		//etape4
		client.sendTextMessage(new TextMessage("get_time"));
		//client.recieveResultFromTextMessage(client.getSocket2020());
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

	public void sendTextMessage(TextMessage message) {
		try {
			socket2020 = new Socket(InetAddress.getLocalHost(), 2020);
			ObjectOutputStream objOutstream = new ObjectOutputStream(socket2020.getOutputStream());
			objOutstream.writeObject(message);
			objOutstream.flush();
			objOutstream.close();
			//socket2020.close();
		}  catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void recieveResultFromTextMessage(Socket socket)  {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String messageReceived = in.readLine();
			System.out.println(messageReceived);
			//socket2020.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendFile() {
		try {
			Socket socket = new Socket(InetAddress.getLocalHost(), 2019);
			File file = new File("C:\\Users\\Utilisateur\\eclipse-workspace\\etna_java\\src\\etna_java\\file01.txt");
	        byte[] bytes = new byte[16 * 1024];
	        InputStream in = new FileInputStream(file);
	        OutputStream out = socket.getOutputStream();
	        int count;
	        while ((count = in.read(bytes)) > 0) {
	            out.write(bytes, 0, count);
	        }
	        out.close();
	        in.close();
	        socket.close();
		}  catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Socket getSocket() {
		return socket;
	}

	public Socket getSocket2020() {
		return socket2020;
	}
}
