package com.example.assignmentone;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class PracticalsListFragment extends Fragment {

    private FragmentPracticalsListsListener listener;
    private ArrayList<Practical> practicalList;

    public PracticalsListFragment(ArrayList<Practical> practicalList) {
        this.practicalList = practicalList;
    }

    //this interface n method need to be implemented in our activity(MarkingActivity)
    //very similar as onClickListener interface
    public interface FragmentPracticalsListsListener{
        //very similar as onClick method
        void onInputSentPractical(Practical practical);//this method used to send the practical to our activity
    }

    //Good practice: Good to give a warning if the fragment attach to the main acticity
    //but the main activity has not implement the interface we have in the fragment
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof FragmentPracticalsListsListener){ //if the activity has implemented the interface
            //we will pass the interface back to the activity
            listener = (FragmentPracticalsListsListener) context;
        }else{
            //if activity does not implement the interface, crash the program n force him to implement the interface
            throw new RuntimeException(context.toString() + " must implement FragmentPracticalsListsListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_practicals_list, container, false);
        RecyclerView rv = view.findViewById(R.id.recyclerview_practicals_func6);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        PracticalAdapter practicalAdapter = new PracticalAdapter(this.practicalList);
        rv.setAdapter(practicalAdapter);
        return view;
    }

    private class PracticalAdapter extends RecyclerView.Adapter<PracticalViewHolder>{
        private ArrayList<Practical> practicalList;
        public PracticalAdapter(ArrayList<Practical> practicalList){
            this.practicalList = practicalList;
        }
        @NonNull
        @Override
        public PracticalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //LayoutInflater takes an XML file as input and builds the View objects from it.
            LayoutInflater inflater = LayoutInflater.from(getContext());
            //layout id of the row/viewholder,??,??
            View view = inflater.inflate(R.layout.practical_row_prac6,parent,false);
            //create the view holder in the fragment
            PracticalViewHolder practicalViewHolder = new PracticalViewHolder(view);
            return practicalViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull PracticalViewHolder holder, int position) {
            holder.titleV.setText(practicalList.get(position).getTitle());
            holder.markV.setText(String.valueOf(practicalList.get(position).getMark()));
        }

        @Override
        public int getItemCount() {
            return practicalList.size();
        }
    }

    private class PracticalViewHolder extends RecyclerView.ViewHolder{

        private TextView titleV,markV;
        public PracticalViewHolder(@NonNull View itemView) {
            super(itemView);
            titleV = itemView.findViewById(R.id.practical_title_row_func6);
            markV = itemView.findViewById(R.id.practical_mark_row_func6);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //get the practical when the practical row is clicked,
                    //we then want to pass this practical into Marking activity and to other fragment
                    SQLiteDatabase db = null;
                    try {
                        //get the practical when the practical row is clicked,
                        db = new DBHelper(getContext()).getReadableDatabase();
                        String query = "SELECT * FROM " + DBSchema.practicalTable.NAME
                                + " WHERE " + DBSchema.practicalTable.Cols.TITLE + " = \"" + titleV.getText().toString() + "\"";
                        DBCursor dbCursor = new DBCursor(db.rawQuery(query,null));
                        Practical practical = null;
                        //remember to make the cursor back to one ya,Otherwise,CursorIndexOutOfBoundsException: Index -1 requested
                        if(dbCursor.moveToFirst()){
                            practical = dbCursor.getPractical();
                        }else {//Confirm can get a practical so do nothing here
                        }
                        //send this practical back to our MarkingActivity via interface method(onInputSentPractical)
                        listener.onInputSentPractical(practical);
                    }finally {
                        db.close();//remember to close the database otherwise will have memory leak
                    }
                }
            });
        }
    }
}