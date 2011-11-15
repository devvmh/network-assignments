package activities.mainActivity;

import helperClasses.UserInfoObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import activities.prefActivity.PrefActivity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.a3.R;


public class MainUI {
	public MainActivity activity;
	public MainControl control;
	
	private Button refresh_Button;
	private ProgressBar progressBar1;
	private TextView loading_TextView;
	
	private ListView userList_ListView;
	
	public void setComponents(MainActivity act, MainControl ctrl){
		this.activity = act;
		this.control = ctrl;
	}
	
	public void init() {
		findViews();
		setListeners();
		
		this.progressBar1.setVisibility(View.INVISIBLE);
		this.loading_TextView.setVisibility(View.INVISIBLE);
	}//init

	public void destroy() {
		return;
	}//destroy

	private void findViews(){
		this.refresh_Button = (Button) activity.findViewById(R.id.refresh_Button);
		this.progressBar1 = (ProgressBar) activity.findViewById(R.id.progressBar1);
		this.loading_TextView = (TextView) activity.findViewById(R.id.loading_TextView);
		this.userList_ListView = (ListView) activity.findViewById(R.id.userList_ListView);
	}//findViews
	
	private void setListeners(){
		this.refresh_Button.setOnClickListener(control.buttonListener);
	}//setListeners
	
	public void updateUI_enableLoading(boolean enable){
		if (enable){
			this.refresh_Button.setEnabled(false);
			this.progressBar1.setVisibility(View.VISIBLE);
			this.loading_TextView.setVisibility(View.VISIBLE);
		} else {
			this.refresh_Button.setEnabled(true);
			this.progressBar1.setVisibility(View.INVISIBLE);
			this.loading_TextView.setVisibility(View.INVISIBLE);
		}
	}
	
	public void updateUI_loadUserList(List<UserInfoObject> userInfoList){
		SimpleAdapter userListAdapter;
		if (userInfoList.isEmpty ()) {
			String [] from = {"text"};
			int [] to = {R.id.noUsersFound_TextView};
			
			List <Map<String, Object>> noUsersList = new ArrayList <Map<String, Object>> ();
			Map<String, Object> item = new HashMap<String, Object> ();
			item.put ("text", "No Users Found");
			noUsersList.add (item);
			
			userListAdapter = new SimpleAdapter (activity, noUsersList, R.layout.no_users_list, from, to);
		} else {
			String[] from = {"img", "userid", "distance", "interests"};
			int[] to = {R.id.img_ImageView, R.id.userid_TextView, R.id.distance_TextView, R.id.interests_TextView};
			userListAdapter = new SimpleAdapter(activity, buildUserListView(userInfoList), R.layout.listitem, from, to);
		}//if
		this.userList_ListView.setAdapter(userListAdapter);
	}//updateUI_loadUserList
	
	
	private List<Map<String, Object>> buildUserListView(List<UserInfoObject> userInfoList) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		for (int i = 0; i < userInfoList.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("img", R.drawable.icon);
			map.put("userid", userInfoList.get(i).userid);
			map.put("distance", "is " + activity.model.getDistance (userInfoList.get(i)) + " meters away from you.");
			map.put("interests", "\"" + userInfoList.get(i).interests + "\"");
			list.add(map);
		}
		
		return list;
	}
	
	public boolean createOptionsMenu (Menu menu, MenuInflater inflater, Context context) {
		 inflater.inflate(R.menu.main_menu, menu);

		 //prefs intent
		 Intent prefsIntent = new Intent(context,
				 PrefActivity.class);
		 MenuItem preferences = menu.findItem(R.id.settings_option_item);
		 preferences.setIntent(prefsIntent);
		 return true;
	}

}
