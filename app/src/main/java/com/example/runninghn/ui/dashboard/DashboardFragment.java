package com.example.runninghn.ui.dashboard;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentController;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.runninghn.ActivityNuevaCarrera;
import com.example.runninghn.MapsActivity;
import com.example.runninghn.R;
import com.example.runninghn.databinding.ActivityMapsBinding;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.DecimalFormat;

public class DashboardFragment extends Fragment implements OnMapReadyCallback {



    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private LatLng sydney = new LatLng(-33.81, 151.211);

    Double dLatitud = 14.0676649;
    Double dLongitud = -87.2112;







    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Retrieve the content view that renders the map.

        binding = ActivityMapsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

        LatLng prueba = new LatLng(-33.81, 151.215);
        DecimalFormat df = new DecimalFormat("#.00");
        Double km = Double.valueOf(df.format(CalcularDistanciaenKM(sydney,prueba)));

        mMap.addMarker(new MarkerOptions().position(new LatLng(dLatitud,dLongitud)).title("Punto Inicial").icon(BitmapDescriptorFactory.fromResource(R.drawable.corredor)));

        ejecutar();

//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney").icon(BitmapDescriptorFactory.fromResource(R.drawable.corredor)));
//        mMap.addMarker(new MarkerOptions().position(prueba).title("Prueba").icon(BitmapDescriptorFactory.fromResource(R.drawable.descansando)));
////        Polyline polyline = mMap.addPolyline(new PolylineOptions().add(
//
//                new LatLng(-33.81, 151.211),
//                new LatLng(-33.81, 151.215),
//                new LatLng(-33.82, 151.217)
//        ).width(7).color(Color.RED).geodesic(true));





        mMap.getUiSettings().setZoomControlsEnabled(true);



    }

    public double CalcularDistanciaenKM(LatLng StartP, LatLng EndP) {
        int Radius=6371;//radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lon2-lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult= Radius*c;
        double km=valueResult/1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec =  Integer.valueOf(newFormat.format(km));
        double meter=valueResult%1000;
        int  meterInDec= Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value",""+valueResult+"   KM  "+kmInDec+" Meter   "+meterInDec);
        return Radius * c;
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

        Double latitud = 0.0 ;
        latitud= ActivityNuevaCarrera.getlatitud();
        Toast.makeText(getContext(),"latitud "+latitud,Toast.LENGTH_SHORT).show();

        Double longitud = 0.0;
        longitud= ActivityNuevaCarrera.getLongitud();
        Toast.makeText(getContext(),"longitud "+longitud,Toast.LENGTH_SHORT).show();
         LatLng pfinal = new LatLng(latitud, longitud);

        mMap.addMarker(new MarkerOptions().position(new LatLng(latitud, longitud)).title("Punto Final").icon(BitmapDescriptorFactory.fromResource(R.drawable.descansando)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitud,longitud), 15));
        //mMap.addPolyline(new PolylineOptions().add(new LatLng(dLatitud,dLongitud),new LatLng(latitud, longitud)).width(7).color(Color.RED).geodesic(true));



    }


}