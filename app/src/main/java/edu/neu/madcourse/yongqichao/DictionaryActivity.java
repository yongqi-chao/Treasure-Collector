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
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;


public class DictionaryActivity extends AppCompatActivity {
    Button returnButton, clearButton, acknowledgementButton;
    AutoCompleteTextView enterText;
    ListView listofword;
    public static final String Dictionary_RESTORE = "Dictionary_restore";

    public HashSet<String> newHash= new HashSet<>();
    private ArrayList<String> list = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);
        setTitle("Test Dictionary");

        String dictionaryData = getPreferences(MODE_PRIVATE).getString(Dictionary_RESTORE, null);
        if (dictionaryData != null) {
            list = parseToList(dictionaryData);
        }

        //dismiss keyboard at the start
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //read words from raw file wordlist.txt
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        InputStreamReader input = new InputStreamReader(getResources().openRawResource(R.raw.wordlist));
                        BufferedReader r = new BufferedReader(input);
                        String word;
                        //convert word to hashset
                        try {
                            while ((word = r.readLine()) != null){
                                newHash.add(word);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).start();


        //a way of displaying a list of words.
        listofword = (ListView) findViewById(R.id.listofw);

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


    public ArrayList<String> parseToList(String dictionaryData){
        String[] fields = dictionaryData.split(",");
        ArrayList<String> listofwords = new ArrayList<>();
        for (int index = 0; index < fields.length; index++) {
            listofwords.add(fields[index]);
        }
        return  listofwords;
    }


    public String parseToString(ArrayList<String> listofwords){
        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < listofwords.size(); index++) {
            builder.append(listofwords.get(index));
            builder.append(',');
        }
        return builder.toString();
    }



    @Override
    protected void onPause() {
        super.onPause();
        String dictionaryData = parseToString(list);
        getPreferences(MODE_PRIVATE).edit()
                .putString(Dictionary_RESTORE, dictionaryData)
                .commit();
    }

}