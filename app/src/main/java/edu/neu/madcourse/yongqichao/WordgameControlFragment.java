package edu.neu.madcourse.yongqichao;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WordgameControlFragment extends Fragment {
    private AlertDialog mDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =
                inflater.inflate(R.layout.activity_wordgame_control_fragment, container, false);
        View main = rootView.findViewById(R.id.button_main);
        View restart = rootView.findViewById(R.id.button_restart);
        final View pause = rootView.findViewById(R.id.PauseWordGame);
        final View resume = rootView.findViewById(R.id.ResumeWordGame);
        View hint = rootView.findViewById(R.id.hintWordGame);


        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((WordgamePlay) getActivity()).restartGame();
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((WordgamePlay) getActivity()).pauseGame();
                pause.setVisibility(View.GONE);
                resume.setVisibility(View.VISIBLE);
            }
        });

        resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((WordgamePlay) getActivity()).resumeGame();
                pause.setVisibility(View.VISIBLE);
                resume.setVisibility(View.GONE);
            }
        });

        hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.hintWordGame);
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

        return rootView;
    }

}
