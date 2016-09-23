package EventTest;

import event.Event;
import event.StatusUpdateEvent;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by ... on 9/19/16.
 */
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
