package com.example.assignmentone;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewAllInstructorFragment extends Fragment {

    private ArrayList<Instructor> instructors;
    public ViewAllInstructorFragment(ArrayList<Instructor> instructors) {
        this.instructors = instructors;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_all_instructor, container, false);
        RecyclerView rv = view.findViewById(R.id.recyclerview_ViewAllInstructors);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        InstructorAdapter instructorAdapter = new InstructorAdapter(this.instructors);
        rv.setAdapter(instructorAdapter);
        return view;
    }

    private class InstructorAdapter extends RecyclerView.Adapter<InstructorViewHolder>{
        private ArrayList<Instructor> instructors;
        public InstructorAdapter(ArrayList<Instructor> instructors){this.instructors = instructors;}
        @NonNull
        @Override
        public InstructorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View view  = layoutInflater.inflate(R.layout.instructor_row,parent,false);//inflate the instructor row
            return new InstructorViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull InstructorViewHolder holder, int position) {
            holder.username.setText(instructors.get(position).getUsername());
            holder.pin.setText(String.valueOf(instructors.get(position).getPinNum()));
            holder.name.setText(instructors.get(position).getName());
            holder.email.setText(instructors.get(position).getEmail());
            //set the image resourece
            holder.country.setImageResource(instructors.get(position).getCountry());
        }

        @Override
        public int getItemCount() {
            return instructors.size();
        }
    }

    private class InstructorViewHolder extends RecyclerView.ViewHolder{
        private TextView username,pin,name,email;
        private ImageView country;
        public InstructorViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.instructor_username_row);
            pin = itemView.findViewById(R.id.instructor_pin_row);
            name = itemView.findViewById(R.id.instructor_name_row);
            country = itemView.findViewById(R.id.imageView_country_instructor_row);
            email = itemView.findViewById(R.id.instructor_email_row);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //do your udpate or delete in the AEDInstructorActivity
                    Intent intent = new Intent(getContext(),AEDInstructorActivity.class);
                    intent.putExtra("instructor",instructors.get(getAdapterPosition()));
                    //getAdapterPostition() return the adapter position of the item if it still exists in the adapter.
                    //JUST KNOW THAT getAdapterPosition() should be called in ViewHolder class instead of in Adapter class.Embarrassed....
                    startActivity(intent);
                }
            });
        }
    }
}