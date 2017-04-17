package edu.neu.madcourse.yongqichao.mapgame;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class User {

    public String username;
    public String password;
    public Integer level;
    public String score;
    public List<LatLng> myPoints;
    public List<LatLng> otherPoints;



    public User(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String password, Integer level){
        this.username = username;
        this.password = password;
        this.level = level;
    }


    public User(String username, String password, Integer level, String score, List<LatLng> myPoints, List<LatLng> otherPoints){
        this.username = username;
        this.password = password;
        this.level = level;
        this.score = score;
        this.myPoints = myPoints;
        this.otherPoints = otherPoints;
    }

}