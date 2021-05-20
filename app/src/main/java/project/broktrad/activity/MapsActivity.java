package project.broktrad.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.SphericalUtil;

import java.util.List;

import project.broktrad.R;
import project.broktrad.pojo.Gasolinera;
import project.broktrad.utilities.PermissionUtils;
import project.broktrad.utilities.Validaciones;

public class MapsActivity extends AppCompatActivity implements OnMyLocationButtonClickListener,
        OnMyLocationClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean permissionDenied = false;
    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap mMap;
    private Gasolinera gasolinera;
    private List<Gasolinera> gasolineras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtener el SupportMapFragment y notificar cuando el mapa está listo para usar
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        gasolinera = (Gasolinera) getIntent().getSerializableExtra("gasolinera");
        gasolineras = (List<Gasolinera>) getIntent().getSerializableExtra("Gasolineras");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (gasolinera != null) {

            double lat = Double.parseDouble(Validaciones.cambiarComaPunt(gasolinera.getLatitud()));
            double lng = Double.parseDouble(Validaciones.cambiarComaPunt(gasolinera.getLongitud()));

            // Añade un marcador con la gasolinera recibida
            LatLng gasolineraMarker = new LatLng(lat, lng);
            mMap.addMarker(new MarkerOptions().position(gasolineraMarker).title("Gasolinera " +
                    gasolinera.getRotulo()).snippet(gasolinera.getMunicipio() + " " + gasolinera.getDireccion()));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(gasolineraMarker, 16.0f));
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        } else {
            mMap.setOnMyLocationButtonClickListener(this);
            mMap.setOnMyLocationClickListener(this);
            enableMyLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                LatLng miPos = new LatLng(location.getLatitude(), location.getLongitude());
                                Gasolinera gasolineraMasCercana = null;
                                double distanciaActual = Double.MAX_VALUE;
                                double latitud;
                                double longitud;
                                LatLng posicionActual = null;

                                for(int i=0; i < gasolineras.size(); i++) {
                                    latitud = Double.parseDouble(Validaciones.cambiarComaPunt(gasolineras.get(i).getLatitud()));
                                    longitud = Double.parseDouble(Validaciones.cambiarComaPunt(gasolineras.get(i).getLongitud()));
                                    posicionActual = new LatLng(latitud, longitud);
                                    double distancia = SphericalUtil.computeDistanceBetween(miPos, posicionActual);
                                    if (distanciaActual > distancia) {
                                        gasolineraMasCercana = gasolineras.get(i);
                                        distanciaActual = distancia;
                                    }
                                }
                                double lat = Double.parseDouble(Validaciones.cambiarComaPunt(gasolineraMasCercana.getLatitud()));
                                double lng = Double.parseDouble(Validaciones.cambiarComaPunt(gasolineraMasCercana.getLongitud()));

                                LatLng gasolineraSelecLatLng = new LatLng(lat, lng);
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(gasolineraSelecLatLng, 16.0f));
                                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

                                mMap.addMarker(new MarkerOptions().position(gasolineraSelecLatLng).title("Gasolinera " +
                                        gasolineraMasCercana.getRotulo()).snippet(gasolineraMasCercana.getMunicipio() + " " + gasolineraMasCercana.getDireccion()));
                                mMap.addPolyline(new PolylineOptions()
                                        .add(new LatLng(location.getLatitude(), location.getLongitude()), new LatLng(lat, lng))
                                        .width(10)
                                        .color(Color.RED));
                            }
                        }
                    });
        }
    }

    private void enableMyLocation() {
        // [START maps_check_location_permission]
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            // Falta el permiso para acceder a la ubicación, muestra la justificación y solicita permiso
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
        // [END maps_check_location_permission]
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, R.string.ubicacion_actual, Toast.LENGTH_LONG).show();
    }

    // [START maps_check_location_permission_result]
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Habilita la capa de mi ubicación si se ha concedido el permiso
            enableMyLocation();
        } else {
            // Se denegó el permiso, mostrar un mensaje de error
            // [START_EXCLUDE]
            // Muestra el cuadro de diálogo de error de permiso faltante cuando se reanudan los fragmentos
            permissionDenied = true;
            // [END_EXCLUDE]
        }
    }
    // [END maps_check_location_permission_result]

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (permissionDenied) {
            // No se otorgó permiso, muestra el cuadro de diálogo de error
            showMissingPermissionError();
            permissionDenied = false;
        }
    }

    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}