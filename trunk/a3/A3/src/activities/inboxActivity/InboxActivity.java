package activities.inboxActivity;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.a3.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class InboxActivity extends Activity {

	private ListView messageList_ListView;
	private MessageListObject messageListObject;


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inbox);   

		this.messageList_ListView = (ListView) findViewById(R.id.messageList_ListView);
		this.updateUI_refresh();

	}



	private void updateUI_refresh(){
		this.messageListObject = InboxHelper.getObject(this);

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
			map.put("user", messageObjectList.get(i).from);
			map.put("date", messageObjectList.get(i).date);
			map.put("message", messageObjectList.get(i).message);
			list.add(map);
		}
		return list;
	}

}
