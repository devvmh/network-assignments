package activities.contactListActivity;

public class ContactListControl {
	private ContactListActivity activity;
	private ContactListModel model;

	public void setComponents(ContactListActivity act, ContactListModel m){
		this.activity = act;
		this.model = m;
	}
	
	public void init() {
		this.model.init();	
	}
	
	public void onResume(){
   		model.onResume();
	}//onResume
	
	public void onPause(){
		model.onPause();
	}//onPause
	
	public void destroy() {
		this.model.destroy();
	}//destroy
}//MainControl class
