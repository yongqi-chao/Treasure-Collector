package edu.neu.madcourse.yongqichao.mapgame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import edu.neu.madcourse.yongqichao.R;

public class MapGameStreetView extends AppCompatActivity {

    Button backToMap;
    AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_game_street_view);

        setTitle("You Found a Coin!");

        Bundle b = this.getIntent().getExtras();
        final LatLng position = (LatLng) b.get("lat");


        SupportStreetViewPanoramaFragment streetViewPanoramaFragment =
                (SupportStreetViewPanoramaFragment)
                        getSupportFragmentManager().findFragmentById(R.id.street);

        streetViewPanoramaFragment.getStreetViewPanoramaAsync(
                new OnStreetViewPanoramaReadyCallback() {
                    @Override
                    public void onStreetViewPanoramaReady(StreetViewPanorama panorama) {
                        // Only set the panorama to SYDNEY on startup (when no panoramas have been
                        // loaded which is when the savedInstanceState is null).
//                        if (savedInstanceState == null) {
                            panorama.setPosition(position);
//                        }
                    }
                });

        backToMap = (Button) findViewById(R.id.BacktoMap);
        backToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(MapGameStreetView.this);
        builder.setMessage("Congratulation!\n You found a coin \n You earned 100 score");
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
}