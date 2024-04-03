package com.example.assignmentone;

//!!!!!!!MUST REMEMBER!!!!!!!!!!!
//DON'T PUT SPACE IN THE NAME OF THE COLUMN
//Eg: "pin" is fine, Don't put "pin number" n also don't put space at " XXXXX ";
//If put pin number,u will keep having syntax error n dun know why u get syntax error
//SQLiteLog: (1) near "number": syntax error in "INSERT INTO admin(pin number,username) VALUES (?,?)"
public class DBSchema {

    public static class adminTable
    {
        public static final String NAME = "admin";//Table name
        public static class Cols{
            public static final String USERNAME = "username";
            public static final String PIN = "pin";
        }
    }

    public static class instructorTable
    {
        public static final String NAME = "instructor";//Table name
        public static class Cols{
            public static final String USERNAME = "username";
            public static final String PIN = "pin";
            public static final String NAME = "name";
            public static final String EMAIL = "email";
            public static final String COUNTRY = "country";
        }
    }

    public static class studentTable
    {
        public static final String NAME = "student";//Table name
        public static class Cols{
            public static final String USERNAME = "username";
            public static final String PIN = "pin";
            public static final String NAME = "name";
            public static final String EMAIL = "email";
            public static final String COUNTRY = "country";
            public static final String INSTRUCTOR= "instructor";
            public static final String MARK= "mark";
            public static final String PRACTICALS= "practicals";
        }
    }

    public static class practicalTable{
        public static final String NAME = "practical";
        public static class Cols{
            public static final String TITLE = "title";
            public static final String DESCRIPTION = "description";
            public static final String MARK = "mark";
            public static final String INSTRUCTOR= "instructor";
        }
    }
}
