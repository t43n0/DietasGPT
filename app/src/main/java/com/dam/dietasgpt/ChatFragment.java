package com.dam.dietasgpt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

// Importaciones necesarias
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Importaciones para Retrofit
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import retrofit2.converter.gson.GsonConverterFactory;

public class ChatFragment extends Fragment {

    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private List<Message> mensajes;
    private EditText editTextMensaje;
    private Button btnEnviar;
    private FirebaseAuth mAuth;
    private String usuarioId;

    private ApiService apiService;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        mAuth = FirebaseAuth.getInstance();
        usuarioId = mAuth.getCurrentUser().getUid();

        // Inicializar Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://<TU_DIRECCION_IP>:5000") // Reemplaza con tu URL base
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        // Inicializar vistas
        recyclerView = view.findViewById(R.id.recyclerViewMensajes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mensajes = new ArrayList<>();
        adapter = new MessageAdapter(mensajes);
        recyclerView.setAdapter(adapter);

        editTextMensaje = view.findViewById(R.id.editTextMensaje);
        btnEnviar = view.findViewById(R.id.btnEnviar);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mensajeTexto = editTextMensaje.getText().toString().trim();
                if (!mensajeTexto.isEmpty()) {
                    enviarMensaje(mensajeTexto);
                    editTextMensaje.setText("");
                }
            }
        });

        // Iniciar conversación con el chatbot
        iniciarConversacion();

        return view;
    }

    private void iniciarConversacion() {
        mensajes.add(new Message("assistant", "¡Hola! ¿En qué puedo ayudarte hoy?"));
        adapter.notifyDataSetChanged();
    }

    private void enviarMensaje(String contenidoUsuario) {
        mensajes.add(new Message("user", contenidoUsuario));
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(mensajes.size() - 1);

        // Enviar mensaje al backend para obtener respuesta del chatbot
        obtenerRespuestaDelChatbot();
    }

    private void obtenerRespuestaDelChatbot() {
        // Preparar el contexto de la conversación
        List<Map<String, String>> contexto = new ArrayList<>();
        for (Message mensaje : mensajes) {
            Map<String, String> msg = new HashMap<>();
            msg.put("role", mensaje.getRole());
            msg.put("content", mensaje.getContent());
            contexto.add(msg);
        }

        Map<String, Object> request = new HashMap<>();
        request.put("contexto", contexto);
        request.put("usuarioId", usuarioId);

        Call<PreguntaResponse> call = apiService.obtenerPregunta((ContextoRequest) request);
        call.enqueue(new Callback<PreguntaResponse>() {
            @Override
            public void onResponse(Call<PreguntaResponse> call, Response<PreguntaResponse> response) {
                if (response.isSuccessful()) {
                    String respuestaChatbot = response.body().getPregunta();
                    mensajes.add(new Message("assistant", respuestaChatbot));
                    adapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(mensajes.size() - 1);
                } else {
                    // Manejar errores en la respuesta
                    Toast.makeText(getContext(), "Error al obtener respuesta del chatbot", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PreguntaResponse> call, Throwable t) {
                // Manejar errores de comunicación
                mensajes.add(new Message("assistant", "Lo siento, ha ocurrido un error."));
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(mensajes.size() - 1);
            }
        });
    }
}
