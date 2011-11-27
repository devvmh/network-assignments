package activities.inboxActivity;

import java.io.Serializable;

public class MessageObject implements Serializable{
	public String senderInternal = null;
	public String senderExternal = null;
	public String date = null;
	public String message = null;
	
	public MessageObject(String internal, String external, String date, String message){
		this.senderInternal = internal;
		this.senderExternal = external;
		this.date = date;
		this.message = message;
	}
}
