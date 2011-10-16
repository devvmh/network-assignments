package com.utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

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
	



//	public boolean sendStringArray(String[] sArray){
//		try {
//			SerializedStringArray ssArray = new SerializedStringArray(sArray);
//			oos.writeInt(2);
//			oos.writeObject(ssArray);
//			oos.flush();
//			oos.close();
//			return true;
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println(e.getMessage());
//		}
//		return false;
//	}
	

	
	
}
