package activities.mainActivity;

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
		ui.updateUI_enableLoading(true);
	}
	
	public void onResume(){
		acceptTimerTask = true;
	}
	
	public void onPause(){
		acceptTimerTask = false;
	}
	
	
	private class HttpGetTask extends AsyncTask<String, Integer, String>{
		protected void onPreExecute() {
			super.onPreExecute();
			
		}

		protected String doInBackground(String... arg0) {
			return null;
		}
		
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		}
		
	}
	
	
	

}
