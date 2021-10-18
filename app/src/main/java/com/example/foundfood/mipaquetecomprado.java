package com.example.foundfood;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class mipaquetecomprado extends AppCompatActivity{
    private String paquete ,idusuario;
    private ImageView Paquete;
    private String Opciones[];
    TextView FechaExpiracion,FechaActual;
    private LinearLayout ComprarPaquete;


    private String Fecha_Expiracion,Fecha_Compra, Fecha_Actual;

    private String Sencillo;// = "https://www.mercadopago.com.mx/checkout/v1/redirect?pref_id=39654980-1686e956-d60d-47ce-9ae0-810f9f99dc39";
    private String Avanzado;// = "https://www.mercadopago.com.mx/checkout/v1/redirect?pref_id=39654980-059839f2-e7ec-475f-af03-75111d0eb273";
    private String Premium;// = "https://www.mercadopago.com.mx/checkout/v1/redirect?pref_id=39654980-c7b5360e-e00a-40b6-95d6-d2fc6b8810ff";

    private Button ComprarPaqueteSencillo,ComprarPaquetePremium,ComprarPaqueteAvanzado;
    private Button PagarPaqueteSencillo, PagarPaqueteAvanzado,PagarPaquetePremium;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mipaquetecomprado);

        VariablesGlobales Url = new VariablesGlobales();
        Sencillo = Url.getSencillo();
        Avanzado = Url.getAvanzado();
        Premium = Url.getPremium();

        obtenerid();

        Paquete = findViewById(R.id.imgpaquetescomprados);
        FechaExpiracion = findViewById(R.id.txtExipiraciondepaquete);
        FechaActual = findViewById(R.id.txtFechaActual);
        ComprarPaquete = findViewById(R.id.layoutComprarPaquetes);

        Date date = new Date();

        SimpleDateFormat d = new SimpleDateFormat("dd/MM/yyyy");
        Fecha_Actual = d.format(date);

        if(Fecha_Compra == "null" && Fecha_Expiracion == "null"){
            Fecha_Compra = "*No hay fecha de compra*";
            Fecha_Expiracion = "*No hay fecha de expiración*";
        }

        if(Fecha_Actual.equals(Fecha_Expiracion)){
            if(paquete.equals("prueba")){
                FechaExpiracion.setText("Tú paquete es de prueba, solo puedes agregar 1 producto por categoria y una promoción");
            }else{
                FechaExpiracion.setText("Tú paquete "+paquete+" ya expiró, solo puedes agregar 1 producto por categoria");
            }

            FechaActual.setText("Fecha Actual: "+Fecha_Actual.trim()+"\n"+
                    "Fecha de compra del paquete "+Fecha_Compra+" \n"+"Fecha de expiración del paquete "+Fecha_Expiracion);
        }else{

            FechaActual.setText("Fecha Actual: "+Fecha_Actual+"\n " +
                    "Fecha de compra del paquete "+Fecha_Compra+" \n"+"Fecha de expiración del paquete "+Fecha_Expiracion);
        }

        llenaropciones();
    }

    private void obtenerid(){
        Intent iduser = this.getIntent();
        idusuario = iduser.getStringExtra("iduser");
        paquete = iduser.getStringExtra("paquete");
        Fecha_Compra = iduser.getStringExtra("fechacompra");
        Fecha_Expiracion = iduser.getStringExtra("fechaexpira");

        //Toast.makeText(getApplicationContext(),idusuario,Toast.LENGTH_SHORT).show();
    }

    public void Regresar(View view){
        Intent perfil = new Intent(this, perfilestablecimiento.class);
        perfil.putExtra("idusuario",idusuario);
        startActivity(perfil);
        finish();
    }

    private void llenaropciones(){

        ////////////////COMPRAS//////////////////////////
        ComprarPaqueteSencillo = new Button(this);
        //ComprarPaqueteSencillo.setPadding(0,0,0,0); //Elimina padding
        //Crea contenedor con peso de 1 para cada elemento.
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(370,200);
        ComprarPaqueteSencillo.setLayoutParams(params);
        ComprarPaqueteSencillo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comprarpaque(Sencillo);
            }
        });

        ComprarPaqueteSencillo.setBackgroundResource(R.drawable.comprarsencillo);

        //////////////////////////////////////////
        ComprarPaquetePremium = new Button(this);
        //ComprarPaquetePremium.setPadding(0,0,0,0); //Elimina padding
        //Crea contenedor con peso de 1 para cada elemento.
        LinearLayout.LayoutParams paramss = new LinearLayout.LayoutParams(370,200);
        ComprarPaquetePremium.setLayoutParams(paramss);

        ComprarPaquetePremium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comprarpaque(Premium);
            }
        });

        ComprarPaquetePremium.setBackgroundResource(R.drawable.comprarpremium);

        ///////////////////////////////////////////////
        ComprarPaqueteAvanzado = new Button(this);
        //ComprarPaqueteAvanzado.setPadding(0,0,0,0); //Elimina padding
        //Crea contenedor con peso de 1 para cada elemento.
        LinearLayout.LayoutParams paramsss = new LinearLayout.LayoutParams(370,200);
        ComprarPaqueteAvanzado.setLayoutParams(paramsss);

        ComprarPaqueteAvanzado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comprarpaque(Avanzado);
            }
        });

        ComprarPaqueteAvanzado.setBackgroundResource(R.drawable.compraravanzado);

        //////////////////////PAGOS//////////////////////////////////////////
        PagarPaqueteSencillo = new Button(this);
        //ComprarPaqueteSencillo.setPadding(0,0,0,0); //Elimina padding
        //Crea contenedor con peso de 1 para cada elemento.
        LinearLayout.LayoutParams PagarS  = new LinearLayout.LayoutParams(370,200);
        PagarPaqueteSencillo.setLayoutParams(PagarS);
        PagarPaqueteSencillo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comprarpaque(Sencillo);
            }
        });

        PagarPaqueteSencillo.setBackgroundResource(R.drawable.pagarpaquetesencillo);

        //////////////////////////////////////////
        PagarPaquetePremium = new Button(this);
        //ComprarPaquetePremium.setPadding(0,0,0,0); //Elimina padding
        //Crea contenedor con peso de 1 para cada elemento.
        LinearLayout.LayoutParams pagarP = new LinearLayout.LayoutParams(370,200);
        PagarPaquetePremium.setLayoutParams(pagarP);

        PagarPaquetePremium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comprarpaque(Premium);
            }
        });

        PagarPaquetePremium.setBackgroundResource(R.drawable.pagarpaquetepremium);

        ///////////////////////////////////////////////
        PagarPaqueteAvanzado = new Button(this);
        //ComprarPaqueteAvanzado.setPadding(0,0,0,0); //Elimina padding
        //Crea contenedor con peso de 1 para cada elemento.
        LinearLayout.LayoutParams pagarA = new LinearLayout.LayoutParams(370,200);
        PagarPaqueteAvanzado.setLayoutParams(pagarA);

        PagarPaqueteAvanzado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comprarpaque(Avanzado);
            }
        });

        PagarPaqueteAvanzado.setBackgroundResource(R.drawable.pagarpaqueteavanzado);


        if(paquete.equals("sencillo")){
            Paquete.setImageResource(R.drawable.paquetesencillocomprado);
            ComprarPaquete.addView(PagarPaqueteSencillo);
            ComprarPaquete.addView(ComprarPaqueteAvanzado);
            ComprarPaquete.addView(ComprarPaquetePremium);

        }

        if(paquete.equals("premium")){
            Paquete.setImageResource(R.drawable.paquetepremiumcomprado);
            ComprarPaquete.addView(PagarPaquetePremium);
            ComprarPaquete.addView(ComprarPaqueteSencillo);
            ComprarPaquete.addView(ComprarPaqueteAvanzado);
        }

        if(paquete.equals("avanzado")){
            Paquete.setImageResource(R.drawable.paqueteavanzadocomprado);
            ComprarPaquete.addView(PagarPaqueteAvanzado);
            ComprarPaquete.addView(ComprarPaqueteSencillo);
            ComprarPaquete.addView(ComprarPaquetePremium);
        }

        if(paquete.equals("prueba")){
            //Paquete.setImageResource(R.drawable.paqueteavanzadocomprado);
            ComprarPaquete.addView(ComprarPaqueteSencillo);
            ComprarPaquete.addView(ComprarPaquetePremium);
            ComprarPaquete.addView(ComprarPaqueteAvanzado);
        }


    }

    public void InfoPaquetes(View view){
        AlertDialog.Builder alerta = new AlertDialog.Builder(mipaquetecomprado.this);
        alerta.setMessage("Si compra otro paquete cuando aún no se le vence el que tiene, se le dará de baja y se le pondrá el nuevo paquete comprado.").setNegativeButton("Ok", null).create().show();

    }


    private void comprarpaque(String URL){
        Uri url = Uri.parse(URL);
        Intent coordenadas = new Intent(Intent.ACTION_VIEW, url);
        startActivity(coordenadas);
    }

    private void MostrarDialogoOpciones(){
        final CharSequence[] opciones ={"Continuar","Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Elija una Opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Continuar")){
                    comprarpaque(Sencillo);
                }else {
                    dialogInterface.dismiss();
                }
            }

        });
        builder.show();
    }
}
