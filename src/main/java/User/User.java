package User;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;

/*****************************************************************************
 * User class represents a user connecting in from client port,
 * or an offline user. Offline Users do not have an output stream
 * and will not receive published events, but they will be processed for them
 *****************************************************************************/
public class User {
	private final int id;
	private HashSet<User> followers;
	private HashSet<User> usersIFollow;
	private DataOutputStream outputStream;

	public User(String id, OutputStream outputStream) {
		try {
			this.id = Integer.valueOf(id);
		} catch(NumberFormatException e) {
			throw new NumberFormatException();
		}

		if(outputStream != null)
			this.outputStream = new DataOutputStream(outputStream);

		followers = new HashSet<>();
		usersIFollow = new HashSet<>();
	}

	//constructor for offline user
	public User(int id) {
		this.id = Integer.valueOf(id);
		this.outputStream = null;
		followers = new HashSet<>();
		usersIFollow = new HashSet<>();
	}


	/****************************************
	 * For Follow Event
	 * when this user wants to follow someone
	 * @param whoToFollow - User to follow
	 *****************************************/
	public void followAnotherUser(User whoToFollow){
		if(whoToFollow == null)
			throw new NullPointerException();

		if(usersIFollow.contains(whoToFollow))
			return;
		usersIFollow.add(whoToFollow);
	}

	/***************************************************
	 * When someone wants to follow this user
	 * @param followee - user wants to follow this user
	 **************************************************/
	public void followMe(User followee){
		if(followee == null)
			throw new NullPointerException();

		if(followers.contains(followee))
				return;
		followers.add(followee);
	}

	/*********************************************
	 * Unfollow event, when someone wants to unfollow
	 * this user
	 * @param unfollower - User who wants to unfollow
	 *********************************************/
	public void unfollowMe(User unfollower) {
		if(followers.contains(unfollower))
			followers.remove(unfollower);
	}


	public int getId(){ return id;}


	public HashSet<User> getFollowers() {
		return followers;
	}

	/****************************************************
	 * Used when an offline user goes online
	 * @param outputStream - outputStream to publish
	 *                     events to user
	 ****************************************************/
	public void addConnection(OutputStream outputStream) {
		this.outputStream = new DataOutputStream(outputStream);
	}

	/**************************************
	 * Publish an event to a user
	 * @param message - message to send
	 **************************************/
	public void notify(String message){
		//check if user online
		if(outputStream == null)
			return;

		try {
			outputStream.writeBytes(message + "\n");
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**************************************
	 * close output stream, disconnect user
	 **************************************/
	public void disconnect(){
		if(outputStream == null){
			return;
		}

		try {
			outputStream.close();
		} catch (IOException e) {
		}
	}
}
