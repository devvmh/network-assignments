package helperClasses;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import activities.inboxActivity.InboxActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

public class Client {
	//This is a blocking method.
	//Use Http Get to obtain a list of all users except yourself (the parameter)
	//returns null if there was a problem
	public static List<UserInfoObject> getUserInfoList(UserInfoObject self){
		String uri = Constants.URL;
		HttpGet get = new HttpGet(uri);
		HttpClient client = new DefaultHttpClient();
		
		HttpResponse response;
		String result = null;
		
		//Try to query the server using GET
		try {
			response = client.execute(get);
			
	        if(response.getStatusLine().getStatusCode() == 200) 
	        {
	          result = EntityUtils.toString(response.getEntity());
	          System.out.println(result);
	        } else {
	        	return null;
	        }
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Parse result into UserInfoObject list
		List<UserInfoObject> UserInfoList = new ArrayList<UserInfoObject>();
		
		StringTokenizer st = new StringTokenizer(result,"\n");
		
		int n = Integer.parseInt(st.nextToken());
		for (int i = 0; i < n; i++){
			try {
			UserInfoObject userInfoObject = new UserInfoObject();
			userInfoObject.intIP = st.nextToken();
			userInfoObject.extIP = st.nextToken ();
			userInfoObject.latitude = Double.parseDouble(st.nextToken());
			userInfoObject.longitude = Double.parseDouble(st.nextToken());
			userInfoObject.interests = st.nextToken();
			UserInfoObject one = userInfoObject;
			UserInfoObject two = self;
			boolean three = userInfoObject.equals (self);
			boolean four = (! userInfoObject.equals(self));
			if (! userInfoObject.equals(self)) {
				UserInfoList.add(userInfoObject);
			}//if
			} catch (NumberFormatException e) {
				System.out.println ("User wasn't parseable");
				return null;
			}
		}
		
		return UserInfoList;
	}
	
	
	//This is a blocking method
	//Use Http post to send info of 1 user to server
	public static void postUserInfo(UserInfoObject userInfoObject){
		// Construct data
		String data = null;
		try {
			data = URLEncoder.encode("internal", "UTF-8") + "=" + URLEncoder.encode(userInfoObject.intIP, "UTF-8");
			data += "&" + URLEncoder.encode ("external", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(userInfoObject.extIP), "UTF-8");
			data += "&" + URLEncoder.encode("latitude", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(userInfoObject.latitude), "UTF-8");
			data += "&" + URLEncoder.encode("longitude", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(userInfoObject.longitude), "UTF-8");
			data += "&" + URLEncoder.encode("interests", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(userInfoObject.interests), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
	    // Send data
	    try {
			URL url = new URL(Constants.URL);
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(data);
			wr.flush();
			
		    // Get the response
		    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		    String line;
		    while ((line = rd.readLine()) != null) {
		        System.out.println(line);
		    }
		    wr.close();
		    rd.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//this is a blocking method
	//asks www.whatismyip.org what my external ip address is
	public static String checkIP () {
		String uri = Constants.IPServer;
		HttpGet get = new HttpGet(uri);
		HttpClient client = new DefaultHttpClient();
		
		HttpResponse response;
		String result = null;
		
		//Try to query the server using GET
		try {
			response = client.execute(get);
			
			if(response.getStatusLine().getStatusCode() == 200) 
			{
				result = EntityUtils.toString(response.getEntity());
				System.out.println(result);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}//try-catch
		
		return result;
	}//checkIP
	
	public static String getLocalIPAddress() {
		//iterates over all network interfaces' ip addrs
		//courtesy of http://www.droidnova.com/get-the-ip-address-of-your-device,304.html
	    try {
	        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
	            NetworkInterface intf = en.nextElement();
	            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
	                InetAddress inetAddress = enumIpAddr.nextElement();
	                if (!inetAddress.isLoopbackAddress()) {
	                    return inetAddress.getHostAddress().toString();
	                }
	            }
	        }
	    } catch (SocketException e) {
	        e.printStackTrace ();
	    }
	    return null;
	}
	
	

	
	
	
	
	
	
	
}
