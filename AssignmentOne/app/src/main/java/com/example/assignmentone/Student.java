package com.example.assignmentone;

import android.util.Log;
import android.util.Patterns;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class Student extends User{

    public static final double NO_MARK = 0.0;//0.0 means no mark
    public static final double MIN_MARK = 0.0;//0.0 means minimum mark
    public static final double MAX_MARK = 100.0;//maximum mark
    private String name;
    private String email;
    private double mark;
    private int country;//country's drawable id
    private String instructor;//instructor's username, to know this student is under which instructor
    private ArrayList<Practical> practicals;

    //used by AEDStudentActivity addStudent()
    public Student(String username, int pinNum,String name,String email,int country,String instructor) {
        super(username, pinNum);
        //this.email = email;//we need to validate the email address
        setName(name);
        setEmail(email);
        this.country = country;
        this.instructor = instructor;
        this.mark = NO_MARK;//initially have no mark at all
        practicals = new ArrayList<>();
    }

    public Student(String username, int pinNum,String name, String email,int country,String instructor,double mark,ArrayList<Practical> practicals) {
        super(username, pinNum);
        setName(name);
        setEmail(email);
        this.country = country;
        this.instructor = instructor;
        this.mark = mark;
        this.practicals = practicals;
    }


    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setEmail(String email) {
        if(validateEmail(email)){
            this.email = email;
        }else {
            throw new IllegalArgumentException("Illegal email format.");
        }
    }

    public String getEmail(){return this.email;}

    public void setMark(double mark){
        if(Double.compare(this.mark,MAX_MARK) > 0){//if this.mark is greater than MAX_MARK
            this.mark = MAX_MARK;
        }
        else if(Double.compare(this.mark,MIN_MARK) < 0){//if this.mark is smaller than MIN_MARK
            this.mark = MIN_MARK;
        }
        else{
            this.mark = mark;
        }
    }

    public double getMark(){return this.mark;}

    public void setCountry(int country){
        this.country = country;
    }
    public int getCountry(){return this.country;}

    public void setInstructor(String instructor){
        this.instructor = instructor;
    }

    public String getInstructor(){return this.instructor;}

    public void addPractical(Practical practical){
        //add the practical into the list
        this.practicals.add(practical);
    }
    public void removePractical(Practical practical){
        this.practicals.remove(practical);
    }
    /*public void clean(){
        this.mark = 0.0;
        this.practicals = new ArrayList<>();}//clean the list when u want,like this u no need wipe data liao XD*/
    public Practical getPractical(String title){
        Practical practical = null;
        if(!this.practicals.isEmpty()){
            Iterator<Practical> iterator = getPracticals().iterator();
            Practical currentPrac = null;
            while(iterator.hasNext()) {
                currentPrac = iterator.next();
                // Do something with the value
                if(currentPrac.getTitle().equals(title)){
                    practical = currentPrac;
                }
            }
            if(currentPrac.getTitle().equals(title)){
                practical = currentPrac;
            }
        }
        return practical;
    }

    public ArrayList<Practical> getPracticals(){return this.practicals;}

    public String toString(){
        return "Username: " + getUsername() + "pin: " + getPinNum() + "name: " + getName()
                + "Email: " + getEmail() + "Country: " + getCountry() + "Instructor: " + getInstructor() + "Mark: " + getMark()
                + "Practicals: " + practicals.toString() + "\n";
    }

    //I THINK IT IS BETTER TO CHECK IN THE ACTIVITY(JUST LIKE WHAT HE DOES)
    //How to validate the email?https://www.youtube.com/watch?v=cnD_7qFeZcY
    //check our email pattern(Not 100% correct,but is good to check)
    private boolean validateEmail(String email){
        boolean b = false;
        //if the input email address does not match the formal pattern
        if(Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            b = true;
        }
        return b;
    }

    //to read the byte[] array in the studentTable, then we convert it into Practical object
    public static ArrayList<Practical> readPracticals(byte[] data) {
        ArrayList<Practical> practicals = null;
        try {
            ByteArrayInputStream baip = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(baip);
            practicals = (ArrayList<Practical>) ois.readObject();
            baip.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return practicals;
    }

}
