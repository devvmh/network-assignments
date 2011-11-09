package activities.firstRunActivity;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import com.a3.*;

public class FirstRunControl{
	private FirstRunActivity activity;
	private FirstRunModel model;
	

	public ButtonListener buttonListener;
	
	
	public void setComponents(FirstRunActivity act, FirstRunModel m){
		this.activity = act;
		this.model = m;
	}
	
	public void init() {
		this.buttonListener = new ButtonListener();
		
		
		this.model.init();
	}

	public void destroy() {
		this.model.destroy();
	}
	
	public class ButtonListener implements OnClickListener{
		public void onClick(View v) {
			EditText userid_EditText = (EditText) activity.findViewById(R.id.userid_EditText);
			EditText interest_EditText = (EditText) activity.findViewById(R.id.interest_EditText);
			
			String userid = userid_EditText.getText().toString();
			String interest = interest_EditText.getText().toString();
			
			model.submitInfo(userid, interest);
		}
	}

}
