package EventTest;

import event.Event;
import event.PrivateMsgEvent;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by ...on 9/19/16.
 */
public class PrivateMsgEventTest {
	String message = "1|533|600";

	@Test
	public void testCreation() {
		Event event = new PrivateMsgEvent("1", "533", "600", message);
		assertTrue(event instanceof PrivateMsgEvent);
		assertEquals(event.getSequenceNum(), 1);
	}

	@Test
	public void testUserParams() {
		Event event = new PrivateMsgEvent("1", "533", "600", message);
		assertEquals(event.getSequenceNum(), 1);
	}

	@Test
	public void testGetMessage() {
		Event event = new PrivateMsgEvent("1", "522","600",message);
		assertEquals(event.getMessage(), message);
	}

	@Test (expected = NumberFormatException.class)
	public void testInvalidParams() {
		Event event = new PrivateMsgEvent(null, "533", "600", message);
	}

	@Test (expected = NumberFormatException.class)
	public void testInvalidParamsEmptyString() {
		Event event = new PrivateMsgEvent("", "533", "600", message);
	}
}
