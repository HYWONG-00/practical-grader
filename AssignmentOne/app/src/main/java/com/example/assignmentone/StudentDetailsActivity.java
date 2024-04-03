package com.example.assignmentone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
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
import android.widget.Toast;

import java.util.ArrayList;

public class StudentDetailsActivity extends AppCompatActivity {

    private TextView usernameV,markV,pinV,nameV,emailV,instructorV,practicalV;
    private EditText newMarkE;
    private ImageView countryImage;
    private Button UDStudentBtn,updateMarkBtn,deleteMarkBtn;
    private RecyclerView recyclerViewPracticals;
    private DBModel dbModel;
    private Student student;
    private Practical practical;//set when onClick.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);

        usernameV = findViewById(R.id.student_details_username);
        pinV = findViewById(R.id.student_details_pin);
        nameV = findViewById(R.id.student_details_name);
        emailV= findViewById(R.id.student_details_email);
        markV = findViewById(R.id.student_details_mark);
        instructorV = findViewById(R.id.student_details_instructor);
        countryImage = findViewById(R.id.imageView_student_details__country);
        UDStudentBtn = findViewById(R.id.UD_student_detailsBtn);

        practicalV = findViewById(R.id.student_details_practical_title);
        newMarkE = findViewById(R.id.student_details_practical_mark);
        updateMarkBtn = findViewById(R.id.update_student_mark);
        deleteMarkBtn = findViewById(R.id.delete_student_mark);
        recyclerViewPracticals = findViewById(R.id.recyclerview_practicals);

        //pass from ViewALLStudentActivity.class(itemView.onClick)
        this.student = (Student)getIntent().getSerializableExtra("student");

        //Funcitionality 10
        //pass from MainActivity,Login(if this is student)
        if(student == null){//u r not come from ViewAllStudentActivity
            student = (Student)getIntent().getSerializableExtra("loginStudent");
            //disable all of the button as it should be view only
            UDStudentBtn.setEnabled(false);
            updateMarkBtn.setEnabled(false);
            deleteMarkBtn.setEnabled(false);
            newMarkE.setEnabled(false);
        }
        //End

        //set the contents for this student
        usernameV.setText(student.getUsername());
        pinV.setText(String.valueOf(student.getPinNum()));
        nameV.setText(student.getName());
        emailV.setText(student.getEmail());
        markV.setText(String.format("%.2f",student.getMark()));
        instructorV.setText(student.getInstructor());
        countryImage.setImageResource(student.getCountry());

        //used for update and delete the practical mark
        dbModel = new DBModel();
        dbModel.load(this);

        UDStudentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AEDStudentActivity.class);
                //Not sure if we need to pass the instructor as well, later see..
                intent.putExtra("student",student);
                startActivity(intent);
            }
        });


        PracticalAdapter adapter = new PracticalAdapter(student.getPracticals());
        recyclerViewPracticals.setAdapter(adapter);//for creating the student rows
        recyclerViewPracticals.setLayoutManager(new LinearLayoutManager(this));

        updateMarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(practical == null){
                    alert("Please select a practical");
                }else{
                    if(!markV.getText().toString().isEmpty()) {//ensure the mark box is not empty
                        //find the practical(with max mark)
                        Practical originalPrac = dbModel.getPracticalByQuery("SELECT * FROM " + DBSchema.practicalTable.NAME
                                + " WHERE " + DBSchema.practicalTable.Cols.TITLE + " = \"" + practical.getTitle() + "\"");
                        Log.d(getApplicationContext().toString(),originalPrac.toString());
                        Log.d(getApplicationContext().toString(),String.valueOf(originalPrac.getMark()));
                        Log.d(getApplicationContext().toString(),newMarkE.getText().toString());
                        if (Double.compare(originalPrac.getMark(),Double.parseDouble(newMarkE.getText().toString().trim())) >= 0) {//ensure the mark assign to the student will not exceed the mark of the practical
                            //practical.getMark() is OLD one, we need to pass new mark instead
                            double newMark = Double.parseDouble(newMarkE.getText().toString());
                            Practical newPrac = new Practical(originalPrac.getTitle(),originalPrac.getDescription(), newMark,originalPrac.getInstructor());
                            dbModel.addOrUpdatePracticalMark(usernameV.getText().toString(), newPrac);
                        } else {
                            alert("Mark set for student cannot be greater than the maximum mark " +  originalPrac.getMark());
                        }
                    }
                }
            }
        });

        deleteMarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(practical == null){
                    alert("Please select a practical");
                }else{
                    if(!markV.getText().toString().isEmpty()) {//ensure the mark box is not empty
                        //find the practical(with max mark)
                        Practical originalPrac = dbModel.getPracticalByQuery("SELECT * FROM " + DBSchema.practicalTable.NAME
                                + " WHERE " + DBSchema.practicalTable.Cols.TITLE + " = \"" + practical.getTitle() + "\"");
                        Log.d(getApplicationContext().toString(),originalPrac.toString());
                        Log.d(getApplicationContext().toString(),String.valueOf(originalPrac.getMark()));
                        Log.d(getApplicationContext().toString(),newMarkE.getText().toString());
                        if (Double.compare(originalPrac.getMark(),Double.parseDouble(newMarkE.getText().toString().trim())) >= 0) {//ensure the mark assign to the student will not exceed the mark of the practical
                            //practical.getMark() is OLD one, we need to pass new mark instead
                            //double newMark = calculateMark(Double.parseDouble(newMarkE.getText().toString().trim()),originalPrac.getMark());
                            double newMark =Double.parseDouble(newMarkE.getText().toString().trim());
                            Practical newPrac = new Practical(originalPrac.getTitle(),originalPrac.getDescription(), newMark,originalPrac.getInstructor());
                            dbModel.deletePracticalMark(usernameV.getText().toString(), newPrac);
                        } else {
                            alert("Mark set for student cannot be greater than the maximum mark " +  originalPrac.getMark());
                        }
                    }

                }
            }
        });
    }

    public void alert(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(StudentDetailsActivity.this);
        builder.setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do nothing
                    }
                });
        builder.create().show();
    }

    /*//calculate the mark for the practical
    public static double calculateMark(double mark,double totalMark){
        double newMark = mark/totalMark * 100;
        return newMark;
    }*/

    private class PracticalAdapter extends RecyclerView.Adapter<PracticalViewHolder>{
        private ArrayList<Practical> practicals;
        public PracticalAdapter(ArrayList<Practical> practicals){this.practicals = practicals;}
        @NonNull
        @Override
        public PracticalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            View view = layoutInflater.inflate(R.layout.practical_row,parent,false);
            return new PracticalViewHolder(view,practicals);
        }

        @Override
        public void onBindViewHolder(@NonNull PracticalViewHolder holder, int position) {
            holder.title.setText(practicals.get(position).getTitle());
            holder.description.setText(practicals.get(position).getDescription());
            holder.instructor.setText(practicals.get(position).getInstructor());
            holder.mark.setText(String.format("%.2f",practicals.get(position).getMark()));
        }

        @Override
        public int getItemCount() {
            return practicals.size();
        }
    }

    private class PracticalViewHolder extends RecyclerView.ViewHolder{
        private TextView title,description,instructor,mark;
        private ArrayList<Practical> practicals;
        public PracticalViewHolder(@NonNull View itemView,ArrayList<Practical> practicals) {
            super(itemView);
            this.practicals = practicals;
            title = itemView.findViewById(R.id.practical_title_row);
            description = itemView.findViewById(R.id.practical_description_row);
            instructor = itemView.findViewById(R.id.practical_instructor_row);
            mark = itemView.findViewById(R.id.practical_mark_row);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    practical = practicals.get(getAdapterPosition());
                    practicalV.setText(practicals.get(getAdapterPosition()).getTitle());
                }
            });
        }
    }
}