package EventTest;

import Event.Event;
import Event.UnfollowEvent;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/*********************************
 * unit tests of Unfollow Event
 **********************************/
public class UnfollowEventTest {
	String message = "1|533|600";

	@Test
	public void testCreation() {
		Event event = new UnfollowEvent("1", "533", "600", message);
		assertTrue(event instanceof UnfollowEvent);
		assertEquals(event.getSequenceNum(), 1);
	}

	@Test
	public void testUserParams() {
		Event event = new UnfollowEvent("1", "533", "600", message);
		assertEquals(event.getSequenceNum(), 1);
	}

	@Test
	public void testGetMessage() {
		Event event = new UnfollowEvent("1", "522","600",message);
		assertEquals(event.getMessage(), message);
	}

	@Test (expected = NumberFormatException.class)
	public void testInvalidParams() {
		Event event = new UnfollowEvent(null, "533", "600", message);
	}

	@Test (expected = NumberFormatException.class)
	public void testInvalidParamsEmptyString() {
		Event event = new UnfollowEvent("", "533", "600", message);
	}
}
