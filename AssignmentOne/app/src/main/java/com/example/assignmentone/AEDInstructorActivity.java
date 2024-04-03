package com.example.assignmentone;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AEDInstructorActivity extends AppCompatActivity{//implements SelectCountryActivity.CountryListener

    public final static int NO_FLAG = -1;//-1 means no country image selected
    private EditText usernameV,pinV,nameV,emailV;
    private TextView countryV;
    private ImageView countryView;
    private int country_flag;//drawable id of the flag
    private Button add_instructorBtn,update_instructorBtn,delete_instructorBtn;
    private DBModel dbModel;
    private Instructor instructor;//from ViewAllInstructorFragment
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aedinstructor);

        instructor = (Instructor) getIntent().getSerializableExtra("instructor");

        usernameV = findViewById(R.id.username_instructor);
        pinV = findViewById(R.id.pin_instructor);
        nameV = findViewById(R.id.name_instructor);
        emailV = findViewById(R.id.email_instructor);
        countryV = findViewById(R.id.country_instructor);
        countryView = findViewById(R.id.imageView_country_instructor);

        add_instructorBtn = findViewById(R.id.add_instructorBtn);
        update_instructorBtn = findViewById(R.id.udpate_instructorBtn);
        delete_instructorBtn = findViewById(R.id.delete_instructorBtn);

        dbModel = new DBModel();
        dbModel.load(AEDInstructorActivity.this);

        country_flag = NO_FLAG;
        //for Updating the instructor from ViewAllInstructorFragment
        if(instructor != null){
            country_flag = instructor.getCountry();//initially set the country_flag as the original flag first. To ensure it will not disappear after updating
        }
        //end

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {

            @Override//where u get your result from registration activity/other activity
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == RESULT_OK){
                    Intent returnedData = result.getData();
                    if(returnedData != null) {
                            country_flag = returnedData.getIntExtra("country_id", 0);
                            Log.d(getApplicationContext().toString() + " We get ", String.valueOf(country_flag));

                            //update the imageview with the country name
                            countryView.setImageResource(country_flag);
                    }
                }
            }
        });

        //This is for updating/deleting the instructor when view and click it(Functionality 7)
        Instructor instructor = (Instructor) getIntent().getSerializableExtra("instructor");
        if (instructor != null) {
            add_instructorBtn.setEnabled(false);
            //allow update for pin,name,email and country only.Username cannot change
            usernameV.setEnabled(false);
            //show all of the details(Func 7 want)
            usernameV.setText(instructor.getUsername());
            pinV.setText(String.valueOf(instructor.getPinNum()));
            nameV.setText(instructor.getName());
            emailV.setText(instructor.getEmail());
            countryView.setImageResource(instructor.getCountry());
        }
        //End

        countryV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AEDInstructorActivity.this,SelectCountryActivity.class);
                activityResultLauncher.launch(intent);
            }
        });


            add_instructorBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //checking the field before proceed
                    if(usernameV.getText().toString().isEmpty()){
                        usernameV.setError("Username cannot be empty");
                    }
                    else if(pinV.getText().length() != 4){
                        pinV.setError("4 digits PIN Number must be entered");
                    }
                    else if(nameV.getText().toString().isEmpty()){
                        nameV.setError("Name cannot be empty");
                    }
                    else if(country_flag == NO_FLAG){
                        Toast.makeText(AEDInstructorActivity.this,"Please select the country",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        try {
                            String instructorUsername = usernameV.getText().toString().trim();
                            int instructorPIN = Integer.parseInt(pinV.getText().toString().trim());
                            String instructorName = nameV.getText().toString().trim();
                            String instructorEmail = emailV.getText().toString().trim();
                            int instructorCountry = country_flag;
                            Instructor instructor = new Instructor(instructorUsername, instructorPIN, instructorName, instructorEmail, instructorCountry);

                            dbModel.addInstructor(instructor);//insert the instructor into the database
                        } catch (IllegalArgumentException e) {
                            emailV.setError(e.getMessage());
                        }
                    }
                }
            });

            update_instructorBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //checking the field before proceed
                    if(usernameV.getText().toString().isEmpty()){
                        usernameV.setError("Username cannot be empty");
                    }
                    else if(pinV.getText().length() != 4){
                        pinV.setError("4 digits PIN Number must be entered");
                    }
                    else {
                    try {
                        String instructorUsername = usernameV.getText().toString();
                        int instructorPIN = Integer.parseInt(pinV.getText().toString());
                        String instructorName = nameV.getText().toString();
                        String instructorEmail = emailV.getText().toString();
                        int instructorCountry = country_flag;

                        Instructor instructor = new Instructor(instructorUsername, instructorPIN, instructorName, instructorEmail, instructorCountry);

                        dbModel.updateInstructor(instructor);//udpate the instructor in the database
                    } catch (IllegalArgumentException e) {
                        emailV.setError(e.getMessage());
                    }
                    }
                }
            });

            delete_instructorBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //checking the field before proceed
                    if(usernameV.getText().toString().isEmpty()){
                        usernameV.setError("Username cannot be empty");
                    }
                    else {
                        String instructorUsername = usernameV.getText().toString();
                        //simply set only,cuz delete based on the instructor's username
                        int instructorPIN = 1212;
                        String instructorName = "";
                        String instructorEmail = "ins@s.c";
                        int instructorCountry = 0;
                        //Actually we only need the username to delete the instructor
                        Instructor instructor = new Instructor(instructorUsername, instructorPIN, instructorName, instructorEmail, instructorCountry);

                        dbModel.deleteInstructor(instructor);//delete the instructor in the database
                    }
                }
            });
    }

    /*@Override
    public void onInputSentCountry(String countryName) {
        countryV.setText(countryName);
    }*/
}