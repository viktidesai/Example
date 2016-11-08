package com.mobileappclass.assignment3;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;



import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDatabase;
    public LinearLayout layout;
    public FragmentManager fragmentManager = getFragmentManager();
    ServerDatabase serverDatabase = new ServerDatabase();
    LocalDatabase localDatabase = new LocalDatabase();
    public ArrayList<String> dispList = new ArrayList<>();
    public static boolean wifiConnected = false;
    public static String wifiName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDatabase = new DatabaseHelper(this, null, null, 1);
        LocalDatabase fragment = new LocalDatabase();
        FragmentTransaction fragmentTransaction3 = fragmentManager.beginTransaction();
        fragmentTransaction3.add(R.id.fragment_container, fragment, "fragment_offline_tag");
        fragmentTransaction3.commit();
        startService(new Intent(getBaseContext(),MyService.class));
        layout = (LinearLayout) findViewById(R.id.fragment_container);
        getCurrentSsid(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater(); // reads XML
        inflater.inflate(R.menu.actionmenu, menu); // to create
        return super.onCreateOptionsMenu(menu); // the menu4


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case (R.id.offline): {
                FragmentTransaction fragmentTransaction1 = fragmentManager.beginTransaction();
                fragmentTransaction1.replace(R.id.fragment_container, localDatabase);
                fragmentTransaction1.commit();


            }

            case (R.id.online):{
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, serverDatabase);
                fragmentTransaction.commit();
                //stopService(new Intent(getBaseContext(),MyService.class));
            }

            default:
                return super.onOptionsItemSelected(item);


        }


    }

    public static void getCurrentSsid(Context context) {
        // String ssid = null;
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                wifiName = connectionInfo.getSSID();
                wifiConnected = true;

            }
        }
        System.out.println("299 Wifi name " + wifiName);
    }
}


