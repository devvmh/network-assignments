package activities.prefActivity;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;

import com.a3.R;

public class PrefActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.prefs);
        
        //warn user if interests string is blank, since mainActivity won't do anything until you
        //set it! It'll keep sending you back here
        EditTextPreference editText = (EditTextPreference) findPreference ("interests_string");
        if (editText.getEditText().getText().toString () == "") {
        	editText.setSummary("Your interests can't be blank");
        } else {
        	editText.setSummary("");
        }//if
        
        editText.setOnPreferenceChangeListener( new OnPreferenceChangeListener() {
        	public boolean onPreferenceChange (Preference oldString, Object newString) {
        		exit ();
        		return true;
        	}//onPreferenceChange
        });
    }//onCreate
    
    //exits the activity to return to MainActivity
    protected void exit () {
    	this.finish();
    }//exit
}//PrefActivity class
