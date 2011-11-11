package activities.mainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import helperClasses.UserInfoObject;

import com.a3.R;

import android.app.LauncherActivity.ListItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;


public class MainUI {
	public MainActivity activity;
	public MainControl control;
	
	private Button refresh_Button;
	private ProgressBar progressBar1;
	private TextView loading_TextView;
	
	private ListView userList_ListView;
	private ArrayAdapter<String> userListArrayAdapter;
	
	public void setComponents(MainActivity act, MainControl ctrl){
		this.activity = act;
		this.control = ctrl;
	}
	
	public void init() {
		findViews();
		setListeners();
		
		this.progressBar1.setVisibility(View.INVISIBLE);
		this.loading_TextView.setVisibility(View.INVISIBLE);
		
//		this.userListArrayAdapter = new ArrayAdapter<String>(activity, R.layout.list_item);
//		this.userList_ListView.setAdapter(userListArrayAdapter);
	}

	public void destroy() {
		
	}
	

	
	private void findViews(){
		this.refresh_Button = (Button) activity.findViewById(R.id.refresh_Button);
		this.progressBar1 = (ProgressBar) activity.findViewById(R.id.progressBar1);
		this.loading_TextView = (TextView) activity.findViewById(R.id.loading_TextView);
		this.userList_ListView = (ListView) activity.findViewById(R.id.userList_ListView);
	}
	
	private void setListeners(){
		this.refresh_Button.setOnClickListener(control.buttonListener);
	}
	
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
		String[] ids = {"img", "userid", "distance", "interests"};
		int[] views = {R.id.img_ImageView, R.id.userid_TextView, R.id.distance_TextView, R.id.interests_TextView};
		
		SimpleAdapter userListAdapter = new SimpleAdapter(activity, buildUserListView(userInfoList), R.layout.listitem, ids, views);
		this.userList_ListView.setAdapter(userListAdapter);
		
	}
	
	
	private List<Map<String, Object>> buildUserListView(List<UserInfoObject> userInfoList) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		for (int i = 0; i < userInfoList.size(); i++){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("img", R.drawable.icon);
			map.put("userid", userInfoList.get(i).userid);
			map.put("distance", userInfoList.get(i).latitude);
			map.put("interests", userInfoList.get(i).interest);
			list.add(map);
		}
		
		return list;
	}

}
