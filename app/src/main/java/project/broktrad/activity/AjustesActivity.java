package project.broktrad.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import project.broktrad.R;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.MenuItem;

import java.util.Locale;

public class AjustesActivity extends android.preference.PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(android.R.id.content, new PreferenciasFragment());
        ft.commit();

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
            if (key.equals("idioma")) {
                Preference preference = findPreference(key);
                preference.setSummary(sharedPreferences.getString(key, ""));

                if (sharedPreferences.getString(key, "").equalsIgnoreCase("ESP")) {
                    localizacion = new Locale("es", "ES");
                } else if (sharedPreferences.getString(key, "").equalsIgnoreCase("ENG")) {
                    localizacion = new Locale("en", "US");
                }
                Locale.setDefault(localizacion);
                config = new Configuration();
                config.locale = localizacion;
                getResources().updateConfiguration(config, null);
            }

                /*if(key.equals("reproducirMusica")){
                    mediaPlayer = MediaPlayer.create(context.getActivity(), R.raw.sound_relax);
                    if (sharedPreferences.getBoolean(key,false) == true){
                        mediaPlayer.start();
                        Log.i("Start", "");
                    }else {
                        mediaPlayer.stop();
                        Log.i("Stop", "");
                    }
                }

                if(key.equals("color_fondo_botones")){

                    PrincipalActivity.colorFondoBotones(sharedPreferences);
                }*/

        }

    }

}