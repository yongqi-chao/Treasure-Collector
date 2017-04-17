package edu.neu.madcourse.yongqichao.mapgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.neu.madcourse.yongqichao.R;

public class mapgameMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapgame_main);
        setTitle("Map Game");

        Intent map = new Intent(mapgameMain.this, MapGameMapView.class);
        startActivity(map);
    }
}
