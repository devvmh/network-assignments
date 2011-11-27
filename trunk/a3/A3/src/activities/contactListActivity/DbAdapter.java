/*
 * Copyright (C) 2008 Google Inc.

 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
//Adapted this file from Google's Notepad Tutorial

package activities.contactListActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Simple notes database access helper class. Defines the basic CRUD operations
 * for the notepad example, and gives the ability to list all notes as well as
 * retrieve or modify a specific note.
 * 
 * This has been improved from the first version of this tutorial through the
 * addition of better error handling and also using returning a Cursor instead
 * of using a collection of inner classes (which is less scalable and not
 * recommended).
 */
public class DbAdapter {

	//iptuple is the two IPs, separated by a comma. They are the primary key of the table
	public static final String KEY_ROWID = "_id";
    public static final String KEY_IPTUPLE = "_iptuple";
    public static final String KEY_LONG = "_longitude";
    public static final String KEY_LAT = "_latitude";
    public static final String KEY_INTERESTS = "_interests";
    public static final String KEY_TTL = "_ttl";
    
    private static final String TAG = "DbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * Database creation SQL statement
     */
    private static final String DATABASE_CREATE =
        "create table contacts (_id integer primary key autoincrement, "
        + "_iptuple text not null, _interests text not null, _longitude "
        + "text not null, _latitude text not null, _ttl time not null);";

    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE = "contacts";
    private static final int DATABASE_VERSION = 3;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS contacts");
            onCreate(db);
        }
        
        public void deleteAll (SQLiteDatabase db) {
        	db.execSQL ("DROP TABLE IF EXISTS contacts");
        	onCreate (db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public DbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an 
     * exception to signal the failure
     */
    public DbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    /**
     * Create a new entry using the 5 pieces of information provided. If the note is
     * successfully created return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     */
    public long addContact(String internal, String external, String longitude, 
    		String latitude, String interests) {
        ContentValues initialValues = new ContentValues();
        
        //sanitize - replace " with '
        if (interests.contains("\"")) {
        	interests.replace ('"', '\'');
        }
        String ipTuple = internal + "," + external;
        String time = ((Long)System.nanoTime ()).toString ();
        
        initialValues.put(KEY_IPTUPLE, "\"" + ipTuple + "\"");
        initialValues.put(KEY_INTERESTS, "\"" + interests + "\"");
        initialValues.put(KEY_LONG, longitude);
        initialValues.put(KEY_LAT, latitude);
        initialValues.put(KEY_TTL, time);

        long retval;
        retval = mDb.insert(DATABASE_TABLE, null, initialValues);
        //if there's an error, try to update instead. -1 is error, all other values are success
        if (retval == -1) {
        	retval = mDb.update(DATABASE_TABLE, initialValues, "_iptuple", new String [] {ipTuple});
        }
        return retval;
    }//addContact

     //Delete the entry with the given IP addresses
    public boolean deleteNote(String internal, String external) {
    	String ipTuple = internal + "," + external;
        return mDb.delete(DATABASE_TABLE, KEY_IPTUPLE + "=" + ipTuple , null) > 0;
    }
    
    public void deleteAllNotes () {
        mDbHelper.deleteAll (mDb);
    }
    
    //deletes expired contacts TODO
    //public boolean deleteOldNotes () {
    //}

    /**
     * Return a Cursor over the list of all notes in the database
     * 
     * @return Cursor over all notes
     */
    public Cursor fetchAllNotes() {

        return mDb.query(DATABASE_TABLE, new String[] {KEY_IPTUPLE, KEY_LONG,
                KEY_LAT, KEY_INTERESTS, KEY_TTL}, null, null, null, null, null);
    }

    /**
     * Return a Cursor positioned at the note that matches the given IPs
     */
    public Cursor fetchNote(String internal, String external) throws SQLException {

    	String ipTuple = internal + "," + external;
        Cursor mCursor =

            mDb.query(true, DATABASE_TABLE, new String[] {KEY_IPTUPLE, KEY_LONG,
                    KEY_LAT, KEY_INTERESTS}, KEY_IPTUPLE + "=" + ipTuple, null,
                    null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    //Update the contact using the details provided and current time
    public boolean updateNote(String internal, String external, String longitude,
    		String latitude, String interests) {
    	String ipTuple = internal + "," + external;
    	String time = ((Long)System.nanoTime ()).toString ();
    	
        ContentValues args = new ContentValues();
        args.put(KEY_IPTUPLE, ipTuple);
        args.put(KEY_LONG, longitude);
        args.put(KEY_LAT, latitude);
        args.put(KEY_INTERESTS, interests);
        args.put(KEY_TTL, time);

        return mDb.update(DATABASE_TABLE, args, KEY_IPTUPLE + "=" + ipTuple, null) > 0;
    }//updateNote
}//DbAdapter class
