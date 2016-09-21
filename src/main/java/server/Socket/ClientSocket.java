package server.Socket;

import User.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by ... on 9/17/16.
 */
public class ClientSocket implements Runnable{

	private Socket client;
	private BufferedReader inputReader;
	private HashMap<Integer, User> connectedUsers;

	public ClientSocket(Socket client, HashMap<Integer, User> connectedUsers) {
		this.client = client;
		this.connectedUsers = connectedUsers;
	}

	@Override
	public void run() {

		try {
			inputReader = new BufferedReader((new InputStreamReader(client.getInputStream())));
			String userID = inputReader.readLine();
			synchronized (connectedUsers){
				if(connectedUsers.containsKey(Integer.valueOf(userID))) {
					User user = connectedUsers.get(Integer.valueOf(userID));
					user.addConnection(client.getOutputStream());
					connectedUsers.replace(Integer.valueOf(userID), user);
				}

				User user = new User(userID, client.getOutputStream());
				connectedUsers.put(user.getId(), user);
			}

		} catch (IOException e) {
			System.out.println("Failure listening in on Client port");
			e.printStackTrace();
		}
	}
}
