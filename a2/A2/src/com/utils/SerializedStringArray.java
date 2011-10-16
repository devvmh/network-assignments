package com.utils;

import java.io.Serializable;

public class SerializedStringArray implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String[] stringArray = null;
	
	public SerializedStringArray(String[] sArray){
		this.stringArray = sArray;
	}
	
	public String[] getArray(){
		return this.stringArray;
	}

}
