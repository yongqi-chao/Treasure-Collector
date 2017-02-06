package edu.neu.madcourse.yongqichao;

//import com.google.gson.annotations.SerializedName;
//import com.google.gson.Gson;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;


public class DictionaryActivity extends AppCompatActivity {
    Button returnButton, clearButton, acknowledgementButton;
    AutoCompleteTextView enterText;
    ListView listofword;
    FileInputStream inputStream;

    public HashSet<String> newHash= new HashSet<>();

    public ProgressBar progressBar;
    TextView diconaryLoadingInfo, hint;
    public char firstletter;
    private int mProgressStatus = 1;
    private Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);
        setTitle("Test Dictionary");

        diconaryLoadingInfo = (TextView) findViewById(R.id.dictionaryLoadingInfo);
        diconaryLoadingInfo.setText("Indexing dictionary");
        hint = (TextView) findViewById(R.id.hint);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar.setMax(26);
        progressBar.setScaleY(6f);

        //read pre-saved hashmap(world list) from internal storage.
        new Thread(new Runnable() {
            @Override
            public void run() {
                firstletter = 'a';
                for (;mProgressStatus <= 26 ; mProgressStatus ++){
                    //loading hashmap
                    try {
                        inputStream = openFileInput("" + firstletter);
                        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                        HashSet<String> adder = (HashSet<String>) objectInputStream.readObject();
                        newHash.addAll(adder);
                        objectInputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();}
                    firstletter++;

                    //show loading progress on screen
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(mProgressStatus < 25) {
                                progressBar.setProgress(mProgressStatus);
                                String info = "Indexing dictionary:" + mProgressStatus + "/26" + "\n" + "abcdefghijklmnopqrstuvwxyz";
                                Spannable spannable = new SpannableString(info);
                                spannable.setSpan(new ForegroundColorSpan(Color.GREEN), 25,
                                        mProgressStatus + 25, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                spannable.setSpan(new ForegroundColorSpan(Color.RED),
                                        mProgressStatus + 25, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                diconaryLoadingInfo.setText(spannable, TextView.BufferType.SPANNABLE);
                            }
                            else{
                                progressBar.setVisibility(View.GONE);
                                diconaryLoadingInfo.setText("Indexing dictionary: FINISHED");
                                hint.setText("");
                            }
                        }
                    });
                }
            }
        }).start();


        //a way of displaying a list of words.
        listofword = (ListView) findViewById(R.id.listofw);
        final ArrayList<String> list = new ArrayList<>();

        //a way of entering text
        enterText = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        //beep sound when find a word
        final MediaPlayer clickSound = MediaPlayer.create(this, R.raw.beep);

        //implementation of entering text and adding to the list
        enterText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0  ){
                    if(isAlpha(s.toString()) && (newHash.contains(s.toString()) )){
                        if(!list.contains(s.toString())) {
                            list.add(s.toString());
                            clickSound.start();
                        }
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });


        //Display a list of words
        ArrayAdapter adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                list);
        listofword.setAdapter(adapter);


        //add CLEAR button
        clearButton = (Button) findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterText.setText("");
                list.clear();
            }
        });

        //acknowledgements
        acknowledgementButton = (Button) findViewById(R.id.acknowledgment);
        acknowledgementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent acknowledgement = new Intent(DictionaryActivity.this, Acknowledgements.class);
                startActivity(acknowledgement);
            }
        });

        //add RETURN button
        returnButton = (Button) findViewById(R.id.returnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //return true iff the input string does not contain capital character
    public boolean isAlpha(String name) {
        return name.matches("[a-z]+");
    }

}