package com.utils;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
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
	public String receivedFileName;
	
	
	
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
				System.out.println("message type " + type);
				switch (type){
				case 2:
					SerializedStringArray ssArray = (SerializedStringArray) ois.readObject();
					receivedArray = ssArray.getArray();
					break;
				case 3:
					break;
				case 4: // file request
					receivedFileName = (String) ois.readUTF();
					break;
				case 5:
					String fileName = (String) ois.readUTF();
					System.out.println("received filename:" + fileName);
					SerializedFile sFile = (SerializedFile) ois.readObject();
					sFile.saveFileTo("/mnt/sdcard/" + fileName);	
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
		this.receivedFileName = null;
	}
	
	public void sendFile(String filePath){
		try {
			System.out.println("Entering sendFile()");
			oos.writeInt(5);
			String fileName = new File(filePath).getName();
			oos.writeUTF(fileName);
			SerializedFile sFile = new SerializedFile(filePath);
			oos.writeObject(sFile);
			oos.flush();
			System.out.println("file flushed");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
}
