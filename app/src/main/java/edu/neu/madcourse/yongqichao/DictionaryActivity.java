package edu.neu.madcourse.yongqichao;

import com.google.gson.annotations.SerializedName;
import com.google.gson.Gson;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Timer;

import static java.lang.System.out;

public class DictionaryActivity extends AppCompatActivity {
    Button returnButton, clearButton;
    AutoCompleteTextView enterText;
    ListView listofword;
    FileInputStream inputStream;

    public Trie a = new Trie();
    public Trie b = new Trie();
    public Trie c = new Trie();
    public Trie d = new Trie();
    public Trie e = new Trie();
    //public Trie f = new Trie();
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
    public HashMap<String,String> newHash= new HashMap<>();

    public ProgressBar progressBar;
    TextView diconaryLoadingInfo, hint;
    public char firstletter;
    public String ss;
    public Scanner text;
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

        text = new Scanner(getResources().openRawResource(R.raw.wordlist));

        new Thread(new Runnable() {
            @Override
            public void run() {
                firstletter = 'a';
                for (; mProgressStatus <= 26 ; mProgressStatus ++){

                    //mProgressStatus += 1;
                    //if(inputStream == null) {
                        try {
                            inputStream = openFileInput("" + firstletter);
                            System.out.println(""+firstletter);
                            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                            HashMap<String,String> adder;//= new HashMap<>();
                            adder = (HashMap<String, String>) objectInputStream.readObject();
                            newHash.putAll(adder);
                            System.out.println(newHash.size());
                            objectInputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();}
                   // }
                    firstletter++;


                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(mProgressStatus < 25) {
                                progressBar.setProgress(mProgressStatus);
                                //diconaryLoadingInfo.setText("loading dictionary component:" + mProgressStatus + "/26");
                                String info = "Indexing dictionary:" + mProgressStatus + "/26" + "\n"
                                        + "abcdefghijklmnopqrstuvwxyz";
                                Spannable spannable = new SpannableString(info);

                                spannable.setSpan(new ForegroundColorSpan(Color.GREEN), 25, mProgressStatus + 25, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                spannable.setSpan(new ForegroundColorSpan(Color.RED), mProgressStatus + 25, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

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

                text.close();
            }
        }).start();






        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        // this.getSharedPreferences("low", Context.MODE_PRIVATE);
        //String low = prefs.getString("low", null);
        //Gson gson= new Gson();
        //Trie n = gson.fromJson(low,Trie.class);


        System.out.println("start this q new trie");


        File file;
        FileInputStream input;
//        try {
//            // file = File.createTempFile("MyCache", null, getCacheDir());
//            //file = new File(getCacheDir(), "Attempt1");
//            //input = new FileInputStream(file);
//            input = openFileInput("Attempt1");
//            Gson gson= new Gson();
//            //input.read();
//            StringBuilder builder = new StringBuilder();
//            int ch;
//            while((ch = input.read()) != -1){
//                builder.append((char)ch);
//            }
//            String asa = builder.toString();
//            //q = gson.fromJson(asa,Trie.class);
//            System.out.println(builder.length());
//            newHash = gson.fromJson(asa, HashMap.class);
//            System.out.println(newHash.size());
//            input.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


       // Intent ii = getIntent();

//        Scanner s = new Scanner(getResources().openRawResource(R.raw.wordlist));
//
//
//        try {
//            while (s.hasNext()) {
//                n.addString(s.next());
//                // ....
//
//            }
//
//        } catch (Exception e) {
//            out.println("I caught: " + e);
//        }
//
//        s.close();
        System.out.println("finish this q trie");
//
//        //read trie
//        if(inputStream == null) {
//            try {
//                inputStream = openFileInput("axz");
//                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
//                newHash = (HashMap<String, String>) objectInputStream.readObject();
//                objectInputStream.close();
//            } catch (Exception e) {
//                e.printStackTrace();}
//        }

//        System.out.println("finish this a trie");
//        if(inputStream == null) {
//            try {
//                inputStream = openFileInput("b");
//                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
//                b = (Trie) objectInputStream.readObject();
//                objectInputStream.close();
//            } catch (Exception e) {
//                e.printStackTrace();}
//        }
//        System.out.println("finish this b tri");
//        if(inputStream == null) {
//            try {
//                inputStream = openFileInput("d");
//                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
//                d = (Trie) objectInputStream.readObject();
//                objectInputStream.close();
//            } catch (Exception e) {
//                e.printStackTrace();}
//        }
//        if(inputStream != null) {
//            try {
//                inputStream = openFileInput("z");
//                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
//                z = (Trie) objectInputStream.readObject();
//                objectInputStream.close();
//            } catch (Exception e) {
//                e.printStackTrace();}
//        }
//        System.out.println("finish this z trie");

        //a way of displaying a list of words.
        listofword = (ListView) findViewById(R.id.listofw);

//        String[] values = new String[] { "android", "aah", "aah", "aam", "abc", "app"};
        final ArrayList<String> list = new ArrayList<>();
//        for (int i = 0; i < values.length; ++i) {
//            if(n.isWord(values[i])) {
//                list.add(values[i]);
//
//            }
//        }

        //a way of entering text
        enterText = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        //final ArrayList<String> source = new ArrayList<>();
        final MediaPlayer clickSound = MediaPlayer.create(this, R.raw.beep);

        enterText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("1rd");

                //hui che jian !!
                if(s.length()>0  ){//&& a.isWord(s.toString()) ){//|| b.isWord(s.toString()) ||
                                    //d.isWord(s.toString()) || z.isWord(s.toString())  )) {
                    System.out.println("2rd" + s.toString() + "\n");
                    if(//!s.toString().contains("\n") &&
                            isAlpha(s.toString())
                            && (newHash.containsKey(s.toString()) )){// || b.isWord(s.toString()) || q.isWord(s.toString()) || z.isWord(s.toString())) ){
                    System.out.println(s.toString());

                    //source.clear();

                    //source.add(s.toString());  }  }
                //System.out.println("3rd");

                //if(!source.isEmpty()) {
                    //ArrayAdapter<String> enterTextAdapter = new ArrayAdapter<>(DictionaryActivity.this,
                      //      android.R.layout.simple_dropdown_item_1line, source);
                    //enterText.setAdapter(enterTextAdapter);
                    //System.out.println("sdasd");
                    //enterText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        //@Override
                        //public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //String item = source.get(position);
                            //System.out.println(item);

                        if(!list.contains(s.toString())) {
                            list.add(s.toString());
                            clickSound.start();
                        }
                          //  System.out.println("4rd");
//                            //source.remove(item);
//                            adapter = new MyAdapter(getActivity(),LocationList);
//                            list.setAdapter(adapter);

                        }
                    //});
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //source.clear();


            }
        });


        //add CLEAR button
        clearButton = (Button) findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterText.setText("");
                list.clear();


            }
        });


        //listview of list of words
        ArrayAdapter adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                list);
        listofword.setAdapter(adapter);








        //add RETURN button
        returnButton = (Button) findViewById(R.id.returnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public boolean isAlpha(String name) {
        return name.matches("[a-z]+");
    }

}


//        BufferedReader input = null;
//        File file = null;
//        try {
//            file = new File(getCacheDir(), "MyCache"); // Pass getFilesDir() and "MyFile" to read file
//
//            input = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
//            String line;
//            StringBuffer buffer = new StringBuffer();
//            while ((line = input.readLine()) != null) {
//                buffer.append(line);
//            }
//            Gson gson= new Gson();
//            String low = buffer.toString();
//            n = gson.fromJson(low,Trie.class);
//
//           // Log.d(TAG, buffer.toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

