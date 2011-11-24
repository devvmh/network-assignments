package helperClasses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// This object simply wraps all the MessageObjects for persistent storage
public class MessageListObject implements Serializable {
	public List<MessageObject> messageRecievedList;
	public List<MessageObject> messageSentList;
	
	public MessageListObject(){
		this.messageRecievedList = new ArrayList<MessageObject>();
		this.messageSentList = new ArrayList<MessageObject>();
	}
}
