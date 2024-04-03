//how we use ActivityResultLauncher? https://www.youtube.com/watch?v=ZzQ4gIMs3iQ

package com.example.assignmentone;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    //private FrameLayout frameLayout;
    private Button login;
    private TextView signout;
    private EditText username,pin;
    private DBModel dbModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //frameLayout = findViewById(R.id.frameLayout);
        login = findViewById(R.id.loginBtn);
        signout = findViewById(R.id.signOutView);
        username = findViewById(R.id.username_main);
        pin = findViewById(R.id.pin_main);

        //Need this to get writeable database for used in MainActivity.this
        //Need to add new admin
        dbModel = new DBModel();
        dbModel.load(getApplicationContext());

        try {
            //same function as startActivityForResult()
            ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {

                @Override//where u get your result from registration activity/other activity
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK){
                        Intent returnedData = result.getData();
                        if(returnedData != null){
                            //update the thing in MainActivity with the returnedData(if got thing to update)
                            Log.d("We get the result successfully.","Yeyyy");

                            //Come back from RegistrationAct always be the Admin account
                            //1.Create an admin
                            String username = returnedData.getStringExtra("username");
                            int pinNum = returnedData.getIntExtra("pin",0);//MUST set defaultValue, else u get 'getIntExtra(java.lang.String, int)' cannot be applied to '(java.lang.String)'
                            Log.d("We get the result successfully.","username: " + username + "pin: " + pinNum);
                            Admin admin = new Admin(username,pinNum);

                            //2. Save him into the database(ensure close app n open again, we still have this admin)
                            dbModel.addAdmin(admin);
                        }
                    }
                }
            });

            signout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //CHECK IF ADMIN ACCOUNT IS NOT EXIST(DATABASE IS EMPTY??)
                    //instead of replace the fragment i will just start a new activity for result
                    //replaceFragment(new enterUsername());
                    //Lecture 4 recording - 3:30: ActivityResultLauncher(catch ActivityNotFoundException)
                    Intent intent = new Intent(MainActivity.this,RegistrationActivity.class);
                    activityResultLauncher.launch(intent);

                }
            });

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (username.getText().toString().isEmpty()) {
                        username.setError("Username cannot be empty");
                    } else if (pin.getText().toString().isEmpty()) {
                        pin.setError("PIN cannot be empty");
                    }
                    else {
                        Intent intent;
                        String user_username = username.getText().toString();
                        int user_pinNum = Integer.parseInt(pin.getText().toString());

                        SQLiteDatabase db = new DBHelper(MainActivity.this).getReadableDatabase();//get a readable database n do SELECT clause

                        Cursor admins = db.rawQuery("SELECT * FROM " + DBSchema.adminTable.NAME,null);
                        Cursor adminCursor = db.rawQuery("SELECT * FROM " + DBSchema.adminTable.NAME + " WHERE " + DBSchema.adminTable.Cols.USERNAME + " = \"" + user_username + "\"", null);//get all admin from admin table
                        Cursor instructorCursor = db.rawQuery("SELECT * FROM " + DBSchema.instructorTable.NAME + " WHERE " + DBSchema.instructorTable.Cols.USERNAME + " = \"" + user_username + "\"",null);
                        Cursor studentCursor = db.rawQuery("SELECT * FROM " + DBSchema.studentTable.NAME + " WHERE " + DBSchema.studentTable.Cols.USERNAME + " = \"" + user_username + "\"",null);
                        try {
                            if(!admins.moveToFirst()){//admins.moveToFirst() == false, means we have no admin at all
                                //then we need to jump to registration activity
                                intent = new Intent(MainActivity.this, RegistrationActivity.class);
                                activityResultLauncher.launch(intent);
                            }
                            else if (adminCursor.moveToFirst()) {//true means we have admin in admin table
                                Admin admin = new DBCursor(adminCursor).getAdmin();//directly get the first admin in the table since we only have one admin???

                                //find the admin that has matched username
                                //Cursor adminCursor = db.rawQuery("SELECT * FROM " + DBSchema.adminTable.NAME + " WHERE " + DBSchema.adminTable.Cols.USERNAME + " = \"" + user_username + "\"",null);
                                //SQLiteException: no such column: NOCLASH (code 1 SQLITE_ERROR): , while compiling: SELECT * FROM admin WHERE username = NOCLASH
                                //U get the error because here use a string to select from the database. The string is not in quotes(") so it is expected to be a column.To fix this, u need to \" to make it a correct query.

                                //Cursor adminCursor = db.query(DBSchema.adminTable.NAME, null, DBSchema.adminTable.Cols.USERNAME + "=?", new String[]{user_username}, null, null, null);
                                //keep getting CursorIndexOutOfBoundsException: Index -1 requested, with a size of 1,Error in DBCursor.java:17 & MainActivity.java:107)
                                // when trying to get the admin with matched username...

                                if (user_pinNum == admin.getPinNum()) {//and the pin number is same as the admin's pin number
                                    intent = new Intent(MainActivity.this, AdminActivity.class);
                                    intent.putExtra("admin", admin);//pass the current user:admin
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(MainActivity.this, "Incorrect PIN number.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else if(instructorCursor.moveToFirst()){//if this is not admin, check if he is instructor
                                Instructor instructor = new DBCursor(instructorCursor).getInstructor();//get the instructor
                                //check the pin number before enter instructor activity
                                if(user_pinNum == instructor.getPinNum()){
                                    intent = new Intent(MainActivity.this,InstructorActivity.class);
                                    intent.putExtra("instructor",instructor);
                                    startActivity(intent);
                                }else {
                                    Toast.makeText(MainActivity.this, "Incorrect PIN number.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else if(studentCursor.moveToFirst()){//if this is not admin and is not instructor, check if he is student
                                //if can pass the condition, means this is student
                                Student student = new DBCursor(studentCursor).getStudent();
                                //check the pin number before jump to StudentDetailsActivity
                                if(user_pinNum == student.getPinNum()){
                                    intent = new Intent(MainActivity.this,StudentDetailsActivity.class);
                                    intent.putExtra("loginStudent",student);
                                    startActivity(intent);
                                }else {
                                    Toast.makeText(MainActivity.this, "Incorrect PIN number.", Toast.LENGTH_SHORT).show();
                                }

                            }else {//if this is not admin,student,instructor, means user is not exist at all.
                                Toast.makeText(MainActivity.this,"User is not exist.", Toast.LENGTH_SHORT).show();
                            }
                        } finally {
                            admins.close();
                            adminCursor.close();
                            instructorCursor.close();
                            studentCursor.close();
                            db.close();
                        }

                    }
                }
            });

        }catch(ActivityNotFoundException e){
            e.printStackTrace();
        }

    }
}