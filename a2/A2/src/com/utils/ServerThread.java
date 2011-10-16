package com.utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import android.os.Handler;

public class ServerThread extends Thread {
	
	private Handler handler;
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	
	
	
	public String[] receivedArray; 
	
	
	
	
	public ServerThread(Handler h){
		handler = h;
	}
	

	
	
	public void startServer(){
		start();
	}
	
	
	public void stopServer(){
		disconnect();
	}
	
	
	public void run(){
		super.run();
		
		try {
			serverSocket = new ServerSocket(62009);
			clientSocket = serverSocket.accept();
			handler.sendEmptyMessage(101); // server is connected to client
			
			ois = new ObjectInputStream(clientSocket.getInputStream());
			oos = new ObjectOutputStream(clientSocket.getOutputStream());
			
			oos.writeInt(1);
			oos.flush();
			
			
			while (true){
				int type = ois.readInt(); 
				switch (type){
				case 2:
					SerializedStringArray ssArray = (SerializedStringArray) ois.readObject();
					receivedArray = ssArray.getArray();
					break;
				case 3:
					break;
				case 4:
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
			if (oos != null)
				oos.close();
			if (ois != null)
				ois.close();
			if (clientSocket != null)
				clientSocket.close();

			serverSocket.close();
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
	
	public void clearInbox(){
		this.receivedArray = null;
	}
	
	
}
