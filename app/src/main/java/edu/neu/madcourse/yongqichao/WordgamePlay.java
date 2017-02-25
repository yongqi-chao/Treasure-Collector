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
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class WordgamePlay extends AppCompatActivity {
    public enum Phase {
        Phase1, Phase2,PhaseTimeOut
    }

    //
    //  add LOADDING LOADDDING LOADDING TO THIS SCREEN  ADN HINT TO THIS SCREEN
    //

    public static final String KEY_RESTORE = "key_restore";
    public static final String State_RESTORE = "state_restore";
    public static final String Word_RESTORE = "word_restore";
    public static final String MoveTrack_RESTORE = "moveTrack_restore";
    public static final String Timer_RESTORE = "timer_restore";
    public static final String Phase_RESTORE = "phase_restore";
    private MediaPlayer mMediaPlayer;
    private Handler mHandler = new Handler();
    private WordgamePlayFragment gameFragment;

    private ArrayList<String> nineCharWordDictionary = new ArrayList<>();
    private ArrayList<String> dictionary = new ArrayList<>();
    private Chronometer chronometer;
    private TextView matchWordReporter;
    private Button selectIt,startPhase2Button;
    private Phase phase = Phase.Phase1;
    private long stoppedTime;
    private Thread loadingNineCharThread;
    private Thread loadingDictionaryThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordgame_play);
        //start loading game fregment
        gameFragment = (WordgamePlayFragment) getFragmentManager()
                .findFragmentById(R.id.fragment_game);
        boolean restore = getIntent().getBooleanExtra(KEY_RESTORE, false);
        //start timer
        startChronometer(gameFragment);

        if (restore) {
            //restore Phase
            String phaseData = getPreferences(MODE_PRIVATE)
                    .getString(Phase_RESTORE, null);
            if (phaseData != null) {
                phase = Phase.valueOf(phaseData);
            }
            //restore game state
            String stateData = getPreferences(MODE_PRIVATE)
                    .getString(State_RESTORE, null);
            if (stateData != null) {
                gameFragment.putState(stateData);
            }
            //restore game words
            String wordData = getPreferences(MODE_PRIVATE)
                    .getString(Word_RESTORE, null);
            if (wordData != null){
                gameFragment.putWords(wordData);
            }
            //restore game's moveTrack of words
            String moveData = getPreferences(MODE_PRIVATE)
                    .getString(MoveTrack_RESTORE, null);
            if (moveData != null) {
                gameFragment.putMoveTrack(moveData);
            }
            //restore timer
            String timeData = getPreferences(MODE_PRIVATE)
                    .getString(Timer_RESTORE, null);
            if (timeData != null) {
                long passedTime = Long.parseLong(timeData);
                chronometer.setBase(SystemClock.elapsedRealtime()-passedTime);
            }
        }

        if(phase == Phase.Phase2) gameFragment.initPhase2();
        putNineCharDictionaryIntoFragment();
        putDictionaryIntoFragment();



        Log.d("Wordgame", "restore = " + restore);

    }

    public void startChronometer(final WordgamePlayFragment gameFragment){
        //start a timer
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        chronometer.start();
        //timer
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(final Chronometer chronometer) {
                long passedTime = SystemClock.elapsedRealtime() - chronometer.getBase();
                stoppedTime = passedTime;
                startPhase2Button = (Button) findViewById(R.id.startPhase2);
                startPhase2Button.setVisibility(View.GONE);
                System.out.println(phase);
                switch (phase) {
                    case Phase1:
                        // finish phase 1 and prepare to start phase 2
                        if (passedTime >= 10000) {
                            chronometer.stop();
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "TIME UP - PHASE 1 ENDED", Toast.LENGTH_LONG);
                            toast.show();
                            //clear view content
                            matchWordReporter = (TextView) findViewById(R.id.matchWordReporter);
                            selectIt = (Button) findViewById(R.id.selectIt);
                            matchWordReporter.setText("");
                            selectIt.setVisibility(View.GONE);
                            //clear fragment words
                            gameFragment.finishGame();
                            System.out.println("are youkidding me????");
                            phase = Phase.PhaseTimeOut;
                            //----------------------------
                            //start phase 2
//                            startPhase2Button = (Button) findViewById(R.id.startPhase2);
                            startPhase2Button.setVisibility(View.VISIBLE);
                            startPhase2Button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startPhase2Button.setVisibility(View.GONE);
                                    startPhase2(gameFragment);
                                    phase = Phase.Phase2;
                                    chronometer.setBase(SystemClock.elapsedRealtime()-stoppedTime);
                                    chronometer.start();
                                }
                            });
                        }
                        break;
                    case PhaseTimeOut:
                        System.out.println("you enter a wrong phase");
                        chronometer.stop();
                        startPhase2Button.setVisibility(View.VISIBLE);
                        startPhase2Button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startPhase2Button.setVisibility(View.GONE);
                                startPhase2(gameFragment);
                                phase = Phase.Phase2;
                                chronometer.setBase(SystemClock.elapsedRealtime()-stoppedTime);
                                chronometer.start();
                            }
                        });
                        break;
                    case Phase2:
                        if (passedTime >= 180000) {
                            chronometer.stop();
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "TIME UP - PHASE 2 ENDED", Toast.LENGTH_LONG);
                            toast.show();
                            //clear view content
                            matchWordReporter = (TextView) findViewById(R.id.matchWordReporter);
                            selectIt = (Button) findViewById(R.id.selectIt);
                            matchWordReporter.setText("");
                            selectIt.setVisibility(View.GONE);
                            //clear fragment words
                            gameFragment.finishPhase2();
                            //not right!!!!!!!!!
                        }
                        break;

                }
            }
        });
    }

    public void startPhase2(final WordgamePlayFragment gameFragment){
        gameFragment.initPhase2();
    }

    public void restartGame() {
        gameFragment.restartGame();

        //reset buttons
        //startPhase2Button = (Button) findViewById(R.id.startPhase2);
        matchWordReporter = (TextView) findViewById(R.id.matchWordReporter);
        selectIt = (Button) findViewById(R.id.selectIt);
        matchWordReporter.setText("");
        selectIt.setVisibility(View.GONE);
        startPhase2Button.setVisibility(View.GONE);

        //restart timer from 00:00
        //90,000 is 90 seconds , 1:30 mins
        chronometer.setBase(SystemClock.elapsedRealtime());
        System.out.println(SystemClock.elapsedRealtime());
        System.out.println(chronometer.getBase());
        //waiting for dictionary to be loaded
        phase = Phase.Phase1;
        //in order to delete start phase 2 button
        chronometer.start();
        chronometer.stop();
        //end here
        mHandler.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        chronometer.start();
                    }
                },2000);

    }

    public void reportMatchedWord(final String matchedWord, final int largeTile){
        matchWordReporter = (TextView) findViewById(R.id.matchWordReporter);
        selectIt = (Button) findViewById(R.id.selectIt);

        if (matchedWord == null){
            matchWordReporter.setText("");
            selectIt.setVisibility(View.GONE);
        }else {
            matchWordReporter.setText("You Got A Match! : " + matchedWord);
            selectIt.setVisibility(View.VISIBLE);
            selectIt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectIt.setVisibility(View.GONE);
                    matchWordReporter.setText("");
                    gameFragment.finishATile(largeTile);
                }
            });
        }
    }

    public void reportMatchedWordPhase2(final String matchedWord){
        matchWordReporter = (TextView) findViewById(R.id.matchWordReporter);
        selectIt = (Button) findViewById(R.id.selectIt);
        if (matchedWord == null){
            matchWordReporter.setText("");
            selectIt.setVisibility(View.GONE);
        }else {
            matchWordReporter.setText("You Got A Match! : " + matchedWord);
            selectIt.setVisibility(View.VISIBLE);
            selectIt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectIt.setVisibility(View.GONE);
                    matchWordReporter.setText("");
                    gameFragment.finishPhase2();
                }
            });
        }

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

        //store game information
        String stateData = gameFragment.getState();
        getPreferences(MODE_PRIVATE).edit()
                .putString(State_RESTORE, stateData)
                .apply();
        Log.d("UT3", "state = " + stateData);

        String wordData = gameFragment.getWords();
        getPreferences(MODE_PRIVATE).edit()
                .putString(Word_RESTORE, wordData)
                .apply();

        Log.d("UT3", "state = " + wordData);

        //phase 1
        String moveData = gameFragment.getMoveTrack();
        getPreferences(MODE_PRIVATE).edit()
                .putString(MoveTrack_RESTORE, moveData)
                .apply();
        Log.d("UT3", "state = " + moveData);


        String timer = String.valueOf(stoppedTime);
        getPreferences(MODE_PRIVATE).edit()
                .putString(Timer_RESTORE, timer)
                .apply();

        Log.d("UT3", "state = " + timer);

        String phaseData = String.valueOf(phase);
        getPreferences(MODE_PRIVATE).edit()
                .putString(Phase_RESTORE, phaseData)
                .apply();

        Log.d("UT3", "state = " + phaseData);
    }


    public void putNineCharDictionaryIntoFragment(){

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("start thiking thinking thinking");

                        InputStreamReader input = new InputStreamReader(getResources().openRawResource(R.raw.wordlist));
                        BufferedReader r = new BufferedReader(input);
                        String word;
                        //convert word to hashset

                        try {
                            while ((word = r.readLine()) != null) {
                                if (word.length() == 9) nineCharWordDictionary.add(word);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println("finish thiking thinking thinking");

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                gameFragment.initAllSmallTiles(nineCharWordDictionary);
                            }
                        });

                        System.out.println("quit thiking thinking thinking");


                    }
                }
        ).start();
    }



    public void putDictionaryIntoFragment(){

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("finish thiking thinking thinking1");

                        InputStreamReader input = new InputStreamReader(getResources().openRawResource(R.raw.wordlist));
                        BufferedReader r = new BufferedReader(input);
                        String word;
                        //convert word to hashset

                        try {
                            while ((word = r.readLine()) != null) {
                                dictionary.add(word);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        System.out.println("finish thiking thinking thinking1");

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                gameFragment.initDictionary(dictionary);
                            }
                        });

                        System.out.println("finish thiking thinking thinking1");


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
