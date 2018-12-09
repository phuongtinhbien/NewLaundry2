package com.example.vuphu.newlaundry.Order.Activity;

import android.Manifest;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.vuphu.newlaundry.CurrentUserQuery;
import com.example.vuphu.newlaundry.GetCustomerQuery;
import com.example.vuphu.newlaundry.GetServiceBranchQuery;
import com.example.vuphu.newlaundry.Graphql.GraphqlClient;
import com.example.vuphu.newlaundry.Order.Adapter.CustomInfoWindowAdapter;
import com.example.vuphu.newlaundry.Order.OBBranch;
import com.example.vuphu.newlaundry.Order.OBOrderDetail;
import com.example.vuphu.newlaundry.Popup.Popup;
import com.example.vuphu.newlaundry.R;
import com.example.vuphu.newlaundry.Utils.PermissionUtils;
import com.example.vuphu.newlaundry.Utils.PreferenceUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import static com.example.vuphu.newlaundry.Utils.LocationUtils.getDistanceFromLocation;
import static com.example.vuphu.newlaundry.Utils.StringKey.DROPOFF_KEY;
import static com.example.vuphu.newlaundry.Utils.StringKey.ID_BRANCH;
import static com.example.vuphu.newlaundry.Utils.StringKey.LATITUDE;
import static com.example.vuphu.newlaundry.Utils.StringKey.LIST_SERVICE;
import static com.example.vuphu.newlaundry.Utils.StringKey.LONGITUDE;
import static com.example.vuphu.newlaundry.Utils.StringKey.PICKUP_KEY;
import static com.example.vuphu.newlaundry.Utils.StringKey.SPECIAL_STRING;
import static com.example.vuphu.newlaundry.Utils.StringKey.TOTAL_PRICE;
import static com.example.vuphu.newlaundry.Utils.StringKey.TOTAL_WEIGHT;


public class PrepareOrderAddressActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;
    private GoogleMap mMap;
    private ArrayList<String> listService;
    private EditText pickUp,dropOff;
    private TextView customerName, customerAddress;
    private String token;
    private ArrayList<OBBranch> listBranchTemp;
    private ArrayList<OBBranch> listBranch;
    private FloatingActionButton prepareNext;
    private Popup popup;
    private LatLng location;
    private double latitude;
    private double longitude;
    private Marker myMarker;
    private String price;
    private static GetCustomerQuery.CustomerById customer;
    private float distanceCheckValue = 5000;
    private LatLngBounds.Builder builder;
    private int padding = 70;
    private String branchNameChoose;
    private String id;

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare_order_address);
        token = PreferenceUtil.getAuthToken(PrepareOrderAddressActivity.this);
        builder = new LatLngBounds.Builder();
        popup = new Popup(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        Intent intent = getIntent();
        if(intent.hasExtra(TOTAL_PRICE)){
            price = intent.getStringExtra(TOTAL_PRICE);
        }
        if(intent.hasExtra(LATITUDE)){
            latitude = intent.getDoubleExtra(LATITUDE, 0);
        }
        if(intent.hasExtra(LONGITUDE)) {
            longitude = intent.getDoubleExtra(LONGITUDE, 0);
        }
        location = new LatLng(latitude, longitude);
        pickUp = findViewById(R.id.prepare_order_address_pick_up);
        dropOff = findViewById(R.id.prepare_order_address_drop_off);
        customerName = findViewById(R.id.item_prepare_order_your_name);
        customerAddress = findViewById(R.id.item_prepare_order_your_address);
        prepareNext = findViewById(R.id.prepare_order_next);
        mapFragment.getMapAsync(this);
        listService = new ArrayList<>();
        listBranchTemp = new ArrayList<OBBranch>();
        listBranch = new ArrayList<OBBranch>();
        prepareNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleIdBranch();
                if(validate()){
                    Intent intent = new Intent(getApplicationContext(), InfoOrderActivity.class);
                    intent.putExtra(ID_BRANCH, id);
                    intent.putExtra(PICKUP_KEY, pickUp.getText().toString());
                    intent.putExtra(DROPOFF_KEY, dropOff.getText().toString());
                    intent.putStringArrayListExtra(LIST_SERVICE, listService);

                    intent.putExtra(TOTAL_PRICE, price);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext() ,getResources().getString(R.string.choose_map), Toast.LENGTH_LONG).show();
                }

            }
        });
        getCustomerInfo();
        initToolbar();


    }

    private void handleIdBranch() {
        for(OBBranch obBranch : listBranch) {
            if(obBranch.getBranchName().equals(branchNameChoose)) {
                id = obBranch.getId();
                break;
            }
        }
    }

    private boolean validate() {
        return !TextUtils.isEmpty(dropOff.getText().toString()) && !TextUtils.isEmpty(pickUp.getText().toString()) && !TextUtils.isEmpty(id);
    }

    private void getCustomerInfo() {
        customer = PreferenceUtil.getCurrentUser(getApplicationContext());
            customerName.setText(customer.fullName());
            customerAddress.setText(customer.address());
            pickUp.setText(customer.address());
            dropOff.setText(customer.address());
            if(location != null) {
                initMap();
            }
    }


    private void initMap() {
        popup.createLoadingDialog();
        popup.show();
       ArrayList<OBOrderDetail> list = PreferenceUtil.getListOrderDetail(PrepareOrderAddressActivity.this);
       for(OBOrderDetail obOrderDetail : list) {
           if(!listService.contains(obOrderDetail.getIdService())) {
               listService.add(obOrderDetail.getIdService());
           }
       }
        GraphqlClient.getApolloClient(token, false).query(GetServiceBranchQuery.builder().list(listService).build()).enqueue(new ApolloCall.Callback<GetServiceBranchQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<GetServiceBranchQuery.Data> response) {
                List<GetServiceBranchQuery.Node> nodes = response.data().allServiceTypeBranches().nodes();
                PrepareOrderAddressActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        handleList(nodes);
                    }
                });
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                Log.e("getServiceTypeBranch", e.getCause() +" - "+e);
            }
        });
    }

    private void handleList(List<GetServiceBranchQuery.Node> nodes) {
        for(GetServiceBranchQuery.Node node: nodes) {
            if(checkBranch(node, nodes)){
                float distance =  getDistanceFromLocation(location.latitude, location.longitude, Float.parseFloat(node.branchByBranchId().latidute()), Float.parseFloat(node.branchByBranchId().longtidute()));
                if(distance > 0){
                    listBranchTemp.add(new OBBranch(node.branchByBranchId().id(), node.branchByBranchId().branchName(), node.branchByBranchId().latidute(), node.branchByBranchId().longtidute(), node.branchByBranchId().address(), distance));
                }
            }
        }

        for(OBBranch obBranch: listBranchTemp) {
            if(listBranch.isEmpty()) {
                listBranch.add(obBranch);
            }
            else if(!listBranchcontains(obBranch)) {
                listBranch.add(obBranch);
            }
        }
        sortListBanch();
        initMarkerBranch();
    }

    private void sortListBanch() {
        Collections.sort(listBranch, new Comparator<OBBranch>() {
            @Override
            public int compare(OBBranch obBranch, OBBranch t1) {
                return Float.compare(obBranch.getDistance(), t1.getDistance());
            }
        });
    }

    private boolean isDistanceCondition(float distance, int i) {
        if(distanceCheckValue > 10000) {
            return false;
        }
        else if(distance > distanceCheckValue) {
            distanceCheckValue += 1000;
            if(i == 1){
                isDistanceCondition(distance, i);
            }
        }
        else {
            return true;
        }
        return false;
    }

    private void initMarkerBranch() {
        for(int i=0; i<listBranch.size(); i++) {
            if(isDistanceCondition(listBranch.get(i).getDistance(), i)){
                createMarker(listBranch.get(i).getBranchName(), Double.parseDouble(listBranch.get(i).getLatitude()), Double.parseDouble(listBranch.get(i).getLongitude()), listBranch.get(i).getBranchAddress());
            }
        }
        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.moveCamera(cu);
        popup.hide();
    }

    private void createMarker(String branchName, double latitude, double longitude, String branchAddress) {
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title(branchName)
                .snippet(branchAddress + SPECIAL_STRING + price)
                .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("icon_app",80,80)))
        );
        builder.include(new LatLng(latitude, longitude));
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getApplication()));
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                branchNameChoose = marker.getTitle();
                Toast.makeText(getApplicationContext(),  getResources().getString(R.string.you_choose) +" " + marker.getTitle(), Toast.LENGTH_LONG).show();
                marker.hideInfoWindow();
            }
        });


    }

    public Bitmap resizeMapIcons(String iconName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    private boolean listBranchcontains(OBBranch obBranch) {
        for(OBBranch obBranchtmp: listBranch) {
            if(obBranch.getId().equals(obBranchtmp.getId())) {
                return true;
            }
        }
        return false;
    }

    private boolean checkBranch(GetServiceBranchQuery.Node node, List<GetServiceBranchQuery.Node> nodes) {
        int count = 0;
        for(String serviceId : listService) {
            for(GetServiceBranchQuery.Node sb: nodes) {
                if(sb.branchByBranchId().id().equals(node.branchByBranchId().id())){
                    if(sb.serviceTypeId().equals(serviceId)){
                        count++;
                        break;
                    }
                }
            }
        }
        return count == listService.size();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }



    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.your_address);
        toolbar.getMenu().findItem(R.id.menu_location_action).setVisible(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_location, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(10);
        if(location != null) {
            myMarker = mMap.addMarker(new MarkerOptions()
                    .position(location)
                    .title("My Address")
            );
            builder.include(location);
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
//            Circle circle = mMap.addCircle(new CircleOptions()
//                    .center(location)
//                    .radius(5000)
//                    .strokeColor(Color.RED));
        } else {
            Toast.makeText(PrepareOrderAddressActivity.this, "Adrress null", Toast.LENGTH_LONG).show();
        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(marker.equals(myMarker)) {
                    return true;
                }
                return false;
            }
        });
    }
}
