package UserTest;

import User.User;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/********************************
 * User functionality unit tests
 ********************************/
public class UserFunctionality {
	User user;
	User testFollowUser;


	@Before
	public void setup() {
		user = new User("123", null);
		testFollowUser = new User("200", null);
	}

	//test follow
	@Test
	public void testFollow() {
		user.followAnotherUser(testFollowUser);
		testFollowUser.followMe(user);
		assertEquals(testFollowUser.getFollowers().size(), 1);
	}

	//test follow me
	@Test
	public void testFollowMe() {
		user.followMe(testFollowUser);
		assertEquals(user.getFollowers().size(), 1);
	}

	//test unfollow me
	@Test
	public void testUnfollowMe() {
		user.followMe(testFollowUser);
		assertEquals(user.getFollowers().size(), 1);
		user.unfollowMe(testFollowUser);
		assertEquals(user.getFollowers().size(), 0);
	}

	@Test
	public void testUnfollowEmpty() {
		user.unfollowMe(testFollowUser);
		assertEquals(user.getFollowers().size(),0);
		user.unfollowMe(null);
		assertEquals(user.getFollowers().size(),0);
	}

	//test illegal arguments
	@Test(expected = NullPointerException.class)
	public void testFollowNullExceptions() {
		user.followAnotherUser(null);
	}

	@Test(expected = NullPointerException.class)
	public void testFollowMeNullExceptions() {
		user.followMe(null);
	}
}
