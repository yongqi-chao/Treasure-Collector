package edu.neu.madcourse.yongqichao.firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import edu.neu.madcourse.yongqichao.R;

import edu.neu.madcourse.yongqichao.firebase.FCMActivity;
import edu.neu.madcourse.yongqichao.firebase.RealtimeDatabaseActivity;

public class FIREMAIN extends AppCompatActivity {

    private static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firemain);

    }



    public void openFCMActivity(View view) {
        startActivity(new Intent(FIREMAIN.this, FCMActivity.class));
    }

    public void openDBActivity(View view) {
        startActivity(new Intent(FIREMAIN.this, RealtimeDatabaseActivity.class));
    }

    public void sendNotification(View view){
        // Prepare intent which is triggered if the
        // notification is selected
        Intent intent = new Intent(this, ReceiveNotificationActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        // Build notification
        // Actions are just fake
        Notification noti = new Notification.Builder(this)
                .setContentTitle("New mail from " + "test@gmail.com")
                .setContentText("Subject").setSmallIcon(R.drawable.o_red)
                .setContentIntent(pIntent).build();
/*                .addAction(R.drawable.icon, "Call", pIntent)
                .addAction(R.drawable.icon, "More", pIntent)
                .addAction(R.drawable.icon, "And more", pIntent).build();*/

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL ;

        notificationManager.notify(0, noti);
    }
}