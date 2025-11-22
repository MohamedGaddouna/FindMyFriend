package gaddounamohamed.grp1.findmyfriend;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import gaddounamohamed.grp1.findmyfriend.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private double latitude = -34;  // Default Sydney
    private double longitude = 151;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get location from intent
        if (getIntent() != null) {
            String latStr = getIntent().getStringExtra("latitude");
            String lonStr = getIntent().getStringExtra("longitude");

            if (latStr != null && lonStr != null) {
                try {
                    latitude = Double.parseDouble(latStr);
                    longitude = Double.parseDouble(lonStr);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }

        // Obtain the SupportMapFragment and get notified when the map is ready
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker at the received location
        LatLng location = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions()
                .position(location)
                .title("Position de votre ami"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
    }
}