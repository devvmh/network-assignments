package activities.mainActivity;

import helperClasses.Constants;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainControl {
	private MainActivity activity;
	private MainModel model;
	
	private LocationManager locationManager;
	private String provider;
	public CurLocationListener curLocationListener;
	
	public ButtonListener buttonListener;
	public PostButtonListener postButtonListener;
	public ListViewListener listViewListener;
	
	protected Handler handler;
	protected Runnable POSTRunnable;
	protected Runnable checkIPRunnable;

	public void setComponents(MainActivity act, MainModel m){
		this.activity = act;
		this.model = m;
	}
	
	public void init() {
		//send POST requests every Constants.upInt seconds if run method is called
		handler = new Handler ();
		POSTRunnable = new Runnable () {
			public void run(){
				model.sendBroadcastMessage();
				handler.postDelayed (POSTRunnable, Constants.upInt*1000);
			}//run
		};//POSTRunnable	
		
		//check external IP every Constants.upInt seconds if run method is called
		checkIPRunnable = new Runnable () {
			public void run () {
				model.checkIP ();
				handler.postDelayed(checkIPRunnable, Constants.upInt*1000);
			}//run
		};//checkIPRunnable
		
		//initialize listeners
		this.buttonListener = new ButtonListener();
		this.postButtonListener = new PostButtonListener ();
		this.listViewListener = new ListViewListener ();
		
		//tell model to init
		this.model.init(POSTRunnable, checkIPRunnable);	
		
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
		
		//get the first list of users
		model.refreshUserList();
	}
	
	public void onResume(){
		//check for updates for a move of greater than 5m and check every upInt seconds
   		locationManager.requestLocationUpdates(provider, Constants.upInt, 5, this.curLocationListener);
   		model.onResume();
	}//onResume
	
	public void onPause(){
		//stop looking for location updates
		locationManager.removeUpdates(this.curLocationListener);
		model.onPause();
	}//onPause
	
	public void destroy() {
		//when you exit the app, then you stop doing the POSTs
		handler.removeCallbacks (POSTRunnable);
		handler.removeCallbacks (checkIPRunnable);
		this.model.destroy();
	}//destroy
	
	private class CurLocationListener implements LocationListener{
		public void onLocationChanged(Location location) {
			model.setCurrentLocation(location.getLatitude(), location.getLongitude());
		}
		public void onProviderDisabled(String provider) {
			Toast.makeText(activity, "Disabled GPS provider " + provider, Toast.LENGTH_SHORT).show();
		}
		public void onProviderEnabled(String provider) {
			Toast.makeText(activity, "Enabled new provider " + provider, Toast.LENGTH_SHORT).show();
		}
		public void onStatusChanged(String provider, int status, Bundle extras) {}
	}//CurLocationListener
	
	private class ButtonListener implements OnClickListener{
		public void onClick(View v) {
			model.refreshUserList();
		}//onClick
	}//ButtonListener class
	
	private class PostButtonListener implements OnClickListener {
		public void onClick (View v) {
			Toast.makeText(activity, "Checking...", Toast.LENGTH_SHORT).show ();
			model.sendBroadcastMessage ();
		}//onClick
	}//PostButtonListener class
	
	public class ListViewListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> a, View v, int position, long id) {
			//user to send to identified by these two parameters
			model.sendMessage (a, position);
		}
	}//ListViewListener class
}//MainControl class
