package com.sdsmdg.pulkit.callingtext;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    EditText et_number ;
    Button btn_save;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        et_number = (EditText)findViewById(R.id.et_number);
        btn_save = (Button)findViewById(R.id.btn_save);


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_number.getText()!=null){
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("NUMBER", et_number.getText().toString()).apply();
                    Toast.makeText(getBaseContext(),"NUMBER SAVED",Toast.LENGTH_LONG).show();

                }else {
                    Toast.makeText(getBaseContext(),"PLEASE ENTER NUMBER",Toast.LENGTH_LONG).show();
                }



            }
        });


    }
}
