package activities.inboxActivity;

import helperClasses.Constants;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.codec.binary.Base64;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

public class MessageHelper {
	
	// Trigger this method when a message is received from POST
	public static void showViewMessageDialog(final Activity currentActivity, String senderId){
		AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
		builder.setTitle("Message Received")
			   .setMessage(senderId + "sent you a text message.")
		       .setPositiveButton("View", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		           	Intent intent = new Intent(currentActivity.getApplicationContext(), InboxActivity.class);
		           	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		           	currentActivity.getApplicationContext().startActivity(intent);
		           }
		       })
		       .setNegativeButton("Close", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	// Trigger this method when click on someone on the contact list
	public static void showSendMessageDialog(final Activity currentActivity, final String receiverId){
		AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
		builder.setTitle("Send new message")
			   .setMessage("Send a text message to" + receiverId + " ?")
		       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		           	Intent intent = new Intent(currentActivity.getApplicationContext(), ComposeMessageActivity.class);
		           	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		           	intent.putExtra(Constants.Userid, receiverId);
		           	currentActivity.getApplicationContext().startActivity(intent);
		           }
		       })
		       .setNegativeButton("Close", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	public static MessageListObject getObject(Activity currentActivity){
		MessageListObject messageListObject = null;
		
		//Obtain the sharePreference file
        SharedPreferences sp = currentActivity.getSharedPreferences(Constants.MessageFile, 0);
        
        //Check if this is the first run
        String messageListObjectBase64 = sp.getString(Constants.MessageListObjectBase64, "");  
        if (messageListObjectBase64.equals("")){
        	//Create a new MessageListObject
        	messageListObject = new MessageListObject();
        } else {
        	//Read the MessageListObject from SharedPref
            try {
				byte[] base64Bytes = Base64.decodeBase64(messageListObjectBase64.getBytes());  
				ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);  
				ObjectInputStream ois = new ObjectInputStream(bais);  
				messageListObject = (MessageListObject) ois.readObject(); //obtain the object
			} catch (Exception e) {
				e.printStackTrace();
			} 
        }
        return messageListObject;
	}
	
	public static void saveObject(Activity currentActivity, MessageListObject messageListObject){
		//Obtain the sharePreference file
        SharedPreferences sp = currentActivity.getSharedPreferences(Constants.MessageFile, 0);
        
        try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			ObjectOutputStream oos = new ObjectOutputStream(baos);  
			oos.writeObject(messageListObject);  

			//Save to SharedPref
			String messageListObjectBase64 = new String(Base64.encodeBase64(baos.toByteArray()));  
			SharedPreferences.Editor editor = sp.edit();  
			editor.putString(Constants.MessageListObjectBase64, messageListObjectBase64);  
			editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}  
        
	}
	
	public static void addMessage(Activity currentActivity, String from, String date, String message){
		
		MessageListObject messageListObject = MessageHelper.getObject(currentActivity);
		        
		//Create a new MessageObject
		MessageObject newMessage = new MessageObject();
		newMessage.from = from;
		newMessage.date = date;
		newMessage.message = message;
		
		messageListObject.messageRecievedList.add(newMessage);
		
		MessageHelper.saveObject(currentActivity, messageListObject);
	}
	
	
	
}
