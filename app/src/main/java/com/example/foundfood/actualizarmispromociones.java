package com.example.foundfood;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class actualizarmispromociones extends AppCompatActivity {

    private String nombreproducto,dia,idproducto,iduser,img;
    private EditText NombreProducto;
    private ImageView ImagenPro;

    private RadioButton Lunes,Martes,Miercoles,Jueves,Viernes,Sabado,Domingo;

    private Bitmap bitmappromo;

    private ImageView imgFotoPromo;

    private static final int COD_SELECCIONA = 10;
    private static final int COD_FOTO = 20;

    private  int tipofoto;

    private String DominioURL;// = "https://fofo111.000webhostapp.com/app";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizarmispromociones);

        VariablesGlobales Url = new VariablesGlobales();
        DominioURL = Url.getURL();

        NombreProducto = findViewById(R.id.txtNombrePromocionactualizar);

        Lunes = (RadioButton)findViewById(R.id.rdbLunes);
        Martes = (RadioButton)findViewById(R.id.rdbMartes);
        Miercoles = (RadioButton)findViewById(R.id.rdbMiercoles);
        Jueves = (RadioButton)findViewById(R.id.rdbJueves);
        Viernes = (RadioButton)findViewById(R.id.rdbViernes);
        Sabado = (RadioButton)findViewById(R.id.rdbSabado);
        Domingo = (RadioButton)findViewById(R.id.rdbDomingo);

        imgFotoPromo = findViewById(R.id.imgFotoProDuct);

        ObtenemosId();
    }

    public void Regresar(View view){
        //ObtenemosId();

        Intent menu = new Intent(this, mispromociones.class);
        menu.putExtra("iduser",iduser);
        startActivity(menu);
        finish();
    }

    public void  TOMARFOTOPROMOCION(View view){
        tipofoto = 3;
        MostrarDialogoOpciones();
    }

    public void GuardadCambios(View view){
        Validacion();
    }

    private void Validacion(){

        String NombrePro = NombreProducto.getText().toString();

        if(NombrePro.equals("")){
            Toast.makeText(getApplicationContext(), "Favor de agregar el nombre del producto " , Toast.LENGTH_SHORT).show();
        }else if(bitmappromo == null){
            Toast.makeText(getApplicationContext(),"No ha ingresado la imagen de la promoción ",Toast.LENGTH_SHORT).show();
        }else{
            AGREGAR_PROMOCION(NombrePro);
            //ActualizarDatos(idproducto,iduser,NombrePro,CostoPro,imagen,tipo);

            NombreProducto.setText("");
            bitmappromo = null;
        }
    }

    public void Mispromociones(){
        //Toast.makeText(getApplicationContext(),"Datos del prodcto llamado "+idproducto+iduser,Toast.LENGTH_SHORT).show();

        Intent promo = new Intent(this, mispromociones.class);
        promo.putExtra("iduser",iduser);
        startActivity(promo);
        finish();
    }

    private void ObtenemosId(){
        //Se obtienen los datos mandados por la actividad misproductos
        Intent id = this.getIntent();
        nombreproducto = id.getStringExtra("nombre");
        dia = id.getStringExtra("dia");
        idproducto = id.getStringExtra("idpro");
        iduser = id.getStringExtra("iduser");
        img  = id.getStringExtra("img");

        NombreProducto.setText(nombreproducto);
        CargarImagen(DominioURL+"/imgPromociones/"+img+".jpg");

        if(dia.equals("lunes")){
            Lunes.setChecked(true);
        }else if(dia.equals("martes")){
            Martes.setChecked(true);
        }else if(dia.equals("miercoles")){
            Miercoles.setChecked(true);
        }else if(dia.equals("jueves")){
            Jueves.setChecked(true);
        }else if(dia.equals("viernes")){
            Viernes.setChecked(true);
        }else if(dia.equals("sabado")){
            Sabado.setChecked(true);
        }else if(dia.equals("domingo")){
            Domingo.setChecked(true);
        }

    }

    public void OnclicLunes(View view){
        dia = "lunes";
    }

    public void OnclicMartes(View view){
        dia = "martes";
    }

    public void OnclicMiercoles(View view){
        dia = "miercoles";
    }

    public void OnclicJueves(View view){
        dia = "jueves";
    }

    public void OnclicViernes(View view){
        dia = "viernes";
    }

    public void OnclicSabado(View view){
        dia = "sabado";
        //Toast.makeText(getApplicationContext(),"Dia: "+dia,Toast.LENGTH_SHORT).show();
    }

    public void OnclicDomingo(View view){
        dia = "domingo";
    }

    public void AGREGAR_PROMOCION(String NamePromo){
        //ObtenemosId();

        SubriPromocion(idproducto,iduser,NamePromo,dia);

        /*if(Lunes.isChecked()){
            SubriPromocion(idproducto,iduser,NamePromo,dia);
        }else if(Martes.isChecked()){
            SubriPromocion(idproducto,iduser,NamePromo,dia);
        }else if(Miercoles.isChecked()){
            SubriPromocion(idproducto,iduser,NamePromo,dia);
        }else if (Jueves.isChecked()){
            SubriPromocion(idproducto,iduser,NamePromo,dia);
        }else if (Viernes.isChecked()){
            //Toast.makeText(getApplicationContext(),"Iduser: "+idusuario+", NombreESTA: "+nombre+", Dia: Viernes"+", Nombre Promo"+NamePromo,Toast.LENGTH_SHORT).show();
            SubriPromocion(idproducto,iduser,NamePromo,dia);
        }else if(Sabado.isChecked()){
            Toast.makeText(getApplicationContext(),"Dia: Sabado",Toast.LENGTH_SHORT).show();
            SubriPromocion(idproducto,iduser,NamePromo,dia);
        }else if(Domingo.isChecked()){
            SubriPromocion(idproducto,iduser,NamePromo,dia);
        }else {
            Toast.makeText(getApplicationContext(),"No a elegido el dia para la promoción",Toast.LENGTH_SHORT).show();
        }*/
    }

    private void SubriPromocion(String idproducto,String idusuario,String NombrePromo,String Dia){

        String imagen = getStringImagen(bitmappromo);

        final ProgressDialog loading = ProgressDialog.show(this, "Subiendo promoción...", "Espere por favor");

        Response.Listener<String> respuesta = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject jsonRespuesta = null;

                try {
                    jsonRespuesta = new JSONObject(response);

                    mandarmensagepromocion(jsonRespuesta.getBoolean("success"));
                    loading.dismiss();

                    //mandarmensage(, Tipo);

                } catch (JSONException e) {
                    loading.dismiss();
                    e.getMessage();
                }
            }
        };

        ActualizarMisPromocionesRequest r = new ActualizarMisPromocionesRequest(idproducto.trim(),idusuario.trim(),NombrePromo.trim(),Dia.trim(),imagen,DominioURL,respuesta);
        RequestQueue cola = Volley.newRequestQueue(actualizarmispromociones.this);
        cola.add(r);
    }

    private void mandarmensagepromocion(boolean msm){

        if(msm == true){
            Toast.makeText(getApplicationContext(),"Se ha actualizado su promoción",Toast.LENGTH_SHORT).show();
            NombreProducto.setText("");
            bitmappromo = null;
            imgFotoPromo.setImageResource(R.drawable.vistaprevia);
            Mispromociones();
        }else{
            AlertDialog.Builder alerta = new AlertDialog.Builder(actualizarmispromociones.this);
            alerta.setMessage("No se realizó la actualización de su promoción").setNegativeButton("Ok", null).create().show();
        }
    }

    private void MostrarDialogoOpciones(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        //intent.setType("DCIM/Camera/");
        startActivityForResult(intent.createChooser(intent,"Seleccione "),COD_SELECCIONA);
        /*
        final CharSequence[] opciones ={"Tomar Foto","Elegir de Galeria","Canselar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Elije una Opcion");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar Foto")){
                    //Llamado almetodo para activar camara
                    //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //startActivityForResult(intent,COD_FOTO);
                    //Toast.makeText(getApplicationContext(),"Cargar Camara",Toast.LENGTH_SHORT).show();
                }else {
                    if (opciones[i].equals("Elegir de Galeria")){
                        //Llamado almetodo para activar camara
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        //intent.setType("DCIM/Camera/");
                        startActivityForResult(intent.createChooser(intent,"Seleccione "),COD_SELECCIONA);
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

                    imgFotoPromo.setImageURI(miPath);

                try {
                        bitmappromo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), miPath);
                        imgFotoPromo.setImageBitmap(bitmappromo);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case COD_FOTO:
                bitmappromo = (Bitmap) data.getExtras().get("data");
                imgFotoPromo.setImageBitmap(bitmappromo);

                /*MediaScannerConnection.scanFile(perfilestablecimiento.this , new String[]{path}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("Path",""+path);
                            }
                        });*/

                //bitmaplogo = BitmapFactory.decodeFile(path);
                //bitmaplogo = BitmapFactory.decodeFile(path);
                //imgFoto.setImageBitmap(bitmap);
                break;
        }

    }


    //CONVIERTE LA IMAGEN EN UN STRING
    private String getStringImagen(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void CargarImagen(String URL){
        RequestQueue request = Volley.newRequestQueue(this);

        URL = URL.replace(" ","%20");

        ImageRequest imageRequest  = new ImageRequest(URL,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        imgFotoPromo.setImageBitmap(response);
                        bitmappromo = response;
                    }
                }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                imgFotoPromo.setImageResource(R.drawable.vistapreviainfo);
                //Toast.makeText(getApplicationContext(),"El establecimiento "+Nombre+" no a publicado su logo",Toast.LENGTH_SHORT).show();
            }
        });

        request.add(imageRequest);
    }
}
