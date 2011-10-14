package com.core;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class UI {
	private A2Activity activity;
	private Controller controller;
	
	private Button startscan_Button;
	private TextView lastscan_TextView;
	private TextView myMAC_TextView;
	private TextView myIP_TextView;
	private ListView neighbourMACs_ListView;
	private Button startStopServer_Button;
	private TextView status_TextView;
	private ProgressBar progressBar1;
	private Button sendBLSquery_Button;
	private ProgressBar progressBar2;
	private ListView ipList_ListView;
	
	private ArrayAdapter<String> bluetoothArrayAdapter;
	private ArrayAdapter<String> queryResultArrayAdapter;
	
	
	public UI(A2Activity act){
		this.activity = act;
	}
	
	public void setController(Controller c){
		this.controller = c;
	}
	
    public void initialize(){
		this.findViews();
		this.setListeners();
		
    	this.startscan_Button.setText("Start Scanning");
    	this.lastscan_TextView.setText("Never Scanned.");
    	this.myMAC_TextView.setText("No mac");
    	this.myIP_TextView.setText("No ip");
    	this.sendBLSquery_Button.setText("Send BLS Query");
    	this.startStopServer_Button.setText("Start Server");
    	
    	this.sendBLSquery_Button.setEnabled(false);
    	this.startStopServer_Button.setEnabled(false);
    	this.progressBar1.setVisibility(View.INVISIBLE);
    	this.progressBar2.setVisibility(View.INVISIBLE);
    	this.status_TextView.setText("No status.");
    	
    	
    	this.bluetoothArrayAdapter = new ArrayAdapter<String>(activity, R.layout.list_item);
    	this.neighbourMACs_ListView.setAdapter(this.bluetoothArrayAdapter);
    	
    	this.queryResultArrayAdapter = new ArrayAdapter<String>(activity, R.layout.list_item);
    	this.ipList_ListView.setAdapter(this.queryResultArrayAdapter);
    	
    }
    
    private void findViews(){
		this.startscan_Button = (Button) this.activity.findViewById(R.id.startscan_button);
		this.lastscan_TextView = (TextView) this.activity.findViewById(R.id.lastscantime);
		this.myMAC_TextView = (TextView) this.activity.findViewById(R.id.myMACaddr);
		this.myIP_TextView = (TextView) this.activity.findViewById(R.id.myIPaddr);
		this.progressBar1 = (ProgressBar) this.activity.findViewById(R.id.progressBar1);
		this.neighbourMACs_ListView = (ListView) this.activity.findViewById(R.id.neighbourMACs);
		this.startStopServer_Button = (Button) this.activity.findViewById(R.id.startStopServer_button);
		this.status_TextView = (TextView) this.activity.findViewById(R.id.status);
		this.sendBLSquery_Button = (Button) this.activity.findViewById(R.id.sendBLSquery_Button);
		this.progressBar2 = (ProgressBar) this.activity.findViewById(R.id.progressBar2);
		this.ipList_ListView = (ListView) this.activity.findViewById(R.id.queryResult_listView);
    }
    
    public void setListeners(){
    	this.startscan_Button.setOnClickListener(this.controller.startScanListener);
    	this.sendBLSquery_Button.setOnClickListener(this.controller.blsQueryListener);
    	this.startStopServer_Button.setOnClickListener(this.controller.startServerListener);
    }
    
    
    public void updateUI_setMyMac(String myMac){
    	this.myMAC_TextView.setText(myMac);
    }
    
    public void updateUI_setMyip(String ip){
    	this.myIP_TextView.setText(ip);
    }
    
    public void updateUI_setStartScanningBnLael(String label){
    	this.startscan_Button.setText(label);
    }
    
    public void updateUI_setLastScann(String time){
    	this.lastscan_TextView.setText("Last Scan: " + time);
    }
    
    public void updateUI_addItemToMACList(String item){
    	this.bluetoothArrayAdapter.add(item);
    }
    
    public void updateUI_clearMACList(){
    	this.bluetoothArrayAdapter.clear();
    }
    
    
    public void updateUI_addItemToQueryResultList(String item){
    	this.queryResultArrayAdapter.add(item);
    }
    
    public void updateUI_clearQueryResultList(){
    	this.queryResultArrayAdapter.clear();
    }
    
    public void updateUI_setProgressbar1Visible(boolean isVisible){
    	if (isVisible){
    		this.progressBar1.setVisibility(View.VISIBLE);
    	} else {
    		this.progressBar1.setVisibility(View.INVISIBLE);
    	}
    }
    
    public void updateUI_setProgressbar2Visible(boolean isVisible){
    	if (isVisible){
    		this.progressBar2.setVisibility(View.VISIBLE);
    	} else {
    		this.progressBar2.setVisibility(View.INVISIBLE);
    	}
    }
    
    public void updateUI_setSendQueryBnEnabled(boolean enable){
    	this.sendBLSquery_Button.setEnabled(enable);
    }
    
    
    
    
    
}
