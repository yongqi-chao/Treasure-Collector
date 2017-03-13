package edu.neu.madcourse.yongqichao.leaderboard;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.yongqichao.R;

public class scoreBoard extends AppCompatActivity {

    public static final String SCORE_BOARD = "score_board";
    private ListView scoreBoard;
    private ArrayList<String> scorelist = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);

        scoreBoard = (ListView) findViewById(R.id.scoreboard);

        restoreBoard();
        //Display a list of scores
        ArrayAdapter adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                scorelist);
        scoreBoard.setAdapter(adapter);
    }


    public void restoreBoard(){
        SharedPreferences score_board = getSharedPreferences(SCORE_BOARD, 0);
        String scoreboardData = score_board.getString(SCORE_BOARD, null);
        if (scoreboardData == null) {
            scorelist.add("NO NEW SCORE");
        }
        else {
            String[] fields = scoreboardData.split(",");
            for (int index = 1; index < fields.length; index++){
                scorelist.add(fields[index]);
                index++;
            }
        }
    }

}
