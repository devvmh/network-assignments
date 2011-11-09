package activities.mainActivity;

import com.a3.R;

import activities.firstRunActivity.FirstRunActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import helperClasses.Constants;

public class MainActivity extends Activity {
    
	private MainUI ui;
	private MainModel model;
	private MainControl control;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        SharedPreferences sp = getSharedPreferences(Constants.PREF_FILE, 0);
        boolean isFirstRun = sp.getBoolean("isFirstRun", true);
        if (isFirstRun){
        	Intent intent = new Intent(this, FirstRunActivity.class);
        	startActivity(intent);
        }
        
        
        
        this.ui = new MainUI();
        this.model = new MainModel();
        this.control = new MainControl();
        
        this.ui.setComponents(this, this.control);
        this.model.setComponents(this, ui);
        this.control.setComponents(this, model);
        
        this.control.init();
        
        
    }
    
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		this.control.destroy();
	}
	
	protected void onResume() {
		super.onResume();
		this.control.onResume();
	}
	
	protected void onPause() {
		super.onPause();
		this.control.onPause();
	}
	
	
}