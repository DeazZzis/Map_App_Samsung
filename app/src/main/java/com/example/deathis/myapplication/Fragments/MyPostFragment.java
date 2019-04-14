package com.example.deathis.myapplication.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.deathis.myapplication.Adapter.MyPostAdapter;
import com.example.deathis.myapplication.GPSTracker;
import com.example.deathis.myapplication.MapsActivity;
import com.example.deathis.myapplication.NewPostActivity;
import com.example.deathis.myapplication.Models.Post;
import com.example.deathis.myapplication.R;
import com.example.deathis.myapplication.Models.Rep;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyPostFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private ArrayList<Post> arrayList;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private DatabaseReference ref;
    private MyPostAdapter myPostAdapter;
    private double myLat, myLng;
    private Button button_createnew;
    private ArrayList<Rep> arrayListRepUp, arrayListRepDown;


    public MyPostFragment() {
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
        view = inflater.inflate(R.layout.fragment_mypost, container, false);

        recyclerView = view.findViewById(R.id.recyclerview2);
        arrayList = new ArrayList<Post>();
        arrayListRepUp = new ArrayList<Rep>();
        arrayListRepDown = new ArrayList<Rep>();

        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();

        ref = myRef.child("posts");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();
                arrayListRepUp.clear();
                arrayListRepDown.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
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
                    ArrayList<Rep> alru = new ArrayList<Rep>(arrayListRepUp);
                    ArrayList<Rep> alrd = new ArrayList<Rep>(arrayListRepDown);
                    post.setRep_up(alru);
                    post.setRep_down(alrd);
                    if(post.getAuthor().toString().equals(mAuth.getUid())){
                        arrayList.add(post);
                    } else {

                    }

                }

                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(layoutManager);

                MapsActivity mapsActivity = new MapsActivity();

                GPSTracker gpsTracker = new GPSTracker(view.getContext());
                myLng = gpsTracker.getLongitude();
                myLat = gpsTracker.getLatitude();

                myPostAdapter = new MyPostAdapter(arrayList, view.getContext(), myLat, myLng);
                recyclerView.setAdapter(myPostAdapter);


            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        button_createnew = view.findViewById(R.id.buttonCreateNewPost);
        button_createnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(),
                        NewPostActivity.class));
            }
        });

        return view;
    }

}
