package project.broktrad.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import project.broktrad.R;
import project.broktrad.adapter.AdapterGasolineras;
import project.broktrad.dao.GasolineraDAO;
import project.broktrad.pojo.Gasolinera;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BuscadorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BuscadorFragment extends Fragment {

    private EditText municipio;
    private Button buscar;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BuscadorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GasolFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BuscadorFragment newInstance(String param1, String param2) {
        BuscadorFragment fragment = new BuscadorFragment();
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
        View myInflatedView = inflater.inflate(R.layout.fragment_buscador, container, false);

        municipio = (EditText) myInflatedView.findViewById(R.id.editText_municipio);
        buscar = (Button) myInflatedView.findViewById(R.id.button_buscar);
        buscar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                String municipioIntroducido = municipio.getText().toString();
                GasolineraDAO gas = new GasolineraDAO(getActivity());
                gas.abrir();

                ArrayList<Gasolinera> gasolineras = gas.gasolinerasPorMunicipio(municipioIntroducido);
                if (gasolineras == null)
                    Toast.makeText(getActivity(), R.string.ningun_municipio, Toast.LENGTH_SHORT).show();
                else{
                    Fragment frgGasolineras = new GasolinerasFragment();
                    Bundle args = new Bundle();
                    args.putSerializable("Gasolineras", gasolineras);
                    frgGasolineras.setArguments(args);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, frgGasolineras)
                            .addToBackStack(null).commit();
                }



            }
        });

        return myInflatedView;
    }

}