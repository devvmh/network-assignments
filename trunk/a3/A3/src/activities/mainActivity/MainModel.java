package activities.mainActivity;

import helperClasses.Client;
import helperClasses.GlobalVariables;
import helperClasses.UserInfoObject;

import java.util.HashMap;
import java.util.List;

import activities.contactListActivity.ContactListActivity;
import android.content.Intent;
import activities.inboxActivity.MessageHelper;
import activities.inboxActivity.MessageObject;
import android.os.AsyncTask;
import android.widget.Toast;

public class MainModel {
	private MainActivity activity;
	private MainUI ui;
	private UserInfoObject self;
	
	public void setComponents(MainActivity act, MainUI u){
		this.activity = act;
		this.ui = u;
	}
	
	public void init(Runnable POSTRunnable, Runnable checkIPRunnable) {
		GlobalVariables.self = new UserInfoObject (); // ComposeMessageActivity will also need self to do a POST
		self = GlobalVariables.self;
		//keep posting data every interval seconds until onDestroy is called
		//must be called after "self" is created
		POSTRunnable.run ();
		
		//keep updating IP every interval seconds until onDestroy
		checkIPRunnable.run ();
		
		this.ui.init();
	}
	
	public void setCurrentLocation(double lat, double lon){
		GlobalVariables.self.latitude = lat;
		self.longitude = lon;
	}
	
	public void setInterests (String i) {
		self.interests = i;
	}
	
	public void setIntIP (String ip) {
		self.intIP = ip;
	}
	
	public void setExtIP (String ip) {
		self.extIP = ip;
	}
	
	//sends a POST of your data to the server
	//called in controller every upInt seconds
	public void sendBroadcastMessage() {
		if (self.hasNull ()) {
			String message = "";
			if (self.intIP == null || self.extIP == null){
				message += "Missing IP!\n";
			}
			if (self.longitude == null || self.latitude == null) {
				message += "Missing GPS fix!\n";
			}
			Toast.makeText (activity, message, Toast.LENGTH_SHORT).show ();
		} else {
			(new HttpPostTask ()).execute (self);
		}//if
	}//sendBroadcastMessage
	
	public void checkIP () {
		(new CheckIPTask ()).execute ();
	}
	
	//get users with a GET, called by clicking the Refresh button
	public void refreshUserList(){
		(new HttpGetTask()).execute(self);
	}
	
	private class HttpGetTask extends AsyncTask<UserInfoObject, Void, List<UserInfoObject>>{
		protected void onPreExecute() {
			super.onPreExecute();
			ui.updateUI_enableLoading(true);
		}

		protected List<UserInfoObject> doInBackground (UserInfoObject... arg0) {
			UserInfoObject self = arg0 [0];
			return Client.getUserInfoList (self);
		}
		
		protected void onPostExecute(List<UserInfoObject> result) {
			super.onPostExecute(result);
			if (result == null) {
				Toast.makeText (activity, "Problem communicating with server", Toast.LENGTH_LONG);
			}//error check
			ui.updateUI_enableLoading(false);
			ui.updateUI_loadUserList(result);
		}
	}//HttpGetTask
	
	private class HttpPostTask extends AsyncTask<UserInfoObject, Void, Void> {
		List<MessageObject> messageList;
		protected Void doInBackground (UserInfoObject... arg0) {
			UserInfoObject self = arg0 [0];
			messageList = Client.postUserInfo(self);
			return null;
		}//doInBackground
		
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (messageList.size() != 0){
				for (int i = 0; i < messageList.size(); i++){
					MessageHelper.addMessage(activity, messageList.get(i));
				}
				MessageHelper.showViewMessageDialog(activity, "internal/external");
			}
		}
		
	}//HttpPostTask
	
	private class CheckIPTask extends AsyncTask<Void, Void, Void> {
		protected Void doInBackground (Void... arg0) {
			
			setExtIP (Client.checkIP ());
			setIntIP (Client.getLocalIPAddress ());
			return null;
		}//doInBackground
	}//checkIPTask
	
	//adds a contact to the database in ContactListActivity
	public void updateContact (HashMap<String, Object> userMap) {
		String longitude = ((Double) userMap.get("longitude")).toString ();
		String latitude = ((Double) userMap.get("latitude")).toString ();
		String internal = (String) userMap.get("internal");
		String external = (String) userMap.get("external");
		String interests = (String) userMap.get("interests");
		
		//adds to ContactListActivity's database
		Intent intent = new Intent (activity.getApplicationContext(),
    	        ContactListActivity.class);
		intent.putExtra("internal", internal);
		intent.putExtra("external", external);
		intent.putExtra("longitude", longitude);
		intent.putExtra("latitude", latitude);
		intent.putExtra("interests", interests);
		activity.startActivity(intent);
		return;
	}//updateContact
	
	public void onResume() {
		return;
	}//onResume
	
	public void onPause() {
		return;
	}//onPause
	
	public void destroy() {		
		this.ui.destroy();
	}//destroy
}//MainModel class
