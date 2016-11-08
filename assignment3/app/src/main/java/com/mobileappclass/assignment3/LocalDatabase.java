package com.mobileappclass.assignment3;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

//import com.mobileappclass.viktidesai.vikti_assignment3.R;

import java.util.ArrayList;


public class LocalDatabase extends Fragment {
    public ListView listView;
    DatabaseHelper dbHelper;
    public ArrayList<String> displayList = new ArrayList<>();
    ArrayAdapter adapter ;
    MyService service = new MyService();

    public LocalDatabase() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local_database, container, false);
        dbHelper = new DatabaseHelper(getActivity(),null, null,1);
        // Inflate the layout for this fragment
        listView = (ListView) view.findViewById(R.id.listView);
        getLocalList();
        return view;



    }




    public void getLocalList ()
    {
        ArrayList<String> locationList;
        locationList= dbHelper.getRows();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, locationList);
        listView.setAdapter(adapter);
    }


    // TODO: Rename method, update argument and hook method into UI event
}

