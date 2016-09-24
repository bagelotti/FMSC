package Server.Socket;

import Event.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.PriorityBlockingQueue;

/*******************************
 * Listens in on Event port
 *******************************/
public class EventSocket implements Runnable {

	private final int port = 9090;
	private ServerSocket socket;
	private Socket client;
	private BufferedReader inputReader;
	private PriorityBlockingQueue<Event> queue;
	public EventSocket(PriorityBlockingQueue<Event> queue) {
		if(queue == null)
			throw new IllegalArgumentException("Empty queue passed into Event socket");
		this.queue = queue;
	}


	/***********************************************
	 * - takes in stream from Event port
	 * - parses each line into a new Event
	 * - enqueue's into priority queue
	 * - priority is based on sequence num.
	 *     Smaller sequence numbers get pushed up the queue
	 ***********************************************/
	@Override
	public void run() {
		try {
			socket = new ServerSocket(port);
			socket.setSoTimeout(30000);
			client = socket.accept();
			inputReader = new BufferedReader((new InputStreamReader(client.getInputStream())));
			String payload = inputReader.readLine();

			while(payload != null) {
				String[] content = payload.split("\\|");
				//parse payload
				Event event = parsePayloadToEvent(content, payload);
				queue.put(event);
				payload = inputReader.readLine();
			}

		} catch (IOException e) {
				// just terminate
		} finally {
				terminate();
		}

	}

	/*************************************************
	 * Parses incoming payload string into an Event
	 * @param payload - Incoming string array parsed from request
	 * @param message - original message from request
	 * @return - Event object to enqueue
	 **************************************************/
	private Event parsePayloadToEvent(String[] payload, String message) {
		if(payload == null || payload.length == 0)
			throw new IllegalArgumentException("empty payload");

		switch(payload[1]) {
			case "F":
				return new FollowEvent(payload[0],payload[2],payload[3], message);

			case "U":
				return new UnfollowEvent(payload[0],payload[2],payload[3], message);

			case "B":
				return new BroadcastEvent(payload[0], message);

			case "P":
				return new PrivateMsgEvent(payload[0],payload[2],payload[3], message);

			case "S":
				return new StatusUpdateEvent(payload[0],payload[2], message);
			default:
				throw new IllegalArgumentException("invalid Event type");
		}
	}

	/**********************************************************
	 * Safely shutsdown our sockets, and
	 * adds the terminate Event into queue (poison pill pattern)
	 **********************************************************/
	private void terminate() {
		try {
			if(inputReader != null)
				inputReader.close();
			if(client != null)
				client.close();
			socket.close();
			queue.put(new ShutdownEvent());
		}catch (IOException ec ) {
			ec.printStackTrace();
		}
	}
}
