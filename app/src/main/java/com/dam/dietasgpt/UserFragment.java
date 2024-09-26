package com.dam.dietasgpt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

// Importaciones necesarias
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserFragment extends Fragment {

    private TextView textViewEdad, textViewAltura, textViewPesoActual, textViewPesoObjetivo, textViewNivelActividad;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String usuarioId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        usuarioId = mAuth.getCurrentUser().getUid();

        textViewEdad = view.findViewById(R.id.textViewEdad);
        textViewAltura = view.findViewById(R.id.textViewAltura);
        textViewPesoActual = view.findViewById(R.id.textViewPesoActual);
        textViewPesoObjetivo = view.findViewById(R.id.textViewPesoObjetivo);
        textViewNivelActividad = view.findViewById(R.id.textViewNivelActividad);
        // Inicializa otros campos si es necesario

        obtenerDatosUsuario();

        return view;
    }

    private void obtenerDatosUsuario() {
        db.collection("usuarios").document(usuarioId)
                .get()
                .addOnSuccessListener(new com.google.android.gms.tasks.OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            int edad = documentSnapshot.getLong("edad").intValue();
                            double altura = documentSnapshot.getDouble("altura");
                            double pesoActual = documentSnapshot.getDouble("pesoActual");
                            double pesoObjetivo = documentSnapshot.getDouble("pesoObjetivo");
                            String nivelActividad = documentSnapshot.getString("nivelActividad");
                            String sexo = documentSnapshot.getString("sexo");
                            // Obtén los demás campos si es necesario

                            textViewEdad.setText("Edad: " + edad + " años");
                            textViewAltura.setText("Altura: " + altura + " cm");
                            textViewPesoActual.setText("Peso Actual: " + pesoActual + " kg");
                            textViewPesoObjetivo.setText("Peso Objetivo: " + pesoObjetivo + " kg");
                            textViewNivelActividad.setText("Nivel de Actividad: " + nivelActividad);
                            // Muestra los demás campos
                        }
                    }
                })
                .addOnFailureListener(new com.google.android.gms.tasks.OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error al obtener datos
                        Toast.makeText(getContext(), "Error al obtener datos del usuario", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
