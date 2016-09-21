package event;

import User.User;

import java.util.HashMap;

/**
 * Created by ... on 9/19/16.
 */
public class FollowEvent implements Event, Comparable<Event> {
	private final String type = "F";
	private final int sequenceNum;
	private final String message;

	private final int fromUser;
	private final int toUser;

	public FollowEvent(String sequenceNum, String fromUser, String toUser, String message) {
		try {
			this.sequenceNum = Integer.parseInt(sequenceNum);
			this.fromUser = Integer.parseInt(fromUser);
			this.toUser = Integer.parseInt(toUser);
			this.message = message;
		} catch(NumberFormatException e) {
			throw new NumberFormatException();
		}

	}


	public int getSequenceNum() {
		return sequenceNum;
	}

	public String getEventType() { return type; }

	public String getMessage() { return message; }

	public int getFromUser() { return fromUser; }

	public int getToUser() { return toUser; }

	@Override
	public int compareTo(Event o) {
		return Integer.compare(this.getSequenceNum(),o.getSequenceNum());
	}
}
