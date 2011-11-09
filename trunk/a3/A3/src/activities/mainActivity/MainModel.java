package activities.mainActivity;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;


public class MainModel {
	private MainActivity activity;
	private MainUI ui;
	
	public void setComponents(MainActivity act, MainUI u){
		this.activity = act;
		this.ui = u;
	}
	
	public void init() {
		this.ui.init();
	}

	public void destroy() {
		this.ui.destroy();
	}
	
	
	public void setCurrentLocation(double latitude, double longitude){
		System.out.println("Lat " + latitude);
		System.out.println("Long " + longitude);
	}
	
	

}
