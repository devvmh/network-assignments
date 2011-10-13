package ca.uwaterloo.student.cs.dmhoward.adhocnet;

import java.net.Socket;

import android.app.Activity;
import android.content.res.Resources;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class AdhocNetworkingActivity extends Activity {
	public boolean started;
	public boolean connected;
	public WifiManager wManager;


	private A2Server server = new A2Server();
	private A2Client client;

	private BluetoothUtils bluetooth;
	
	
	
	private Button startscan_Button;
	private TextView lastscan_TextView;
	private TextView myMAC_TextView;
	private TextView myIP_TextView;
	private ListView neighbourMACs_ListView;
	private Button startStopServer_Button;
	private TextView status_TextView;
	
	
	



	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);


		this.bluetooth = new BluetoothUtils(this);

		
		this.findViews();
		this.initializeViews();
		
		

	}//onCreate

	public void onDestroy() {
		super.onDestroy();
		this.bluetooth.unregisterReceiver();
		
		

	}
	
	private void findViews(){
		this.startscan_Button = (Button) findViewById(R.id.startscan_button);
		
		this.lastscan_TextView = (TextView) findViewById(R.id.lastscantime);
		
		this.myMAC_TextView = (TextView) findViewById(R.id.myMACaddr);
		
		this.myIP_TextView = (TextView) findViewById(R.id.myIPaddr);
		
		this.neighbourMACs_ListView = (ListView) findViewById(R.id.neighbourMACs);
		
		this.startStopServer_Button = (Button) findViewById(R.id.startstop_button);
		
		this.status_TextView = (TextView) findViewById(R.id.status);
		
	}
	
	private void initializeViews(){
		this.startscan_Button.setText("Start Scanning");
		this.startscan_Button.setOnClickListener(new StartScanListener());
		
		this.lastscan_TextView.setText("Never Scanned.");
		
		this.myMAC_TextView.setText(this.bluetooth.getMyMAC());
		
		this.myIP_TextView.setText(this.bluetooth.getLocalIpAddress());
		
		this.neighbourMACs_ListView.setAdapter(this.bluetooth.getArrayAdapter());
		this.neighbourMACs_ListView.setOnItemClickListener(new listClickListener());
		
		
		this.startStopServer_Button.setText("Start Server");
		this.startStopServer_Button.setOnClickListener(new StartServerListener());
		
		this.status_TextView.setText("No status.");
	}
	
	

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






	class StartScanListener extends AsyncTask<String, Integer, String> implements OnClickListener{

		public void onClick(View arg0) {
			if (bluetooth.isDiscovering()){
				bluetooth.cancelDiscovery();
				while (bluetooth.isDiscovering()){
					System.out.println("waiting to cancel discovery");
				}
			} else {
				bluetooth.startDiscovery();
				while (!bluetooth.isDiscovering()){
					System.out.println("waiting to start discovery");
				}
				String now = String.valueOf(System.currentTimeMillis() / 1000);
				lastscan_TextView.setText("Last Scan: " + now);
			}
			
		}

		protected String doInBackground(String... arg0) {
			return null;
		}

	}

	class BlsQueryListener extends AsyncTask<String, Integer, String> implements OnClickListener{

		public void onClick(View v) {
			System.out.println("BlsQueryListener");
		}

		protected String doInBackground(String... params) {




			// TODO Auto-generated method stub
			return null;
		}

	}

	class StartServerListener extends AsyncTask<String, Integer, String> implements OnClickListener{
		public void onClick(View v) {
			System.out.println("StartServerListener");

		}

		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return null;
		}

	}

	class listClickListener extends AsyncTask<String, Integer, String> implements OnItemClickListener{
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

			System.out.println("listClickListener");

		}

		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return null;
		}

	}



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