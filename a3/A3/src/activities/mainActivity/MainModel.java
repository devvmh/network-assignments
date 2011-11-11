package activities.mainActivity;

import helperClasses.UserInfoObject;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;


public class MainModel {
	private MainActivity activity;
	private MainUI ui;
	
	private boolean acceptTimerTask;
	
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
			System.out.println("i am here");
		}
	}
	
	
	public void refreshUserList(){
		(new HttpGetTask()).execute();
		
		//for testing
		List<UserInfoObject> a = new ArrayList<UserInfoObject>();
		UserInfoObject b = new UserInfoObject();
		b.userid = "asdf";
		b.latitude = 123;
		b.longitude = 456;
		b.interest = "swimming";
		a.add(b);
		a.add(b);
		a.add(b);
		a.add(b);
		a.add(b);
		a.add(b);
		a.add(b);
		ui.updateUI_loadUserList(a);
	}
	

	
	
	private class HttpGetTask extends AsyncTask<String, Integer, String>{
		protected void onPreExecute() {
			super.onPreExecute();
			ui.updateUI_enableLoading(true);
		}

		protected String doInBackground(String... arg0) {
			return null;
		}
		
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			ui.updateUI_enableLoading(false);
		}
		
	}
	
	
	
	public void onResume(){
		acceptTimerTask = true;
	}
	
	public void onPause(){
		acceptTimerTask = false;
	}
	
	
	

}
