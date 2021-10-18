package com.example.foundfood;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class menuparaclientes extends AppCompatActivity {

    private String NomEsta,NumTel,Estatus,Mesas_disponibles,idesta,iduser;

    private TextView NombreEsta,NumeroTelEsta,EstatusEsta,MesasDispoEsta,Disponibilidad;

    ImageView imgLogoEsta;
    private ImageView imgLlamar;

    private final int PHONE_CALL_CODE = 100;

    private String DominioURL;// = "https://fofo111.000webhostapp.com/app";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_para_clientes);

        VariablesGlobales Url = new VariablesGlobales();
        DominioURL = Url.getURL();

        NombreEsta = (TextView)findViewById(R.id.txtNomEsta);
        NumeroTelEsta = (TextView)findViewById(R.id.txtNumTel);
        EstatusEsta = (TextView)findViewById(R.id.txtEstatus);
        MesasDispoEsta = (TextView)findViewById(R.id.txtMesasDisponibles);
        Disponibilidad = (TextView)findViewById(R.id.txtDisponibilidad);

        imgLlamar = (ImageView) findViewById(R.id.imgLlamar);
        imgLogoEsta = findViewById(R.id.imgLogoEsta);

        ObtenemosElnombreEsta();

        imgLlamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PHONE_CALL_CODE);

                }

            }
        });
    }

    private void ObtenemosElnombreEsta(){
        //Se obtienen los datos mandados por la actividad LoginUusario
        Intent nombresta = this.getIntent();
        NomEsta = nombresta.getStringExtra("nombreesta");

        MostrarDatosEsta(NomEsta);
    }

    //Metodo para pasar a otra vista
    public void Comidas(View view) {
        Intent Comida = new Intent(this, comidas.class);
        Comida.putExtra("nombreesta",NomEsta);
        Comida.putExtra("iduseresta",idesta);
        startActivity(Comida);
        finish();
    }

    //Metodo para pasar a otra vista
    public void Bebidas(View view) {
        Intent bebida = new Intent(this, bebidas.class);
        bebida.putExtra("nombreesta",NomEsta);
        bebida.putExtra("iduseresta",idesta);
        startActivity(bebida);
        finish();
    }

    //Metodo para pasar a otra vista
    public void Postres(View view) {
        Intent postre = new Intent(this, postres.class);
        postre.putExtra("nombreesta",NomEsta);
        postre.putExtra("iduseresta",idesta);
        startActivity(postre);
        finish();
    }

    //Metodo para pasar a otra vista
    public void Botanas(View view) {
        Intent botanas = new Intent(this, com.example.foundfood.botanas.class);
        botanas.putExtra("nombreesta",NomEsta);
        botanas.putExtra("iduseresta",idesta);
        startActivity(botanas);
        finish();
    }

    public void Regresar(View view){
        Intent mapa = new Intent(this, vistamapa.class);
        startActivity(mapa);
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case PHONE_CALL_CODE:

                String permission = permissions[0];
                int result = grantResults[0];

                if (permission.equals(Manifest.permission.CALL_PHONE)) {
                    //Comprobar si ah sido aceptado o denegada la peticion de permiso
                    if (result == PackageManager.PERMISSION_GRANTED) {
                        //Acepto permisos

                        Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + NumTel));

                        if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                            return;
                        }
                        startActivity(intentCall);
                    } else {
                        //No Acepto permiso
                        Toast.makeText(getApplicationContext(), "Rechazaste los permisos", Toast.LENGTH_SHORT).show();
                    }

                }


                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }


    }

    private void MostrarDatosEsta(String NombreEstaa){

        final ProgressDialog loading = ProgressDialog.show(this, "Obteniendo información...", "Espere por favor");

        Response.Listener<String> respuesta = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonRespuesta = null;
                try {
                    jsonRespuesta = new JSONObject(response);
                    boolean ok = jsonRespuesta.getBoolean("success");
                    if (ok == true){
                        idesta = jsonRespuesta.getString("idesta");
                        String nombre = jsonRespuesta.getString("nombre");
                        String numerotel = jsonRespuesta.getString("numerotel");
                        String esatatus = jsonRespuesta.getString("esatatus");
                        String mesasdispo = jsonRespuesta.getString("mesasdispo");
                        String img = jsonRespuesta.getString("img");
                        String Disponible = jsonRespuesta.getString("disponible");
                        String horario = jsonRespuesta.getString("horario");

                        NumTel = numerotel;
                        CargarImagen(DominioURL+"/imgLogos/"+img+".jpg",nombre);
                        NombreEsta.setText(nombre);
                        NumeroTelEsta.setText(numerotel);
                        EstatusEsta.setText(esatatus);
                        MesasDispoEsta.setText(mesasdispo);

                        if(Disponible.equals("Cerrado")){
                            Disponibilidad.setText(Disponible+" "+horario);
                        }else{
                            Disponibilidad.setText(Disponible+" de: "+horario);
                        }

                        loading.dismiss();


                        //insertardatos(numerotel,esatatus,mesasdispo);
                        //Toast.makeText(getApplicationContext(),"Obtencion Exitosa",Toast.LENGTH_SHORT).show();
                    }else{
                        loading.dismiss();
                        AlertDialog.Builder alerta = new AlertDialog.Builder(menuparaclientes.this);
                        alerta.setMessage("Error de conexion").setNegativeButton("Reintentar", null).create().show();
                    }
                } catch (JSONException e) {
                    e.getMessage();
                }
            }
        };

        PerfilEstablecimientoInfoParaElUsuarioFinalRequest r = new PerfilEstablecimientoInfoParaElUsuarioFinalRequest(NombreEstaa.trim(),DominioURL,respuesta);
        RequestQueue cola = Volley.newRequestQueue(menuparaclientes.this);
        cola.add(r);
    }

    private void CargarImagen(String URL, final String Nombre){
        RequestQueue request = Volley.newRequestQueue(this);

        URL = URL.replace(" ","%20");

        ImageRequest imageRequest  = new ImageRequest(URL,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        imgLogoEsta.setImageBitmap(response);
                    }
                }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                imgLogoEsta.setImageResource(R.drawable.vistapreviainfo);
                //Toast.makeText(getApplicationContext(),"El establecimiento "+Nombre+" no a publicado su logo",Toast.LENGTH_SHORT).show();
            }
        });

        request.add(imageRequest);
    }
    private boolean CheckPermission(String permission) {
        int result = this.checkCallingOrSelfPermission(permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }
}