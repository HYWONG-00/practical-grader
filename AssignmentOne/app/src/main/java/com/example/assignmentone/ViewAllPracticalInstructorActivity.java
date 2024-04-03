package com.example.assignmentone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class ViewAllPracticalInstructorActivity extends AppCompatActivity {
    private Button view_instructorsBtn,view_practicalsBtn;
    private DBModel dbModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_practical_instructor);

        view_instructorsBtn = findViewById(R.id.view_instructorsBtn);
        view_practicalsBtn = findViewById(R.id.view_practicalsBtn);

        dbModel = new DBModel();
        dbModel.read(ViewAllPracticalInstructorActivity.this);

        //learn how to inflate tow different fragment in the same activity
        //https://www.youtube.com/watch?v=Btli00YA1eI
        //If you use .add(), you can only stick with one fragment layout(I guess la,not sure also) and also will get ClassCastException:
        // com.example.assignmentone.ViewAllInstructorFragment cannot be cast to com.example.assignmentone.ViewAllPracticalFragment
        //because you have casted the fragment as ViewAllInstructorFragment
        view_instructorsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*ViewAllInstructorFragment viewAllInstructorFragment =
                        (ViewAllInstructorFragment) fragmentManager.findFragmentById(R.id.fragment_container_ViewIPs);//get the fragment
                if(viewAllInstructorFragment == null){//if fragment is not exists
                    ArrayList<Instructor> instructors = dbModel.getInstructors();//get all the instructors from the instructors table
                    viewAllInstructorFragment = new ViewAllInstructorFragment(instructors);
                    fragmentManager.beginTransaction().add(R.id.fragment_container_ViewIPs, viewAllInstructorFragment).commit();
                }*/
                replaceFragment(new ViewAllInstructorFragment(dbModel.getInstructors()));
            }
        });

        view_practicalsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*ViewAllPracticalFragment viewAllPracticalFragment =
                        (ViewAllPracticalFragment)fragmentManager.findFragmentById(R.id.fragment_container_ViewIPs);
                if(viewAllPracticalFragment == null){
                    ArrayList<Practical> practicals = dbModel.getPracticals();//get all the practicals
                    viewAllPracticalFragment = new ViewAllPracticalFragment(practicals);
                    fragmentManager.beginTransaction().add(R.id.fragment_container_ViewIPs,viewAllPracticalFragment).commit();
                }*/
                replaceFragment(new ViewAllPracticalFragment(dbModel.getPracticals()));//get all the practicals
            }
        });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();//to manage the fragment/find the fragment in fragmentmanager
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_ViewIPs,fragment);//replacing(!!!NOT .add()) the framelayout with some layout/another fragment
        fragmentTransaction.commit();
    }
}