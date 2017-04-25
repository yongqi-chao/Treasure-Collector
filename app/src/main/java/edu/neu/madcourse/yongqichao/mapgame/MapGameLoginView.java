package edu.neu.madcourse.yongqichao.mapgame;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.util.logging.Handler;

import edu.neu.madcourse.yongqichao.R;

public class MapGameLoginView extends AppCompatActivity
        implements View.OnClickListener {
    EditText usernameText;
    EditText passwordText;
    Button loginButton, createUser;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_game_login_view);
        setTitle("LOG IN");

        //beep sound when match a user and password


        //a way of entering username
        usernameText = (EditText) findViewById(R.id.usernameText);
        usernameText.setOnClickListener(this);
//            @Override
//            public void onClick(View v) {
//                usernameText.setText("");
//            }
//        });
        usernameText.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        passwordText = (EditText) findViewById(R.id.passwordText);

        loginButton = (Button) findViewById(R.id.loginViewlogin);
        loginButton.setOnClickListener(this);
        createUser = (Button) findViewById(R.id.createUser);
        createUser.setOnClickListener(this);
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

//            // Views
//            mStatusTextView = (TextView) findViewById(R.id.status);
//            mDetailTextView = (TextView) findViewById(R.id.detail);
//            mEmailField = (EditText) findViewById(R.id.field_email);
//            mPasswordField = (EditText) findViewById(R.id.field_password);
//
//            // Buttons
//            findViewById(R.id.email_sign_in_button).setOnClickListener(this);
//            findViewById(R.id.email_create_account_button).setOnClickListener(this);
//            findViewById(R.id.sign_out_button).setOnClickListener(this);
//            findViewById(R.id.verify_email_button).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

    }

    //    public class EmailPasswordActivity extends BaseActivity implements
//            View.OnClickListener {

    private static final String TAG = "EmailPassword";
//
//        private TextView mStatusTextView;
//        private TextView mDetailTextView;
//        private EditText mEmailField;
//        private EditText mPasswordField;


//        @Override
//        public void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_emailpassword);
//
//            // Views
//            mStatusTextView = (TextView) findViewById(R.id.status);
//            mDetailTextView = (TextView) findViewById(R.id.detail);
//            mEmailField = (EditText) findViewById(R.id.field_email);
//            mPasswordField = (EditText) findViewById(R.id.field_password);
//
//            // Buttons
//            findViewById(R.id.email_sign_in_button).setOnClickListener(this);
//            findViewById(R.id.email_create_account_button).setOnClickListener(this);
//            findViewById(R.id.sign_out_button).setOnClickListener(this);
//            findViewById(R.id.verify_email_button).setOnClickListener(this);
//
//            // [START initialize_auth]
//            mAuth = FirebaseAuth.getInstance();
//            // [END initialize_auth]
//        }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }
    // [END on_start_check_user]

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
            if (!validateForm()) {
                Toast.makeText(MapGameLoginView.this, "No Space in neither name nor password",
                        Toast.LENGTH_SHORT).show();
                return;
            }

        showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MapGameLoginView.this, "Authentication Succeed.",
                                    Toast.LENGTH_SHORT).show();
                            signIn(usernameText.getText().toString() + "@yq.com", passwordText.getText().toString());
                            //hideProgressDialog();

                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MapGameLoginView.this, "Authentication Failed.",
                                    Toast.LENGTH_SHORT).show();
                            hideProgressDialog();

                            //updateUI(null);
                        }

                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
            if (!validateForm()) {
                Toast.makeText(MapGameLoginView.this, "No Space in neither name nor password",
                        Toast.LENGTH_SHORT).show();
                return;
            }

        showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MapGameLoginView.this, "Authentication Success.",
                                    Toast.LENGTH_SHORT).show();
                            Intent login = new Intent(MapGameLoginView.this, MapGameMapView.class);
                            login.putExtra("username", usernameText.getText().toString());
                            startActivity(login);
                            final MediaPlayer clickSound = MediaPlayer.create(MapGameLoginView.this, R.raw.beep);
                            clickSound.start();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MapGameLoginView.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            //mStatusTextView.setText(R.string.auth_failed);
                        }
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    private void signOut() {
        mAuth.signOut();
        //updateUI(null);
    }

//        private void sendEmailVerification() {
//            // Disable button
//            findViewById(R.id.verify_email_button).setEnabled(false);
//
//            // Send verification email
//            // [START send_email_verification]
//            final FirebaseUser user = mAuth.getCurrentUser();
//            user.sendEmailVerification()
//                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            // [START_EXCLUDE]
//                            // Re-enable button
//                            findViewById(R.id.verify_email_button).setEnabled(true);
//
//                            if (task.isSuccessful()) {
//                                Toast.makeText(EmailPasswordActivity.this,
//                                        "Verification email sent to " + user.getEmail(),
//                                        Toast.LENGTH_SHORT).show();
//                            } else {
//                                Log.e(TAG, "sendEmailVerification", task.getException());
//                                Toast.makeText(EmailPasswordActivity.this,
//                                        "Failed to send verification email.",
//                                        Toast.LENGTH_SHORT).show();
//                            }
//                            // [END_EXCLUDE]
//                        }
//                    });
//            // [END send_email_verification]
//        }

        private boolean validateForm() {
            boolean valid = true;

            String name = usernameText.getText().toString();
            String password = usernameText.getText().toString();

//            if (TextUtils.isEmpty(email)) {
//                mEmailField.setError("Required.");
//                valid = false;
//            } else {
//                mEmailField.setError(null);
//            }
//
//            String password = mPasswordField.getText().toString();
//            if (TextUtils.isEmpty(password)) {
//                mPasswordField.setError("Required.");
//                valid = false;
//            } else {
//                mPasswordField.setError(null);
//            }
            if(!name.matches("[a-zA-Z]+")){
                valid = false;
            }
            if(!password.matches("[a-zA-Z]+")){
                valid = false;
            }

            return valid;
        }

//        private void updateUI(FirebaseUser user) {
//            hideProgressDialog();
//            if (user != null) {
//                mStatusTextView.setText(getString(R.string.emailpassword_status_fmt,
//                        user.getEmail(), user.isEmailVerified()));
//                mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));
//
//                findViewById(R.id.email_password_buttons).setVisibility(View.GONE);
//                findViewById(R.id.email_password_fields).setVisibility(View.GONE);
//                findViewById(R.id.signed_in_buttons).setVisibility(View.VISIBLE);
//
//                findViewById(R.id.verify_email_button).setEnabled(!user.isEmailVerified());
//            } else {
//                mStatusTextView.setText(R.string.signed_out);
//                mDetailTextView.setText(null);
//
//                findViewById(R.id.email_password_buttons).setVisibility(View.VISIBLE);
//                findViewById(R.id.email_password_fields).setVisibility(View.VISIBLE);
//                findViewById(R.id.signed_in_buttons).setVisibility(View.GONE);
//            }
//        }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.usernameText) {
            usernameText.setText("");

//                createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if (i == R.id.passwordText) {
            //signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if (i == R.id.loginViewlogin) {
            if(!passwordText.getText().toString().isEmpty() && !usernameText.getText().toString().isEmpty()){
                signIn(usernameText.getText().toString() + "@yq.com", passwordText.getText().toString());

            }
            else{
                //signIn("qq@yq.com", "123123");
                Toast.makeText(MapGameLoginView.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
            }





            //signOut();
        }else if (i == R.id.createUser) {
            if(!passwordText.getText().toString().isEmpty() && !usernameText.getText().toString().isEmpty()){
                createAccount(usernameText.getText().toString() + "@yq.com", passwordText.getText().toString());

            }
            else{
                Toast.makeText(MapGameLoginView.this, "Create Account Failed.",
                        Toast.LENGTH_SHORT).show();
            }





            //signOut();
        }

//            else if (i == R.id.verify_email_button) {
//                sendEmailVerification();
//            }
    }

    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Logging in");

            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }
}
