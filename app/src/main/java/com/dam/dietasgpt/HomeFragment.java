package com.dam.dietasgpt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private SwipeFlingAdapterView swipeView;
    private PlatoAdapter adapter;
    private List<Plato> listaPlatos;
    private FirebaseFirestore db;
    private String usuarioId = "usuario_demo"; // Deberías obtener el ID real del usuario

    private ApiService apiService;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        db = FirebaseFirestore.getInstance();
        listaPlatos = new ArrayList<>();
        adapter = new PlatoAdapter(getContext(), R.layout.item_plato, listaPlatos);

        swipeView = view.findViewById(R.id.frame);
        swipeView.setAdapter(adapter);

        obtenerRecomendacionesPersonalizadas();

        swipeView.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {

            @Override
            public void removeFirstObjectInAdapter() {
                // Eliminar el primer elemento de la lista
                if (!listaPlatos.isEmpty()) {
                    listaPlatos.remove(0);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                // Deslizado a la izquierda (No Me Gusta)
                Plato plato = (Plato) dataObject;
                guardarPreferencia(plato, "no_me_gusta");
                Toast.makeText(getContext(), "No te gustó: " + plato.getNombre(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                // Deslizado a la derecha (Me Gusta)
                Plato plato = (Plato) dataObject;
                guardarPreferencia(plato, "me_gusta");
                Toast.makeText(getContext(), "Te gustó: " + plato.getNombre(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Cargar más platos si es necesario
                if (itemsInAdapter == 0) {
                    obtenerRecomendacionesPersonalizadas();
                }
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                // Opcional: manejar efectos visuales durante el deslizamiento
            }
        });

        return view;
    }

    private void obtenerRecomendacionesPersonalizadas() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://100.65.217.49:5000") // Reemplaza con la URL de tu backend
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        Map<String, String> data = new HashMap<>();
        data.put("usuarioId", usuarioId);

        Call<List<Plato>> call = apiService.obtenerRecomendacionesPersonalizadas(data);
        call.enqueue(new Callback<List<Plato>>() {
            @Override
            public void onResponse(Call<List<Plato>> call, Response<List<Plato>> response) {
                if (response.isSuccessful()) {
                    List<Plato> recomendaciones = response.body();
                    listaPlatos.clear();
                    listaPlatos.addAll(recomendaciones);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Error al obtener recomendaciones", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Plato>> call, Throwable t) {
                Toast.makeText(getContext(), "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void guardarPreferencia(Plato plato, String preferencia) {
        Map<String, Object> data = new HashMap<>();
        data.put("platoId", plato.getId());
        data.put("preferencia", preferencia);

        db.collection("usuarios").document(usuarioId)
                .collection("preferencias").document(plato.getId())
                .set(data)
                .addOnSuccessListener(new com.google.android.gms.tasks.OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Preferencia guardada exitosamente
                    }
                })
                .addOnFailureListener(new com.google.android.gms.tasks.OnFailureListener() {
                    @Override
                    public void onFailure(@androidx.annotation.NonNull Exception e) {
                        Toast.makeText(getContext(), "Error al guardar preferencia", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
