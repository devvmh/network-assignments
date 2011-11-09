package activities.firstRunActivity;

import com.a3.R;

import android.app.Activity;
import android.os.Bundle;

public class FirstRunActivity extends Activity {
	
	private FirstRunUI ui;
	private FirstRunModel model;
	private FirstRunControl control;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstrun);
        
        this.ui = new FirstRunUI();
        this.model = new FirstRunModel();
        this.control = new FirstRunControl();
        
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
}
