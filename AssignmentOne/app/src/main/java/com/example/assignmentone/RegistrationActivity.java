package com.example.assignmentone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegistrationActivity extends AppCompatActivity {

    private Button enter;
    private EditText username,pin,reenterPIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        username = findViewById(R.id.username_registration);
        pin = findViewById(R.id.pin1_registration);
        reenterPIN = findViewById(R.id.pin2_registration);
        enter = findViewById(R.id.pinEnter_registration);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check the error before return result
                //1. check if the username is unique(any same username exists in database?)/match the pattern
                //Since in this case only have one admin, so no need to check?
                if (username.getText().toString().isEmpty()) {
                    username.setError("Username cannot be empty");
                }
                else if (pin.getText().toString().isEmpty()) {
                    pin.setError("Field cannot be empty");
                }
                else if (reenterPIN.getText().toString().isEmpty()) {
                    reenterPIN.setError("Field cannot be empty");
                }
                else {
                    //2. check if two pin number are the same
                    if (Integer.parseInt(String.valueOf(pin.getText())) == Integer.parseInt(String.valueOf(reenterPIN.getText()))) {
                        //Since username is unique + PIN number entered twice are matched,then we just go back to original activity and ask him to login
                        Intent returnedData = new Intent();
                        int pinNum = Integer.parseInt(String.valueOf(pin.getText()));
                        returnedData.putExtra("username", String.valueOf(username.getText()).trim());
                        returnedData.putExtra("pin", pinNum);
                        setResult(RESULT_OK, returnedData);
                        finish();
                    } else {
                        Toast.makeText(RegistrationActivity.this, "PIN number must be matched", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

}
