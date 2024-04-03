package com.example.assignmentone;

import static com.example.assignmentone.Student.readPracticals;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.ArrayList;

public class DBCursor extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public DBCursor(Cursor cursor) {
        super(cursor);
    }

    public Admin getAdmin(){
        String username = getString(getColumnIndex(DBSchema.adminTable.Cols.USERNAME));
        int pin = getInt(getColumnIndex(DBSchema.adminTable.Cols.PIN));
        return new Admin(username,pin);
    }

    public Instructor getInstructor(){
        String username = getString(getColumnIndex(DBSchema.instructorTable.Cols.USERNAME));
        int pin = getInt(getColumnIndex(DBSchema.instructorTable.Cols.PIN));
        String name = getString(getColumnIndex(DBSchema.instructorTable.Cols.NAME));
        String email = getString(getColumnIndex(DBSchema.instructorTable.Cols.EMAIL));
        int country = getInt(getColumnIndex(DBSchema.instructorTable.Cols.COUNTRY));
        return new Instructor(username,pin,name,email,country);
    }

    public Student getStudent(){
        String username = getString(getColumnIndex(DBSchema.studentTable.Cols.USERNAME));
        int pin = getInt(getColumnIndex(DBSchema.studentTable.Cols.PIN));
        String name = getString(getColumnIndex(DBSchema.studentTable.Cols.NAME));
        String email = getString(getColumnIndex(DBSchema.studentTable.Cols.EMAIL));
        int country = getInt(getColumnIndex(DBSchema.studentTable.Cols.COUNTRY));
        String instructor = getString(getColumnIndex(DBSchema.studentTable.Cols.INSTRUCTOR));
        double mark = getDouble(getColumnIndex(DBSchema.studentTable.Cols.MARK));
        byte[] practicalsInBytes = getBlob(getColumnIndex(DBSchema.studentTable.Cols.PRACTICALS));
        ArrayList<Practical> practicals = readPracticals(practicalsInBytes);
        //will convert byte[] practicals to ArrayList<Practical> ,NOT SURE IF IT WORKS
        return new Student(username,pin,name,email,country,instructor,mark,practicals);
    }

    public Practical getPractical(){
        String title = getString(getColumnIndex(DBSchema.practicalTable.Cols.TITLE));
        String description = getString(getColumnIndex(DBSchema.practicalTable.Cols.DESCRIPTION));
        double mark = getDouble(getColumnIndex(DBSchema.practicalTable.Cols.MARK));
        String instructor = getString(getColumnIndex(DBSchema.practicalTable.Cols.INSTRUCTOR));
        return new Practical(title,description,mark,instructor);
    }
}
