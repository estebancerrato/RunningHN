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
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.ActivityTransition;
import com.google.android.gms.location.DetectedActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
public class ActivityUsuario extends AppCompatActivity {


    Button btnComenzar;
    EditText txtLat,txtLon;

    public static String latitud = "";
    public static String longitud = "";


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
                    displayMapDraving();

                try {

                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Debe de tomarse una foto ",Toast.LENGTH_LONG).show();
                }
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationStart();
        }
        //mando a llamar el metodo ejecutar y cada 10 segundos se encargara de setear la nueva ubicacion
        ejecutar();

    }

    private void ejecutar(){
        final Handler handler= new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                metodoEjecutar();//llamamos nuestro metodo
                handler.postDelayed(this,10000);//se ejecutara cada 10 segundos
            }
        },5000);//empezara a ejecutarse después de 5 milisegundos
    }
    private void metodoEjecutar() {
        //valida si tiene los permisos de ser asi manda a llamar el metodo locationStart()

        Toast.makeText(getApplicationContext(), "Prueba ", Toast.LENGTH_SHORT).show();
    }

    private void displayMapDraving() {
        try {
            double latitude = Double.parseDouble(latitud);
            double longitude = Double.parseDouble(longitud);
            /*
                mode = 1  AUTOMOVIL
                mode = d TREN
                mode = b BICICLETA
                mode = W CAMINANDO
                mode = l MOTOCICLETA
            */
            Uri uri = Uri.parse("google.navigation:q="+latitude+","+longitude+"&mode=w");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage("com.google.android.apps.maps");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        } catch (Exception e) {
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
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


    public class Localizacion implements LocationListener {
        ActivityUsuario mainActivity;

        public void setMainActivity(ActivityUsuario mainActivity) {
            this.mainActivity = mainActivity;
        }

        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion

            loc.getLatitude();
            loc.getLongitude();
            ActivityUsuario.setLatitud(loc.getLatitude()+"");
            ActivityUsuario.setLongitud(loc.getLongitude()+"");
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
        ActivityUsuario.latitud = latitud;
    }
    public static void setLongitud(String longitud) {
        ActivityUsuario.longitud = longitud;
    }



}