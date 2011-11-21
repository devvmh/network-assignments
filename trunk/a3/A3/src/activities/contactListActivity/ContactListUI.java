package activities.contactListActivity;

public class ContactListUI {
	public ContactListActivity activity;
	public ContactListControl control;
	
	public void setComponents(ContactListActivity act, ContactListControl ctrl){
		this.activity = act;
		this.control = ctrl;
	}
	
	public void init() {
		findViews();
		setListeners();
	}//init

	public void destroy() {
		return;
	}//destroy

	private void findViews(){
		return;
	}//findViews
	
	private void setListeners(){
		return;
	}//setListeners
}//ContactListUI
