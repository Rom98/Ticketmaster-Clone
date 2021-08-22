package com.example.eventsearch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class favItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    List<favItems> data;
    private Context context;
    private LayoutInflater inflater;

    public favItemsAdapter(Context context, List<favItems> data){        // create constructor to initialize context and data sent from MainActivity
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= inflater.inflate(R.layout.fav_items, parent,false);
        ViewHolder holder = new ViewHolder(view);
        return (holder);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder myholder = (ViewHolder) holder;

        favItems current=data.get(position);

//        Log.i("name", current.name);
//        Log.i("name", current.venue);
//        Log.i("name", current.date);


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

        myholder.favName.setText(current.name);                //myholder.name - 'name' is from ViewHolder class below
        myholder.favVenue.setText(current.venue);
        myholder.favDate.setText(current.date);

        if(current.genre.contains("Music"))
            myholder.imgViewIcon.setImageDrawable(context.getDrawable(R.drawable.music_icon));
        else if(current.genre.contains("Sports"))
            myholder.imgViewIcon.setImageDrawable(context.getDrawable(R.drawable.ic_sport_icon));
        else if(current.genre.contains("Arts & Theatre"))
            myholder.imgViewIcon.setImageDrawable(context.getDrawable(R.drawable.art_icon));
        else if(current.genre.contains("Film"))
            myholder.imgViewIcon.setImageDrawable(context.getDrawable(R.drawable.film_icon));
        else
            myholder.imgViewIcon.setImageDrawable(context.getDrawable(R.drawable.miscellaneous_icon));

        SharedPreferences sp = this.context.getSharedPreferences("UserEventPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();



        myholder.imgViewHeart.setBackgroundResource(R.drawable.heart_fill_red);
        myholder.imgViewHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(sp.contains(current.id)){
                    editor.remove(current.id);
                    myholder.imgViewHeart.setBackgroundResource(R.drawable.heart_outline_black);
                }
                else{
                    String s = current.name+","+current.venue+","+current.date+","+current.genre;
                    editor.putString(current.id, s);
                    myholder.imgViewHeart.setBackgroundResource(R.drawable.heart_fill_red);
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
                intent.putExtra("EVENTX",String.valueOf(current.name));
                intent.putExtra("ARTIST",String.valueOf(current.artist));
                intent.putExtra("Latitude",String.valueOf(current.lat));
                intent.putExtra("Longitude",String.valueOf(current.lng));
                intent.putExtra("DATE",String.valueOf(current.date));
                intent.putExtra("GENRE",String.valueOf(current.genre));
                //Log.i("ARTIST FROM MAIN DATA", current.artist);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() { return data.size(); }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgViewIcon,imgViewHeart;
        TextView favName,favVenue, favDate;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            imgViewIcon = itemView.findViewById(R.id.fav_imageView);
            imgViewHeart = itemView.findViewById(R.id.fav_heart);
            favName = itemView.findViewById(R.id.fav_event);
            favVenue = itemView.findViewById(R.id.fav_venue);
            favDate = itemView.findViewById(R.id.fav_date);
        }
    }
}
