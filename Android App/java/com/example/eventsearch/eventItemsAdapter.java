package com.example.eventsearch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;
import java.util.zip.Inflater;

public class eventItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<items> data;
    private Context context;
    private LayoutInflater inflater;

    public eventItemsAdapter(Context context, List<items> data){        // create constructor to initialize context and data sent from MainActivity
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= inflater.inflate(R.layout.activity_event_data, parent,false);
        ViewHolder holder=new ViewHolder(view);
        return (holder);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
// Get current position of item in recyclerview to bind data and assign values from list
        ViewHolder myHolder= (ViewHolder) holder;
        items current=data.get(position);

        Log.i("name", current.name);
        Log.i("name", current.venue);
        Log.i("name", current.date);

        String x = current.name;


        int index = 0;
        if (current.name.length() > 35) {                       //To append '...' if string length is longer than 35 characters
            if (current.name.charAt(34) != ' ') {
                for (int i = 35; i > 0; i--) {
                    if (current.name.charAt(i) == ' ') {
                        index = i;
                        break;
                    }
                }
            }
            if (index == 0) {
                index = 35;
            }
            current.name = current.name.substring(0, index) + "...";
        }

        myHolder.eName.setText(current.name);                //myholder.name - 'name' is from ViewHolder class below
        myHolder.eVenue.setText(current.venue);
        myHolder.eDate.setText(current.date);

        if(current.genre.contains("Music"))
            myHolder.imgViewIcon.setImageDrawable(context.getDrawable(R.drawable.music_icon));
        else if(current.genre.contains("Sports"))
            myHolder.imgViewIcon.setImageDrawable(context.getDrawable(R.drawable.ic_sport_icon));
        else if(current.genre.contains("Arts & Theatre"))
            myHolder.imgViewIcon.setImageDrawable(context.getDrawable(R.drawable.art_icon));
        else if(current.genre.contains("Film"))
            myHolder.imgViewIcon.setImageDrawable(context.getDrawable(R.drawable.film_icon));
        else
            myHolder.imgViewIcon.setImageDrawable(context.getDrawable(R.drawable.miscellaneous_icon));

        SharedPreferences sp = this.context.getSharedPreferences("UserEventPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if(sp.contains(current.id)){
            myHolder.imgViewHeart.setBackgroundResource(R.drawable.heart_fill_red);
        }
        else
            myHolder.imgViewHeart.setBackgroundResource(R.drawable.heart_outline_black);


        myHolder.imgViewHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(sp.contains(current.id)){
                    editor.remove(current.id);
                    myHolder.imgViewHeart.setBackgroundResource(R.drawable.heart_outline_black);
                }
                else{
                    String s = current.name+","+current.venue+","+current.date+","+current.genre+","+current.artist+","+current.Lat+","+current.Lng;
                    editor.putString(current.id, s);
                    myHolder.imgViewHeart.setBackgroundResource(R.drawable.heart_fill_red);
                }
                editor.commit();
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), event_details.class);
                intent.putExtra("ID",String.valueOf(current.id));
                intent.putExtra("VENUE",String.valueOf(current.venue));
                intent.putExtra("EVENT",String.valueOf(current.name));
                intent.putExtra("EVENTX",String.valueOf(x));
                intent.putExtra("ARTIST",String.valueOf(current.artist));
                Log.i("FIRST TIME ARTISTS ARE ENCONTERED IN EVENTITEMSADAPTER", current.artist);
                intent.putExtra("Latitude",String.valueOf(current.Lat));
                intent.putExtra("Longitude",String.valueOf(current.Lng));
                intent.putExtra("DATE",String.valueOf(current.date));
                intent.putExtra("GENRE",String.valueOf(current.genre));
                //Log.i("ARTIST FROM MAIN DATA", current.artist);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgViewIcon,imgViewHeart;
        TextView eName,eVenue, eDate;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            imgViewIcon = itemView.findViewById(R.id.icon);
            imgViewHeart = itemView.findViewById(R.id.heart);
            eName = itemView.findViewById(R.id.event_name);
            eVenue = itemView.findViewById(R.id.event_venue);
            eDate = itemView.findViewById(R.id.event_date);
        }
    }
}
