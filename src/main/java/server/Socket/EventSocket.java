package server.Socket;

import event.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

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
		this.queue = queue;
	}

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
				terminate(); //test
			} finally {
				terminate();
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

	private void terminate() {
		try {
			inputReader.close();
			client.close();
			socket.close();
			queue.put(new ShutdownEvent());
		}catch (IOException ec ) {
			ec.printStackTrace();
		}
	}
}
