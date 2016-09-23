package SocketTest;

import User.User;
import org.junit.Test;
import org.mockito.Mock;
import server.Socket.ClientSocket;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/******************************************************************
 * - uses mock testing to test some of the internals of the class
 ******************************************************************/
public class ClientSocketTest {
	@Mock
	Socket mockClient = mock(Socket.class);

	@Mock
	InputStream mockReader = mock(InputStream.class);

	@Test
	public void testValidParams() {
		ClientSocket clientSocket = new ClientSocket(mockClient, new HashMap<>());
		assertTrue(clientSocket instanceof ClientSocket);
	}


	// test illegal arguments
	@Test(expected = IllegalArgumentException.class)
	public void testConstruction() {
		ClientSocket clientSocket = new ClientSocket(mockClient,null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructionEmptySocket() {
		ClientSocket clientSocket = new ClientSocket(null, new HashMap<>());
	}

	 // make sure we catch the exception safely with a mock
	@Test
	public void testClientSocketException() throws IOException {
		ClientSocket clientSocket = new ClientSocket(mockClient, new HashMap<Integer, User>());
		when(mockClient.getInputStream()).thenThrow(new IOException());
		clientSocket.run();
	}

	 // test closing of socket with a mock
	@Test
	public void testClientSocketInputReader() throws IOException {
		ClientSocket clientSocket = new ClientSocket(mockClient, new HashMap<Integer, User>());
		when(mockClient.getInputStream()).thenReturn(mockReader);
		clientSocket.run();
		verify(mockReader,times(1)).close();
	}
}
