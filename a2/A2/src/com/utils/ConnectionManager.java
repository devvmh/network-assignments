package com.utils;

import android.os.Handler;

public class ConnectionManager {
	
	private enum Kind{Server, Client, None;}
	
	
	public Handler handler;
	
	private ServerThread serverThread;
	private ClientThread clientThread;
	
	private Kind myKind;
	


	public ConnectionManager(){
		myKind = Kind.None;
	}

	public void setHandler(Handler h){
		this.handler = h;
		

	}


	public void sendFileListRequest(){
		if (myKind == Kind.Client){
			this.clientThread.sendFileListRequest();
		} else {
			this.serverThread.sendFileListRequest();
		}
	}



	public void sendFileList(String[] stringArray){
		if (myKind == Kind.Client){
			this.clientThread.sendFileList(stringArray);
		} else {
			this.serverThread.sendFileList(stringArray);
		}
	}
	
	public void connectTo(String ip){
		this.clientThread = new ClientThread(handler);
		clientThread.connect(ip);
		myKind = Kind.Client;
	}
	
	public void startServer(){
		this.serverThread = new ServerThread(handler);
		this.serverThread.startServer();
		myKind = Kind.Server;
	}
	
	public void stopServer(){
		this.serverThread.stopServer();
		myKind = Kind.None;
	}

	public void disconnect(boolean isFromExternal){

		try {
			if (myKind == Kind.None)
				throw new Exception();
		} catch (Exception e){
			e.printStackTrace();
		}
		
		if (myKind == Kind.Server){
			if (!isFromExternal)
				this.serverThread.sendDisconnectSignal();
			this.serverThread.disconnect();
			this.startServer();
		}  else if (myKind == Kind.Client){
			if (!isFromExternal)
				this.clientThread.sendDisconnectSignal();
			this.clientThread.disconnect();
			myKind = Kind.None;
		}
	}
	
	
	
	public String[] getReceivedArray(){
		if (myKind == Kind.Client){
			return this.clientThread.receivedArray;
		}
		
		if (myKind == Kind.Server){
			return this.serverThread.receivedArray;
		}
		
		return null;
	}
	
	public void clearInbox(){
		if (myKind == Kind.Client){
			this.clientThread.clearInbox();
		}
		
		if (myKind == Kind.Server){
			this.serverThread.clearInbox();
		}
	}
	
	public void sendFileRequest(String filename){
		if (myKind == Kind.Client){
			this.clientThread.sendFileRequest(filename);
		}
		
		if (myKind == Kind.Server){
			this.serverThread.sendFileRequest(filename);
		}
	}
	
	public String getReceivedFileName(){
		if (myKind == Kind.Client){
			return this.clientThread.receivedFileName;
		}
		
		if (myKind == Kind.Server){
			return this.serverThread.receivedFileName;
		}
		
		return null;
	}
	

	
	
}
