package project.broktrad.service;

import java.util.List;

import project.broktrad.pojo.GasolinerasJson;
import project.broktrad.pojo.Municipio;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiService {
    String API_ROUTE_MUNICIPIOS_VALENCIA = "ServiciosRESTCarburantes/PreciosCarburantes/Listados/MunicipiosPorProvincia/46";
    String API_ROUTE_GASOLINERAS_VALENCIA = "ServiciosRESTCarburantes/PreciosCarburantes/EstacionesTerrestres/FiltroProvincia/46";
    String API_ROUTE_GASOLINERAS_MUNICIPIO = "ServiciosRESTCarburantes/PreciosCarburantes/EstacionesTerrestres/FiltroMunicipio/{idMunicipio}";

    @GET(API_ROUTE_MUNICIPIOS_VALENCIA)
    Call<List<Municipio>> getMunicipios();

    @GET(API_ROUTE_GASOLINERAS_VALENCIA)
    Call<GasolinerasJson> getGasolinerasValencia();

    @GET(API_ROUTE_GASOLINERAS_MUNICIPIO)
    Call<GasolinerasJson> getGasolinerasMunicipio(@Path("idMunicipio") String idMunicipio);

}
