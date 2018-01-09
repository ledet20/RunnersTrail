package com.ledetawano.runnerstrail;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults != null && grantResults.length > 0) {

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)  == PackageManager.PERMISSION_GRANTED) {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         locationManager =  (LocationManager) this.getSystemService(LOCATION_SERVICE);

         locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                // location translated into language of users set device
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try {

                    //store current address in list
                    // code will run if the user initially doesnt have location services on but allows it when application runs
                    List<Address> listAddress = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                    if(listAddress != null && listAddress.size() > 0) {

                        // concatenate users full address if info is available
                        String fullAddress = "";

                        Toast.makeText(getApplicationContext(), listAddress.get(0).toString(), Toast.LENGTH_LONG).show();
                        
                    }


                } catch (IOException e) {

                    e.printStackTrace();
                }

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if(Build.VERSION.SDK_INT < 23) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        } else {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                // if the app is launched and we can get the users location  we want to get their last known location

                Location lastKnownlocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                Geocoder geo = new Geocoder(getApplicationContext(), Locale.getDefault());

                try {

                    List<Address> addressList = geo.getFromLocation(lastKnownlocation.getLatitude(), lastKnownlocation.getLongitude(), 1);

                    if(addressList != null && addressList.size() > 0) {

                        Toast.makeText(getApplicationContext(), addressList.get(0).toString(),Toast.LENGTH_LONG).show();

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                // LatLng currentLocation = new LatLng(lastKnownlocation.getLatitude(), lastKnownlocation.getLongitude());

            }

        }
    }
}
