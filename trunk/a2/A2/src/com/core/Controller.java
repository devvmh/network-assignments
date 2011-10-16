package com.core;

import android.os.Handler;
import android.os.Message;
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
	public DisconnectBnListener disconnectBnListener;
	public ServerMessageHandler serverMessageHandler;
	
	
	
	public Controller(Model m){
		this.model = m;
		this.startScanListener = new StartScanListener();
		this.blsQueryListener = new BlsQueryListener();
		this.ipListClickListener = new IpListClickListener();
		this.fileListClickListener = new FileListClickListener();
		this.serverStateBnClickListener = new ServerStateBnClickListener();
		this.sendFileListBnListener = new SendFileListBnListener();
		this.disconnectBnListener = new DisconnectBnListener();
		this.serverMessageHandler = new ServerMessageHandler();
	}
	
	
	public void initialize(){
		this.model.initialize();
		this.model.setServerHandler(this.serverMessageHandler);
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
			model.connectTo(position);
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
			model.sendFilelistRequest();
		}
		
	}
	
	public class DisconnectBnListener implements OnClickListener{
		public void onClick(View v){
			model.disconnect(false);
		}
	}
	
	public class ServerMessageHandler extends Handler{
		public void handleMessage(Message msg){
			System.out.println(msg.what);
			switch (msg.what){
			case 1:
				break;
			case 2:
				model.stringArrayArrived();
				break;
			case 3:
				model.sendFileList();
				break;
			case 4:
				model.sendFile();
				break;
				
			case 99:
				model.cannotConnectToServer();
				break;
			case 100:
				model.connectedToServer();
				break;
			case 101:
				model.connectedToClient();
				break;
			case 999:
				model.disconnect(true);
			}
		}
	}
	
	
	
}
