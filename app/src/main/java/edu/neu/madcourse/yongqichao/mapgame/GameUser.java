package edu.neu.madcourse.yongqichao.mapgame;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class GameUser {

    public String username;
    public Integer level;
    public Integer score;
    public List<String> myPoints;
    public List<String> otherPoints;



    public GameUser(){
        this.level = 0;
        this.score = 0;
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public GameUser(String username, String password, Integer level){
        this.username = username;
        //this.password = password;
        this.level = level;
    }


    public GameUser(String username, String password, Integer level, Integer score, List<String> myPoints, List<String> otherPoints){
        this.username = username;
        //this.password = password;
        this.level = level;
        this.score = score;
        this.myPoints = myPoints;
        this.otherPoints = otherPoints;
    }

}