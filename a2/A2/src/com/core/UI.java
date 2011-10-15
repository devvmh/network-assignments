package com.core;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

public class UI {
	private A2Activity activity;
	private Controller controller;
	
	private Button startscan_Button;
	private TextView lastscan_TextView;
	private TextView myMAC_TextView;
	private TextView myIP_TextView;
	
	private TextView status_TextView;
	private ProgressBar progressBar1;
	private Button sendBLSquery_Button;
	private ProgressBar progressBar2;
	private ToggleButton serverState_Button;
	private Button sendFileList_Button;
	
	private ListView macList_ListView;
	private ListView ipList_ListView;
	private ListView fileList_ListView;
	
	private ArrayAdapter<String> macListArrayAdapter;
	private ArrayAdapter<String> ipListArrayAdapter;
	private ArrayAdapter<String> fileListArrayAdapter;
	
	
	
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
    	this.sendFileList_Button.setText("Send My file list");
    	
    	
    	this.sendBLSquery_Button.setEnabled(false);
    	
    	this.sendFileList_Button.setEnabled(false);
    	
    	this.progressBar1.setVisibility(View.INVISIBLE);
    	this.progressBar2.setVisibility(View.INVISIBLE);
    	    	
    	this.status_TextView.setText("No status.");
    	
    	
    	this.macListArrayAdapter = new ArrayAdapter<String>(activity, R.layout.list_item);
    	this.macList_ListView.setAdapter(this.macListArrayAdapter);
    	
    	this.ipListArrayAdapter = new ArrayAdapter<String>(activity, R.layout.list_item);
    	this.ipList_ListView.setAdapter(this.ipListArrayAdapter);
    	
    	this.fileListArrayAdapter = new ArrayAdapter<String>(activity, R.layout.list_item);
    	this.fileList_ListView.setAdapter(fileListArrayAdapter);
    	
    	
    }
    
    private void findViews(){
		this.startscan_Button = (Button) this.activity.findViewById(R.id.startscan_button);
		this.lastscan_TextView = (TextView) this.activity.findViewById(R.id.lastscantime);
		this.myMAC_TextView = (TextView) this.activity.findViewById(R.id.myMACaddr);
		this.myIP_TextView = (TextView) this.activity.findViewById(R.id.myIPaddr);
		this.progressBar1 = (ProgressBar) this.activity.findViewById(R.id.progressBar1);
		this.macList_ListView = (ListView) this.activity.findViewById(R.id.macList_ListView);
		this.status_TextView = (TextView) this.activity.findViewById(R.id.status);
		this.sendBLSquery_Button = (Button) this.activity.findViewById(R.id.sendBLSquery_Button);
		this.progressBar2 = (ProgressBar) this.activity.findViewById(R.id.progressBar2);
		this.ipList_ListView = (ListView) this.activity.findViewById(R.id.queryResult_listView);
		this.fileList_ListView = (ListView) this.activity.findViewById(R.id.fileList_ListView);
		this.serverState_Button = (ToggleButton) this.activity.findViewById(R.id.serverState_Button);
		this.sendFileList_Button = (Button) this.activity.findViewById(R.id.sendFileList_Button);
    }
    
    public void setListeners(){
    	this.startscan_Button.setOnClickListener(this.controller.startScanListener);
    	this.sendBLSquery_Button.setOnClickListener(this.controller.blsQueryListener);
    	this.ipList_ListView.setOnItemClickListener(this.controller.ipListClickListener);
    	this.fileList_ListView.setOnItemClickListener(this.controller.fileListClickListener);
    	this.serverState_Button.setOnCheckedChangeListener(this.controller.serverStateBnClickListener);
    	this.sendFileList_Button.setOnClickListener(this.controller.sendFileListBnListener);
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
    	this.macListArrayAdapter.add(item);
    }
    
    public void updateUI_clearMACList(){
    	this.macListArrayAdapter.clear();
    }
    
    
    public void updateUI_addItemToQueryResultList(String item){
    	this.ipListArrayAdapter.add(item);
    }
    
    public void updateUI_clearQueryResultList(){
    	this.ipListArrayAdapter.clear();
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
    
    public void updateUI_enableIpList(boolean enable){
    	this.ipList_ListView.setEnabled(enable);
    }
    
    public void updateUI_addItemToFileList(String item){
    	this.fileListArrayAdapter.add(item);
    }
    
    public void updateUI_clearFileList(){
    	this.fileListArrayAdapter.clear();
    }
    
    public void updateUI_setStatus(String status){
    	this.status_TextView.setText(status);
    }
    
    public void updateUI_setSendFilelistEnabled(boolean enable){
    	this.sendFileList_Button.setEnabled(enable);
    }
    
}
