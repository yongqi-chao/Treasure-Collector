package edu.neu.madcourse.yongqichao;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    RelativeLayout background;
    Button errorButton, quitButton;

    public  Button aboutButton;

    public void init(){
        aboutButton = (Button) findViewById(R.id.aboutbutton);
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent about = new Intent(MainActivity.this,AboutActivity.class);
                startActivity(about);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        background = (RelativeLayout) findViewById(R.id.activity_main);
        errorButton = (Button) findViewById(R.id.errorbutton);
        quitButton = (Button) findViewById(R.id.quitbutton);

        errorButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                background.setBackgroundColor(Color.parseColor("#006699we231"));
            }
        });

        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        init();
    }
}
