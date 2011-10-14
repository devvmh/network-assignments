package com.core;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.utils.BLS;
import com.utils.QueryResult;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;

public class Model {
	private A2Activity activity;
	private UI ui;
	private BluetoothAdapter bluetoothAdapter;
	
	private List<String> nameList;
	private List<String> macList;
	private List<String> ipList;
	private List<QueryResult> queryResultList;
	
	public Model(A2Activity act, UI u){
		this.activity = act;
		this.ui = u;
		
		this.nameList = new ArrayList<String>();
		this.macList = new ArrayList<String>();
		this.ipList = new ArrayList<String>();
		this.queryResultList = new ArrayList<QueryResult>();
		
		
		this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	}
	
	public void initialize(){
		this.ui.initialize();
		
		this.ui.updateUI_setMyMac(this.bluetoothAdapter.getAddress());
		this.ui.updateUI_setMyip(this.getLocalIP());
		
    	IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
    	filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
    	activity.registerReceiver(receiver, filter); // Don't forget to unregister during onDestroy
		
	}
	
	public void startScanning(){
		if (this.bluetoothAdapter.isDiscovering()){
			this.bluetoothAdapter.cancelDiscovery();
			while (bluetoothAdapter.isDiscovering()){}
			this.ui.updateUI_setStartScanningBnLael("Start Scanning");
			this.ui.updateUI_setProgressbar1Visible(false);
		} else {
			this.ui.updateUI_clearMACList();
			this.macList.clear();
			this.nameList.clear();
			this.bluetoothAdapter.startDiscovery();
			while (!bluetoothAdapter.isDiscovering()){}
			this.ui.updateUI_setStartScanningBnLael("Stop Scanning");
			this.ui.updateUI_setProgressbar1Visible(true);
			this.ui.updateUI_setSendQueryBnEnabled(false);
		}

	}
	
	
	public void sendQuery(){
		this.ui.updateUI_setSendQueryBnEnabled(false);
		this.ui.updateUI_setProgressbar2Visible(true);
		
		
		this.ui.updateUI_clearQueryResultList();
		this.ipList.clear();
		
		(new QuerySender()).execute();
	}
	
	
	
	class QuerySender extends AsyncTask<String, Integer, String>{
		
		protected String doInBackground(String... params) {
			
			try {
				queryResultList = BLS.sendQueries(macList);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			
			for (int i = 0; i < queryResultList.size(); i++){
				if (queryResultList.get(i) != null){
					String ip = queryResultList.get(i).lanIP;
					ipList.add(ip);
					ui.updateUI_addItemToQueryResultList(macList.get(i)+ " -- " + ip);
				} else {
					ui.updateUI_addItemToQueryResultList(macList.get(i)+ " Not found in BLS server.");
				}
			}
			
			ui.updateUI_setSendQueryBnEnabled(true);
			ui.updateUI_setProgressbar2Visible(false);
			
			
		}
		

		
	}
	
	
	private String getLocalIP(){
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
	}
	
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
            	BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            	// Add the name and address to an array adapter to show in a ListView
            	
            	if (!macList.contains(device.getAddress())){
            		nameList.add(device.getName());
            		macList.add(device.getAddress());
            	}
            	ui.updateUI_addItemToMACList(device.getName() + " -- " + device.getAddress());
            }
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
        		String now = String.valueOf(System.currentTimeMillis() / 1000);
        		ui.updateUI_setLastScann(now);

            	ui.updateUI_setProgressbar1Visible(false);
            	ui.updateUI_setStartScanningBnLael("Start Scanning");
            	
            	if (macList.size() == 0){
            		ui.updateUI_addItemToMACList("No device was found");
            	} else {
            		ui.updateUI_setSendQueryBnEnabled(true);
            	}
            }

        }
    };
	
}
