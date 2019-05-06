package com.example.geekmover;

import android.graphics.Color;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import java.util.List;

public class MapClass {

    private GoogleMap map;

    private Polyline polyline;
    private PolylineOptions polylineOptions;

    public MapClass() {
        this.polyline = null;
        this.polylineOptions = null;
    }

    public void setMap(GoogleMap map) {
        this.map = map;
    }


    public void createPolyLine(){
        polylineOptions = new PolylineOptions().width(3).color(Color.RED).geodesic(true);
        polyline = map.addPolyline(polylineOptions);
    }

    public void drawPolyLine(List<LatLng> latLngList){
        if(this.polylineOptions == null){ // create a polyline if there is no polyline
            createPolyLine();
        }

        if (this.polylineOptions!= null) { // if polyline exists, update it
            updatePolyLine(latLngList);
        }
    }

    public void updatePolyLine(List<LatLng> latLngList){
        polyline.setPoints(latLngList); //draw a polyline based on all points
    }

    public void updateCamera(Coordinates latestCoordinates){
        LatLng myLocation = latestCoordinates.getLatLng();

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15f));
    }
}
