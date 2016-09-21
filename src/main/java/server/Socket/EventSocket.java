package server.Socket;

import event.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by ... on 9/17/16.
 */
public class EventSocket implements Runnable {

	private final int port = 9090;
	private ServerSocket socket;
	private Socket client;
	private BufferedReader inputReader;
	private PriorityBlockingQueue<Event> queue;

	public EventSocket(PriorityBlockingQueue<Event> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		try {
			socket = new ServerSocket(port);
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
				System.out.println("Failure listening in on Event port");
				e.printStackTrace();
			}

	}


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
				throw new IllegalArgumentException("invalid event type");
		}
	}


}
