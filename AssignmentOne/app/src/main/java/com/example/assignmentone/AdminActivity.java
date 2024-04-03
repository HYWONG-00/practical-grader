package com.example.assignmentone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminActivity extends AppCompatActivity {

    private static final String STUDENTS = "students";//used as id for passing student list to ViewAllStudentActivity
    private Button aedStudentBtn,aedInstructorBtn,aedPracBtn,markingBtn,viewIntrucsPracsBtn,viewStudentBtn;
    private Admin admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Intent intent = getIntent();
        admin = (Admin)intent.getSerializableExtra("admin");

        aedStudentBtn = findViewById(R.id.aed_student);
        aedInstructorBtn = findViewById(R.id.aed_instructor);
        aedPracBtn = findViewById(R.id.aed_practical);
        markingBtn = findViewById(R.id.mark_student);
        viewIntrucsPracsBtn = findViewById(R.id.view_instructors_practicals);
        viewStudentBtn = findViewById(R.id.view_students);

        aedStudentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this,AEDStudentActivity.class);
                startActivity(intent);
            }
        });

        aedInstructorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this,AEDInstructorActivity.class);
                startActivity(intent);
            }
        });

        aedPracBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this,AEDPracticalActivity.class);
                startActivity(intent);
            }
        });

        markingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this,MarkingActivity.class);
                startActivity(intent);
            }
        });

        viewIntrucsPracsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this,ViewAllPracticalInstructorActivity.class);
                startActivity(intent);
            }
        });

        viewStudentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //actually getReadableDatabase() will be better than getWriteableDatabase()(in load()),since we only view rn.
                DBModel dbModel = new DBModel();
                dbModel.read(getApplicationContext());//getApplicationContext(), return AdminActivity.this in this case
                Intent intent = new Intent(AdminActivity.this, ViewAllStudentActivity.class);
                intent.putExtra(STUDENTS, dbModel.getStudents());
                startActivity(intent);
            }
        });
    }
}