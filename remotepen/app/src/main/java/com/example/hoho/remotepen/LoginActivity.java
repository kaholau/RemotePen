package com.example.hoho.remotepen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.Delayed;
import android.os.Handler;
/*
# COMP 4521    #  LAU KA HO        20094423  khlauad@ust.hk
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
//TODO
    //send to email is forget password
    public static final String TAG="RemotePen_LoginActivity";
    public static final String LOGIN_SHARED_PRE="remotePenLogin";
    public static final String PASSWORD ="password";
    SharedPreferences password_storage;
    Button btn[] = new Button[11];
    EditText userinput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        password_storage=getSharedPreferences(LOGIN_SHARED_PRE,MODE_PRIVATE);

        SharedPreferences ip_storage=getSharedPreferences(SetIPActivity.TAG,MODE_PRIVATE);
        if(!ip_storage.getString(SetIPActivity.IP,"null").equals("null"))
            ChromeSockect.address=ip_storage.getString(SetIPActivity.IP,"null");
        if(!ip_storage.getString(SetIPActivity.PORT,"null").equals("null"))
            ChromeSockect.port=ip_storage.getString(SetIPActivity.PORT, "null");

        Log.d(TAG,"Existing password is "+password_storage.getString(PASSWORD,"default value"));
/*
SetPasswordActivity will be shown if the retrieved password value is null.
 */
        if(isFirstLaunch())
        {
            Intent intent = new Intent(this, SetPasswordActivity.class);
            startActivity(intent);
        }

        userinput = (EditText)findViewById(R.id.numberPadText);
/*
11 buttons are used to create the number pad which includes 0~9 and clear button. All buttons have circle border.
 */
         //register the buttons
        btn[0] = (Button)findViewById(R.id.button1);
        btn[1] = (Button)findViewById(R.id.button2);
        btn[2] = (Button)findViewById(R.id.button3);
        btn[3] = (Button)findViewById(R.id.button4);
        btn[4] = (Button)findViewById(R.id.button5);
        btn[5] = (Button)findViewById(R.id.button6);
        btn[6] = (Button)findViewById(R.id.button7);
        btn[7] = (Button)findViewById(R.id.button8);
        btn[8] = (Button)findViewById(R.id.button9);
        btn[9] = (Button)findViewById(R.id.button0);
        btn[10] = (Button)findViewById(R.id.clearbutton);

        for(int i =0;i<11;i++){
            btn[i].setOnClickListener(this);
        }

    }
    @Override
    protected void onResume(){
        super.onResume();
    }
    public void onClick(View v){
        switch(v.getId()){
            case R.id.button1:
                addToArray("1");
                break;
            case R.id.button2:
                addToArray("2");
                break;
            case R.id.button3:
                addToArray("3");
                break;
            case R.id.button4:
                addToArray("4");
                break;
            case R.id.button5:
                addToArray("5");
                break;
            case R.id.button6:
                addToArray("6");
                break;
            case R.id.button7:
                addToArray("7");
                break;
            case R.id.button8:
                addToArray("8");
                break;
            case R.id.button9:
                addToArray("9");
                break;
            case R.id.button0:
                addToArray("0");
                break;
            case R.id.clearbutton:
                userinput.setText("");
                break;
            default:
        }
        /*
        . If the user input 4 number, the activity will automatically check the correctness of the input using attemptLogin().
         */
        userinput = (EditText)findViewById(R.id.numberPadText);
        if(userinput.length()==4){
            Log.d(TAG, "userInput: " + userinput.getText().toString());
                if(attemptLogin(userinput.getText().toString())){
                    finish();
                }
        }
    }
    private void addToArray(String numbers){
        userinput.append(numbers);
    }

    @Override
    public void onBackPressed() { }

    private boolean isFirstLaunch(){
       if(password_storage.getString(PASSWORD,"default value").equals("default value"))
           return true;
        return false;
    }

/*
. If the user input 4 number, the activity will automatically check the correctness of the input using attemptLogin().
 */
    private boolean attemptLogin(String pw){
        if(password_storage.getString(PASSWORD,"default value").equals(pw)){
            Log.d(TAG,"Login successfully");
            return true;
        }
        Log.d(TAG,"Existing password is "+password_storage.getString(PASSWORD,"default value"));
        final TextView error=(TextView)findViewById(R.id.input_password_error);
        error.setText("Wrong!!!!");
        Handler h = new Handler();
        h.postDelayed(new Runnable()
        {
            public void run() {
                error.setText("");
            }
        }, 1000);
        return false;
    }

}
