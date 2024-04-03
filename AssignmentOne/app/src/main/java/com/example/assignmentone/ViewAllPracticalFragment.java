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
import android.widget.TextView;

import java.util.ArrayList;

public class ViewAllPracticalFragment extends Fragment {

    private ArrayList<Practical> practicals;

    public ViewAllPracticalFragment(ArrayList<Practical> practicals) {
        this.practicals = practicals;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_all_practical, container, false);
        RecyclerView rv = view.findViewById(R.id.recyclerview_ViewAllPracticals);
        PracticalAdapter practicalAdapter = new PracticalAdapter(this.practicals);
        rv.setAdapter(practicalAdapter);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    private class PracticalAdapter extends RecyclerView.Adapter<PracticalViewHolder>{
        private ArrayList<Practical> practicals;

        public PracticalAdapter(ArrayList<Practical> practicals) {
            this.practicals = practicals;
        }

        @NonNull
        @Override
        public PracticalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater  = LayoutInflater.from(getContext());
            View view = layoutInflater.inflate(R.layout.practical_row,parent,false);
            return new PracticalViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PracticalViewHolder holder, int position) {
            holder.title.setText(practicals.get(position).getTitle());
            holder.instructor.setText(practicals.get(position).getInstructor());
            holder.description.setText(practicals.get(position).getDescription());
            holder.mark.setText(String.valueOf(practicals.get(position).getMark()));
        }

        @Override
        public int getItemCount() {
            return practicals.size();
        }
    }

    private class PracticalViewHolder extends RecyclerView.ViewHolder{
        private TextView title,instructor,description,mark;
        public PracticalViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.practical_title_row);
            instructor = itemView.findViewById(R.id.practical_instructor_row);
            description = itemView.findViewById(R.id.practical_description_row);
            mark = itemView.findViewById(R.id.practical_mark_row);

            //How do I get the position selected in a RecyclerView?
            //https://stackoverflow.com/questions/26682277/how-do-i-get-the-position-selected-in-a-recyclerview
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(),AEDPracticalActivity.class);
                    intent.putExtra("practical",practicals.get(getAdapterPosition()));
                    //getAdapterPostition() return the adapter position of the item if it still exists in the adapter.
                    //JUST KNOW THAT getAdapterPosition() should be called in ViewHolder class instead of in Adapter class.Embarrassed....
                    startActivity(intent);
                }
            });
        }

    }
}