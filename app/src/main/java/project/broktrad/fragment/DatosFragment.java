package project.broktrad.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import project.broktrad.MapsActivity;
import project.broktrad.R;
import project.broktrad.dao.FavoritoDAO;
import project.broktrad.pojo.Gasolinera;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DatosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DatosFragment extends Fragment {

    private Gasolinera gasolinera;
    private Cursor cursor;
    private String email;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DatosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DatosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DatosFragment newInstance(String param1, String param2) {
        DatosFragment fragment = new DatosFragment();
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
        gasolinera = (Gasolinera) getArguments().get("Gasolinera");

        View myInflatedView = inflater.inflate(R.layout.fragment_datos, container, false);

        FavoritoDAO favDao = new FavoritoDAO(getContext());
        favDao.abrir();

        TextView rotulo = (TextView) myInflatedView.findViewById(R.id.textRotulo);
        TextView direccion = (TextView) myInflatedView.findViewById(R.id.textDireccion);
        TextView txtGas95 = (TextView) myInflatedView.findViewById(R.id.textGas95);
        TextView txtGas98 = (TextView) myInflatedView.findViewById(R.id.textGas98);
        TextView txtGasA = (TextView) myInflatedView.findViewById(R.id.textGasA);
        TextView txtGasP = (TextView) myInflatedView.findViewById(R.id.textGasP);
        TextView txtPreGas95 = (TextView) myInflatedView.findViewById(R.id.textPreGas95);
        TextView txtPreGas98 = (TextView) myInflatedView.findViewById(R.id.textPreGas98);
        TextView txtPreGasA = (TextView) myInflatedView.findViewById(R.id.textPreGasA);
        TextView txtPreGasP = (TextView) myInflatedView.findViewById(R.id.textPreGasP);
        TextView provincia = (TextView) myInflatedView.findViewById(R.id.textProvincia);
        TextView municipio = (TextView) myInflatedView.findViewById(R.id.textMunicipio);
        TextView codPostal = (TextView) myInflatedView.findViewById(R.id.textCodPostal);
        TextView horario = (TextView) myInflatedView.findViewById(R.id.textHorario);

        rotulo.setText(gasolinera.getRotulo());
        direccion.setText(gasolinera.getDireccion());
        if (gasolinera.getPrecioGasolina95().isEmpty()){
            txtGas95.setVisibility(View.INVISIBLE);
            txtPreGas95.setVisibility(View.INVISIBLE);
        }else {
            txtGas95.setVisibility(View.VISIBLE);
            txtPreGas95.setVisibility(View.VISIBLE);
            txtPreGas95.setText(gasolinera.getPrecioGasolina95());
        }
        if (gasolinera.getPrecioGasolina98().isEmpty()){
            txtGas98.setVisibility(View.INVISIBLE);
            txtPreGas98.setVisibility(View.INVISIBLE);
        }else {
            txtGas98.setVisibility(View.VISIBLE);
            txtPreGas98.setVisibility(View.VISIBLE);
            txtPreGas98.setText(gasolinera.getPrecioGasolina98());
        }
        if (gasolinera.getPrecioGasoleoA().isEmpty()){
            txtGasA.setVisibility(View.INVISIBLE);
            txtPreGasA.setVisibility(View.INVISIBLE);
        }else {
            txtGasA.setVisibility(View.VISIBLE);
            txtPreGasA.setVisibility(View.VISIBLE);
            txtPreGasA.setText(gasolinera.getPrecioGasoleoA());
        }
        if (gasolinera.getPrecioGasoleoPremium().isEmpty()){
            txtGasP.setVisibility(View.INVISIBLE);
            txtPreGasP.setVisibility(View.INVISIBLE);
        }else {
            txtGasP.setVisibility(View.VISIBLE);
            txtPreGasP.setVisibility(View.VISIBLE);
            txtPreGasP.setText(gasolinera.getPrecioGasoleoPremium());
        }
        provincia.setText(gasolinera.getProvincia());
        municipio.setText(gasolinera.getMunicipio());
        codPostal.setText(gasolinera.getCodPostal());
        horario.setText(gasolinera.getHorario());

        Button btnFavorito = (Button) myInflatedView.findViewById(R.id.buttonFavorito);

        SharedPreferences prefs = getActivity().getSharedPreferences("prefersUsuario", Context.MODE_PRIVATE);
        email = prefs.getString("email", "email@gmail.com");
        cursor = favDao.getRegistroUnico(gasolinera.getLongitud(), gasolinera.getLatitud(), email);
        if (cursor.getCount() != 0)
            btnFavorito.setText("ELIMINAR FAVORITO");
        else
            btnFavorito.setText("AÑADIR FAVORITO");

        btnFavorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnFavorito.getText().equals("ELIMINAR FAVORITO")) {
                    favDao.delete(gasolinera.getLongitud(), gasolinera.getLatitud(), email);
                    btnFavorito.setText("AÑADIR FAVORITO");
                    Toast.makeText(getContext(), "La gasolinera se ha eliminado de favoritos", Toast.LENGTH_SHORT).show();
                }else{
                    ContentValues reg = new ContentValues();
                    reg.put(FavoritoDAO.C_COLUMNA_ID_GASOL_LONG, gasolinera.getLongitud());
                    reg.put(FavoritoDAO.C_COLUMNA_ID_GASOL_LAT, gasolinera.getLatitud());
                    reg.put(FavoritoDAO.C_COLUMNA_ID_USUARIO, email);
                    favDao.add(reg);
                    btnFavorito.setText("ELIMINAR FAVORITO");
                    Toast.makeText(getContext(), "Gasolinera añadida a favoritos", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button btnMapa = (Button) myInflatedView.findViewById(R.id.buttonMapa);
        btnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MapsActivity.class);
                i.putExtra("gasolinera", gasolinera);
                startActivity(i);
            }
        });
        return myInflatedView;
    }
}