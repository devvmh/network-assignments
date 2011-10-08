package ca.uwaterloo.student.cs.dmhoward.adhocnet;

import java.net.Socket;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.res.Resources;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AdhocNetworkingActivity extends Activity {
	public boolean started;
	public boolean connected;
	public BluetoothAdapter btAdapter;
	public WifiManager wManager;
	public ServerTask server = null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        started = false;
        
        //populate UI address fields
        updateMAC ();
        updateIp ();
    }//onCreate
    
    public void updateMAC () {
        //set bluetooth MAC addr in UI
        btAdapter = BluetoothAdapter.getDefaultAdapter ();
        TextView myMACaddr = (TextView) findViewById (R.id.myMACaddr);
        String addr = btAdapter.getAddress();
        myMACaddr.setText ("Bluetooth Address: " + addr);
    }//updateMAC
    
    public void updateIp () {
        //set device local IP
    	TextView myIPaddr = (TextView) findViewById (R.id.myIPaddr);
		try {
			Socket sock = new Socket("www.google.ca", 80);
			//substring strips leading '/' returned by Socket
        	String ipString = sock.getLocalAddress().toString().substring(1);
        	sock.close ();
        	myIPaddr.setText ("IP Address: " + ipString);
        	connected = true;
		} catch (Exception e) {
			//not connected
        	Resources res = getResources ();
        	myIPaddr.setText (res.getString (R.string.myIPaddr));
        	connected = false;
		}//try-catch
    }//updateIp
    
    public String ipToString (int ip) {
    	int [] section = new int [4];
    	for (int i = 3; i >= 0; i -= 1) {
    		section [i] = ip % 256;
    		ip -= section [i];
    		ip /= 256;
    	}
    	return String.valueOf (section [0]) + "." + String.valueOf (section [1]) + "." +
    		String.valueOf (section [2]) + "." + String.valueOf (section [3]);
    }
    
    public void startScan (View view) {
    	return;
    }//startScan
    
    public void startStopServer (View view) {
    	Resources res = getResources ();
		Button button = (Button) findViewById (R.id.startstop_button);
    	if (started) {
    		stopServer ();
    		button.setText(res.getString(R.string.startserver));
    		started = false;
    	} else {
    		startServer ();
    		button.setText(res.getString(R.string.stopserver));
    		started = true;
    	}//if
    }//startStopServer
    
    public void stopServer () {
    	server.cancel (false);
    	server = null;
    	return;
    }//stopServer
    
    public void startServer () {
    	if (server != null) {
    		server.cancel (false);
    	}
    	server.execute ();
    	return;
    }//startServer
    
    private class ServerTask extends AsyncTask<Void, Void, Void> {
		protected Void doInBackground(Void... arg0) {
			publishProgress ();
			return null;
		}
		
		public void onProgressUpdate (Void... update) {
			return;
		}//onProgressUpdate
    }//ServerTask
}//AdhocNetworkingActivity