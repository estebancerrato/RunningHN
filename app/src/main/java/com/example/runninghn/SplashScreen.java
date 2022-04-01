package com.example.runninghn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.runninghn.Modelo.RestApiMethods;

public class SplashScreen extends AppCompatActivity {
    SharedPreferences mSharedPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        mSharedPrefs = getSharedPreferences("anuncios", Context.MODE_PRIVATE);
        String olvidar = mSharedPrefs.getString("olvidar","");


        new Handler().postDelayed(new Runnable () {
            @Override
            public void run() {
                if(olvidar.equals( "1")){

                    startActivity(new Intent(SplashScreen.this, ActivityLogin.class));
                    finish();
                }else {
                    startActivity(new Intent(SplashScreen.this, ActivityAnuncio1.class));
                    finish();
                }
            }
        },2000);
    }

}