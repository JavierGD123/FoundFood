package com.example.foundfood;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class postres extends AppCompatActivity {

    private TextView NombrePro,CostoPro,Separador;
    private ImageView FotoProducto;
    private LinearLayout Info;

    private String IDUser,Nombreesta;

    private  int TotalRegistrosPostre;

    private RequestQueue request;
    private ImageRequest imageRequest;
    private String Paquetecomprado,PagoPaquete;

    private String DominioURL;// = "https://fofo111.000webhostapp.com/app";

    //PAQUETE SENCILLO///////////////////
    private int PostrespermitidasS = 3;
    ////////////////////////////////////

    //PAQUETE PREMIUM///////////////////
    private int PostrespermitidasP =   10;
    ////////////////////////////////////

    //PAQUETE PRUEBA///////////////////
    private int ComidaspermitidasPR =  1;
    ////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postres);

        VariablesGlobales Url = new VariablesGlobales();
        DominioURL = Url.getURL();

        ObtenemosId();

        Info = (LinearLayout)findViewById(R.id.layoudInfo);

        request = Volley.newRequestQueue(postres.this);

        Contadordetiempo();
        //CargarDatosProductosPostres("https://fofo111.000webhostapp.com/ObtenciondeInfodeCadaProductoPorEstablecimiento.php?idesta="+IDUser+"&tipopro=Postre");

    }

    private void ObtenemosId(){
        //Se obtienen los datos mandados por la actividad LoginUusario
        Intent id = this.getIntent();
        IDUser = id.getStringExtra("iduseresta");
        Nombreesta = id.getStringExtra("nombreesta");

        ObtenenciondePauqetecomprado(IDUser,"si");
    }

    public void Regresar(View view){
        Intent menu = new Intent(this, menuparaclientes.class);
        menu.putExtra("nombreesta",Nombreesta);
        startActivity(menu);
        finish();
    }

    private int nuevoesta = 0,sinproductos = 0;

    private void CargarDatosProductosPostres(String URL) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                //TotalRegistrosPostre = response.length();

                //Toast.makeText(getApplicationContext(),"Total registros: "+TotalRegistrosPostre,Toast.LENGTH_SHORT).show();

                if(TotalRegistrosPostre < response.length()){
                    TotalRegistrosPostre = response.length();
                    nuevoesta = 1;
                }
                // Toast.makeText(getApplicationContext(),"Total registros",Toast.LENGTH_SHORT).show();
                if(nuevoesta == 1){

                    if(Paquetecomprado.equals("sencillo") && PagoPaquete.equals("si")){
                        TotalRegistrosPostre = PostrespermitidasS;
                       // Toast.makeText(getApplicationContext(),"Paquere del vendedor: "+Paquetecomprado,Toast.LENGTH_SHORT).show();
                    }else   if(Paquetecomprado.equals("premium") && PagoPaquete.equals("si")){
                        TotalRegistrosPostre = PostrespermitidasP;
                       // Toast.makeText(getApplicationContext(),"Paquere del vendedor: "+Paquetecomprado,Toast.LENGTH_SHORT).show();
                    }else if(Paquetecomprado.equals("avanzado") && PagoPaquete.equals("si")){
                        // Toast.makeText(getApplicationContext(),"Paquere del vendedor: "+Paquetecomprado,Toast.LENGTH_SHORT).show();
                    }else {
                        TotalRegistrosPostre = ComidaspermitidasPR;
                        // Toast.makeText(getApplicationContext(),"Paquere del vendedor: "+Paquetecomprado,Toast.LENGTH_SHORT).show();
                    }

                    if(TotalRegistrosPostre != 0){
                       //Toast.makeText(getApplicationContext(),"Total registros: "+TotalRegistrosPostre,Toast.LENGTH_SHORT).show();
                        final ProgressDialog loading = ProgressDialog.show(postres.this, "Obteniendo postres ofrecidos por el establecimiento...", "Espere por favor");

                        for (int i = 0; i < TotalRegistrosPostre; i++) {
                            try {
                                loading.dismiss();
                                jsonObject = response.getJSONObject(i);
                                //Se almacena cada dato del establecimiento

                                String nombre = jsonObject.getString("Nombre"),costo = jsonObject.getString("Costo");

                                CargarImagen(DominioURL+"/imgPostres/"+jsonObject.getString("Imagen")+".jpg",nombre,costo);

                            } catch (JSONException e) {
                                loading.dismiss();
                                //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        TotalRegistrosPostre = response.length();
                    }else{
                        Toast.makeText(getApplicationContext(),"No hay postres que ofrecer",Toast.LENGTH_SHORT).show();
                    }

                    nuevoesta = 0;
                    sinproductos = 1;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(sinproductos == 0){
                    NombrePro = new TextView(postres.this);
                    NombrePro.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT, Color.BLUE));
                    NombrePro.setText("No hay postres que ofrecer");
                    //NombrePro.setInputType("@font/aclonica");
                    NombrePro.setTextSize(25);
                    Info.addView(NombrePro);
                    sinproductos = 1;
                }
                //Toast.makeText(getApplicationContext(), "No existen productos de comida", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue cola = Volley.newRequestQueue(postres.this);
        cola.add(jsonArrayRequest);

        Contadordetiempo();
    }

    private void CargarImagen(String URL, final String Nombre, final String Costo){

        URL = URL.replace(" ","%20");

        imageRequest  = new ImageRequest(URL,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        NombrePro = new TextView(postres.this);
                        CostoPro =  new TextView(postres.this);
                        Separador =  new TextView(postres.this);
                        FotoProducto = new ImageView(postres.this);

                        NombrePro.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT, Color.BLUE));
                        NombrePro.setText("Nombre: "+Nombre);
                        NombrePro.setTextSize(25);

                        CostoPro.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                        CostoPro.setText("Costo: $"+Costo+" Pesos");
                        CostoPro.setTextSize(25);
                        CostoPro.setTextColor(Color.parseColor("#387192"));

                        FotoProducto.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,1550));
                        FotoProducto.setImageBitmap(response);

                        Separador.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,20));
                        //Separador.setBackground(R.drawable.boton1);
                        Separador.setBackgroundResource(R.drawable.separador);

                        Info.addView(FotoProducto);
                        Info.addView(NombrePro);
                        Info.addView(CostoPro);
                        Info.addView(Separador);

                    }
                }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NombrePro = new TextView(postres.this);
                CostoPro =  new TextView(postres.this);
                Separador =  new TextView(postres.this);
                FotoProducto = new ImageView(postres.this);

                NombrePro.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT, Color.BLUE));
                NombrePro.setText("Nombre: "+Nombre);

                CostoPro.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                CostoPro.setText("Costo: $"+Costo+" Pesos");
                CostoPro.setTextColor(Color.parseColor("#387192"));

                FotoProducto.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
                FotoProducto.setImageResource(R.drawable.vistapreviainfo);

                Separador.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,20));
                //Separador.setBackground(R.drawable.boton1);
                Separador.setBackgroundResource(R.drawable.separador);

                Info.addView(FotoProducto);
                Info.addView(NombrePro);
                Info.addView(CostoPro);
                Info.addView(Separador);

                //Toast.makeText(getApplicationContext(),"Error al cargar la image",Toast.LENGTH_SHORT).show();
            }
        });

        request.add(imageRequest);
    }

    private void Contadordetiempo(){

        new CountDownTimer(1000,1000) {
            @Override

            public void onTick(long l) {
                Log.e("segundos restantes",""+l/1000);
            }

            @Override
            public void onFinish() {
                // Toast.makeText(getApplicationContext(),"Puntos actualizados",Toast.LENGTH_SHORT).show();
                //mMap.clear();
                CargarDatosProductosPostres(DominioURL+"/ObtenciondeInfodeCadaProductoPorEstablecimiento.php?idesta="+IDUser+"&tipopro=Postre");
            }
        }.start();
    }

    private void ObtenenciondePauqetecomprado(final String iduser, String pago){
        //Toast.makeText(getApplicationContext(),iduser+pago,Toast.LENGTH_LONG).show();
        // final ProgressDialog loading = ProgressDialog.show(this, "Cargando sus datos ...", "Espere por favor");

        Response.Listener<String> respuesta = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                JSONObject jsonRespuesta = null;
                try {
                    jsonRespuesta = new JSONObject(response);
                    boolean ok = jsonRespuesta.getBoolean("success");
                    if (ok == true){
                        //loading.dismiss();

                        Paquetecomprado = jsonRespuesta.getString("nombre");
                        PagoPaquete = jsonRespuesta.getString("pagopaquete");

                    }else{
                        ///AlertDialog.Builder alerta = new AlertDialog.Builder(perfilestablecimiento.this);
                        ///alerta.setMessage("Usuario o Contraseña incorrecta").setNegativeButton("Reintentar", null).create().show();
                    }
                } catch (JSONException e) {
                    e.getMessage();
                }
            }
        };

        PerfilEstablecimientoPaqueteRequest r = new PerfilEstablecimientoPaqueteRequest(iduser.trim(),pago.trim(),respuesta);
        RequestQueue cola = Volley.newRequestQueue(this);
        cola.add(r);
    }
}
