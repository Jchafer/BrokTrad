package project.broktrad.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import project.broktrad.R;
import project.broktrad.pojo.Gasolinera;

public class AdapterFavoritos extends ArrayAdapter<Gasolinera> {
    Activity context;
    ArrayList<Gasolinera> datos;

    public AdapterFavoritos(Fragment context, ArrayList<Gasolinera> datos) {
        super(context.getActivity(), R.layout.layout_gasolinera_list, datos);
        this.context = context.getActivity();
        this.datos = datos;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View item = inflater.inflate(R.layout.layout_gasolineras_fav_list, null);
        TextView rotulo = (TextView) item.findViewById(R.id.txtRotulo);
        rotulo.setText(datos.get(position).getRotulo());
        TextView direccion = (TextView) item.findViewById(R.id.txtDireccion);
        direccion.setText(datos.get(position).getDireccion());
        TextView gas95 = (TextView) item.findViewById(R.id.txtPrecioGas95);
        gas95.setText(datos.get(position).getPrecioGasolina95());
        TextView gasA = (TextView) item.findViewById(R.id.txtPrecioGasA);
        gasA.setText(datos.get(position).getPrecioGasoleoA());
        return (item);
    }
}
