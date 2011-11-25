package activities.inboxActivity;

import helperClasses.Constants;

import com.a3.R;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ComposeMessageActivity extends Activity {
	private TextView userid_TextView;
	private EditText editMessage_Edittext;
	private Button send_Button;
	private Button reset_Button;
	
	private String userid;
	
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.composemessage);   
        
        userid_TextView = (TextView) findViewById(R.id.userid_TextView);
        editMessage_Edittext = (EditText) findViewById(R.id.editMessage_EditText);
        send_Button = (Button) findViewById(R.id.send_Button);
        reset_Button = (Button) findViewById(R.id.reset_Button);
        
        ButtonListener buttonListener = new ButtonListener();
        send_Button.setOnClickListener(buttonListener);
        reset_Button.setOnClickListener(buttonListener);
        
        Bundle extras = getIntent().getExtras();
        userid = extras.getString(Constants.Userid);
        userid_TextView.setText(userid);
        
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
			}
		}
	}

	private class PostMessageTask extends AsyncTask<String, Integer, String>{
		protected void onPreExecute() {
			super.onPreExecute();
			send_Button.setEnabled(false);
		}
		protected String doInBackground(String... arg0) {
			return null;
		}
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			send_Button.setEnabled(true);
			finish();
		}
	}


}
