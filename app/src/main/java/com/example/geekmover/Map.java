package com.example.geekmover;

import android.graphics.Color;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import java.util.List;

public class Map {

    private GoogleMap map;
    //private Marker startMarker;
    private Marker locationMarker;

    private Polyline polyline;
    private PolylineOptions polylineOptions;

    public Map() {
        this.polyline = null;
        this.polylineOptions = null;
        this.locationMarker = null;
    }

    public Polyline getPolyline() {
        return polyline;
    }

    public Marker getLocationMarker() {
        return locationMarker;
    }

    public void setMap(GoogleMap map) {
        this.map = map;
    }

    public void createPolyLine(){
        polylineOptions = new PolylineOptions().width(9).color(Color.RED).geodesic(true);
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

    public void createStartMarker(Coordinates latestCoordinates){
        LatLng myLocation = latestCoordinates.getLatLng();

        map.addMarker(new MarkerOptions()
                .position(myLocation)
                .title("StartLocation")
                .alpha(0.5f)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
    }

    public void createLocationMarker(Coordinates latestCoordinates){
        LatLng myLocation = latestCoordinates.getLatLng();
        locationMarker = map.addMarker(new MarkerOptions()
                .position(myLocation)
                .title("CurrentLocation")
                .alpha(1.0f)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
    }

    public void updateMarker(Coordinates latestCoordinates){
        if(locationMarker != null){
            LatLng myLocation = latestCoordinates.getLatLng();
            locationMarker.setPosition(myLocation);
        }
    }
}
