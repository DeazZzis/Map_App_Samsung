package com.example.deathis.myapplication;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Executor;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.RViewHolder> {

    private static int viewHolderCount;
    private int numItems;
    private String title, text, lat, lng, meters;
    private ArrayList<Post> mainS;
    private Context parent;
    private double myLat, myLng;
    private float dis[] = new float[10];


    public RVAdapter(ArrayList<Post> mainS, Context context, double myLat, double myLng){
        this.mainS = mainS;
        parent = context;
        this.myLat = myLat;
        this.myLng = myLng;
    }

    @Override
    public RViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.post_list_item;

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);

        RViewHolder rViewHolder = new RViewHolder(view);

        return rViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull RViewHolder holder, int position) {
        holder.bind(mainS.get(position).getTitle(), mainS.get(position).getLat(),
                mainS.get(position).getLng());
    }

    @Override
    public int getItemCount() {
        return mainS.size();
    }


    class RViewHolder extends RecyclerView.ViewHolder {

        TextView textView_title;
        TextView textView_text;

        double D;

        public RViewHolder(View itemView)  {
            super(itemView);

            textView_title = itemView.findViewById(R.id.textview_title);
            textView_text = itemView.findViewById(R.id.textview_time);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    title = mainS.get(position).getTitle();
                    text = mainS.get(position).getText();
                    lat = String.valueOf(mainS.get(position).getLat());
                    lng = String.valueOf(mainS.get(position).getLng());

                    D = distance(myLat, myLng, Float.parseFloat(lat), Float.parseFloat(lng));
                    if (D != 0  && D <= 20000){
                        meters = dist_int(D);
                    } else {
                        meters = "0";
                    }

                    Intent in = new Intent(parent, InfoPostActivity.class);
                    in.putExtra("title", title);
                    in.putExtra("text", text);
                    in.putExtra("lat", lat);
                    in.putExtra("lng", lng);
                    in.putExtra("meters", meters);
                    in.putExtra("D", String.valueOf(D));
                    parent.startActivity(in);
                }
            });
        }

        void bind(String s1, String lat2, String lng2){
            textView_title.setText(s1);

            D = distance(myLat, myLng, Float.parseFloat(lat2), Float.parseFloat(lng2));
            if (D != 0  && D <= 20000){
                meters = dist_int(D);
            } else {
                meters = "0";
            }

            if (meters != "0"){
                textView_text.setText(meters);
            } else {
                textView_text.setVisibility(View.INVISIBLE);
            }
        }
        private double distance(final double myLat, final double myLng, final float eLat, final float eLng){
            Location.distanceBetween(myLat, myLng, eLat, eLng, dis);
            return dis[0];
        }

        private String dist_int(double dis){
            int disInt = (int) (dis / 50);
            disInt *= 50;
            String t = "м";
            if (disInt <= 100) {
                return ">" + String.valueOf(disInt) + t;
            } else if (disInt <= 950){
                disInt += 50;
                return ">" + String.valueOf(disInt) + t;
            } else if (disInt > 1000){
                disInt /= 1000;
                t = "км";
                return ">" + String.valueOf(disInt) + t;
            }
            return ">" + String.valueOf(disInt) + t;
        }
    }

}
