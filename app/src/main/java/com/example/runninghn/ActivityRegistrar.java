package com.example.runninghn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.runninghn.Modelo.Pais;
import com.example.runninghn.Modelo.RestApiMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityRegistrar extends AppCompatActivity {
    EditText nombres, apellidos, telefono, correo, contrasenia1, contrasenia2, fechaNac, peso, altura;
    Spinner cmbpais;
    Button btnguardar;
    String contrasenia;

    Pais pais;
    List<Pais> paisList;
    ArrayList<String> arrayPaises;
    ArrayAdapter adp;
    int codigoPaisSeleccionado;


    Intent intent;

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

        intent = new Intent(getApplicationContext(),ActivityRegistrar.class);//para obtener el contacto seleccionado mas adelante

        btnguardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarContrasenia();
                RegistrarUsuario();
            }
        });

        cmbpais.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //setComboboxSeleccionado();//obtengo el usuario seleccionado de la lista
                String cadena = adapterView.getSelectedItem().toString();

                //Quitar los caracteres del combobox para obtener solo el codigo del pais
                codigoPaisSeleccionado = Integer.valueOf(extraerNumeros(cadena).toString().replace("]","").replace("[",""));

                //Toast.makeText(getApplicationContext(),"usuario id: "+codigoPaisSeleccionado, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        comboboxPaises();

    }

    List<Integer> extraerNumeros(String cadena) {
        List<Integer> todosLosNumeros = new ArrayList<Integer>();
        Matcher encuentrador = Pattern.compile("\\d+").matcher(cadena);
        while (encuentrador.find()) {
            todosLosNumeros.add(Integer.parseInt(encuentrador.group()));
        }
        return todosLosNumeros;
    }



    private String validarContrasenia() {
        if (contrasenia1.getText().toString().equals(contrasenia2.getText().toString())){
            contrasenia = contrasenia1.getText().toString();
        }
        return contrasenia;
    }

    private void comboboxPaises(){
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, RestApiMethods.EndPointListarPaises,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray contactoArray = jsonObject.getJSONArray( "pais");

                            arrayPaises = new ArrayList<>();
//                            arrayPaises.clear();//limpiar la lista de usuario antes de comenzar a listar
                            for (int i=0; i<contactoArray.length(); i++)
                            {
                                JSONObject RowPais = contactoArray.getJSONObject(i);
                                pais = new Pais(  RowPais.getInt("codigo_pais"),
                                        RowPais.getString("nombre")
                                );

                                arrayPaises.add(pais.getNombre() + " ["+pais.getId()+"]");
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ActivityRegistrar.this, android.R.layout.simple_spinner_dropdown_item, arrayPaises);
                            cmbpais.setAdapter(adapter);

                        }catch (JSONException ex){
                            Toast.makeText(getApplicationContext(), "mensaje "+ex, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(getApplicationContext(), "mensaje "+error, Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    private void RegistrarUsuario() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        HashMap<String, String> parametros = new HashMap<>();

        parametros.put("nombres", nombres.getText().toString());
        parametros.put("apellidos", apellidos.getText().toString());
        parametros.put("telefono", telefono.getText().toString());
        parametros.put("email", correo.getText().toString());
        parametros.put("clave", contrasenia);
        parametros.put("fecha_nac", fechaNac.getText().toString());
        parametros.put("peso", peso.getText().toString());
        parametros.put("altura", altura.getText().toString());
        parametros.put("codigo_pais", codigoPaisSeleccionado+"");
        parametros.put("foto", "010101");
        parametros.put("estado","1");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, RestApiMethods.EndPointCreateUsuario,
                new JSONObject(parametros), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Toast.makeText(getApplicationContext(), "String Response " + response.getString("mensaje").toString(), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

//    private void setComboboxSeleccionado() {
//
//        intent.putExtra("id", pais.getId()+"");
//        intent.putExtra("nombre", pais.getNombre());
//        Toast.makeText(getApplicationContext(), "isuario:"+pais.getNombre().toString(), Toast.LENGTH_SHORT).show();
//
//    }

}