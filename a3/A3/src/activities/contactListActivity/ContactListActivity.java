package activities.contactListActivity;


import helperClasses.GlobalVariables;
import helperClasses.UserInfoObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import activities.inboxActivity.MessageHelper;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

import com.a3.R;

public class ContactListActivity extends Activity {
	private DbAdapter mDbHelper;
	private ListView listView;
	private ListViewListener listViewListener;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.contactlist);
        
        mDbHelper = new DbAdapter(this);
        mDbHelper.open();
        
        listView = (ListView) findViewById(R.id.contactListView);
        listViewListener = new ListViewListener ();
		this.listView.setOnItemClickListener(listViewListener);
	}//onCreate
	
	@Override
	public void onDestroy () {
		super.onDestroy ();
		mDbHelper.close ();
	}//onDestroy
	
	@Override
	public void onResume () {
		super.onResume ();
		//if extras are passed, that means we're just putting an entry in the contact list
		Bundle extras = getIntent().getExtras(); 
		if(extras != null)
		{
		 	addContact (extras);
		 	finish();
		} else {
			fillData ();
		}//if
	}//onResume
	
	private void addContact(Bundle extras) {
		String internal = extras.getString("internal");
		String external = extras.getString("external");
		String longitude = extras.getString("longitude");
		String latitude = extras.getString("latitude");
		String interests = extras.getString("interests");
		mDbHelper.addContact(internal, external, longitude, latitude, interests);
	}//addContact

	public void fillData () {
        Cursor cursor = mDbHelper.fetchAllNotes();
        startManagingCursor(cursor);
        long currentTime = System.nanoTime ();
        final long DAY = 60000;//1000 * 60 * 60 * 24;
        
        //this gigantic for loop turns the database entries into a contact list
        //the contact list should remain fairly small as entries are purged after 24 hours
        //most of these lines are just roundabout parsing of the Strings
        List<UserInfoObject> userInfoList = new ArrayList<UserInfoObject>();
        for (cursor.moveToFirst (); ! cursor.isAfterLast (); cursor.moveToNext ()) {
        	String ipTuple = cursor.getString (cursor.getColumnIndex ("_iptuple"));
        	int comma = ipTuple.indexOf(",");
        	String internal = ipTuple.substring(0, comma);
        	String external = ipTuple.substring (comma + 1);

        	long entryTime = Long.parseLong(cursor.getString(cursor.getColumnIndex("_ttl")));
        	if (entryTime + DAY > currentTime) {
        		//entries expire after a day
        		mDbHelper.deleteContact(internal, external);
        		continue;
        	}
        	
        	String lat = cursor.getString (cursor.getColumnIndex ("_latitude"));
        	String lon = cursor.getString (cursor.getColumnIndex ("_longitude"));
        	String interests = cursor.getString(cursor.getColumnIndex ("_interests"));
        	
        	UserInfoObject userInfoObject = new UserInfoObject();
			userInfoObject.latitude = Double.parseDouble (lat);
			userInfoObject.longitude = Double.parseDouble(lon);

			//shave off the quotation marks
			userInfoObject.interests = interests;//.substring(1, interests.length () - 1);
        	userInfoObject.intIP = internal;//.substring(1, internal.length ());
			userInfoObject.extIP = external;//.substring(0, external.length () - 1);
			
			userInfoList.add(userInfoObject);
        }//iterate over all rows

        updateUI_loadUserList(userInfoList);
	}//fillData
	
	public void updateUI_loadUserList(List<UserInfoObject> userInfoList){
		SimpleAdapter userListAdapter;
		if (userInfoList.isEmpty ()) {
			String [] from = {"text"};
			int [] to = {R.id.noUsersFound_TextView};
			
			List <Map<String, Object>> noUsersList = new ArrayList <Map<String, Object>> ();
			Map<String, Object> item = new HashMap<String, Object> ();
			item.put ("text", "No Users Found");
			noUsersList.add (item);
			
			userListAdapter = new SimpleAdapter (this, noUsersList, R.layout.no_users_list, from, to);
		} else {
			String[] from = {"img", "distance", "I" +
					"nterests"};
			int[] to = {R.id.img_ImageView, R.id.distance_TextView, R.id.interests_TextView};
			userListAdapter = new SimpleAdapter(this, buildUserListView(userInfoList), R.layout.listitem, from, to);
		}//if
		this.listView.setAdapter(userListAdapter);
	}//updateUI_loadUserList
	
	private List<Map<String, Object>> buildUserListView(List<UserInfoObject> userInfoList) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		UserInfoObject self = GlobalVariables.self;
		
		for (int i = 0; i < userInfoList.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("img", R.drawable.icon);
			map.put("distance", "Distance: " + self.getDistance (userInfoList.get(i)) + " meters away from you.");
			map.put("Interests", "Interests: " + userInfoList.get(i).interests + ".");
			
			//for the contactListActivity
			map.put("interests", userInfoList.get(i).interests);
			map.put("longitude", userInfoList.get(i).longitude);
			map.put("latitude", userInfoList.get(i).latitude);
			map.put("external", userInfoList.get(i).extIP);
			map.put("internal", userInfoList.get(i).intIP);
			list.add(map);
		}
		
		return list;
	}
	
	//adds a contact to the database in ContactListActivity
	public void updateContact (HashMap<String, Object> userMap) {
		String longitude = ((Double) userMap.get("longitude")).toString ();
		String latitude = ((Double) userMap.get("latitude")).toString ();
		String internal = (String) userMap.get("internal");
		String external = (String) userMap.get("external");
		String interests = (String) userMap.get("interests");
		
		//adds to ContactListActivity's database
		Intent intent = new Intent (getApplicationContext(),
				ContactListActivity.class);
		intent.putExtra("internal", internal);
		intent.putExtra("external", external);
		intent.putExtra("longitude", longitude);
		intent.putExtra("latitude", latitude);
		intent.putExtra("interests", interests);
		startActivity(intent);
		return;
	}//updateContact
	
	private class ListViewListener implements OnItemClickListener {
		@SuppressWarnings("unchecked")
		public void onItemClick(AdapterView<?> a, View v, int position, long id) {
			//get the user's IP addresses and send the message
			SimpleAdapter adapter = (SimpleAdapter) a.getAdapter();
			HashMap<String,Object> map = (HashMap<String,Object>) adapter.getItem(position);
			String destExternal = map.get("external").toString ();
			String destInternal = map.get("internal").toString ();
			MessageHelper.showSendMessageDialog(ContactListActivity.this, destInternal, destExternal);
		}//onClick
	}//ListViewListener class
}//ContactListActivity