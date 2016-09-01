package com.example.mover.mover;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;

/**
 * Created by LUKE on 2016/07/10.
 */
public class signupActivity extends AppCompatActivity {

    EditText nameText;
    EditText surnameText;
    EditText emailText;
    EditText passwordText;
    EditText confirmPasswordText;
    String name;
    String surname;
    String email;
    String password;
    String confirmPassword;
    String postResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        nameText = (EditText) findViewById(R.id.nameText);
        surnameText = (EditText) findViewById(R.id.surnameText);
        emailText = (EditText) findViewById(R.id.emailText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        confirmPasswordText = (EditText) findViewById(R.id.confirmPasswordText);


    }

    //TODO: validate fields and add user to db
    public void attemptSignup(View view) {

        name = nameText.getText().toString();
        surname = surnameText.getText().toString();
        email = emailText.getText().toString();
        password = passwordText.getText().toString();
        confirmPassword = confirmPasswordText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        //Check if passwords match
        if (!password.equals(confirmPassword)) {
            confirmPasswordText.setError(Html.fromHtml("<font color='red'>Non matching passwords</font>"));
            focusView = passwordText;
            cancel = true;
        }
        // Check for a valid password, if the user entered one.
        if (!isPasswordValid(password)) {
            passwordText.setError(Html.fromHtml("<font color='red'>Invalid Password</font>"));
            focusView = passwordText;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            emailText.setError(Html.fromHtml("<font color='red'>This Field is Required</font>"));
            focusView = emailText;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailText.setError(Html.fromHtml("<font color='red'>Invalid Email</font>"));
            focusView = emailText;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            //All fields filled out fine, try sign up
            if(signupRequest(name, surname, email, password)){
                Mover.setUser(email);
                //Go to main activity
                Intent k = new Intent(this, MainActivity.class);
                startActivity(k);
            }
        }
    }


    public Boolean signupRequest(String name, String surname, String user, String password) {

        postRequest asyncTask = (postRequest) new postRequest(new postRequest.AsyncResponse() {

            @Override
            public void processFinish(String output) {

                Context context = getApplicationContext();

                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, output, duration);
                toast.show();
            }
        }, "name=" + name + "&surname=" + surname + "&email=" + user + "&password=" + password + "&password_confirm=" + password ).execute("http://139.162.178.79:4000/register");

        try {
            postResponse = asyncTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if(postResponse.contains("error") || postResponse.contains("Exception")){
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

}
