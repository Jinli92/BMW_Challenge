package com.example.yiming.gps_test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyLocation {
    static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    String ID;
    String Name;
    String Latitude;
    String Longitude;
    String Address;
    String ArrivalTime;
    Double LatitudeD;
    Double LongitudeD;

    MyLocation(String id, String name, String latitude, String longitude, String address, String arrivalTime) {
        ID = id;
        Name = name;
        Latitude = latitude;
        Longitude = longitude;
        Address = address;
        ArrivalTime = arrivalTime;

        LatitudeD=Double.valueOf(latitude);
        LongitudeD=Double.valueOf(longitude);


    }

    public Double compareDistance(Double la, Double lon){
        Double distance=Math.pow((lon-LongitudeD),2) +  Math.pow((la-LatitudeD),2);
        distance=Math.sqrt(distance);
        return distance;
    }

    public int compareArraivlTime(String arrival)  {

//       String sdate=(new SimpleDateFormat("yyyy-MM-dd")).format(sdate);

//        //2018-04-16T05:02:19.787
//        Date d = format.parse(arrival);
//        Date t =format.parse(ArrivalTime);
        Timestamp ts2=Timestamp.valueOf(arrival);
        Timestamp ts1= Timestamp.valueOf(ArrivalTime);

        return ts1.getTime()>ts2.getTime()? 1:-1;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getArrivalTime() {
        return ArrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        ArrivalTime = arrivalTime;
    }
}

