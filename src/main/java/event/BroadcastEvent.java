package event;

/**
 * Created by ... on 9/19/16.
 */
public class BroadcastEvent implements Event, Comparable<Event>{
	private final String type = "B";
	private final int sequenceNum;
	private final String message;

	public BroadcastEvent(String sequenceNum, String message) {
		try {
			this.sequenceNum = Integer.parseInt(sequenceNum);
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

	@Override
	public int compareTo(Event o) {
		return Integer.compare(this.getSequenceNum(), o.getSequenceNum());
	}
}
