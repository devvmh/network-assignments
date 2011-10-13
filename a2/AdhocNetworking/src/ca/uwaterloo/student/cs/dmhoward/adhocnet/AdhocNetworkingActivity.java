package ca.uwaterloo.student.cs.dmhoward.adhocnet;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.app.Activity;
import android.content.res.Resources;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AdhocNetworkingActivity extends Activity {
	public boolean started;
	public boolean connected;
	public WifiManager wManager;


	private A2Server server = new A2Server();
	private A2Client client;

	private BluetoothUtils bluetooth;
	
	
	
	private ArrayAdapter<String> ipListArrayAdapter;
	
	
	private Button startscan_Button;
	private TextView lastscan_TextView;
	private TextView myMAC_TextView;
	private TextView myIP_TextView;
	private ListView neighbourMACs_ListView;
	private Button startStopServer_Button;
	private TextView status_TextView;
	private Button sendBLSquery_Button;
	private ProgressBar BLSprogressBar;
	private ListView ipList_ListView;
	



	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);


		this.bluetooth = new BluetoothUtils(this, this.myHandler);
		
		ipListArrayAdapter = new ArrayAdapter<String>(this, R.layout.list_item);

		
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
		
		this.startStopServer_Button = (Button) findViewById(R.id.startStopServer_button);
		
		this.status_TextView = (TextView) findViewById(R.id.status);
		
		this.sendBLSquery_Button = (Button) findViewById(R.id.sendBLSquery_Button);
		
		this.BLSprogressBar = (ProgressBar) findViewById(R.id.progressBar2);
		
		this.ipList_ListView = (ListView) findViewById(R.id.IPs_listView);
	}
	
	private void initializeViews(){
		this.startscan_Button.setText("Start Scanning");
		this.startscan_Button.setOnClickListener(new StartScanListener());
		
		this.lastscan_TextView.setText("Never Scanned.");
		
		this.myMAC_TextView.setText(this.bluetooth.getMyMAC());
		
		this.myIP_TextView.setText(this.bluetooth.getLocalIpAddress());
		
		this.neighbourMACs_ListView.setAdapter(this.bluetooth.getArrayAdapter());
		this.neighbourMACs_ListView.setOnItemClickListener(new listClickListener());
		
		this.sendBLSquery_Button.setText("Send BLS Query");
		this.sendBLSquery_Button.setOnClickListener(new BlsQueryListener());
//		this.sendBLSquery_Button.setEnabled(false);
		
		this.BLSprogressBar.setVisibility(View.INVISIBLE);
		
		this.ipList_ListView.setAdapter(ipListArrayAdapter);
		
		this.startStopServer_Button.setText("Start Server");
		this.startStopServer_Button.setOnClickListener(new StartServerListener());
//		this.startStopServer_Button.setEnabled(false);
		
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






	class StartScanListener implements OnClickListener{

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

	}

	class BlsQueryListener extends AsyncTask<String, Integer, String> implements OnClickListener{

		private BLS bls;
		
		public void onClick(View v) {
			BLSprogressBar.setVisibility(View.VISIBLE);
			ipListArrayAdapter.clear();
			execute();
		}
		
		protected void onPreExecute() {
			super.onPreExecute();
			this.bls = new BLS();
		}

		protected String doInBackground(String... params) {
			
			List<String> testMAClist = new ArrayList<String>();
			testMAClist.add("38:E7:D8:46:4E:B4");
			
			
			try {
				bls.sendQueries(testMAClist);
			} catch (Exception e) {
				e.printStackTrace();
			}

		

//			LinkedHashMap<String, String> macMap = bluetooth.getMACmap();
//
//			Set<Entry<String, String>> set = macMap.entrySet();
//			Iterator<Entry<String, String>> i = set.iterator();
//			while(i.hasNext()) {
//				Map.Entry<String, String> me = (Map.Entry<String, String>) i.next();
//				System.out.print(me.getKey() + ": " + me.getValue());
//			}


			return null;
		}

		protected void onPostExecute(String result) {

			List<QueryResult> resultList = bls.getQueryResults();

			for (int i = 0; i < resultList.size(); i++){
				if (resultList.get(i) != null){
					System.out.println(ipListArrayAdapter);
					ipListArrayAdapter.add(resultList.get(i).lanIP);

				}
			}
			BLSprogressBar.setVisibility(View.INVISIBLE);
		}

	}
	
	

	class StartServerListener extends AsyncTask<String, Integer, String> implements OnClickListener{
		public void onClick(View v) {
			System.out.println("StartServerListener");
			ipListArrayAdapter.add("1");
			execute();

		}

		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			ipListArrayAdapter.add("2");
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
	
    private Handler myHandler = new Handler(){
    	public void handleMessage(Message msg){
    		switch (msg.what){
    		case 1: // MAC address Map from Bluetooth is ready
    			System.out.println("Here!!!");
    			sendBLSquery_Button.setEnabled(true);
    			break;
    		}
    	}
    };



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