package edu.neu.madcourse.yongqichao;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;



public class MainActivity extends AppCompatActivity {
    RelativeLayout background;
    Button errorButton, aboutButton, dictionaryButton, quitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Yongqi Chao");

        background = (RelativeLayout) findViewById(R.id.activity_main);

        //add ABOUT button.
        aboutButton = (Button)findViewById(R.id.about);
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent about = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(about);
            }
        });

        //add ERROR button
        errorButton = (Button)findViewById(R.id.error);
        errorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                background.setBackgroundColor(Color.parseColor("#006699we231"));
            }
        });

        //add DICTIONARY button
        dictionaryButton = (Button) findViewById(R.id.dictionary);
        dictionaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dictionary = new Intent(MainActivity.this, DictionaryActivity.class);
                startActivity(dictionary);
            }
        });

        //add QUIT button.
        quitButton = (Button)findViewById(R.id.quit);
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //check does app install dictionary component?
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        String name = preferences.getString("installation", "not finish");

        //if not, then install it.
        if(!name.equalsIgnoreCase("done")) {
            Intent install = new Intent(MainActivity.this, Installation_Activity.class);
            startActivity(install);
            editor.putString("installation","done");
            editor.apply();
        }



    }
}

