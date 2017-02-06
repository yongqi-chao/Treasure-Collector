package edu.neu.madcourse.yongqichao;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Acknowledgements extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acknowledgements);
        setTitle("Acknowledgements");

        //set text size
        TextView sound = (TextView) findViewById(R.id.SOUND);
        sound.setTextSize(20);

        TextView song = (TextView) findViewById(R.id.SONG);
        song.setTextSize(20);
    }
}
