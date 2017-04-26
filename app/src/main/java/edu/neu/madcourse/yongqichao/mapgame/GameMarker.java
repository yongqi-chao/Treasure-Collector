package edu.neu.madcourse.yongqichao.mapgame;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;


@IgnoreExtraProperties
public class GameMarker {

    public Integer markerId;
    public String username;
    public double longitude;
    public double latitude;



    public GameMarker(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public GameMarker(String username, double longitude, double latitude){
        this.username = username;
        this.latitude = 0;
        this.longitude = 0;
    }


}