package edu.neu.madcourse.yongqichao.leaderboard;

import com.google.firebase.database.IgnoreExtraProperties;

import com.google.firebase.database.IgnoreExtraProperties;
/**
 * Created by Jay on 3/12/17.
 */

@IgnoreExtraProperties
public class WordgameRecord {

    public String rank;
    public String username;
    public String date;
    public String wordWithHighestScore;
    public String finalScore;


    public WordgameRecord(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public WordgameRecord(String rank, String date, String word, String score){
        this.rank = rank;
        this.username = "Anonymous";
        this.date = date;
        this.wordWithHighestScore = word;
        this.finalScore = score;
    }

    public WordgameRecord(String rank, String username, String date , String word, String score){
        this.rank = rank;
        this.username = username;
        this.date = date;
        this.wordWithHighestScore = word;
        this.finalScore = score;
    }
}
