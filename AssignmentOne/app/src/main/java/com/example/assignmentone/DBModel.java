package com.example.assignmentone;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

import com.example.assignmentone.DBSchema.adminTable;
import com.example.assignmentone.DBSchema.studentTable;
import com.example.assignmentone.DBSchema.instructorTable;
import com.example.assignmentone.DBSchema.practicalTable;

public class DBModel implements Serializable {
    private SQLiteDatabase db;
    private Context context;

    public void load(Context context){

        this.db = new DBHelper(context).getWritableDatabase();
        this.context = context;
    }

    public void read(Context context){

        this.db = new DBHelper(context).getReadableDatabase();
        this.context = context;
    }

    public void addAdmin(Admin admin){
        try
        {
        if (getAdminByQuery("SELECT * FROM " + adminTable.NAME
                + " WHERE " + adminTable.Cols.USERNAME + " = \"" + admin.getUsername() + "\"") != null) {
            throw new SQLiteConstraintException("Adding duplicate admin username is not allowed.");
        }
        else {
            ContentValues cv = new ContentValues();
            cv.put(adminTable.Cols.USERNAME, admin.getUsername());
            cv.put(adminTable.Cols.PIN, admin.getPinNum());

            //insert the student into database
            long result = db.insert(adminTable.NAME, null, cv);
            //nullColumnHack: if any column does not have values/being set with a values, help me put it as null

            //returns the row ID of newly inserted row, -1 means error occured
            if (result == -1) {
                Toast.makeText(context, "Failed to add admin.", Toast.LENGTH_SHORT).show();
                //throw new SQLiteConstraintException("Admin has been added before.");
            } else {
                Toast.makeText(context, "Admin added successfully.", Toast.LENGTH_SHORT).show();
            }
        }
        }catch(SQLiteConstraintException e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void addInstructor(Instructor instructor){
        try {
            if (getInstructorByQuery("SELECT * FROM " + instructorTable.NAME
                    + " WHERE " + instructorTable.Cols.USERNAME + " = \"" + instructor.getUsername() + "\"") != null) {
                throw new SQLiteConstraintException("Adding duplicate instructor username is not allowed.");
            } else {
                ContentValues cv = new ContentValues();
                cv.put(instructorTable.Cols.USERNAME, instructor.getUsername());
                cv.put(instructorTable.Cols.PIN, instructor.getPinNum());
                cv.put(instructorTable.Cols.NAME, instructor.getName());
                cv.put(instructorTable.Cols.EMAIL, instructor.getEmail());
                cv.put(instructorTable.Cols.COUNTRY, instructor.getCountry());
                long result = db.insert(DBSchema.instructorTable.NAME, null, cv);

                if (result == -1) {
                    Toast.makeText(context, "Failed to add instructor.", Toast.LENGTH_SHORT).show();
                    //throw new SQLiteConstraintException("Instructor has been added before.");
                } else {
                    Toast.makeText(context, "Instructor added successfully.", Toast.LENGTH_SHORT).show();
                    Log.d("addInstructor: ", instructor.toString());
                }
            }
        }catch(SQLiteConstraintException e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //To know how to use delete()...
    //https://stackoverflow.com/questions/7510219/deleting-row-in-sqlite-in-android
    public void deleteInstructor(Instructor instructor){
        String[] whereValue = {String.valueOf(instructor.getUsername())};
        //delete instructor by its username, since username is unique(will not delete wrong people)
        int result = db.delete(DBSchema.instructorTable.NAME,DBSchema.instructorTable.Cols.USERNAME + "=? ",whereValue);

        //Notify the user what's going on
        if(result == 1){//number of rows affected should be = 1 as username is unique
            Toast.makeText(context,"Instructor:(Username: " + instructor.getUsername() + ") is deleted successfully",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context,"Failed to delete.",Toast.LENGTH_SHORT).show();
        }
    }

    //combination between add and delete
    public void updateInstructor(Instructor instructor){
        ContentValues cv = new ContentValues();
        cv.put(instructorTable.Cols.USERNAME,instructor.getUsername());
        cv.put(instructorTable.Cols.PIN,instructor.getPinNum());
        cv.put(instructorTable.Cols.NAME,instructor.getName());
        cv.put(instructorTable.Cols.EMAIL,instructor.getEmail());
        cv.put(instructorTable.Cols.COUNTRY,instructor.getCountry());
        //Table name, condition, answer the condition/value for condition
        int result = db.update(DBSchema.instructorTable.NAME,cv,DBSchema.instructorTable.Cols.USERNAME+ "=?", new String[] {String.valueOf(instructor.getUsername())});

        if(result == 1){//number of rows affected should be = 1 as username is unique
            Toast.makeText(context,"Updated successfully",Toast.LENGTH_SHORT).show();
            Log.d(context.toString(), instructor.toString());
        }
        else{
            Toast.makeText(context,"Failed to udpate.",Toast.LENGTH_SHORT).show();
        }
    }

    public void addStudent(Student student){
        try {
            if (getStudentByQuery("SELECT * FROM " + studentTable.NAME
                    + " WHERE " + studentTable.Cols.USERNAME + " = \"" + student.getUsername() + "\"") != null) {
                throw new SQLiteConstraintException("Adding duplicate student username is not allowed.");
            } else {
                ContentValues cv = new ContentValues();
                cv.put(studentTable.Cols.USERNAME, student.getUsername());
                cv.put(studentTable.Cols.PIN, student.getPinNum());
                cv.put(studentTable.Cols.NAME, student.getName());
                cv.put(studentTable.Cols.EMAIL, student.getEmail());
                cv.put(studentTable.Cols.COUNTRY, student.getCountry());
                cv.put(studentTable.Cols.INSTRUCTOR, student.getInstructor());
                cv.put(studentTable.Cols.MARK, student.getMark());
                cv.put(studentTable.Cols.PRACTICALS, serializeObject(student.getPracticals()));//convert the ArrayList<Practicla> to byte[] b4 storing
                long result = db.insert(DBSchema.studentTable.NAME, null, cv);

                //returns the row ID of newly inserted row, -1 means error occured
                if (result == -1) {
                    Toast.makeText(context, "Failed to add student.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Student added successfully.", Toast.LENGTH_SHORT).show();
                    Log.d(context.toString(), student.toString());
                }
            }
        }catch(SQLiteConstraintException e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteStudent(Student student){
        String[] whereValue = {String.valueOf(student.getUsername())};
        //delete student by its username, since username is unique(will not delete wrong people)
        int result = db.delete(DBSchema.studentTable.NAME,DBSchema.studentTable.Cols.USERNAME + "=? ",whereValue);
        //return number of rows affected if a where clause passed in

        //Notify the user what's going on
        if(result == 1){//number of rows affected should be = 1 as username is unique
            Toast.makeText(context,"Student:(Username: " + student.getUsername() + ") is deleted successfully.",Toast.LENGTH_SHORT).show();
            Log.d(context.toString(), student.toString());
        }
        else{
            Toast.makeText(context,"Failed to delete.",Toast.LENGTH_SHORT).show();
        }
    }

    public void updateStudent(Student student){
        ContentValues cv = new ContentValues();
        cv.put(studentTable.Cols.USERNAME,student.getUsername());
        cv.put(studentTable.Cols.PIN,student.getPinNum());
        cv.put(studentTable.Cols.NAME,student.getName());
        cv.put(studentTable.Cols.EMAIL,student.getEmail());
        cv.put(studentTable.Cols.COUNTRY,student.getCountry());
        cv.put(studentTable.Cols.INSTRUCTOR,student.getInstructor());
        cv.put(studentTable.Cols.MARK,student.getMark());
        cv.put(studentTable.Cols.PRACTICALS,serializeObject(student.getPracticals()));//convert the ArrayList<Practicla> to byte[] b4 storing
        //Table name, condition, answer the condition/value for condition
        int result = db.update(DBSchema.studentTable.NAME,cv,DBSchema.studentTable.Cols.USERNAME+ "=?", new String[] {String.valueOf(student.getUsername())});
        if(result == 1){//number of rows affected should be = 1 as username is unique
            Toast.makeText(context,"Student updated successfully.",Toast.LENGTH_SHORT).show();
            Log.d(context.toString(), student.toString());
        }
        else{
            Toast.makeText(context,"Failed to udpate.",Toast.LENGTH_SHORT).show();
        }
    }

    public void addPractical(Practical practical){
        try {
            if (getPracticalByQuery("SELECT * FROM " + practicalTable.NAME
                    + " WHERE " + practicalTable.Cols.TITLE + " = \"" + practical.getTitle() + "\"") != null) {
                throw new SQLiteConstraintException("Adding duplicate practical title is not allowed.");
            }else {
                ContentValues cv = new ContentValues();
                cv.put(practicalTable.Cols.TITLE, practical.getTitle());
                cv.put(practicalTable.Cols.DESCRIPTION, practical.getDescription());
                cv.put(practicalTable.Cols.MARK, practical.getMark());
                cv.put(practicalTable.Cols.INSTRUCTOR, practical.getInstructor());

                long result = db.insert(DBSchema.practicalTable.NAME, null, cv);

                //returns the row ID of newly inserted row, -1 means error occured
                if (result == -1) {
                    Toast.makeText(context, "Failed to add practical.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Practical added successfully.", Toast.LENGTH_SHORT).show();
                    Log.d(context.toString(), practical.toString());
                }
            }
        }catch(SQLiteConstraintException e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void deletePractical(Practical practical){
        //!!!!!MUST ADD THE "" or '' if you search a string in database
        String query = "SELECT * FROM " + practicalTable.NAME + " WHERE " + practicalTable.Cols.TITLE + " = \"" + practical.getTitle() + "\"";
        DBCursor dbCursor = new DBCursor(db.rawQuery(query,null));
        if(dbCursor.moveToFirst()) {
            //delete practical is allowed only when the practical has no mark
            Log.d(context.toString() + "deletePractical", practical.hasNoMark() + "");
            if (practical.hasNoMark()) {
                String[] whereValue = {String.valueOf(practical.getTitle())};
                int result = db.delete(practicalTable.NAME, practicalTable.Cols.TITLE + "=? ", whereValue);
                //return number of rows affected if a where clause passed in

                //Notify the user what's going on
                if (result == 1) {//number of rows affected should be = 1 as username is unique
                    Toast.makeText(context, "Practical:(Title: " + practical.getTitle() + ") is deleted successfully.", Toast.LENGTH_SHORT).show();
                    Log.d(context.toString(), practical.toString());
                } else {
                    Toast.makeText(context, "Failed to delete.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Cannot delete as the practical has mark.", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(context, "This practical is not exist before.", Toast.LENGTH_SHORT).show();
        }

    }

    public void updatePractical(Practical practical){
        //!!!!!MUST ADD THE "" or '' if you search a string in database
        String query = "SELECT * FROM " + practicalTable.NAME + " WHERE " + practicalTable.Cols.TITLE + " = \"" + practical.getTitle() + "\"";
        DBCursor dbCursor = new DBCursor(db.rawQuery(query,null));
        if(dbCursor.moveToFirst()) {
            Practical oldPractical = dbCursor.getPractical();
            Log.d("updatePractical", oldPractical.hasNoMark() + "");
            //update is allowed only when the old practical has no mark
            if (oldPractical.hasNoMark()) {
                ContentValues cv = new ContentValues();
                cv.put(practicalTable.Cols.TITLE, practical.getTitle());
                cv.put(practicalTable.Cols.DESCRIPTION, practical.getDescription());
                cv.put(practicalTable.Cols.MARK, practical.getMark());
                cv.put(practicalTable.Cols.INSTRUCTOR,practical.getInstructor());
                //Table name, condition, answer the condition/value for condition
                int result = db.update(practicalTable.NAME, cv, practicalTable.Cols.TITLE + "=?", new String[]{String.valueOf(practical.getTitle())});
                if (result == 1) {//number of rows affected should be = 1 as username is unique
                    Toast.makeText(context, "Updated successfully.", Toast.LENGTH_SHORT).show();
                    Log.d(context.toString(), practical.toString());
                } else {
                    Toast.makeText(context, "Failed to udpate.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Cannot update as practical has mark:" + oldPractical.getMark(), Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(context, "This practical is not exist before.", Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<Student> getStudents(){
        ArrayList<Student> studentList = new ArrayList<>();
        //query() return the entire table u want in cursor
        //How do I order my SQLITE database in descending order, for an android app?
        //you just need to specify on what column you want to do orderBy +"ASC" (or) orderBy +"DESC"(yourColumn+" DESC")
        String query = "SELECT * FROM " + studentTable.NAME + " ORDER BY " + studentTable.Cols.NAME;
        Cursor cursor = db.rawQuery(query,null);//db.query(studentTable.NAME,null,null,null,null,null,null);
        DBCursor studentDBCursor = new DBCursor(cursor);

        try{
            studentDBCursor.moveToFirst();
            while(!studentDBCursor.isAfterLast()){
                studentList.add(studentDBCursor.getStudent());
                studentDBCursor.moveToNext();
            }
        }
        finally {
            cursor.close();
            //studentDBCursor.close();//no need to close
        }
        return studentList;
    }

    public ArrayList<Instructor> getInstructors(){
        ArrayList<Instructor> instructorList = new ArrayList<>();
        Cursor cursor = db.query(instructorTable.NAME,null,null,null,null,null,null);
        DBCursor instructorDBCursor = new DBCursor(cursor);

        try{
            instructorDBCursor.moveToFirst();
            while(!instructorDBCursor.isAfterLast()){
                instructorList.add(instructorDBCursor.getInstructor());
                instructorDBCursor.moveToNext();
            }
            /*//will fail if the table is empty,maybe u try with while loop
            do{
                instructorList.add(instructorDBCursor.getInstructor());
            }while(instructorDBCursor.moveToNext());*/
        }
        finally {
            cursor.close();
        }
        return instructorList;
    }

    public ArrayList<Practical> getPracticals(){
        ArrayList<Practical> practicalList = new ArrayList<>();
        Cursor cursor = db.query(practicalTable.NAME,null,null,null,null,null,null);
        DBCursor dbCursor = new DBCursor(cursor);

        try{
            dbCursor.moveToFirst();
            while(!dbCursor.isAfterLast()){
                practicalList.add(dbCursor.getPractical());
                dbCursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }
        return practicalList;
    }

    //when edit the mark,update that practical mark
    //when delete the mark,set the practical mark back to -1.0.I know it is not good but...
    public void addOrUpdatePracticalMark(String username,Practical practical){//username,practical to add.practical from instructor
        String query = "SELECT * FROM " + studentTable.NAME + " WHERE " + studentTable.Cols.USERNAME + " = \"" + username + "\"";
        DBCursor studentCursor = new DBCursor(db.rawQuery(query,null));
        if(studentCursor.moveToFirst()){//if we find this student
            Student student = studentCursor.getStudent();//get the student with ArrayList<Practical> practicals array
            Log.d(context.toString() + "addOrUpdatePracticalMark",student.toString());
            Practical returnedPrac = student.getPractical(practical.getTitle());//get old practical from student(want to cancel it if there is)
            String alert = "";
            //if we does not find the practical from student's practical list,
            if(returnedPrac == null){
                student.addPractical(practical);//insert this practical into the list
                alert += "This practical has been added for student.\n";
            }
            else{//if we have this practical odi
                //returnedPrac.setMark(practical.getMark());//we just get the practical and update the mark//NO NEED AS IT DOESN'T WORK AT ALL
                //I'M NOT SURE WILL THIS UPDATE THE PRACTICAL IN STUDENT OR WE NEED TO SET IT BACK INTO THE PRACTICAL LIST. ANSWER: NO
                student.removePractical(returnedPrac);//delete the old one
                student.addPractical(practical);//add the new one
                Log.d(context.toString() + "addOrUpdatePracticalMark",student.toString());
            }
            //update the total mark of the student
            updateAverageMark(student);

            //then update it in the database
            updateStudent(student);
            //tell the user update successfully
            alert += "Mark udpate successfully.";
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(alert).
                    setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Do nothing
                        }
                    });
            builder.create().show();
        }
        else{//if we cannot find the student
            //ACTUALLY, it is IMPOSSIBLE to not find the student as we show the student list in the fragment below
            // (u can click the student = the student 100% exists)
            //just do nothing
        }
    }

    public void deletePracticalMark(String username,Practical practical){
        String query = "SELECT * FROM " + studentTable.NAME + " WHERE " + studentTable.Cols.USERNAME + " = \"" + username + "\"";
        DBCursor studentCursor = new DBCursor(db.rawQuery(query,null));
        if(studentCursor.moveToFirst()){//if we find this student
            Student student = studentCursor.getStudent();//get the student with ArrayList<Practical> practicals array
            Practical returnedPrac = student.getPractical(practical.getTitle());//get old practical from student(want to cancel it if there is)
            //if we found the practical from student's practical list,
            if(returnedPrac != null){
                //update the total mark of the student before updating the practical list and inserting into database
                student.removePractical(returnedPrac);//delete the old one
            }
            //update the total mark of the student
            updateAverageMark(student);

            //then update it in the database
            updateStudent(student);
            //tell the user delete successfully
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Practical is removed.Mark delete successfully.").
                    setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Do nothing
                        }
                    });
            builder.create().show();
        }
        else{}//if we cannot find the student
        //ACTUALLY, it is IMPOSSIBLE to not find the student as we show the student list in the fragment below
        // (u can click the student = the student 100% exists)
        //just do nothing
    }

    public void updateAverageMark(Student student){
        ArrayList<Practical> practicals = student.getPracticals();
        if(!practicals.isEmpty()) {
            double newMark = 0.0;
            Iterator<Practical> iter = practicals.iterator();
            Practical practical = iter.next();
            Practical originalPractical = getPracticalByQuery("SELECT * FROM " + practicalTable.NAME
                    + " WHERE " + practicalTable.Cols.TITLE + " = \"" + practical.getTitle() + "\"");
            while (iter.hasNext()) {
                //Log.d(context.toString() + "NOOOOOO",String.valueOf(newMark));
                newMark += (practical.getMark() / originalPractical.getMark());
                practical = iter.next();
                originalPractical = getPracticalByQuery("SELECT * FROM " + practicalTable.NAME
                        + " WHERE " + practicalTable.Cols.TITLE + " = \"" + practical.getTitle() + "\"");
            }
            //Log.d(context.toString() + "NOOOOOO",String.valueOf(newMark));
            newMark += (practical.getMark() / originalPractical.getMark());
            if(practicals.size() != 0) {
                student.setMark(newMark / practicals.size() * 100);
            }
            else{
                student.setMark(Student.NO_MARK);//cuz if practicals.size() = 0 means no practical, then how can u hv mark
            }
        }
        else{
            student.setMark(Student.NO_MARK);
        }
    }

    public Practical getPracticalByQuery(String query){
        Practical practical = null;
        DBCursor cursor = new DBCursor(db.rawQuery(query,null));
        if(cursor.moveToFirst()){
            practical = cursor.getPractical();
        }
        return practical;
    }

    public Admin getAdminByQuery(String query){
        Admin admin = null;
        DBCursor cursor = new DBCursor(db.rawQuery(query,null));
        if(cursor.moveToFirst()){
            admin = cursor.getAdmin();
        }
        return admin;
    }
    public Instructor getInstructorByQuery(String query){
        Instructor instructor = null;
        DBCursor cursor = new DBCursor(db.rawQuery(query,null));
        if(cursor.moveToFirst()){
            instructor = cursor.getInstructor();
        }
        return instructor;
    }
    public Student getStudentByQuery(String query){
        Student student = null;
        DBCursor cursor = new DBCursor(db.rawQuery(query,null));
        if(cursor.moveToFirst()){
            student = cursor.getStudent();
        }
        return student;
    }

    public ArrayList<Practical> getPracticalsByQuery(String query){
        //ArrayList<Practical> practicals = null;//SET TO NULL, U WANT TO GET NULL POINTER EXCEPTION HARH!!!!!
        ArrayList<Practical> practicals = new ArrayList<>();
        DBCursor cursor = new DBCursor(db.rawQuery(query,null));
        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                practicals.add(cursor.getPractical());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }
        return practicals;
    }

    public ArrayList<Student> getStudentsByQuery(String query){
        //ArrayList<Student> students = null;//SET TO NULL, U WANT TO GET NULL POINTER EXCEPTION HARH!!!!!
        ArrayList<Student> students = new ArrayList<>();
        DBCursor cursor = new DBCursor(db.rawQuery(query,null));
        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                students.add(cursor.getStudent());
                Log.d(context.toString(),cursor.getStudent().toString());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }
        return students;
    }

    //how to save serialized object into the database
    //https://stackoverflow.com/questions/23577825/android-save-object-as-blob-in-sqlite/23578035
    //https://stackoverflow.com/questions/1243181/how-to-store-object-in-sqlite-database
    //use to convert the object into the byte[] array and store in the SQL database by getBlob()
    public byte[] serializeObject(Object o) {

        byte[] bytes = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(o);
            // Convert/Get the bytes of the serialized object
            bytes = bos.toByteArray();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }
}
