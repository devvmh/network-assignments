package com.core;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Controller {
	private Model model;
	
	public StartScanListener startScanListener;
	public BlsQueryListener blsQueryListener;
	public IpListClickListener ipListClickListener;
	public FileListClickListener fileListClickListener;
	public ServerStateBnClickListener serverStateBnClickListener;
	public SendFileListBnListener sendFileListBnListener;
	
	
	
	public Controller(Model m){
		this.model = m;
		this.startScanListener = new StartScanListener();
		this.blsQueryListener = new BlsQueryListener();
		this.ipListClickListener = new IpListClickListener();
		this.fileListClickListener = new FileListClickListener();
		this.serverStateBnClickListener = new ServerStateBnClickListener();
		this.sendFileListBnListener = new SendFileListBnListener();
	}
	
	
	public void initialize(){
		this.model.initialize();
	}
	
	public class StartScanListener implements OnClickListener{
		public void onClick(View arg0) {
			model.startScanning();
		}

	}
	
	
	public class BlsQueryListener implements OnClickListener{
		public void onClick(View v) {
			model.sendQuery();
		}

	}
	
	
	public class IpListClickListener implements OnItemClickListener{
		public void onItemClick(AdapterView<?> a, View v, int position, long id) {
			model.connectToIp(position);
		}
	}
	
	public class FileListClickListener implements OnItemClickListener{
		public void onItemClick(AdapterView<?> a, View v, int position, long id) {
			model.selectFile(position);
		}
	}
	
	public class ServerStateBnClickListener implements OnCheckedChangeListener{

		public void onCheckedChanged(CompoundButton arg0, boolean on) {
			model.startServer(on);
			
		}
		
	}
	
	public class SendFileListBnListener implements OnClickListener{
		public void onClick(View arg0) {
			model.sendFileList();
		}
		
	}
	
}
