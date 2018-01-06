package com.example.hoho.remotepen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * # COMP 4521    #  LAU KA HO        20094423  khlauad@ust.hk
 The TextBaseActivity is responsible to keep checking whether the App is put into Background and call LoginActivity after the App resume from background.
 */
public abstract class TextBaseActivity extends AppCompatActivity {

    public static final String TAG="RemotePen";
    protected boolean isClickInsideApp =false;
    private boolean loginCalled=false;
    private static boolean firstLogin=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,  getActivityName()+" onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) setSupportActionBar(toolbar);


    }
    protected abstract int getLayoutResource();
    protected abstract String getActivityName();
    protected abstract void preOnResume();
    protected abstract void postOnResume();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        Log.d(TAG, getActivityName() + " onStart()");
        super.onStart();
        if (firstLogin) {
            Log.d(TAG, getActivityName() + " call login");
            firstLogin=false;
           // isClickInsideApp=true;
            callLoginActivity();
        }

    }

    @Override
    protected void onRestart(){
        Log.d(TAG, getActivityName() + " onRestart()");
        super.onRestart();

    }
/*
When the icons in Actionbar is click to switch activity, the isClickInsideApp will set to true. The put into background detection is implemented in callLoginActivity().
 */
    public void callLoginActivity(){
        preOnResume();
        if (!loginCalled&&!isClickInsideApp)
        {
            loginCalled=true;
            Intent LoginIntent = new Intent(this, LoginActivity.class);
            startActivityForResult(LoginIntent,1);
        }
        isClickInsideApp =false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 1) {
            loginCalled=false;
        }
    }

    @Override
    protected void onStop(){
        Log.d(TAG, getActivityName() + " onStop()");
        super.onStop();
        //callLoginActivity();
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d(TAG, getActivityName() + " onPause()");
        callLoginActivity();
    }


    @Override
    protected void onResume(){
        super.onResume();
        Log.d(TAG, getActivityName() + " onResume()");
        postOnResume();
    }
/*
().  onBackPressed() is override with empty context in order to prohibit user clicking back key.
 */
    @Override
    public void onBackPressed() { }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        isClickInsideApp =true;
        switch (item.getItemId()) {
            case R.id.action_settings:
                intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_input:
                intent = new Intent(this, TextInputActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_history:
                intent = new Intent(this, TextHistoryActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_bookmark:
                intent = new Intent(this, TextBookmarkActivity.class);
                startActivity(intent);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
