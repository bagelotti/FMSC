package main;

import server.Server;

/********************************
 * Main method. Runs the server
 ********************************/
public class main {
	public static void main(String[] args) {
		Server server = new Server();
		server.start();
	}
}
