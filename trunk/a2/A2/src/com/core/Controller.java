package com.core;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class Controller {
	private Model model;
	
	public StartScanListener startScanListener;
	public BlsQueryListener blsQueryListener;
	public StartServerListener startServerListener;
	public IpListClickListener ipListClickListener;
	public FileListClickListener fileListClickListener;
	
	
	
	public Controller(Model m){
		this.model = m;
		this.startScanListener = new StartScanListener();
		this.blsQueryListener = new BlsQueryListener();
		this.startServerListener = new StartServerListener();
		this.ipListClickListener = new IpListClickListener();
		this.fileListClickListener = new FileListClickListener();
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
	
	public class StartServerListener implements OnClickListener{
		public void onClick(View v) {
			model.startServer();
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
	
}
