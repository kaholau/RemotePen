package com.example.hoho.remotepen;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * # COMP 4521    #  LAU KA HO        20094423  khlauad@ust.hk
 */
public class TextInputActivity extends TextBaseActivity {

    private Controller mc;
    private EditText inputField;
    private Button send;
    private String input;
    private static final String INPUT_SHAREDPREFERENCE="RemotePen_input_store";
    private static final String INPUT="RemotePen_input";
    private SharedPreferences input_store;

    @Override
    public int getLayoutResource() {
        return R.layout.input_activity;
    }

    @Override
    protected String getActivityName(){
        return this.getLocalClassName();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        input_store=getSharedPreferences(INPUT_SHAREDPREFERENCE,MODE_PRIVATE);
        inputField=(EditText)findViewById(R.id.text_input_field);
        send=(Button)findViewById(R.id.send_button);
        mc=new Controller(this);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input=inputField.getText().toString();
                mc.send(input, Controller.SAVE);
            }
        });
    }
    @Override
    protected void preOnResume(){
        input=inputField.getText().toString();
        SharedPreferences.Editor inputEditor=input_store.edit();
        inputEditor.putString(INPUT,input);
        inputEditor.commit();
        Log.d(TAG,"temp save input: "+input);
    }

    @Override
    protected void postOnResume(){
        Log.d(TAG,"restore input: "+input);
        inputField.setText(input_store.getString(INPUT,""));
    }

}
