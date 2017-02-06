package edu.neu.madcourse.yongqichao;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;
import com.google.gson.annotations.SerializedName;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.FileNotFoundException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Scanner;

import static java.lang.System.out;


public class MainActivity extends AppCompatActivity {

    public Trie a = new Trie();
    public Trie b = new Trie();
    public Trie c = new Trie();
    public Trie d = new Trie();
    public Trie e = new Trie();
    public Trie f = new Trie();
    public Trie g = new Trie();
    public Trie h = new Trie();
    public Trie i = new Trie();
    public Trie j = new Trie();
    public Trie k = new Trie();
    public Trie l = new Trie();
    public Trie m = new Trie();
    public Trie n = new Trie();
    public Trie o = new Trie();
    public Trie p = new Trie();
    public Trie q = new Trie();
    public Trie r = new Trie();
    public Trie s = new Trie();
    public Trie t = new Trie();
    public Trie u = new Trie();
    public Trie v = new Trie();
    public Trie w = new Trie();
    public Trie x = new Trie();
    public Trie y = new Trie();
    public Trie z = new Trie();


    FileOutputStream outputStream;


    RelativeLayout background;
    Button errorButton, aboutButton, dictionaryButton, quitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Yongqi Chao");



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
                //dictionary.putExtra("MyClass", n);
                startActivity(dictionary);

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



        //n.addString("aah");
        //n.addString("android");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();


        Map<String,String> newHash= new HashMap<>();

        // SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String name = preferences.getString("installation", "not finish");
        if(!name.equalsIgnoreCase("done")) {
            Intent install = new Intent(MainActivity.this, Installation_Activity.class);
            startActivity(install);
            editor.putString("installation","done");
            editor.apply();
        }



        //Gson gson= new Gson();
        //String json=gson.toJson(n);
        //editor.putString("low","aah");
//        editor.apply();//name = json;

//        FileOutputStream fileOutputStream = null;
//        try {
//             fileOutputStream = this.openFileOutput("myMap.whateverExtension", Context.MODE_ENABLE_WRITE_AHEAD_LOGGING);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
////        FileOutputStream fileOutputStream = null;
////        try {
////            fileOutputStream = new FileOutputStream("myMap.whateverExtension");
////        } catch (FileNotFoundException e) {
////            e.printStackTrace();
////        }
//        ObjectOutputStream objectOutputStream= null;
//        try {
//            objectOutputStream = new ObjectOutputStream(fileOutputStream);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            objectOutputStream.writeObject(n);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            objectOutputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

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
            file = new File(getCacheDir(), "MyCache");
            output = new FileOutputStream(file);
            output.write(content.getBytes());
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        System.out.println("MyFileMyFileMyFileMyFileMyFileMyFileMyFileMyFileMyFileMyFileMyFileMyFileMyFileMyFileMyFileMyFileMyFileMyFileMyFileMyFile: ");
//
//        if(outputStream == null) {
//            try {
//                outputStream = openFileOutput("a", Context.MODE_PRIVATE);
//                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
//                objectOutputStream.writeObject(newHash);
//                objectOutputStream.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        if(outputStream == null) {
//            try {
//                outputStream = openFileOutput("b", Context.MODE_PRIVATE);
//                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
//                objectOutputStream.writeObject(b);
//                objectOutputStream.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        if(outputStream == null) {
//            try {
//                outputStream = openFileOutput("d", Context.MODE_PRIVATE);
//                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
//                objectOutputStream.writeObject(d);
//                objectOutputStream.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        if(outputStream == null) {
//            try {
//                outputStream = openFileOutput("z", Context.MODE_PRIVATE);
//                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
//                objectOutputStream.writeObject(z);
//                objectOutputStream.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }


    }

    public Trie getN() {
        return n;
    }

}

