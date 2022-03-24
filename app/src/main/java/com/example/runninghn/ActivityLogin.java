package com.example.runninghn;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.runninghn.Modelo.RestApiMethods;
import com.example.runninghn.Modelo.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ActivityLogin extends AppCompatActivity {
    Button btnIngresar, btnRegistrarse;
    EditText txtcorreo, txtcontrasenia;
    CheckBox Recordar;
    final Context context = this;

    private SharedPreferences mSharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnIngresar = (Button) findViewById(R.id.albtnIngresar);
        btnRegistrarse = (Button)findViewById(R.id.albtnRegistrar);
        txtcorreo = (EditText) findViewById(R.id.altxtUser);
        txtcontrasenia = (EditText) findViewById(R.id.altxtPass);
        Recordar = (CheckBox) findViewById(R.id.alckRecordar);

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuario = txtcorreo.getText().toString();
                String contrasenia = txtcontrasenia.getText().toString();
                loginUsuario(usuario,contrasenia);

            }
        });

        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ActivityRegistrar.class);
                startActivity(intent);
            }
        });
//======================PERSISTENCIA DE DATOS=========================================//

        cargarDatosGuardados();

        Recordar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loguearCheckbox(view);
            }
        });



    }

    private void cargarDatosGuardados() {
        mSharedPrefs = getSharedPreferences("credencales",Context.MODE_PRIVATE);//Abre el archivo credenciales sin necedidad de volver a crearlo.

        //se crea variable con la preferencias y le asignamos el identificador
        String user = mSharedPrefs.getString("usuario","No existe informacion");
        String pass = mSharedPrefs.getString("password","No existe informacion");

        txtcorreo.setText(user);
        txtcontrasenia.setText(pass);
    }

    //======================PERSISTENCIA DE DATOS=========================================//
    public void loguearCheckbox(View v) {
        String s = "Estado: " + (Recordar.isChecked() ? "Datos Guardados" : "");
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();

        mSharedPrefs = getSharedPreferences("credencales",Context.MODE_PRIVATE);

        String user = txtcorreo.getText().toString();
        String pass = txtcontrasenia.getText().toString();

        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.putString("usuario",user);
        editor.putString("password",pass);

        txtcorreo.setText(user);
        txtcontrasenia.setText(pass);

        editor.commit();
    }


    private void loginUsuario(String correo, String clave) {
        RequestQueue queue = Volley.newRequestQueue(this);
        HashMap<String, String> parametros = new HashMap<>();
        parametros.put("email", correo);
        parametros.put("clave", clave);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, RestApiMethods.EndPointValidarLogin,
                    new JSONObject(parametros), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        if (response.getString("mensaje").toString().equals("login exitoso")){
                            Toast.makeText(getApplicationContext(), "Response " + response.getString("mensaje").toString(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),ActivityRegistrar.class);
                            startActivity(intent);
                            finish();
                        }else{
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                            alertDialogBuilder.setTitle("Datos invalidos");
                            alertDialogBuilder
                                    .setMessage("Verifique su correo o contraseña")
                                    .setCancelable(false)
                                    .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                            // create alert dialog
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error "+error.toString(), Toast.LENGTH_SHORT).show();
                }
            });

        queue.add(jsonObjectRequest);
    }

}