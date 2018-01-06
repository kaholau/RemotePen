package com.example.hoho.remotepen;

import android.content.Context;
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
 * Created by HOHO on 23/5/2016.
 */
public class SetIPActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String TAG = "RemotePen_SET_IP_ACTIVITY";
    public static final String IP="remotepen_ip";
    public static final String PORT="remotepen_port";
    Controller mc;
    SharedPreferences ip_storage;
    EditText ipField;
    EditText portField;
    TextView error;
    Button confirm_btn;
    Button cancel_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_ip_activity);
        mc=new Controller(this);
        error=(TextView)findViewById(R.id.set_ip_error);

        ipField=(EditText)findViewById(R.id.ip_field);
        portField=(EditText)findViewById(R.id.port_field);

        ip_storage=getSharedPreferences(TAG,MODE_PRIVATE);
        if(!ip_storage.getString(IP,"null").equals("null"))
            ipField.setText(ip_storage.getString(IP,"null"));
        if(!ip_storage.getString(PORT,"null").equals("null"))
            portField.setText(ip_storage.getString(PORT,null));

        confirm_btn =(Button)findViewById(R.id.set_ip_confirm_button);
        confirm_btn.setOnClickListener(this);
        cancel_btn=(Button)findViewById(R.id.set_ip_cancel_button);
        cancel_btn.setOnClickListener(this);
//        Intent intent=getIntent();
//        if(!intent.getBooleanExtra(SettingActivity.isFromSetting,false)){
//            cancel_btn.setVisibility(View.GONE);
//        }
    }

    @Override
    public void onBackPressed() { }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_ip_confirm_button: {
                String ip = ipField.getText().toString();
                String   port = portField.getText().toString();
//                if (ps.length() < 4 || ps_repeat.length() < 4) {
//                    error.setText("Not complete");
//                    Log.d(TAG, "Not complete");
//                    return;
//                }
//
//                if (!ps.matches("[0-9]+") || !ps_repeat.matches("[0-9]+")) {
//                    error.setText("Number only");
//                    Log.d(TAG, "Number only");
//                    return;
//                }
//
//                if (!ps.equals(ps_repeat)) {
//                    error.setText("Password not match!");
//                    Log.d(TAG, "Password not match");
//                    return;
//                }
                SharedPreferences.Editor psEdit = getSharedPreferences(TAG, MODE_PRIVATE).edit();
                psEdit.putString(IP, ip);
                psEdit.putString(PORT, port);
                psEdit.commit();
                ChromeSockect.address=ip;
                ChromeSockect.port=port;
                finish();
            }
            break;
            case R.id.set_ip_cancel_button:
                Log.d(TAG," cancel");
                finish();
                break;
            default:
        }

    }
}
