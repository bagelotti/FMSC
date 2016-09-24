package Event;

/************************
 * Status Update Event
 ************************/
public class StatusUpdateEvent implements Event, Comparable<Event> {
	private final String type = "S";
	private final int sequenceNum;
	private final int fromUser;
	private final String message;

	public StatusUpdateEvent(String sequenceNum, String fromUser, String message) {
		if(message == null)
			throw new NullPointerException();

		try {
			this.sequenceNum = Integer.parseInt(sequenceNum);
			this.fromUser = Integer.parseInt(fromUser);
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


	@Override
	public int compareTo(Event o) {
		return Integer.compare(this.getSequenceNum(), o.getSequenceNum());
	}
}
