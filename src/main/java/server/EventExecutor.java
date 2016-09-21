package server;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

import User.User;
import event.Event;
import server.Socket.EventSocket;
/**
 * Created by anon on 9/17/16.
 */
public class EventExecutor {
	//get max number of processors to initialize our thread cnt
	private final int NTHREADS = Runtime.getRuntime().availableProcessors();
	private ExecutorService executorService;
	private ExecutorService workerExecutorService;

	private PriorityBlockingQueue eventQueue;
	private HashMap<Integer, User> connectedUsers;

	public EventExecutor(PriorityBlockingQueue<Event> eventQueue, HashMap<Integer, User> connectedUsers) {
		this.executorService = Executors.newSingleThreadExecutor();
		this.workerExecutorService = Executors.newSingleThreadExecutor();

		this.eventQueue = eventQueue;
		this.connectedUsers = connectedUsers;
	}

	public void start() {
		executorService.execute(new EventSocket(eventQueue));
		workerExecutorService.execute(new EventWorker(eventQueue, connectedUsers));
	}
}
