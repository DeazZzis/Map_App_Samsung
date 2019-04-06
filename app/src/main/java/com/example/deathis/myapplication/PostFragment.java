package com.example.deathis.myapplication;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

public class PostFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private RVAdapter rvAdapter;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private DatabaseReference ref;
    private ArrayList<Post> post1;
    private int numberOfItems;
    private ArrayList<Post> arrayListPost;
    private ArrayList<Rep> arrayListRepUp, arrayListRepDown;
    private double myLat, myLng;


    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            myLat = bundle.getDouble("lat");
            myLng = bundle.getDouble("lng");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_post, container, false);

        recyclerView = view.findViewById(R.id.rv);

        arrayListPost = new ArrayList<Post>();

        arrayListRepUp = new ArrayList<Rep>();
        arrayListRepDown = new ArrayList<Rep>();

        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();

        ref = myRef.child("posts");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayListPost.clear();
                arrayListRepUp.clear();
                arrayListRepDown.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    arrayListRepUp.clear();
                    arrayListRepDown.clear();
                    for (DataSnapshot postSnapshotRep_up : postSnapshot.child("rep_up").getChildren()) {
                        Rep rep = postSnapshotRep_up.getValue(Rep.class);
                        arrayListRepUp.add(rep);
                    }
                    for (DataSnapshot postSnapshotRep_down : postSnapshot.child("rep_down").getChildren()) {
                        Rep rep = postSnapshotRep_down.getValue(Rep.class);
                        arrayListRepDown.add(rep);
                    }
                    Post post = new Post();
                    post.setTime(postSnapshot.child("time").getValue().toString());
                    post.setTitle(postSnapshot.child("title").getValue().toString());
                    post.setText(postSnapshot.child("text").getValue().toString());
                    post.setAuthor(postSnapshot.child("author").getValue().toString());
                    post.setLat(postSnapshot.child("lat").getValue().toString());
                    post.setLng(postSnapshot.child("lng").getValue().toString());
                    post.setRep_up(arrayListRepUp);
                    post.setRep_down(arrayListRepDown);
                    arrayListPost.add(post);
                }


                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(layoutManager);

                MapsActivity mapsActivity = new MapsActivity();

                GPSTracker gpsTracker = new GPSTracker(view.getContext());
                myLat = gpsTracker.getLatitude();
                myLng = gpsTracker.getLongitude();


                rvAdapter = new RVAdapter(arrayListPost, view.getContext(), myLat, myLng);
                recyclerView.setAdapter(rvAdapter);


            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });


        return view;
    }

}
