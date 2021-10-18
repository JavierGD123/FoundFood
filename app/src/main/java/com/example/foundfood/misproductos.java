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

public class misproductos extends AppCompatActivity {
    private AutoCompleteTextView NombreProductoComida,NombreProductoBebida,NombreProductoPostre,NombreProductoBotana;
    private String ProductosComidas[],ProductosBebidas[],ProductosPostres[],ProductosBotanas[], iduser,idPro,TotalProductos[];
    private  int TotalRegistrosCmidas,TotalRegistrosBebidas,TotalRegistrosPostres,TotalRegistrosBotanas,TotalRegisros;

    private TextView NombreProComida,CostoProComida;
    private TextView NombreProBebida,CostoProBebida;
    private TextView NombreProPostre,CostoProPostre;
    private TextView NombreProBotana,CostoProBotana;

    private ImageView ImgProComida,ImgProBebida,ImgProPostre,ImgProBotana;

    private String NombrePro,CostoPro,ImgPro;

    private RequestQueue request;
    private ImageRequest imageRequest;

    private String DominioURL;// = "https://fofo111.000webhostapp.com/app";

    int BusquedaComida,BusquedaBebida, BusquedaPostre, BusquedaBotana;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_productos);

        VariablesGlobales Url = new VariablesGlobales();
        DominioURL = Url.getURL();

        NombreProductoComida = (AutoCompleteTextView)findViewById(R.id.txtNombreProductoComida);
        NombreProductoBebida = (AutoCompleteTextView)findViewById(R.id.txtNombreProductoBebida);
        NombreProductoPostre = (AutoCompleteTextView)findViewById(R.id.txtNombreProductoPostre);
        NombreProductoBotana = (AutoCompleteTextView)findViewById(R.id.txtNombreProductoBotana);

        NombreProComida = (TextView)findViewById(R.id.txtNombreComida);
        CostoProComida = (TextView)findViewById(R.id.txtCostoComida);
        ImgProComida = (ImageView) findViewById(R.id.imgComida);

        NombreProBebida = (TextView)findViewById(R.id.txtNombreBebida);
        CostoProBebida = (TextView)findViewById(R.id.txtCostoBebida);
        ImgProBebida  = (ImageView)findViewById(R.id.imgBebida);

        NombreProPostre = (TextView)findViewById(R.id.txtNombrePostre);
        CostoProPostre = (TextView)findViewById(R.id.txtCostoPostre);
        ImgProPostre  = (ImageView)findViewById(R.id.imgPostre);

        NombreProBotana = (TextView)findViewById(R.id.txtNombreBotana);
        CostoProBotana = (TextView)findViewById(R.id.txtCostoBotana);
        ImgProBotana  = (ImageView)findViewById(R.id.imgBotana);


        CargarAutocompletador();

        request = Volley.newRequestQueue(misproductos.this);
    }

    //Seccion de botones
    ///Botones para buscar algun producto
    public void BuscarProductoComida(View view){
        Buscar(NombreProductoComida.getText().toString(),ProductosComidas,"Comida");
        BusquedaComida = 1;
    }

    public void BuscarProductoBebida(View view){
        Buscar(NombreProductoBebida.getText().toString(),ProductosBebidas,"Bebida");
        BusquedaBebida = 1;
    }

    public void BuscarProductoPostre(View view){
        Buscar(NombreProductoPostre.getText().toString(),ProductosPostres,"Postre");
        BusquedaPostre = 1;

    }

    public void BuscarProductoBotana(View view){
        Buscar(NombreProductoBotana.getText().toString(),ProductosBotanas,"Botana");
        BusquedaBotana = 1;
    }

    private void Buscar(String  NombreRefe, String ProductoaBuscar[],String TipoProducto){
        int encontrado = 0;
        // Toast.makeText(getApplicationContext(),Promociones[0],Toast.LENGTH_SHORT).show();
        if(NombreRefe.equals("")){
            Toast.makeText(getApplicationContext(),"No ha seleccionado un producto a buscar",Toast.LENGTH_SHORT).show();
        }else{
            for(int i = 0; i < ProductoaBuscar.length; i++){
                //Toast.makeText(getApplicationContext(),NombreRefe,Toast.LENGTH_SHORT).show();
                if(NombreRefe.equals(ProductoaBuscar[i])){
                    Mandardatos(TipoProducto,NombreRefe);
                    encontrado = 1;
                    break;
                }

            }
        }

        if(encontrado == 0){
            Toast.makeText(getApplicationContext(),"No hay "+TipoProducto+"s con esa referencia",Toast.LENGTH_SHORT).show();
        }
    }
//////////////////////////////////////////////////////////////////////////

    ///Botones para actualizar algun producto
    public void ActualizarComida(View view){
        if(BusquedaComida == 0){
            Toast.makeText(getApplicationContext(),"Favor de buscar primero el producto",Toast.LENGTH_SHORT).show();
        }else{
            Actualizar(NombreProductoComida.getText().toString(),ProductosComidas,"Comida");
            BusquedaComida = 0;
        }
    }

    public void ActualizarBebida(View view){
        if(BusquedaBebida == 0){
            Toast.makeText(getApplicationContext(),"Favor de buscar primero el producto",Toast.LENGTH_SHORT).show();
        }else{
            Actualizar(NombreProductoBebida.getText().toString(),ProductosBebidas,"Bebida");
            BusquedaBebida = 0;
        }
    }

    public void ActualizarPostre(View view){
        if(BusquedaPostre == 0){
            Toast.makeText(getApplicationContext(),"Favor de buscar primero el producto",Toast.LENGTH_SHORT).show();
        }else{
            Actualizar(NombreProductoPostre.getText().toString(),ProductosPostres,"Postre");
            BusquedaPostre = 0;
        }
    }

    public void ActualizarBotana(View view){
        if(BusquedaBotana == 0){
            Toast.makeText(getApplicationContext(),"Favor de buscar primero el producto",Toast.LENGTH_SHORT).show();
        }else{
            Actualizar(NombreProductoBotana.getText().toString(),ProductosBotanas,"Botana");
            BusquedaBotana = 0;
        }
    }

    private void Actualizar(String  NombreRefe, String ProductoaBuscar[],String TipoProducto){
        int encontrado = 0;
        // Toast.makeText(getApplicationContext(),Promociones[0],Toast.LENGTH_SHORT).show();
        if(NombreRefe.equals("")){
            Toast.makeText(getApplicationContext(),"No ha seleccionado un producto a actualizar",Toast.LENGTH_SHORT).show();
        }else{
            for(int i = 0; i < ProductoaBuscar.length; i++){
                //Toast.makeText(getApplicationContext(),NombreRefe,Toast.LENGTH_SHORT).show();
                if(NombreRefe.equals(ProductoaBuscar[i])){
                    IrActualizar(TipoProducto);
                    encontrado = 1;
                    break;
                }

            }
        }

        if(encontrado == 0){
            Toast.makeText(getApplicationContext(),"No hay "+TipoProducto+"s con esa referencia",Toast.LENGTH_SHORT).show();
        }
    }

 ///////////////////////////////////////////////////
    /////Botones para eliminar algun producto

    private void Eliminar(String  NombreRefe, String ProductoaBuscar[],String TipoProducto){
        int encontrado = 0;
        // Toast.makeText(getApplicationContext(),Promociones[0],Toast.LENGTH_SHORT).show();
        if(NombreRefe.equals("")){
            Toast.makeText(getApplicationContext(),"No ha seleccionado un producto a actualizar",Toast.LENGTH_SHORT).show();
        }else{
            for(int i = 0; i < ProductoaBuscar.length; i++){
                //Toast.makeText(getApplicationContext(),NombreRefe,Toast.LENGTH_SHORT).show();
                if(NombreRefe.equals(ProductoaBuscar[i])){
                    EliminarPro(TipoProducto,idPro,iduser,ImgPro);

                    if(TipoProducto.equals("Comida")){
                        NombreProductoComida.setText("");
                        idPro = "";
                        NombreProComida.setText("Nombre: ");
                        CostoProComida.setText("Costo: ");
                        ProductosComidas = null;

                    }else  if(TipoProducto.equals("Bebida")){
                        NombreProductoBebida.setText("");
                        idPro = "";
                        NombreProBebida.setText("Nombre: ");
                        CostoProBebida.setText("Costo: ");
                        ProductosBebidas = null;
                    }else  if(TipoProducto.equals("Postre")){
                        NombreProductoPostre.setText("");
                        idPro = "";
                        NombreProPostre.setText("Nombre: ");
                        CostoProPostre.setText("Costo: ");
                        ProductosPostres = null;
                    }else{
                        NombreProductoBotana.setText("");
                        idPro = "";
                        NombreProBotana.setText("Nombre: ");
                        CostoProBotana.setText("Costo: ");
                        ProductosBotanas = null;
                    }

                    encontrado = 1;
                    break;
                }

            }
        }

        if(encontrado == 0){
            Toast.makeText(getApplicationContext(),"No hay "+TipoProducto+"s con esa referencia",Toast.LENGTH_SHORT).show();
        }
    }

    public void EliminarComida(View view){
        if(BusquedaComida == 0){
            Toast.makeText(getApplicationContext(),"Favor de buscar primero el producto",Toast.LENGTH_SHORT).show();
        }else{
            Eliminar(NombreProductoComida.getText().toString(),ProductosComidas,"Comida");
            BusquedaComida = 0;
        }
    }

    public void EliminarBebida(View view){
        if(BusquedaBebida == 0){
            Toast.makeText(getApplicationContext(),"Favor de buscar primero el producto",Toast.LENGTH_SHORT).show();
        }else{
            Eliminar(NombreProductoBebida.getText().toString(),ProductosBebidas,"Bebida");
            BusquedaBebida = 0;
        }

    }
    public void EliminarPostre(View view){
        if(BusquedaPostre == 0){
            Toast.makeText(getApplicationContext(),"Favor de buscar primero el producto",Toast.LENGTH_SHORT).show();
        }else{
            Eliminar(NombreProductoPostre.getText().toString(),ProductosPostres,"Postre");
            BusquedaPostre = 0;
        }
    }

    public void EliminarBotana(View view){
        if(BusquedaBotana == 0){
            Toast.makeText(getApplicationContext(),"Favor de buscar primero el producto",Toast.LENGTH_SHORT).show();
        }else{
            Eliminar(NombreProductoBotana.getText().toString(),ProductosBotanas,"Botana");
            BusquedaBotana = 0;
        }
    }

    public void Regresar(View view){
        Intent perfil = new Intent(this, perfilestablecimiento.class);
        perfil.putExtra("idusuario",iduser);
        startActivity(perfil);
        finish();
    }

    private void ActualizarUnaEliminacion(){
        Intent mapa = new Intent(this, misproductos.class);
        mapa.putExtra("iduser",iduser);
        startActivity(mapa);
        finish();
    }

    private void IrActualizar(String tipo){
        Intent actualizacion = new Intent(this, actualizarmiproducto.class);
        actualizacion.putExtra("iduser",iduser);
        actualizacion.putExtra("idpro",idPro);
        actualizacion.putExtra("nombre",NombrePro);
        actualizacion.putExtra("costo",CostoPro);
        actualizacion.putExtra("tipo",tipo);
        actualizacion.putExtra("img",ImgPro);
        NombreProductoComida.setText("");
        NombreProductoBebida.setText("");
        NombreProductoPostre.setText("");
        NombreProductoBotana.setText("");
        iduser = "";
        idPro = "";
        startActivity(actualizacion);
        finish();
    }
    ///////////////////////////////////

    private void Mandardatos(final String Tipo, String Nombre){

        final ProgressDialog loading = ProgressDialog.show(this, "Obteniendo información del producto...", "Espere por favor");

        Response.Listener<String> respuesta = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonRespuesta = null;
                try {
                    jsonRespuesta = new JSONObject(response);
                    boolean ok = jsonRespuesta.getBoolean("success");
                    if (ok == true){
                        idPro = jsonRespuesta.getString("idproducto");
                        String nombre = jsonRespuesta.getString("nombreproducto");
                        String costo = jsonRespuesta.getString("costoproducto");
                        String img = jsonRespuesta.getString("imagenproducto");
                        insertardatos(Tipo,nombre,costo,img);
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),"Obtencion Exitosa",Toast.LENGTH_SHORT).show();
                    }else{
                        loading.dismiss();
                        AlertDialog.Builder alerta = new AlertDialog.Builder(misproductos.this);
                        alerta.setMessage("No existe el producto").setNegativeButton("Reintentar", null).create().show();
                    }
                } catch (JSONException e) {
                    loading.dismiss();
                    e.getMessage();
                }
            }
        };

        loading.dismiss();
        PerfilEstablecimientoMisProductosRequest r = new PerfilEstablecimientoMisProductosRequest(Tipo.trim(),Nombre.trim(),DominioURL,respuesta);
        RequestQueue cola = Volley.newRequestQueue(misproductos.this);
        cola.add(r);
    }

    private void EliminarPro(String Tipo, String idPro,String iduser,String img){

        final ProgressDialog loading = ProgressDialog.show(this, "Eliminando producto ...", "Espere por favor");

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
                        Toast.makeText(getApplicationContext(),"Eliminación correcta",Toast.LENGTH_SHORT).show();
                    }else{
                        loading.dismiss();
                        AlertDialog.Builder alerta = new AlertDialog.Builder(misproductos.this);
                        alerta.setMessage("Error no fue posible eliminar su producto").setNegativeButton("Reintentar", null).create().show();
                    }
                } catch (JSONException e) {
                    loading.dismiss();
                    e.getMessage();
                }
            }
        };

        EiminarMisProductosRequest r = new EiminarMisProductosRequest(Tipo.trim(),idPro.trim(),iduser.trim(),img.trim(),DominioURL,respuesta);
        RequestQueue cola = Volley.newRequestQueue(misproductos.this);
        cola.add(r);
    }


    private void insertardatos(String tipo, String nombre, String Costo,String img){

        NombrePro = nombre;
        CostoPro = Costo;
        ImgPro = img;

        if(tipo.equals("Comida")){
            NombreProComida.setText("Nombre: "+nombre);
            CostoProComida.setText("Costo: "+Costo+" pesos");
            CargarImagen(DominioURL+"/imgComidas/"+img+".jpg",1);
            //ImgProComida.setImageBitmap(null);
        }else if(tipo.equals("Bebida")){
            NombreProBebida.setText("Nombre: "+nombre);
            CostoProBebida.setText("Costo: "+Costo+" pesos");
            CargarImagen(DominioURL+"/imgBebidas/"+img+".jpg",2);
            //ImgProBebida.setImageBitmap(null);
        }else if(tipo.equals("Postre")){
            NombreProPostre.setText("Nombre: "+nombre);
            CostoProPostre.setText("Costo: "+Costo+" pesos");
            CargarImagen(DominioURL+"/imgPostres/"+img+".jpg",3);
            //ImgProPostre.setImageBitmap(null);
        }else if(tipo.equals("Botana")){
            NombreProBotana.setText("Nombre: "+nombre);
            CostoProBotana.setText("Costo: "+Costo+" pesos");
            CargarImagen(DominioURL+"/imgBotanas/"+img+".jpg",4);
            //ImgProBotana.setImageBitmap(null);
        }else{
            Toast.makeText(getApplicationContext(),"No ha ingresado el nombre del producto "+tipo,Toast.LENGTH_SHORT).show();
        }
    }
///////
private void CargarImagen(String URL, final int tipo){

    URL = URL.replace(" ","%20");

    imageRequest  = new ImageRequest(URL,
            new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    if(tipo==1){
                        ImgProComida.setImageBitmap(response);
                    }else if(tipo==2){
                        ImgProBebida.setImageBitmap(response);
                    }if(tipo==3){
                        ImgProPostre.setImageBitmap(response);
                    }if(tipo==4){
                        ImgProBotana.setImageBitmap(response);
                    }
                }
            }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            ImgProComida.setImageResource(R.drawable.vistaprevia);
            //Toast.makeText(getApplicationContext(),"El establecimiento "+Nombre+" no a publicado su logo",Toast.LENGTH_SHORT).show();
            }
    });

        request.add(imageRequest);
    }

    private void CargarAutocompletador(){
        ObtenemosId();

        CargarNombreProductosComidas(DominioURL+"/ObtenciondeproductosComidas.php?iduseresta="+iduser);
        CargarNombreProductosPostres(DominioURL+"/ObtenciondeproductosPostres.php?iduseresta="+iduser);
        CargarNombreProductosBotanas(DominioURL+"/ObtenciondeproductosBotanas.php?iduseresta="+iduser);
        CargarNombreProductosBebidas(DominioURL+"/ObtenciondeproductosBebidas.php?iduseresta="+iduser);

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
                TotalRegistrosCmidas = response.length();

                ProductosComidas = new String[TotalRegistrosCmidas];

                //Toast.makeText(getApplicationContext(),TotalRegistros,Toast.LENGTH_SHORT).show();
                for (int i = 0; i < TotalRegistrosCmidas; i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        //Se almacena cada dato del establecimiento
                        ProductosComidas[i]  = jsonObject.getString("Nombre");
                        //Toast.makeText(getApplicationContext(),"Comida: "+ProductosComidas[i],Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                if(ProductosComidas != null){
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(misproductos.this,android.R.layout.simple_dropdown_item_1line,ProductosComidas);
                    NombreProductoComida.setAdapter(adapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "No existen productos de comida", Toast.LENGTH_SHORT).show();
                ProductosComidas = new String[1];
            }
        });

        RequestQueue cola = Volley.newRequestQueue(misproductos.this);
        cola.add(jsonArrayRequest);
    }

    private void CargarNombreProductosBebidas(String URL) {
        //Toast.makeText(getApplicationContext(),"comidas: "+ProductosComidas,Toast.LENGTH_SHORT).show();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                TotalRegistrosBebidas = response.length();
                //Toast.makeText(getApplicationContext(),"Total de Registros: "+response.length(),Toast.LENGTH_SHORT).show();
                ProductosBebidas = new String[TotalRegistrosBebidas];

                //Toast.makeText(getApplicationContext(),TotalRegistros,Toast.LENGTH_SHORT).show();
                for (int i = 0; i < TotalRegistrosBebidas; i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        //Se almacena cada dato del establecimiento
                        ProductosBebidas[i]  = jsonObject.getString("Nombre");

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                if(ProductosBebidas != null){
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(misproductos.this,android.R.layout.simple_dropdown_item_1line,ProductosBebidas);
                    NombreProductoBebida.setAdapter(adapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "No existen productos de bebidas", Toast.LENGTH_SHORT).show();
                ProductosBebidas = new String[1];
            }
        });

        RequestQueue cola = Volley.newRequestQueue(misproductos.this);
        cola.add(jsonArrayRequest);
    }

    private void CargarNombreProductosPostres(String URL) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                TotalRegistrosPostres = response.length();
                //Toast.makeText(getApplicationContext(),"Total de Registros: "+response.length(),Toast.LENGTH_SHORT).show();
                ProductosPostres = new String[TotalRegistrosPostres];

                //Toast.makeText(getApplicationContext(),TotalRegistros,Toast.LENGTH_SHORT).show();
                for (int i = 0; i < TotalRegistrosPostres; i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        //Se almacena cada dato del establecimiento
                        ProductosPostres[i]  = jsonObject.getString("Nombre");

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                if(ProductosPostres != null){
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(misproductos.this,android.R.layout.simple_dropdown_item_1line,ProductosPostres);
                    NombreProductoPostre.setAdapter(adapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "No existen productos de postres", Toast.LENGTH_SHORT).show();
                ProductosPostres = new String[1];
            }
        });

        RequestQueue cola = Volley.newRequestQueue(misproductos.this);
        cola.add(jsonArrayRequest);
    }

    private void CargarNombreProductosBotanas(String URL) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                TotalRegistrosBotanas = response.length();
                //Toast.makeText(getApplicationContext(),"Total de Registros: "+response.length(),Toast.LENGTH_SHORT).show();
                ProductosBotanas = new String[TotalRegistrosBotanas];

                //Toast.makeText(getApplicationContext(),TotalRegistros,Toast.LENGTH_SHORT).show();
                for (int i = 0; i < TotalRegistrosBotanas; i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        //Se almacena cada dato del establecimiento
                        ProductosBotanas[i]  = jsonObject.getString("Nombre");

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                if(ProductosBotanas != null){
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(misproductos.this,android.R.layout.simple_dropdown_item_1line,ProductosBotanas);
                    NombreProductoBotana.setAdapter(adapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "No existen productos de botanas", Toast.LENGTH_SHORT).show();
                ProductosBotanas = new String[1];
            }
        });

        RequestQueue cola = Volley.newRequestQueue(misproductos.this);
        cola.add(jsonArrayRequest);
    }


}
