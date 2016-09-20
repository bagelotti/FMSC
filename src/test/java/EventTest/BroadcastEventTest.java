package EventTest;

import event.BroadcastEvent;
import event.Event;
import event.FollowEvent;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by ... on 9/19/16.
 */
public class BroadcastEventTest {

	@Test
	public void testCreation() {
		Event event = new BroadcastEvent("1");
		assertTrue(event instanceof BroadcastEvent);
		assertEquals(event.getSequenceNum(), 1);
	}

	@Test
	public void testUserParams() {
		Event event = new BroadcastEvent("1");
		assertEquals(event.getSequenceNum(), 1);
	}

	@Test (expected = NumberFormatException.class)
	public void testInvalidParams() {
		Event event = new BroadcastEvent(null);
	}

	@Test (expected = NumberFormatException.class)
	public void testInvalidParamsEmptyString() {
		Event event = new BroadcastEvent("");
	}
}
