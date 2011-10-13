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
	public ListClickListener listClickListener;
	
	
	public Controller(Model m){
		this.model = m;
		this.startScanListener = new StartScanListener();
		this.blsQueryListener = new BlsQueryListener();
		this.startServerListener = new StartServerListener();
		this.listClickListener = new ListClickListener();
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

		}
	}
	
	public class ListClickListener implements OnItemClickListener{
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {


		}
	}
	
}
