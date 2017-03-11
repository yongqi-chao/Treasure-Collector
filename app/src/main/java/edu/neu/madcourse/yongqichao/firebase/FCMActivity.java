package edu.neu.madcourse.yongqichao.firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import edu.neu.madcourse.yongqichao.R;

public class FCMActivity extends AppCompatActivity {

    private static final String TAG = FCMActivity.class.getSimpleName();

    // Please add the server key from your firebase console in the follwoing format "key=<serverKey>"
    private static final String SERVER_KEY = "key=AAAAuseFPB0:APA91bFsOFJtvWkdBwOtXHLZ8OjkgzAI3D6xUCKLdCZaFCi4nwROmz5wWwuZXP9IWJ_jXHQUmX-XqkDyNQ3qca3M8JLYLsoox54_xfTIHpsKalsL4NywiinkTILmPhGqTZh3HG-5fqQf";

    // This is the client registration token
    private static final String CLIENT_REGISTRATION_TOKEN = "ezAsqIUGOdk:APA91bEO8pdimUogQcqYZJ_cCBLOnAGZJwl63MH77ieerpzMZY1gpxWWvCMRMBGgb61mhKAP-hq0rpdzyxe9uhdLIHglnLrXSrR0iBLgXQmNTSpjQQOOr3rMFtyQO9DtwyjL2JmYXgvu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fcm);

        Button logTokenButton = (Button) findViewById(R.id.logTokenButton);
        logTokenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get token
                String token = FirebaseInstanceId.getInstance().getToken();

                // Log and toast
                String msg = getString(R.string.msg_token_fmt, token);
                Log.d(TAG, msg);
                Toast.makeText(FCMActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void pushNotification(View type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                pushNotification();
            }
        }).start();
    }

    /**
     * Pushes a notification to a given device-- in particular, this device,
     * because that's what the instanceID token is defined to be.
     */
    private void pushNotification() {
        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        try {
            jNotification.put("title", "Google I/O 2016");
            jNotification.put("body", "But really this");
            jNotification.put("sound", "default");
            jNotification.put("badge", "1");
            jNotification.put("click_action", "OPEN_ACTIVITY_1");

            // If sending to a single client
            jPayload.put("to", CLIENT_REGISTRATION_TOKEN);

            /*
            // If sending to multiple clients (must be more than 1 and less than 1000)
            JSONArray ja = new JSONArray();
            ja.put(CLIENT_REGISTRATION_TOKEN);
            // Add Other client tokens
            ja.put(FirebaseInstanceId.getInstance().getToken());
            jPayload.put("registration_ids", ja);
            */

            jPayload.put("priority", "high");
            jPayload.put("notification", jNotification);


            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", SERVER_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Send FCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jPayload.toString().getBytes());
            outputStream.close();

            // Read FCM response.
            InputStream inputStream = conn.getInputStream();
            final String resp = convertStreamToString(inputStream);

            Handler h = new Handler(Looper.getMainLooper());
            h.post(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "run: " + resp);
                    Toast.makeText(FCMActivity.this,resp,Toast.LENGTH_LONG).show();
                }
            });
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }

    public void subscribeToNews(View view){
        // [START subscribe_topics]
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        // [END subscribe_topics]

        // Log and toast
        String msg = getString(R.string.msg_subscribed);
        Log.d(TAG, msg);
        Toast.makeText(FCMActivity.this, msg, Toast.LENGTH_SHORT).show();


        // Build notification
        // Actions are just fake
        Notification repliedNotification =
                new Notification.Builder(this)
                        .setSmallIcon(R.drawable.o_red)
                        .setContentText(msg)
                        .build();


        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        repliedNotification.flags |= Notification.FLAG_AUTO_CANCEL ;

        notificationManager.notify(0, repliedNotification);



    }

}