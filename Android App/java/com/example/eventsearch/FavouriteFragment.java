package com.example.eventsearch;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavouriteFragment newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavouriteFragment extends Fragment {

    RecyclerView recyclerView;
    favItemsAdapter adapter;
    SharedPreferences sp;

    public String EventData;
    public ImageView Heart;
    TextView t;

    @Override
    public void onResume() {
        super.onResume();
        insert_fav_data();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(FavouriteFragment.this).attach(FavouriteFragment.this).commit();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {        //All logic stuff here
        recyclerView = view.findViewById(R.id.fav_recycler_view);
        sp = getContext().getSharedPreferences("UserEventPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        t =  view.findViewById(R.id.no_favdata);;
        insert_fav_data();

    }


    private void insert_fav_data() {

        List<favItems> d = new ArrayList<>();
        Map<String,?> entries = sp.getAll();
        Set<String> keys = entries.keySet();
        Log.i("SIZE", String.valueOf(sp.getAll().size()));
        if(keys.size() == 0){
            t.setText("No items.");
            t.setVisibility(View.VISIBLE);
        }
        else{
            t.setVisibility(View.INVISIBLE);
        }
        try{
            for (String key : keys) {

                favItems Data = new favItems();

                String s = sp.getString(key,"");
                String[] arr = s.split(",");
                Log.i("VALUES OF KEY ", s);
                Data.name = arr[0];
                Data.venue = arr[1];
                Data.date = arr[2];
                Data.genre = arr[3];
                Data.artist = arr[4];
                Data.lat = arr[5];
                Data.lng = arr[6];
                Data.id = key;
                Log.i("VALUES OF EVENT ", arr[0]);
                Log.i("VALUES OF VENUE ", arr[1]);
                Log.i("VALUES OF DATE ", arr[2]);
                Log.i("VALUES OF DATE ", arr[3]);
                Log.i("VALUES OF DATE ", arr[4]);
                Log.i("VALUES OF DATE ", arr[5]);
                Log.i("VALUES OF DATE ", arr[6]);

                d.add(Data);
            }
        }catch (Exception e){
        }

        adapter = new favItemsAdapter(getContext(), d);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //t.setVisibility(View.INVISIBLE);
        return inflater.inflate(R.layout.fragment_favourite, container, false);
    }
}