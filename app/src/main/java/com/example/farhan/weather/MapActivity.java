package com.example.farhan.weather;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hamweather.aeris.communication.AerisEngine;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        AerisEngine.initWithKeys(this.getString(R.string.aeris_client_id), this.getString(R.string.aeris_client_secret), this);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        MapFragment myFragment = new MapFragment();

        Bundle bundle = getIntent().getExtras();
        myFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.MapFrame, myFragment);
        fragmentTransaction.commit();


    }
}
