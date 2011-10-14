package com.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.os.Environment;

public class Trace {
	private final static File traceFile = new File(Environment.getExternalStorageDirectory(),"trace.txt");
	
	private static String now(){
		String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
	    Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
	    return sdf.format(cal.getTime());
	}
	
	public static void logMessage(String message){
		if (!traceFile.exists())
		{
			try	
			{
				traceFile.createNewFile();
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		try
		{
			//BufferedWriter for performance, true to set append to file flag
			BufferedWriter buf = new BufferedWriter(new FileWriter(traceFile, true)); 
			buf.append(Trace.now() + ": " + message);
			buf.newLine();
			buf.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		} 
	}
	
	public static void deleteOldTrace(){
		if (traceFile.exists())
			traceFile.delete();
	}
}
