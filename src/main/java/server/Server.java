package server;

import User.User;
import event.Event;


import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by .. on 9/17/16.
 */
public class Server {
	private EventExecutor eventExecutor;
	private ClientExecutor clientExecutor;
	private PriorityBlockingQueue<Event> eventQueue;
	private HashMap<Integer, User> usersConnected;

	public Server() {
		this.eventQueue = new PriorityBlockingQueue<>();
		this.usersConnected = new HashMap<>();
		this.eventExecutor = new EventExecutor(eventQueue);
		this.clientExecutor = new ClientExecutor(usersConnected);

	}

	public void start() {
		eventExecutor.start();
		clientExecutor.start();
	}

}

