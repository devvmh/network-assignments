package ca.uwaterloo.student.cs.dmhoward.adhocnet;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;

public class BluetoothUtils {
	private Activity activity;
	private BluetoothAdapter bluetoothAdapter;
	private ArrayAdapter<String> arrayAdapter;
	
	private Button startscan_Button;
	private ProgressBar progressBar;
	
	public BluetoothUtils(Activity act){
		this.activity = act;
		this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
		this.findViews();
		this.initializeViews();
		
    	IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
    	filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
    	activity.registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
	}
	
	private void findViews(){
		this.startscan_Button = (Button) this.activity.findViewById(R.id.startscan_button);
		this.progressBar = (ProgressBar) this.activity.findViewById(R.id.progressBar1);
	}
	
	private void initializeViews(){
		this.progressBar.setVisibility(View.INVISIBLE);
	}
	
    public String getLocalIpAddress() {
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
    
    public String checkBluetoothStatus(){
    	if (bluetoothAdapter == null){
    		return "Bluetooth NOT support.";
    	}else{
    		if (bluetoothAdapter.isEnabled()){
    			if(bluetoothAdapter.isDiscovering()){
    				return "Bluetooth is currently in device discovery process.";
    			} else {
    				return "Bluetooth is Enabled.";
    			}
    		}else{
    			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
    			this.activity.startActivityForResult(enableBtIntent, 1);
    			return "Bluetooth is NOT Enabled!";
    		}
    	}
    }
    
    public String getMyMAC(){
    	return this.bluetoothAdapter.getAddress();
    }
    

        
    public ArrayAdapter<String> getArrayAdapter(){
    	this.arrayAdapter = new ArrayAdapter<String>(activity, R.layout.list_item);

    	
    	
    	return this.arrayAdapter;
    }
    
    public void startDiscovery(){
    	this.arrayAdapter.clear();
    	this.bluetoothAdapter.startDiscovery();
    	this.startscan_Button.setText("Stop Scanning");
    	this.progressBar.setVisibility(View.VISIBLE);
    }
    
    public void cancelDiscovery(){
    	this.bluetoothAdapter.cancelDiscovery();
    	this.progressBar.setVisibility(View.INVISIBLE);
    }
    
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
            	BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            	// Add the name and address to an array adapter to show in a ListView
            	String deviceInfo = device.getName() + "\n" + device.getAddress();
            	if (!contains(arrayAdapter, deviceInfo)){
            		arrayAdapter.add(device.getName() + "\n" + device.getAddress());
            		arrayAdapter.notifyDataSetInvalidated();
            	}
            }
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            	progressBar.setVisibility(View.INVISIBLE);
            	startscan_Button.setText("Start Scanning");
            	if (arrayAdapter.getCount() == 0) {
            		arrayAdapter.add("No device.");
            	}
            }

        }
    };

    public void unregisterReceiver(){
    	this.activity.unregisterReceiver(mReceiver);
    }
    
    public boolean isDiscovering(){
    	return this.bluetoothAdapter.isDiscovering();
    }

    private boolean contains(ArrayAdapter<String> aa, String s){
        for (int i = 0; i < aa.getCount(); i++){
        	if (aa.getItem(i).equals(s)){
        		return true;
        	}
        }
        return false;
    }
    
    
    
    
    
    
}
