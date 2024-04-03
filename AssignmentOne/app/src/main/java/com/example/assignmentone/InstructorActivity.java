package com.example.assignmentone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class InstructorActivity extends AppCompatActivity{

    private Button aedStudentB,editMarkB,viewStudentsB;
    private Instructor instructor;
    private DBModel dbModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor);
        setTitle("Instructor page");

        instructor = (Instructor) getIntent().getSerializableExtra("instructor");

        aedStudentB = findViewById(R.id.aed_student);
        editMarkB = findViewById(R.id.edit_mark);
        viewStudentsB = findViewById(R.id.instructor_viewAllStudentBtn);

        dbModel = new DBModel();
        dbModel.read(this);

        aedStudentB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InstructorActivity.this,AEDStudentActivity.class);
                intent.putExtra("instructor",instructor);
                startActivity(intent);
            }
        });

        editMarkB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InstructorActivity.this,MarkingActivity.class);
                intent.putExtra("instructor",instructor);
                startActivity(intent);
            }
        });

        viewStudentsB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pass the students which under this instructor only(so we no need to pass instructor anymore)
                ArrayList<Student> students = new ArrayList<>();
                students = dbModel.getStudentsByQuery("SELECT * FROM " + DBSchema.studentTable.NAME
                        + " WHERE " + DBSchema.studentTable.Cols.INSTRUCTOR + " = \"" + instructor.getUsername() + "\"");
                if(students.isEmpty()){
                    Toast.makeText(InstructorActivity.this,"Nothing to view. No student under this instructor",Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(InstructorActivity.this,ViewAllStudentActivity.class);
                    //intent.putExtra("instructor",instructor);
                    intent.putExtra("students",students);
                    startActivity(intent);
                }
            }
        });
    }


}