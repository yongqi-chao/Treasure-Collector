package edu.neu.madcourse.yongqichao;

import android.app.Fragment;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class WordgamePlayFragment extends Fragment {
    static private int mLargeIds[] = {R.id.large1, R.id.large2, R.id.large3,
            R.id.large4, R.id.large5, R.id.large6, R.id.large7, R.id.large8,
            R.id.large9,};
    static private int mSmallIds[] = {R.id.small1, R.id.small2, R.id.small3,
            R.id.small4, R.id.small5, R.id.small6, R.id.small7, R.id.small8,
            R.id.small9,};
    private Handler mHandler = new Handler();
    private WordgameTile mEntireBoard = new WordgameTile(this);
    private WordgameTile mLargeTiles[] = new WordgameTile[9];
    private WordgameTile mSmallTiles[][] = new WordgameTile[9][9];
    //    private WordgameTile.Owner mPlayer = WordgameTile.Owner.X;
    private Set<WordgameTile> availableTiles = new HashSet<WordgameTile>();
    //    private int mSoundX, mSoundO, mSoundMiss, mSoundRewind;
//    private SoundPool mSoundPool;
//    private float mVolume = 1f;
    private int lastSelectedLarge;
    private int lastSelectedSmall;

    private ArrayList<String> nineCharWordDictionary = new ArrayList<>();
    private ArrayList<String> dictionary = new ArrayList<>();
    //a lsit to remember random number
    private HashSet<Integer> rememberRandomNumber = new HashSet<>();
    private boolean finishLoadingNineCharDictionary = false;
    private boolean finishLoadingDictionary = false;
    private boolean haveContinueGameData = false;
    private  ArrayList<int[]> tileAvailableList =new ArrayList<>();
    //activity_wordgame_play_fragment
    public WordgamePlay.Phase phase= WordgamePlay.Phase.Phase1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retain this fragment across configuration changes.
        setRetainInstance(true);
        initGame();
//        mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
//        mSoundX = mSoundPool.load(getActivity(), R.raw.sergenious_movex, 1);
//        mSoundO = mSoundPool.load(getActivity(), R.raw.sergenious_moveo, 1);
//        mSoundMiss = mSoundPool.load(getActivity(), R.raw.erkanozan_miss, 1);
//        mSoundRewind = mSoundPool.load(getActivity(), R.raw.joanne_rewind, 1);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =
                inflater.inflate(R.layout.large_board, container, false);
        initViews(rootView);
        updateAllTiles();
        return rootView;
    }

    private void initViews(View rootView) {
        mEntireBoard.setView(rootView);
        for (int large = 0; large < 9; large++) {
            View outer = rootView.findViewById(mLargeIds[large]);
            mLargeTiles[large].setView(outer);

            for (int small = 0; small < 9; small++) {
                Button inner = (Button) outer.findViewById
                        (mSmallIds[small]);
                final int fLarge = large;
                final int fSmall = small;
                final WordgameTile smallTile = mSmallTiles[large][small];
//                final MediaPlayer clickSound = MediaPlayer.create(this.getActivity(), R.raw.beep);
                smallTile.setButton(inner);
                // ...
                inner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        smallTile.animate();
                        // ...
                        if (smallTile.getAvailable() && (finishLoadingNineCharDictionary | haveContinueGameData)
                                && finishLoadingDictionary) {
                            if(phase == WordgamePlay.Phase.Phase1){
                                makeMove(fLarge, fSmall);
                            }else if(phase == WordgamePlay.Phase.Phase2){
                                makeMovePhase2(fLarge,fSmall);
                            }
                            //((WordgamePlay)getActivity()).startThinking();
//                            mSoundPool.play(mSoundX, mVolume, mVolume, 1, 0, 1f);
                            //think();
                            // clickSound.start();
                        } else {
//                            mSoundPool.play(mSoundMiss, mVolume, mVolume, 1, 0, 1f);
                        }
                    }
                });
            }
        }
    }

    public void initPhase2(){
        phase = WordgamePlay.Phase.Phase2;
        clearAllMatchedPhase2();
        setAllAvailablePhase2();
        updateAllTiles();
    }

    public void resumePhase2(){
        phase = WordgamePlay.Phase.Phase2;
        setAllAvailablePhase2();
        updateAllTiles();
    }

    public void makeMovePhase2(int large, int small){
        cancleLastSelect();
        WordgameTile smallTile = mSmallTiles[large][small];
        WordgameTile largeTile = mLargeTiles[large];
        switchSelect(smallTile);
        if(lastSelectedSmall == small && lastSelectedLarge == large ){
            ArrayList<WordgameTile> moveTrack = mEntireBoard.moveTrackPhase2;
            moveTrack.remove(moveTrack.size()-1);
            //if there is no last move
            if(moveTrack.isEmpty()) {
                lastSelectedLarge = -1;
                lastSelectedSmall = -1;
            }else{
                lastSelectedLarge = moveTrack.get(moveTrack.size()-1).largePositionNumber;
                lastSelectedSmall = moveTrack.get(moveTrack.size()-1).positionNumber;
            }
        }else {
            lastSelectedLarge = large;
            lastSelectedSmall = small;
            mEntireBoard.moveTrackPhase2.add(smallTile);
        }
        setAvailableFromLastMovePhase2(lastSelectedLarge,lastSelectedSmall);
        updateAllTiles();

        //find match word and report match word
        if (largeTile.gotMatch(dictionary,mEntireBoard.moveTrackPhase2)){
            String matchedWord = largeTile.trackToString(mEntireBoard.moveTrackPhase2);
            ((WordgamePlay)getActivity()).reportMatchedWordPhase2(matchedWord);
        }else {
            ((WordgamePlay)getActivity()).reportMatchedWordPhase2(null);
        }
    }


    private void think() {

//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (getActivity() == null) return;
//                if (mEntireBoard.getOwner() == WordgameTile.Owner.NEITHER) {
//                    int move[] = new int[2];
//                    pickMove(move);
//                    if (move[0] != -1 && move[1] != -1) {
//                        switchTurns();
////                        mSoundPool.play(mSoundO, mVolume, mVolume,
////                                1, 0, 1f);
//                        makeMove(move[0], move[1]);
//                        switchTurns();
//                    }
//                }
////                ((WordgamePlay) getActivity()).stopThinking();
//            }
//        }, 1000);
    }

//    private void pickMove(int move[]) {
//        WordgameTile.Owner opponent = mPlayer == WordgameTile.Owner.X ? WordgameTile.Owner.O : WordgameTile
//                .Owner.X;
//        int bestLarge = -1;
//        int bestSmall = -1;
//        int bestValue = Integer.MAX_VALUE;
//        for (int large = 0; large < 9; large++) {
//            for (int small = 0; small < 9; small++) {
//                WordgameTile smallTile = mSmallTiles[large][small];
//                if (isAvailable(smallTile)) {
//                    // Try the move and get the score
//                    WordgameTile newBoard = mEntireBoard.deepCopy();
//                    newBoard.getSubTiles()[large].getSubTiles()[small]
//                            .setOwner(opponent);
//                    int value = newBoard.evaluate();
//                    Log.d("UT3",
//                            "Moving to " + large + ", " + small + " gives value " +
//                                    "" + value
//                    );
//                    if (value < bestValue) {
//                        bestLarge = large;
//                        bestSmall = small;
//                        bestValue = value;
//                    }
//                }
//            }
//        }
//        move[0] = bestLarge;
//        move[1] = bestSmall;
//        Log.d("UT3", "Best move is " + bestLarge + ", " + bestSmall);
//    }

//    private void switchTurns() {
//        mPlayer = mPlayer == WordgameTile.Owner.X ? WordgameTile.Owner.O : WordgameTile
//                .Owner.X;
//    }




    private void switchSelect(WordgameTile smallTile) {
        boolean tileSelect = smallTile.getSelected() == true ? false: true;
        smallTile.setSelected(tileSelect);
    }

    private void cancleLastSelect() {
        if (lastSelectedSmall != -1) {
            WordgameTile smallTile = mSmallTiles[lastSelectedLarge][lastSelectedSmall];
            smallTile.lastSelected = false;
        }
    }



    private void makeMove(int large, int small) {
        cancleLastSelect();
        WordgameTile smallTile = mSmallTiles[large][small];
        WordgameTile largeTile = mLargeTiles[large];
        switchSelect(smallTile);
        if(lastSelectedSmall == small){
            ArrayList<WordgameTile> moveTrack = largeTile.getMoveTrack();
            moveTrack.remove(moveTrack.size()-1);
            //if there is no last move
            if(moveTrack.isEmpty()) {
                lastSelectedLarge = -1;
                lastSelectedSmall = -1;
            }else{
                lastSelectedLarge = large;
                lastSelectedSmall = moveTrack.get(moveTrack.size()-1).positionNumber;
            }
        }else {
            lastSelectedLarge = large;
            lastSelectedSmall = small;
            largeTile.getMoveTrack().add(smallTile);
        }
        setAvailableFromLastMove(lastSelectedLarge,lastSelectedSmall);
        updateAllTiles();

        //find match word and report match word
        if (largeTile.gotMatch(dictionary,largeTile.getMoveTrack())){
            String matchedWord = largeTile.trackToString(largeTile.getMoveTrack());
            ((WordgamePlay)getActivity()).reportMatchedWord(matchedWord, large);
        }else {
            ((WordgamePlay)getActivity()).reportMatchedWord(null, large);
        }


//        WordgameTile.Owner oldWinner = largeTile.getOwner();
//        WordgameTile.Owner winner = largeTile.findWinner();
//        if (winner != oldWinner) {
////            largeTile.animate();
//            largeTile.setOwner(winner);
//        }
//        winner = mEntireBoard.findWinner();
//        mEntireBoard.setOwner(winner);
//        updateAllTiles();
//        if (winner != WordgameTile.Owner.NEITHER) {
//            ((WordgamePlay)getActivity()).reportWinner(winner);
//        }
    }

    public void restartGame() {
        haveContinueGameData = false;
        phase = WordgamePlay.Phase.Phase1;
        rememberRandomNumber.clear();
//        mSoundPool.play(mSoundRewind, mVolume, mVolume, 1, 0, 1f);
        initGame();
        initViews(getView());

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        while (finishLoadingNineCharDictionary == false) {
                            //waiting
                        }
                        initAllSmallTiles(nineCharWordDictionary);
                    }
                }).start();
    }

    public void initGame() {
        Log.d("Word Game", "init game");
        mEntireBoard = new WordgameTile(this);
        // Create all the tiles
        for (int large = 0; large < 9; large++) {
            mLargeTiles[large] = new WordgameTile(this);
            for (int small = 0; small < 9; small++) {
                mSmallTiles[large][small] = new WordgameTile(this);
                mSmallTiles[large][small].positionNumber = small;
                mSmallTiles[large][small].largePositionNumber = large;
            }
            mLargeTiles[large].setSubTiles(mSmallTiles[large]);
//            mLargeTiles[large].moveTrackingNumber = large;
        }
        mEntireBoard.setSubTiles(mLargeTiles);

        // Set all tiles before game start
        lastSelectedLarge = -1;
        lastSelectedSmall = -1;
        initSmallTileAvailability();
        setAvailableFromLastMove(lastSelectedLarge,lastSelectedSmall);
    }

    //finish entire board
    public void finishPhase2() {
        if (mEntireBoard.gotMatch(dictionary,mEntireBoard.moveTrackPhase2)) {
            for (int large = 0; large < 9; large++) {
                for (int small = 0; small < 9; small++) {
                    WordgameTile smallTile = mSmallTiles[large][small];
                    if (smallTile.getSelected()) {
                        smallTile.setMatched(true);
                        smallTile.setAvailable(false);
                    } else {
                        smallTile.setCharacter(' ');
                        smallTile.setAvailable(false);
                    }
                }
            }
        }else {
            for (int large = 0; large < 9; large++) {
                for (int small = 0; small < 9; small++) {
                    WordgameTile smallTile = mSmallTiles[large][small];
                    smallTile.setAvailable(false);
                    smallTile.setCharacter(' ');
                }
            }
        }
        //update visual effect to users
        lastSelectedLarge = -1;
        lastSelectedSmall = -1;
        updateAllTiles();
    }

    //finish large tile one by one
    public void finishGame() {
        for (int large = 0; large < 9; large++) {
            WordgameTile largeTile = mLargeTiles[large];
            if (!largeTile.finished && largeTile.gotMatch(dictionary,largeTile.getMoveTrack())) {
                finishATile(large);
            }
            else if(!largeTile.finished) {
                for (int small = 0; small < 9; small++) {
                    WordgameTile smallTile = mSmallTiles[large][small];
                    smallTile.lastSelected = false;
                    smallTile.setSelected(false);
                    smallTile.setAvailable(false);
                    smallTile.setCharacter(' ');
                }
                //finish a large tile ; lock it to avoid changes on it
                largeTile.finished = true;
            }
        }
        //update visual effect to users
        lastSelectedLarge = -1;
        lastSelectedSmall = -1;
        updateAllTiles();
    }


    private void updateAllTiles() {
        // mEntireBoard.updateDrawableState();
        for (int large = 0; large < 9; large++) {
            //mLargeTiles[large].updateDrawableState();
            for (int small = 0; small < 9; small++) {
                mSmallTiles[large][small].updateButtonState();
            }
        }
    }

    //THE FOLLOWING FUNCTION IS FOR FINISHING A TILE AFTER PRESSED SELECT IT BUTTON
    //AND THE LARGE TILE HAVE A MATCHED WORD
    //-------------------------------------------------------------------------------------------

    public void finishATile(int largeTileNumber){
        for (int small = 0; small < 9; small++) {
            WordgameTile smallTile = mSmallTiles[largeTileNumber][small];
            if(smallTile.getSelected()){
                smallTile.setMatched(true);
                smallTile.setAvailable(false);
            }else {
                smallTile.setCharacter(' ');
                smallTile.setAvailable(false);
            }
        }
        //finish a large tile ; lock it to avoid changes on it
        WordgameTile largeTile = mLargeTiles[largeTileNumber];
        largeTile.finished = true;
        lastSelectedLarge = -1;
        lastSelectedSmall = -1;
        setAvailableFromLastMove(lastSelectedLarge, lastSelectedSmall);
        updateAllTiles();
    }

    //THE FOLLOWING PART IT GETTING AND SETTING GAME DATA-----------------------------------------

    /** Create a string containing the state of the game. */
    public String getState() {
        if(haveContinueGameData) {
            StringBuilder builder = new StringBuilder();
            builder.append(lastSelectedLarge);
            builder.append(',');
            builder.append(lastSelectedSmall);
            builder.append(',');
            for (int large = 0; large < 9; large++) {
                //store finish state for large tiles
                builder.append(mLargeTiles[large].finished);
                builder.append(',');
                //store 3 main states for small tiles
                for (int small = 0; small < 9; small++) {
                    builder.append(mSmallTiles[large][small].getMatched());
                    builder.append(',');
                    builder.append(mSmallTiles[large][small].getSelected());
                    builder.append(',');
                    builder.append(mSmallTiles[large][small].getAvailable());
                    builder.append(',');
                }
            }
            return builder.toString();
        }
        else return null;
    }

    /** Create a string containing the words of the game. */
    public String getWords(){
        if(haveContinueGameData) {
            StringBuilder builder = new StringBuilder();
            for (int large = 0; large < 9; large++) {
                for (int small = 0; small < 9; small++) {
                    builder.append(mSmallTiles[large][small].getCharacter());
                    builder.append(',');
                }
            }
            return builder.toString();
        }
        else return null;
    }

    /** Create a string containing the words of the game. */
    public String getMoveTrack(){
        if(haveContinueGameData) {
            StringBuilder builder = new StringBuilder();
            for (int large = 0; large < 9; large++) {
                ArrayList<WordgameTile> moveTrack = mLargeTiles[large].getMoveTrack();
                for (int small = 0; small < moveTrack.size(); small++) {
                    builder.append(moveTrack.get(small).positionNumber);
                    builder.append(',');
                }
                builder.append(';');
            }
            ArrayList<WordgameTile> moveTrack = mEntireBoard.moveTrackPhase2;
            for (int pos = 0; pos < moveTrack.size(); pos++){
                builder.append(moveTrack.get(pos).largePositionNumber);
                builder.append(',');
                builder.append(moveTrack.get(pos).positionNumber);
                builder.append(',');
            }
            builder.append(';');
            return builder.toString();
        }
        else return null;
    }

    /** Restore the state of the game from the given string. */
    public void putState(String gameData) {
        String[] fields = gameData.split(",");
        int index = 0;
        lastSelectedLarge = Integer.parseInt(fields[index++]);
        lastSelectedSmall = Integer.parseInt(fields[index++]);
        for (int large = 0; large < 9; large++) {
            //reteive finish state for large tiles
            mLargeTiles[large].finished = Boolean.parseBoolean((fields[index++]));
            //retreive 3 main state for small tiles
            for (int small = 0; small < 9; small++) {
                mSmallTiles[large][small].setMatched(Boolean.parseBoolean((fields[index++])));
                mSmallTiles[large][small].setSelected(Boolean.parseBoolean((fields[index++])));
                mSmallTiles[large][small].setAvailable(Boolean.parseBoolean((fields[index++])));
            }
        }
        setAvailableFromLastMove(lastSelectedLarge,lastSelectedSmall);
        updateAllTiles();
    }

    /** Restore the words of the game from the given string. */
    public void putWords(String gameData) {
        String[] fields = gameData.split(",");
        int index = 0;
        for (int large = 0; large < 9; large++) {
            for (int small = 0; small < 9; small++) {
                mSmallTiles[large][small].setCharacter(fields[index++].toString().charAt(0));
            }
        }
        updateAllTiles();
    }

    public void putMoveTrack(String gameData) {
        String[] fields = gameData.split(";");
        System.out.println("put move track error" + fields.length);
        for (int large = 0; large < 9 && large < fields.length; large++) {
            if(fields[large].length() > 0) {
                String[] moveTrack = fields[large].split(",");
                WordgameTile largeTile = mLargeTiles[large];
                for (int small = 0; small < moveTrack.length; small++) {
                    WordgameTile smallTile = mSmallTiles[large][Integer.parseInt(moveTrack[small])];
                    largeTile.getMoveTrack().add(smallTile);
                }
            }
        }
        System.out.println("put move track error");
        System.out.println("put move track error" + fields.length);
        if(fields.length == 10 && fields[fields.length-1].length() > 0) {
            String[] moveTrack = fields[fields.length-1].split(",");
            for (int small = 0; small < moveTrack.length; ) {
                WordgameTile smallTile = mSmallTiles[Integer.parseInt(moveTrack[small++])]
                                                    [Integer.parseInt(moveTrack[small++])];
                mEntireBoard.moveTrackPhase2.add(smallTile);
            }
        }
        updateAllTiles();
        haveContinueGameData = true;
    }


    //THE FOLLOWING IS ABOUT LOADING DICTIONARY ------------------------------------------------

    //loading 9-char word dictionary and set words into tiles.
    public void initAllSmallTiles(final ArrayList<String> inputDictionary) {
        //loading the nine-char dictionary
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        if(finishLoadingNineCharDictionary == false) {
                            nineCharWordDictionary.addAll(inputDictionary);
                        }

                        //update all tiles with new 9-char words
                        if(haveContinueGameData == false) {
                            for (int large = 0; large < 9; large++) {
                                //find a random value
                                Random random = new Random();
                                int value = random.nextInt(nineCharWordDictionary.size());
                                //find a random 9 character word without repeatation
                                while (rememberRandomNumber.contains(value)) {
                                    value = random.nextInt(nineCharWordDictionary.size());
                                }
                                rememberRandomNumber.add(value);
                                String tileString = nineCharWordDictionary.get(value);
                                //set char to tile
                                for (int small = 0; small < 9; small++) {
                                    mSmallTiles[large][small].setCharacter(tileString.charAt(small));
                                }
                            }
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    updateAllTiles();
                                }
                            });
                        }
                        finishLoadingNineCharDictionary = true;
                        haveContinueGameData = true;
                    }
                }
        ).start();
    }

    //Loading a completed dictionary
    public void initDictionary(final ArrayList<String> inputDictionary) {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        if(finishLoadingDictionary == false) {
                            dictionary.addAll(inputDictionary);
                        }
                        finishLoadingDictionary = true;
                    }
                }
        ).start();
    }


    //THE FOLLOWING IS ABOUT TILES AVAILABILITY  ------------------------------------------------

    private void setAvailableFromLastMove(int large, int small) {
        setAllTilesNotAvailable();
        // Make all the tiles at the destination available
        if (small != -1 | large != -1) {
            //get available tile list for a small tile
            int[] availableList = tileAvailableList.get(small);
            //the small tile you clicked right now
            mSmallTiles[large][small].setAvailable(true);
            mSmallTiles[large][small].lastSelected = true;
            //asign available tiles around this small tile
            for (int n = 0; n < availableList.length; n++) {
                int s = availableList[n];
                WordgameTile tile = mSmallTiles[large][s];
                if (tile.getSelected() == false) {
                    tile.setAvailable(true);
                }
            }
        }
        // If there were none available, make all squares available but matched and selected
        else {
            setAllAvailable();
        }
    }

    //set all tiles available except matched and selected tiles
    private void setAllAvailable() {
        for (int large = 0; large < 9; large++) {
            if (!mLargeTiles[large].finished) {
                for (int small = 0; small < 9; small++) {
                    WordgameTile tile = mSmallTiles[large][small];
                    if (!tile.getSelected() | !tile.getMatched()) {
                        tile.setAvailable(true);
                    }
                }
            }
        }
    }

    private void setAllTilesNotAvailable() {
        for (int large = 0; large < 9; large++) {
            for (int small = 0; small < 9; small++) {
                WordgameTile tile = mSmallTiles[large][small];
                tile.setAvailable(false);
            }
        }
    }

    private void setAllTilesNotSelected() {
        for (int large = 0; large < 9; large++) {
            for (int small = 0; small < 9; small++) {
                WordgameTile tile = mSmallTiles[large][small];
                tile.setSelected(false);
            }
        }
    }

    //THE FOLLOWING IS ABOUT TILES AVAILABILITY for phase 2 ---------------------------------------

    private void setAvailableFromLastMovePhase2(int large, int small) {
        // Make all the tiles at the destination available
        if (small != -1 | large != -1) {
            //get available tile list for a small tile
//            int[] availableList = tileAvailableList.get(small);
            //asign available tiles around this small tile
            for (int l = 0; l < 9; l++) {
                for (int s = 0; s < 9; s++) {
                    WordgameTile tile = mSmallTiles[l][s];
                    if (tile.getCharacter() != ' ' &&
                            l != large &&
                            (!tile.getSelected() && !tile.getMatched())) {
                            tile.setAvailable(true);
                    }else {
                        tile.setAvailable(false);
                    }
                }
            }
            //the small tile you clicked right now
            mSmallTiles[large][small].setAvailable(true);
            mSmallTiles[large][small].lastSelected = true;
        }
        // If there were none available, make all squares available but matched and selected
        else {
            setAllAvailablePhase2();
        }
    }

    //set all tiles available except matched and selected tiles for phase 2
    private void setAllAvailablePhase2() {
        for (int large = 0; large < 9; large++) {
                for (int small = 0; small < 9; small++) {
                    WordgameTile tile = mSmallTiles[large][small];
                    if (tile.getCharacter() != ' ' &&
                            (!tile.getSelected() | !tile.getMatched())) {
                        tile.setAvailable(true);
                    }
            }
        }
    }

    //clearAllMatched tiles
    private void clearAllMatchedPhase2(){
        for (int large = 0; large < 9; large++) {
            WordgameTile largeTile = mLargeTiles[large];
            for (int small = 0; small < 9; small++) {
                WordgameTile tile = mSmallTiles[large][small];
                tile.setMatched(false);
                tile.setSelected(false);
                tile.setAvailable(false);
                tile.lastSelected = false;
            }
        }
    }

//--------------------------------------------------------------------------

    //this will initialize the availability of small tiles after you make a move
    private void initSmallTileAvailability(){
        tileAvailableList.add(new int[]{0,1,3,4});
        tileAvailableList.add(new int[]{0,1,2,3,4,5});
        tileAvailableList.add(new int[]{1,2,4,5});
        tileAvailableList.add(new int[]{0,1,3,4,6,7});
        tileAvailableList.add(new int[]{0,1,2,3,4,5,6,7,8});
        tileAvailableList.add(new int[]{1,2,4,5,7,8});
        tileAvailableList.add(new int[]{3,4,6,7});
        tileAvailableList.add(new int[]{3,4,5,6,7,8});
        tileAvailableList.add(new int[]{4,5,7,8});
    }

}

