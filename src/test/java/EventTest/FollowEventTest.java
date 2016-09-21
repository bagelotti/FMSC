package EventTest;

import event.Event;
import event.FollowEvent;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
/**
 * Created by ... on 9/19/16.
 */
public class FollowEventTest {

	@Test
	public void testCreation() {
		String message = "1|533|600";
		Event event = new FollowEvent("1", "533", "600", message);
		assertTrue(event instanceof FollowEvent);
		assertEquals(event.getSequenceNum(), 1);
	}

	@Test
	public void testUserParams() {
		String message = "1|533|600";
		Event event = new FollowEvent("1", "533", "600", message);
		assertEquals(event.getSequenceNum(), 1);
	}

	@Test (expected = NumberFormatException.class)
	public void testInvalidParams() {
		String message = "1|533|600";
		Event event = new FollowEvent(null, "533", "600", message);
	}

	@Test (expected = NumberFormatException.class)
	public void testInvalidParamsEmptyString() {
		String message = "1|533|600";
		Event event = new FollowEvent("", "533", "600", message);
	}

}
