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
    private int mLastLarge;
    private int mLastSmall;

    private ArrayList<String> dictionary = new ArrayList<>();
    //a lsit to remember random number
    private HashSet<Integer> rememberRandomNumber = new HashSet<>();
    private boolean finishLoadingDictionary = false;
    private boolean haveContinueGameData = false;
    private  ArrayList<int[]> tileAvailableList =new ArrayList<>();
    //activity_wordgame_play_fragment

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
                        if (smallTile.getAvailable() && (finishLoadingDictionary | haveContinueGameData)) {
                            //((WordgamePlay)getActivity()).startThinking();
//                            mSoundPool.play(mSoundX, mVolume, mVolume, 1, 0, 1f);
                            makeMove(fLarge, fSmall);
                           // clickSound.start();
//                            think();
                        } else {
//                            mSoundPool.play(mSoundMiss, mVolume, mVolume, 1, 0, 1f);
                        }
                    }
                });
                // ...
            }
        }
        Log.d("12321","123sddaadswqewqdasadsxzcxzcasdasdqwewqadsdascxzxzc");

    }

//    private void think() {
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
//    }

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

    private void cancleLastSelect(int large, int small) {
        if (small != -1) {
            WordgameTile smallTile = mSmallTiles[large][small];
            smallTile.lastSelected = false;
        }
    }

    private void makeMove(int large, int small) {
        cancleLastSelect(mLastLarge,mLastSmall);
        WordgameTile smallTile = mSmallTiles[large][small];
        WordgameTile largeTile = mLargeTiles[large];
        switchSelect(smallTile);
        if(mLastSmall == small){
            ArrayList<WordgameTile> moveTrack = largeTile.getMoveTrack();
            moveTrack.remove(moveTrack.size()-1);
            //-1
            if(moveTrack.isEmpty()) {
                mLastLarge = -1;
                mLastSmall = -1;
            }else{
                mLastLarge = large;
                mLastSmall = moveTrack.get(moveTrack.size()-1).moveTrackingNumber;
                smallTile = mSmallTiles[mLastLarge][mLastSmall];
                smallTile.lastSelected = true;
            }
        }else {
            mLastLarge = large;
            mLastSmall = small;
            smallTile.moveTrackingNumber = small;
            largeTile.getMoveTrack().add(smallTile);
            smallTile = mSmallTiles[mLastLarge][mLastSmall];
            smallTile.lastSelected = true;
        }
        setAvailableFromLastMove(mLastLarge,mLastSmall);


        WordgameTile.Owner oldWinner = largeTile.getOwner();
        WordgameTile.Owner winner = largeTile.findWinner();
        if (winner != oldWinner) {
//            largeTile.animate();
            largeTile.setOwner(winner);
        }
        winner = mEntireBoard.findWinner();
        mEntireBoard.setOwner(winner);
        updateAllTiles();
        if (winner != WordgameTile.Owner.NEITHER) {
            ((WordgamePlay)getActivity()).reportWinner(winner);
        }
    }

    public void restartGame() {
        haveContinueGameData = false;
//        mSoundPool.play(mSoundRewind, mVolume, mVolume, 1, 0, 1f);
        // ...
        initGame();
        initViews(getView());
        //updateAllTiles();

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        while (finishLoadingDictionary == false) {
                            //waiting
                        }
                        initAllSmallTiles(dictionary);
                    }
                }).start();
    }

    public void initGame() {
        Log.d("UT3", "init game");
        mEntireBoard = new WordgameTile(this);
        // Create all the tiles
        for (int large = 0; large < 9; large++) {
            mLargeTiles[large] = new WordgameTile(this);
            for (int small = 0; small < 9; small++) {
                mSmallTiles[large][small] = new WordgameTile(this);
            }
            mLargeTiles[large].setSubTiles(mSmallTiles[large]);
        }
        mEntireBoard.setSubTiles(mLargeTiles);

        // If the player moves first, set which spots are available
        mLastSmall = -1;
        mLastLarge = -1;
        initDefaultSmallTileAvailability();
        setAvailableFromLastMove(mLastLarge,mLastSmall);
    }



    private void updateAllTiles() {
        mEntireBoard.updateDrawableState();
        for (int large = 0; large < 9; large++) {
            mLargeTiles[large].updateDrawableState();
            for (int small = 0; small < 9; small++) {
                mSmallTiles[large][small].updateButtonState();
            }
        }
    }

    /** Create a string containing the state of the game. */
    public String getState() {
        if(haveContinueGameData) {
            StringBuilder builder = new StringBuilder();
            builder.append(mLastLarge);
            builder.append(',');
            builder.append(mLastSmall);
            builder.append(',');
            for (int large = 0; large < 9; large++) {
                for (int small = 0; small < 9; small++) {
                    builder.append(mSmallTiles[large][small].getSelected());
                    builder.append(',');
                }
            }
            return builder.toString();
        }
        else return null;
    }

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

    /** Restore the state of the game from the given string. */
    public void putState(String gameData) {
        String[] fields = gameData.split(",");
        int index = 0;

        mLastLarge = Integer.parseInt(fields[index++]);
        mLastSmall = Integer.parseInt(fields[index++]);
        for (int large = 0; large < 9; large++) {
            for (int small = 0; small < 9; small++) {
//                WordgameTile. owner = WordgameTile.Owner.valueOf(fields[index++]);
                boolean select = (fields[index++]).equals("true") ? true : false;
                mSmallTiles[large][small].setSelected(select);
            }
        }
        setAvailableFromLastMove(mLastLarge,mLastSmall);
        updateAllTiles();
    }


    /** Restore the words of the game from the given string. */
    public void putWords(String gameData) {
        String[] fields = gameData.split(",");
        int index = 0;
        for (int large = 0; large < 9; large++) {
            for (int small = 0; small < 9; small++) {
//                WordgameTile. owner = WordgameTile.Owner.valueOf(fields[index++]);
                mSmallTiles[large][small].setCharacter(fields[index++].toString().charAt(0));
            }
        }
        //setAvailableFromLastMove(mLastSmall);
        updateAllTiles();
        haveContinueGameData = true;
    }

//    /** Restore the dictionary of the game from the given string. */
//    public void putDictionary(String dictionaryData) {
//        Log.d("size", String.valueOf(dictionary.size()));
//
//        String[] fields = dictionaryData.split(",");
//        ArrayList<String> listofwords = new ArrayList<>();
//        for (int index = 0; index < fields.length; index++) {
//            listofwords.add(fields[index]);
//        }
//        dictionary.addAll(listofwords);
//        Log.d("size", String.valueOf(dictionary.size()));
//
//        initAllSmallTiles();
//    }


    public void initAllSmallTiles(final ArrayList<String> dictionaryArray) {

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        if(finishLoadingDictionary == false) {
//                            InputStreamReader input = new InputStreamReader(getResources().openRawResource(R.raw.wordlist));
//                            BufferedReader r = new BufferedReader(input);
//                            String word;
//                            //convert word to hashset
//
//                            try {
//                                while ((word = r.readLine()) != null) {
//                                    if (word.length() == 9) dictionary.add(word);
//                                }
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }

                            dictionary.addAll(dictionaryArray);
                        }

                        if(haveContinueGameData == false) {
                            for (int large = 0; large < 9; large++) {

                                //find a random value
                                Random random = new Random();
                                int value = random.nextInt(dictionary.size());
                                while (rememberRandomNumber.contains(value)) {
                                    value = random.nextInt(dictionary.size());
                                }
                                //find a random 9 character word
                                rememberRandomNumber.add(value);
                                String tileString = dictionary.get(value);

                                for (int small = 0; small < 9; small++) {
                                    mSmallTiles[large][small].setCharacter(tileString.charAt(small));
                                    mSmallTiles[large][small].setSelected(false);
                                    // ...
                                }
                            }


                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    updateAllTiles();

                                }
                            });
                        }
//
                        finishLoadingDictionary = true;
                        haveContinueGameData = true;

                    }
                }
        ).start();

    }


    /// game logic

    private void clearAvailable() {
        availableTiles.clear();
        setAllNotAvailable();
    }

    private void addAvailable(WordgameTile tile) {
//        tile.animate();
        availableTiles.add(tile);
    }

//    public boolean isAvailable(WordgameTile tile) {
//        return availableTiles.contains(tile);
//    }

    private void setAvailableFromLastMove(int large, int small) {
        clearAvailable();
        // Make all the tiles at the destination available
        if (small != -1 | large != -1) {
            int[] availableList = tileAvailableList.get(small);
            for (int n = 0; n < availableList.length; n++) {
                int s = availableList[n];
                WordgameTile tile = mSmallTiles[large][s];
                if (tile.getSelected() == false) {
                    addAvailable(tile);
                    tile.setAvailable(true);
                }
                addAvailable(mSmallTiles[large][small]);
                mSmallTiles[large][small].setAvailable(true);
            }
        }
        // If there were none available, make all squares available
        if (availableTiles.isEmpty()) {
            setAllAvailable();
        }
    }

    private void setAllAvailable() {
        for (int large = 0; large < 9; large++) {
            for (int small = 0; small < 9; small++) {
                WordgameTile tile = mSmallTiles[large][small];
                if (tile.getSelected() == false) {
                    addAvailable(tile);
                    tile.setAvailable(true);
                }
//                if (tile.getOwner() == WordgameTile.Owner.NEITHER)
//                    addAvailable(tile);

            }
        }
    }

    private void setAllNotAvailable() {
        for (int large = 0; large < 9; large++) {
            for (int small = 0; small < 9; small++) {
                WordgameTile tile = mSmallTiles[large][small];
                //if (tile.getSelected() == false) {
                    //addAvailable(tile);
                tile.setAvailable(false);

//                if (tile.getOwner() == WordgameTile.Owner.NEITHER)
//                    addAvailable(tile);

            }
        }
    }


    private void initDefaultSmallTileAvailability(){
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

