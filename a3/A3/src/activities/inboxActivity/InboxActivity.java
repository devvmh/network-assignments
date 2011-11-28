package activities.inboxActivity;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import activities.contactListActivity.ContactListActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

import com.a3.R;

public class InboxActivity extends Activity {

	private ListView messageList_ListView;
	private ListViewListener listViewListener;
	private MessageListObject messageListObject;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inbox);   

		listViewListener = new ListViewListener ();
		this.messageList_ListView = (ListView) findViewById(R.id.messageList_ListView);
		messageList_ListView.setOnItemClickListener (listViewListener);
		this.updateUI_refresh();

	}



	private void updateUI_refresh(){
		this.messageListObject = MessageHelper.getObject(this);

		SimpleAdapter userListAdapter = null;
		if (messageListObject.messageRecievedList.isEmpty ()) {

		} else {
			String[] from = {"user", "date", "message"};
			int[] to = {R.id.user_TextView, R.id.date_TextView, R.id.message_TextView};
			userListAdapter = new SimpleAdapter(this, buildMessageListView(messageListObject.messageRecievedList), R.layout.messageitem, from, to);
		}//if
		this.messageList_ListView.setAdapter(userListAdapter);
	}



	private List<Map<String, String>> buildMessageListView(List<MessageObject> messageObjectList) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		for (int i = 0; i < messageObjectList.size(); i++){
			Map<String, String> map = new HashMap<String, String>();
			MessageObject curMessage = messageObjectList.get(i);
			map.put("user", curMessage.senderInternal + "/" + curMessage.senderExternal);
			map.put("date", curMessage.date);
			map.put("message", curMessage.message);
			list.add(map);
		}
		return list;
	}

	 @Override
	 public boolean onCreateOptionsMenu(Menu menu) {
		 getMenuInflater ().inflate(R.menu.inbox_menu, menu);
		 MenuItem clear = menu.findItem (R.id.clear_option_item);
		 clear.setOnMenuItemClickListener(new OnMenuItemClickListener() {
				public boolean onMenuItemClick(MenuItem item) {
					MessageHelper.clearMessages (InboxActivity.this);
					InboxActivity.this.updateUI_refresh ();
					return true;
				}
			});
		 return true;
	 }//onCreateOptionsMenu
	 
		public class ListViewListener implements OnItemClickListener {

			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position, long id) {
				//get the user's info
				SimpleAdapter adapter = (SimpleAdapter) a.getAdapter();
				HashMap<String,Object> map = (HashMap<String,Object>) adapter.getItem(position);
				String ipTuple = map.get("user").toString ();
				int slash = ipTuple.indexOf('/');
				String destInternal = ipTuple.substring(0,slash);
				String destExternal = ipTuple.substring(slash+1, ipTuple.length());
				
				//send the message
				MessageHelper.showSendMessageDialog(InboxActivity.this, destInternal, destExternal);
			}
		}//ListViewListener class
}
