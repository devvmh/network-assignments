package com.utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import android.os.Handler;

public class ClientThread extends Thread {
	
	private String ip;
	private Handler handler;
	private Socket clientSocket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	
	public String[] receivedArray; 
	public String receivedFileName;
	
	public ClientThread(Handler h){
		handler = h;
	}
	
	public void connect(String ipAddress){
		ip = ipAddress;
		start();
	}
	
	
	public void sendDisconnectSignal(){
		try {
			oos.writeInt(999);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void disconnect(){
		try {
			oos.close();
			ois.close();
			clientSocket.close();
			handler.sendEmptyMessage(999);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
		super.run();
		
		try {
			clientSocket = new Socket(ip, 62009);
		} catch (Exception e){
			handler.sendEmptyMessage(99); // cannot connect to server
			return;
		}
		
		handler.sendEmptyMessage(100); // connected to server
		
		try {
			oos = new ObjectOutputStream(clientSocket.getOutputStream());
			ois = new ObjectInputStream(clientSocket.getInputStream());
			
			while (true){
				int type = ois.readInt(); 
				switch (type){
				case 1:
					break;
				case 2:
					SerializedStringArray ssArray = (SerializedStringArray) ois.readObject();
					receivedArray = ssArray.getArray();
					break;
				case 3:
					break;
				case 4: //file request
					receivedFileName = (String) ois.readUTF();
					break;
				case 999:
					break;
				default:
					throw new Exception();
				}
				handler.sendEmptyMessage(type);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendFileList(String[] stringArray){
		try {
			SerializedStringArray ssArray = new SerializedStringArray(stringArray);
			oos.writeInt(2);
			oos.writeObject(ssArray);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendFileListRequest(){
		try {
			oos.writeInt(3);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendFileRequest(String filename){
		try {
			oos.writeInt(4);
			oos.writeUTF(filename);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void clearInbox(){
		this.receivedArray = null;
	}
}
