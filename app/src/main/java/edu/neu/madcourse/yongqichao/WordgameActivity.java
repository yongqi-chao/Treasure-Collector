package edu.neu.madcourse.yongqichao;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class WordgameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordgame);
    }

    MediaPlayer mMediaPlayer;
    // ...

    @Override
    protected void onResume() {
        super.onResume();
        //mMediaPlayer = MediaPlayer.create(this, R.raw.a_guy_1_epicbuilduploop);
       // mMediaPlayer.setVolume(0.5f, 0.5f);
       // mMediaPlayer.setLooping(true);
       // mMediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mMediaPlayer.stop();
//        mMediaPlayer.reset();
//        mMediaPlayer.release();
    }
}
