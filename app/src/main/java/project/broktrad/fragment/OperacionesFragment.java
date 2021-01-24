package project.broktrad.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import project.broktrad.R;
import project.broktrad.adapter.AdapterAcciones;
import project.broktrad.adapter.AdapterOperaciones;
import project.broktrad.pojo.Accion;
import project.broktrad.pojo.Operacion;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OperacionesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OperacionesFragment extends Fragment {

    private ArrayList<Operacion> operaciones;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OperacionesFragment() {
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
    public static OperacionesFragment newInstance(String param1, String param2) {
        OperacionesFragment fragment = new OperacionesFragment();
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
        operaciones = (ArrayList<Operacion>) getArguments().get("Operaciones");

        View myInflatedView = inflater.inflate(R.layout.fragment_operaciones, container, false);

        ListView listadoOperaciones = (ListView) myInflatedView.findViewById(R.id.listViewOperaciones);

        listadoOperaciones.setAdapter(new AdapterOperaciones(this, operaciones));

        listadoOperaciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> list, View view, int pos, long id) {
                Toast.makeText(getActivity(), "" + operaciones.get(pos).toString(), Toast.LENGTH_SHORT).show();
            }
        });
        return myInflatedView;
    }
}