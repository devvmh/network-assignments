package activities.contactListActivity;

public class ContactListModel {
	private ContactListActivity activity;
	private ContactListUI ui;
	
	public void setComponents(ContactListActivity act, ContactListUI u){
		this.activity = act;
		this.ui = u;
	}
	
	public void init() {
		this.ui.init();
	}
	
	public void onResume() {
		return;
	}//onResume
	
	public void onPause() {
		return;
	}//onPause
	
	public void destroy() {		
		this.ui.destroy();
	}//destroy
}//MainModel class
