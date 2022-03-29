package com.example.runninghn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
//import com.example.runninghn.ListaAmigo.CustomListAdapter;
//import com.example.runninghn.ListaAmigo.Movie;
import com.example.runninghn.Modelo.RestApiMethods;
import com.example.runninghn.Modelo.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActivityAgregarAmigos extends AppCompatActivity {


    EditText txtcorreo;
    String user;
    ListView listAmigos;
    List<Usuario> usuarioList;
   // private CustomListAdapter adapter;
    ArrayList<String> arrayUsuario;
    ImageView imgAmigo;
    Usuario usuario;
    private List movieItems;
    //ArrayAdapter   adapter;
    private final ArrayList<Usuario> listaUsuarios = new ArrayList<Usuario>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_amigos);

        listAmigos = (ListView) findViewById(R.id.listaAmigos);
        usuarioList= new ArrayList<>();
       // arrayUsuario = new ArrayList<String >();
      AdaptadorUsuario  adapter = new AdaptadorUsuario(this,usuarioList);
        listAmigos.setAdapter(adapter);


        //listarUsuarios();
        RequestQueue queue = Volley.newRequestQueue(this);
        HashMap<String, String> parametros = new HashMap<>();
        parametros.put("email", "sergioagustincastillo@gmail.com");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, RestApiMethods.EndPointListarUsuarioPaise,
                new JSONObject(parametros), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //Toast.makeText(getApplicationContext(),"Toy Arta" + response.toString(), Toast.LENGTH_SHORT).show();
                    //JSONObject jsonObject = new JSONObject(response.getString("mensaje"));
                    JSONArray usuarioArray = response.getJSONArray( "usuario");

                    // arrayUsuario.clear();//limpiar la lista de usuario antes de comenzar a listar
                    arrayUsuario = new ArrayList<>();
                    for (int i=0; i<usuarioArray.length(); i++)
                    {
                        JSONObject RowUsuario = usuarioArray.getJSONObject(i);
                        usuario = new Usuario(  RowUsuario.getInt("codigo_usuario"),
                                RowUsuario.getString("nombres"),
                                RowUsuario.getString("apellidos"),
                                RowUsuario.getString("foto"));

                        usuarioList.add(usuario);

                        //arrayUsuario.add(usuario.getNombres()+' '+usuario.getApellidos());
                    }

                    // adp = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_checked, arrayUsuario);
                    /*ArrayAdapter<String> adp = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_checked, arrayUsuario);
                    listAmigos.setAdapter(adp);*/

                }catch (JSONException ex){
                    Toast.makeText(getApplicationContext(), "mensaje"+ex, Toast.LENGTH_SHORT).show();
                }
                
                adapter.notifyDataSetChanged();
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonObjectRequest);
    }

    /*private void listarUsuarios() {
        RequestQueue queue = Volley.newRequestQueue(this);
        HashMap<String, String> parametros = new HashMap<>();
        parametros.put("email", "sergioagustincastillo@gmail.com");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, RestApiMethods.EndPointListarUsuarioPaise,
                new JSONObject(parametros), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //Toast.makeText(getApplicationContext(),"Toy Arta" + response.toString(), Toast.LENGTH_SHORT).show();
                    //JSONObject jsonObject = new JSONObject(response.getString("mensaje"));
                    JSONArray usuarioArray = response.getJSONArray( "usuario");

                    // arrayUsuario.clear();//limpiar la lista de usuario antes de comenzar a listar
                    arrayUsuario = new ArrayList<>();
                    for (int i=0; i<usuarioArray.length(); i++)
                    {
                        JSONObject RowUsuario = usuarioArray.getJSONObject(i);
                        usuario = new Usuario(  RowUsuario.getInt("codigo_usuario"),
                                RowUsuario.getString("nombres"),
                                RowUsuario.getString("apellidos"),
                                RowUsuario.getString("foto"));

                        //usuarioList.add(usuario);

                        arrayUsuario.add(usuario.getNombres()+' '+usuario.getApellidos());
                    }

                   // adp = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_checked, arrayUsuario);

                    ArrayAdapter<String> adp = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_checked, arrayUsuario);
                    listAmigos.setAdapter(adp);

                }catch (JSONException ex){
                    Toast.makeText(getApplicationContext(), "mensaje"+ex, Toast.LENGTH_SHORT).show();
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonObjectRequest);
    }*/

    class AdaptadorUsuario extends ArrayAdapter<Usuario> {

        AppCompatActivity appCompatActivity;

        AdaptadorUsuario(AppCompatActivity context, List<Usuario> usuarioList) {
            super(context, R.layout.amigo, listaUsuarios);
            appCompatActivity = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = appCompatActivity.getLayoutInflater();
            View item = inflater.inflate(R.layout.amigo, null);

            imgAmigo = item.findViewById(R.id.imgAmigo);
            TextView txt = item.findViewById(R.id.txtNombreAmigo);

            Usuario m = (Usuario) movieItems.get(position);
            txt.setText(m.getNombres());

            return(item);
        }
    }
}