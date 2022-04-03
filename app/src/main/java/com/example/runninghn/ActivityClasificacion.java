package com.example.runninghn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.runninghn.Modelo.Actividad;
import com.example.runninghn.Modelo.Clasificacion;
import com.example.runninghn.Modelo.RestApiMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivityClasificacion extends AppCompatActivity {

    private final ArrayList<Clasificacion> listaClasificacion = new ArrayList<>();
    ListView listViewCustomAdapter;
    AdaptadorClasificacion adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clasificacion);

        listViewCustomAdapter = findViewById(R.id.listadoClasificacion);
        adaptador = new AdaptadorClasificacion(this);
        //mando a llamar el metodo que me traera el listado de clasificacion
        obtenerlistadoClasificacion(ActivityTablero.tablero_codigo_usuario);
    }

    class AdaptadorClasificacion extends ArrayAdapter<Clasificacion> {

        AppCompatActivity appCompatActivity;

        AdaptadorClasificacion(AppCompatActivity context) {
            super(context, R.layout.adapterclasificacion, listaClasificacion);
            appCompatActivity = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = appCompatActivity.getLayoutInflater();
            View item = inflater.inflate(R.layout.adapterclasificacion, null);
            TextView top, nombrecompleto, kilometro, kcal;
            ImageView foto;

            top = item.findViewById(R.id.clasiNumtop);
            foto = item.findViewById(R.id.cimgfoto);
            nombrecompleto = item.findViewById(R.id.cNombreCompleto);
            kilometro = item.findViewById(R.id.ckilometro);
            kcal = item.findViewById(R.id.cKcal);

            top.setText(listaClasificacion.get(position).getTop());
            nombrecompleto.setText(listaClasificacion.get(position).getNombrecompleto());
            kilometro.setText(listaClasificacion.get(position).getKm());
            kcal.setText(listaClasificacion.get(position).getKcal());
            mostrarFoto(listaClasificacion.get(position).getFoto(),foto);

            return (item);
        }
    }

    private void obtenerlistadoClasificacion(String codigo_usuario) {
        RequestQueue queue = Volley.newRequestQueue(this);
        HashMap<String, String> parametros = new HashMap<>();
        parametros.put("codigo_usuario", codigo_usuario);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, RestApiMethods.listaClasifiacion,
                new JSONObject(parametros), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray usuarioArray = response.getJSONArray("clasificacion");
                    //listaClasificacion.clear();//limpiar la lista de usuario antes de comenzar a listar
                    for (int i = 0; i < usuarioArray.length(); i++) {
                        JSONObject Row = usuarioArray.getJSONObject(i);
                        Clasificacion clasificacion = new Clasificacion(Row.getString("top"),
                                Row.getString("foto"),
                                Row.getString("nombrecompleto"),
                                Row.getString("KmRecorrido"),
                                Row.getString("sumkcal")
                                );
                        listaClasificacion.add(clasificacion);

                    }
                    listViewCustomAdapter.setAdapter(adaptador);

                }catch (JSONException ex){
                    Toast.makeText(getApplicationContext(), "Error al mostrar listado de clasificación "+ex, Toast.LENGTH_SHORT).show();
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

    public void mostrarFoto(String foto, ImageView Foto) {
        try {
            String base64String = "data:image/png;base64,"+foto;
            String base64Image = base64String.split(",")[1];
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            Foto.setImageBitmap(decodedByte);//setea la imagen al imageView
        }catch (Exception ex){
            ex.toString();
        }
    }
}