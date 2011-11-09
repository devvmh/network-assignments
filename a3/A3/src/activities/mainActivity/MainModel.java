package activities.mainActivity;


public class MainModel {
	private MainActivity activity;
	private MainUI ui;
	
	public void setComponents(MainActivity act, MainUI u){
		this.activity = act;
		this.ui = u;
	}
	
	public void init() {
		this.ui.init();
	}

	public void destroy() {
		this.ui.destroy();
	}

}
