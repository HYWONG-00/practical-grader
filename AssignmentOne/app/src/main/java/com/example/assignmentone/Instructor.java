package com.example.assignmentone;

import android.util.Patterns;
import android.widget.Toast;

import java.io.Serializable;

public class Instructor extends User implements Serializable {
    private String name;
    private String email;
    private int country;//country's drawable id
    public Instructor(String username, int pinNum,String name,String email,int country) {
        super(username, pinNum);
        setName(name);
        setEmail(email);
        setCountry(country);
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setEmail(String email) {
        //try{
        if (validateEmail(email)) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Illegal email format.");
        }
        //}catch(IllegalArgumentException e){
        //Log.d("Catch IllegalArgumentException!!!!!!!!!!!", e.getMessage());}

    }
    public String getEmail(){return this.email;}

    public void setCountry(int country) {this.country = country;}
    public int getCountry(){return this.country;}
    public String toString(){
        return "Username: " + this.getUsername() + ", PIN: " + this.getPinNum() + ", Name: " + getName() + ", Email: " + getEmail() + ", Country: " + getCountry();
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
}