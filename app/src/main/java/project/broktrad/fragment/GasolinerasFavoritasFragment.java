package project.broktrad.fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import project.broktrad.R;
import project.broktrad.adapter.AdapterFavoritos;
import project.broktrad.adapter.AdapterGasolineras;
import project.broktrad.pojo.Gasolinera;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GasolinerasFavoritasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GasolinerasFavoritasFragment extends Fragment {

    private ArrayList<Gasolinera> gasolineras;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GasolinerasFavoritasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OperacionesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GasolinerasFavoritasFragment newInstance(String param1, String param2) {
        GasolinerasFavoritasFragment fragment = new GasolinerasFavoritasFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        gasolineras = (ArrayList<Gasolinera>) getArguments().get("GasolinerasFavoritas");

        View myInflatedView = inflater.inflate(R.layout.fragment_gasolineras, container, false);

        ListView listadoOperaciones = (ListView) myInflatedView.findViewById(R.id.listViewGasolineras);

        listadoOperaciones.setAdapter(new AdapterGasolineras(this, gasolineras));

        listadoOperaciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> list, View view, int pos, long id) {
                Fragment frgDatosGasol = new DatosFragment();
                Bundle args = new Bundle();
                args.putSerializable("Gasolinera", gasolineras.get(pos));
                frgDatosGasol.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, frgDatosGasol)
                        .addToBackStack(null).commit();
            }
        });
        return myInflatedView;
    }

}