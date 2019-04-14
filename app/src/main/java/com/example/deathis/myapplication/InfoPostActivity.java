package com.example.deathis.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.deathis.myapplication.Fragments.MapFragment;
import com.example.deathis.myapplication.Models.User;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InfoPostActivity extends AppCompatActivity {

    private String title, text;
    private float lat, lng;
    private String dis, nik, uid, key_user, k_rep_up, k_rep_down;
    private double D;
    private DatabaseReference myRef, ref;
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
        final TextView textViewRep = findViewById(R.id.textview_rep1_info);
        TextView textViewText = findViewById(R.id.textview_text_info);
        TextView textViewTitle = findViewById(R.id.title_info_info);
        TextView textViewMet = findViewById(R.id.meters_info_info);
        ar_down = findViewById(R.id.arrow_down);
        ar_up = findViewById(R.id.arrow_up);

        textViewTitle.setText(title);
        textViewText.setText(text);


//        String k = key();

//        ar_down_b = false;
//
//
//        if (check_rep_up()) {
//            ar_up.setBackground(getResources().getDrawable(R.drawable.ic_arrow_upward_green_24dp));
//            ar_down.setBackground(getResources().getDrawable(R.drawable.ic_arrow_downward_black_24dp));
//            ar_up_b = true;
//            myRef.child("posts").child(key()).child("rep_up").child(k_rep_up).removeValue();
//        } else {
//            ar_up_b = false;
//        }
//
//        if (check_rep_down()) {
//            ar_down.setBackground(getResources().getDrawable(R.drawable.ic_arrow_downward_blue_24dp));
//            ar_up.setBackground(getResources().getDrawable(R.drawable.ic_arrow_upward_black_24dp));
//            ar_down_b = true;
//            myRef.child("posts").child(key()).child("rep_down").child(k_rep_down).removeValue();
//        } else {
//            ar_down_b = false;
//        }
//


        ar_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ar_up.setBackground(getResources().getDrawable(R.drawable.ic_arrow_upward_green_24dp));
                ar_down.setBackground(getResources().getDrawable(R.drawable.ic_arrow_downward_black_24dp));
                i = +1;
                textViewRep.setText(String.valueOf(i));

//                ar_up_b = true;
//                ar_down_b = false;
            }
        });

        ar_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ar_down.setBackground(getResources().getDrawable(R.drawable.ic_arrow_downward_blue_24dp));
                ar_up.setBackground(getResources().getDrawable(R.drawable.ic_arrow_upward_black_24dp));
                i = -1;
                textViewRep.setText(String.valueOf(i));

//                ar_down_b = true;
//                ar_up_b = false;
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
                    if (postSnapshot.getKey().toString().equals(uid)) {
                        User user = new User();
                        user = postSnapshot.getValue(User.class);
                        nik = user.getNik();
                        key_user = user.getUid();
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

        textViewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfoPostActivity.this, MessageActivity.class);
                intent.putExtra("user_uid", key_user);
                startActivity(intent);
            }
        });

        MapFragment mapFragment = new MapFragment(latLng, 17);
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout2_info, mapFragment).commit();

        myRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onDestroy() {
//        Rep rep = new Rep();
//        rep.setAuth(mAuth.getUid());
//
//        if (ar_up_b) {
//            myRef.child("posts").child(key()).child("rep_up").push().setValue(rep);
//        } else {
//
//        }
//        if (ar_down_b) {
//            myRef.child("posts").child(key()).child("rep_down").push().setValue(rep);
//        } else {
//
//        }

        super.onDestroy();
    }

//    private String key() {
//        final String[] k = new String[1];
//
//        ref = FirebaseDatabase.getInstance().getReference();
//        ref.child("posts").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    if ( postSnapshot.child("title").getValue().equals(title) &&
//                            postSnapshot.child("text").getValue().equals(text)) {
//                        k[0] = postSnapshot.getKey();
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//            }
//        });
//
//        Toast.makeText(this, k[0], Toast.LENGTH_LONG);
//        return k[0];
//    }
//
//    private boolean check_rep_up() {
//        final boolean[] b = {false};
//
//
//        myRef = FirebaseDatabase.getInstance().getReference();
//
//        myRef.child("posts").child(key()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot postSnapshotRep_up : dataSnapshot.child("rep_up").getChildren()) {
//                    Rep rep = postSnapshotRep_up.getValue(Rep.class);
//                    if (rep.getAuth() == mAuth.getUid()) {
//                        b[0] = true;
//                        k_rep_up = postSnapshotRep_up.getKey();
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//            }
//        });
//
//        return b[0];
//    }
//
//    private boolean check_rep_down() {
//        final boolean[] b = {false};
//
//        myRef = FirebaseDatabase.getInstance().getReference();
//
//        myRef.child("posts").child(key()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot postSnapshotRep_down : dataSnapshot.child("rep_down").getChildren()) {
//                    Rep rep = postSnapshotRep_down.getValue(Rep.class);
//                    if (rep.getAuth() == mAuth.getUid()) {
//                        b[0] = true;
//                        k_rep_down = postSnapshotRep_down.getKey();
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//            }
//        });
//
//        return b[0];
//    }
}
