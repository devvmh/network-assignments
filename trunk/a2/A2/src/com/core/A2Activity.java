package com.core;

import com.utils.Trace;

import android.app.Activity;
import android.os.Bundle;

public class A2Activity extends Activity {


	private UI view;
	private Model model;
	private Controller controller;
	

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Trace.deleteOldTrace();
        
        this.view = new UI(this);
        this.model = new Model(this, this.view);
        this.controller = new Controller(this.model);
        
        this.view.setController(controller);
        
                
        this.controller.initialize();
        
    }
    

   
    
}