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
import project.broktrad.pojo.Operacion;

public class AdapterOperaciones extends ArrayAdapter<Operacion> {
    Activity context;
    ArrayList<Operacion> datos;

    public AdapterOperaciones(Fragment context, ArrayList<Operacion> datos) {
        super(context.getActivity(), R.layout.layout_operacion_list, datos);
        this.context = context.getActivity();
        this.datos = datos;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View item = inflater.inflate(R.layout.layout_operacion_list, null);
        TextView nombreAccion = (TextView) item.findViewById(R.id.txtNombreAccionOper);
        nombreAccion.setText(datos.get(position).getAccion().getNombre());
        TextView valorAccion = (TextView) item.findViewById(R.id.txtValorAccionOper);
        valorAccion.setText(String.valueOf(datos.get(position).getValorAccion()));
        TextView inversionOper = (TextView) item.findViewById(R.id.txtInversionOper);
        inversionOper.setText(String.valueOf(datos.get(position).getInversion()));
        return (item);
    }
}
