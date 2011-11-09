package activities.firstRunActivity;


public class FirstRunModel{
	private FirstRunActivity activity;
	private FirstRunUI ui;

	public void setComponents(FirstRunActivity act, FirstRunUI u){
		this.activity = act;
		this.ui = u;
	}
	public void init() {
		this.ui.init();
	}
	
	public void submitInfo(String id, String interest){
		System.out.println("userid is " + id);
		System.out.println("interest is " + interest);
		activity.finish();
	}

	public void destroy() {
		this.ui.destroy();
	}

}
