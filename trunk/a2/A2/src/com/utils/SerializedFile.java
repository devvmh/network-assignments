package com.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;

public class SerializedFile implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private byte[] fileInBytes;
	

	public SerializedFile(String path) throws Exception {
		File file = new File(path);
		FileInputStream fis = new FileInputStream(file);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		byte[] buffer = new byte[1];
		
        for (int readNum; (readNum = fis.read(buffer)) != -1;) 
            bos.write(buffer, 0, readNum);
            
        this.fileInBytes = bos.toByteArray();
        bos.close();
        fis.close();
	}
	
	public void saveFileTo(String path) throws Exception {
		File file = new File(path);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(fileInBytes);
        fos.flush();
        fos.close();
	}
	
}
