package server;

import User.User;
import event.*;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by ... on 9/17/16.
 */
public class EventWorker implements Runnable{
	private PriorityBlockingQueue<Event> queue;
	private int sequenceCounter;
	HashMap<Integer, User> connectedUsers;

	public EventWorker(PriorityBlockingQueue queue, HashMap<Integer, User> connectedUsers) {
		this.queue = queue;
		this.connectedUsers = connectedUsers;
		this.sequenceCounter = 1;
	}


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

			default:
				return;
		}
	}


	private void processFollow(FollowEvent event) {
		User followee = getUser(event.getFromUser());
		User whoToFollow = getUser(event.getToUser());
		followee.followAnotherUser(whoToFollow);
		whoToFollow.followMe(followee);
		whoToFollow.notify(event.getMessage());
	}

	private void processUnfollow(UnfollowEvent event) {
		User followee = getUser(event.getFromUser());
		User whoToUnfollow = getUser(event.getToUser());
		whoToUnfollow.unfollowMe(followee);
	}

	private void processBroadcast(BroadcastEvent event) {
		for(User user : connectedUsers.values()) {
			user.notify(event.getMessage());
		}
	}

	private void processPrivateMsg(PrivateMsgEvent event) {
		User followee = getUser(event.getFromUser());
		User whoToFollow = getUser(event.getToUser());
		whoToFollow.notify(event.getMessage());
	}

	private void processStatusUpdate(StatusUpdateEvent event) {
		User updatedUser = getUser(event.getFromUser());
		for(User user : updatedUser.getFollowers()) {
			user.notify(event.getMessage());
		}
	}


	/*
	 create an offline user if we do not have any record of them
	 */
	private User getUser(int userID) {
		if(connectedUsers.containsKey(userID))
				return connectedUsers.get(userID);
		connectedUsers.put(userID, new User(userID));
		return connectedUsers.get(userID);
	}

	@Override
	public void run() {
		Event event;
		Timer timer = new Timer();
		timer.schedule(new printSize(queue), 0, 5000);
		while(true) {

			if(!queue.isEmpty() && queue.peek().getSequenceNum() == sequenceCounter) {
				event = queue.poll();
				processEvent(event);
				sequenceCounter++;
			}

		}
	}


	class printSize extends TimerTask {
		PriorityBlockingQueue<Event> queue;
		public printSize(PriorityBlockingQueue<Event> queue) {
			this.queue = queue;
		}
		@Override
		public void run() {
			System.out.println(queue.size());
			System.out.println("users: " + connectedUsers.size());
		}
	}

}
