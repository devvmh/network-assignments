package activities.mainActivity;

import com.a3.R;

import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MainUI {
	public MainActivity activity;
	public MainControl control;
	
	private Button refresh_Button;
	private ProgressBar progressBar1;
	private TextView loading_TextView;
	
	public void setComponents(MainActivity act, MainControl ctrl){
		this.activity = act;
		this.control = ctrl;
	}
	
	public void init() {
		findViews();
		setListeners();
		
		this.progressBar1.setVisibility(View.INVISIBLE);
		this.loading_TextView.setVisibility(View.INVISIBLE);
	}

	public void destroy() {
		
	}
	
	public void updateUI_enableLoading(boolean enable){
		if (enable){
			this.progressBar1.setVisibility(View.VISIBLE);
			this.loading_TextView.setVisibility(View.VISIBLE);
		} else {
			this.progressBar1.setVisibility(View.INVISIBLE);
			this.loading_TextView.setVisibility(View.INVISIBLE);
		}
	}
	
	private void findViews(){
		this.refresh_Button = (Button) activity.findViewById(R.id.refresh_Button);
		this.progressBar1 = (ProgressBar) activity.findViewById(R.id.progressBar1);
		this.loading_TextView = (TextView) activity.findViewById(R.id.loading_TextView);
	}
	
	private void setListeners(){
		this.refresh_Button.setOnClickListener(control.buttonListener);
	}

}
