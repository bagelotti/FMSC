package server;

import event.Event;
import event.FollowEvent;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by ... on 9/17/16.
 */
public class EventWorker implements Runnable{
	private PriorityBlockingQueue<Event> queue;
	private int sequenceCounter;

	public EventWorker(PriorityBlockingQueue queue) {
		this.queue = queue;
		this.sequenceCounter = 1;
	}


	private void processEvent(Event event) {
		switch(event.getEventType()) {
			case "F":

				break;
			case "U":
				break;
			case "B":
				break;
			case "P":
				break;
			case "S":
				break;
			default:
				return;
		}
	}



	@Override
	public void run() {
		Event event;
		while(true) {
			if(!queue.isEmpty() && queue.peek().getSequenceNum() == sequenceCounter) {
				event = queue.poll();
				processEvent(event);
				sequenceCounter++;
			}

		}
	}


}
