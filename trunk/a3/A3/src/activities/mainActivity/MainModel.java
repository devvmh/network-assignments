package activities.mainActivity;

import helperClasses.Client;
import helperClasses.UserInfoObject;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;


public class MainModel {
	private MainActivity activity;
	private MainUI ui;
	
	private boolean acceptTimerTask;
	protected List<UserInfoObject> userList;
	
	public void setComponents(MainActivity act, MainUI u){
		this.activity = act;
		this.ui = u;
	}
	
	public void init() {
		acceptTimerTask = true;
		this.ui.init();
	}

	public void destroy() {
		this.ui.destroy();
	}
	
	
	public void setCurrentLocation(double latitude, double longitude){
		System.out.println("Lat " + latitude);
		System.out.println("Long " + longitude);
	}
	
	public void sendBroadcastMessage(){
		if (acceptTimerTask == true){
//			System.out.println("i am here");
		}
	}
	
	
	public void refreshUserList(){
		(new HttpGetTask()).execute();
	}
	
	private class HttpGetTask extends AsyncTask<String, Integer, String>{
		protected void onPreExecute() {
			super.onPreExecute();
			ui.updateUI_enableLoading(true);
		}

		protected String doInBackground(String... arg0) {
			userList = Client.getUserInfoList ();
			return null;
		}
		
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			ui.updateUI_enableLoading(false);
			ui.updateUI_loadUserList(userList);
		}
		
	}
	
	
	
	public void onResume(){
		acceptTimerTask = true;
	}
	
	public void onPause(){
		acceptTimerTask = false;
	}
	
	
	

}
