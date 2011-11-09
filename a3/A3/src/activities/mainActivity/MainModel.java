package activities.mainActivity;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;


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
	
	public void onResume(){
		acceptTimerTask = true;
	}
	
	public void onPause(){
		acceptTimerTask = false;
	}
	
	

}
