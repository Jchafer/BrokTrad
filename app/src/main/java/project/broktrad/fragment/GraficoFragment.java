package project.broktrad.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import project.broktrad.R;
import project.broktrad.activity.MapsActivity;
import project.broktrad.pojo.Gasolinera;
import project.broktrad.pojo.GasolinerasJson;
import project.broktrad.pojo.Municipio;
import project.broktrad.service.ApiService;
import project.broktrad.utilities.Validaciones;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GraficoFragment extends Fragment {

    private LineChart lineChart;
    private LineDataSet lineGasolina95;
    private LineDataSet lineGasolina98;
    private LineDataSet lineGasoleoA;
    private LineDataSet lineGasoleoP;
    /*ArrayList<String> fechas = new ArrayList<>();
    ArrayList<Float> mediaPrecioGasolina95;
    ArrayList<Float> mediaPrecioGasolina98;
    ArrayList<Float> mediaPrecioGasoleoA;
    ArrayList<Float> mediaPrecioGasoleoP;*/

    public GraficoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_grafico, container, false);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar c = Calendar.getInstance();
        ArrayList<String> fechas = new ArrayList<>();
        ArrayList<Float> mediaPrecioGasolina95 = new ArrayList<>();
        ArrayList<Float> mediaPrecioGasolina98 = new ArrayList<>();
        ArrayList<Float> mediaPrecioGasoleoA = new ArrayList<>();
        ArrayList<Float> mediaPrecioGasoleoP = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            c.add(Calendar.DATE, -2);
            Date ultimaFecha = c.getTime();
            String fecha = sdf.format(ultimaFecha);
            fechas.add(fecha);
        }
        Log.e("Fechas", fechas.toString());
        for (int j = 0; j < 7; j++) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://sedeaplicaciones.minetur.gob.es/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ApiService apiService = retrofit.create(ApiService.class);
            Call<GasolinerasJson> call = apiService.getHistoricoValencia(fechas.get(j));
            call.enqueue(new Callback<GasolinerasJson>() {

                @Override
                public void onResponse(Call<GasolinerasJson> call, Response<GasolinerasJson> response) {
                    DecimalFormat df2 = new DecimalFormat("#.##");

                    float precioGasolina95 = 0;
                    int contGasolina95 = 0;
                    float precioGasolina98 = 0;
                    int contGasolina98 = 0;
                    float precioGasoleoA = 0;
                    int contGasoleoA = 0;
                    float precioGasoleoP = 0;
                    int contGasoleoP = 0;
                    for(Gasolinera gasolinera : response.body().getListaGasolineras()) {
                        if (!gasolinera.getPrecioGasolina95().isEmpty()) {
                            precioGasolina95 += Float.parseFloat(Validaciones.cambiarComaPunt(gasolinera.getPrecioGasolina95()));
                            contGasolina95++;
                        }
                        if (!gasolinera.getPrecioGasolina98().isEmpty()) {
                            precioGasolina98 += Float.parseFloat(Validaciones.cambiarComaPunt(gasolinera.getPrecioGasolina98()));
                            contGasolina98++;
                        }
                        if (!gasolinera.getPrecioGasoleoA().isEmpty()) {
                            precioGasoleoA += Float.parseFloat(Validaciones.cambiarComaPunt(gasolinera.getPrecioGasoleoA()));
                            contGasoleoA++;
                        }
                        if (!gasolinera.getPrecioGasoleoPremium().isEmpty()) {
                            precioGasoleoP += Float.parseFloat(Validaciones.cambiarComaPunt(gasolinera.getPrecioGasoleoPremium()));
                            contGasoleoP++;
                        }
                    }

                    /*Log.e("Precio", String.valueOf(precioGasolina95 + " -> " + contGasolina95 + "\n" +
                            precioGasolina98 + " -> " + contGasolina98 + "\n" +
                            precioGasoleoA + " -> " + contGasoleoA + "\n" +
                            precioGasoleoP + " -> " + contGasoleoP));*/
                    float mediaGasolina95 = precioGasolina95 / contGasolina95;
                    float mediaGasolina98 = precioGasolina98 / contGasolina98;
                    float mediaGasoleoA = precioGasoleoA / contGasoleoA;
                    float mediaGasoleoP = precioGasoleoP / contGasoleoP;

                    mediaPrecioGasolina95.add(mediaGasolina95);
                    mediaPrecioGasolina98.add(mediaGasolina98);
                    mediaPrecioGasoleoA.add(mediaGasoleoA);
                    mediaPrecioGasoleoP.add(mediaGasoleoP);

                    //Log.e("Media", df2.format(mediaGasolina95) + " " + df2.format(mediaGasolina98) + " " + df2.format(mediaGasoleoA) + " " + df2.format(mediaGasoleoP));
                    cargarGrafico(myInflatedView, mediaPrecioGasoleoP.size(), mediaPrecioGasolina95, mediaPrecioGasolina98, mediaPrecioGasoleoA, mediaPrecioGasoleoP);
                }
                @Override
                public void onFailure(Call<GasolinerasJson> call, Throwable t) {
                    Toast.makeText(getActivity(), R.string.sin_gasolineras, Toast.LENGTH_SHORT).show();
                }
            });
        }
        return myInflatedView;
    }

    public View cargarGrafico(View myInflatedView, int contador, ArrayList mediaPrecioGasolina95, ArrayList mediaPrecioGasolina98, ArrayList mediaPrecioGasoleoA, ArrayList mediaPrecioGasoleoP){
        if (contador == 7){
            // Enlazamos al XML
            lineChart = myInflatedView.findViewById(R.id.lineChart);

            // Creamos un set de datos
            ArrayList<Entry> dataGasolina95 = new ArrayList<Entry>();
            ArrayList<Entry> dataGasolina98 = new ArrayList<Entry>();
            ArrayList<Entry> dataGasoleoA = new ArrayList<Entry>();
            ArrayList<Entry> dataGasoleoP = new ArrayList<Entry>();
            //Log.e("cantidad", String.valueOf(mediaPrecioGasolina95.size()));
            for (int i = 0; i<6; i++){
                dataGasolina95.add(new Entry((float) i,(float)mediaPrecioGasolina95.get(i)));
                //Log.e("95", String.valueOf(mediaPrecioGasolina95.get(i)));
                dataGasolina98.add(new Entry((float) i,(float)mediaPrecioGasolina98.get(i)));
                //Log.e("98", String.valueOf(mediaPrecioGasolina98.get(i)));
                dataGasoleoA.add(new Entry((float) i,(float)mediaPrecioGasoleoA.get(i)));
                //Log.e("A", String.valueOf(mediaPrecioGasoleoA.get(i)));
                dataGasoleoP.add(new Entry((float) i,(float)mediaPrecioGasoleoP.get(i)));
                //Log.e("P", String.valueOf(mediaPrecioGasoleoP.get(i)));
            }

            // Unimos los datos al data set
            lineGasolina95 = new LineDataSet(dataGasolina95, "Gasolina95");
            lineGasolina95.setColor(50,100);
            lineGasolina98 = new LineDataSet(dataGasolina98, "Gasolina98");
            lineGasolina98.setColor(100,200);
            lineGasoleoA = new LineDataSet(dataGasoleoA, "GasoleoA");
            lineGasoleoA.setColor(150,300);
            lineGasoleoP = new LineDataSet(dataGasoleoP, "GasoleoP");
            lineGasoleoP.setColor(200,400);

            // Asociamos al gráfico
            LineData lineData = new LineData();
            lineData.addDataSet(lineGasolina95);
            lineData.addDataSet(lineGasolina98);
            lineData.addDataSet(lineGasoleoA);
            lineData.addDataSet(lineGasoleoP);
            lineChart.setData(lineData);
            return myInflatedView;
        }
        return null;
    }

}