package server;

import User.User;
import event.Event;
import server.Socket.ClientSocket;
import server.Socket.EventSocket;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

/***************************
 * Server that runs all of our multithreading
 *****************************/
public class Server {
	private final int NTHREADS = Runtime.getRuntime().availableProcessors();
	private final int clientPort = 9099;

	private ExecutorService executorService;
	private PriorityBlockingQueue<Event> eventQueue;
	private HashMap<Integer, User> usersConnected;
	private ServerSocket clientSocket;

	public Server() {
		this.executorService = Executors.newFixedThreadPool(NTHREADS);
		this.eventQueue = new PriorityBlockingQueue<>();
		this.usersConnected = new HashMap<>();
	}

	/**************************************************************************************
	 * - starts a thread to listen in for incoming events and enqueues them by sequence num
	 * - starts a consumer thread that pops off events from queue
	 * - the remaining threads available in the thread pool are used to connect all our clients as
	 * 		fast as possible
	 **************************************************************************************/
	public void start() {
		executorService.execute(new EventSocket(eventQueue));
		executorService.execute(new EventProcessRunnable(eventQueue, usersConnected));
		try {
			startClient();
		} catch (IOException e) {
			shutdown();
		}
	}

	/******************************************************************************************************
	 * - starts multiple threads (up to thread pool size) to connect incoming clients
	 * @throws IOException: thrown when all clients are connected and we don't receive any incoming request
	 * within the timeout time
	 ******************************************************************************************************/
	public void startClient() throws IOException{
			clientSocket = new ServerSocket(clientPort);
			clientSocket.setSoTimeout(10000);
			while(true) {
				Socket client = clientSocket.accept();
				executorService.execute(new ClientSocket(client, usersConnected));
			}
	}

	/***************************************************
	 * - calls safe termination of ExecutorService
	 * - waits until all current Runnables are done processing
	 * on Excception: will force shutdown
	 ***************************************************/
	private void shutdown() {
		try {
			executorService.shutdown();
			executorService.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
		} catch (InterruptedException e1) {
			executorService.shutdownNow();
		}
	}


}

