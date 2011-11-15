package activities.mainActivity;

import com.a3.R;

import activities.prefActivity.PrefActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;

public class MainActivity extends Activity {
    
	private MainUI ui;
	protected MainModel model;
	private MainControl control;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);      
        
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
		//get the interests string. If it's unset, send them to preferences to set it.
    	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    	String interests = sp.getString ("interests_string", "");
    	if (interests.equals("")) {
    		Intent intent = new Intent (this, PrefActivity.class);
    		startActivity(intent);
    	} else {
    		this.model.setInterests (interests);
    		this.control.onResume();
    	}//if
	}//onResume
	
	protected void onPause() {
		super.onPause();
		this.control.onPause();
	}
	
	 @Override
	 public boolean onCreateOptionsMenu(Menu menu) {
		 return ui.createOptionsMenu (menu, getMenuInflater (), this.getApplicationContext());
	 }//onCreateOptionsMenu

	
}