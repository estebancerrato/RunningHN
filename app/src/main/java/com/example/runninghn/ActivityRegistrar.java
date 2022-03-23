package com.example.runninghn;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.runninghn.Modelo.Pais;

public class ActivityRegistrar extends AppCompatActivity {
    EditText nombres, apellidos, telefono, correo, contrasenia1, contrasenia2, fechaNac, peso, altura;
    Spinner cmbpais;
    Button btnguardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        nombres = (EditText) findViewById(R.id.rtxtnombres);
        apellidos = (EditText) findViewById(R.id.rtxtapellidos);
        telefono = (EditText) findViewById(R.id.rtxtTelefono);
        correo = (EditText) findViewById(R.id.rtxtcorreo);
        contrasenia1 = (EditText) findViewById(R.id.rtxtcontraseña1);
        contrasenia2 = (EditText) findViewById(R.id.rtxtcontraseña2);
        fechaNac = (EditText) findViewById(R.id.rtxtFechaNacimiento);
        peso = (EditText) findViewById(R.id.rtxtPeso);
        altura = (EditText) findViewById(R.id.rtxtAltura);
        cmbpais = (Spinner) findViewById(R.id.rcmbPais);
        btnguardar = (Button) findViewById(R.id.rbtnGuardar);

        btnguardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

}