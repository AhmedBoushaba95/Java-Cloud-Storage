package etna_java.myclasses;

import java.net.Socket;

public interface MessageHandler {

	ServerState handleMessage(String message);
	ServerState handleMessage(Socket socket, String message);
}
