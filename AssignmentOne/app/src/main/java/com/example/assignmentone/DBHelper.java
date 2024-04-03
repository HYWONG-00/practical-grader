package com.example.assignmentone;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.assignmentone.DBSchema.adminTable;
import com.example.assignmentone.DBSchema.instructorTable;
import com.example.assignmentone.DBSchema.studentTable;
import com.example.assignmentone.DBSchema.practicalTable;

public class DBHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "lab.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+adminTable.NAME
                +"("+adminTable.Cols.USERNAME+" TEXT PRIMARY KEY, "
                + adminTable.Cols.PIN+ " INTEGER);");//PRIMARY KEY => username is unique
        db.execSQL("CREATE TABLE "+instructorTable.NAME
                +"("+instructorTable.Cols.USERNAME+" TEXT PRIMARY KEY, "
                + instructorTable.Cols.PIN+ " INTEGER, "
                + instructorTable.Cols.NAME+" TEXT, "
                + instructorTable.Cols.EMAIL+" TEXT, "
                + instructorTable.Cols.COUNTRY + " INTEGER);");
        db.execSQL("CREATE TABLE "+studentTable.NAME
                +"("+studentTable.Cols.USERNAME+" TEXT PRIMARY KEY, "
                + studentTable.Cols.PIN+ " INTEGER, "
                + studentTable.Cols.NAME+" TEXT, "
                + studentTable.Cols.EMAIL+" TEXT, "
                + studentTable.Cols.COUNTRY+" INTEGER, "
                + studentTable.Cols.INSTRUCTOR +" TEXT, "
                + studentTable.Cols.MARK +" REAL, "
                + studentTable.Cols.PRACTICALS + " BLOB);");
        db.execSQL("CREATE TABLE "+practicalTable.NAME
                +"("+practicalTable.Cols.TITLE+" TEXT PRIMARY KEY, "
                + practicalTable.Cols.DESCRIPTION+ " TEXT, "
                + practicalTable.Cols.MARK+" REAL, "
                + practicalTable.Cols.INSTRUCTOR+ " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new UnsupportedOperationException("Sorry.");
    }
}