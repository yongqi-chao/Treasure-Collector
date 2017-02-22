package edu.neu.madcourse.yongqichao;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class WordgamePlay extends AppCompatActivity {
    public static final String KEY_RESTORE = "key_restore";
    public static final String PREF_RESTORE = "pref_restore";
    public static final String WordGame_RESTORE = "wordgame_restore";
    public static final String DICTIONARY_RESTORE = "wordgame_restore";
    private MediaPlayer mMediaPlayer;
    private Handler mHandler = new Handler();
    private WordgamePlayFragment gameFragment;

    private ArrayList<String> dictionary = new ArrayList<>();
    private Chronometer chronometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordgame_play);
        gameFragment = (WordgamePlayFragment) getFragmentManager()
                .findFragmentById(R.id.fragment_game);
        boolean restore = getIntent().getBooleanExtra(KEY_RESTORE, false);

        if (restore) {
            String gameData = getPreferences(MODE_PRIVATE)
                    .getString(PREF_RESTORE, null);
            if (gameData != null) {
                gameFragment.putState(gameData);
            }
            String wordData = getPreferences(MODE_PRIVATE)
                    .getString(WordGame_RESTORE, null);
            if (wordData != null){
                gameFragment.putWords(wordData);
            }
        }

        putDictionaryIntoFragment();
        //timer
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        chronometer.start();


        Log.d("Wordgame", "restore = " + restore);

    }

    public void restartGame() {
        gameFragment.restartGame();
        //restart timer from 00:00
        System.out.println(chronometer.getBase());
        //90,000 is 90 seconds , 1:30 mins
        chronometer.setBase(SystemClock.elapsedRealtime());
        System.out.println(SystemClock.elapsedRealtime());
        System.out.println(chronometer.getBase());
        chronometer.start();

    }

    public void reportWinner(final WordgameTile.Owner winner) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
//            mMediaPlayer.stop();
//            mMediaPlayer.reset();
//            mMediaPlayer.release();
//        }
        builder.setMessage(getString(R.string.declare_winner_wordgame, winner));
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.ok_label_wordgame,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
        final Dialog dialog = builder.create();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                mMediaPlayer = MediaPlayer.create(GameActivity.this,
//                        winner == Tile.Owner.X ? R.raw.oldedgar_winner
//                                : winner == Tile.Owner.O ? R.raw.notr_loser
//                                : R.raw.department64_draw
//                );
//                mMediaPlayer.start();
                dialog.show();
            }
        }, 500);

        // Reset the board to the initial position
        gameFragment.initGame();
    }

//    public void startThinking() {
//        View thinkView = findViewById(R.id.thinking);
//        thinkView.setVisibility(View.VISIBLE);
//    }
//
//    public void stopThinking() {
//        View thinkView = findViewById(R.id.thinking);
//        thinkView.setVisibility(View.GONE);
//    }

    @Override
    protected void onResume() {
        super.onResume();
//        mMediaPlayer = MediaPlayer.create(this, R.raw.frankum_loop001e);
//        mMediaPlayer.setLooping(true);
//        mMediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(null);
//        mMediaPlayer.stop();
//        mMediaPlayer.reset();
//        mMediaPlayer.release();
        String gameData = gameFragment.getState();
        getPreferences(MODE_PRIVATE).edit()
                .putString(PREF_RESTORE, gameData)
                .commit();
        Log.d("UT3", "state = " + gameData);

        String wordgameData = gameFragment.getWords();
        getPreferences(MODE_PRIVATE).edit()
                .putString(WordGame_RESTORE, wordgameData)
                .apply();

        Log.d("UT3", "state = " + wordgameData);



    }


    public void putDictionaryIntoFragment(){

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        InputStreamReader input = new InputStreamReader(getResources().openRawResource(R.raw.wordlist));
                        BufferedReader r = new BufferedReader(input);
                        String word;
                        //convert word to hashset

                        try {
                            while ((word = r.readLine()) != null) {
                                if (word.length() == 9) dictionary.add(word);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                gameFragment.initAllSmallTiles(dictionary);
                            }
                        });

                    }
                }
        ).start();
    }


//    public ArrayList<String> parseToList(String dictionaryData){
//        String[] fields = dictionaryData.split(",");
//        ArrayList<String> listofwords = new ArrayList<>();
//        for (int index = 0; index < fields.length; index++) {
//            listofwords.add(fields[index]);
//        }
//        return  listofwords;
//    }
//
//
//    public String parseToString(ArrayList<String> listofwords){
//        StringBuilder builder = new StringBuilder();
//        for (int index = 0; index < listofwords.size(); index++) {
//            builder.append(listofwords.get(index));
//            builder.append(',');
//        }
//        return builder.toString();
//    }



}
