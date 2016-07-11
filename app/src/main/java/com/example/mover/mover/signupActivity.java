package com.example.mover.mover;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;

/**
 * Created by LUKE on 2016/07/10.
 */
public class signupActivity extends AppCompatActivity {

    Calendar myCalendar = Calendar.getInstance();
    EditText nameText;
    EditText surnameText;
    EditText emailText;
    EditText dobText;
    EditText passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        nameText = (EditText) findViewById(R.id.nameText);
        surnameText = (EditText) findViewById(R.id.surnameText);
        emailText = (EditText) findViewById(R.id.emailText);
        dobText = (EditText) findViewById(R.id.dobText);
        passwordText = (EditText) findViewById(R.id.passwordText);

        dobText.setText(myCalendar.get(Calendar.DATE) + "/"
                + (myCalendar.get(Calendar.MONTH) + 1) + "/"
                + myCalendar.get(Calendar.YEAR));

        Button signupButton = (Button) findViewById(R.id.sign_in_button);
       signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignup();
            }
        });

    }

    //TODO: validate fields and add user to db
    public void attemptSignup() {
        Intent k = new Intent(this, MainActivity.class);
        startActivity(k);
    }

}
