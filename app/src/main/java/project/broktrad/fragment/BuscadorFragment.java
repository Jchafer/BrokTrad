package project.broktrad.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import project.broktrad.R;
import project.broktrad.activity.MapsActivity;
import project.broktrad.pojo.Gasolinera;
import project.broktrad.pojo.GasolinerasJson;
import project.broktrad.pojo.Municipio;
import project.broktrad.service.ApiService;
import project.broktrad.utilities.Validaciones;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.text.Normalizer;

public class BuscadorFragment extends Fragment {

    private SharedPreferences prefs;
    private EditText municipio;
    private Button buscar;
    private Button buscarPosicion;
    private ArrayList<Municipio> municipios = new ArrayList<>();
    private Municipio municipioSeleccionado = null;
    private ArrayList<Gasolinera> gasolinerasSeleccionadas = new ArrayList<>();

    public BuscadorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setRetainInstance(true);
        View myInflatedView = inflater.inflate(R.layout.fragment_buscador, container, false);

        prefs = this.getActivity().getSharedPreferences("prefersUsuario", Context.MODE_PRIVATE);

        // Obtenemos la lista de municipios
        getMunicipios();

        municipio = (EditText) myInflatedView.findViewById(R.id.editText_municipio);
        municipio.setText("Albal");

        buscar = (Button) myInflatedView.findViewById(R.id.button_buscar);
        buscar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                String municipioIntroducido = municipio.getText().toString();
                gasolinerasSeleccionadas.clear();

                // Obtenemos nombre introducido y comprobamos si existe en la lista de municipios
                for (Municipio municipio : municipios) {
                    if (Validaciones.cleanString(municipio.getNombre().toUpperCase()).equalsIgnoreCase(Validaciones.cleanString(municipioIntroducido.toUpperCase()))){
                        municipioSeleccionado = municipio;
                    }
                }

                // Si el municipio existe le pasamos su id a apiService para obtener las gasolineras
                if (municipioSeleccionado != null)
                    getGasolinerasMunicipio(municipioSeleccionado.getID());

                else
                    Toast.makeText(getActivity(), R.string.ningun_municipio, Toast.LENGTH_SHORT).show();
                municipioSeleccionado = null;
            }
        });

        buscarPosicion = (Button) myInflatedView.findViewById(R.id.button_buscar_posicion);
        buscarPosicion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGasolineras();
            }
        });

        return myInflatedView;
    }

    private void getMunicipios() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://sedeaplicaciones.minetur.gob.es/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<Municipio>> call = apiService.getMunicipios();
        call.enqueue(new Callback<List<Municipio>>() {
            @Override
            public void onResponse(Call<List<Municipio>> call, Response<List<Municipio>> response) {
                for(Municipio municipio : response.body()) {
                    municipios.add(municipio);
                }
            }
            @Override
            public void onFailure(Call<List<Municipio>> call, Throwable t) {
                Toast.makeText(getActivity(), R.string.sin_municipios, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getGasolinerasMunicipio(String idMunicipio) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://sedeaplicaciones.minetur.gob.es/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<GasolinerasJson> call = apiService.getGasolinerasMunicipio(idMunicipio);
        call.enqueue(new Callback<GasolinerasJson>() {

            @Override
            public void onResponse(Call<GasolinerasJson> call, Response<GasolinerasJson> response) {
                for(Gasolinera gasolinera : response.body().getListaGasolineras()) {
                    gasolinerasSeleccionadas.add(gasolinera);
                }

                // Pasamos una lista de gasolineras que tiene el municipio
                Fragment frgGasolineras = new GasolinerasFragment();
                Bundle args = new Bundle();
                args.putSerializable("Gasolineras", gasolinerasSeleccionadas);
                frgGasolineras.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, frgGasolineras)
                        .addToBackStack(null).commit();

            }
            @Override
            public void onFailure(Call<GasolinerasJson> call, Throwable t) {
                Toast.makeText(getActivity(), R.string.sin_gasolineras, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getGasolineras() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://sedeaplicaciones.minetur.gob.es/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<GasolinerasJson> call = apiService.getGasolinerasValencia();
        call.enqueue(new Callback<GasolinerasJson>() {

            @Override
            public void onResponse(Call<GasolinerasJson> call, Response<GasolinerasJson> response) {
                for(Gasolinera gasolinera : response.body().getListaGasolineras()) {
                    gasolinerasSeleccionadas.add(gasolinera);
                }

                // Pasamos una lista de todas las gasolineras de Valencia
                Intent i = new Intent(getActivity(), MapsActivity.class);
                i.putExtra("Gasolineras", gasolinerasSeleccionadas);
                startActivity(i);
            }
            @Override
            public void onFailure(Call<GasolinerasJson> call, Throwable t) {
                Toast.makeText(getActivity(), R.string.sin_gasolineras, Toast.LENGTH_SHORT).show();
            }
        });
    }

}