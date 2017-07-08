package com.sdsmdg.pulkit.callingtext;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Settings extends AppCompatActivity {

    EditText editTextNumber, editTextStatus ;
    Button saveButton;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users_status");
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        editTextNumber = (EditText)findViewById(R.id.et_number);
        editTextStatus = (EditText)findViewById(R.id.et_status);
        saveButton = (Button)findViewById(R.id.btn_save);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editTextNumber.getText()!=null){
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("NUMBER", editTextNumber.getText().toString()).apply();
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("STATUS", editTextStatus.getText().toString()).apply();
                    /*
                     * Adding the user status to the firebase database
                     * Adding the user's status to the firebase database as a child of users_status reference
                     * reference.child(editTextNumber.getText().toString()).setValue(editTextStatus.getText().toString());
                     */
                    Toast.makeText(getBaseContext(),"NUMBER SAVED",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getBaseContext(),"PLEASE ENTER NUMBER",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
