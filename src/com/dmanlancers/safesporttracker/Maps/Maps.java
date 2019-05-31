/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dmanlancers.safesporttracker.Maps;

import com.dmanlancers.safesporttracker.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Context;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.Toast;

public class Maps extends FragmentActivity {
private GoogleMap map;
final int RQS_GooglePlayServices = 1;

@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
GooglePlayServicesUtil
.isGooglePlayServicesAvailable(getApplicationContext());
setContentView(R.layout.mapas);

map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
map.setMyLocationEnabled(true);

//LocationSource a = (LocationSource) getSystemService(Context.LOCATION_SERVICE);
//LocationManager b = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//map.setLocationSource(a);

Criteria criteria = new Criteria();

//criteria.setAltitudeRequired(false);
//criteria.setBearingRequired(false);
//criteria.setCostAllowed(true);

//String provider = b.getBestProvider(criteria, true);

//Location location1 = b.getLastKnownLocation(provider);




LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
String provider = locationManager.getBestProvider(criteria, true);
criteria.setAccuracy(Criteria.ACCURACY_FINE);
criteria.setPowerRequirement(Criteria.POWER_LOW);
Location location = locationManager.getLastKnownLocation(provider);
double lat = location.getLatitude();
double lng = location.getLongitude();
LatLng coordinate = new LatLng(lat, lng);
Marker marker;

//CameraPosition.Builder x = CameraPosition.builder();
//x.target(coordinate);
//x.zoom(17);


//Projection proj = map.getProjection();
//Point focus = proj.toScreenLocation(coordinate);

//map.animateCamera(CameraUpdateFactory.newLatLng(coordinate));
//map.animateCamera(CameraUpdateFactory.zoomTo(13));
map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

map.moveCamera(CameraUpdateFactory.newLatLng(coordinate));
map.moveCamera(CameraUpdateFactory.zoomBy(16));
//marker = map.addMarker(new MarkerOptions().position(coordinate)
//.title("").snippet("").draggable(true));
//marker.getPosition();
//LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
}
@Override
protected void onResume() {
 // TODO Auto-generated method stub
 super.onResume();

 int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
 
 if (resultCode == ConnectionResult.SUCCESS){
  //Toast.makeText(getApplicationContext(), 
   // "isGooglePlayServicesAvailable SUCCESS", 
    //Toast.LENGTH_LONG).show();
 }else{
  GooglePlayServicesUtil.getErrorDialog(resultCode, this, RQS_GooglePlayServices);
 }
 
}
}