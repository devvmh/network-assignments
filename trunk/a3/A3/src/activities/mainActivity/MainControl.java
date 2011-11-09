package activities.mainActivity;

import android.app.Activity;

public class MainControl {
	private MainActivity activity;
	private MainModel model;

	public void setComponents(MainActivity act, MainModel m){
		this.activity = act;
		this.model = m;
	}
	
	public void init() {
		this.model.init();	
	}

	public void destroy() {
		this.model.destroy();
	}
	
}
