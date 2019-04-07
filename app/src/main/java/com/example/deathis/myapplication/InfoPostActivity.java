package com.example.deathis.myapplication;

import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.util.Random;

public class InfoPostActivity extends AppCompatActivity {

    private String title, text;
    private float lat, lng;
    private String dis, nik, uid;
    private double D;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private int i;
    private ImageButton ar_up, ar_down;
    private boolean ar_up_b, ar_down_b;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_post);


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

        final TextView textViewUser = findViewById(R.id.textview_user_info);
        TextView textViewRep = findViewById(R.id.textview_rep1_info);
        TextView textViewText = findViewById(R.id.textview_text_info);
        TextView textViewTitle = findViewById(R.id.title_info_info);
        TextView textViewMet = findViewById(R.id.meters_info_info);
        ar_down = findViewById(R.id.arrow_down);
        ar_up = findViewById(R.id.arrow_up);

        textViewTitle.setText(title);
        textViewText.setText(text);

        ar_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ar_up.setBackground(getResources().getDrawable(R.drawable.ic_arrow_upward_green_24dp));
                ar_down.setBackground(getResources().getDrawable(R.drawable.ic_arrow_downward_black_24dp));
            }
        });

        ar_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ar_down.setBackground(getResources().getDrawable(R.drawable.ic_arrow_downward_blue_24dp));
                ar_up.setBackground(getResources().getDrawable(R.drawable.ic_arrow_upward_black_24dp));
            }
        });


        if (D != 0 && D < 20000) {
            textViewMet.setText(dis);
        } else {
            textViewMet.setText("");
        }

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

        MapFragment mapFragment = new MapFragment(latLng, 17);
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout2_info, mapFragment).commit();

        myRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

    }


}
