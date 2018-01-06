package com.example.hoho.remotepen;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
/**
 *# COMP 4521    #  LAU KA HO        20094423  khlauad@ust.hk
 */
public class ChromeSockect  {

    public static final String TAG="RemotePen_ChromeSocket";
    Context mContext;
    Socket socket = null;
    BufferedReader in;
    PrintWriter out;
    static String address;
    static String port;
    public ChromeSockect(Context context){
        mContext=context;
    }

    public void send(String msg){
        if(address.equals(null)||port.equals(null))
        {
            address="0.0.0.0";
            port="0";
        }
            new SendTask().execute(msg);
    }


    private class SendTask extends AsyncTask<String, Void, Void> {
        String response="";
        @Override
        protected Void doInBackground(String ...msgs) {

            try {
                Log.d(TAG, "Send to "+address+" "+port +" "+msgs[0]);
                socket = new Socket(address, Integer.parseInt(port));
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                byte[] data = msgs[0].getBytes("UTF-8");
                String base64 = Base64.encodeToString(data, Base64.DEFAULT);
                out.println(base64);
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "IOException: " + e.toString();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                        response += "Socket is closed";
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Log.d(TAG,response);
        }
    }




}
