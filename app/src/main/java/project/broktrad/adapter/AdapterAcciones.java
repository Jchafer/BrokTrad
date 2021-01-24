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
import project.broktrad.pojo.Accion;

public class AdapterAcciones extends ArrayAdapter<Accion> {
    Activity context;
    ArrayList<Accion> datos;

    public AdapterAcciones(Fragment context, ArrayList<Accion> datos) {
        super(context.getActivity(), R.layout.layout_accion_list, datos);
        this.context = context.getActivity();
        this.datos = datos;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View item = inflater.inflate(R.layout.layout_accion_list, null);
        TextView nombreAccion = (TextView) item.findViewById(R.id.txtNombreAccion);
        nombreAccion.setText(datos.get(position).getNombre());
        TextView valorAccion = (TextView) item.findViewById(R.id.txtValorAccion);
        valorAccion.setText(String.valueOf(datos.get(position).getValor()));
        return (item);
    }
}
