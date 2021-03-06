package com.example.deathis.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.deathis.myapplication.Fragments.MapFragment;
import com.example.deathis.myapplication.Models.Post;
import com.example.deathis.myapplication.Models.User;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OneMyPostActivity extends AppCompatActivity {

    private String title, text;
    private float lat, lng;
    private String dis, nik, uid;
    private double D;
    private DatabaseReference myRef, ref, del_ref;
    private FirebaseAuth mAuth;
    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_my_post);


        Bundle bundle = getIntent().getExtras();
        uid = bundle.getString("auth");
        title = bundle.getString("title");
        text = bundle.getString("text");
        lat = Float.parseFloat(bundle.getString("lat"));
        lng = Float.parseFloat(bundle.getString("lng"));
        dis = bundle.getString("meters");
        D = Double.parseDouble(bundle.getString("D"));
        i = bundle.getInt("rep_int");
        LatLng latLng = new LatLng(lat, lng);

        final TextView textViewUser = findViewById(R.id.textview_user);
        TextView textViewRep = findViewById(R.id.textview_rep1);
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
                            if (postSnapshot.child("author").getValue().toString().equals(mAuth.getUid()) &&
                                    postSnapshot.child("title").getValue().equals(title)) {
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

        myRef = FirebaseDatabase.getInstance().getReference();
        myRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (postSnapshot.getKey().toString().equals(mAuth.getUid())) {
                        User user = new User();
                        user = postSnapshot.getValue(User.class);
                        nik = user.getNik();
                        textViewUser.setText("@" + nik);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        textViewTitle.setText(title);
        textViewText.setText(text);
        textViewRep.setText(String.valueOf(i));


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
