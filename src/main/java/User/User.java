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
	private HashSet<User> followees;
	private DataOutputStream outputStream;

	public User(String id, OutputStream outputStream) {
		try {
			this.id = Integer.valueOf(id);
		} catch(NumberFormatException e) {
			throw new NumberFormatException();
		}

		this.outputStream = new DataOutputStream(outputStream);
		followers = new HashSet<>();
		followees = new HashSet<>();
	}

	public int getId(){ return id;}

	public void followAnotherUser(User whoToFollow){
		followees.add(whoToFollow);
	}

	public void followMe(User followee){
		followers.add(followee);
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
