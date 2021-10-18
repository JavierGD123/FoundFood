package com.example.foundfood;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class actualizarmiproducto extends AppCompatActivity {

    private String nombreproducto,costoproducto,idproducto,iduser,tipo,img;
    private EditText Nombre,Costo;
    private ImageView ImagenPro;

    //Variables para la camara
    private static final String CARPETA_PRINCIPAL = "misImagenesApp/";//Directorio principal
    private static final String CARPETA_IMAGEN = "imagenes";//Carpeta donde se guardan las fotos
    private static final String DIRECTORIO_IMAGEN = CARPETA_PRINCIPAL+CARPETA_IMAGEN;//Ruta carpeta de direcciones
    private String path;//almacena la ruta de la imagen

    private File fileImagen;
    private Bitmap bitmap;
    private ProgressDialog progreso;

    private ImageView imgFoto;

    private RequestQueue request;
    private StringRequest stringReques;

    private static final int COD_SELECCIONA = 10;
    private static final int COD_FOTO = 20;
    /////////////////////////////////////

    private String DominioURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_mi_producto);

        VariablesGlobales Url = new VariablesGlobales();
        DominioURL = Url.getURL();

        Nombre = (EditText)findViewById(R.id.txtNombreProducto);
        Costo = (EditText)findViewById(R.id.txtCostoProducto);

        imgFoto =(ImageView)findViewById(R.id.imgFotoProDuct);

        request = Volley.newRequestQueue(actualizarmiproducto.this);

        ObtenemosId();

    }

    private void ObtenemosId(){
        //Se obtienen los datos mandados por la actividad misproductos
        Intent id = this.getIntent();
        nombreproducto = id.getStringExtra("nombre");
        costoproducto = id.getStringExtra("costo");
        idproducto = id.getStringExtra("idpro");
        iduser = id.getStringExtra("iduser");
        tipo = id.getStringExtra("tipo");
        img  = id.getStringExtra("img");

        Nombre.setText(nombreproducto);
        Costo.setText(costoproducto);

        if(tipo.equals("Comida")){
            CargarImagen(DominioURL+"/imgComidas/"+img+".jpg");
        }else if(tipo.equals("Bebida")){
            CargarImagen(DominioURL+"/imgBebidas/"+img+".jpg");
        }else if(tipo.equals("Postre")){
            CargarImagen(DominioURL+"/imgPostres/"+img+".jpg");
        }else if(tipo.equals("Botana")){
            CargarImagen(DominioURL+"/imgBotanas/"+img+".jpg");
        }

    }

    public void GuardadCambios(View view){
        Validacion();
    }

    private void Validacion(){

        String NombrePro = Nombre.getText().toString();
        String CostoPro = Costo.getText().toString();
        String imagen = getStringImagen(bitmap);

        if((NombrePro.equals("") && CostoPro.equals(""))){
            Toast.makeText(getApplicationContext(),"Favor de agregar el nombre y costo de su producto "+tipo,Toast.LENGTH_SHORT).show();
        }else if(NombrePro.equals("")){
            Toast.makeText(getApplicationContext(),"Favor de agregar el Nombre de su producto "+tipo,Toast.LENGTH_SHORT).show();
            Nombre.requestFocus();
        }else if(CostoPro.equals("")){
            Toast.makeText(getApplicationContext(),"Favor de agregar el Costo de su producto "+tipo,Toast.LENGTH_SHORT).show();
            Costo.requestFocus();
        }else if(bitmap == null){
            Toast.makeText(getApplicationContext(),"No ha ingresado la imagen del producto "+tipo,Toast.LENGTH_SHORT).show();
        }else{
           ActualizarDatos(idproducto,iduser,NombrePro,CostoPro,imagen,tipo);
            Nombre.setText("");
            Costo.setText("");
            bitmap = null;
        }
    }

    public void Misproductos(){
        //Toast.makeText(getApplicationContext(),"Datos del prodcto llamado "+idproducto+iduser,Toast.LENGTH_SHORT).show();

        Intent mapa = new Intent(this, misproductos.class);
        mapa.putExtra("iduser",iduser);
        startActivity(mapa);
        finish();
    }

    public void  TomarFoto(View view){
        MostrarDialogoOpciones();
    }

    private void MostrarDialogoOpciones(){

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        //intent.setType("DCIM/Camera/");
        startActivityForResult(intent.createChooser(intent,"Seleccione"),COD_SELECCIONA);

        /*final CharSequence[] opciones ={"Tomar Foto","Elegir de Galeria","Canselar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Elije una Opcion");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar Foto")){
                    //Llamado almetodo para activar camara
                    // abrirCamara();
                    //Toast.makeText(getApplicationContext(),"Cargar Camara",Toast.LENGTH_SHORT).show();
                }else {
                    if (opciones[i].equals("Elegir de Galeria")){
                        //Llamado almetodo para activar camara
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        //intent.setType("DCIM/Camera/");
                        startActivityForResult(intent.createChooser(intent,"Seleccione"),COD_SELECCIONA);
                    }else{
                        dialogInterface.dismiss();
                    }
                }
            }

        });
        builder.show();*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case COD_SELECCIONA:
                Uri miPath = data.getData();
                imgFoto.setImageURI(miPath);

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), miPath);
                    imgFoto.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case COD_FOTO:
                MediaScannerConnection.scanFile(actualizarmiproducto.this , new String[]{path}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("Path",""+path);
                            }
                        });
                bitmap = BitmapFactory.decodeFile(path);
                imgFoto.setImageBitmap(bitmap);
                break;
        }
    }

    public void Regresar(View view){
        //ObtenemosId();

        Intent menu = new Intent(this, misproductos.class);
        menu.putExtra("iduser",iduser);
        startActivity(menu);
        finish();
    }

    //CONVIERTE LA IMAGEN EN UN STRING
    public String getStringImagen(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void ActualizarDatos(String idPro, String iduser, String nombre, String costo, String img, String tipo){

        final ProgressDialog loading = ProgressDialog.show(this, "Cargando sus datos ...", "Espere por favor");

        //Toast.makeText(getApplicationContext(),"idPro: "+idPro+"IDUser: "+iduser+"Nombre: "+nombre+"Csto: "+costo+"Img: "+img +"Tipo: "+tipo,Toast.LENGTH_SHORT).show();

        Response.Listener<String> respuesta = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonRespuesta = null;
                try {
                    jsonRespuesta = new JSONObject(response);
                    boolean ok = jsonRespuesta.getBoolean("success");
                    if (ok == true){
                        loading.dismiss();
                        Misproductos();
                        //Toast.makeText(getApplicationContext(),"Sean actualizado los datos cerrectamente",Toast.LENGTH_SHORT).show();
                    }else{
                        loading.dismiss();
                        //Toast.makeText(getApplicationContext(),"Tipo "+actualizo,Toast.LENGTH_SHORT).show();
                        //validaciodeusercontradb(User,username);
                        AlertDialog.Builder alerta = new AlertDialog.Builder(actualizarmiproducto.this);
                        alerta.setMessage("No se han podido actualizar los datos").setNegativeButton("Reintentar", null).create().show();
                    }
                } catch (JSONException e) {
                    e.getMessage();
                }
            }
        };

        ActualizarMisProductosRequest r = new ActualizarMisProductosRequest(idPro.trim(),iduser.trim(),nombre.trim(),costo.trim(),img.trim(),tipo.trim(),DominioURL,respuesta);
        RequestQueue cola = Volley.newRequestQueue(actualizarmiproducto.this);
        cola.add(r);
    }

    private void CargarImagen(String URL){
        RequestQueue request = Volley.newRequestQueue(this);

        URL = URL.replace(" ","%20");

        ImageRequest imageRequest  = new ImageRequest(URL,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        imgFoto.setImageBitmap(response);
                        bitmap = response;
                    }
                }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                imgFoto.setImageResource(R.drawable.vistapreviainfo);
                //Toast.makeText(getApplicationContext(),"El establecimiento "+Nombre+" no a publicado su logo",Toast.LENGTH_SHORT).show();
            }
        });

        request.add(imageRequest);
    }
}
