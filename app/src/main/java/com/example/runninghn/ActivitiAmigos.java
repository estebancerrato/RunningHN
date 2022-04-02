package com.example.runninghn;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.example.runninghn.Modelo.RestApiMethods;
import com.example.runninghn.Modelo.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivitiAmigos extends AppCompatActivity {

    ImageView imgAmigo;
    ListView listViewCustomAdapter;
    Usuario usuario;
    TextView txtnombreCompleto;
    AdaptadorUsuario adaptador;

    private final ArrayList<Usuario> listaUsuarios = new ArrayList<>();
    final Context context = this;

    int amigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activiti_amigos);

        listViewCustomAdapter = findViewById(R.id.listaMisAmigos);
        adaptador = new ActivitiAmigos.AdaptadorUsuario(this);

        String codigoUser = RestApiMethods.codigo_usuario;
        listarUsuarios(codigoUser);

    }
    //consulta en la base de datos el pais del correo que se logueo, luego manda a llamar el listado de personas de ese pais
    private void listarUsuarios(String codUser) {
        RequestQueue queue = Volley.newRequestQueue(this);
        HashMap<String, String> parametros = new HashMap<>();
        parametros.put("codigo_usuario",codUser);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, RestApiMethods.EndPointListaAmigosAdd,
                new JSONObject(parametros), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray usuarioArray = response.getJSONArray("amigo");

                    listaUsuarios.clear();//limpiar la lista de usuario antes de comenzar a listar
                    for (int i = 0; i < usuarioArray.length(); i++) {
                        JSONObject RowUsuario = usuarioArray.getJSONObject(i);
                        usuario = new Usuario(RowUsuario.getInt("codigo_usuario"),
                                RowUsuario.getString("nombres"),
                                RowUsuario.getString("apellidos"),
                                RowUsuario.getString("foto"));

                        listaUsuarios.add(usuario);
                    }
                    listViewCustomAdapter.setAdapter(adaptador);

                } catch (JSONException ex) {
                    Toast.makeText(getApplicationContext(), "No tienes amigos en tu lista", Toast.LENGTH_SHORT).show();
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonObjectRequest);
    }


    class AdaptadorUsuario extends ArrayAdapter<Usuario> {

        AppCompatActivity appCompatActivity;

        AdaptadorUsuario(AppCompatActivity context) {
            super(context, R.layout.amigo, listaUsuarios);
            appCompatActivity = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = appCompatActivity.getLayoutInflater();
            View item = inflater.inflate(R.layout.amigo, null);

            imgAmigo = item.findViewById(R.id.imgAmigo);
            mostrarFoto(listaUsuarios.get(position).getFoto(),imgAmigo);

            txtnombreCompleto = item.findViewById(R.id.txtNombreAmigo);
            String nombrecompleto= listaUsuarios.get(position).getNombres()+" "+listaUsuarios.get(position).getApellidos();
            txtnombreCompleto.setText(nombrecompleto);

            CheckBox cBox=(CheckBox)item.findViewById(R.id.checkBox);
            cBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (cBox.isChecked()){

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                        alertDialogBuilder.setTitle("Eliminar amigo");
                        alertDialogBuilder
                                .setMessage("¿Desea Eliminar de amigos a "+nombrecompleto+" ?")
                                .setCancelable(false)
                                .setPositiveButton("Si",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        eliminarAmigo(Integer.valueOf(RestApiMethods.codigo_usuario),amigo);

                                    }
                                })
                                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        cBox.setChecked(false);
                                        dialog.cancel();
                                    }
                                });


                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }else{

                    }
                    Toast.makeText(getApplicationContext(), "Se Elimino de tu lista de amigos"+amigo, Toast.LENGTH_SHORT).show();

                    amigo = listaUsuarios.get(position).getId();
                }
            });

            return(item);
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

    private void eliminarAmigo(int codigoUsuario, int codigoAmigo) {
        RequestQueue queue = Volley.newRequestQueue(this);
        HashMap<String, String> parametros = new HashMap<>();
        parametros.put("codigo_usuario", codigoUsuario+"");
        parametros.put("codigo_amigo", codigoAmigo+"");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, RestApiMethods.EndPointEliminarAmigosAdd,
                new JSONObject(parametros), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("mensaje").toString().equals("Amigo eliminado")){

                        Toast.makeText(getApplicationContext(), "Se Elimino de tu lista de amigos", Toast.LENGTH_SHORT).show();
                    }
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
}