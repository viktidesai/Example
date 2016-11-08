package com.mobileappclass.assignment3;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class MyService extends Service {

    //Declaring the variables
    DatabaseHelper dbHelper;
    public LocationListener listener;
    public LocationManager locationManager;
    public double latitude;
    public double longitude;
    public String dateTime;
    public ArrayList<String> getList;
    public MyService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        dbHelper = new DatabaseHelper(this, null, null, 1);
        getList= new ArrayList<>();
        getList = dbHelper.getRows();

    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    final class MyThreadClass implements Runnable {

        int SERVICE_ID;

        public MyThreadClass(int serviceID) {
            this.SERVICE_ID = serviceID;
        }

        @Override
        public void run() {

            int i = 0;

            synchronized (this) {
                while (i < 1000) {
                    try {


                        if (dateTime != null) {
                            dbHelper.addRow(dateTime, latitude, longitude);

                            //test
                            System.out.println(" Say Inserted row " + dateTime + " " + latitude + " " + longitude);
                        }


                        Intent intent = new Intent();
                        //intent.putExtra(MainActivity.MY_MSG, i);
                        // intent.setAction(MainActivity.MY_FILTER);
                        sendBroadcast(intent);
                        wait(10000);
                        i++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }

    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(MyService.this, "Service Started", Toast.LENGTH_SHORT).show();

        setLocationListener();
        performLocationCheck();

        Thread thread = new Thread(new MyThreadClass(startId));
        thread.start();
        return START_STICKY;
    }


    public void setLocationListener() {
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Date date = new Date(location.getTime());
                SimpleDateFormat getDate = new SimpleDateFormat("MM-dd HH:mm:ss a");
                getDate.setTimeZone(TimeZone.getTimeZone("EST"));


                dateTime = getDate.format(date);
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                System.out.println("\n899--- x " + location.getLatitude() + " y " + location.getLongitude());

                Toast.makeText(MyService.this, "Got the location!!!", Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                System.out.println(" disabled");
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }

        };
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void performLocationCheck() {



        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates("gps", 6000, 0, listener);
        //  }

    }

    public void onDestroy()
    {
        super.onDestroy();
        Toast.makeText(MyService.this, "Service Stopped", Toast.LENGTH_SHORT).show();
    }


}
