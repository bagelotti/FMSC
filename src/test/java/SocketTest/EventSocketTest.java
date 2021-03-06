package SocketTest;

import Event.Event;
import Event.ShutdownEvent;
import org.junit.Test;
import org.mockito.Mock;
import Server.Socket.EventSocket;
import java.util.concurrent.PriorityBlockingQueue;


import static org.mockito.Mockito.*;
/*******************************************************************************
 * Unit tests of Event Socket. Will run until the duration of a time, and verify
 * that we safely terminate and add a Shutdown Event
 ******************************************************************************/
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
