package activities.inboxActivity;

import helperClasses.Client;
import helperClasses.Constants;
import helperClasses.GlobalVariables;

import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.a3.R;

public class ComposeMessageActivity extends Activity {
	private TextView userid_TextView;
	private EditText editMessage_Edittext;
	private Button send_Button;
	private Button reset_Button;
	
	private String destInternal;
	private String destExternal;
	private Activity thisActiviy;
	
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.composemessage);   
        
        thisActiviy = this;
        
        userid_TextView = (TextView) findViewById(R.id.userid_TextView);
        editMessage_Edittext = (EditText) findViewById(R.id.editMessage_EditText);
        send_Button = (Button) findViewById(R.id.send_Button);
        reset_Button = (Button) findViewById(R.id.reset_Button);
        
        ButtonListener buttonListener = new ButtonListener();
        send_Button.setOnClickListener(buttonListener);
        reset_Button.setOnClickListener(buttonListener);
        
        Bundle extras = getIntent().getExtras();
        destInternal = extras.getString(Constants.DestInternal);
        destExternal = extras.getString(Constants.DestExternal);
        userid_TextView.setText(destInternal + "/" + destExternal);
        
    }
    
	private class ButtonListener implements OnClickListener{
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.send_Button:
				(new PostMessageTask()).execute();
				break;
			case R.id.reset_Button:
				editMessage_Edittext.setText("");
				break;
			}//switch
		}//onClick
	}//ButtonListener

	private class PostMessageTask extends AsyncTask<String, Integer, String>{
		private List<MessageObject> messageList;
		
		protected void onPreExecute() {
			super.onPreExecute();
			send_Button.setEnabled(false);
		}//onPreExecute
		
		protected String doInBackground(String... arg0) {
			String message = editMessage_Edittext.getText().toString();
			
			messageList = Client.postUserInfoWithMessage(GlobalVariables.self, destInternal, destExternal, message);

			
			return null;
		}//doInBackground
		
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (messageList.size() != 0){
				for (int i = 0; i < messageList.size(); i++){
					MessageHelper.addMessage(thisActiviy, messageList.get(i));
				}
				MessageHelper.showViewMessageDialog(thisActiviy, "unknown");
			}
			
			send_Button.setEnabled(true);
			finish();
		}//onPostExecute
	}//PostMessageTask
}//ComposeMessageActivity
