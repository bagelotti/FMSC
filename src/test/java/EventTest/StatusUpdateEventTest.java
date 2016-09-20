package EventTest;

import event.Event;
import event.FollowEvent;
import event.StatusUpdateEvent;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by ... on 9/19/16.
 */
public class StatusUpdateEventTest {

	@Test
	public void testCreation() {
		Event event = new StatusUpdateEvent("1", "533");
		assertTrue(event instanceof StatusUpdateEvent);
		assertEquals(event.getSequenceNum(), 1);
	}

	@Test
	public void testUserParams() {
		Event event = new StatusUpdateEvent("1", "533");
		assertEquals(event.getSequenceNum(), 1);
	}

	@Test (expected = NumberFormatException.class)
	public void testInvalidParams() {
		Event event = new StatusUpdateEvent(null, "533");
	}

	@Test (expected = NumberFormatException.class)
	public void testInvalidParamsEmptyString() {
		Event event = new StatusUpdateEvent("", "533");
	}
}
