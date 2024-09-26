package com.dam.dietasgpt;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/recomendaciones_personalizadas")
    Call<List<Plato>> obtenerRecomendacionesPersonalizadas(@Body Map<String, String> data);

    @POST("/obtener_pregunta")
    Call<PreguntaResponse> obtenerPregunta(@Body ContextoRequest request);
}
