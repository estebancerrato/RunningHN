package com.example.runninghn;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.runninghn.Modelo.Actividad;
import com.example.runninghn.Modelo.RestApiMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ActivityEstadisticas extends AppCompatActivity {

    TextView recorridoMes, caloriasMensuales, recorridoSemanaAnterior, recorridoSemanaActual, recorridoMesAnterior, recorridoMesActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);
        TextView btnAtras = findViewById(R.id.textViewEstadisAtras);
        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recorridoMes = findViewById(R.id.TextViewRecorridoMes);
        caloriasMensuales = findViewById(R.id.TextViewKcalMes);
        recorridoSemanaAnterior = findViewById(R.id.TextViewRecorridoSeAnt);
        recorridoSemanaActual = findViewById(R.id.TextViewRecorridoSeActual);
        recorridoMesAnterior = findViewById(R.id.TextViewRecorridoMesAnterio);
        recorridoMesActual = findViewById(R.id.TextViewRecorridoMesActual);
        mostrarEstadisticos(ActivityTablero.tablero_codigo_usuario);

    }


    private void mostrarEstadisticos(String codigo_usuario) {
        RequestQueue queue = Volley.newRequestQueue(this);
        HashMap<String, String> parametros = new HashMap<>();
        parametros.put("codigo_usuario", codigo_usuario);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, RestApiMethods.Estadistico,
                new JSONObject(parametros), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray usuarioArray = response.getJSONArray("datos");

                    //listaActividades.clear();//limpiar la lista de usuario antes de comenzar a listar
                    for (int i = 0; i < usuarioArray.length(); i++) {
                        JSONObject Row = usuarioArray.getJSONObject(i);
                        recorridoMes.setText(Row.getString("skmmesact"));
                        caloriasMensuales.setText(Row.getString("sumkcal"));
                        recorridoSemanaAnterior.setText(Row.getString("skmsemant"));
                        recorridoSemanaActual .setText(Row.getString("skmsemact"));
                        recorridoMesAnterior.setText(Row.getString("skmmesant"));
                        recorridoMesActual.setText(Row.getString("skmmesact"));
                    }

                }catch (JSONException ex){
                    Toast.makeText(getApplicationContext(), "Error al mostrar estadistico "+ex, Toast.LENGTH_SHORT).show();
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



