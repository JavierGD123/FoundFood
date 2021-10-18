package com.example.foundfood;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class promociones extends AppCompatActivity {
    Bitmap[] imagenes;

    private String DominioURL;// = "https://fofo111.000webhostapp.com/app";

    private RequestQueue request;
    private ImageRequest imageRequest;

    private int TotalRegistros;

    private String Fecha_Actual;
    private String NombreEstablecimiento[];

    TextView NombreEsta, NombrePro,Separador;
    ImageView ImagenPromo;

    private LinearLayout Informacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promociones);

        VariablesGlobales Url = new VariablesGlobales();
        DominioURL = Url.getURL();

        request = Volley.newRequestQueue(promociones.this);

        Informacion = findViewById(R.id.layoutInfopromos);

       OptenerFechaActual();

       //lista.setAdapter(new Adaptador(this,datos,datosImg));

    }
    private  String iduser;

    public void Regresar(View view){
        //ObtenemosIdDePerfilUsuario();

        Intent menu = new Intent(this, vistamapa.class);
        //menu.putExtra("idusuario",iduser);
        startActivity(menu);
        finish();
    }

    private void ObtenemosIdDePerfilUsuario(){
        //Se obtienen los datos mandados por la actividad LoginUusario
        Intent datosperfil = this.getIntent();
        iduser = datosperfil.getStringExtra("idusuario");
    }

    private void OptenerFechaActual(){

        Date date = new Date();

        SimpleDateFormat d = new SimpleDateFormat("EEEE");
        Fecha_Actual = d.format(date);

        if(Fecha_Actual.equals("sábado")){
            Fecha_Actual = "sabado";
        }else if(Fecha_Actual.equals("miércoles")){
            Fecha_Actual = "miercoles";
        }

        CargarDatosPromociones(DominioURL+"/ObtenciondeInfodeCadaPromocion.php",Fecha_Actual);

        //datos= new String[2][2];

        //CargarDatosPromociones(DominioURL+"/ObtenciondeInfodeCadaPromocion.php",Fecha_Actual);


       // Toast.makeText(getApplicationContext(),"Registros: "+TotalRegistros,Toast.LENGTH_SHORT).show();

       /// Toast.makeText(getApplicationContext(),"Dato: "+datos[0][0],Toast.LENGTH_SHORT).show();

    }

    private void CargarDatosPromociones(String URL, final String FechaActual) {

        final int[] hayuno = {0};
        final ProgressDialog loading = ProgressDialog.show(this, "Obteniendo promociones...", "Espere por favor");

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                TotalRegistros = response.length();

                if(TotalRegistros != 0){

                    //final ProgressDialog loading = ProgressDialog.show(comidas.this, "Obteniendo comidas ofrecidas por el establecimiento...", "Espere por favor");

                    for (int i = 0; i < TotalRegistros; i++) {
                        try {

                            jsonObject = response.getJSONObject(i);
                            //Se almacena cada dato del establecimiento

                            String nombreesta = jsonObject.getString("Nombre_Establecimiento");
                            String NombrePromo = jsonObject.getString("Nombre_Promo");
                            String dia = jsonObject.getString("Dia");

                            //NombreEstablecimiento[i] = nombreesta;

                            //Toast.makeText(getApplicationContext(),"Dia:"+Fecha_Actual+", "+dia,Toast.LENGTH_SHORT).show();

                            if(FechaActual.equals(dia)){
                                CargarImagen(DominioURL+"/imgPromociones/"+jsonObject.getString("Imagen")+".jpg",nombreesta,NombrePromo);
                                loading.dismiss();
                                hayuno[0] = 1;
                                //loading.dismiss();
                                //CargarImagen(DominioURL+"/imgPromociones/"+jsonObject.getString("Imagen")+".jpg",nombreesta,NombrePromo,i);
                                //Toast.makeText(getApplicationContext(),"Dia: "+Fecha_Actual,Toast.LENGTH_SHORT).show();
                            }else{
                                loading.dismiss();
                            }



                        } catch (JSONException e) {
                            // loading.dismiss();
                            //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    if(hayuno[0] == 0){
                        Toast.makeText(getApplicationContext(),"Hoy no hay promociones",Toast.LENGTH_SHORT).show();
                    }

                }else{
                    loading.dismiss();
                    ///Toast.makeText(getApplicationContext(),"No hay promociones",Toast.LENGTH_SHORT).show();
                }
            }

            //Toast.makeText(getApplicationContext(), "Total decomidas"+TotalRegistrosCmidas, Toast.LENGTH_SHORT).show();

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
               // if(sinproductos == 0){
                    NombrePro = new TextView(promociones.this);
                    NombrePro.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT, Color.BLUE));
                    NombrePro.setText("No existen promociones");
                    //NombrePro.setInputType("@font/aclonica");
                    NombrePro.setTextSize(25);
                    Informacion.addView(NombrePro);
                    //sinproductos = 1;
                //}
               // Toast.makeText(getApplicationContext(), "No existen promociones", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue cola = Volley.newRequestQueue(promociones.this);
        cola.add(jsonArrayRequest);

        ///Contadordetiempo();
    }

    private void CargarImagen(String URL, final String Nombre, final String NombrePromo){

        URL = URL.replace(" ","%20");

        imageRequest  = new ImageRequest(URL,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        NombreEsta = new TextView(promociones.this);
                        NombrePro =  new TextView(promociones.this);
                        Separador = new TextView(promociones.this);
                        ImagenPromo = new ImageView(promociones.this);

                        NombreEsta.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT, Color.BLUE));
                        NombreEsta.setText("\nEstablecimiento: \n"+Nombre);
                        NombreEsta.setTextSize(25);


                        NombrePro.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
                        NombrePro.setText("Producto: \n"+NombrePromo+"\n");
                        NombrePro.setTextSize(25);
                        //CostoPro.setTextColor(Color.parseColor("#387192"));

                        ImagenPromo.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,1500));
                        //FotoProducto.setHe
                        ImagenPromo.setImageBitmap(response);
                        ImagenPromo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //Toast.makeText(getApplicationContext(),"Hola",Toast.LENGTH_SHORT).show();
                            }
                        });

                        Separador.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,20));
                        //Separador.setBackground(R.drawable.boton1);
                        Separador.setBackgroundResource(R.drawable.separador);

                        Informacion.addView(NombreEsta);
                        Informacion.addView(NombrePro);
                        Informacion.addView(ImagenPromo);
                        Informacion.addView(Separador);

                    }
                }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NombreEsta = new TextView(promociones.this);
                NombrePro =  new TextView(promociones.this);
                ImagenPromo = new ImageView(promociones.this);

                NombreEsta.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT, Color.BLUE));
                NombreEsta.setText("Establecimiento: "+Nombre);

                NombrePro.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                NombrePro.setText("Producto: "+NombrePromo);
                //CostoPro.setTextColor(Color.parseColor("#387192"));

                ImagenPromo.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                ImagenPromo.setImageResource(R.drawable.vistaprevia);

                Informacion.addView(NombreEsta);
                Informacion.addView(NombrePro);
                Informacion.addView(ImagenPromo);
                Informacion.addView(Separador);
                // Toast.makeText(getApplicationContext(),"Error al cargar la image",Toast.LENGTH_SHORT).show();
            }
        });

        request.add(imageRequest);
    }

    private void Abrirubicacion(String NombreEsta){
        Toast.makeText(getApplicationContext(),NombreEsta,Toast.LENGTH_SHORT).show();
    }
}
