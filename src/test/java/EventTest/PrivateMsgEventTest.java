package EventTest;

import event.Event;
import event.FollowEvent;
import event.PrivateMsgEvent;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by ...on 9/19/16.
 */
public class PrivateMsgEventTest {
	@Test
	public void testCreation() {
		Event event = new PrivateMsgEvent("1", "533", "600");
		assertTrue(event instanceof PrivateMsgEvent);
		assertEquals(event.getSequenceNum(), 1);
	}

	@Test
	public void testUserParams() {
		Event event = new PrivateMsgEvent("1", "533", "600");
		assertEquals(event.getSequenceNum(), 1);
	}

	@Test (expected = NumberFormatException.class)
	public void testInvalidParams() {
		Event event = new PrivateMsgEvent(null, "533", "600");
	}

	@Test (expected = NumberFormatException.class)
	public void testInvalidParamsEmptyString() {
		Event event = new PrivateMsgEvent("", "533", "600");
	}
}
