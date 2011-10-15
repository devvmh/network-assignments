package com.utils;

import java.io.Serializable;

public class SerializedStringArray implements Serializable {
	private String[] stringArray = null;
	
	public SerializedStringArray(String[] sArray){
		this.stringArray = sArray;
	}
	
	public String[] getArray(){
		return this.stringArray;
	}

}
