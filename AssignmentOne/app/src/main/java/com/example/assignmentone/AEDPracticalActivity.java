package com.example.assignmentone;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AEDPracticalActivity extends AppCompatActivity {

    private Button addPracB,updatePracB,deletePracB;
    private EditText pracTitleV,pracDescriptionV,pracMarkV,insUsernameV;
    private DBModel dbModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aedpractical);
        setTitle("Add/Update/Delete Practical");

        addPracB = findViewById(R.id.add_practicalBtn);
        updatePracB = findViewById(R.id.udpate_practicalBtn);
        deletePracB = findViewById(R.id.delete_practicalBtn);

        pracTitleV = findViewById(R.id.practical_title);
        pracDescriptionV = findViewById(R.id.practical_description);
        pracMarkV = findViewById(R.id.practical_mark);
        insUsernameV = findViewById(R.id.instructor_username);

        dbModel = new DBModel();
        dbModel.load(getApplicationContext());

        //This is for updating/deleting the practical when view and click it(Functionality 7)
        Practical practical = (Practical) getIntent().getSerializableExtra("practical");
        if(practical != null){//means you are entered from Functionality 7(click the practical row)

            //ensure it does not have mark b4 edit n delete
            if(practical.getMark() != Practical.NO_MARK) {
                Toast.makeText(AEDPracticalActivity.this,"Practical cannot be updated/deleted once the mark is set.",
                        Toast.LENGTH_SHORT).show();
                updatePracB.setEnabled(false);
                deletePracB.setEnabled(false);
            }
            //disable the add button and the practical title first
            addPracB.setEnabled(false);
            pracTitleV.setEnabled(false);//remember Func 5 says edit and delete is possible only when no mark for this prac
            //show all of the details(Func 7 want)
            pracTitleV.setText(practical.getTitle());
            pracDescriptionV.setText(practical.getDescription());
            pracMarkV.setText(String.valueOf(practical.getMark()));
            insUsernameV.setText(practical.getInstructor());
        }
        //End


            addPracB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //checking the field before proceed
                    if(pracTitleV.getText().toString().isEmpty()){
                        pracTitleV.setError("Title cannot be empty");
                    }else {
                        Practical prac = null;
                        String title = pracTitleV.getText().toString();
                        String description = pracDescriptionV.getText().toString();
                        String username = insUsernameV.getText().toString();
                        if (validateInstructor(username)) {
                            if (pracMarkV.getText().toString().equals("")) {//means no mark input for the practical
                                prac = new Practical(title, description, Practical.NO_MARK, username);
                            } else {
                                double mark = Double.parseDouble(pracMarkV.getText().toString());
                                prac = new Practical(title, description, mark, username);
                            }
                            //add it into the database..............
                            dbModel.addPractical(prac);//insert the practical into the database
                        } else {
                            insUsernameV.setError("Invalid instructor's username");
                        }
                    }
                }
            });

            //check the practical mark is assigned or not
            //using db.query to find the practical(by using title?i also not sure)...
            //if prac.noMark() = true, means no mark, we should disable the udpate n delete button
            updatePracB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //checking the field before proceed
                    if(pracTitleV.getText().toString().isEmpty()){
                        pracTitleV.setError("Title cannot be empty");
                    }else {
                        Practical prac = null;
                        String title = pracTitleV.getText().toString();
                        String description = pracDescriptionV.getText().toString();
                        String username = insUsernameV.getText().toString();
                        if (validateInstructor(username)) {
                            if (pracMarkV.getText().toString().equals("")) {//means no mark input for the practical
                                prac = new Practical(title, description, Practical.NO_MARK, username);
                            } else {
                                double mark = Double.parseDouble(pracMarkV.getText().toString());
                                prac = new Practical(title, description, mark, username);
                            }
                            //update into database
                            dbModel.updatePractical(prac);
                        } else {
                            insUsernameV.setError("Invalid instructor's username");
                        }
                    }
                }
            });

            deletePracB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //checking the field before proceed
                    if(pracTitleV.getText().toString().isEmpty()){
                        pracTitleV.setError("Title cannot be empty");
                    }
                    else {
                        Practical prac = null;
                        String title = pracTitleV.getText().toString();
                        String description = pracDescriptionV.getText().toString();
                        if (pracMarkV.getText().toString().equals("")) {//means no mark input for the practical
                            prac = new Practical(title, description, Practical.NO_MARK, null);//null as instructor username is not important for deleting practical
                        } else {
                            double mark = Double.parseDouble(pracMarkV.getText().toString());
                            prac = new Practical(title, description, mark, null);
                        }
                        //delete into database
                        dbModel.deletePractical(prac);
                    }
                }
            });
    }
    //ensure the instructor is exist in the database before add the instructor username in the practical
    //true means instructor is exist
    private boolean validateInstructor(String username){
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
}