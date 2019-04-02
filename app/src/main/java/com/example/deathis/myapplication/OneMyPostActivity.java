package com.example.deathis.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class OneMyPostActivity extends AppCompatActivity {

    private String title, text;
    private float lat, lng;
    private String dis;
    private double D;
    private DatabaseReference myRef, ref, del_ref;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_my_post);


        Bundle bundle = getIntent().getExtras();
        title = bundle.getString("title");
        text = bundle.getString("text");
        lat = Float.parseFloat(bundle.getString("lat"));
        lng = Float.parseFloat(bundle.getString("lng"));
        dis = bundle.getString("meters");
        D = Double.parseDouble(bundle.getString("D"));
        LatLng latLng = new LatLng(lat, lng);

        TextView textViewText = findViewById(R.id.textview_text_one_my_post);
        TextView textViewTitle = findViewById(R.id.title_info_one_my_post);
        TextView textViewMet = findViewById(R.id.meters_info_one_my_post);

        Button button_remove = findViewById(R.id.buttonRemovePost);

        button_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<Post> arrayList = new ArrayList<Post>();

                mAuth = FirebaseAuth.getInstance();
                myRef = FirebaseDatabase.getInstance().getReference();

                ref = myRef.child("posts");
                del_ref = ref;
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        arrayList.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Post post = postSnapshot.getValue(Post.class);
                            if (post.getAuthor().toString().equals(mAuth.getUid()) &&
                                    post.getTitle().equals(title)) {
                                del_ref.child(postSnapshot.getKey()).removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                    }
                });

                startActivity(new Intent(OneMyPostActivity.this, MapsActivity.class));
                finish();
            }
        });

        textViewTitle.setText(title);
        textViewText.setText(text);


        if (D != 0 && D < 20000) {
            textViewMet.setText(dis);
        } else {
            textViewMet.setText("");
        }

        MapFragment mapFragment = new MapFragment(latLng, 17);
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout2_one_my_post, mapFragment).commit();

        myRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

    }


}
