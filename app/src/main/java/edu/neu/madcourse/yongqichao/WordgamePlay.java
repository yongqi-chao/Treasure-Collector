package edu.neu.madcourse.yongqichao;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

import edu.neu.madcourse.yongqichao.firebase.FCMActivity;
import edu.neu.madcourse.yongqichao.leaderboard.WordgameRecord;

public class WordgamePlay extends AppCompatActivity {
    public static final String USER_NAME_RESTORE = "username_restore";
    public static final String USER_NAME = "username";
    private DatabaseReference mDatabase;

    public enum Phase {
        Phase1, Phase2,PhaseTimeOut,GameFinish
    }

    public static final String KEY_RESTORE = "key_restore";
    public static final String State_RESTORE = "state_restore";
    public static final String Word_RESTORE = "word_restore";
    public static final String MoveTrack_RESTORE = "moveTrack_restore";
    public static final String Timer_RESTORE = "timer_restore";
    public static final String Phase_RESTORE = "phase_restore";
    public static final String Score_RESTORE = "score_restore";
    public static final String GameProgress_RESTORE = "gameProgress_restore";
    public static final String SCORE_BOARD = "score_board";
    public static final String LEADER_BOARD = "learder_board";
    public static final String LAST_HIGHESTSCORE_WITH_WORD = "lastscore_with_word";
    String lastHigestScore_word = "";
    private Handler mHandler = new Handler();
    private WordgamePlayFragment gameFragment;

    private ArrayList<String> nineCharWordDictionary = new ArrayList<>();
    private ArrayList<String> dictionary = new ArrayList<>();
    private Chronometer chronometer;
    private TextView matchWordReporter, gameScore;
    public Button selectIt,startPhase2Button;
    private Phase phase = Phase.Phase1;
    private long stoppedTime;
    public int score;
    private int gameProgress;
    Thread nineWordDictionary, mainDictionary;
    private Vibrator v;
    private MediaPlayer matchSound;
    private ArrayList<String> scoreboard = new ArrayList<>();
    private ArrayList<String> leaderlist = new ArrayList<>();
    private String username;


    // Please add the server key from your firebase console in the follwoing format "key=<serverKey>"
    private static final String SERVER_KEY = "key=AAAAuseFPB0:APA91bFsOFJtvWkdBwOtXHLZ8OjkgzAI3D6xUCKLdCZaFCi4nwROmz5wWwuZXP9IWJ_jXHQUmX-XqkDyNQ3qca3M8JLYLsoox54_xfTIHpsKalsL4NywiinkTILmPhGqTZh3HG-5fqQf";

    // This is the client registration token
    private static final String CLIENT_REGISTRATION_TOKEN = FirebaseInstanceId.getInstance().getToken();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordgame_play);
        setTitle("Word Game");

        System.out.println("Refreshed token: " + FirebaseInstanceId.getInstance().getToken());
        System.out.println("Refreshed token: " + CLIENT_REGISTRATION_TOKEN);

        v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        matchSound = MediaPlayer.create(this, R.raw.oldedgar_winner);

        //start loading game fregment
        gameFragment = (WordgamePlayFragment) getFragmentManager()
                .findFragmentById(R.id.fragment_game);
        boolean restore = getIntent().getBooleanExtra(KEY_RESTORE, false);
        String usernamerestore = getIntent().getStringExtra(USER_NAME_RESTORE);
        //start timer
        startChronometer(gameFragment);

        if (restore) {
            //restore Phase
            String phaseData = getPreferences(MODE_PRIVATE)
                    .getString(Phase_RESTORE, null);
            if (phaseData != null) {
                phase = Phase.valueOf(phaseData);
            }
            //restore game score
            String scoreData = getPreferences(MODE_PRIVATE)
                    .getString(Score_RESTORE, null);
            if (scoreData != null) {
                score = Integer.parseInt(scoreData);
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
            //restore game progress
            String progressData = getPreferences(MODE_PRIVATE)
                    .getString(GameProgress_RESTORE, null);
            if (progressData != null) {
                gameProgress = Integer.parseInt(progressData);
            }
            //restore lastHigestScore_word
            lastHigestScore_word = getPreferences(MODE_PRIVATE)
                    .getString(LAST_HIGHESTSCORE_WITH_WORD, "");
            //restore username
            String usernameData = getPreferences(MODE_PRIVATE)
                    .getString(USER_NAME, null);
            if (usernameData != null) {
                username = usernameData;
            }
        } else score = 0;
        displayScore();

        //restore username
        if(usernamerestore != null && usernamerestore != "") {
            username = usernamerestore;
        }

        putNineCharDictionaryIntoFragment();
        putDictionaryIntoFragment();
        loadLeaderBoard();

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
                switch (phase) {
                    case Phase1:
                        // finish phase 1 and prepare to start phase 2
                        if (passedTime >= 90000) {
                            gameFragment.finishGame();
                            selectIt = (Button) findViewById(R.id.selectIt);
                            chronometer.stop();
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "TIME UP - PHASE 1 ENDED", Toast.LENGTH_LONG);
                            toast.show();
                            //clear view content
                            matchWordReporter = (TextView) findViewById(R.id.matchWordReporter);
                            matchWordReporter.setText("");
                            selectIt.setVisibility(View.GONE);
                            //clear fragment words
                            phase = Phase.PhaseTimeOut;
                            //----------------------------
                            //start phase 2
//                            startPhase2Button = (Button) findViewById(R.id.startPhase2);
                            startPhase2Button.setVisibility(View.VISIBLE);
                            toast = Toast.makeText(getApplicationContext(),
                                    "(SCORE will be multiplied in phase 2)", Toast.LENGTH_LONG);
                            toast.show();
                            startPhase2Button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startPhase2Button.setVisibility(View.GONE);
                                    startPhase2(gameFragment);
                                    phase = Phase.Phase2;
                                    chronometer.setBase(SystemClock.elapsedRealtime()-stoppedTime);
                                    chronometer.start();
                                    displayScore();
                                }
                            });
                        }
                        break;
                    case PhaseTimeOut:
                        chronometer.stop();
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Phase 1 Finished", Toast.LENGTH_LONG);
                        toast.show();
                        //start phase 2 button
                        startPhase2Button.setVisibility(View.VISIBLE);
                        toast = Toast.makeText(getApplicationContext(),
                                "(SCORE will be multiplied in phase 2)", Toast.LENGTH_LONG);
                        toast.show();
                        startPhase2Button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startPhase2Button.setVisibility(View.GONE);
                                startPhase2(gameFragment);
                                phase = Phase.Phase2;
                                chronometer.setBase(SystemClock.elapsedRealtime()-stoppedTime);
                                chronometer.start();
                                displayScore();
                            }
                        });
                        break;
                    case Phase2:
                        if (passedTime >= 180000) {
                            chronometer.stop();
                            toast = Toast.makeText(getApplicationContext(),
                                    "TIME UP - PHASE 2 ENDED", Toast.LENGTH_LONG);
                            toast.show();
                            //clear view content
                            matchWordReporter = (TextView) findViewById(R.id.matchWordReporter);
                            selectIt = (Button) findViewById(R.id.selectIt);
                            matchWordReporter.setText("");
                            selectIt.setVisibility(View.GONE);
                            //clear fragment words
                            gameFragment.finishPhase2();
                            //report t0 leaderboard and scoreboard
                            addtoScoreBoard();
                            addtoleaderBoard();
                        }
                        break;
                    case GameFinish:
                        chronometer.stop();
                        toast = Toast.makeText(getApplicationContext(),
                                "Game Finished", Toast.LENGTH_LONG);
                        toast.show();
                        break;
                }
            }
        });
    }

    public void startPhase2(final WordgamePlayFragment gameFragment){
        gameFragment.initPhase2();
    }

    public void pauseGame(){
        chronometer.stop();
        gameFragment.getView().setVisibility(View.GONE);
    }

    public void resumeGame(){
        gameFragment.getView().setVisibility(View.VISIBLE);
        chronometer.setBase(SystemClock.elapsedRealtime()-stoppedTime);
        chronometer.start();
    }

    public void restartGame() {
        gameFragment.restartGame();
        gameFragment.getView().setVisibility(View.VISIBLE);
        gameProgress = 0;

        //reset buttons
        //startPhase2Button = (Button) findViewById(R.id.startPhase2);
        matchWordReporter = (TextView) findViewById(R.id.matchWordReporter);
        selectIt = (Button) findViewById(R.id.selectIt);
        matchWordReporter.setText("");
        selectIt.setVisibility(View.GONE);
        startPhase2Button.setVisibility(View.GONE);
        phase = Phase.Phase1;
        score = 0;
        displayScore();

        //restart timer from 00:00
        //90,000 is 90 seconds , 1:30 mins
        chronometer.setBase(SystemClock.elapsedRealtime());
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
                },3000);

    }

    public void reportMatchedWord(final String matchedWord, final int largeTile){
        matchWordReporter = (TextView) findViewById(R.id.matchWordReporter);
        selectIt = (Button) findViewById(R.id.selectIt);


        if (matchedWord == null){
            matchWordReporter.setText("");
            selectIt.setVisibility(View.GONE);
        }else {
            //remember the highest score words
            if(matchedWord.length() > lastHigestScore_word.length()){
                lastHigestScore_word = matchedWord;
            }

            v.vibrate(500);
            matchSound.start();
            matchWordReporter.setText("You Got A Match! : " + matchedWord);
            selectIt.setVisibility(View.VISIBLE);
            selectIt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectIt.setVisibility(View.GONE);
                    matchWordReporter.setText("");
                    gameFragment.finishATile(largeTile);
                    score = score + matchedWord.length();
                    displayScore();
                    gameProgress++;
                    v.vibrate(500);
                    matchSound.start();
                    if(gameProgress >= 9){
                        chronometer.setBase(SystemClock.elapsedRealtime()-90000);
                    }
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
            //remember the highest score words
            if(matchedWord.length() > lastHigestScore_word.length()){
                lastHigestScore_word = matchedWord;
            }

            v.vibrate(500);
            matchSound.start();
            matchWordReporter.setText("You Got A Match! : " + matchedWord);
            selectIt.setVisibility(View.VISIBLE);
            selectIt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectIt.setVisibility(View.GONE);
                    matchWordReporter.setText("");
                    v.vibrate(500);
                    matchSound.start();
                    gameFragment.finishPhase2();
                    //multiply for phase 2
                    score = score * matchedWord.length();
                    displayScore();
                    //finish the game before time
                    phase = Phase.GameFinish;
                    //report t0 leaderboard and scoreboard
                    addtoScoreBoard();
                    addtoleaderBoard();
                }
            });
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(null);

        //store game information
        String stateData = gameFragment.getState();
        getPreferences(MODE_PRIVATE).edit()
                .putString(State_RESTORE, stateData)
                .apply();
        Log.d("wordgame", "state = " + stateData);

        String wordData = gameFragment.getWords();
        getPreferences(MODE_PRIVATE).edit()
                .putString(Word_RESTORE, wordData)
                .apply();

        Log.d("wordgame", "state = " + wordData);

        //phase 1
        String moveData = gameFragment.getMoveTrack();
        getPreferences(MODE_PRIVATE).edit()
                .putString(MoveTrack_RESTORE, moveData)
                .apply();
        Log.d("wordgame", "state = " + moveData);

        String timer = String.valueOf(stoppedTime);
        getPreferences(MODE_PRIVATE).edit()
                .putString(Timer_RESTORE, timer)
                .apply();

        Log.d("wordgame", "state = " + timer);

        String phaseData = String.valueOf(phase);
        getPreferences(MODE_PRIVATE).edit()
                .putString(Phase_RESTORE, phaseData)
                .apply();

        Log.d("wordgame", "state = " + phaseData);

        String scoreData = String.valueOf(score);
        getPreferences(MODE_PRIVATE).edit()
                .putString(Score_RESTORE, scoreData)
                .apply();

        Log.d("wordgame", "state = " + scoreData);

        String gameprogress = String.valueOf(gameProgress);
        getPreferences(MODE_PRIVATE).edit()
                .putString(GameProgress_RESTORE, gameprogress)
                .apply();

        Log.d("wordgame", "state = " + gameprogress);

        getPreferences(MODE_PRIVATE).edit()
                .putString(LAST_HIGHESTSCORE_WITH_WORD, lastHigestScore_word)
                .apply();

        Log.d("wordgame", "state = " + lastHigestScore_word);

        getPreferences(MODE_PRIVATE).edit()
                .putString(USER_NAME, username)
                .apply();

        Log.d("wordgame", "state = " + username);
    }


    public void putNineCharDictionaryIntoFragment(){

        nineWordDictionary = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
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
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                gameFragment.initAllSmallTiles(nineCharWordDictionary);
                            }
                        });
                    }
                }
        );
        nineWordDictionary.start();
    }


    public void putDictionaryIntoFragment(){

        mainDictionary = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
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
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                gameFragment.initDictionary(dictionary);
                            }
                        });
                    }
                }
        );
        mainDictionary.start();
    }


    /////////SCOREBOARD ------------------------------------///////////////////////////
    public void addtoScoreBoard(){
        loadScoreBoard();

        Calendar rightNow = Calendar.getInstance();
        StringBuilder builder = new StringBuilder();
        builder.append("Played at: " + rightNow.getTime() + "\n");
        builder.append("Highest Score: " + lastHigestScore_word.length() + "---" + lastHigestScore_word + "\n");
        builder.append("FINAL SCORE:" + score);
        insertIntoScoreboard(score,builder.toString());

        saveScoreBoard();
    }

    public void insertIntoScoreboard(int score, String word){
        if(scoreboard.isEmpty()){
            scoreboard.add(Integer.toString(score));
            scoreboard.add(word);
        }
        else {
            for (int index = 0; index < scoreboard.size(); index++){
                int scoreFromTheBoard = Integer.valueOf(scoreboard.get(index));
                int inputPosition = index;
                if(score > scoreFromTheBoard){
                    scoreboard.add(inputPosition++,Integer.toString(score));
                    scoreboard.add(inputPosition,word);
                    if(scoreboard.size() > 20){
                        scoreboard.remove(21);
                        scoreboard.remove(20);
                    }
                    break;
                }
                index++;
            }
        }
    }

    public void loadScoreBoard(){
        SharedPreferences score_board = getSharedPreferences(SCORE_BOARD, 0);
        String scoreboardData = score_board.getString(SCORE_BOARD, null);
        if (scoreboardData != null) {
            String[] fields = scoreboardData.split(",");
            for (int index = 0; index < fields.length; index++){
                scoreboard.add(fields[index]);
            }
        }
    }

    public void saveScoreBoard(){
        SharedPreferences score_board = getSharedPreferences(SCORE_BOARD, 0);
        String scoreboardData = score_board.getString(SCORE_BOARD, null);
        //build a score board string
        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < scoreboard.size(); index++){
            builder.append(scoreboard.get(index));
            builder.append(",");
        }

        SharedPreferences.Editor editor = score_board.edit();
        editor.putString(SCORE_BOARD, builder.toString());
        // Commit the edits!
        editor.apply();
    }

    /////////LEADERBOARD ------------------------------------///////////////////////////
    public void addtoleaderBoard(){
//        loadLeaderBoard();

        Calendar rightNow = Calendar.getInstance();
        StringBuilder builder = new StringBuilder();
        String user = (username == null || username.equals(""))? "Anonymous": username;
        builder.append(user + ",");
        builder.append(rightNow.getTime() + ",");
        builder.append(lastHigestScore_word + "--" + lastHigestScore_word.length() + ",");
        //send notification
        StringBuilder notify = new StringBuilder();
        notify.append(user + " achieve a new score: " + score);
        //do the insertion action
        insertIntoLeaderboard(score,builder.toString(), notify.toString());

        saveLeaderBoard();
    }

    public void insertIntoLeaderboard(int score, String word, String noti){
        for (int index = 0; index < leaderlist.size(); index++){
            int scoreFromTheBoard = Integer.valueOf(leaderlist.get(index));
            int inputPosition = index;
            System.out.println("32123121312312251325433453545341541315431431234121212211231221"+scoreFromTheBoard);
            if(score > scoreFromTheBoard){
                System.out.println("leaderboard!@#$%^&*34262167898679342786546781678234143678342768!@!@#$#@!$@#!@!#@#$%#$^#$%^");
                leaderlist.add(inputPosition++,Integer.toString(score));
                leaderlist.add(inputPosition,word);
                if(leaderlist.size() > 20){
                    leaderlist.remove(21);
                    leaderlist.remove(20);
                }
                pushNotification(noti);
                break;
            }
            index++;
        }
    }

    public void loadLeaderBoard(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(int i = 1; i<11;i++) {
                    String number = "Number" + i;
                    WordgameRecord records = dataSnapshot.child(number).getValue(WordgameRecord.class);
                    String inputRecord = records.username + "," + records.date +
                            "," + records.wordWithHighestScore;
                    leaderlist.add(records.finalScore);
                    leaderlist.add(inputRecord);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        };
        mDatabase.child("wordGameRecords").addListenerForSingleValueEvent(postListener);
    }

    public void saveLeaderBoard(){
        int N = 1;
        for(int i = 0; i<leaderlist.size();i++) {
            String number = "Number" + N++;
            WordgameRecord record = new WordgameRecord();
            //add score to database
            record.finalScore = leaderlist.get(i++);
            String userinfo = leaderlist.get(i);
            //add other infomation to databse
            String[] fields = userinfo.split(",");
            record.username = fields[0];
            record.date = fields[1];
            record.wordWithHighestScore = fields[2];

            mDatabase.child("wordGameRecords").child(number).setValue(record);
        }

    }



    ////display score on the screen

    public void displayScore(){
        gameScore = (TextView) findViewById(R.id.gameScore);
        gameScore.setText("Your Score is: " + score);
    }


    //SEND NOTIFICATION TO USERS @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    public void pushNotification(final String body) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                pushNotificationThread(body);
            }
        }).start();
    }

    /**
     * Pushes a notification to a given device-- in particular, this device,
     * because that's what the instanceID token is defined to be.
     */
    private void pushNotificationThread(String body) {
        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        try {
            jNotification.put("title", "WordGame LEADERBOARD");
            jNotification.put("body", body);
            jNotification.put("sound", "default");
            jNotification.put("badge", "1");
            jNotification.put("click_action", "OPEN_ACTIVITY_1");

            // If sending to a single client
            jPayload.put("to", "/topics/news");//CLIENT_REGISTRATION_TOKEN);

//            // If sending to multiple clients (must be more than 1 and less than 1000)
//            JSONArray ja = new JSONArray();
//            ja.put(CLIENT_REGISTRATION_TOKEN);
//            // Add Other client tokens
//            ja.put(FirebaseInstanceId.getInstance().getToken());
//            jPayload.put("registration_ids", ja);


            jPayload.put("priority", "high");
            jPayload.put("notification", jNotification);


            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", SERVER_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Send FCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jPayload.toString().getBytes());
            outputStream.close();

            // Read FCM response.
            InputStream inputStream = conn.getInputStream();
            final String resp = convertStreamToString(inputStream);

//            Handler h = new Handler(Looper.getMainLooper());
//            h.post(new Runnable() {
//                @Override
//                public void run() {
//                    Log.e(TAG, "run: " + resp);
//                    Toast.makeText(FCMActivity.this,resp,Toast.LENGTH_LONG).show();
//                }
//            });
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }

    public void subscribeToNews(View view){
        // [START subscribe_topics]
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        // [END subscribe_topics]

        // Log and toast
        String msg = getString(R.string.msg_subscribed);
        //Log.d(TAG, msg);
       // Toast.makeText(FCMActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

}
