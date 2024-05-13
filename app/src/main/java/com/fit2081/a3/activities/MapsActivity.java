package com.fit2081.a3.activities;

import androidx.fragment.app.FragmentActivity;

import android.location.Geocoder;
import android.location.Address;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.fit2081.a3.databinding.ActivityMapsBinding;
import com.fit2081.a3.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        location = getIntent().getStringExtra("location");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses = new ArrayList<>();

        LatLng locationLatLng = new LatLng(0,0);

        try {
            addresses = geocoder.getFromLocationName(location, 1);
        } catch (IOException e) {
            Toast.makeText(this, "Category address not found", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        if (!addresses.isEmpty()) {
            locationLatLng = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
        } else {
            try {
                // default location. should always go to try and never to catch
                addresses = geocoder.getFromLocationName("Monash University Malaysia, Subang Jaya, Malaysia", 1);
                locationLatLng = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        mMap.addMarker(new MarkerOptions().position(locationLatLng).title(location));
        Log.d("MapsActivity", "Geocoded location: " + locationLatLng);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationLatLng, 10));
    }
}