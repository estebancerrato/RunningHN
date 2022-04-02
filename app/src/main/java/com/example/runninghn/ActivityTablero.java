package com.example.runninghn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.runninghn.Modelo.RestApiMethods;

public class ActivityTablero extends AppCompatActivity {

    TextView btnNuevaCarrera;
    public static final String tablero_correo = RestApiMethods.correo;
    public static final String tablero_codigo_usuario = RestApiMethods.codigo_usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablero);

        ImageView perfil = findViewById(R.id.perfil);
        btnNuevaCarrera = findViewById(R.id.btnNuevaCarrera);
        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Activity_Perfil.class);
                startActivity(intent);
            }
        });
        btnNuevaCarrera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityNuevaCarrera.class);
                startActivity(intent);
            }
        });
    }
}