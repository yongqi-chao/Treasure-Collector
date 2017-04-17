package edu.neu.madcourse.yongqichao;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.firebase.messaging.FirebaseMessaging;

import edu.neu.madcourse.yongqichao.mapgame.MapGameMainView;


public class MainActivity extends AppCompatActivity {
    RelativeLayout background;
    Button errorButton, aboutButton, dictionaryButton, wordgameButton, quitButton, mapgameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Yongqi Chao");

        FirebaseMessaging.getInstance().subscribeToTopic("news");

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

        //add Word Game button
        wordgameButton = (Button) findViewById(R.id.wordgame);
        wordgameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent wordgame = new Intent(MainActivity.this, WordgameActivity.class);
                startActivity(wordgame);
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


        //add running Game button
        mapgameButton = (Button) findViewById(R.id.MapGame);
        mapgameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapgame = new Intent(MainActivity.this, MapGameMainView.class);
                startActivity(mapgame);
            }
        });
        mapgameButton.setBackgroundColor(getResources().getColor(R.color.blue_color));
    }
}

