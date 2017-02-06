package edu.neu.madcourse.yongqichao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static java.lang.System.out;

public class Installation_Activity extends Activity {
    private static final int PROGRESS = 0x1;

    TextView installText;
    public char firstletter;
    public char nextletter;
    public String ss;
    public Scanner text;
    public Map<String,String> newHash= new HashMap<>();

    public File file;// = new File("Attempt1");
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


        text = new Scanner(getResources().openRawResource(R.raw.wordlist));

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(480000);
        progressBar.setScaleY(6f);
        final MediaPlayer song = MediaPlayer.create(this, R.raw.shapeofyou);


        new Thread(new Runnable() {
            @Override
            public void run() {
                song.start();
                firstletter = 'a';
                while (text.hasNext()){
                    mProgressStatus += 1;
                    ss = text.next();
                    nextletter = ss.charAt(0);
                    if(nextletter != firstletter){
                        //HashMap.class.getField(name).set(this,Boolean.TRUE);
                        try {
                            output = openFileOutput(""+firstletter, Context.MODE_PRIVATE);
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(output);
                            objectOutputStream.writeObject(newHash);
                            objectOutputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        firstletter++;
                        newHash.clear();

                    }
                    newHash.put(ss,ss);
//
//                    if(firstletter == 'a' || firstletter == 'z'
//                                          || firstletter == 'x'){
//                        System.out.println("installl this QQQQQQWERTYUI" + mProgressStatus);
//
//                    }

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            progressBar.setProgress(mProgressStatus);
                            installText.setText("installing dictionary component:" + mProgressStatus/5000 + "%");
                        }
                    });
                }

                text.close();

                //finishSaveData(newHash);
                ///////////////////////////////////////////////////////////////////////////////
//                System.out.println("finish this hash table");
//                String fileName = "MyFile";
//                Gson gson= new Gson();
//                System.out.println(newHash.size());
//                String json=gson.toJson(newHash);
//                String content = json;
//                System.out.println(json.length());
//                //chang du 7------   jian shao   hashmap string de chang du???
//
//                try {
//                    // file = File.createTempFile("MyCache", null, getCacheDir());
//                    //file = new File(getCacheDir(), "Attampt1");
//                    System.out.println("qazqazqazqazqazqazqazqazzqazqazqazqzazqazqazqzzqazzqaswwsxsxsddcdcrccrdcdrdccdrcdrcdrcdrcrdcdrcdrcdrcdrcdrcdrcdrcrdcrddcrrdccrd");
//                    output = openFileOutput("Attempt1", Context.MODE_PRIVATE);
//                    //output = new FileOutputStream(file);//, Context.MODE_PRIVATE);
//                    output.write(content.getBytes());
//                    output.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                ///////////////////////////////////////////////////////////////////////////////
                if(output == null) {
//            try {
//                output = openFileOutput(""+firstletter, Context.MODE_PRIVATE);
//                ObjectOutputStream objectOutputStream = new ObjectOutputStream(output);
//                objectOutputStream.writeObject(newHash);
//                objectOutputStream.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
                try {
                    output = openFileOutput(""+firstletter, Context.MODE_PRIVATE);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(output);
                    objectOutputStream.writeObject(newHash);
                    objectOutputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

//                Intent main = new Intent(Installation_Activity.this, MainActivity.class);
//                startActivity(main);
                song.stop();
                finish();
            }
        }).start();







//        try {
//            while (text.hasNext()){ //for (int aint = 0; aint< 100; aint++){// (s.hasNext()) {
//                ss = text.next();
//                firstletter = ss.charAt(0);
//                if(firstletter == 'a'){
//                    newHash.put(ss,ss);}
//
//            }
//            Intent main = new Intent(Installation_Activity.this, MainActivity.class);
//            startActivity(main);
//
//        } catch (Exception e) {
//            out.println("I caught: " + e);
//        }
//
//        text.close();
    }

    public void finishSaveData(Map newHash){
        ///////////////////////////////////////////////////////////////////////////////
        System.out.println("finish this hash table");
        String fileName = "MyFile";
        Gson gson= new Gson();
        System.out.println(newHash.size());
        String json=gson.toJson(newHash);
        String content = json;
        System.out.println(json.length());
        //chang du 7------   qudiao   hashmap string de chang du???



        File file;
        FileOutputStream output;
        try {
            // file = File.createTempFile("MyCache", null, getCacheDir());
            file = new File(getCacheDir(), "Attampt1");
            output = new FileOutputStream(file);
            output.write(content.getBytes());
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ///////////////////////////////////////////////////////////////////////////////

    }
}
