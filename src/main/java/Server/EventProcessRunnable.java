package Server;

import User.User;
import Event.*;
import java.util.HashMap;
import java.util.concurrent.PriorityBlockingQueue;
/****************************************************************
 * Runnable that works as a consumer for Event queue
 * processes the Event, and sends it to the appropriate user(s)
 ****************************************************************/
public class EventProcessRunnable implements Runnable{
	private PriorityBlockingQueue<Event> queue;
	private int sequenceCounter;
	private boolean foundLastEvent;
	HashMap<Integer, User> connectedUsers;


	public EventProcessRunnable(PriorityBlockingQueue<Event> queue, HashMap<Integer, User> connectedUsers) {
		if(queue == null || connectedUsers == null)
			throw new IllegalArgumentException("null params in Event process runnable");
		this.queue = queue;
		this.connectedUsers = connectedUsers;
		this.sequenceCounter = 1;
		this.foundLastEvent = false;
	}

	/***********************************************
	 * sends incoming Event to process by its type
	 * @param event - next Event to send out
	 ***********************************************/
	private void processEvent(Event event) {
		switch(event.getEventType()) {
			case "F":
				processFollow((FollowEvent) event);
				break;
			case "U":
				processUnfollow((UnfollowEvent) event);
				break;

			case "B":
				processBroadcast((BroadcastEvent) event);
				break;

			case "P":
				processPrivateMsg((PrivateMsgEvent) event);
				break;

			case "S":
				processStatusUpdate((StatusUpdateEvent) event);
				break;

			case "X":
				// Poison pill Event, signal that we've reached all events and should terminate
				foundLastEvent = true;
				break;
			default:
				return;
		}
	}

	/********************************************
	 * Processes follow Event
	 * @param event - Follow Event
	 ********************************************/
	private void processFollow(FollowEvent event) {
		User followee = getUser(event.getFromUser());
		User whoToFollow = getUser(event.getToUser());
		followee.followAnotherUser(whoToFollow);
		whoToFollow.followMe(followee);
		whoToFollow.notify(event.getMessage());
	}

	/********************************************
	 * Processes unfollow Event
	 * @param event - Unfollow Event
	 ********************************************/
	private void processUnfollow(UnfollowEvent event) {
		User followee = getUser(event.getFromUser());
		User whoToUnfollow = getUser(event.getToUser());
		whoToUnfollow.unfollowMe(followee);
	}

	/********************************************
	 * Processes broadcast Event
	 * @param event - Broadcast Event
	 ********************************************/
	private void processBroadcast(BroadcastEvent event) {
		for(User user : connectedUsers.values()) {
			user.notify(event.getMessage());
		}
	}

	/********************************************
	 * Processes private message Event
	 * @param event - Private Message Event
	 ********************************************/
	private void processPrivateMsg(PrivateMsgEvent event) {
		User whoToFollow = getUser(event.getToUser());
		whoToFollow.notify(event.getMessage());
	}

	/********************************************
	 * Processes status update Event
	 * @param event - StatusUpdate Event
	 ********************************************/
	private void processStatusUpdate(StatusUpdateEvent event) {
		User updatedUser = getUser(event.getFromUser());
		for(User user : updatedUser.getFollowers()) {
			user.notify(event.getMessage());
		}
	}

	/********************************************
	 * returns a user that is currently connected
	 * creates an "offline" user if we receive an Event
	 * that effects a user that is currently not connected.
	 * We need offline users to store followers and other relative info
	 * @param userID - ID of user
	 ********************************************/
	private User getUser(int userID) {
		synchronized (connectedUsers) {
			if(connectedUsers.containsKey(userID))
				return connectedUsers.get(userID);
			connectedUsers.put(userID, new User(userID));
			return connectedUsers.get(userID);
		}
	}

	/******************************************
	 * Main method of Runnable
	 * - pops off root of Event queue when the next ordered Event is at the root
	 * - will wait if root element is not the next Event to be processed
	 * - when last Event is found (terminating Event) and processed, we prepare to shut down the service,
	 * 		and disconnect the users
	 ******************************************/
	@Override
	public void run() {
		Event event;
		while(!foundLastEvent) {
			if(!queue.isEmpty() && (queue.peek().getSequenceNum() == sequenceCounter ||
					queue.peek() instanceof ShutdownEvent)) {
				event = queue.poll();
				processEvent(event);
				sequenceCounter++;
			}

		}

		//all events are processed, disconnect user connections
		for(User user : connectedUsers.values()) {
			user.disconnect();
		}
	}
}
