package UserTest;
import User.User;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Created by ... on 9/20/16.
 */
@RunWith(JUnitParamsRunner.class)
public class UserTest {
	OutputStream mockedOutputStream = mock(OutputStream.class);

	//valid parameters
	public final Object[] validParams() {
		return new Object[] {
			new Object[] {"123", null},
			new Object[] {"45", mockedOutputStream},
		};
	}

	//invalid params
	public final Object[] invalidParams() {
		return new Object[] {
			new Object[] {"XX"},
			new Object[] {""}
		};
	}

	@Test
	public void testConstruction() {
		User user = new User("1", new OutputStream() {
			@Override
			public void write(int b) throws IOException {

			}
		});
		assertTrue(user instanceof User);
	}

	@Test
	@Parameters(method = "validParams")
	public void testValidParams(String id, OutputStream stream) {
		int intValue = Integer.valueOf(id);
		User user = new User(id, stream);
		assertEquals(user.getId(), intValue);
		assertTrue(user != null);
	}

	@Test
	public void testOfflineConstruction() {
		int id = 1;
		User user = new User(id);
		assertTrue(user instanceof User);
		assertEquals(user.getId(), id);
	}

	@Test (expected = NumberFormatException.class)
	@Parameters(method = "invalidParams")
	public void testConstructorExceptions(String invalidID) {
		User user = new User(invalidID, mockedOutputStream);
	}

}
