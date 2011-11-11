package helperClasses;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class Client {
	
	
	//This is a blocking method.
	//Use Http Get to obtain a list of all users.
	public static List<UserInfoObject> getUserInfoList(){
		String uri = Constants.URL + "some variable";
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
			UserInfoObject userInfoObject = new UserInfoObject();
			userInfoObject.userid = st.nextToken();
			userInfoObject.latitude = Double.parseDouble(st.nextToken());
			userInfoObject.longitude = Double.parseDouble(st.nextToken());
			userInfoObject.interest = st.nextToken();
			UserInfoList.add(userInfoObject);
		}
		
		return UserInfoList;
	}
	
	
	//This is a blocking method
	//Use Http post to send info of 1 user to server
	public static void postUserInfo(UserInfoObject userInfoObject){
		// Construct data
		String data = null;
		try {
			data = URLEncoder.encode("userid", "UTF-8") + "=" + URLEncoder.encode(userInfoObject.userid, "UTF-8");
			data += "&" + URLEncoder.encode("lat", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(userInfoObject.latitude), "UTF-8");
			data += "&" + URLEncoder.encode("long", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(userInfoObject.longitude), "UTF-8");
			data += "&" + URLEncoder.encode("string", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(userInfoObject.interest), "UTF-8");
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
}
