package com.mobileappclass.assignment3;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class ServerDatabase extends Fragment {

    DatabaseHelper dbHandler;
    public ListView listView;
    public Button syncButton;
    public TextView  serverStatus;
    public TextView  networkType;
    public static boolean checkWifi = false;
    public static String getWifiName;


    public ServerDatabase()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        dbHandler = new DatabaseHelper(getActivity(),null,null,1);
        final View view = inflater.inflate(R.layout.fragment_server_database, container, false);
        syncButton = (Button) view.findViewById(R.id.syncButton);
        listView = (ListView) view.findViewById(R.id.listView);
        serverStatus = (TextView) view.findViewById(R.id.serverStatus);
        networkType =(TextView)  view.findViewById(R.id.networkType);
        getConnectivity(getActivity());
        setNetworkType();



        

        // Inflate the layout for this fragment

        return view;


    }
    public static void getConnectivity(Context context) {
        // String ssid = null;
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                getWifiName = connectionInfo.getSSID();
                checkWifi = true;

            }
        }
        System.out.println("hello Wifi name " + getWifiName);
    }

    public void setNetworkType(){
        networkType.setText(getWifiName);
    }

    public void queryClick(){
        new MyAsyncTask().execute();
    }

    class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }

        @Override
        protected Void doInBackground(Void... params) {


            ArrayList<String> uploadList = dbHandler.getRows();
            int i =0;

            while (i < uploadList.size()) {

                String[] listItems = uploadList.get(i).split("\t");
                System.out.println(" 2299 " + uploadList.get(i));
                System.out.println(" 2299 -- " + listItems[0] + " " + listItems[1] +" " + listItems[2]);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Students"); // What database can I actually talk to?
                DatabaseReference students = ref.child("vdd23");
                DatabaseReference netid = students.child(listItems[0].replace("p.m.", ""));
                netid.child("date").setValue(listItems[0].replace("p.m.", ""));
                netid.child("netid").setValue("vdd23");
                netid.child("x").setValue(listItems[1]);
                netid.child("y").setValue(listItems[2], new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        System.out.println("Inserted into Firebase");
                    }
                });
                i++;
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            Toast.makeText(getActivity(), "Successfully uploaded to FireBase", Toast.LENGTH_SHORT).show();
        }
    }


}
