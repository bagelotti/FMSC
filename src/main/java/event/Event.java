package event;

/*******************************************
 * Interface that defines our Event classes
 ********************************************/
public interface Event {
	int getSequenceNum();
	String getEventType();
	String getMessage();
}
