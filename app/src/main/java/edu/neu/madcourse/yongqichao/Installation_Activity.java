package edu.neu.madcourse.yongqichao;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;


public class Installation_Activity extends Activity {
    TextView installText;
    public char firstletter;
    public char nextletter;
    public String word;
    public Scanner text;
    public HashSet<String> newHash= new HashSet<>();

    public File file;
    public FileOutputStream output = null;

    private ProgressBar progressBar;
    private int mProgressStatus = 0;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installation);
        setTitle("Installation App");

        installText = (TextView) findViewById(R.id.installText);
        installText.setText("installing dictionary component");

        //Use shared preference to tell phone whether it installed dictionary component
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = preferences.edit();

        //read wordlist text file from raw
        text = new Scanner(getResources().openRawResource(R.raw.wordlist));

        //add a progress bar to screen
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(480000);
        progressBar.setScaleY(6f);
        final MediaPlayer song = MediaPlayer.create(this, R.raw.shapeofyou);

        //convert words from text file to hashmap and save into internal storage
        new Thread(new Runnable() {
            @Override
            public void run() {
                song.start();
                firstletter = 'a';
                //convert 'a' - 'y' to hashmap and save it.
                while (text.hasNext()){
                    mProgressStatus += 1;
                    word = text.next();
                    nextletter = word.charAt(0);
                    if(nextletter != firstletter){
                        try {
                            output = openFileOutput(""+firstletter, Context.MODE_PRIVATE);
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(output);
                            objectOutputStream.writeObject(newHash);
                            objectOutputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        firstletter++;
                        //to avoid out of memory
                        newHash.clear();
                    }
                    newHash.add(word);

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(mProgressStatus);
                            installText.setText("installing dictionary component:" + mProgressStatus/4800 + "%");
                        }
                    });
                }
                text.close();

                //save 'z' hashmap to internal storage.
                try {
                    output = openFileOutput(""+firstletter, Context.MODE_PRIVATE);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(output);
                    objectOutputStream.writeObject(newHash);
                    objectOutputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                song.stop();
                //tell the phone that it installed this dictionary component.
                editor.putString("installation","done");
                editor.apply();
                finish();
            }
        }).start();
    }
}
