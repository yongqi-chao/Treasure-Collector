package edu.neu.madcourse.yongqichao.leaderboard;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import edu.neu.madcourse.yongqichao.R;

public class leaderBoard extends AppCompatActivity {

    private DatabaseReference mDatabase;

    private ListView leaderboard;
    private ArrayList<String> leaderlist = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        leaderboard = (ListView) findViewById(R.id.leaderboard);

        mDatabase = FirebaseDatabase.getInstance().getReference();


        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(int i = 1; i<11;i++) {
                    String number = "Number" + i;
                    WordgameRecord records = dataSnapshot.child(number).getValue(WordgameRecord.class);
                    String inputRecord = "UserName: " + records.username +
                            "\nDate: " + records.date +
                            "\nWord With Score : " + records.wordWithHighestScore +
                            "\nFinalScore: " + records.finalScore;
                    leaderlist.add(inputRecord);
                }
                //Display a list of scores
                ArrayAdapter adapter = new ArrayAdapter<>(getBaseContext(),
                        android.R.layout.simple_list_item_1,
                        leaderlist);
                leaderboard.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        };
        mDatabase.child("wordGameRecords").addListenerForSingleValueEvent(postListener);
    }





}
