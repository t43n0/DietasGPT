package com.dam.dietasgpt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

// Importaciones necesarias
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CompleteProfileActivity extends AppCompatActivity {

    private EditText editTextEdad, editTextAltura, editTextPesoActual, editTextPesoObjetivo, editTextNivelActividad;
    private Spinner spinnerSexo;
    private Button buttonGuardar;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        editTextEdad = findViewById(R.id.editTextEdad);
        editTextAltura = findViewById(R.id.editTextAltura);
        editTextPesoActual = findViewById(R.id.editTextPesoActual);
        editTextPesoObjetivo = findViewById(R.id.editTextPesoObjetivo);
        editTextNivelActividad = findViewById(R.id.editTextNivelActividad);
        spinnerSexo = findViewById(R.id.spinnerSexo);
        buttonGuardar = findViewById(R.id.buttonGuardar);

        // Configurar el Spinner de Sexo
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.array_sexo, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSexo.setAdapter(adapter);

        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarPerfil();
            }
        });
    }

    private void guardarPerfil() {
        String edadStr = editTextEdad.getText().toString().trim();
        String alturaStr = editTextAltura.getText().toString().trim();
        String pesoActualStr = editTextPesoActual.getText().toString().trim();
        String pesoObjetivoStr = editTextPesoObjetivo.getText().toString().trim();
        String nivelActividad = editTextNivelActividad.getText().toString().trim();
        String sexo = spinnerSexo.getSelectedItem().toString();
        // Aquí puedes agregar más campos si es necesario

        if (edadStr.isEmpty() || alturaStr.isEmpty() || pesoActualStr.isEmpty() || pesoObjetivoStr.isEmpty() || nivelActividad.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        String usuarioId = mAuth.getCurrentUser().getUid();

        Map<String, Object> perfil = new HashMap<>();
        perfil.put("edad", Integer.parseInt(edadStr));
        perfil.put("altura", Double.parseDouble(alturaStr));
        perfil.put("pesoActual", Double.parseDouble(pesoActualStr));
        perfil.put("pesoObjetivo", Double.parseDouble(pesoObjetivoStr));
        perfil.put("nivelActividad", nivelActividad);
        perfil.put("sexo", sexo);
        // Agrega los demás campos aquí

        db.collection("usuarios").document(usuarioId)
                .set(perfil)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Perfil guardado exitosamente
                        Intent intent = new Intent(CompleteProfileActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception error) {
                        // Error al guardar el perfil
                        Toast.makeText(CompleteProfileActivity.this, "Error al guardar perfil", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
