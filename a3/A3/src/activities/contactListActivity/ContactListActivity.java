package activities.contactListActivity;

import android.app.Activity;
import android.os.Bundle;

import com.a3.R;

public class ContactListActivity extends Activity {
	private ContactListUI ui;
	protected ContactListModel model;
	private ContactListControl control;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);      
        
        this.ui = new ContactListUI();
        this.model = new ContactListModel();
        this.control = new ContactListControl();
        
        this.ui.setComponents(this, this.control);
        this.model.setComponents(this, ui);
        this.control.setComponents(this, model);
        
        this.control.init();
    }
    
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.control.destroy();
	}
	
	protected void onResume() {
		super.onResume();
    	this.control.onResume();
	}//onResume
	
	protected void onPause() {
		super.onPause();
		this.control.onPause();
	}
}//ContactListActivity