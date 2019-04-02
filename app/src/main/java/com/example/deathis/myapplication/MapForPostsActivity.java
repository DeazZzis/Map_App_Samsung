package com.example.deathis.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.example.deathis.myapplication.MapFragment.MY_PERMISSIONS_REQUEST_LOCATION;

public class MapForPostsActivity extends AppCompatActivity {


    private GoogleMap Mmap;
    private GPSTracker gps;
    private Marker marker;
    private Button b;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_for_posts);


        b = findViewById(R.id.buttonSetPoint);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng pointLatLng = marker.getPosition();
                double lat = pointLatLng.latitude;
                double lng = pointLatLng.longitude;
                Intent intent = new Intent(MapForPostsActivity.this,
                        NewPostActivity.class);
                intent.putExtra("lat", lat);
                intent.putExtra("lng", lng);
                startActivity(intent);
            }
        });

        checkLocationPermission();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment()).commit();

        }
        gps = new GPSTracker(MapForPostsActivity.this);


        ((MapFragment) getFragmentManager().findFragmentById(R.id.maP)).getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Mmap = googleMap;

                try {
                    // Customise the styling of the base map using a JSON object defined
                    // in a raw resource file.
                    boolean success = Mmap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    MapForPostsActivity.this, R.raw.style_map));
                    if (!success) {
//                        Log.e(TAG, "Style parsing failed.");
                    }
                } catch (Resources.NotFoundException e) {
//                    Log.e(TAG, "Can't find style. Error: ", e);
                }

                setMarker();

                double curlat = gps.getLatitude();
                double curlon = gps.getLongitude();
                LatLng currentpos = new LatLng(curlat, curlon);
                Mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(curlat, curlon), 13));

                marker = Mmap.addMarker(new MarkerOptions().position(currentpos)
                        .title("Маркет події")
                        .snippet("Затисни і перемісти маркет для визначення місця")
                        .draggable(true));
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mark_start)));
                Mmap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

                    @Override
                    public void onMarkerDrag(Marker arg0) {
                        // TODO Auto-generated method stub
                        Log.d("Marker", "Dragging");
                    }

                    @Override
                    public void onMarkerDragEnd(Marker arg0) {
                        // TODO Auto-generated method stub
                        LatLng markerLocation = marker.getPosition();
                        Toast.makeText(MapForPostsActivity.this, markerLocation.toString(), Toast.LENGTH_LONG).show();
                        Log.d("Marker", "finished");
                    }

                    @Override
                    public void onMarkerDragStart(Marker arg0) {
                        // TODO Auto-generated method stub
                        Log.d("Marker", "Started");

                    }
                });
            }
        });
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MapForPostsActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {


                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MapForPostsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    public  void setMarker(){
        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();

        final ArrayList<Post> arrayList = new ArrayList<Post>();
        final Post post = new Post();

        ref = myRef.child("posts");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    arrayList.add(post);

                    float lat = Float.parseFloat(post.getLat());
                    float lng = Float.parseFloat(post.getLng());
                    String title = post.getTitle();

                    LatLng marketLL = new LatLng(lat, lng);
                    Mmap.addMarker(new MarkerOptions().position(marketLL).title(title));
                }

            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }

    public static class PlaceholderFragment extends Fragment {


        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container,
                    false);

            return rootView;
        }
    }


}
