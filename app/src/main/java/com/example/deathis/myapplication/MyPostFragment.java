package com.example.deathis.myapplication;

import android.content.Context;
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

        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();

        ref = myRef.child("posts");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    if(post.getAuthor().toString().equals(mAuth.getUid())){
                        arrayList.add(post);
                    } else {

                    }

                }

                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(layoutManager);

                MapsActivity mapsActivity = new MapsActivity();


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
