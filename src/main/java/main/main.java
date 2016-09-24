package Main;

import Server.Server;

/********************************
 * Main method. Runs the Server
 ********************************/
public class main {
	public static void main(String[] args) {
		System.out.println("running");
		Server server = new Server();
		server.start();
		System.out.println("done");
	}
}
