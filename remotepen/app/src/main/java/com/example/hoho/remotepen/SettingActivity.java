package com.example.hoho.remotepen;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;

/**
 * # COMP 4521    #  LAU KA HO        20094423  khlauad@ust.hk
 */
public class SettingActivity extends TextBaseActivity implements View.OnClickListener {

    private Button change_pw_btn;
    private Button clear_all_hist_btn;
    private Button set_chrome_address_btn;
    public static final String isFromSetting="isFromSetting";

    @Override
    public int getLayoutResource() {
        return R.layout.setting_activity;
    }

    @Override
    protected String getActivityName(){
        return this.getLocalClassName();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        change_pw_btn=(Button)findViewById(R.id.change_password_button);
        clear_all_hist_btn=(Button)findViewById(R.id.clear_all_hist_btn);
        set_chrome_address_btn=(Button)findViewById(R.id.change_ip_button);
        change_pw_btn.setOnClickListener(this);
        clear_all_hist_btn.setOnClickListener(this);
        set_chrome_address_btn.setOnClickListener(this);
    }

    @Override
        public void onClick(View v){
        Intent intent;
        switch (v.getId()){
            case R.id.change_password_button:
                isClickInsideApp=true;
                 intent = new Intent(this, SetPasswordActivity.class);
                intent.putExtra(isFromSetting,true);
                startActivity(intent);
                break;
            case R.id.change_ip_button:
                isClickInsideApp=true;
                intent = new Intent(this, SetIPActivity.class);
                startActivity(intent);
                break;
            case R.id.clear_all_hist_btn:
                buildDialog(this);
                break;
            default:
        }
    }

    private void buildDialog(final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.confirm_clear_all_hist_title)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Controller mc=new Controller(context);
                        mc.deleteAllHistory();
                        clear_all_hist_btn.setText("Done");
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog dialog= builder.create();
        dialog.show();
    }
    @Override
    protected void preOnResume(){
    }

    @Override
    protected void postOnResume(){
    }
}
