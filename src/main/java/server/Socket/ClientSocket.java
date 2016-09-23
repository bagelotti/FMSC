package server.Socket;

import User.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;

/*****************************************************
 * Listens in client port
 * stores all connections of users to send them events
 ******************************************************/
public class ClientSocket implements Runnable{
	private Socket client;
	private BufferedReader inputReader;
	private HashMap<Integer, User> connectedUsers;


	public ClientSocket(Socket client, HashMap<Integer, User> connectedUsers) {
		if(client == null || connectedUsers == null)
			throw new IllegalArgumentException("invalid parameter at client socket");

		this.client = client;
		this.connectedUsers = connectedUsers;
	}

	/*****************************************************************
	 * adds users into our connected users hash
	 * as they're coming in
	 * - if a user is already in the hash, then
	 *	he was an offline user ( a user that had an event related to him, but was offline i.e followEvent)
	 * 	and we add the outputstream to the user, to make them online
	 *****************************************************************/
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
				else {
					User user = new User(userID, client.getOutputStream());
					connectedUsers.put(user.getId(), user);
				}
			}

		} catch (IOException e) {
			if(inputReader != null)
				try {
					inputReader.close();
				} catch (IOException e1) {

				}
		}
	}
}
