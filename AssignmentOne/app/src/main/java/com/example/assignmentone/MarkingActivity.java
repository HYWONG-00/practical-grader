package com.example.assignmentone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

//How to pass data/update data between two fragments
//https://www.youtube.com/watch?v=i22INe14JUc


public class MarkingActivity extends AppCompatActivity implements StudentsListFragment.FragmentStudentsListsListener, PracticalsListFragment.FragmentPracticalsListsListener {//must implement this interface to get data from fragmentB
    private DBModel dbModel;
    private MarkingDetailsFunc6Fragment markDetailsFragment;
    private StudentsListFragment studentsListFragment;
    private PracticalsListFragment practicalsListFragment;
    private Instructor instructor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marking);

        dbModel = new DBModel();
        dbModel.read(getApplicationContext());

        instructor = (Instructor) getIntent().getSerializableExtra("instructor");

        FragmentManager frag = getSupportFragmentManager();
        markDetailsFragment = (MarkingDetailsFunc6Fragment)frag.findFragmentById(R.id.fragmentA_functionality6);
        if (markDetailsFragment == null){
            //if admin, can just pass whole student list
            //if instructor, can only pass the student list belongs to him
            markDetailsFragment = new MarkingDetailsFunc6Fragment();
            frag.beginTransaction().add(R.id.fragmentA_functionality6, markDetailsFragment).commit();
        }
        studentsListFragment = (StudentsListFragment) frag.findFragmentById(R.id.fragmentB_functionality6);
        if (studentsListFragment == null){
            //if admin, can just pass whole student list
            ArrayList<Student> students = null;
            if(instructor == null){
                students = dbModel.getStudents();
            }
            //if instructor, can only pass the student list belongs to him
            else{
                String query = "SELECT * FROM " + DBSchema.studentTable.NAME
                        + " WHERE " + DBSchema.studentTable.Cols.INSTRUCTOR + " = '" + instructor.getUsername() + "'";
                students = dbModel.getStudentsByQuery(query);
                if(students == null){//if student list is empty
                    students = new ArrayList<>();//just pass a simple empty list to StudentListFragment??
                    // (Otherwise,NullPointerException for getItemCount()....
                }
            }

            studentsListFragment = new StudentsListFragment(students,getApplicationContext());
            frag.beginTransaction().add(R.id.fragmentB_functionality6, studentsListFragment).commit();
        }

        practicalsListFragment = (PracticalsListFragment) frag.findFragmentById(R.id.fragmentC_functionality6);
        if(practicalsListFragment == null){
            //if admin, can just pass whole practical list
            ArrayList<Practical> practicals = null;
            if(instructor == null){
                practicals = dbModel.getPracticals();
            }
            //if instructor, can only pass the practical list belongs to him
            else{
                String query = "SELECT * FROM " + DBSchema.practicalTable.NAME
                        + " WHERE " + DBSchema.practicalTable.Cols.INSTRUCTOR + " = \"" + instructor.getUsername() + "\"";
                practicals = dbModel.getPracticalsByQuery(query);
                if(practicals == null){//if practical list is empty
                    practicals = new ArrayList<>();//just pass a simple empty list to PracticalListFragment??
                    // (Otherwise,NullPointerException for getItemCount()....
                }
            }
            practicalsListFragment = new PracticalsListFragment(practicals);
            frag.beginTransaction().add(R.id.fragmentC_functionality6, practicalsListFragment).commit();
        }
    }

    //implement the method in interface(StudentsListFragment.FragmentStudentsListsListener)
    @Override
    public void onInputSentStudent(Student student) {//student = the one user clicked in the recyclerview
        markDetailsFragment.updateStudentUsername(student);
    }

    @Override
    public void onInputSentPractical(Practical practical) {
        markDetailsFragment.updatePracticalTitle(practical);
    }
}