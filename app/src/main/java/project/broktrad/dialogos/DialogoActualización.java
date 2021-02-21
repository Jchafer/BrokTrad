package project.broktrad.dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import project.broktrad.R;

public class DialogoActualización extends DialogFragment {
    public String info;
    public TextView datos;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();


        View view = inflater.inflate(R.layout.layout_dialog_movimiento, null);
        datos = view.findViewById(R.id.tvDatos);
        datos.setText(info);
        //Log.i("///////", "Mov: " + importe.getText());

        builder.setView(inflater.inflate(R.layout.layout_dialog_movimiento, null)).
                setView(view).
                setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }
}
