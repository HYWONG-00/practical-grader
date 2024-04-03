package com.example.assignmentone;

import java.io.Serializable;

public class Practical implements Serializable {

    public static final double minMark = 0.0;//min mark for all assessment
    public static final double maxMark = 100.0;//max for total mark
    public static final double NO_MARK = -1.0;
    private String title;
    private String description;
    private double mark;//total mark = q1+q2+q3 / 3
    //if create for instructor, then q1,q2,q3 will be the max mark
    //if create for student, then q1,q2,q3 will be there own marks
    //private double mark_q1,mark_q2,mark_q3;
    private String instructor;//instructor's username//if it is admin, we set this as null

    public Practical(String title,String description,String instructor){
        this.title = title;
        this.description = description;
        this.mark = NO_MARK;
        this.instructor = instructor;
    }

    public Practical(String title,String description,double mark,String instructor){
        this.title = title;
        this.description = description;
        this.mark = mark;
        this.instructor = instructor;
    }

    public void setDescription(String description){this.description = description;}

    public String getDescription(){return this.description;}

    public void setTitle(String title){this.title=title;}

    public String getTitle(){ return this.title;}

    public void setMark(double mark) {
        if(hasNoMark()){//if this practical has no mark,set it as NO_MARK
            this.mark = NO_MARK;
        }else{
            if(mark < minMark || mark > maxMark)
                this.mark = mark;
        }
    }

    public double getMark(){return this.mark;}

    public void setInstructor(String instructor){
        this.instructor = instructor;
    }

    public String getInstructor(){return this.instructor;}

    public String toString(){
        return "Title : " + this.title + ", Description: " + this.description + ", Mark: " + this.mark + "Instructor: " + this.instructor + "\n";
    }
    public boolean hasNoMark(){
        boolean b = false;
        //this.mark == NO_MARK!!!!!!COMPARE double cannot use == need to use Math.abs
        if(Double.compare(this.mark,NO_MARK) == 0){
            b = true;
        }
        return b;
    }
}