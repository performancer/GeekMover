package com.example.geekmover;

import android.graphics.Color;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Arrays;
import java.util.List;

public class Map {

    private GoogleMap map;
    private JogProgram jogProgram;

    private Polyline polyline;
    private PolylineOptions polylineOptions;

    private List<LatLng> latLngList;
    private Coordinates coordinates;


    public Map()
    {
        this.polyline = null;
        this.polylineOptions = null;
        //this.coordinates = jogProgram.getLatestCoordinates();
        //this.latLngList = jogProgram.getLatLngList();

    }

    public void updatePolyLine(){

        latLngList = jogProgram.getLatLngList();

        polyline.setPoints(latLngList); //draw a polyline based on all points

        System.out.println(Arrays.toString(latLngList.toArray()));
    }

    public void updateCamera(){
        this.coordinates = jogProgram.getLatestCoordinates();

        LatLng myLocation = coordinates.getLatLng();

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15f));
    }


}
