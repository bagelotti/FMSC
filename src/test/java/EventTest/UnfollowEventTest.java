package EventTest;

import event.Event;
import event.UnfollowEvent;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by ...on 9/19/16.
 */
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

	@Test (expected = NumberFormatException.class)
	public void testInvalidParams() {
		Event event = new UnfollowEvent(null, "533", "600", message);
	}

	@Test (expected = NumberFormatException.class)
	public void testInvalidParamsEmptyString() {
		Event event = new UnfollowEvent("", "533", "600", message);
	}
}
