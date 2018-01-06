package com.example.hoho.remotepen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by HOHO on 17/5/2016.
 */
public class SetPasswordActivity extends AppCompatActivity implements View.OnClickListener{

    public final String TAG = "RemotePen_SET_PW_ACTIVITY";
    SharedPreferences password_storage;
    EditText[] passwordField=new EditText[2];
    TextView error;
    Button confirm_btn;
    Button cancel_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_password_layout);
        error=(TextView)findViewById(R.id.set_password_error);
        password_storage=getSharedPreferences(LoginActivity.LOGIN_SHARED_PRE,MODE_PRIVATE);
        Log.d(TAG,"Existing password is "+password_storage.getString(LoginActivity.PASSWORD,"default value"));
        passwordField[0]=(EditText)findViewById(R.id.password_field);
        passwordField[1]=(EditText)findViewById(R.id.password_repeat_field);
        confirm_btn =(Button)findViewById(R.id.set_password_confirm_button);
        confirm_btn.setOnClickListener(this);
        cancel_btn=(Button)findViewById(R.id.set_password_cancel_button);
        cancel_btn.setOnClickListener(this);
        Intent intent=getIntent();
        if(!intent.getBooleanExtra(SettingActivity.isFromSetting,false)){
            cancel_btn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() { }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_password_confirm_button: {
                String ps = passwordField[0].getText().toString();
                String ps_repeat = passwordField[1].getText().toString();
                if (ps.length() < 4 || ps_repeat.length() < 4) {
                    error.setText("Not complete");
                    Log.d(TAG, "Not complete");
                    return;
                }

                if (!ps.matches("[0-9]+") || !ps_repeat.matches("[0-9]+")) {
                    error.setText("Number only");
                    Log.d(TAG, "Number only");
                    return;
                }

                if (!ps.equals(ps_repeat)) {
                    error.setText("Password not match!");
                    Log.d(TAG, "Password not match");
                    return;
                }
                SharedPreferences.Editor psEdit = getSharedPreferences(LoginActivity.LOGIN_SHARED_PRE, MODE_PRIVATE).edit();
                psEdit.putString(LoginActivity.PASSWORD, ps);
                psEdit.commit();
                Log.d(TAG, "DONE!! Set password to "+ps);
                finish();
            }
            break;
            case R.id.set_password_cancel_button:
                Log.d(TAG," cancel");
                finish();
            break;
            default:
        }

    }
}
