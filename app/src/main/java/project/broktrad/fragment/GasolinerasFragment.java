package project.broktrad.fragment;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import project.broktrad.R;
import project.broktrad.adapter.AdapterGasolineras;
import project.broktrad.pojo.Gasolinera;

public class GasolinerasFragment extends Fragment {

    private ArrayList<Gasolinera> gasolinerasApi;

    public GasolinerasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setRetainInstance(true);
        gasolinerasApi = (ArrayList<Gasolinera>) getArguments().get("Gasolineras");

        View myInflatedView = inflater.inflate(R.layout.fragment_gasolineras, container, false);

        ListView listadoGasolineras = (ListView) myInflatedView.findViewById(R.id.listViewGasolineras);

        listadoGasolineras.setAdapter(new AdapterGasolineras(this, gasolinerasApi));

        listadoGasolineras.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> list, View view, int pos, long id) {
                Fragment frgDatosGasol = new DatosFragment();
                Bundle args = new Bundle();
                args.putSerializable("Gasolinera", gasolinerasApi.get(pos));
                frgDatosGasol.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, frgDatosGasol)
                        .addToBackStack(null).commit();
            }
        });
        return myInflatedView;
    }
}