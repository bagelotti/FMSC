package server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

import event.Event;
import server.Socket.EventSocket;
/**
 * Created by anon on 9/17/16.
 */
public class EventExecutor {
	//get max number of processors to initialize our thread cnt
	private ExecutorService executorService;
	private ExecutorService workerExecutorService;

	private EventWorker eventConsumer;
	private PriorityBlockingQueue eventQueue;

	public EventExecutor(PriorityBlockingQueue<Event> eventQueue) {
		this.executorService = Executors.newSingleThreadExecutor();
		this.workerExecutorService = Executors.newSingleThreadExecutor();
		this.eventQueue = eventQueue;
		this.eventConsumer = new EventWorker(eventQueue);
	}

	public void start() {
		executorService.execute(new EventSocket(eventQueue));
		workerExecutorService.execute(new EventWorker(eventQueue));
	}
}
