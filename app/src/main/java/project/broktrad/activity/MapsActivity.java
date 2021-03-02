package project.broktrad.activity;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import project.broktrad.R;
import project.broktrad.pojo.Gasolinera;
import project.broktrad.pojo.Usuario;

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
        double lat = Double.parseDouble(gasolinera.getLatitud().replaceAll(",", "."));
        double lng = Double.parseDouble(gasolinera.getLongitud().replaceAll(",", "."));

        // Añade un marcador con la gasolinera recibida
        LatLng gasolineraMarker = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(gasolineraMarker).title("Gasolinera " +
                gasolinera.getRotulo()).snippet(gasolinera.getMunicipio() + " " + gasolinera.getDireccion()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(gasolineraMarker, 16.0f));
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

    }
}