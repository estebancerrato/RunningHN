package com.example.runninghn;

import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.runninghn.Modelo.RestApiMethods;
import com.example.runninghn.Modelo.Usuario;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.runninghn.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback  {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    List<Double> pilatitud = new ArrayList<>();
    List<Double> pilongitud = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        obtenerRecorrido("1111140");
        ejecutar();
        mMap.getUiSettings().setZoomControlsEnabled(true);


    }



    private void obtenerRecorrido(String codigo_actividad) {

        RequestQueue queue = Volley.newRequestQueue(this);
        HashMap<String, String> parametros = new HashMap<>();
        parametros.put("codigo_actividad", codigo_actividad);
        String url = "http://transportweb2.online/API/listadetalleactividad.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                new JSONObject(parametros), new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray usuarioArray = response.getJSONArray("detalle");
                    for (int i = 0; i < usuarioArray.length(); i++) {
                        JSONObject RowDetalle = usuarioArray.getJSONObject(i);
                        pilatitud.add(RowDetalle.getDouble("Latitud"));
                        pilongitud.add(RowDetalle.getDouble("Longitud"));
                    }


                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Error "+e, Toast.LENGTH_SHORT).show();
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


    private void ejecutar(){
        final Handler handler= new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                metodoEjecutar();//llamamos nuestro metodo
                //handler.postDelayed(this,0);//se ejecutara cada 10 segundos
            }
        },2000);//empezara a ejecutarse después de 5 milisegundos
    }
    private void metodoEjecutar() {
        //valida si tiene los permisos de ser asi manda a llamar el metodo locationStart()
        LatLng pfinal = new LatLng(pilatitud.get(1),pilongitud.get(1));
        LatLng pinicial =new LatLng(pilatitud.get(0), pilongitud.get(0));
        mMap.addMarker(new MarkerOptions().position(pinicial).title("Posicion inicial").icon(BitmapDescriptorFactory.defaultMarker()));
        mMap.addPolyline(new PolylineOptions().add(pfinal, pinicial).width(7).color(Color.RED).geodesic(true));
        for(int i = 0;i<pilatitud.size() ;i++)
        {
            pinicial = new LatLng(pilatitud.get(i),pilongitud.get(i));
            mMap.addMarker(new MarkerOptions().position(pinicial).icon(BitmapDescriptorFactory.fromResource(R.drawable.corredor)));
            mMap.addPolyline(new PolylineOptions().add(pfinal, pinicial).width(7).color(Color.RED).geodesic(true));
            pfinal = pinicial;//actualizamos el punto inicial con el final
            if (i == pilatitud.size()){
                mMap.addMarker(new MarkerOptions().position(pfinal).title("Posicion Final").icon(BitmapDescriptorFactory.fromResource(R.drawable.descansando)));
            }
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pfinal, 15));


    }






}