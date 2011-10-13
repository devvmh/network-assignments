package ca.uwaterloo.student.cs.dmhoward.adhocnet;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.StringTokenizer;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Base64;

public class BLS {
	public String mac;
	public String lanIP;
	public String wwanIP;
	public String timestamp;
	public String humanTimestamp;
	
	private final String url = "http://blow.cs.uwaterloo.ca/cgi-bin/bls_query.pl";
	
	
	public BLS(){
		this.mac = null;
		this.lanIP = null;
		this.wwanIP = null;
		this.timestamp = null;
		this.humanTimestamp = null;
	}
	
	public void SendQuery(String macAddr) throws Exception{
		String uri = this.url + "?btmachash=" + convertToSHA1(macAddr);
		HttpGet get = new HttpGet(uri);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(get);
		
		String result;
		
        if(response.getStatusLine().getStatusCode() == 200) 
        {
          result = EntityUtils.toString(response.getEntity());
        }
        else
        {
        	throw new Exception();
        }
        
        StringTokenizer st = new StringTokenizer(result,"\n");
        
        this.mac = st.nextToken();
        this.lanIP = st.nextToken();
        this.wwanIP = st.nextToken();
        this.timestamp = st.nextToken();
        this.humanTimestamp = st.nextToken();
        
	}
	
	private String convertToSHA1(String mac) throws NoSuchAlgorithmException{
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		byte[] byteArray = mac.getBytes();
		md.update(byteArray);
		String BtMacHash = Base64.encodeToString(md.digest(), 0);
		return BtMacHash;
	}
	
//	private String convertToMAC(String sha1) throws NoSuchAlgorithmException{
//		byte[] byteArray =  Base64.decode(sha1, 0);
//		MessageDigest md = MessageDigest.getInstance("SHA-1");
//		byte[] byteArray =  
//		md.update(byteArray);
//	}
	
	
}
