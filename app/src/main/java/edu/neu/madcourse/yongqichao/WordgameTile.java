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

    public enum Owner {
        X, O /* letter O */, NEITHER, BOTH
    }

    // These levels are defined in the drawable definitions
    private static final int LEVEL_X = 0;
    private static final int LEVEL_O = 1; // letter O
    private static final int LEVEL_BLANK = 2;
    private static final int LEVEL_AVAILABLE = 3;
    private static final int LEVEL_TIE = 3;

    private final WordgamePlayFragment mGame;
    private Owner mOwner = Owner.NEITHER;
    private View mView;
    private WordgameTile mSubTiles[];


    private Button smallTile;
    private boolean matched;
    private boolean selected;
    public boolean lastSelected;
    private boolean available;
    private char aChar;
    private ArrayList<WordgameTile> moveTrack = new ArrayList<>();
    public int moveTrackingNumber;

    public WordgameTile(WordgamePlayFragment game) {
        this.mGame = game;
    }

    public WordgameTile deepCopy() {
        WordgameTile tile = new WordgameTile(mGame);
        tile.setOwner(getOwner());
        if (getSubTiles() != null) {
            WordgameTile newTiles[] = new WordgameTile[9];
            WordgameTile oldTiles[] = getSubTiles();
            for (int child = 0; child < 9; child++) {
                newTiles[child] = oldTiles[child].deepCopy();
            }
            tile.setSubTiles(newTiles);
        }
        return tile;
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

    public Owner getOwner() {
        return mOwner;
    }

    public void setOwner(Owner owner) {
        this.mOwner = owner;
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

    public WordgameTile[] getSubTiles() {
        return mSubTiles;
    }

    public void setSubTiles(WordgameTile[] subTiles) {
        this.mSubTiles = subTiles;
    }

    public ArrayList<WordgameTile> getMoveTrack() {
        return moveTrack;
    }

    public void setMoveTrack(ArrayList<WordgameTile> moveTrack) {
        this.moveTrack = moveTrack;
    }




    public void updateDrawableState() {
        if (mView == null) return;
        int level = getLevel();
        if (mView.getBackground() != null) {
            mView.getBackground().setLevel(level);
        }
    }

    public void updateButtonState(){
        if (smallTile == null) return;
        if(smallTile.getBackground() == null) return;

        if (matched){
            smallTile.setBackgroundColor(Color.rgb(255,160,122));
        }
        else if (lastSelected){
            smallTile.setBackgroundColor(Color.rgb(255,69,0));
        }
        else if (selected) {
            smallTile.setBackgroundColor(Color.rgb(255,255,153));
//            Drawable drawable = ((ImageButton) mView).getDrawable();
//            drawable.setLevel(level);
        }
        else if (aChar != 0  && available){
            smallTile.setBackgroundColor(Color.rgb(46,139,87));
        }
        else{
            smallTile.setBackgroundColor(Color.GRAY);
        }

        smallTile.setText(String.valueOf(aChar));
    }


    private int getLevel() {
        int level = LEVEL_BLANK;
        switch (mOwner) {
            case X:
                level = LEVEL_X;
                break;
            case O: // letter O
                level = LEVEL_O;
                break;
            case BOTH:
                level = LEVEL_TIE;
                break;
            case NEITHER:
               // level = mGame.isAvailable(this) ? LEVEL_AVAILABLE : LEVEL_BLANK;
                level = available ? LEVEL_AVAILABLE : LEVEL_BLANK;
                break;
        }
        return level;
    }

    private void countCaptures(int totalX[], int totalO[]) {
        int capturedX, capturedO;
        // Check the horizontal
        for (int row = 0; row < 3; row++) {
            capturedX = capturedO = 0;
            for (int col = 0; col < 3; col++) {
                Owner owner = mSubTiles[3 * row + col].getOwner();
                if (owner == Owner.X || owner == Owner.BOTH) capturedX++;
                if (owner == Owner.O || owner == Owner.BOTH) capturedO++;
            }
            totalX[capturedX]++;
            totalO[capturedO]++;
        }

        // Check the vertical
        for (int col = 0; col < 3; col++) {
            capturedX = capturedO = 0;
            for (int row = 0; row < 3; row++) {
                Owner owner = mSubTiles[3 * row + col].getOwner();
                if (owner == Owner.X || owner == Owner.BOTH) capturedX++;
                if (owner == Owner.O || owner == Owner.BOTH) capturedO++;
            }
            totalX[capturedX]++;
            totalO[capturedO]++;
        }

        // Check the diagonals
        capturedX = capturedO = 0;
        for (int diag = 0; diag < 3; diag++) {
            Owner owner = mSubTiles[3 * diag + diag].getOwner();
            if (owner == Owner.X || owner == Owner.BOTH) capturedX++;
            if (owner == Owner.O || owner == Owner.BOTH) capturedO++;
        }
        totalX[capturedX]++;
        totalO[capturedO]++;
        capturedX = capturedO = 0;
        for (int diag = 0; diag < 3; diag++) {
            Owner owner = mSubTiles[3 * diag + (2 - diag)].getOwner();
            if (owner == Owner.X || owner == Owner.BOTH) capturedX++;
            if (owner == Owner.O || owner == Owner.BOTH) capturedO++;
        }
        totalX[capturedX]++;
        totalO[capturedO]++;
    }

    public Owner findWinner() {
        // If owner already calculated, return it
        if (getOwner() != Owner.NEITHER)
            return getOwner();

        int totalX[] = new int[4];
        int totalO[] = new int[4];
        countCaptures(totalX, totalO);
        if (totalX[3] > 0) return Owner.X;
        if (totalO[3] > 0) return Owner.O;

        // Check for a draw
        int total = 0;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                Owner owner = mSubTiles[3 * row + col].getOwner();
                if (owner != Owner.NEITHER) total++;
            }
            if (total == 9) return Owner.BOTH;
        }

        // Neither player has won this tile
        return Owner.NEITHER;
    }

    public int evaluate() {
        switch (getOwner()) {
            case X:
                return 100;
            case O:
                return -100;
            case NEITHER:
                int total = 0;
                if (getSubTiles() != null) {
                    for (int tile = 0; tile < 9; tile++) {
                        total += getSubTiles()[tile].evaluate();
                    }
                    int totalX[] = new int[4];
                    int totalO[] = new int[4];
                    countCaptures(totalX, totalO);
                    total = total * 100 + totalX[1] + 2 * totalX[2] + 8 *
                            totalX[3] - totalO[1] - 2 * totalO[2] - 8 * totalO[3];
                }
                return total;
        }
        return 0;
    }

//    public void animate() {
//        Animator anim = AnimatorInflater.loadAnimator(mGame.getActivity(),
//                R.animator.tictactoe);
//        if (getView() != null) {
//            anim.setTarget(getView());
//            anim.start();
//        }
//    }
}
