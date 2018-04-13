package com.example.yiming.gps_test;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter{
    Context context;
    List<MyLocation> locationList;

    LocationAdapter(Context c,List<MyLocation> list){
        context=c;
        locationList=list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.location_layout,parent,false);
        return new LocationHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyLocation myLocation=locationList.get(position);
        LocationHolder locationHolder= (LocationHolder) holder;
        locationHolder.id.setText(myLocation.getID());
        locationHolder.name.setText(myLocation.getName());
        locationHolder.latitude.setText(myLocation.getLatitude());
        locationHolder.longitude.setText(myLocation.getLongitude());
        locationHolder.address.setText(myLocation.getAddress());
        locationHolder.arivalTime.setText(myLocation.getArrivalTime());

    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    class LocationHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView id;
        TextView name;
        TextView latitude;
        TextView longitude;
        TextView address;
        TextView arivalTime;


        public LocationHolder(View itemView) {
            super(itemView);
            id=itemView.findViewById(R.id.ID);
            name=itemView.findViewById(R.id.Name);
            latitude=itemView.findViewById(R.id.Latitude);
            longitude=itemView.findViewById(R.id.Longitude);
            address=itemView.findViewById(R.id.Address);
            arivalTime=itemView.findViewById(R.id.ArrivalTime);
            itemView.setOnClickListener(this);
        }
        //jump to mapActivity if click item
        @Override
        public void onClick(View v) {
            Log.i("onclick ",id.getText().toString());
            Intent intent=new Intent(context,MapsActivity.class);
            intent.putExtra("latitude",latitude.getText().toString());
            intent.putExtra("longitude",longitude.getText().toString());
            intent.putExtra("id",id.getText().toString());
            intent.putExtra("name",name.getText().toString());
            intent.putExtra("address",address.getText().toString());
            intent.putExtra("arivalTime",arivalTime.getText().toString());
            context.startActivity(intent);
        }
    }
}
