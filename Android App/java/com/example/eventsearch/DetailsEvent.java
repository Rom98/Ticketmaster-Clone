package com.example.eventsearch;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailsEvent newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsEvent extends Fragment {

    private String data,id;
    private TextView artist_name,venue, date, category, price, status, ticketmaster, seatmap,t;
    private LinearLayout l1,l2,l3,l4,l5,l6,l7,l8;
    public DetailsEvent() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {        //All logic stuff here

    }


    private void insert_data(String s){
        data = s;
        Log.i("JSON data of individual event inside fragment", data);
        artist_name = getView().findViewById(R.id.details_event_artistname);
        venue = getView().findViewById(R.id.details_event_venue);
        date = getView().findViewById(R.id.details_event_date);
        category = getView().findViewById(R.id.details_event_category);
        price = getView().findViewById(R.id.details_event_price);
        status = getView().findViewById(R.id.details_event_status);
        ticketmaster = getView().findViewById(R.id.details_event_ticketmaster);
        seatmap = getView().findViewById(R.id.details_event_seatmap);

        l1 = getView().findViewById(R.id.NameLayout);
        l2 = getView().findViewById(R.id.AddresLayout);
        l3 = getView().findViewById(R.id.CityLayout);
        l4 = getView().findViewById(R.id.PhoneLayout);
        l5 = getView().findViewById(R.id.OpenLayout);
        l6 = getView().findViewById(R.id.GeneralLayout);
        l7 = getView().findViewById(R.id.vChildLayout);
        l8 = getView().findViewById(R.id.SeatLayout);

        t = getView().findViewById(R.id.event_details_text);
        if(data.length() == 14){
            t.setText("No data.");
            t.setVisibility(View.VISIBLE);
        }
        else{
            t.setVisibility(View.INVISIBLE);
        }

        try {
            Log.i("Inside onviewcreated", data);
            JSONObject object = new JSONObject(data);
            JSONArray jArray = object.getJSONArray("results");
            JSONObject json_data = jArray.getJSONObject(0);

            if(json_data.getString("Artist").equals(""))
                l1.setVisibility(getView().GONE);
            else
                artist_name.setText(json_data.getString("Artist"));
            if(json_data.getString("Venue").equals(""))
                l2.setVisibility(getView().GONE);
            else
                venue.setText(json_data.getString("Venue"));
            if(json_data.getString("Date").equals(""))
                l3.setVisibility(getView().GONE);
            else
                date.setText(json_data.getString("Date"));
            if(json_data.getString("Category").equals(""))
                l4.setVisibility(getView().GONE);
            else
                category.setText(json_data.getString("Category"));
            if(json_data.getString("Price").equals(""))
                l5.setVisibility(getView().GONE);
            else
                price.setText(json_data.getString("Price"));
            if(json_data.getString("TicketStatus").equals(""))
                l6.setVisibility(getView().GONE);
            else
                status.setText(json_data.getString("TicketStatus"));
            if(json_data.getString("BuyTicketAt").equals(""))
                l7.setVisibility(getView().GONE);
            else {
                String master = String.format("<a href=\"%s\">Ticketmaster</a> ", json_data.getString("BuyTicketAt"));
                Spannable spannedText = (Spannable) Html.fromHtml(master);
                //ticketmaster.setText(Html.fromHtml(master));
                ticketmaster.setMovementMethod(LinkMovementMethod.getInstance());
                Spannable processedText = removeUnderlines(spannedText);
                ticketmaster.setText(processedText);
            }
            if(json_data.getString("SeatMap").equals(""))
                l8.setVisibility(getView().GONE);
            else{
                String map = String.format("<a href=\"%s\">View Seat Map Here</a> ", json_data.getString("SeatMap"));
                Spannable spannedText = (Spannable) Html.fromHtml(map);
                //seatmap.setText(Html.fromHtml(map));
                seatmap.setMovementMethod(LinkMovementMethod.getInstance());
                Spannable processedText = removeUnderlines(spannedText);
                seatmap.setText(processedText);
            }

        }catch (JSONException e){
            //Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();

        }
    }

    public static Spannable removeUnderlines(Spannable p_Text) {
        URLSpan[] spans = p_Text.getSpans(0, p_Text.length(), URLSpan.class);
        for (URLSpan span : spans) {
            int start = p_Text.getSpanStart(span);
            int end = p_Text.getSpanEnd(span);
            p_Text.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            p_Text.setSpan(span, start, end, 0);
        }
        return p_Text;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            id= extras.getString("ID");
        }
        String url = "https://myhw8proj-73215.wl.r.appspot.com/event_data/?Id=" + id;
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

        //Log.i("JSON data of individual event inside fragment", data);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details_event, container, false);
    }

}