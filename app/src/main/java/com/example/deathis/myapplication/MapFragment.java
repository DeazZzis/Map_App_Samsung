package com.example.deathis.myapplication;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import kotlin.collections.MapAccessorsKt;

import static android.content.ContentValues.TAG;
import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.MODE_APPEND;
import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private String provider;
    private LatLng latLngLviv, latLng, myLocation;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private DatabaseReference ref;
    private int zoomS;
    private ArrayList<Post> arrayListPost;
    private ArrayList<Rep> arrayListRepUp, arrayListRepDown;

    public MapFragment() {

    }

    @SuppressLint("ValidFragment")
    public MapFragment(LatLng latLng, int zoomS) {
        this.latLng = latLng;
        this.zoomS = zoomS;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        final View view = inflater.inflate(R.layout.fragment_map, container, false);

        zoomS = 14;

        final SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                if (latLng == null){
                    GPSTracker gpsTracker = new GPSTracker(view.getContext());
                    latLngLviv = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
                    zoomC(latLngLviv, zoomS);
                } else {
                    mMap.addMarker(new MarkerOptions().position(latLng));
                    zoomC(latLng, zoomS);
                }

                setMarker();

                try {
                    // Customise the styling of the base map using a JSON object defined
                    // in a raw resource file.
                    boolean success = mMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    getContext(), R.raw.style_map));

                    if (!success) {
                        Log.e(TAG, "Style parsing failed.");
                    }
                } catch (Resources.NotFoundException e) {
                    Log.e(TAG, "Can't find style. Error: ", e);
                }


                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);

                } else {
                    checkLocationPermission();
                }
                mMap.setOnMyLocationButtonClickListener(new OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {

                        return false;
                    }
                });
                mMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
                    @Override
                    public void onMyLocationClick(@NonNull Location location) {

                    }
                });

            }
        });
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.map, mapFragment);

        return view;
    }

    public LatLng getMyLocation(){
        return myLocation;
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {


                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        locationManager.requestLocationUpdates(provider, 400, 1, (LocationListener) this);
                    }

                } else {


                }
                return;
            }

        }
    }


    private static final String TAG = MapsActivity.class.getSimpleName();

    public void zoomC(LatLng latLng, int zoom){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    public  void setMarker(){
        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();

        final ArrayList<Post> arrayList = new ArrayList<Post>();
        final Post post = new Post();

        arrayListPost = new ArrayList<Post>();

        arrayListRepUp = new ArrayList<Rep>();
        arrayListRepDown = new ArrayList<Rep>();

        ref = myRef.child("posts");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayListPost.clear();
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
                    arrayListPost.add(post);

                    float lat = Float.parseFloat(post.getLat());
                    float lng = Float.parseFloat(post.getLng());
                    String title = post.getTitle();

                    LatLng marketLL = new LatLng(lat, lng);
                    mMap.addMarker(new MarkerOptions().position(marketLL).title(title))
                            .setIcon(BitmapDescriptorFactory.defaultMarker(150));
                }

            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }

}