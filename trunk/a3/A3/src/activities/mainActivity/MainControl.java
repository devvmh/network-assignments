package activities.mainActivity;

import com.a3.R;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class MainControl {
	private MainActivity activity;
	private MainModel model;
	
	private LocationManager locationManager;
	private String provider;
	public CurLocationListener curLocationListener;
	public ButtonListener buttonListener;
	
	private Thread timer;

	public void setComponents(MainActivity act, MainModel m){
		this.activity = act;
		this.model = m;
	}
	
	public void init() {
		//Initialize location utilities
		locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(provider);
		
		if (location != null){
			System.out.println("Provider is " + provider);
			model.setCurrentLocation(location.getLatitude(), location.getLongitude());
		} else {
			System.out.println("location not availabe.");
		}
		this.curLocationListener = new CurLocationListener();
		
		//initialize the timer
		timer = new Thread(){
			public void run(){
				while (true){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				model.sendBroadcastMessage();
				}
			}
		};
		
		timer.start();
		
		//initialize ButtonListener
		this.buttonListener = new ButtonListener();
		
		
		//tell model to init
		this.model.init();	
	}
	
	public void onResume(){
		locationManager.requestLocationUpdates(provider, 400, 1, this.curLocationListener);
		model.onResume();
	}
	
	public void onPause(){
		locationManager.removeUpdates(this.curLocationListener);
		model.onPause();
	}
	
	

	public void destroy() {
		this.model.destroy();
	}
	
	private class CurLocationListener implements LocationListener{
		public void onLocationChanged(Location location) {
			model.setCurrentLocation(location.getLatitude(), location.getLongitude());
		}
		public void onProviderDisabled(String provider) {
			Toast.makeText(activity, "Disabled provider " + provider, Toast.LENGTH_SHORT).show();
		}
		public void onProviderEnabled(String provider) {
			Toast.makeText(activity, "Enabled new provider " + provider, Toast.LENGTH_SHORT).show();
		}
		public void onStatusChanged(String provider, int status, Bundle extras) {}
	}
	
	private class ButtonListener implements OnClickListener{
		public void onClick(View v) {
			model.refreshUserList();
		}
	}
	
	
}
