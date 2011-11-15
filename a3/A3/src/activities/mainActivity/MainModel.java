package activities.mainActivity;

import helperClasses.Client;
import helperClasses.UserInfoObject;
import java.util.List;

import android.location.Location;
import android.os.AsyncTask;
import android.widget.Toast;


public class MainModel {
	private MainActivity activity;
	private MainUI ui;
	
	protected UserInfoObject self;
	
	public void setComponents(MainActivity act, MainUI u){
		this.activity = act;
		this.ui = u;
	}
	
	public void init(Runnable POSTRunnable, Runnable checkIPRunnable) {
		self = new UserInfoObject ();
		
		//keep posting data every interval seconds until onDestroy is called
		//must be called after "self" is created
		POSTRunnable.run ();
		
		//keep updating IP every interval seconds until onDestroy
		checkIPRunnable.run ();
		
		this.ui.init();
	}
	
	public void setCurrentLocation(double lat, double lon){
		self.latitude = lat;
		self.longitude = lon;
	}
	
	public void setInterests (String i) {
		self.interests = i;
	}
	
	public void setIP (String ip) {
		self.userid = ip;
	}
	
	public float getDistance (UserInfoObject u) {
		float [] dist = {0};
		Location.distanceBetween(self.latitude, self.longitude, u.latitude, u.longitude, dist);
		return dist [0];
	}
	
	//sends a POST of your data to the server
	//called in controller every upInt seconds
	public void sendBroadcastMessage() {
		if (self.hasNull ()) {
			String message = "";
			if (self.userid == null) {
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
			ui.updateUI_enableLoading(false);
			ui.updateUI_loadUserList(result);
		}
	}//HttpGetTask
	
	private class HttpPostTask extends AsyncTask<UserInfoObject, Void, Void> {
		protected Void doInBackground (UserInfoObject... arg0) {
			UserInfoObject self = arg0 [0];
			Client.postUserInfo(self);
			return null;
		}//doInBackground
	}//HttpPostTask
	
	private class CheckIPTask extends AsyncTask<Void, Void, Void> {
		protected Void doInBackground (Void... arg0) {
			setIP (Client.checkIP ());
			return null;
		}//doInBackground
	}//checkIPTask
	
	public void onResume(){
		return;
	}//onResume
	
	public void onPause(){
		return;
	}//onPause
	
	public void destroy() {		
		this.ui.destroy();
	}//destroy
}//MainModel class
