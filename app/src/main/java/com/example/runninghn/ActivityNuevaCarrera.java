package com.example.runninghn;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.runninghn.Modelo.RestApiMethods;
import com.example.runninghn.databinding.ActivityMapsBinding;
import com.example.runninghn.ui.dashboard.DashboardFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;

public class ActivityNuevaCarrera extends AppCompatActivity{


    public static Button btnComenzar;
    EditText txtLat,txtLon;

    public static String latitud = "";
    public static String longitud = "";
    GoogleMap mMap;
    private ActivityMapsBinding binding;
    final String[] codigo_actividad = new String[1];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);




        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {

        }




        btnComenzar = (Button) findViewById(R.id.btnComenzar);

        txtLat = (EditText) findViewById(R.id.txtLat);
        txtLon = (EditText) findViewById(R.id.txtLon);



        btnComenzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnComenzar.getText().equals("Comenzar")){
                    latitud = txtLat.getText().toString();
                    longitud = txtLon.getText().toString();
                    btnComenzar.setText("DETENER");

                }else if (btnComenzar.getText().equals("DETENER")){
                    try {
                        guardarRecorrido(RestApiMethods.codigo_usuario,DashboardFragment.km);


                        new CountDownTimer(5000, 1000) {
                            public void onFinish() {
                                // When timer is finished
                                // Execute your code here
                                System.out.println("codigo actividad: "+codigo_actividad[0]);
                                System.out.println("Latitud: "+DashboardFragment.recorridoMapLatitud.get(0));
                                System.out.println("Longitud:"+DashboardFragment.recorridoMapLongitud.get(0));
                                if (DashboardFragment.recorridoMapLongitud !=null) {
                                    Toast.makeText(getApplicationContext(),"recorrido"+DashboardFragment.recorridoMapLatitud.get(0),Toast.LENGTH_SHORT).show();
                                    for (int indice = 0; indice < DashboardFragment.recorridoMapLongitud.size(); indice++) {
                                        guardarDetallesRecorrido(codigo_actividad[0], DashboardFragment.recorridoMapLatitud.get(indice), DashboardFragment.recorridoMapLongitud.get(indice));
                                    }
                                }

                            }

                            public void onTick(long millisUntilFinished) {
                                // millisUntilFinished    The amount of time until finished.
                            }
                        }.start();




                        cerrarActividad();

                        Toast.makeText(getApplicationContext(),"recorrido "+ DashboardFragment.recorridoMapLatitud+", "+DashboardFragment.recorridoMapLongitud,Toast.LENGTH_LONG).show();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }


            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationStart();
        }
        //mando a llamar el metodo ejecutar y cada 10 segundos se encargara de setear la nueva ubicacion


    }









    //-----------------------------LATITUD Y LONGITUD----------------------------------------
    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setMainActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            //SE VA A LA CONFIGURACION DEL SISTEMA PARA QUE ACTIVE EL GPS UNA VEZ QUE INICIA LA APLICACION
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);


    }

    //-------------------------GUARDAR RECORRIDO--------------------------

    private void guardarRecorrido(String codigoUsuario, Double distancia) {

        RequestQueue queue = Volley.newRequestQueue(this);
        HashMap<String, String> parametros = new HashMap<>();
        parametros.put("codigo_usuario", codigoUsuario);
        parametros.put("distancia", distancia+"");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, RestApiMethods.GuardarActidad,
                new JSONObject(parametros), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    codigo_actividad[0] = String.valueOf(response.getString("mensaje"));

                    Toast.makeText(getApplicationContext(), "Actividad guardada exitosamente"+ codigo_actividad[0], Toast.LENGTH_SHORT).show();

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

    private void guardarDetallesRecorrido(String codigoactividad, Double latitud, Double longitud) {
        RequestQueue queue = Volley.newRequestQueue(this);
        HashMap<String, String> parametros = new HashMap<>();
        parametros.put("codigo_actividad", codigoactividad);
        parametros.put("Latitud", latitud+"");
        parametros.put("Longitud", longitud+"");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, RestApiMethods.DetallesGuardarActidad,
                new JSONObject(parametros), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Toast.makeText(getApplicationContext(), response.getString("mensaje"), Toast.LENGTH_SHORT).show();
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

    private void cerrarActividad(){
        getSupportFragmentManager().beginTransaction().
                remove(getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView)).commit();
        getSupportFragmentManager().beginTransaction().
                remove(getSupportFragmentManager().findFragmentById(R.id.navigation_dashboard));
        finish();
    }



    public class Localizacion implements LocationListener {
        ActivityNuevaCarrera mainActivity;

        public void setMainActivity(ActivityNuevaCarrera mainActivity) {
            this.mainActivity = mainActivity;
        }

        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion

            loc.getLatitude();
            loc.getLongitude();
            ActivityNuevaCarrera.setLatitud(loc.getLatitude()+"");
            ActivityNuevaCarrera.setLongitud(loc.getLongitude()+"");
            txtLat.setText(loc.getLatitude()+"");
            txtLon.setText(loc.getLongitude()+"");
            this.mainActivity.setLocation(loc);
        }


        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }

    public void setLocation(Location loc) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void setLatitud(String latitud) {
        ActivityNuevaCarrera.latitud = latitud;
    }
    public static void setLongitud(String longitud) {
        ActivityNuevaCarrera.longitud = longitud;
    }

    public static Double getlatitud(){
        return Double.valueOf(latitud);
    }
    public static Double getLongitud(){
        return Double.valueOf(longitud);
    }



}