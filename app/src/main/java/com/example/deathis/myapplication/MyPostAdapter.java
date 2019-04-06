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

//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;
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

public class MyPostAdapter extends RecyclerView.Adapter<MyPostAdapter.RViewHoldeR> {

    private static int viewHolderCount;
    private int numItems;
    private String title, text, lat, lng, meters;
    private ArrayList<Post> mainS;
    private Context parent;
    private float dis[] = new float[10];
    private double myLat, myLng;


    public MyPostAdapter(ArrayList<Post> mainS, Context context, double myLat, double myLng) {
        this.mainS = mainS;
        parent = context;
        this.myLat = myLat;
        this.myLng = myLng;
    }

    @Override
    public RViewHoldeR onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.post_list_item;

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);

        RViewHoldeR rViewHoldeR = new RViewHoldeR(view);

        return rViewHoldeR;
    }


    @Override
    public void onBindViewHolder(@NonNull RViewHoldeR holder, int position) {
        holder.bind(mainS.get(position).getTitle(), mainS.get(position).getLat(),
                mainS.get(position).getLng(), mainS.get(position));
    }

    @Override
    public int getItemCount() {
        return mainS.size();
    }


    class RViewHoldeR extends RecyclerView.ViewHolder {

        TextView textView_title;
        TextView textView_text;
        TextView textView_rep;


        double D;

        public RViewHoldeR(View itemView) {
            super(itemView);

            textView_title = itemView.findViewById(R.id.textview_title);
            textView_text = itemView.findViewById(R.id.textview_met);
            textView_rep = itemView.findViewById(R.id.textview_rep);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    title = mainS.get(position).getTitle();
                    text = mainS.get(position).getText();
                    lat = String.valueOf(mainS.get(position).getLat());
                    lng = String.valueOf(mainS.get(position).getLng());

                    if (D != 0 && D <= 15000) {
                        meters = String.valueOf((int) D) + "м";
                    } else {
                        meters = "";
                    }

                    Intent in = new Intent(parent, OneMyPostActivity.class);
                    in.putExtra("title", title);
                    in.putExtra("text", text);
                    in.putExtra("lat", lat);
                    in.putExtra("lng", lng);
                    in.putExtra("meters", meters);
                    in.putExtra("rep_int", mainS.get(position).getRep_up().size() -
                            mainS.get(position).getRep_down().size());
                    in.putExtra("auth", mainS.get(position).getAuthor());
                    in.putExtra("D", String.valueOf(D));
                    parent.startActivity(in);
                }
            });
        }

        void bind(String s1, String lat2, String lng2, Post post) {
            textView_title.setText(s1);
            int rep_size = post.getRep_up().size() - post.getRep_down().size();
            textView_rep.setText(String.valueOf(rep_size));

            D = distance(myLat, myLng, Float.parseFloat(lat2), Float.parseFloat(lng2));
            if (D != 0 && D <= 15000) {
                meters = String.valueOf((int) D) + "м";
            } else {
                meters = "0";
            }

            if (meters != "0") {
                textView_text.setText(meters);
            } else {
                textView_text.setVisibility(View.INVISIBLE);
            }
        }

        private double distance(final double myLat, final double myLng, final float eLat, final float eLng) {
            Location.distanceBetween(myLat, myLng, eLat, eLng, dis);
            return dis[0];
        }

        private String str_dis(double dist) {
            String d = "";

            return d;
        }
    }

}

