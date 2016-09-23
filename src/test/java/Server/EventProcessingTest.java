package Server;

import User.User;
import event.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Spy;
import server.EventProcessRunnable;
import java.util.HashMap;
import java.util.concurrent.PriorityBlockingQueue;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/****************************************
 * Created by  on 9/22/16.
 * Tests all processing events using mocks and spiesto verify correct calls
 * Mockito and JUnit are used together for these tests
 *
 * Each event is simulated and affected users are spied
 * so we can check they're correctly notified of the event
 *
 * each test has a terminating shutdown event to end the queue
 * processing
 ****************************************/
public class EventProcessingTest {
	private PriorityBlockingQueue<Event> queue;
	private HashMap<Integer, User> userMap;

	@Spy
	private User follower;
	@Spy
	private User followee;


	// common setup before each test
	@Before
	public void setup() {
		queue = new PriorityBlockingQueue<>();
		userMap = new HashMap<>();
		follower = spy(new User(1));
		followee = spy(new User(10));
		userMap.put(follower.getId(), follower);
		userMap.put(followee.getId(), followee);
	}

	 //Test follow event. Ensure the user's follow list is updated	and he is notified
	@Test
	public void testFollowEventProcessing() {
		FollowEvent event = setupFollowEvent();
		queue.put(event);
		queue.put(new ShutdownEvent());
		EventProcessRunnable runnable = new EventProcessRunnable(queue, userMap);
		runnable.run();

		assertTrue(followee.getFollowers().contains(follower));
		verify(followee).notify(event.getMessage());
	}

	//Make sure a user who unfollowed another is removed from the followee's list of followers
	@Test
	public void testUnfollowEventProcessing() {
		FollowEvent followEvent = setupFollowEvent();
		UnfollowEvent unfollowEvent = setupUnfollowEvent();
		queue.put(followEvent);
		queue.put(unfollowEvent);
		queue.put(new ShutdownEvent());

		EventProcessRunnable runnable = new EventProcessRunnable(queue, userMap);
		runnable.run();
		assertTrue(followee.getFollowers().isEmpty());
	}

	// Ensures that the correct user is notified with the correct msg for Private Msg Events
	@Test
	public void testPrivateMsgProcessing() {
		String message = "1|P|1|10";
		String[] msgInfo = message.split("\\|");
		PrivateMsgEvent privateMsgEvent = new PrivateMsgEvent(msgInfo[0],msgInfo[2],msgInfo[3], message);
		queue.put(privateMsgEvent);
		queue.put(new ShutdownEvent());

		EventProcessRunnable runnable = new EventProcessRunnable(queue, userMap);
		runnable.run();
		verify(followee).notify(privateMsgEvent.getMessage());
	}

	// Verify that all followers of an updating user are notified of a user's status update
	@Test
	public void testStatusUpdateProcessing() {
		String message = "2|S|10";
		String[] msgInfo = message.split("\\|");
		StatusUpdateEvent statusUpdateEvent = new StatusUpdateEvent(msgInfo[0],msgInfo[2],message);
		FollowEvent followEvent = setupFollowEvent();
		queue.put(followEvent);
		queue.put(statusUpdateEvent);
		queue.put(new ShutdownEvent());

		EventProcessRunnable runnable = new EventProcessRunnable(queue, userMap);
		runnable.run();
		verify(followee).getFollowers();
		verify(follower).notify(statusUpdateEvent.getMessage());
	}


	// Verify that all connected users are notified of a broadcast
	@Test
	public void testBroadcastProcessing() {
		String message ="1|B|";
		String[] msgInfo = message.split("\\|");
		BroadcastEvent broadcastEvent = new BroadcastEvent(msgInfo[0], message);
		queue.put(broadcastEvent);
		queue.put(new ShutdownEvent());
		EventProcessRunnable runnable = new EventProcessRunnable(queue, userMap);
		runnable.run();

		verify(followee).notify(broadcastEvent.getMessage());
		verify(follower).notify(broadcastEvent.getMessage());
	}

	/**
	 * helper functions to reduce redundancy
	 * @return FollowEvent
	 */
	private FollowEvent setupFollowEvent() {
		String eventMsg = "1|F|1|10";
		String[] eventInfo = eventMsg.split("\\|");
		FollowEvent event = new FollowEvent(eventInfo[0], eventInfo[2], eventInfo[3], eventMsg);
		return event;
	}

	/**
	 * Unfollow event: only called after a follow event, so sequence num is 2
	 * @return UnfollowEvent
	 */
	private UnfollowEvent setupUnfollowEvent() {
		String eventMsg = "2|U|1|10";
		String[] eventInfo = eventMsg.split("\\|");
		UnfollowEvent event = new UnfollowEvent(eventInfo[0], eventInfo[2], eventInfo[3], eventMsg);
		return event;
	}
}
