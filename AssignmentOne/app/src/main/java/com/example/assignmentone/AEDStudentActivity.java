package com.example.assignmentone;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

//Better set TextWatcher to ensure the pin is 4 digits but ...
//https://stackoverflow.com/questions/4348146/how-to-run-code-when-exactly-4-digits-letters-have-been-entered-in-a-edittext
public class AEDStudentActivity extends AppCompatActivity {

    private EditText username,pin,name,email,instructorV;
    private TextView country;
    private ImageView country_view;
    private int country_id;
    private Button add_student,update_student,delete_student;
    private DBModel dbModel;
    private Instructor instructor;
    private Student student;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aedstudent);

        username = findViewById(R.id.username_student);
        pin = findViewById(R.id.pin_student);
        name = findViewById(R.id.name_student);
        email = findViewById(R.id.email_student);
        instructorV = findViewById(R.id.instructor_student);
        country = findViewById(R.id.country_student);
        country_view = findViewById(R.id.imageView_country_student);

        add_student = findViewById(R.id.add_studentBtn);
        update_student = findViewById(R.id.udpate_studentBtn);
        delete_student = findViewById(R.id.delete_studentBtn);

        dbModel = new DBModel();
        dbModel.load(AEDStudentActivity.this);


        //How to disable the edit text
        //https://stackoverflow.com/questions/4297763/disabling-of-edittext-in-android
        //Can add simplicity.Eg: if it is instructor
        instructor = (Instructor)getIntent().getSerializableExtra("instructor");
        if(instructor != null){
            //Use 1: help me fill in the text box with his username
            //Use 2: Func 8 & 9: instructor have no power to pass the student to other instructor(only admin can)
            instructorV.setEnabled(false);
            instructorV.setText(instructor.getUsername());
        }

        //Need for functionality 8 and (9?) from student details act
        student = (Student)getIntent().getSerializableExtra("student");
        if(student != null){
            //in func 8 & 9, can only edit and delete student
            add_student.setEnabled(false);
            //username cannot be changed(since only edit and delete)
            username.setEnabled(false);
            //for simplicity,set the fields for them
            username.setText(student.getUsername());
            pin.setText(String.valueOf(student.getPinNum()));
            name.setText(student.getName());
            email.setText(student.getEmail());
            instructorV.setText(student.getInstructor());
            country_view.setImageResource(student.getCountry());
            country_id = student.getCountry();//set the country first.Otherwise, my country will lost later
        }
        //End

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {

            @Override//where u get your result from registration activity/other activity
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == RESULT_OK){
                    Intent returnedData = result.getData();
                    if(returnedData != null) {
                            country_id = returnedData.getIntExtra("country_id", 0);
                            Log.d(getApplicationContext().toString() + " We get ", String.valueOf(country_id));

                            //update the textview with the country name
                            country_view.setImageResource(country_id);
                    }
                }
            }
        });

        country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AEDStudentActivity.this,SelectCountryActivity.class);
                activityResultLauncher.launch(intent);
            }
        });


            add_student.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //checking the field before proceed
                    if(username.getText().toString().isEmpty()){
                        username.setError("Username cannot be empty");
                    }
                    else if(pin.getText().length() != 4){
                        pin.setError("4 digits PIN Number must be entered");
                    }
                    else {
                        try {
                            String studentUsername = username.getText().toString().trim();
                            int studentPIN = Integer.parseInt(pin.getText().toString().trim());
                            String studentName = name.getText().toString().trim();
                            String studentEmail = email.getText().toString().trim();
                            int studentCountry = country_id;
                            String instructorUsername = instructorV.getText().toString().trim();
                            //No need to check if this person is admin or instructor
                            // as if he is instructor, I have set the field with his name,and he cannot change it
                            Student student = student = new Student(studentUsername, studentPIN, studentName, studentEmail,
                                    studentCountry, instructorUsername);
                            if (validateInstructor(instructorUsername)) {//ensure the instructor is exist before add the student
                                dbModel.addStudent(student);//insert the student into the database
                            } else {
                                instructorV.setError("Invalid instructor username");
                            }

                        } catch (IllegalArgumentException e) {
                            email.setError(e.getMessage());
                        }
                    }
                }
            });

            update_student.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        //checking the field before proceed
                        if (username.getText().toString().isEmpty()) {
                            username.setError("Username cannot be empty");
                        } else if(pin.getText().length() != 4){
                            pin.setError("4 digits PIN Number must be entered");
                        } else {
                            try {
                            String studentUsername = username.getText().toString().trim();
                            int studentPIN = Integer.parseInt(pin.getText().toString().trim());
                            String studentName = name.getText().toString().trim();
                            String studentEmail = email.getText().toString().trim();
                            int studentCountry = country_id;
                            String instructorUsername = instructorV.getText().toString().trim();

                            //let's check if the student is created before( and get the practical list if he exists)
                            ArrayList<Practical> practicals = new ArrayList<>();
                            double mark = Student.NO_MARK;
                            Student oldStudent = dbModel.getStudentByQuery("SELECT * FROM " + DBSchema.studentTable.NAME + " WHERE "
                                    + DBSchema.studentTable.Cols.USERNAME + " = \"" + studentUsername + "\"");
                            if (oldStudent != null) {
                                practicals = oldStudent.getPracticals();
                                mark = oldStudent.getMark();
                            }

                            //No need to check if this person is admin or instructor
                            // as if he is instructor, I have set the field with his name,and he cannot change it
                            Student student = new Student(studentUsername, studentPIN, studentName, studentEmail,
                                    studentCountry, instructorUsername, mark, practicals);
                            if (instructor != null) {
                                dbModel.updateStudent(student);//insert the student into the database
                            } else {
                                if (validateInstructor(instructorUsername)) {//ensure the instructor is exist before add the student
                                    dbModel.updateStudent(student);//insert the student into the database
                                } else {
                                    instructorV.setError("Invalid instructor username");
                                }
                            }
                        } catch(IllegalArgumentException e){
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

            delete_student.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //checking the field before proceed
                    if(username.getText().toString().isEmpty()){
                        username.setError("Username cannot be empty");
                    } else {
                        //Actually, we only need username to delete the student....
                        String studentUsername = username.getText().toString();
                        int studentPIN = Integer.parseInt(pin.getText().toString().trim());
                        String studentName = name.getText().toString().trim();
                        String studentEmail = email.getText().toString().trim();
                        int studentCountry = country_id;
                        String instructorUsername = instructorV.getText().toString().trim();
                        Student student = null;
                        if (instructor == null) {//if this is admin
                            student = new Student(studentUsername, studentPIN, studentName, studentEmail,
                                    studentCountry, instructorUsername);
                            dbModel.deleteStudent(student);//delete the student from the database
                        }
                        //if this is instructor and
                        else if (validateInstructor(studentUsername, instructor.getUsername())) {//the student to delete is created by this instructor
                            student = new Student(studentUsername, studentPIN, studentName, studentEmail,
                                    studentCountry, instructorUsername);
                            dbModel.deleteStudent(student);//insert the student into the database
                        } else {//the student is not under this instructor
                            Toast.makeText(getApplicationContext(), "Delete failed as the student is not under you.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
    }

    //ensure the instructor is exist in the database before add the instructor username in the practical
    //true means instructor is exist
    private boolean validateInstructor(String username){//instructor's username
        boolean b = false;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try{
            db = new DBHelper(getApplicationContext()).getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM " + DBSchema.instructorTable.NAME
                    + " WHERE " + DBSchema.instructorTable.Cols.USERNAME + " = \"" + username + "\"",null);
            if(cursor.moveToFirst()){
                b = true;
            }

        }finally {
            cursor.close();
            db.close();
        }
        return b;
    }

    //ensure the student is created by the instructor
    private boolean validateInstructor(String student,String instructor){//student username,instructor username
        boolean b = false;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try{
            db = new DBHelper(getApplicationContext()).getReadableDatabase();
            //get the student with the entered username and instructor = this.instructor's username
            cursor = db.rawQuery("SELECT * FROM " + DBSchema.studentTable.NAME
                    + " WHERE " + DBSchema.studentTable.Cols.USERNAME + " = \"" + student + "\" AND "
                            + DBSchema.studentTable.Cols.INSTRUCTOR + " = \"" + instructor + "\""
                    ,null);
            if(cursor.moveToFirst()){
                b = true;
            }

        }finally {
            cursor.close();
            db.close();
        }
        return b;
    }
}