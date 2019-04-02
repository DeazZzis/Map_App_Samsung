package com.example.deathis.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MapsActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    private LocationManager locationManager;
    private String provider;
    private BottomNavigationView bottomNavigationView;

    private PostFragment postFragment = new PostFragment();
    private FrameLayout frameLayout;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    private double myLat, myLng;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        myLat = getIntent().getDoubleExtra("lat", 0);
        myLng = getIntent().getDoubleExtra("lng", 0);


        mAuth = FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        tabLayout.setTabTextColors(Color.parseColor("#808080"), Color.parseColor("#353535"));

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            mAuth.signOut();
            startActivity(new Intent(MapsActivity.this,
                    LoginActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()) {
//            case R.id.map_nav_view:
//                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, mapFragment).commit();
//                return true;

            case R.id.post_nav_view:
                Bundle bundle = new Bundle();
                bundle.putDouble("lat", myLat);
                bundle.putDouble("lng", myLng);
                postFragment.setArguments(bundle);
                return true;
        }

        return false;
    }

    public double getMyLat() {
        return myLat;
    }

    public double getMyLng() {
        return myLng;
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }


        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new MapFragment();
                    break;
                case 1:
                    Bundle bundle = new Bundle();
                    bundle.putDouble("lat", myLat);
                    bundle.putDouble("lng", myLng);
                    fragment = new PostFragment();
                    fragment.setArguments(bundle);
                    break;
                case 2:
                    fragment = new MyPostFragment();
                    break;
            }
            return fragment;
        }


        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }


}
