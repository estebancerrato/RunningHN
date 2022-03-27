package com.example.runninghn.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.runninghn.MainActivity;
import com.example.runninghn.Modelo.RestApiMethods;
import com.example.runninghn.Modelo.Usuario;
import com.example.runninghn.R;
import com.example.runninghn.databinding.ActivityMain2Binding;
import com.example.runninghn.databinding.FragmentHomeBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class HomeFragment extends Fragment {
    ImageView foto;
    EditText nombre,peso,altura,pais,edad,fechaNac,correo;

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        nombre = (EditText) binding.fhtxtnombre;
        correo = (EditText) binding.fhcorreo;
        pais = (EditText) binding.fhtxtPais;
        //edad = (EditText) binding.fh
        peso = (EditText) binding.fhpeso;
        altura = (EditText) binding.fhaltura;
        fechaNac = (EditText) binding.fhfecha;
        foto = (ImageView) binding.fhImage;
        nombre.setText("prueba");

        //final TextView textView = binding.textHome;
        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



}