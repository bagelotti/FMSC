package Server;

import User.User;
import event.Event;
import event.ShutdownEvent;
import org.junit.Test;
import org.mockito.Mock;
import server.EventProcessRunnable;
import static org.junit.Assert.assertTrue;
import java.util.HashMap;
import java.util.concurrent.PriorityBlockingQueue;
import static org.mockito.Mockito.*;

/**
 * Tests basic construction, abnormalities and exceptions for the EventProcessRunnable class
 *	Uses a mock for a user to verify we reach the disconnect() method at termination
 * Created by ... on 9/22/16.
 */
public class EventProcessTest {
	PriorityBlockingQueue<Event> queue = new PriorityBlockingQueue<>();
	HashMap<Integer, User> userMap = new HashMap<>();

	@Mock
	User user = mock(User.class);


	@Test
	public void testConstruction() {
		EventProcessRunnable runnable = new EventProcessRunnable(queue, userMap);
		assertTrue(runnable instanceof EventProcessRunnable);
	}

	//Test invalid construction w/ empty queue
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidParamsQueue() {
		EventProcessRunnable runnable = new EventProcessRunnable(null, userMap);
	}

	//Test invalid construction w/ empty hashmap
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidParamsHash() {
		EventProcessRunnable runnable = new EventProcessRunnable(queue, null);
	}

	// Test shutdown event, and disconnect users
	@Test
	public void testDisconnectReach() {
		userMap.put(2, user);
		queue.put(new ShutdownEvent());
		EventProcessRunnable runnable = new EventProcessRunnable(queue, userMap);
		runnable.run();
		verify(user,times(1)).disconnect();
	}


}
