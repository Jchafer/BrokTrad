package project.broktrad.activity;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import project.broktrad.R;
import project.broktrad.dao.UsuarioDAO;
import project.broktrad.pojo.Usuario;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.MenuItem;

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

        prefsManager = PreferenceManager.getDefaultSharedPreferences(AjustesActivity.this);

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
        MediaPlayer mediaPlayer;
        PreferenciasFragment context = this;
        private UsuarioDAO usuarioDAO;

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
                getActivity().recreate();
            }

            if(key.equals("nick")){
                SharedPreferences prefs = getActivity().getSharedPreferences("prefersUsuario", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("nick", sharedPreferences.getString(key, ""));
                editor.apply();

                Usuario usuario = new Usuario(prefs.getString("email", "email@gmail.com"));
                usuarioDAO = new UsuarioDAO(getActivity());
                usuarioDAO.abrir();
                Usuario usuariosBD = (Usuario) usuarioDAO.search(usuario);

                ContentValues reg = new ContentValues();
                reg.put(usuarioDAO.C_COLUMNA_ID_EMAIL, usuariosBD.getEmail());
                reg.put(usuarioDAO.C_COLUMNA_CLAVE, usuariosBD.getClave());
                reg.put(usuarioDAO.C_COLUMNA_NICK, sharedPreferences.getString(key, ""));
                usuarioDAO.update(reg);
            }

        }

    }

}