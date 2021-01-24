package project.broktrad.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import project.broktrad.R;
import project.broktrad.activity.CuentaActivity;
import project.broktrad.activity.MainActivity;
import project.broktrad.activity.OperarActivity;
import project.broktrad.adapter.AdapterAcciones;
import project.broktrad.pojo.Accion;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccionesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccionesFragment extends Fragment {

    private ArrayList<Accion> acciones;
    
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccionesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccionesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccionesFragment newInstance(String param1, String param2) {
        AccionesFragment fragment = new AccionesFragment();
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
        acciones = (ArrayList<Accion>) getArguments().get("Acciones");

        View myInflatedView = inflater.inflate(R.layout.fragment_acciones, container, false);

        ListView listadoAcciones = (ListView) myInflatedView.findViewById(R.id.listViewAcciones);

        listadoAcciones.setAdapter(new AdapterAcciones(this, acciones));

        listadoAcciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> list, View view, int pos, long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), OperarActivity.class);
                intent.putExtra("Accion", acciones.get(pos));
                startActivity(intent);
            }
        });
        return myInflatedView;
    }
}