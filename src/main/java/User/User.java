package User;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;

/**
 * Created by alx on 9/20/16.
 */
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

	public User(int id) {
		this.id = Integer.valueOf(id);
		this.outputStream = null;

		followers = new HashSet<>();
		usersIFollow = new HashSet<>();
	}

	public int getId(){ return id;}

	public void followAnotherUser(User whoToFollow){
		if(usersIFollow.contains(whoToFollow))
			return;

		usersIFollow.add(whoToFollow);
	}

	public void followMe(User followee){
		if(followers.contains(followee))
				return;
		followers.add(followee);
	}

	public void unfollowMe(User unfollower) {
		if(followers.contains(unfollower))
			followers.remove(unfollower);
	}

	public HashSet<User> getFollowers() {
		return followers;
	}

	public void addConnection(OutputStream outputStream) {
		this.outputStream = new DataOutputStream(outputStream);
	}

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

	public void disconnect(){}

}
