package edu.neu.madcourse.yongqichao;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class WordgameActivity extends AppCompatActivity {
    public static final String DICTIONARY_RESTORE = "wordgame_restore";

    private ArrayList<String> dictionary = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordgame);


//        new Thread(
//                new Runnable() {
//                    @Override
//                    public void run() {
//                        InputStreamReader input = new InputStreamReader(getResources().openRawResource(R.raw.wordlist));
//                        BufferedReader r = new BufferedReader(input);
//                        String word;
//                        //convert word to hashset
//
//                        try {
//                            while ((word = r.readLine()) != null){
//                                if(word.length() == 9) dictionary.add(word);
//                            }
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
////                        String dictionaryData = parseToString(dictionary);
////                        getPreferences(MODE_PRIVATE).edit()
////                                .putString(DICTIONARY_RESTORE, dictionaryData)
////                                .commit();
//
////                        Random random = new Random();
////                        int value = random.nextInt(dictionary.size());
//
//                    }
//                }
//        ).start();
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

    public String parseToString(ArrayList<String> listofwords){
        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < listofwords.size(); index++) {
            builder.append(listofwords.get(index));
            builder.append(',');
        }
        return builder.toString();
    }
}
