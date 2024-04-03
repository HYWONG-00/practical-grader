package com.example.assignmentone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

//Another way to pass the interface without onAttach()
//https://www.youtube.com/watch?v=AkiltTv0CjA

public class SelectCountryActivity extends AppCompatActivity {

    public static final int[] COUNTRY_FLAGS = {
            R.drawable.flag__unknown, // No structure
            R.drawable.flag_ad, R.drawable.flag_ae, R.drawable.flag_af,
            R.drawable.flag_ag, R.drawable.flag_ai, R.drawable.flag_al,
            R.drawable.flag_am, R.drawable.flag_ar,
            R.drawable.flag_at, R.drawable.flag_au, R.drawable.flag_az,
            R.drawable.flag_ba, R.drawable.flag_bd, R.drawable.flag_be, R.drawable.flag_bf,
            R.drawable.flag_bg, R.drawable.flag_br, R.drawable.flag_ca, R.drawable.flag_ch,
            R.drawable.flag_cn, R.drawable.flag_cz, R.drawable.flag_de, R.drawable.flag_dk,
            R.drawable.flag_es, R.drawable.flag_fr, R.drawable.flag_gb, R.drawable.flag_ge,
            R.drawable.flag_gr,R.drawable.flag_hk,R.drawable.flag_it,R.drawable.flag_jp,
            R.drawable.flag_kr,R.drawable.flag_lt,R.drawable.flag_mx,R.drawable.flag_my,
            R.drawable.flag_nl,R.drawable.flag_pl,R.drawable.flag_qa,R.drawable.flag_ru,
            R.drawable.flag_uk,R.drawable.flag_us,R.drawable.flag_vn};

    public static final String[] COUNTRY_NAMES = {
            "Unknown",
            "Andorra","United Arab Emirates","Afghanistan",
            "Antigua and Barbuda","Anguilla","Albania",
            "Armenia","Argentina"
            ,"Austria","Australia","Azerbaijan",
            "Bosnia and Herzegovina","Bangladesh","Belgium","Burkina Faso",
            "Bulgaria","Brazil","Canada","Switzerland",
            "China","Czech Republic","Deutschland","Denmark",
            "Spain","France","UK of GreatBritain and NI","Georgia",
            "Greece","Hong Kong","Italy","Japan",
            "Korea","Lithuania","Mexico","Malaysia",
            "Netherlands","Poland","Qatar","Russia",
            "United Kingdom","United States of America","Vietnam"};


    /*//implement this interface in StudentActivity and InstructorActivity
    public interface CountryListener{
        void onInputSentCountry(String countryName);//pass the country name
    }
    private CountryListener countryListener;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_country);
        setTitle("Select country");

        RecyclerView rv= findViewById(R.id.recyclerview_countries);
        SelectCountryActivity.CountryAdapter adapter = new SelectCountryActivity.CountryAdapter(COUNTRY_NAMES,COUNTRY_FLAGS);
        rv.setAdapter(adapter);//for creating the student rows
        rv.setLayoutManager(new LinearLayoutManager(SelectCountryActivity.this));
    }

    private class CountryAdapter extends RecyclerView.Adapter<CountryViewHolder>{
        private String[] countryNames;
        private int[] countryFlags;
        public CountryAdapter(String[] countryNames,int[] countryFlags){
            this.countryNames = countryNames;
            this.countryFlags = countryFlags;
        }
        @NonNull
        @Override
        public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());//SelectCountryActivity.this
            View view = layoutInflater.inflate(R.layout.country_row,parent,false);
            return new CountryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CountryViewHolder holder, int position) {
            holder.country_flag.setImageResource(countryFlags[position]);
            holder.country_name.setText(countryNames[position]);
        }

        @Override
        public int getItemCount() {
            return COUNTRY_NAMES.length;
        }
    }

    private class CountryViewHolder extends RecyclerView.ViewHolder{
        private ImageView country_flag;
        private TextView country_name;
        public CountryViewHolder(@NonNull View itemView) {
            super(itemView);
            country_flag = itemView.findViewById(R.id.country_flag);
            country_name = itemView.findViewById(R.id.country_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //countryListener.onInputSentCountry(country_name.getText().toString());
                    Intent returnedData = new Intent();
                    returnedData.putExtra("country_id", COUNTRY_FLAGS[getAdapterPosition()]);
                    //Since the array is fixed, we can just get the adapter position == index
                    setResult(RESULT_OK, returnedData);
                    finish();
                }
            });
        }
    }
}

