package EventTest;

import Event.Event;
import Event.FollowEvent;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/****************************
 * Unit tests of Follow Event
 ****************************/
public class FollowEventTest {
	private String message = "1|533|600";

	@Test
	public void testCreation() {
		Event event = new FollowEvent("1", "533", "600", message);
		assertTrue(event instanceof FollowEvent);
		assertEquals(event.getSequenceNum(), 1);
	}

	@Test
	public void testUserParams() {
		Event event = new FollowEvent("1", "533", "600", message);
		assertEquals(event.getSequenceNum(), 1);
	}

	@Test
	public void testGetMessage() {
		Event event = new FollowEvent("1", "522","600",message);
		assertEquals(event.getMessage(), message);
	}

	@Test (expected = NumberFormatException.class)
	public void testInvalidParams() {
		Event event = new FollowEvent(null, "533", "600", message);
	}

	@Test (expected = NumberFormatException.class)
	public void testInvalidParamsEmptyString() {
		Event event = new FollowEvent("", "533", "600", message);
	}

}
