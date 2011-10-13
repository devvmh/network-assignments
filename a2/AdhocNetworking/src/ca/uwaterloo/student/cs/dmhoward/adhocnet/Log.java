package ca.uwaterloo.student.cs.dmhoward.adhocnet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.os.Environment;

public class Log {
	private static File traceFile = new File(Environment.getExternalStorageDirectory(),"trace.txt");
	
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
			buf.append(now() + ": " + message);
			buf.newLine();
			buf.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	private static String now(){
		String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
	    Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
	    return sdf.format(cal.getTime());
	}
	
}
