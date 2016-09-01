package com.example.mover.mover;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
//TODO: Add sign up functionality
public class LoginActivity extends AppCompatActivity {

    private EditText userNameText;
    private EditText passwordText;
    String postResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userNameText = (EditText) findViewById(R.id.userNameText);
        passwordText = (EditText) findViewById(R.id.passwordText);

    }

    //TODO: Authenticate User
    public void attemptLogin(View view) {

        // Store values at the time of the login attempt.
        String email = userNameText.getText().toString();
        String password = passwordText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!isPasswordValid(password)) {
            passwordText.setError(Html.fromHtml("<font color='red'>Invalid Password</font>"));
            focusView = passwordText;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
           userNameText.setError(Html.fromHtml("<font color='red'>This Filed is Required</font>"));
            focusView = userNameText;
            cancel = true;
        } else if (!isEmailValid(email)) {
            userNameText.setError(Html.fromHtml("<font color='red'>Invalid Email</font>"));
            focusView = userNameText;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            //All fields filled in correctly, try login

            //All fields filled out fine, try sign up
            if(loginRequest(email, password)){
                Mover.setUser(email);
                //Go to main activity
                Intent k = new Intent(this, MainActivity.class);
                startActivity(k);
            }
        }
    }

    public Boolean loginRequest(String user, String password) {

        postRequest asyncTask = (postRequest) new postRequest(new postRequest.AsyncResponse() {

            @Override
            public void processFinish(String output) {

                Context context = getApplicationContext();

                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, output, duration);
                toast.show();
            }
        }, "email=" + user + "&password=" + password).execute("http://139.162.178.79:4000/login");

        try {
            postResponse = asyncTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if(postResponse.contains("error")){
            return false;
        }
        else {
            return true;
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    public void signup(View view) {
        Intent k = new Intent(this, signupActivity.class);
        startActivity(k);
    }
}

