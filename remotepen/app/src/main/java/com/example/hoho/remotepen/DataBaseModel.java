package com.example.hoho.remotepen;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * # COMP 4521    #  LAU KA HO        20094423  khlauad@ust.hk
 */
public class DataBaseModel {

    private static final String TAG = "RemotePen_DataBaseModel";

    public static final String DATABASE_NAME = "remotePen";
    public static final String SQL_LITE_HISTORY_TABLE = "history";
    public static final String SQL_LITE_BOOKMARK_TABLE = "bookmark";
    public static final int DATABASE_VERSION = 1;

    public static final String HISTORY_ROWID = "tid";
    public static final String HISTORY_TIME = "time";
    public static final String HISTORY_CONTEXT = "text";
    public static final String IS_BOOKMARK = "isBookmark";

    private DatabaseHelper  myDbHelper;
    private final Context myContext;
    private SQLiteDatabase mydb;

    private static final String HISTORY_TABLE_CREATE =
            "CREATE TABLE if not exists " + SQL_LITE_HISTORY_TABLE + " (" +
                    HISTORY_ROWID + " INTEGER PRIMARY KEY  AUTOINCREMENT"+ "," +
                    HISTORY_TIME +" TEXT  NOT NULL "+ "," +
                    HISTORY_CONTEXT +" TEXT NOT NULL"+ "," +
                    IS_BOOKMARK +" INTEGER DEFAULT 0 );";
    private static final String BOOKMARK_TABLE_CREATE =
            "CREATE TABLE if not exists " + SQL_LITE_BOOKMARK_TABLE + " (" +
                    HISTORY_ROWID +" INTEGER  PRIMARY KEY"+");";

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(TAG, HISTORY_TABLE_CREATE);
            db.execSQL(HISTORY_TABLE_CREATE);
            Log.d(TAG, BOOKMARK_TABLE_CREATE);
            db.execSQL(BOOKMARK_TABLE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + SQL_LITE_HISTORY_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + SQL_LITE_BOOKMARK_TABLE);
            onCreate(db);
        }
    }

    public DataBaseModel(Context c){
        this.myContext=c;
    }

    public DataBaseModel open() throws SQLException {
        myDbHelper = new DatabaseHelper(myContext);
        mydb = myDbHelper.getWritableDatabase();
//        deleteAllHistory();
//       insertSomeHistory();
        return this;
    }

    public void close() {
        if (myDbHelper != null) {
            myDbHelper.close();
        }
    }

    public List<HistoryItem> readHistory() {
        String selectQuery = "SELECT  * FROM " + SQL_LITE_HISTORY_TABLE;
        return read(selectQuery);
    }
    public void writeHistory( String text){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy hh:mm:ss aa");

        ContentValues values= new ContentValues();
        values.put(HISTORY_TIME, dateFormat.format(c.getTime()));
        values.put(HISTORY_CONTEXT, text);
        mydb.insert(SQL_LITE_HISTORY_TABLE, null, values);
    }

    public void updateHistory_col_Bookmark(int ID,int isBk){
        ContentValues values= new ContentValues();
        values.put(IS_BOOKMARK,isBk);
        mydb.update(SQL_LITE_HISTORY_TABLE, values, HISTORY_ROWID + "=?", new String[]{Integer.toString(ID)});
    }

    public List<HistoryItem> read(String selectQuery){
        List<HistoryItem> historyList=new ArrayList<HistoryItem>();
        Cursor cursor = mydb.rawQuery(selectQuery, null);

        if(cursor.moveToLast()){
            do{
                HistoryItem history=new HistoryItem();
                history.setID(Integer.parseInt(cursor.getString(0)));
                history.setTime(cursor.getString(1));
                history.setText(cursor.getString(2));
                history.setIsBookmark(Integer.parseInt(cursor.getString(3)));
                historyList.add(history);
            }while (cursor.moveToPrevious ());
        }
        return historyList;
    }


    public void deleteHistory(int ID){
        mydb.delete(SQL_LITE_HISTORY_TABLE,HISTORY_ROWID+" =?",new String[]{Integer.toString(ID)});
    }

    public void deleteAllHistory(){
        String q="DELETE FROM "+SQL_LITE_HISTORY_TABLE
                +" WHERE "+HISTORY_ROWID
                +" NOT IN (SELECT " + HISTORY_ROWID+" FROM "+SQL_LITE_BOOKMARK_TABLE+")";
        mydb.execSQL(q);
        //Log.d(TAG, "deleteHistory " + Integer.toString(doneDelete));
    }

    public int getHistoryCount(){
        String countQuery = "SELECT  * FROM " + SQL_LITE_HISTORY_TABLE;
        Cursor cursor = mydb.rawQuery(countQuery, null);
        int count=cursor.getCount();
        cursor.close();
        // return count
        return count;
    }

    public void deleteBookmark(int ID){
        mydb.delete(SQL_LITE_BOOKMARK_TABLE,HISTORY_ROWID+"=?",new String[]{Integer.toString(ID)});
    }
    public void deleteAllBookmark(){
        int doneDelete = 0;
        doneDelete = mydb.delete(SQL_LITE_BOOKMARK_TABLE, null , null);
        Log.d(TAG, "deleteHistory " + Integer.toString(doneDelete));
    }
    public long writeBookmark(int id){
        ContentValues values=new ContentValues();
        values.put(HISTORY_ROWID, id);
        return mydb.insert(SQL_LITE_BOOKMARK_TABLE, null, values);

    }

    public List<HistoryItem> readBookmark() {
        String selectQuery = "SELECT  * FROM " +
                SQL_LITE_HISTORY_TABLE+","+ SQL_LITE_BOOKMARK_TABLE
                +" WHERE "+SQL_LITE_HISTORY_TABLE+"."+HISTORY_ROWID+"="+SQL_LITE_BOOKMARK_TABLE+"."+HISTORY_ROWID;
        return read(selectQuery);
    }



    public void insertSomeHistory() {
        for(int i=0;i<50;i++){
            writeHistory("Hi"+Integer.toString(i));
        }

    }

}
