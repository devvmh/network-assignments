package com.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Base64;

public class BLS {
	
	private final static String url = "http://blow.cs.uwaterloo.ca/cgi-bin/bls_query.pl";
	
	//Returns a list of QueryResult objects
	public static List<QueryResult> sendQueries(List<String> macList) throws Exception{
		List<QueryResult> resultList = new ArrayList<QueryResult>();
		for (int i = 0; i < macList.size(); i++){
			String mac = macList.get(i);
			QueryResult oneResult = BLS.send1Query(mac);
			resultList.add(oneResult);
		}
		return resultList;
	}
	
	
	//Returns one QueryResult object
	public static QueryResult send1Query(String macAddr) throws Exception{
		Trace.logMessage("BLS_QUERY: " + macAddr);
		
		String uri = url + "?btmachash=" + convertToSHA1(macAddr);
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
        
        QueryResult queryResult = null;

        if (st.countTokens() == 5){
        	queryResult = new QueryResult();
        	queryResult.mac = st.nextToken();
        	queryResult.lanIP = st.nextToken();
        	queryResult.wwanIP = st.nextToken();
        	queryResult.timestamp = st.nextToken();
        	queryResult.humanTimestamp = st.nextToken();
        	
        	Trace.logMessage("BLS_REPLY: " + queryResult.timestamp + ", " + queryResult.lanIP + ", " + queryResult.wwanIP);
        } else {
        	Trace.logMessage("BLS_REPLY: FAILURE");
        }
        
        return queryResult;

	}
	

	
	private static String convertToSHA1(String mac) throws NoSuchAlgorithmException{
		String macTransform = mac.toLowerCase();
		StringTokenizer st = new StringTokenizer(macTransform,":");
		String macTransform2 = "";
		
		while (st.hasMoreTokens()){
			macTransform2 += st.nextToken();
		}
		
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		byte[] byteArray = macTransform2.getBytes();
		md.update(byteArray);
		String BtMacHash = Base64.encodeToString(md.digest(), Base64.NO_WRAP);
		return BtMacHash;
	}
	
	
}
