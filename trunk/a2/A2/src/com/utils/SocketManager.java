package com.utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketManager {
	private ServerSocket serverSocket;
	private Socket socket;
	private String destinationIP;
	private String[] stringArray;
	
	public SocketManager(){
		
	}
	
	public void setDestination(String serverIP){
		this.destinationIP = serverIP;
	}
	

	public void startServer(){
		try {
			serverSocket = new ServerSocket(62009);
			socket = serverSocket.accept();
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			SerializedStringArray ssArray = (SerializedStringArray) ois.readObject();
			stringArray = ssArray.getArray();
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public boolean connect(){
		try {
			this.socket = new Socket(this.destinationIP, 62009);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return false;
	}
	
	public boolean sendStringArray(String[] sArray){
		try {
			SerializedStringArray ssArray = new SerializedStringArray(sArray);
			ObjectOutputStream oos = new ObjectOutputStream(this.socket.getOutputStream());
			oos.writeObject(ssArray);
			oos.flush();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public void shutdownServer(){
		try {
			this.serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String[] getStringArray(){
		return this.stringArray;
	}
	
	
}
