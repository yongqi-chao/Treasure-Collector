package edu.neu.madcourse.yongqichao.leaderboard;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import edu.neu.madcourse.yongqichao.R;

public class leaderBoard extends AppCompatActivity {

    private static final String TAG = edu.neu.madcourse.yongqichao.firebase.RealtimeDatabaseActivity.class.getSimpleName();

    private DatabaseReference mDatabase;
    private TextView userName;
    private TextView score;
    private TextView userName2;
    private TextView score2;
    private RadioButton player1;
    private Button add5;

    public static final String LEADER_BOARD = "learder_board";
    private ListView leaderboard;
    private ArrayList<String> leaderlist = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        leaderboard = (ListView) findViewById(R.id.leaderboard);

        //restoreBoard();


//
//        userName = (TextView) findViewById(R.id.username);
//        score = (TextView) findViewById(R.id.score);
//        userName2 = (TextView) findViewById(R.id.username2);
//        score2 = (TextView) findViewById(R.id.score2);
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
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.child("wordGameRecords").addListenerForSingleValueEvent(postListener);

//        WordgameRecord record3 = new WordgameRecord("Number3","2017", "newword", "0");
//        mDatabase.child("wordGameRecords").child(record3.rank).setValue(record3);

        mDatabase.child("wordGameRecords").addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

//                        if (dataSnapshot.getKey().equalsIgnoreCase("user1")) {
//                            score.setText(user.score);
//                            userName.setText(user.username);
//                        } else {
//                            score2.setText(String.valueOf(user.score));
//                            userName2.setText(user.username);
//                        }
                       // Log.e(TAG, "onChildAdded: dataSnapshot = " + dataSnapshot.getValue());
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                        User user = dataSnapshot.getValue(User.class);
//
//                        if (dataSnapshot.getKey().equalsIgnoreCase("user1")) {
//                            score.setText(user.score);
//                            userName.setText(user.username);
//                        } else {
//                            score2.setText(String.valueOf(user.score));
//                            userName2.setText(user.username);
//                        }
//                        Log.v(TAG, "onChildChanged: "+dataSnapshot.getValue().toString());
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                    }
                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "onCancelled:" + databaseError);
                    }
                }
        );




        Log.e(TAG, "onChildAdded: dataSnapshot = " + mDatabase.child("wordGameRecords").getKey());

//        WordgameRecord record1 = new WordgameRecord("Number1","2017", "newword", "0");
//        mDatabase.child("wordGameRecords").child(record1.rank).setValue(record1);
//        WordgameRecord record2 = new WordgameRecord("Number2","2017", "newword", "0");
//        mDatabase.child("wordGameRecords").child(record2.rank).setValue(record2);
//        record1 = new WordgameRecord("Number3","2017", "newword", "0");
//        mDatabase.child("wordGameRecords").child(record1.rank).setValue(record1);
//        record2 = new WordgameRecord("Number4","2017", "newword", "0");
//        mDatabase.child("wordGameRecords").child(record2.rank).setValue(record2);
//        record1 = new WordgameRecord("Number5","2017", "newword", "0");
//        mDatabase.child("wordGameRecords").child(record1.rank).setValue(record1);
//        record2 = new WordgameRecord("Number6","2017", "newword", "0");
//        mDatabase.child("wordGameRecords").child(record2.rank).setValue(record2);
//        record1 = new WordgameRecord("Number7","2017", "newword", "0");
//        mDatabase.child("wordGameRecords").child(record1.rank).setValue(record1);
//        record2 = new WordgameRecord("Number8","2017", "newword", "0");
//        mDatabase.child("wordGameRecords").child(record2.rank).setValue(record2);
//        record1 = new WordgameRecord("Number9","2017", "newword", "0");
//        mDatabase.child("wordGameRecords").child(record1.rank).setValue(record1);
//        record2 = new WordgameRecord("Number10","2017", "newword", "0");
//        mDatabase.child("wordGameRecords").child(record2.rank).setValue(record2);
    }

//    /**
//     * Called on score add
//     * @param postRef
//     * @param user
//     */
//    private void onAddScore(DatabaseReference postRef, String user) {
//        postRef
//                .child("users")
//                .child(user)
//                .runTransaction(new Transaction.Handler() {
//                    @Override
//                    public Transaction.Result doTransaction(MutableData mutableData) {
//                        User u = mutableData.getValue(User.class);
//                        if (u == null) {
//                            return Transaction.success(mutableData);
//                        }
//
//                        u.score = String.valueOf(Integer.valueOf(u.score) + 5);
//
//                        mutableData.setValue(u);
//                        return Transaction.success(mutableData);
//                    }
//
//                    @Override
//                    public void onComplete(DatabaseError databaseError, boolean b,
//                                           DataSnapshot dataSnapshot) {
//                        // Transaction completed
//                        Log.d(TAG, "postTransaction:onComplete:" + databaseError);
//                    }
//                });
//    }

//    public void addUsers(View view){
//
//        WordgameRecord record = new WordgameRecord("user1", "0");
//        mDatabase.child("wordGameRecords").child(record.username).setValue(record);
//
//    }

    public void doDumbDataAdd(View view){
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                TextView tv = (TextView)findViewById(R.id.dataUpdateTextView);
                tv.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }


    public void restoreBoard(){
        SharedPreferences score_board = getSharedPreferences(LEADER_BOARD, 0);
        String scoreboardData = score_board.getString(LEADER_BOARD, null);
        if (scoreboardData == null) {
            leaderlist.add("NO NEW SCORE");
        }
        else {
            String[] fields = scoreboardData.split(",");
            for (int index = 1; index < fields.length; index++){
                leaderlist.add(fields[index]);
                index++;
            }
        }
    }

}
