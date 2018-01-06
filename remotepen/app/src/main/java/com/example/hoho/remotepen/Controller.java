package com.example.hoho.remotepen;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

/**
 * # COMP 4521    #  LAU KA HO        20094423  khlauad@ust.hk
 */
public class Controller {

    private static final String TAG = "RemotePen_Controller";
    public static final boolean SAVE=true;
    private final Context myContext;
    private String IP;
    private String port;
//    String testurl = "ws://echo.websocket.org";
//    String url="ws://10.89.149.37:9098";
    Calendar calendar;
    ChromeSockect chromeSockect;
    private DataBaseModel mdb;

    public Controller(Context c){
        myContext=c;
        mdb =new DataBaseModel(c);
        //mwebsocket=new WebSocket(testurl);
        chromeSockect=new ChromeSockect(myContext);
        try{
            mdb.open();
        }catch (SQLException e){
            Log.d(TAG, e.toString());
        }
//        for(int i=0;i<50000;i++){
//            send("Hi" + Integer.toString(i), true);
//        }
    };

    public boolean isOnline(){
        ConnectivityManager connMgr=(ConnectivityManager) myContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo= connMgr.getActiveNetworkInfo();

        if(netInfo!=null&&netInfo.isConnected()){
            return true;
        }else {
            return false;
        }
    }

    public void send(String msg, boolean save){
        Log.d(TAG, "send is called");
        if(isOnline()){
            //mwebsocket.send(msg);
            chromeSockect.send(msg);
            if(msg.trim().length()>0&&save)
                mdb.writeHistory(msg);
        }

    }
    public void deleteHistory(int ID){
        mdb.deleteHistory(ID);
        mdb.deleteBookmark(ID);
    }
    public void deleteAllHistory(){
        mdb.deleteAllHistory();
        //mdb.deleteAllBookmark();
    }

    public void saveBookmark(int ID){
        mdb.writeBookmark(ID);
        mdb.updateHistory_col_Bookmark(ID,1);
    }
    public void removeBookmark(int ID){
        mdb.deleteBookmark(ID);
        mdb.updateHistory_col_Bookmark(ID,0);
    }

    public List<HistoryItem> readHistory() {
        List<HistoryItem> hList=mdb.readHistory();
//        for(int i=0;i<hList.size();i++){
//            Log.d(TAG, hList.get(i).getID() + " " + hList.get(i).getTime() + " " + hList.get(i).getText() + " " + hList.get(i).getIsBookmark());
//        }
//        Log.d(TAG, "total history =" + Integer.toString(mdb.getHistoryCount()));
        return hList;
    }

    public List<HistoryItem> readBookmark() {
        List<HistoryItem> hList=mdb.readBookmark();
//        for(int i=0;i<hList.size();i++){
//            Log.d(TAG, hList.get(i).getID() + " " + hList.get(i).getTime() + " " + hList.get(i).getText() + " " + hList.get(i).getIsBookmark());
//        }
//        Log.d(TAG, "total history =" + Integer.toString(mdb.getHistoryCount()));
        return hList;
    }

}
