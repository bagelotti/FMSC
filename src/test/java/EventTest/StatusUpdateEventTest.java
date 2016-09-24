package EventTest;

import Event.Event;
import Event.StatusUpdateEvent;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/****************************************
 * Tests all units of Status Update Event
 ****************************************/
public class StatusUpdateEventTest {
	String message = "1|533|600";

	@Test
	public void testCreation() {
		Event event = new StatusUpdateEvent("1", "533", message);
		assertTrue(event instanceof StatusUpdateEvent);
		assertEquals(event.getSequenceNum(), 1);
	}

	@Test
	public void testUserParams() {
		Event event = new StatusUpdateEvent("1", "533", message);
		assertEquals(event.getSequenceNum(), 1);
	}

	@Test
	public void testGetMessage() {
		Event event = new StatusUpdateEvent("1", "522",message);
		assertEquals(event.getMessage(), message);
	}

	@Test (expected = NumberFormatException.class)
	public void testInvalidParams() {
		Event event = new StatusUpdateEvent(null, "533", message);
	}

	@Test (expected = NumberFormatException.class)
	public void testInvalidParamsEmptyString() {
		Event event = new StatusUpdateEvent("", "533", message);
	}
}
