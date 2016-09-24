package Event;

/********************
 * Unfollow Event
 ********************/
public class UnfollowEvent implements Event, Comparable<Event> {
	private final String type = "U";
	private final int sequenceNum;
	private final String message;
	private final int fromUser;
	private final int toUser;

	public UnfollowEvent(String sequenceNum, String fromUser, String toUser, String message) {
		if(message == null)
			throw new NullPointerException();

		try {
			this.sequenceNum = Integer.parseInt(sequenceNum);
			this.fromUser = Integer.parseInt(fromUser);
			this.toUser = Integer.parseInt(toUser);
		} catch(NumberFormatException e) {
			throw new NumberFormatException();
		}

		this.message = message;
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
		return Integer.compare(this.getSequenceNum(), o.getSequenceNum());
	}
}
