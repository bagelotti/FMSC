package server;

import User.User;
import event.*;
import java.util.HashMap;
import java.util.concurrent.PriorityBlockingQueue;
/****************************************************************
 * Runnable that works as a consumer for event queue
 * processes the event, and sends it to the appropriate user(s)
 ****************************************************************/
public class EventProcessRunnable implements Runnable{
	private PriorityBlockingQueue<Event> queue;
	private int sequenceCounter;
	private boolean foundLastEvent;
	HashMap<Integer, User> connectedUsers;

	public EventProcessRunnable(PriorityBlockingQueue queue, HashMap<Integer, User> connectedUsers) {
		this.queue = queue;
		this.connectedUsers = connectedUsers;
		this.sequenceCounter = 1;
		this.foundLastEvent = false;
	}

	/***********************************************
	 * sends incoming event to process by its type
	 * @param event - next event to send out
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
				// Poison pill event, signal that we've reached all events and should terminate
				foundLastEvent = true;
				break;
			default:
				return;
		}
	}

	/********************************************
	 * Processes follow event
	 * @param event - Follow event
	 ********************************************/
	private void processFollow(FollowEvent event) {
		User followee = getUser(event.getFromUser());
		User whoToFollow = getUser(event.getToUser());
		followee.followAnotherUser(whoToFollow);
		whoToFollow.followMe(followee);
		whoToFollow.notify(event.getMessage());
	}

	/********************************************
	 * Processes unfollow event
	 * @param event - Unfollow event
	 ********************************************/
	private void processUnfollow(UnfollowEvent event) {
		User followee = getUser(event.getFromUser());
		User whoToUnfollow = getUser(event.getToUser());
		whoToUnfollow.unfollowMe(followee);
	}

	/********************************************
	 * Processes broadcast event
	 * @param event - Broadcast event
	 ********************************************/
	private void processBroadcast(BroadcastEvent event) {
		for(User user : connectedUsers.values()) {
			user.notify(event.getMessage());
		}
	}

	/********************************************
	 * Processes private message event
	 * @param event - Private Message event
	 ********************************************/
	private void processPrivateMsg(PrivateMsgEvent event) {
		User whoToFollow = getUser(event.getToUser());
		whoToFollow.notify(event.getMessage());
	}

	/********************************************
	 * Processes status update event
	 * @param event - StatusUpdate event
	 ********************************************/
	private void processStatusUpdate(StatusUpdateEvent event) {
		User updatedUser = getUser(event.getFromUser());
		for(User user : updatedUser.getFollowers()) {
			user.notify(event.getMessage());
		}
	}

	/********************************************
	 * returns a user that is currently connected
	 * creates an "offline" user if we receive an event
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
	 * - pops off root of event queue when the next ordered event is at the root
	 * - will wait if root element is not the next event to be processed
	 * - when last event is found (terminating event) and processed, we prepare to shut down the service,
	 * 		and disconnect the users
	 ******************************************/
	@Override
	public void run() {
		Event event;
		while(!foundLastEvent) {
			if(!queue.isEmpty() && queue.peek().getSequenceNum() == sequenceCounter ||
					queue.peek() instanceof ShutdownEvent) {
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
