package com.example.assignmentone;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class StudentsListFragment extends Fragment {
    private FragmentStudentsListsListener listener;
    private ArrayList<Student> studentList;
    private Context context;

    //this interface n method need to be implemented in our activity(MarkingActivity)
    //very similar as onClickListener interface
    public interface FragmentStudentsListsListener{
        //very similar as onClick method
        void onInputSentStudent(Student student);//this will be sent to our activity
    }

    public StudentsListFragment(ArrayList<Student> studentList,Context context) {
        this.studentList = studentList;
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_students_list, container, false);
        RecyclerView rv = view.findViewById(R.id.recyclerview_students_func6);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        StudentAdapter studentAdapter = new StudentAdapter(this.studentList);
        rv.setAdapter(studentAdapter);
        return view;
    }

    //Good practice: As if someone forget to implement the interface, it make very confused to that person on what error he got
    //this method will be called when the fragment is attached to our activity
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //check if our activity(context) has implemented this interface first
        if(context instanceof FragmentStudentsListsListener){
            //if context have implemented this interface, then we can assign it to the context
            //U see how we pass the student back to MarkingActivity? NOOOOOO
            listener = (FragmentStudentsListsListener) context;
        }else{//if main activity haven't implement the interface
            //make the program crash n see this message
            throw new RuntimeException(context.toString() + " must implement FragmentStudentsListsListener.");
        }
    }

    //if we remove the fragment from our activity
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private class StudentAdapter extends RecyclerView.Adapter<StudentViewHolder>{

        private ArrayList<Student> studentList;
        public StudentAdapter(ArrayList<Student> studentList){
            this.studentList = studentList;
        }
        @NonNull
        @Override
        public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.student_row_func6,parent,false);
            StudentViewHolder studentViewHolder = new StudentViewHolder(view);
            return studentViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
            holder.usernameV.setText(studentList.get(position).getUsername());
            holder.nameV.setText(studentList.get(position).getName());
        }

        @Override
        public int getItemCount() {
            return studentList.size();
        }
    }

    private class StudentViewHolder extends RecyclerView.ViewHolder{

        private TextView usernameV,nameV;
        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameV = itemView.findViewById(R.id.student_row_func6_username);
            nameV = itemView.findViewById(R.id.student_row_func6_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //get the student when the student row is clicked,
                    //we then want to pass this student into Marking activity and to other fragment
                    SQLiteDatabase db = null;
                    try {
                        //get the student when the student row is clicked,
                         db = new DBHelper(context).getReadableDatabase();
                         String query = "SELECT * FROM " + DBSchema.studentTable.NAME
                                + " WHERE " + DBSchema.studentTable.Cols.USERNAME + " = \"" + usernameV.getText().toString() + "\"";
                        DBCursor dbCursor = new DBCursor(db.rawQuery(query,null));
                        Student student = null;
                        //remember to make the cursor back to one ya,Otherwise,CursorIndexOutOfBoundsException: Index -1 requested
                        if(dbCursor.moveToFirst()){
                            student = dbCursor.getStudent();
                        }else {//Confirm can get a student so do nothing here
                        }
                        //send this student back to our MarkingActivity via interface method(onInputSentStudent)
                        listener.onInputSentStudent(student);
                    }finally {
                        db.close();
                    }
                }
            });
        }
    }
}