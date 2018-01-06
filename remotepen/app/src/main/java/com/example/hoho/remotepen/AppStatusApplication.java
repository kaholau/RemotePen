package com.example.hoho.remotepen;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.BroadcastReceiver;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

/**
 *   # COMP 4521    #  LAU KA HO        20094423  khlauad@ust.hk
 * Referenced by http://vardhan-justlikethat.blogspot.hk/2014/02/android-solution-to-detect-when-android.html
 */
public class AppStatusApplication extends Application   {

    private static String TAG = "RemotePen_"+AppStatusApplication.class.getName();

    public static boolean wasInBackground = false;

    ScreenOffReceiver screenOffReceiver = new ScreenOffReceiver();

    class ScreenOffReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            wasInBackground = true;
        }
    }

    public void onCreate() {
        super.onCreate();
        wasInBackground = true;
        registerReceiver(screenOffReceiver, new IntentFilter(
                "android.intent.action.SCREEN_OFF"));
        Log.d(TAG,"AppStatusApplication created");
    }

}
