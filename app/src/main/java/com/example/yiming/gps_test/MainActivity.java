package com.example.yiming.gps_test;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.LocationListener;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    TextView latitude;
    TextView longitude;
    double mla;
    double mlon;
    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;
    FusedLocationProviderApi fusedLocationProviderApi;

    Button button;
    List<MyLocation> locationList;
    JsonArrayRequest jsonArrayRequest;
    RequestQueue queue;
    RecyclerView recyclerView;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        latitude = findViewById(R.id.latitude);
//        longitude = findViewById(R.id.longitude);


        // do nothing
        googleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).
                addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        googleApiClient.connect();
        fusedLocationProviderApi = LocationServices.FusedLocationApi;
        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        recyclerView=findViewById(R.id.recycler);
        locationList=new ArrayList<>();
//        button=findViewById(R.id.button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(MainActivity.this,MapsActivity.class);
//
//                startActivity(intent);
//            }
//        });


        //start to request location jsonArray
        queue= Volley.newRequestQueue(this);
        String url="http://localsearch.azurewebsites.net/api/Locations";
        jsonArrayRequest= new JsonArrayRequest(Request.Method.GET, url,null, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                JSONArray jsonArray= (JSONArray) response;
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject= null;
                            try {
                                jsonObject = jsonArray.getJSONObject(i);
                                MyLocation myLocation=new MyLocation(
                                        jsonObject.getString("ID"),
                                        jsonObject.getString("Name"),
                                        jsonObject.getString("Latitude"),
                                        jsonObject.getString("Longitude"),
                                        jsonObject.getString("Address"),
                                        jsonObject.getString("ArrivalTime"));
                                Log.i("MyLocation ", myLocation.getID());
                                locationList.add(myLocation);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        //sort list by name, address
                        Collections.sort(locationList,new MyComparator());

                        //put list into recyclerView
                        LocationAdapter locationAdapter=new LocationAdapter(MainActivity.this,locationList);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this,DividerItemDecoration.VERTICAL));
                        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        recyclerView.setAdapter(locationAdapter);

                    }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonArrayRequest);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        myLocationUpdate();

    }

    private void myLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location){
//        mla=location.getLatitude();
//        mlon=location.getLongitude();
//
//        latitude.setText(String.valueOf(mla));
//        longitude.setText(String.valueOf(mlon));
    }



    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(googleApiClient.isConnected()){
            myLocationUpdate();
        }
    }


    public class MyComparator implements Comparator{

        @Override
        public int compare(Object o1, Object o2) {
            MyLocation m1= (MyLocation) o1;
            MyLocation m2= (MyLocation) o2;
            if(m1.getName().equals(m2.getName())){
                return m1.getArrivalTime().compareTo(m2.getArrivalTime());
            }
            return  m1.getName().compareTo(m2.getName());
        }
    }

}
