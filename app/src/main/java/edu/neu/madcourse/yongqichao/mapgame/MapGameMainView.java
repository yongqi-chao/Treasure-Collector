package edu.neu.madcourse.yongqichao.mapgame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.neu.madcourse.yongqichao.AboutActivity;
import edu.neu.madcourse.yongqichao.MainActivity;
import edu.neu.madcourse.yongqichao.R;

public class MapGameMainView extends AppCompatActivity {
    Button loginButton, gameInstructionButton;
    private AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_game_main_view);

        setTitle("Running Game");

        //add Log In button.
        loginButton = (Button)findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(MapGameMainView.this, MapGameLoginView.class);
                startActivity(login);
            }
        });

        //add game instruction button.
        gameInstructionButton = (Button)findViewById(R.id.mapGameInstruction);
        gameInstructionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MapGameMainView.this);
                builder.setMessage(R.string.hintWordGame);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.ok_label_wordgame,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // nothing
                            }
                        });
                mDialog = builder.show();
            }
        });

    }
}
