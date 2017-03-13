package edu.neu.madcourse.yongqichao;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

public class WordgameChooseUsername extends AppCompatActivity {

    Button skipButton;
    AutoCompleteTextView enterUserName;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordgame_choose_username);


        skipButton = (Button) findViewById(R.id.skipButton);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WordgameChooseUsername.this, WordgamePlay.class);
                intent.putExtra(WordgamePlay.KEY_RESTORE, "");
                finish();
                startActivity(intent);
            }
        });


        //a way of entering text
        enterUserName = (AutoCompleteTextView) findViewById(R.id.enterUsernameTextView);
        //implementation of entering text and adding to the list
        enterUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                username = s.toString();
            }
        });


        enterUserName.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Intent intent = new Intent(WordgameChooseUsername.this, WordgamePlay.class);
                intent.putExtra(WordgamePlay.USER_NAME_RESTORE, username);
                finish();
                startActivity(intent);
                System.out.println("!you pressed enter key !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

//                if (event != null&& (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
//                    System.out.println("you pressed enter key !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//
//                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    // in.hideSoftInputFromWindow(autoEditText.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                    //Commented line is for hide keyboard. Just make above code as comment and test your requirement
//                    //It will work for your need. I just putted that line for your understanding only
//                    //You can use own requirement here also.
//                }
                return false;
            }
        });
    }
}
