package activities.firstRunActivity;

import com.a3.*;
import android.widget.Button;


public class FirstRunUI {
	private FirstRunActivity activity;
	private FirstRunControl control; 

	private Button submit_Button;
	
	public void setComponents(FirstRunActivity act, FirstRunControl ctrl){
		this.activity = act;
		this.control = ctrl;
	}
	
	public void init() {
		this.submit_Button = (Button) activity.findViewById(R.id.submit_Button);
		submit_Button.setOnClickListener(this.control.buttonListener);
	}
	
	
	public void destroy() {
		
	}

}
