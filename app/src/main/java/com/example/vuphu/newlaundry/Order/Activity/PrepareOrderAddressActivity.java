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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.example.vuphu.newlaundry.Utils.StringKey.SPECIAL_STRING;
import static com.example.vuphu.newlaundry.Utils.StringKey.TOTAL_PRICE;


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
    private static final String PICKUP_KEY = "PICKUP_KEY";
    private static final String DROPOFF_KEY = "DROPOFF_KEY";
    private Marker myMarker;
    private String price;
    private static GetCustomerQuery.CustomerById customer;

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare_order_address);
        token = PreferenceUtil.getAuthToken(PrepareOrderAddressActivity.this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        Intent intent = getIntent();
        if(intent.hasExtra(TOTAL_PRICE)){
            price = intent.getStringExtra(TOTAL_PRICE);
        }
        pickUp = findViewById(R.id.prepare_order_address_pick_up);
        dropOff = findViewById(R.id.prepare_order_address_drop_off);
        customerName = findViewById(R.id.item_prepare_order_your_name);
        customerAddress = findViewById(R.id.item_prepare_order_your_address);
        getCustomerInfo();
        prepareNext = findViewById(R.id.prepare_order_next);
        mapFragment.getMapAsync(this);
        listService = new ArrayList<>();
        listBranchTemp = new ArrayList<OBBranch>();
        listBranch = new ArrayList<OBBranch>();
        popup = new Popup(this);

        initMap();
        prepareNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    Intent intent = new Intent(getApplicationContext(), InfoOrderActivity.class);
                    intent.putExtra(PICKUP_KEY, pickUp.getText().toString());
                    intent.putExtra(DROPOFF_KEY, dropOff.getText().toString());
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext() ,"Address must not null", Toast.LENGTH_LONG).show();
                }

            }
        });
        initToolbar();


    }

    private boolean validate() {
        return !TextUtils.isEmpty(dropOff.getText().toString()) && !TextUtils.isEmpty(pickUp.getText().toString());
    }

    private void getCustomerInfo() {
        customer = PreferenceUtil.getCurrentUser(getApplicationContext());
        if(customer == null) {
            String idUser = PreferenceUtil.getIdUser(PrepareOrderAddressActivity.this);
            GraphqlClient.getApolloClient(token, false)
                    .query(GetCustomerQuery.builder()
                            .id(idUser).build())
                    .enqueue(new ApolloCall.Callback<GetCustomerQuery.Data>() {
                        @Override
                        public void onResponse(@NotNull Response<GetCustomerQuery.Data> response) {
                            customer = response.data().customerById();
                            if ( customer != null){
                                PreferenceUtil.setCurrentUser(PrepareOrderAddressActivity.this, customer);
                            }
                            PrepareOrderAddressActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    customerName.setText(customer.fullName());
                                    customerAddress.setText(customer.address());
                                    pickUp.setText(customer.address());
                                    dropOff.setText(customer.address());
                                    location = getLocationFromAddress(customer.address());
                                }
                            });

                            location = getLocationFromAddress(customer.address());

                        }

                        @Override
                        public void onFailure(@NotNull ApolloException e) {
                            Log.e("customer_err", e.getCause() +" - "+e);
                        }
                    });
        }
        else {
            customerName.setText(customer.fullName());
            customerAddress.setText(customer.address());
            pickUp.setText(customer.address());
            dropOff.setText(customer.address());
            location = getLocationFromAddress(customer.address());
        }
    }

    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(PrepareOrderAddressActivity.this, Locale.getDefault());
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
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
                listBranchTemp.add(new OBBranch(node.branchByBranchId().id(), node.branchByBranchId().branchName(), node.branchByBranchId().latidute(), node.branchByBranchId().longtidute(), node.branchByBranchId().address()));
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
        initMarkerBranch();
    }

    private void initMarkerBranch() {
        for(int i=0; i<listBranch.size(); i++) {
            createMarker(listBranch.get(i).getBranchName(), Double.parseDouble(listBranch.get(i).getLatitude()), Double.parseDouble(listBranch.get(i).getLongitude()), listBranch.get(i).getBranchAddress());
        }
        popup.hide();
    }

    private void createMarker(String branchName, double latitude, double longitude, String branchAddress) {
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title(branchName)
                .snippet(branchAddress + SPECIAL_STRING + price)
                .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("ic_item_service",80,80)))
        );
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getApplication()));
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                marker.hideInfoWindow();
            }
        });
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));

    }

    public Bitmap resizeMapIcons(String iconName,int width, int height){
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        mMap.setMinZoomPreference(12);
        if(location != null) {
            myMarker = mMap.addMarker(new MarkerOptions()
                    .position(location)
                    .title("My Address")
            );
            mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
            Circle circle = mMap.addCircle(new CircleOptions()
                    .center(location)
                    .radius(10000)
                    .strokeColor(Color.RED)
                    .fillColor(Color.BLUE));
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
