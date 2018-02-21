package etna_java.ahmed.etape2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
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
	private ServerState state=ServerState.TEXT_MESSAGE;
	private boolean shouldStop;
	private int nbrMsg;
	private static final String FILES_DIR = "C:\\Users\\Utilisateur\\eclipse-workspace\\etna_java\\src\\etna_java\\files_recevied";

	public static void main(String[] args) {
		Server server = new Server();
		server.clientConnected();
		server.recieveMessage(server.getSocket());
		// etape3
		// server.recieveFile();
		// etape4
		// server.recieveTextMessgae();
		// etape4 command line
		while (true) {
			server.setNbrMsg(server.getNbrMsg() + 1);
			server.recieveTextMessage();
			if (server.isShouldStop()) {
				break;
			}

		}
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
					"C:\\Users\\Utilisateur\\eclipse-workspace\\etna_java\\src\\etna_java\\files_recevied\\file-recevied-"
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

	public void recieveTextMessage() {
		try {
			ServerSocket socketserver = new ServerSocket(2020);
			Socket socket = socketserver.accept();
			ObjectInputStream objIn = new ObjectInputStream(socket.getInputStream());
			Object recievedObject = objIn.readObject();
			if (recievedObject instanceof TextMessage) {
				TextMessage tm = (TextMessage) recievedObject;
				String content = tm.getContent();
				//System.out.println(content);
				// here implement the logic based on the value of tm.getContent()
				// example :get_time
				handleMessage(socket, content);
				if (state.equals(ServerState.TEXT_MESSAGE)) {
					if ("get_time".equals(content)) {
						PrintWriter out = new PrintWriter(socket.getOutputStream());
						out.println("Server says : " + new Date());
						out.flush();
					}
					if ("get_nb_received_txt_messages".equals(content)) {
						PrintWriter out = new PrintWriter(socket.getOutputStream());
						out.println("Server says : I had recieve : " + nbrMsg + " message !");
						out.flush();
					}
					if ("get_files_names".equals(content)) {
						PrintWriter out = new PrintWriter(socket.getOutputStream());
						out.println("Server says : I had all those file : " + getFilesName(FILES_DIR));
						out.flush();
					}
					if (content.contains("download_file")) {
						sendFileToClient(socket,extractFilePath(content));
					}

				} else {
					// server is ready to receive file
					// here the tm is file path
					System.out.println("here ");
					recieveFile();
				}

				if ("stop".equals(content)) {
					System.exit(0);
				}
				if ("shut_down".equals(content)) {
					socket.close();
					socketserver.close();
					shouldStop = true;
				}
			}
			objIn.close();
			socket.close();
			socketserver.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private String extractFilePath(String content) {
		return FILES_DIR+File.separator+content.split("\\+")[1].trim().replace("\"", "");
	}

	public void sendFileToClient(Socket socket,String filePath) {
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				return;
			}
	        byte[] bytes = new byte[16 * 1024];
	        InputStream in = new FileInputStream(file);
	        OutputStream out = socket.getOutputStream();
	        int count;
	        while ((count = in.read(bytes)) > 0) {
	            out.write(bytes, 0, count);
	        }
	        in.close();
		}  catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getFilesName(String filesDir) {
		File folder = new File(filesDir);
		File[] listOfFiles = folder.listFiles();
		String res = "";
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				res += listOfFiles[i].getName() + "|||";
			}
		}
		return res;
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

	@Override
	public ServerState handleMessage(Socket socket, String message) {
		if ("shutdown".equalsIgnoreCase(message)) {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			state = ServerState.DOWN;
		}

		if ("receive_file".equalsIgnoreCase(message)) {
			state = ServerState.FILE_MESSGAE;
		} else if (state.equals(ServerState.FILE_MESSGAE) && message.contains("\\")) {
			state = ServerState.FILE_MESSGAE;
		} else {
			state = ServerState.TEXT_MESSAGE;
		}
		return state;
	}

	public boolean isShouldStop() {
		return shouldStop;
	}

	public int getNbrMsg() {
		return nbrMsg;
	}

	public void setNbrMsg(int nbrMsg) {
		this.nbrMsg = nbrMsg;
	}

}
