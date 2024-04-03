package com.example.assignmentone;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MarkingDetailsFunc6Fragment extends Fragment {

    private TextView student_username,practical_title;
    private EditText mark;
    private Button addMarkB;
    private Practical practical;
    private Student student;
    //set our fragment layout
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_marking_details_func6, container, false);
        student_username = view.findViewById(R.id.student_username_functionality6);
        practical_title = view.findViewById(R.id.practical_title_functionality6);

        mark = view.findViewById(R.id.mark_functionality6);

        addMarkB = view.findViewById(R.id.add_new_markBtn_functionality6);

        student = null;
        practical = null;

        addMarkB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //How to make alert box
                //No need to set anything in layout just need this code..
                //https://www.youtube.com/watch?v=lvoKGGErwhw
                if(student == null){
                    //student_username.setError("Please select a student.");
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Please select a student").
                            setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Do nothing
                                }
                            });
                    builder.create().show();
                }else if(practical == null){
                    //practical_title.setError("Please select a practical.");
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Please select a practical").
                            setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Do nothing
                                }
                            });
                    builder.create().show();
                }else {
                    if(!mark.getText().toString().isEmpty()) {//ensure the mark box is not empty
                            DBModel dbModel = new DBModel();
                            dbModel.read(getContext());
                            Practical practical = dbModel.getPracticalByQuery("SELECT * FROM " + DBSchema.practicalTable.NAME
                                    + " WHERE " + DBSchema.practicalTable.Cols.TITLE + " = \"" + practical_title.getText().toString() + "\"");
                            if(student.getInstructor().equals(practical.getInstructor())) {
                                if (Double.compare(practical.getMark(), Double.parseDouble(mark.getText().toString())) >= 0) {//ensure the mark assign to the student will not exceed the mark of the practical

                                    //practical.getMark() is OLD one, we need to pass new mark instead
                                    //double newMark = calculateMark(Double.parseDouble(mark.getText().toString()),practical.getMark());
                                    double newMark = Double.parseDouble(mark.getText().toString());
                                    Practical newPrac = new Practical(practical.getTitle(), practical.getDescription(), newMark, practical.getInstructor());
                                    dbModel.addOrUpdatePracticalMark(student_username.getText().toString(), newPrac);
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    builder.setMessage("Mark set for student cannot be greater than the maximum mark " + practical.getMark())
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //Do nothing
                                                }
                                            });
                                    builder.create().show();
                                }
                            }
                            else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setMessage("Fail to set mark: This practical belongs to " + practical.getInstructor() + ". The student is under " + student.getInstructor() + ".")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //Do nothing
                                            }
                                        });
                                builder.create().show();
                            }
                    }
                }
            }
        });

        return view;
    }

    public void updateStudentUsername(Student student){
        student_username.setText(student.getUsername());
        this.student = student;
    }

    public void updatePracticalTitle(Practical practical){
        practical_title.setText(practical.getTitle());
        this.practical = practical;
    }

    /*//calculate the mark for the practical
    public static double calculateMark(double mark,double totalMark){
        double newMark = mark/totalMark * 100;
        return newMark;
    }*/
}