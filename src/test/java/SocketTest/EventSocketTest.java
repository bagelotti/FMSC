package SocketTest;

import event.Event;
import event.ShutdownEvent;
import org.junit.Test;
import org.mockito.Mock;
import server.Socket.EventSocket;
import java.util.concurrent.PriorityBlockingQueue;


import static org.mockito.Mockito.*;
/**
 * Created by ... on 9/22/16.
 */
public class EventSocketTest {
	@Mock
	PriorityBlockingQueue<Event> queue = mock(PriorityBlockingQueue.class);


	@Test(expected = IllegalArgumentException.class)
	public void testConstruction() {
		EventSocket eventSocket = new EventSocket(null);
	}

	/**********************************************
	 * make sure we reach termination on exception
	 **********************************************/
	@Test
	public void testEventSocketException() {
		EventSocket eventSocket = new EventSocket(queue);
		eventSocket.run();
		verify(queue,times(1)).put(any(ShutdownEvent.class));
	}

}
