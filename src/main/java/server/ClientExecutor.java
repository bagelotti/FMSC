package server;

import User.User;
import server.Socket.ClientSocket;

import javax.jws.soap.SOAPBinding;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ... on 9/17/16.
 */
public class ClientExecutor {
	private final int port = 9099;
	private ServerSocket socket;
	private ExecutorService executorService;
	private HashMap<Integer, User> connectedUsers;

	public ClientExecutor(HashMap<Integer, User> connectedUsers) {
		this.executorService = Executors.newSingleThreadExecutor();
		this.connectedUsers = connectedUsers;
	}

	public void start() {
		try {
			socket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			while(true) {
				Socket client = socket.accept();
				executorService.execute(new ClientSocket(client, connectedUsers));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}


	}
}
