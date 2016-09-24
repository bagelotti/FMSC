package Event;

/**************************************************************************************
 * Special Event that is only used to enqueue.
 * Once the consumer grabs this Event, we know that we have processed all of our events
 **************************************************************************************/
public class ShutdownEvent implements Event,Comparable<Event> {
	private final String type = "X";
	public ShutdownEvent(){}

	@Override
	public int getSequenceNum() {
		return Integer.MAX_VALUE;
	}

	@Override
	public String getEventType() {
		return type;
	}

	@Override
	public String getMessage() {
		return null;
	}

	@Override
	public int compareTo(Event o) {
		return Integer.compare(this.getSequenceNum(), o.getSequenceNum());
	}

}
