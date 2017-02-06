package edu.neu.madcourse.yongqichao;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    Button backButton;
    TextView aboutText;
    ImageView aboutImage;


    private static final int DEV_ID_PERMISSION = 1;
    private static final int PHOTO_PERMISSION = 15;

    private static final String TAG = "DisplayMessageActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitle("Yongqi Chao");


        //add Back button
        backButton = (Button) findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




        Log.i(TAG, "Creating the Activity now");


        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_about);
        TextView imeiTV = (TextView) findViewById(R.id.imei_textview);
        imeiTV.setTextSize(20);

        // Check the permission
        Log.v(TAG, "About to check permissions");
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Already have permission-- groovy!");
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            imeiTV.setText("IMEI : " + tm.getDeviceId());
        }
        else{
            Log.e(TAG, "Don't have permissions yet, gotta ask the user");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    DEV_ID_PERMISSION);
        }


        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                PHOTO_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d(TAG, "Request Permission returned");
        switch (requestCode) {
            case DEV_ID_PERMISSION: {
                Log.i(TAG, "Do I have DEV_ID permissions? ");
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.v(TAG, "The user gave access-- yay!!");
                    // permission was granted, yay! Do the
                    // permissions-related task you need to do.
                    TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    TextView imeiTV = (TextView) findViewById(R.id.imei_textview);
                    imeiTV.setText(" IMEI : " + tm.getDeviceId());


                } else {
                    Log.e(TAG, "User denied permission. ");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case PHOTO_PERMISSION: {
                Log.i(TAG, "Some code that isn't used. ");
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
