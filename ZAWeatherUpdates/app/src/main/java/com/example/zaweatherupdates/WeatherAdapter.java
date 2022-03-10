package com.example.zaweatherupdates;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.Viewholder> {
    private Context context;
    private ArrayList<WeatherResults> weatherResults;

    public WeatherAdapter(Context context, ArrayList<WeatherResults> weatherResults) {
        this.context = context;
        this.weatherResults = weatherResults;
    }

    @Override
    public WeatherAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent,false);
        return new Viewholder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull WeatherAdapter.Viewholder holder, int position){
        WeatherResults res = weatherResults.get(position);
        holder.txtTmp.setText(""+res.getTemp());
        holder.txtNme.setText(res.getDay());
        Picasso.get().load(res.getIcon()).into(holder.imgDis);
    }

    @Override
    public int getItemCount() {
        return weatherResults.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{

        private ImageView imgDis;
        private TextView txtNme, txtDes, txtTmp;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            imgDis = itemView.findViewById(R.id.imgMltiDay);
            txtNme = itemView.findViewById(R.id.txtnmeMltiD);
            txtTmp = itemView.findViewById(R.id.txttmpMltiD);
        }
    }
}
