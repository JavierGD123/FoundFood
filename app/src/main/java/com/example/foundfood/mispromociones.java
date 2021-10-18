package com.example.foundfood;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
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

public class mispromociones extends AppCompatActivity {

    private AutoCompleteTextView NameProducto;
    private String Promociones[],iduser,idPro;
    private  int TotalRegistros;

    private TextView NombreProducto, DiaPromocion;

    private ImageView ImgPromo;

    private String NombrePro,Diapro,ImgPro;

    private RequestQueue request;
    private ImageRequest imageRequest;

    private String DominioURL;// = "https://fofo111.000webhostapp.com/app";

    int BusquedaPromo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mispromociones);

        VariablesGlobales Url = new VariablesGlobales();
        DominioURL = Url.getURL();

        NameProducto = (AutoCompleteTextView)findViewById(R.id.txtNombreProducto);

        DiaPromocion = (TextView)findViewById(R.id.txtNombreEsta);
        NombreProducto = (TextView)findViewById(R.id.txtNameProducto);
        ImgPromo = (ImageView) findViewById(R.id.imgPromo);

        CargarAutocompletador();

        request = Volley.newRequestQueue(mispromociones.this);
    }

    ///Botones para actualizar algun producto
    public void BuscarProducto(View view){
        String  NombreRefe = NameProducto.getText().toString();

        int noencontrado = 0;
        // Toast.makeText(getApplicationContext(),Promociones[0],Toast.LENGTH_SHORT).show();
        if(NombreRefe.equals("")){
            Toast.makeText(getApplicationContext(),"No ha seleccionado una promoción a buscar",Toast.LENGTH_SHORT).show();
        }else{
            for(int i = 0; i < Promociones.length; i++){
                //Toast.makeText(getApplicationContext(),NombreRefe,Toast.LENGTH_SHORT).show();
                if(NombreRefe.equals(Promociones[i])){
                    Mandardatos(Promociones[i]);
                    noencontrado = 1;
                    break;
                }

            }
        }

        if(noencontrado == 0){
            Toast.makeText(getApplicationContext(),"No hay promociones con esa referencia",Toast.LENGTH_SHORT).show();
        }

        BusquedaPromo = 1;
    }

    public void ActualizarPromocion(View view){

        if(BusquedaPromo == 0){
            Toast.makeText(getApplicationContext(),"Favor de buscar primero la promoción",Toast.LENGTH_SHORT).show();
        }else{
            String  NombreRefe = NameProducto.getText().toString();

            int noencontrado = 0;
            // Toast.makeText(getApplicationContext(),Promociones[0],Toast.LENGTH_SHORT).show();
            if(NombreRefe.equals("")){
                Toast.makeText(getApplicationContext(),"No ha seleccionado una promoción a actualizar",Toast.LENGTH_SHORT).show();
            }else{
                for(int i = 0; i < Promociones.length; i++){
                    //Toast.makeText(getApplicationContext(),NombreRefe,Toast.LENGTH_SHORT).show();
                    if(NombreRefe.equals(Promociones[i])){
                        IrActualizar();
                        noencontrado = 1;
                        break;
                    }

                }
            }

            if(noencontrado == 0){
                Toast.makeText(getApplicationContext(),"No hay promociones con esa referencia",Toast.LENGTH_SHORT).show();
            }
            BusquedaPromo = 0;
        }
    }

    /////Botones para eliminar algun producto
    public void EliminarPromocion(View view){

        if(BusquedaPromo == 0){
            Toast.makeText(getApplicationContext(),"Favor de buscar primero la promoción",Toast.LENGTH_SHORT).show();
        }else{
            Eliminar(NameProducto.getText().toString(), Promociones);
            BusquedaPromo = 0;
        }
    }

    private void Eliminar(String  NombreRefe, String ProductoaBuscar[]){
        int noencontrado = 0;
        // Toast.makeText(getApplicationContext(),Promociones[0],Toast.LENGTH_SHORT).show();
        if(NombreRefe.equals("")){
            Toast.makeText(getApplicationContext(),"No ha seleccionado un producto a actualizar",Toast.LENGTH_SHORT).show();
        }else{
            for(int i = 0; i < ProductoaBuscar.length; i++){
                //Toast.makeText(getApplicationContext(),NombreRefe,Toast.LENGTH_SHORT).show();
                if(NombreRefe.equals(ProductoaBuscar[i])){
                    EliminarPro(idPro,iduser,ImgPro);
                    NameProducto.setText("");
                    idPro = "";
                    DiaPromocion.setText("Dia: ");
                    NombreProducto.setText("Nombre: ");
                    Promociones = null;
                    noencontrado = 1;
                    break;
                }
            }
        }

        if(noencontrado == 0){
            Toast.makeText(getApplicationContext(),"No hay promociones con esa referencia",Toast.LENGTH_SHORT).show();
        }
    }

    public void Regresar(View view){
        Intent perfil = new Intent(this, perfilestablecimiento.class);
        perfil.putExtra("idusuario",iduser);
        startActivity(perfil);
        finish();
    }

    private void ActualizarUnaEliminacion(){
        Intent promo = new Intent(this, mispromociones.class);
        promo.putExtra("iduser",iduser);
        startActivity(promo);
        finish();
    }

    private void IrActualizar(){
        Intent actualizacion = new Intent(this, actualizarmispromociones.class);
        //Toast.makeText(getApplicationContext(),"IdPromo: "+idPro+", Iduser: "+iduser,Toast.LENGTH_SHORT).show();
        actualizacion.putExtra("iduser",iduser);
        actualizacion.putExtra("idpro",idPro);
        actualizacion.putExtra("nombre",NombrePro);
        actualizacion.putExtra("dia",Diapro);
        actualizacion.putExtra("img",ImgPro);
        NameProducto.setText("");
        iduser = "";
        idPro = "";
        startActivity(actualizacion);
        finish();
    }

    private void Mandardatos(String Nombre){

        final ProgressDialog loading = ProgressDialog.show(this, "Obteniendo información de la promoción...", "Espere por favor");

        Response.Listener<String> respuesta = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonRespuesta = null;
                try {
                    jsonRespuesta = new JSONObject(response);
                    boolean ok = jsonRespuesta.getBoolean("success");
                    if (ok == true){
                        idPro = jsonRespuesta.getString("idpromocion");
                        String nombre = jsonRespuesta.getString("nombreproducto");
                        String costo = jsonRespuesta.getString("dia");
                        String img = jsonRespuesta.getString("imagenpromocion");

                        insertardatos(nombre,costo,img);
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),"Obtencion Exitosa",Toast.LENGTH_SHORT).show();
                    }else{
                        loading.dismiss();
                        AlertDialog.Builder alerta = new AlertDialog.Builder(mispromociones.this);
                        alerta.setMessage("No existe esa promoción").setNegativeButton("Reintentar", null).create().show();
                    }
                } catch (JSONException e) {
                    loading.dismiss();
                    e.getMessage();
                }
            }
        };

        PerfilEstablecimientoMisPromocionesRequest r = new PerfilEstablecimientoMisPromocionesRequest(Nombre.trim(),DominioURL,respuesta);
        RequestQueue cola = Volley.newRequestQueue(mispromociones.this);
        cola.add(r);
    }

    private void EliminarPro(String idPro,String iduser,String img){

        final ProgressDialog loading = ProgressDialog.show(this, "Eliminando promoción ...", "Espere por favor");

        Response.Listener<String> respuesta = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonRespuesta = null;
                try {
                    jsonRespuesta = new JSONObject(response);
                    boolean ok = jsonRespuesta.getBoolean("success");
                    if (ok == true){
                        loading.dismiss();
                        ActualizarUnaEliminacion();
                        Toast.makeText(getApplicationContext(),"Eliminacion corecta",Toast.LENGTH_SHORT).show();
                    }else{
                        loading.dismiss();
                        AlertDialog.Builder alerta = new AlertDialog.Builder(mispromociones.this);
                        alerta.setMessage("Error no fue posible eliminar su promoción").setNegativeButton("Reintentar", null).create().show();
                    }
                } catch (JSONException e) {
                    loading.dismiss();
                    e.getMessage();
                }
            }
        };

        EiminarMisPromocionesRequest r = new EiminarMisPromocionesRequest(idPro.trim(),iduser.trim(),img,DominioURL,respuesta);
        RequestQueue cola = Volley.newRequestQueue(mispromociones.this);
        cola.add(r);
    }

    private void insertardatos(String nombre, String Dia,String img){

        NombrePro = nombre;
        Diapro = Dia;
        ImgPro = img;

        if(NombrePro != ""){
            NombreProducto.setText("Nombre: "+NombrePro);
            DiaPromocion.setText("Dia: "+Diapro);
            CargarImagen(DominioURL+"/imgPromociones/"+ImgPro+".jpg",1);
            //ImgProBotana.setImageBitmap(null);
        }else{
            Toast.makeText(getApplicationContext(),"No ha ingresado el nombre del producto de la promoción",Toast.LENGTH_SHORT).show();
        }
    }

    private void CargarImagen(String URL, final int tipo){

        URL = URL.replace(" ","%20");

        imageRequest  = new ImageRequest(URL,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        if(tipo==1){
                            ImgPromo.setImageBitmap(response);
                        }
                    }
                }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ImgPromo.setImageResource(R.drawable.vistaprevia);
                //Toast.makeText(getApplicationContext(),"El establecimiento "+Nombre+" no a publicado su logo",Toast.LENGTH_SHORT).show();
            }
        });

        request.add(imageRequest);
    }

    private void CargarAutocompletador(){
        ObtenemosId();

        CargarNombreProductosComidas(DominioURL+"/Obtenciondepromocionesporesta.php?iduseresta="+iduser);

    }

    private void ObtenemosId(){
        //Se obtienen los datos mandados por la actividad LoginUusario
        Intent id = this.getIntent();
        iduser = id.getStringExtra("iduser");
    }

    private void CargarNombreProductosComidas(String URL) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                TotalRegistros = response.length();

                Promociones = new String[TotalRegistros];

                //Toast.makeText(getApplicationContext(),TotalRegistros,Toast.LENGTH_SHORT).show();
                for (int i = 0; i < TotalRegistros; i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        //Se almacena cada dato del establecimiento
                        Promociones[i]  = jsonObject.getString("Nombre_Promo");
                        //Toast.makeText(getApplicationContext(),"Comida: "+ProductosComidas[i],Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                if(Promociones != null){
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(mispromociones.this,android.R.layout.simple_dropdown_item_1line,Promociones);
                    NameProducto.setAdapter(adapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "No existen promociones", Toast.LENGTH_SHORT).show();
                Promociones = new String[1];
            }
        });

        RequestQueue cola = Volley.newRequestQueue(mispromociones.this);
        cola.add(jsonArrayRequest);
    }
}
