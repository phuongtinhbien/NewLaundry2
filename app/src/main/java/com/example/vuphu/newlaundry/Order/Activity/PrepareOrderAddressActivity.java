package com.example.vuphu.newlaundry.Order.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.vuphu.newlaundry.R;
import com.mapfit.android.MapView;
import com.mapfit.android.Mapfit;
import com.mapfit.android.MapfitMap;
import com.mapfit.android.OnMapReadyCallback;
import com.mapfit.android.annotations.MapfitMarker;
import com.mapfit.android.annotations.MarkerOptions;
import com.mapfit.android.geometry.LatLng;
import com.mapfit.android.location.LocationListener;
import com.mapfit.android.location.LocationPriority;
import com.mapfit.android.location.ProviderStatus;

import org.jetbrains.annotations.NotNull;

public class PrepareOrderAddressActivity extends AppCompatActivity {

    public static final int LOCATION_PERMISSION_REQUEST_CODE = 99;
    public static final String MAP_API_KEY = "591dccc4e499ca0001a4c6a468d0230a15d34cefbe1636563804093e";
    MapView mapView;
    MapfitMap mapfitmap;
    private EditText pickUp,dropOff;

    private FloatingActionButton prepareNext;

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapfit.getInstance(this, MAP_API_KEY);
        setContentView(R.layout.activity_prepare_order_address);
        mapView = findViewById(R.id.mapView);
        pickUp = findViewById(R.id.prepare_order_address_pick_up);
        dropOff = findViewById(R.id.prepare_order_address_drop_off);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NotNull MapfitMap mapfitMap) {
                mapfitmap = mapfitMap;
                LatLng latLng = new LatLng(15.6069896, 96.8611285);
                mapfitmap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .icon(MapfitMarker.ARTS));
            }
        });
//        requestPermission();
        init();
        initToolbar();


    }

    private void init() {
        prepareNext = findViewById(R.id.prepare_order_next);

        prepareNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), InfoOrderActivity.class));
            }
        });
    }

    public void initMaker(LatLng latLng) {
        mapfitmap.setCenter(latLng);
        mapfitmap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(MapfitMarker.ARTS));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void requestPermission(){
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            }
            else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            mapfitmap.getMapOptions().setUserLocationEnabled(true, LocationPriority.HIGH_ACCURACY, new LocationListener() {
                @Override
                public void onLocation(final Location location) {
                    mapView.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(MapfitMap mapfitMap) {
                            LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
                            initMaker(position);
                        }
                    });
                }

                @Override
                public void onProviderStatus(ProviderStatus providerStatus) {

                }
            });
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission is granted
//                mapfitmap.getMapOptions().setUserLocationEnabled(true, LocationPriority.HIGH_ACCURACY, new LocationListener() {
//                    @Override
//                    public void onLocation(final Location location) {
//                        mapView.getMapAsync(new OnMapReadyCallback() {
//                            @Override
//                            public void onMapReady(MapfitMap mapfitMap) {
//                                LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
//                                initMaker(position);
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onProviderStatus(ProviderStatus providerStatus) {
//
//                    }
//                });
            } else {

            }
        }
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.your_address);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        if (item.getItemId() == R.id.menu_location_action){

        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_location, menu);
        return true;
    }
}
