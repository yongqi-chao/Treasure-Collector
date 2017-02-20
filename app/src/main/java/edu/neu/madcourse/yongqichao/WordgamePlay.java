package edu.neu.madcourse.yongqichao;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class WordgamePlay extends AppCompatActivity {
    public static final String KEY_RESTORE = "key_restore";
    public static final String PREF_RESTORE = "pref_restore";
    private MediaPlayer mMediaPlayer;
    private Handler mHandler = new Handler();
    private WordgamePlayFragment gameFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordgame_play);
        gameFragment = (WordgamePlayFragment) getFragmentManager()
                .findFragmentById(R.id.fragment_game);
        boolean restore = getIntent().getBooleanExtra(KEY_RESTORE, false);
        if (restore) {
            String gameData = getPreferences(MODE_PRIVATE)
                    .getString(PREF_RESTORE, null);
            if (gameData != null) {
                gameFragment.putState(gameData);
            }
        }
        Log.d("Wordgame", "restore = " + restore);
    }

    public void restartGame() {
        gameFragment.restartGame();
    }

    public void reportWinner(final WordgameTile.Owner winner) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
//            mMediaPlayer.stop();
//            mMediaPlayer.reset();
//            mMediaPlayer.release();
//        }
        builder.setMessage(getString(R.string.declare_winner_wordgame, winner));
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.ok_label_wordgame,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
        final Dialog dialog = builder.create();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                mMediaPlayer = MediaPlayer.create(GameActivity.this,
//                        winner == Tile.Owner.X ? R.raw.oldedgar_winner
//                                : winner == Tile.Owner.O ? R.raw.notr_loser
//                                : R.raw.department64_draw
//                );
//                mMediaPlayer.start();
                dialog.show();
            }
        }, 500);

        // Reset the board to the initial position
        gameFragment.initGame();
    }

//    public void startThinking() {
//        View thinkView = findViewById(R.id.thinking);
//        thinkView.setVisibility(View.VISIBLE);
//    }
//
//    public void stopThinking() {
//        View thinkView = findViewById(R.id.thinking);
//        thinkView.setVisibility(View.GONE);
//    }

    @Override
    protected void onResume() {
        super.onResume();
//        mMediaPlayer = MediaPlayer.create(this, R.raw.frankum_loop001e);
//        mMediaPlayer.setLooping(true);
//        mMediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(null);
//        mMediaPlayer.stop();
//        mMediaPlayer.reset();
//        mMediaPlayer.release();
        String gameData = gameFragment.getState();
        getPreferences(MODE_PRIVATE).edit()
                .putString(PREF_RESTORE, gameData)
                .commit();
        Log.d("UT3", "state = " + gameData);
    }
}
