package edu.neu.madcourse.yongqichao;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class WordgameTile {

//    private final WordgamePlayFragment mGame;
    private View mView;

    //for small tiles only
    private Button smallTile;
    private boolean matched;
    private boolean selected;
    public boolean lastSelected;
    private boolean available;
    private char aChar;
    public int positionNumber;

    //for large tiles only
    private ArrayList<WordgameTile> moveTrack = new ArrayList<>();
    public boolean finished;

    //for entire tile only
    public ArrayList<WordgameTile> moveTrackPhase2 = new ArrayList<>();
    public int largePositionNumber;


    public WordgameTile() {
//        this.mGame = game;
    }

    public View getView() {
        return mView;
    }

    public void setView(View view) {
        this.mView = view;
    }

    public void setButton(Button button){
        this.smallTile = button;
    }

    public void setSelected(boolean select){
        if(matched) selected = false;
        else selected = select;
    }

    public boolean getSelected(){
        if(matched) return false;
        return  selected;
    }

    public void setCharacter(char Char){
        aChar = Char;
    }

    public char getCharacter(){
       return aChar;
    }

    public boolean getAvailable(){
        if(matched) return false;
        return  available;
    }

    public void setAvailable(boolean availability){
        if(matched) available = false;
        else available = availability;
    }

    public boolean getMatched(){
        return  matched;
    }

    public void setMatched(boolean match){
        matched = match;
    }

    public ArrayList<WordgameTile> getMoveTrack() {
        return moveTrack;
    }




    public void updateButtonState(){
        if (smallTile == null) return;
        if(smallTile.getBackground() == null) return;

        //pink means it got a match
        if (matched){
            smallTile.setBackgroundColor(Color.rgb(255,160,122));
        }//if not matched, set to red only if it is last selected
        else if (lastSelected){
            smallTile.setBackgroundColor(Color.rgb(255,69,0));
        }//if not matched not last selected, set to yellow if it is selected
        else if (selected) {
            smallTile.setBackgroundColor(Color.rgb(255,255,153));
        }//if not matched not selected, set it to green if it is available
        else if (aChar != 0  && available){
            smallTile.setBackgroundColor(Color.rgb(46,139,87));
        }//if none of them, set to grey
        else{
            smallTile.setBackgroundColor(Color.GRAY);
        }

        smallTile.setText(String.valueOf(aChar));
    }


    public boolean gotMatch(ArrayList<String> dictionary, ArrayList<WordgameTile> moveTrack){
        if (dictionary.contains(trackToString(moveTrack))) return true;
        return false;
    }

    public String trackToString(ArrayList<WordgameTile> moveTrack){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0;i < moveTrack.size() ;i++){
            stringBuilder.append(moveTrack.get(i).getCharacter());
        }
        return stringBuilder.toString();
    }

}
