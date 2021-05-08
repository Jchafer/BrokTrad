package project.broktrad.activity;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import project.broktrad.R;
import project.broktrad.pojo.Gasolinera;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Gasolinera gasolinera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        gasolinera = (Gasolinera) getIntent().getSerializableExtra("gasolinera");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        double lat = Double.parseDouble(gasolinera.getLatitud().replaceAll(",", "."));
        double lng = Double.parseDouble(gasolinera.getLongitud().replaceAll(",", "."));

        Log.e("Lat", String.valueOf(lat));
        Log.e("Lng", String.valueOf(lng));

        // Añade un marcador con la gasolinera recibida
        LatLng gasolineraMarker = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(gasolineraMarker).title("Gasolinera " +
                gasolinera.getRotulo()).snippet(gasolinera.getMunicipio() + " " + gasolinera.getDireccion()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(gasolineraMarker, 16.0f));
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

    }
}