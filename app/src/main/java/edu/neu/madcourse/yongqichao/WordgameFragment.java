package edu.neu.madcourse.yongqichao;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.neu.madcourse.yongqichao.leaderboard.leaderBoard;
import edu.neu.madcourse.yongqichao.leaderboard.scoreBoard;

public class WordgameFragment extends Fragment {

    private AlertDialog mDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =
                inflater.inflate(R.layout.activity_wordgame_fragment, container, false);
        // Handle buttons here...
        View newButton = rootView.findViewById(R.id.new_button);
        View continueButton = rootView.findViewById(R.id.continue_button);
        View aboutButton = rootView.findViewById(R.id.about_button);
        View acknowledgement = rootView.findViewById(R.id.acknowledgementWordgame);
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WordgameChooseUsername.class);
                getActivity().startActivity(intent);
            }
        });
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WordgamePlay.class);
                intent.putExtra(WordgamePlay.KEY_RESTORE, true);
                getActivity().startActivity(intent);
            }
        });
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.about_text_wordgame);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.ok_label_wordgame,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // nothing
                            }
                        });
                mDialog = builder.show();
            }
        });
        acknowledgement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Acknowledgements.class);
                getActivity().startActivity(intent);
            }
        });


        //leaderboards activities
        View scoreboardButton = rootView.findViewById(R.id.scoreboard);
        View leaderboardButton = rootView.findViewById(R.id.leaderboard);
        scoreboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), scoreBoard.class);
                //intent.putExtra(WordgamePlay.KEY_RESTORE, true);
                getActivity().startActivity(intent);
            }
        });
        leaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), leaderBoard.class);
                //intent.putExtra(WordgamePlay.KEY_RESTORE, true);
                getActivity().startActivity(intent);
            }
        });


        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();

        // Get rid of the about dialog if it's still up
        if (mDialog != null)
            mDialog.dismiss();
    }
}

