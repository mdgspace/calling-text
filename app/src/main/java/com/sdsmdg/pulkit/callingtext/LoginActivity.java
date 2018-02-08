package com.sdsmdg.pulkit.callingtext;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private String userName;
    private String userNumber;
    EditText nameEditText;
    EditText passwordEditText;
    DataBaseHandler dbHandler;
    SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        session = new SessionManager(getApplicationContext());
        nameEditText = (EditText) findViewById(R.id.userName);
        passwordEditText = (EditText) findViewById(R.id.userPhoneNumber);

        Toast.makeText(this, "User Login Status :" + session.isLoggedIn(), Toast.LENGTH_SHORT).show();
        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUser();
                Intent intent = new Intent(LoginActivity.this, BaseActivity.class);
                startActivity(intent);
                finish();

            }
        });


    }

    /**
     * This function adds a user by checking whether the username and phone number is valid or not
     */
    private void setUser() {
        //get username and usernumber from editText
        userName = nameEditText.getText().toString();
        userNumber = passwordEditText.getText().toString();
        if (userName.trim().length() > 0 && userNumber.trim().length() > 0) {

            //creating user login session
            session.createLoginSession(userName,userNumber);
            //adding user to the database
            dbHandler = DataBaseHandler.getInstance(this);
            dbHandler.addUser(userName, userNumber);
        }
        else
            Toast.makeText(this, "Login failed due to empty fields", Toast.LENGTH_SHORT).show();

    }


}
