package com.example.farhan.weather;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.gms.maps.model.LatLng;
import com.hamweather.aeris.communication.AerisCallback;
import com.hamweather.aeris.communication.EndpointType;
import com.hamweather.aeris.location.LocationHelper;
import com.hamweather.aeris.maps.AerisMapView;
import com.hamweather.aeris.maps.AerisMapView.AerisMapType;
import com.hamweather.aeris.maps.MapViewFragment;
import com.hamweather.aeris.maps.interfaces.OnAerisMapLongClickListener;
import com.hamweather.aeris.maps.interfaces.OnAerisMarkerInfoWindowClickListener;
import com.hamweather.aeris.maps.markers.AerisMarker;
import com.hamweather.aeris.model.AerisResponse;
import com.hamweather.aeris.response.EarthquakesResponse;
import com.hamweather.aeris.response.FiresResponse;
import com.hamweather.aeris.response.StormCellResponse;
import com.hamweather.aeris.response.StormReportsResponse;
import com.hamweather.aeris.tiles.AerisTile;


/**
 * Created by FARHAN_REMOFARHEEN on 12/10/2015.
 */


public class MapFragment extends MapViewFragment implements OnAerisMapLongClickListener, AerisCallback,
        OnAerisMarkerInfoWindowClickListener{
    private  Double lat;
    private  Double lon;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        lat = Double.parseDouble(bundle.getString("lat"));
        lon = Double.parseDouble(bundle.getString("lon"));
        View view = inflater.inflate(R.layout.map_fragment, container, false);
        mapView = (AerisMapView) view.findViewById(R.id.fragment_map);
        mapView.init(savedInstanceState, AerisMapType.GOOGLE);
        initMap(lat,lon);
        setHasOptionsMenu(true);
        return view;
    }

    /**
     * Inits the map with specific setting
     */
    private void initMap( double lat, double lon) {
        mapView.moveToLocation(new LatLng(lat,lon), 6);
        mapView.setOnAerisMapLongClickListener(this);
        mapView.setOnAerisWindowClickListener(this);
        mapView.addLayer(AerisTile.RADSAT);

    }

    @Override
    public void onResult(EndpointType endpointType, AerisResponse aerisResponse) {

    }

    @Override
    public void onMapLongClick(double v, double v1) {

    }

    @Override
    public void wildfireWindowPressed(FiresResponse firesResponse, AerisMarker aerisMarker) {

    }

    @Override
    public void stormCellsWindowPressed(StormCellResponse stormCellResponse, AerisMarker aerisMarker) {

    }

    @Override
    public void stormReportsWindowPressed(StormReportsResponse stormReportsResponse, AerisMarker aerisMarker) {

    }

    @Override
    public void earthquakeWindowPressed(EarthquakesResponse earthquakesResponse, AerisMarker aerisMarker) {

    }
}

