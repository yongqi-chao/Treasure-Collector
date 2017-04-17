package edu.neu.madcourse.yongqichao.mapgame;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import edu.neu.madcourse.yongqichao.R;

public class MapGameLoginView extends AppCompatActivity {
    EditText usernameText;
    EditText passwordText;
    Button loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_game_login_view);
        setTitle("LOG IN");

        //beep sound when match a user and password
        final MediaPlayer clickSound = MediaPlayer.create(this, R.raw.beep);


        //a way of entering username
        usernameText = (EditText) findViewById(R.id.usernameText);
        usernameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameText.setText("");
            }
        });
        usernameText.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        passwordText = (EditText) findViewById(R.id.passwordText);

        loginButton = (Button) findViewById(R.id.loginViewlogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(MapGameLoginView.this, MapGameMapView.class);
                startActivity(login);
                clickSound.start();
            }
        });


    }
}
