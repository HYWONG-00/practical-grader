package com.example.assignmentone;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

//RecyclerView OnClickListener (Best practice way) - But I cannot use as I do everything in one activity(unless I open new java file to write interface)
//https://www.youtube.com/watch?v=69C1ljfDvl0
//Row Click Listener Android for Beginners(Easier way actually, but not good in performance)
//https://www.youtube.com/watch?v=X5bWFcqICmw
//https://www.youtube.com/watch?v=wKFJsrdiGS8

public class ViewAllStudentActivity extends AppCompatActivity{

    public static final String STUDENTS = "students";
    private ArrayList<Student> students;
    private ArrayList<Student> backUpStudents;//cuz after search the original list will gone(cannot get from Intent also, so can only backup)
    private EditText studentNameV;
    private ImageView studentCountryI;
    private int country_id;
    private Button searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_student);

        //get the student passed from the
        students = (ArrayList<Student>) getIntent().getSerializableExtra(STUDENTS);
        //backup the list
        backUpStudents = new ArrayList<>();
        backUpStudents.addAll(students);

        RecyclerView rv= findViewById(R.id.recyclerView_students);
        //rv.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
        StudentAdapter adapter = new StudentAdapter(students);
        rv.setAdapter(adapter);//for creating the student rows
        rv.setLayoutManager(new LinearLayoutManager(ViewAllStudentActivity.this));

        studentNameV = findViewById(R.id.search_student_by_name);
        studentCountryI = findViewById(R.id.search_student_by_countryimage);
        searchBtn = findViewById(R.id.search_studentBtn);
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override//where u get your result from registration activity/other activity
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == RESULT_OK){
                    Intent returnedData = result.getData();
                    if(returnedData != null){
                        country_id = returnedData.getIntExtra("country_id",0);
                        Log.d(getApplicationContext().toString() + " We get ",String.valueOf(country_id));

                        //update the textview with the country name
                        studentCountryI.setImageResource(country_id);
                    }
                }
            }
        });
        studentCountryI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewAllStudentActivity.this,SelectCountryActivity.class);
                activityResultLauncher.launch(intent);
            }
        });

        country_id = R.drawable.flag__unknown;//set the id as flag_unknown first
        //How to update the recyclerview after something is changed
        //https://suragch.medium.com/updating-data-in-an-android-recyclerview-842e56adbfd8
        //based on the students list passed from the there, find the student lists that with same name
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    //students = (ArrayList<Student>) getIntent().getSerializableExtra(STUDENTS);//does not successfully get the original list back
                    //get the original list back
                    students.clear();
                    students.addAll(backUpStudents);
                if(!students.isEmpty()) {
                    //Log.d(this.toString() + "Searchbutton11111", students.toString());

                    String name = studentNameV.getText().toString();
                    //if field is empty,just view all the students(means just back up n directly notify the adapter)
                    if (!name.isEmpty()) {//if field is not empty
                        ArrayList<Student> newList = new ArrayList<>();
                        ListIterator<Student> iter = students.listIterator();
                        Student student = iter.next();
                        //if the first-second last student has the same name with the name in the text box
                        while (iter.hasNext()) {
                            if (student.getName().equals(name)) {
                                newList.add(student);
                            }
                            student = iter.next();
                        }
                        //if the last student has the same name with the name in the text box
                        if (student.getName().equals(name)) {
                            newList.add(student);
                        }

                        //Log.d(this.toString() + "Searchbutton", newList.toString());
                        students.clear();//clear the old students list
                        students.addAll(newList);//udpate it with "newList"
                    }
                    //or you want to search by country
                    //if country is selected and it is not "unknown country"
                    else if (country_id != R.drawable.flag__unknown) {
                        ArrayList<Student> newList = new ArrayList<>();
                        ListIterator<Student> iter = students.listIterator();
                        Student student = iter.next();
                        //if the first-second last student has the same country id as the country selected
                        while (iter.hasNext()) {
                            if (student.getCountry() == country_id) {
                                newList.add(student);
                            }
                            student = iter.next();
                        }
                        //if the last student has the same country id as the country selected
                        if (student.getCountry() == country_id) {
                            newList.add(student);
                        }

                        students.clear();//clear the old students list
                        students.addAll(newList);//udpate it with "newList"
                    }
                    adapter.notifyDataSetChanged();//notify the adapter on changed list
                }
            }
        });

    }

    private class StudentAdapter extends RecyclerView.Adapter<StudentViewHolder>{
        private ArrayList<Student> students;
        public StudentAdapter(ArrayList<Student> students){
            this.students = students;
        }

        @NonNull
        @Override
        //inflate our student row(can say make a new row I guess)
        //ViewHolder = student row in this case
        public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(ViewAllStudentActivity.this);//in this activity's layout??
            View view  = layoutInflater.inflate(R.layout.student_row,parent,false);//inflate the student row
            return new StudentViewHolder(view);
        }

        @Override
        //StudentViewHolder == hv everything(textview,imageview...) in the student row layout(student_row.xml/R.layout.student_row)
        public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
            //!!!!!!!!!!!!!!!!!REMEMBER!!!!!!!!!!!!!!!
            //NullPointerException: Attempt to invoke virtual method 'void android.widget.TextView.setText(java.lang.CharSequence)' on a null object reference in onBindViewHolder
            //Null pointer try to say that the textview is null. That can happen because the findviewbyid is not matching with the right one on your xml. Be sure your id matches on your xml and java file.
            //This is because we have to usernameV = itemView.findViewById(R.id.student_username_row); instead of usernameV = findViewById(R.id.student_username_row);.
            // Otherwise,how it know this textview id is comes from which xml file lolll

            //where we set the text for our textview in student row layout
            holder.nameV.setText(students.get(position).getName());
            holder.markV.setText(String.format("%.2f",students.get(position).getMark()));
            holder.country.setImageResource(students.get(position).getCountry());
            //I know this is redundant but idk how odi
            holder.itemView.setBackgroundColor(updateColor(students.get(position).getMark()));
        }

        @Override
        public int getItemCount() {
            return students.size();//return number of students we have in ArrayList<Student>
        }
    }

    private class StudentViewHolder extends RecyclerView.ViewHolder{
        private TextView nameV,markV;
        private ImageView country;
        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            //get the id (from student rows layout/xml file) n assign to these text views
            nameV = itemView.findViewById(R.id.student_name_row);
            markV = itemView.findViewById(R.id.student_mark_row);
            country = itemView.findViewById(R.id.student_country_row);
            //REMEMBER TO itemView.findViewById(R.id.student_username_row) instead of findViewById(R.id.student_username_row).
            //Otherwise, will get NullPointerException

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ViewAllStudentActivity.this,StudentDetailsActivity.class);
                    //Not sure if we need to pass the instructor as well, later see..
                    intent.putExtra("student",students.get(getAdapterPosition()));//pass the student clicked by user
                    startActivity(intent);
                }
            });
        }
    }

    public int updateColor(double mark){//current student's mark
        int colour = 0;
        if (mark >= 0 && mark <= 50) {
            colour = ContextCompat.getColor(this, R.color.red);
        } else if (mark >= 51 && mark <= 80) {
            colour = ContextCompat.getColor(this, R.color.yellow);
        } else if (mark >= 81 && mark <= 100) {
            colour = ContextCompat.getColor(this, R.color.green);
        }
        return colour;
    }
}