package project.broktrad.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import project.broktrad.R;
import project.broktrad.utilities.Validaciones;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class AjustesActivity extends android.preference.PreferenceActivity {

    SharedPreferences prefsManager;
    Locale localizacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(android.R.id.content, new PreferenciasFragment());
        ft.addToBackStack(null).commit();

        // Obtenemos referencia a las preferencias
        prefsManager = PreferenceManager.getDefaultSharedPreferences(AjustesActivity.this);

        // Cambiamos idioma según esté en preferencias
        if (!prefsManager.getString("idioma", "").isEmpty()){
            String idioma = prefsManager.getString("idioma", "");

            if (idioma.equalsIgnoreCase("ESP"))
                localizacion = new Locale("es", "ES");
            else if (idioma.equalsIgnoreCase("ENG"))
                localizacion = new Locale("en", "US");

            Locale.setDefault(localizacion);
            Configuration config = new Configuration();
            config.locale = localizacion;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }

    }

    // Cargar de nuevo la activity, para así aplicar los cambios de ajustes
    @Override
    public void onBackPressed()
    {
        if(getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
            Intent refrescar = new Intent(this, MainActivity.class);
            startActivity(refrescar);
            finish();
        }else
            super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class PreferenciasFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

        Locale localizacion;
        Configuration config;

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.opciones);

        }

        @Override
        public void onResume() {
            super.onResume();
            // Registrar escucha
            getPreferenceScreen().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            // Eliminar registro de la escucha
            getPreferenceScreen().getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            // Actualizar el resumen de la preferencia
            Preference preference = findPreference(key);
            preference.setSummary(sharedPreferences.getString(key, ""));

            if (key.equals("idioma")) {
                if (sharedPreferences.getString(key, "").equalsIgnoreCase("ESP")) {
                    localizacion = new Locale("es", "ES");
                } else if (sharedPreferences.getString(key, "").equalsIgnoreCase("ENG")) {
                    localizacion = new Locale("en", "US");
                }
                Locale.setDefault(localizacion);
                config = new Configuration();
                config.locale = localizacion;
                getResources().updateConfiguration(config, null);

                // Para aplicar los cambios al momento
                getActivity().recreate();
            }

            if(key.equals("nick")){
                SharedPreferences prefs = getActivity().getSharedPreferences("prefersUsuario", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("nick", sharedPreferences.getString(key, ""));
                editor.apply();
            }

            if(key.equals("email")){
                if (Validaciones.validaEmail(sharedPreferences.getString(key, ""))){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    user.updateEmail(sharedPreferences.getString(key, ""))
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("UPDATE", "User email address updated.");
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        builder.setTitle(R.string.cambiar_correo);
                                        builder.setMessage(R.string.email_cambiado);
                                        builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Log.d("Correct", "Email changed.");
                                            }
                                        });
                                        builder.show();
                                    }
                                }
                            });
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.cambiar_correo);
                    builder.setMessage(R.string.email_incorrecto);
                    builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d("Incorrect", "Email wrong.");
                        }
                    });
                    builder.show();
                }

            }

            if(key.equals("password")){
                if (Validaciones.validaClave(sharedPreferences.getString(key, ""))){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    user.updatePassword(sharedPreferences.getString(key, ""))
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("UPDATE", "User password updated.");
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        builder.setTitle(R.string.cambiar_pass);
                                        builder.setMessage(R.string.pass_cambiado);
                                        builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Log.d("Correct", "Password changed.");
                                            }
                                        });
                                        builder.show();
                                    }
                                }
                            });
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.cambiar_pass);
                    builder.setMessage(R.string.clave_incorrecta);
                    builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d("Incorrect", "Password wrong.");
                        }
                    });
                    builder.show();
                }
            }
        }
    }

}