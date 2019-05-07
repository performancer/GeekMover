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


/**
 * Map is used to draw and update user's location a Google Map in the JogActivity
 * @author Emil
 */
public class Map {

    private GoogleMap map;
    private Marker locationMarker;

    private Polyline polyline;
    private PolylineOptions polylineOptions;


    /**
     * Constructor for Map
     */
    public Map() {
        this.polyline = null;
        this.polylineOptions = null;
        this.locationMarker = null;
    }

    /**
     * getter method for PolyLine
     *
     * @return polyline
     */
    public Polyline getPolyline() {
        return polyline;
    }

    /**
     * getter method for LocationMarker
     *
     * @return polyline
     */
    public Marker getLocationMarker() {
        return locationMarker;
    }

    /**
     * setter method for map variable
     *
     * @param map desired map
     */
    public void setMap(GoogleMap map) {
        this.map = map;
    }

    /**
     *  Calls createPolyLine method if there is no polyline on the map, otherwise calls updatePolyLine method
     *
     * @param latLngList desired list
     */
    public void drawPolyLine(List<LatLng> latLngList){
        if(this.polylineOptions == null){ // create a polyline if there is no polyline
            createPolyLine();
        }

        if (this.polylineOptions!= null) { // if polyline exists, update it
            updatePolyLine(latLngList);
        }
    }

    /**
     * Creates a polyline to the Google Map and sets a width and color to the line.
     */
    public void createPolyLine(){
        polylineOptions = new PolylineOptions().width(9).color(Color.RED).geodesic(true);
        polyline = map.addPolyline(polylineOptions);
    }

    /**
     * Updates the polyline based on a latitude-longitude list (LatLng).
     *
     * @param latLngList desired list
     */
    public void updatePolyLine(List<LatLng> latLngList){
        polyline.setPoints(latLngList); //draw a polyline based on all points
    }

    /**
     * Moves the camera based on user's location. Zooms to a more suitable level.
     *
     * @param latestCoordinates desired coordinates
     * @see Coordinates
     */
    public void updateCamera(Coordinates latestCoordinates){
        LatLng myLocation = latestCoordinates.getLatLng();

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15f));
    }

    /**
     * Creates a Google Map marker to the starting location
     *
     * Modified from:
     * @param latestCoordinates desired coordinates
     * @see Coordinates
     */
    public void createStartMarker(Coordinates latestCoordinates){
        LatLng myLocation = latestCoordinates.getLatLng();

        map.addMarker(new MarkerOptions()
                .position(myLocation)
                .title("StartLocation")
                .alpha(0.5f)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
    }

    /**
     * Creates a Google Map marker to the the current location of the user
     *
     *
     * @param latestCoordinates desired coordinates
     * @see Coordinates
     */
    public void createLocationMarker(Coordinates latestCoordinates){
        LatLng myLocation = latestCoordinates.getLatLng();
        locationMarker = map.addMarker(new MarkerOptions()
                .position(myLocation)
                .title("CurrentLocation")
                .alpha(1.0f)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
    }

    /**
     * Updates the location marker based on user's current location
     *
     * @param latestCoordinates desired coordinates
     * @see Coordinates
     */
    public void updateMarker(Coordinates latestCoordinates){
        if(locationMarker != null){
            LatLng myLocation = latestCoordinates.getLatLng();
            locationMarker.setPosition(myLocation);
        }
    }
}
