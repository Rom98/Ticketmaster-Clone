package com.example.eventsearch;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailsVenue newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsVenue extends Fragment implements OnMapReadyCallback {//

    private String ven,data;
    private TextView name, address, city, phone, openhrs, general, child,t;
    private LinearLayout l1,l2,l3,l4,l5,l6,l7;
    private String lat,lng;

    MapView mapView;
    GoogleMap map;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {        //All logic stuff here

    }

    public void insert_data(String s){
        data = s;

        name = getView().findViewById(R.id.details_venue_artistname);
        address = getView().findViewById(R.id.details_venue_address);
        city = getView().findViewById(R.id.details_venue_city);
        phone = getView().findViewById(R.id.details_venue_phone);
        openhrs = getView().findViewById(R.id.details_venue_open);
        general = getView().findViewById(R.id.details_venue_general);
        child = getView().findViewById(R.id.details_venue_child);

        l1 = getView().findViewById(R.id.NameLayout);
        l2 = getView().findViewById(R.id.AddresLayout);
        l3 = getView().findViewById(R.id.CityLayout);
        l4 = getView().findViewById(R.id.PhoneLayout);
        l5 = getView().findViewById(R.id.OpenLayout);
        l6 = getView().findViewById(R.id.GeneralLayout);
        l7 = getView().findViewById(R.id.vChildLayout);

        t = getView().findViewById(R.id.details_venue_text);
        if(data.length() == 14){
            t.setText("No data.");
            t.setVisibility(View.VISIBLE);
        }
        else{
            t.setVisibility(View.INVISIBLE);
        }


        try {
           // Log.i("Inside onviewcreated", data);
            JSONObject object = new JSONObject(data);
            JSONArray jArray = object.getJSONArray("results");
            JSONObject json_data = jArray.getJSONObject(0);

//            lat = json_data.getString("latitude");
//            lng = json_data.getString("longitude");

//            Log.i("LAT AND LONG", lat+","+lng);

            if(ven.equals(""))
                l1.setVisibility(getView().GONE);
            else
                name.setText(ven);
            if(json_data.getString("Address").equals(""))
                l2.setVisibility(getView().GONE);
            else
                address.setText(json_data.getString("Address"));
            if(json_data.getString("City").equals(""))
                l3.setVisibility(getView().GONE);
            else
                city.setText(json_data.getString("City"));
            if(json_data.getString("Phone").equals(""))
                l4.setVisibility(getView().GONE);
            else
                phone.setText(json_data.getString("Phone"));
            if(json_data.getString("Openhrs").equals(""))
                l5.setVisibility(getView().GONE);
            else
                openhrs.setText(json_data.getString("Openhrs"));
            if(json_data.getString("General").equals(""))
                l6.setVisibility(getView().GONE);
            else
                general.setText(json_data.getString("General"));
            if(json_data.getString("Child").equals(""))
                l7.setVisibility(getView().GONE);
            else
                child.setText(json_data.getString("Child"));


        }catch (JSONException e){
            //Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }


    }

    @SuppressLint("MissingPermission")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            ven= extras.getString("VENUE");
            lat = extras.getString("Latitude");
            lng = extras.getString("Longitude");
        }
        String url = "https://myhw8proj-73215.wl.r.appspot.com/venue_data/?Venue_Name=" + ven.replace(" ","+");
        Log.i("URL", url);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.i("Resp",response.toString());
                        String s = response.toString();
                        //Log.i("lmlklmaf",data);
                        insert_data(s);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(request);

        View v = inflater.inflate(R.layout.fragment_details_venue, container, false);
        mapView = v.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);


        mapView.getMapAsync( this);


        return v;
    }

    @SuppressLint("MissingPermission")
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setMyLocationEnabled(true);
        map.addMarker(new MarkerOptions().position(new LatLng(Float.parseFloat(lat), Float.parseFloat(lng))));
//        map.animateCamera(cameraUpdate);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Float.parseFloat(lat), Float.parseFloat(lng)), 15));

    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}