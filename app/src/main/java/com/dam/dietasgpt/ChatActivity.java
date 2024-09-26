package com.dam.dietasgpt;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private List<Message> mensajes;
    private EditText editTextMensaje;
    private Button btnEnviar;
    private String platoId;
    private String usuarioId = "usuario_demo"; // Deberías obtener el ID real del usuario

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        platoId = getIntent().getStringExtra("platoId");

        // Inicializar Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://tu-backend.com") // Reemplaza con la URL de tu backend
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        recyclerView = findViewById(R.id.recyclerViewMensajes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mensajes = new ArrayList<>();
        adapter = new MessageAdapter(mensajes);
        recyclerView.setAdapter(adapter);

        editTextMensaje = findViewById(R.id.editTextMensaje);
        btnEnviar = findViewById(R.id.btnEnviar);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mensajeTexto = editTextMensaje.getText().toString();
                if (!mensajeTexto.isEmpty()) {
                    enviarMensaje(mensajeTexto);
                    editTextMensaje.setText("");
                }
            }
        });

        // Iniciar conversación con el chatbot
        iniciarConversacion();
    }

    private void iniciarConversacion() {
        mensajes.add(new Message("assistant", "¡Hola! ¿Qué calificación le darías al plato que acabas de seleccionar?"));
        adapter.notifyDataSetChanged();
    }

    private void enviarMensaje(String contenidoUsuario) {
        mensajes.add(new Message("user", contenidoUsuario));
        adapter.notifyDataSetChanged();

        // Enviar mensaje al backend para obtener respuesta del chatbot
        obtenerRespuestaDelChatbot();
    }

    private void obtenerRespuestaDelChatbot() {
        // Preparar el contexto de la conversación
        List<Message> contexto = new ArrayList<>();
        for (Message mensaje : mensajes) {
            contexto.add(mensaje);
        }

        ContextoRequest request = new ContextoRequest(contexto);

        Call<PreguntaResponse> call = apiService.obtenerPregunta(request);
        call.enqueue(new Callback<PreguntaResponse>() {
            @Override
            public void onResponse(Call<PreguntaResponse> call, Response<PreguntaResponse> response) {
                if (response.isSuccessful()) {
                    String respuestaChatbot = response.body().getPregunta();
                    mensajes.add(new Message("assistant", respuestaChatbot));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<PreguntaResponse> call, Throwable t) {
                // Manejar error
                mensajes.add(new Message("assistant", "Lo siento, ha ocurrido un error."));
                adapter.notifyDataSetChanged();
            }
        });
    }
}
