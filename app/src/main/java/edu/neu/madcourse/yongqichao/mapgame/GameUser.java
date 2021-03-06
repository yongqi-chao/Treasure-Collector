package edu.neu.madcourse.yongqichao.mapgame;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class GameUser {

    public String username;
    public Integer level;
    public Integer score;



    public GameUser(){
        this.level = 1;
        this.score = 0;
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public GameUser(String username, Integer level, Integer score){
        this.username = username;
        this.level = level;
        this.score = score;

    }

}