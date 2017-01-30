package edu.neu.madcourse.yongqichao;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.telephony.TelephonyManager;
import android.content.Context;


public class AboutActivity extends AppCompatActivity {

    public Button backButton;
    public TextView imeiText;

    public void init(){
        backButton = (Button) findViewById(R.id.backbutton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

//        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//        String device_id = tm.getDeviceId();
//
//        imeiText = (TextView) findViewById(R.id.IMEI);
//        imeiText.setText(device_id);

        init();

    }
}
