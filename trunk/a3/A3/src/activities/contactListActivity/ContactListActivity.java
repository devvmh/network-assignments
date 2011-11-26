package activities.contactListActivity;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.a3.R;

public class ContactListActivity extends Activity {
	private DbAdapter mDbHelper;
	private ListView listView;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contactlist);
        mDbHelper = new DbAdapter(this);
        mDbHelper.open();
        
        listView = (ListView) findViewById(R.id.contactListView);
	}//onCreate
	
	public void addContact (String internal, String external, String longitude, String latitude,
			String interests) {
		DbAdapter mDbHelper = new DbAdapter (this);
		mDbHelper.open ();
		
		mDbHelper.addContact (internal, external, longitude, latitude, interests);
	}
	
	@Override
	public void onResume () {
		super.onResume ();
		fillData ();
	}//onResume
	
	public void fillData () {
        Cursor notesCursor = mDbHelper.fetchAllNotes();
        startManagingCursor(notesCursor);
        SimpleCursorAdapter notes;

        //showing just visible description, or number too?
        // Create an array to specify the fields we want to display in the list
        String[] from = new String[]{DbAdapter.KEY_IPTUPLE, DbAdapter.KEY_LONG, 
        		DbAdapter.KEY_LAT, DbAdapter.KEY_INTERESTS, DbAdapter.KEY_ROWID};

        // and an array of the fields we want to bind those fields to
        int[] to = new int[]{R.id.textInvisible1, R.id.text1, R.id.text2, R.id.text3,
        		R.id.textInvisible2};

        // Now create a simple cursor adapter and set it to display
        notes = new SimpleCursorAdapter(this, R.layout.contactlist_rows, 
        			notesCursor, from, to);
        listView.setAdapter(notes);
	}//fillData
}//ContactListActivity