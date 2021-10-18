package com.example.foundfood;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.text.SimpleDateFormat;
import java.util.Date;

public class perfilestablecimiento extends AppCompatActivity {

    EditText NombreEstablecimiento,Usuario,Email,Contrasena,Telefono,NombreProducto,CostoProducto,MesasDisponibles,NombrePromocion,EmailNombre;
    //Button CesarrSesionEsta,Actualizar,TomarFoto;

    private static final String STRING_PREFERENCES_ID_Esta="IDEsta";
    private static final String PREFERENCES_ESTADO_ID_Esta= "iduser";

    private String nombre = "";
    private String usuario = "";
    private String telefono = "";
    private String contra = "";
    private String img = "";
    private String email = "", EmailCompleto = "", emailnombre;
    private String Estadoesta = "";
    private String Mesasdispo = "";
    private RadioButton CambiarEmail;
    private boolean isActivateRadioButton;

    private Spinner Correos;

    private String idusuario = "";

    private String Fecha_Expiracion,Fecha_Compra,Fecha_Actual,PagoPaquete = "",Fecha_Aviso;

    private RadioButton Comida,Bebida,Postre,Botana,EstatusLleno,EstatusDisponible;
    private RadioButton Lunes,Martes,Miercoles,Jueves,Viernes,Sabado,Domingo;

    private String Paquetecomprado;

    private int Horade = 1,Horahasta = 1;
    private TextView horade,horahasta,pmde,pmhasta,TuHorario;
    private RadioButton PMDE,AMDE,PMHASTA,AMHASTA,Cerrado,Abierto;
    private Button IncrementarDe,IncrementarHasta,DeCrementarDe,DeCrementarHasta;
    private String PmDE,PmHASTA,Disponibilidad,Horario,HorarioNuevo;

    private Boolean disponible;


    //Variables para la camara
    private static final String CARPETA_PRINCIPAL = "misImagenesApp/";//Directorio principal
    private static final String CARPETA_IMAGEN = "imagenes";//Carpeta donde se guardan las fotos
    private static final String DIRECTORIO_IMAGEN = CARPETA_PRINCIPAL+CARPETA_IMAGEN;//Ruta carpeta de direcciones
    private String path;//almacena la ruta de la imagen

    private File fileImagen;
    private Bitmap bitmaplogo,bitmappro,bitmappromo;
    private ProgressDialog progreso;

    private ImageView imgFoto,imgLogoEsta,imgFotoPromo;
    private String NombreProduct;
    private String CostoProduct;

    private RequestQueue request;
    private StringRequest stringReques;

    private static final int COD_SELECCIONA = 10;
    private static final int COD_FOTO = 20;

    private  int tipofoto;
    /////////////////////////////////////

    private String DominioURL;// = "https://fofo111.000webhostapp.com/app";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_establecimiento);

        VariablesGlobales Url = new VariablesGlobales();
        DominioURL = Url.getURL();

        NombreEstablecimiento = (EditText) findViewById(R.id.txtNombre);
        Usuario = (EditText) findViewById(R.id.txtUsuario);
        Email = (EditText) findViewById(R.id.txtEmail);
        EmailNombre = (EditText) findViewById(R.id.txtEmailNombreEsta);
        Contrasena = (EditText) findViewById(R.id.txtContrasena);
        Telefono = (EditText) findViewById(R.id.txtTelefono);
        NombreProducto = (EditText)findViewById(R.id.txtNombreProducto);
        CostoProducto = (EditText)findViewById(R.id.txtCostoProducto);
        MesasDisponibles = (EditText)findViewById(R.id.txtMesasDisponibles);
        NombrePromocion = (EditText)findViewById(R.id.txtCostoProduct);

        imgFoto = (ImageView)findViewById(R.id.imgFoto);
        imgLogoEsta = (ImageView)findViewById(R.id.imgLogoEstaPerfil);
        imgFotoPromo = findViewById(R.id.imgFotoPromo);

        Comida = (RadioButton)findViewById(R.id.rdbComida);
        Bebida = (RadioButton)findViewById(R.id.rdbBebida);
        Postre = (RadioButton)findViewById(R.id.rdbPostre);
        Botana = (RadioButton)findViewById(R.id.rdbBotana);
        EstatusLleno = (RadioButton)findViewById(R.id.rdbLleno);
        EstatusDisponible = (RadioButton)findViewById(R.id.rdbMesasDisponibles);

        Lunes = (RadioButton)findViewById(R.id.rdbLunes);
        Martes = (RadioButton)findViewById(R.id.rdbMartes);
        Miercoles = (RadioButton)findViewById(R.id.rdbMiercoles);
        Jueves = (RadioButton)findViewById(R.id.rdbJueves);
        Viernes = (RadioButton)findViewById(R.id.rdbViernes);
        Sabado = (RadioButton)findViewById(R.id.rdbSabado);
        Domingo = (RadioButton)findViewById(R.id.rdbDomingo);
        CambiarEmail = (RadioButton) findViewById(R.id.rbtCambiarCorreoEsta);

        horade = (TextView) findViewById(R.id.txtHorade);
        horahasta = (TextView) findViewById(R.id.txtHoraHasta);
        TuHorario = (TextView) findViewById(R.id.txtTuHorario);
        pmde = findViewById(R.id.txtPmde);
        pmhasta = findViewById(R.id.txtpmHasta);

        IncrementarDe = findViewById(R.id.btnIncrementarDe);
        DeCrementarDe = findViewById(R.id.btnDecrementarDe);
        IncrementarHasta = findViewById(R.id.btnIncrementarHasta);
        DeCrementarHasta = findViewById(R.id.btnDecrementarHasta);

        PMDE = findViewById(R.id.rdbpmde);
        AMDE = findViewById(R.id.rdbamde);
        PMHASTA = findViewById(R.id.rdbpmhasta);
        AMHASTA = findViewById(R.id.rdbamhasta);
        Cerrado = findViewById(R.id.rdbCerrado);
        Abierto = findViewById(R.id.rdbAbierto);

        Horade = Horahasta = 0;
        PmDE = PmHASTA = "";

        Correos = findViewById(R.id.OpcionesCorreosPerfilEsta);

        String[] correos = {"@gmail.com","@hotmail.com","@outlook.com","@yahoo.com","@icloud.com"};

        Correos.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, correos));

        Correos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id)
            {
                emailnombre = (String) adapterView.getItemAtPosition(pos);
                //Toast.makeText(adapterView.getContext(),(String) adapterView.getItemAtPosition(pos), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {    }
        });

        isActivateRadioButton = false; //Desactivado boton se mantener sesion
        EmailNombre.setEnabled(false);
        Correos.setEnabled(false);

        ObtenemosIdDeLogin();
        //ObtenerIDUsuario();



    }

    //Secion de botones
    public void CambiarEmail(View view){
        if(isActivateRadioButton == true){
            CambiarEmail.setChecked(false);
            EmailNombre.setEnabled(false);
            Correos.setEnabled(false);
        }else{
            EmailNombre.setEnabled(true);
            Correos.setEnabled(true);
        }
        isActivateRadioButton = CambiarEmail.isChecked();
    }

    public void OnclicCerrado(View view){
        desbilitarohabilitarhorario(false);
    }

    private void desbilitarohabilitarhorario(Boolean habilitar){
        if(habilitar == true){
            IncrementarDe.setEnabled(true);
            DeCrementarDe.setEnabled(true);

            IncrementarHasta.setEnabled(true);
            DeCrementarHasta.setEnabled(true);

            PMDE.setEnabled(true);
            AMDE.setEnabled(true);
            PMHASTA.setEnabled(true);
            AMHASTA.setEnabled(true);

            disponible = true;
        }else{
            IncrementarDe.setEnabled(false);
            DeCrementarDe.setEnabled(false);

            IncrementarHasta.setEnabled(false);
            DeCrementarHasta.setEnabled(false);

            PMDE.setEnabled(false);
            AMDE.setEnabled(false);
            PMHASTA.setEnabled(false);
            AMHASTA.setEnabled(false);

            disponible = false;
        }

    }

    public void OnclicAbierto(View view){
        desbilitarohabilitarhorario(true);
    }

    public void OnclicAmDe(View view){
        pmde.setText("am");
        PmDE = "am";

    }

    public void OnclicPmDe(View view){
        pmde.setText("pm");
        PmDE = "pm";
    }

    public void OnclicAmHasta(View view){
       pmhasta.setText("am");
        PmHASTA = "am";
    }

    public void OnclicPmHasta(View view){
        pmhasta.setText("pm");
        PmHASTA = "pm";
    }

    public void IncrementarHoraDe(View view){
        Horade++;
        if(Horade == 13){
            Horade=1;
        }
        String h = String.valueOf(Horade);
        horade.setText(h);
    }

    public void DecrementarHoraDe(View view){
        Horade--;
        if(Horade == 0){
            Horade=12;
        }
        String h = String.valueOf(Horade);
        horade.setText(h);
    }

    public void IncrementarHoraHasta(View view){
        Horahasta++;
        if( Horahasta == 13){
            Horahasta=1;
        }
        String h = String.valueOf(Horahasta);
        horahasta.setText(h);
    }

    public void DecrementarHoraHasta(View view){
        Horahasta--;
        if(Horahasta == 0){
            Horahasta=12;
        }

        String h = String.valueOf(Horahasta);
        horahasta.setText(h);

        /*if(Horade == 0 && PmDE == ""){
            Horario = +Horahasta+" "+PmHASTA;
        }
        Horario = Horade+" "+PmDE+" a "+Horahasta+" "+PmHASTA;
        TuHorario.setText("Horario: de "+Horario);*/
    }

    public void ACTUALIZAR(View view){
        Actualizar();
    }

    public void CERRAR_SESION(View view){
        loginvendedor.cambiarEstadoButton(perfilestablecimiento.this,false);
        Intent i=new Intent(perfilestablecimiento.this, ContinuarComo.class);
        startActivity(i);
        finish();
    }

    public void  TOMARFOTOLOGO(View view){
        tipofoto = 1;
        MostrarDialogoOpciones();

    }

    public void  TOMARFOTOPRODUCTO(View view){
        tipofoto = 2;
        MostrarDialogoOpciones();

    }

    public void  TOMARFOTOPROMOCION(View view){
        tipofoto = 3;
        MostrarDialogoOpciones();
    }

    public void AGREGAR_PROMOCION(View view){
            if(Lunes.isChecked()){
                validarpromocion("lunes");
            }else if(Martes.isChecked()){
                validarpromocion("martes");
            }else if(Miercoles.isChecked()){
                validarpromocion("miercoles");
            }else if (Jueves.isChecked()){
                validarpromocion("jueves");
            }else if (Viernes.isChecked()){
                validarpromocion("viernes");
            }else if(Sabado.isChecked()){
                validarpromocion("sabado");
            }else if(Domingo.isChecked()){
                validarpromocion("domingo");
            }else {
                Toast.makeText(getApplicationContext(),"No a elegido el dia para la promoción",Toast.LENGTH_SHORT).show();
            }
    }

    private void validarpromocion(String dia){
        String NamePromo = NombrePromocion.getText().toString();
        String imagen = getStringImagen(bitmappromo);

        if(NamePromo.equals("") &&  bitmappromo == null){
            NombrePromocion.requestFocus();
            Toast.makeText(getApplicationContext(),"No ha elegido el nombre ni la imagen de la promoción",Toast.LENGTH_SHORT).show();
        }else if(NamePromo.equals("")){
            NombrePromocion.requestFocus();
            Toast.makeText(getApplicationContext(),"No ha elegido el nombre la promoción",Toast.LENGTH_SHORT).show();
        }else if(bitmappromo == null){
            Toast.makeText(getApplicationContext(),"No ha elegido la imagen de la promoción",Toast.LENGTH_SHORT).show();
        }else{
            ObtenerIDUsuario();
            SubriPromocion(idusuario,nombre,NamePromo,dia, imagen,Paquetecomprado,PagoPaquete);
        }
    }

    private void SubriPromocion(String idusuario,String NombreEsta,String NombrePromo, String Dia, String imagen, String TipoPaquete,String Pago){

        final ProgressDialog loading = ProgressDialog.show(this, "Subiendo promoción...", "Espere por favor");

        Response.Listener<String> respuesta = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject jsonRespuesta = null;

                try {
                    jsonRespuesta = new JSONObject(response);

                    mandarmensagepromocion(jsonRespuesta.getBoolean("success"));
                    loading.dismiss();

                } catch (JSONException e) {
                    loading.dismiss();
                    e.getMessage();
                }
            }
        };

        PerfilEstablecimientoPromocionesRequest r = new PerfilEstablecimientoPromocionesRequest(idusuario.trim(),NombreEsta.trim(),NombrePromo.trim(),Dia.trim(),imagen,TipoPaquete,Pago,DominioURL,respuesta);
        RequestQueue cola = Volley.newRequestQueue(perfilestablecimiento.this);
        cola.add(r);
    }

    private void mandarmensagepromocion(boolean msm){
        //Toast.makeText(getApplicationContext(),"Respuesta del php: "+msm,Toast.LENGTH_SHORT).show();
        if(msm == true){
            Toast.makeText(getApplicationContext(),"Sea publicado su promoción",Toast.LENGTH_SHORT).show();
            NombrePromocion.setText("");
            bitmappromo = null;
            imgFotoPromo.setImageResource(R.drawable.vistaprevia);
        }else{
            AlertDialog.Builder alerta = new AlertDialog.Builder(perfilestablecimiento.this);
            alerta.setMessage("No se realizó el registro de su promoción, a superado el limite de registros favor de revisar su paquete").setNegativeButton("Ok", null).create().show();
        }
    }


    public void AGREGAR_PRODUCTO(View view){

        if(Comida.isChecked()){
           // TipoProducto("Comida");
            validarcamposproductos( NombreProducto.getText().toString(), CostoProducto.getText().toString(),getStringImagen(bitmappro),"Comida");
        }else if(Bebida.isChecked()){
           // TipoProducto("Bebida");
            validarcamposproductos( NombreProducto.getText().toString(), CostoProducto.getText().toString(), getStringImagen(bitmappro),"Bebida");
        }else if(Postre.isChecked()){
            //TipoProducto("Postre");
            validarcamposproductos( NombreProducto.getText().toString(), CostoProducto.getText().toString(), getStringImagen(bitmappro),"Postre");
        }else if (Botana.isChecked()){
           // TipoProducto("Botana");
            validarcamposproductos( NombreProducto.getText().toString(), CostoProducto.getText().toString(), getStringImagen(bitmappro),"Botana");

        }else{
            Toast.makeText(getApplicationContext(),"No a elegido algun Producto",Toast.LENGTH_SHORT).show();
        }

    }

    public void OnclicComida(View view){
        AvilitarCampos(" la Comida");
    }

    public void OnclicBebida(View view){
        AvilitarCampos(" la Bebida");
    }

    public void OnclicPostre(View view){
        AvilitarCampos("l Postre");
    }

    public void OnclicBotana(View view){
        AvilitarCampos(" la Botana");
    }

    public void OnclicEstatus(View view){
        MesasDisponibles.setEnabled(false);
    }

    public  void OnclicLugaresDisponibles(View view){
        MesasDisponibles.setEnabled(true);
    }

    public void MisProductos(View view){

        ObtenerIDUsuario();

        Intent misproductos = new Intent(perfilestablecimiento.this, com.example.foundfood.misproductos.class);
        misproductos.putExtra("iduser",idusuario);
       // Toast.makeText(getApplicationContext(),idusuario,Toast.LENGTH_SHORT).show();
        startActivity(misproductos);
        finish();
    }

    public void MisPromociones(View view){

        ObtenerIDUsuario();

        Intent mispromociones = new Intent(perfilestablecimiento.this,mispromociones.class);
        mispromociones.putExtra("iduser",idusuario);
        // Toast.makeText(getApplicationContext(),idusuario,Toast.LENGTH_SHORT).show();
        startActivity(mispromociones);
        finish();
    }

    public  void ActualizarEstatus(View view){
        String Mesas = MesasDisponibles.getText().toString();
        ObtenerIDUsuario();

        int Yatienehorario = 0;

        if(disponible == false){
            Disponibilidad = "Cerrado";
            Horario = "No hay horario";

            if(EstatusLleno.isChecked()){
                SubirEstatus("lleno","0",idusuario,Disponibilidad, Horario);
                // Toast.makeText(getApplicationContext(),"Se ha actualizado su estatus de disponibilidad a Lleno",Toast.LENGTH_SHORT).show();
            }else if(MesasDisponibles.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(),"Se requiere de un número de mesas para actualizar el estatus",Toast.LENGTH_SHORT).show();
            }else {
                SubirEstatus("disponible",Mesas,idusuario,Disponibilidad,Horario);
            }
        }else{
            HorarioNuevo = Horade+" "+PmDE+" a "+Horahasta+" "+PmHASTA;
            if(Horario != "" && Disponibilidad.equals("Abierto") && Horario.equals(HorarioNuevo)){
                Horade=1;
                PmDE = " ";
                Horahasta = 1;
                PmHASTA = " ";
                HorarioNuevo = Horario;
                Yatienehorario = 1;
            }

            if(Horade == 0 && Horahasta == 0 && PmDE.equals("") && PmHASTA.equals("")){
               Toast.makeText(getApplicationContext(),"Aun no haseleccionado un horario de aperura y cierre del establecimiento",Toast.LENGTH_SHORT).show();
            }else if(Horade == 0){
                Toast.makeText(getApplicationContext(),"Aun no ha seleccionado hora de apertura",Toast.LENGTH_SHORT).show();
            }else if(Horahasta == 0){
                Toast.makeText(getApplicationContext(),"Aun no ha seleccionado hora de cierre del establecimiento",Toast.LENGTH_SHORT).show();
            }else if(PmDE.equals("")){
                Toast.makeText(getApplicationContext(),"Aun no ha seleccionado formato de horario",Toast.LENGTH_SHORT).show();
            }else if(PmHASTA.equals("")){
                Toast.makeText(getApplicationContext(),"Aun no ha seleccionado formato de horario",Toast.LENGTH_SHORT).show();
            }else{
                Disponibilidad = "Abierto";
                if(Yatienehorario == 0){
                    HorarioNuevo = Horade+" "+PmDE+" a "+Horahasta+" "+PmHASTA;
                }

                if(EstatusLleno.isChecked()){
                    SubirEstatus("lleno","0",idusuario,Disponibilidad, HorarioNuevo);
                    // Toast.makeText(getApplicationContext(),"Se ha actualizado su estatus de disponibilidad a Lleno",Toast.LENGTH_SHORT).show();
                }else if(MesasDisponibles.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Se requiere de un número de mesas para actualizar el estatus",Toast.LENGTH_SHORT).show();
                }else {
                    SubirEstatus("disponible",Mesas,idusuario,Disponibilidad,HorarioNuevo);
                }
            }
        }
    }


    public void MIPAQUETE(View view){
        ObtenerIDUsuario();

        Intent perfil = new Intent(this, mipaquetecomprado.class);
        perfil.putExtra("iduser",idusuario);
        perfil.putExtra("paquete",Paquetecomprado);
        perfil.putExtra("fechacompra",Fecha_Compra);
        perfil.putExtra("fechaexpira",Fecha_Expiracion);

       // Toast.makeText(getApplicationContext(),Paquetecomprado+Fecha_Compra+Fecha_Expiracion,Toast.LENGTH_SHORT).show();
        startActivity(perfil);
        finish();
    }
    /////////////////////////////////////

    private void AvilitarCampos(String TipoNombre){
        NombreProducto.setEnabled(true);
        CostoProducto.setEnabled(true);

        //NombreProducto.setHint("Nombre de"+TipoNombre);
       // CostoProducto.setHint("Costo de"+TipoNombre);
    }

    private void validarcamposproductos( String NombrePro, String CostoPro,String imagen,String tipo){
        //Toast.makeText(getApplicationContext(),""+NombrePro+CostoPro+imagen+tipo,Toast.LENGTH_SHORT).show();

        if((NombrePro.equals("") && CostoPro.equals(""))){
            Toast.makeText(getApplicationContext(),"Favor de agregar el nombre y costo de su producto "+tipo,Toast.LENGTH_SHORT).show();
        }else if(NombrePro.equals("")){
            Toast.makeText(getApplicationContext(),"Favor de agregar el Nombre de su producto "+tipo,Toast.LENGTH_SHORT).show();
            NombreProducto.requestFocus();
        }else if(CostoPro.equals("")){
            Toast.makeText(getApplicationContext(),"Favor de agregar el Costo de su producto "+tipo,Toast.LENGTH_SHORT).show();
            CostoProducto.requestFocus();
        }else if(bitmappro == null){
            Toast.makeText(getApplicationContext(),"No ha ingresado la imagen del producto "+tipo,Toast.LENGTH_SHORT).show();
        }else{
            ObtenerIDUsuario();

            SubirProductos(tipo,NombrePro,CostoPro,imagen,idusuario, Paquetecomprado, PagoPaquete);

            //Toast.makeText(getApplicationContext(),"TipoProducto: "+tipo+" Nombre: "+NombrePro+" Costo: "+CostoPro+" Img"+imagen+" IdUser: "+idusuario,Toast.LENGTH_SHORT).show();

        }
    }

    private void mandarmensage(boolean msm, String tipo){

        if(msm == true){
            Toast.makeText(getApplicationContext(),"Sea publicado su producto",Toast.LENGTH_SHORT).show();
            NombreProducto.setText("");
            CostoProducto.setText("");
            bitmappro = null;
            imgFoto.setImageResource(R.drawable.vistaprevia);
        }else{
            AlertDialog.Builder alerta = new AlertDialog.Builder(perfilestablecimiento.this);
            alerta.setMessage("No se realizó el registro de su producto, puede que a aya llegado al limite de "+tipo+"s a registrar").setNegativeButton("Ok", null).create().show();
        }
    }

    private void SubirProductos(final String Tipo, String Nombre, String Costo, String Img, final String IDUSER, String paquete, String Pgado){

        final ProgressDialog loading = ProgressDialog.show(this, "Subiendo producto...", "Espere por favor");

        Response.Listener<String> respuesta = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    JSONObject jsonRespuesta = null;

                    try {
                        jsonRespuesta = new JSONObject(response);

                        mandarmensage(jsonRespuesta.getBoolean("success"), Tipo);
                        loading.dismiss();

                    } catch (JSONException e) {
                        loading.dismiss();
                        e.getMessage();
                    }
                }
            };

            PerfilEstablecimientoProductosRequest r = new PerfilEstablecimientoProductosRequest(Tipo.trim(),Nombre.trim(),Costo.trim(),Img,IDUSER.trim(),paquete.trim(), Pgado.trim(),DominioURL,respuesta);
            RequestQueue cola = Volley.newRequestQueue(perfilestablecimiento.this);
            cola.add(r);
    }

    private void SubirEstatus(String Tipo, final String MesasDisponibles, final String IDUSER, final String Disponibilidad, final String horario){

        final ProgressDialog loading = ProgressDialog.show(this, "Acrualizando estatus...", "Espere por favor");

        Response.Listener<String> respuesta = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonRespuesta = null;
                try {
                    jsonRespuesta = new JSONObject(response);
                    boolean ok = jsonRespuesta.getBoolean("success");
                    if (ok == true){
                        loading.dismiss();
                        TuHorario.setText("Horario: "+horario);
                        Toast.makeText(getApplicationContext(),"Se ha actualizado su estatus con "+MesasDisponibles+" mesas disponibles.\n" +
                                "Disponibilidad: "+Disponibilidad+", Horario: "+horario,Toast.LENGTH_SHORT).show();
                    }else{
                        loading.dismiss();
                        AlertDialog.Builder alerta = new AlertDialog.Builder(perfilestablecimiento.this);
                        alerta.setMessage("Error al agregar su producto").setNegativeButton("Reintentar", null).create().show();
                    }
                } catch (JSONException e) {
                    loading.dismiss();
                    e.getMessage();
                }
            }
        };

        PerfilEstablecimientoEstatusRequest r = new PerfilEstablecimientoEstatusRequest(Tipo.trim(),MesasDisponibles.trim(),IDUSER.trim(),Disponibilidad.trim(),horario,DominioURL,respuesta);
        RequestQueue cola = Volley.newRequestQueue(perfilestablecimiento.this);
        cola.add(r);
    }

    private void ObtenemosIdDeLogin(){
        //Se obtienen los datos mandados por la actividad LoginUusario
        Intent datosperfil = this.getIntent();
        String iduser = datosperfil.getStringExtra("idusuario");

        if(idusuario.equals(iduser)){
            MostrarDatos("si",idusuario);
        }else{
            MostrarDatos("si",iduser);
            GardarIdUsuario(iduser);
        }

    }

    private void Actualizar(){
        String Nombre  = NombreEstablecimiento.getText().toString();
        String User = Usuario.getText().toString();
        String Emaill = Email.getText().toString();
        String Contra = Contrasena.getText().toString();
        String Cel = Telefono.getText().toString();

        if(Nombre.isEmpty()){
            NombreEstablecimiento.requestFocus();
            Toast.makeText(getApplicationContext(),"No ha ingresado su nuevo nombre de establecimiento",Toast.LENGTH_LONG).show();
        }else if(Emaill.isEmpty()){
            Email.requestFocus();
            Toast.makeText(getApplicationContext(),"No ha ingresado su nuevo email",Toast.LENGTH_LONG).show();
        }else if(Contra.isEmpty()){
            Contrasena.requestFocus();
            Toast.makeText(getApplicationContext(),"No ha ingresado su Contraseña",Toast.LENGTH_LONG).show();
        }else if(User.isEmpty()){
            Usuario.requestFocus();
            Toast.makeText(getApplicationContext(),"No ha ingresado su Nombre de usuario",Toast.LENGTH_LONG).show();
        }else if(Cel.isEmpty()){
            Telefono.requestFocus();
            Toast.makeText(getApplicationContext(),"No ha ingresado su Número de contacto",Toast.LENGTH_LONG).show();
        }else{

            if(isActivateRadioButton == true){
                if(EmailNombre.getText().toString().isEmpty()){
                    EmailNombre.requestFocus();
                    Toast.makeText(getApplicationContext(),"No ha ingresado su nuevo nombre de email",Toast.LENGTH_LONG).show();
                }else{
                    Emaill = EmailNombre.getText().toString() + emailnombre;
                    ObtenerIDUsuario();
                    ActualizarDatos("no",idusuario,Nombre,User,Contra,Emaill,Cel);
                }
            }else{
                Emaill =  Email.getText().toString();
                ObtenerIDUsuario();
                ActualizarDatos("no",idusuario,Nombre,User,Contra,Emaill,Cel);
            }
        }
       // Toast.makeText(getApplicationContext(),Nombre,Toast.LENGTH_LONG).show();

    }

    private void ObtenerIDUsuario(){
        SharedPreferences sharef = getSharedPreferences(STRING_PREFERENCES_ID_Esta, Context.MODE_PRIVATE);
        idusuario = sharef.getString(PREFERENCES_ESTADO_ID_Esta,"No existe el ID");
    }

    private void GardarIdUsuario(String idusuario){
        SharedPreferences sharef = getSharedPreferences(STRING_PREFERENCES_ID_Esta, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharef.edit();

        editor.putString(PREFERENCES_ESTADO_ID_Esta,idusuario);
        editor.commit();
    }


    private void MostrarDatos(String cargo, final String iduser){

        final ProgressDialog loading = ProgressDialog.show(this, "Cargando sus datos ...", "Espere por favor");

        Response.Listener<String> respuesta = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonRespuesta = null;
                try {
                    jsonRespuesta = new JSONObject(response);
                    boolean ok = jsonRespuesta.getBoolean("success");
                    if (ok == true){
                        nombre = jsonRespuesta.getString("nombre");
                        usuario = jsonRespuesta.getString("usuario");
                        contra = jsonRespuesta.getString("contra");
                        email = jsonRespuesta.getString("email");
                        telefono = jsonRespuesta.getString("telefono");
                        img = jsonRespuesta.getString("img");
                        Estadoesta = jsonRespuesta.getString("estado");
                        Mesasdispo = jsonRespuesta.getString("mesasdispo");
                        Disponibilidad = jsonRespuesta.getString("disponible");
                        Horario = jsonRespuesta.getString("horario");

                        //Toast.makeText(getApplicationContext(),"Disponible: "+Disponibilidad,Toast.LENGTH_LONG).show();
                        //SECCIOÓN ESTATUS////
                        if(Estadoesta.equals("lleno")){
                            EstatusLleno.setChecked(true);
                            MesasDisponibles.setEnabled(false);//se mantiene desabilitado el campo
                        }else{
                            EstatusDisponible.setChecked(true);
                            MesasDisponibles.setText(Mesasdispo);
                        }

                        if(Disponibilidad.equals("Cerrado")){
                            Cerrado.setChecked(true);
                            TuHorario.setText("Horario: de "+Horario);
                            desbilitarohabilitarhorario(false);
                        }else{
                            Abierto.setChecked(true);
                            TuHorario.setText("Horario: de "+Horario);
                            desbilitarohabilitarhorario(true);
                        }


                        /////////////////////////
                        OptenerFechaActual(iduser);
                        //ObtenenciondePauqetecomptado(iduser,PagoPaquete);
                        CargarImagen(DominioURL+"/imgLogos/"+img+".jpg");
                        loading.dismiss();

                        //SECCIÓN ESTABLECIMIENTO//////////////
                        NombreEstablecimiento.setText(nombre);
                        Usuario.setText(usuario);
                        Email.setText(email);
                        Email.setEnabled(false);
                        Contrasena.setText(contra);
                        Telefono.setText(telefono);
                        //////////////////////////////////////////////////
                        //Toast.makeText(getApplicationContext(),nombre+usuario+telefono+contra,Toast.LENGTH_LONG).show();
                    }else{
                        loading.dismiss();
                        //validaciodeusercontradb(User,username);
                        AlertDialog.Builder alerta = new AlertDialog.Builder(perfilestablecimiento.this);
                        alerta.setMessage("Usuario o Contraseña incorrecta").setNegativeButton("Reintentar", null).create().show();
                    }
                } catch (JSONException e) {
                    e.getMessage();
                }
            }
        };

        PerfilEstablecimientoRequest r = new PerfilEstablecimientoRequest(cargo.trim(),iduser.trim(),nombre.trim(),usuario.trim(),contra.trim(),email.trim(),telefono.trim(),"",DominioURL,respuesta);
        RequestQueue cola = Volley.newRequestQueue(perfilestablecimiento.this);
        cola.add(r);
    }

    private void OptenerFechaActual(String iduser){
        Date date = new Date();

        SimpleDateFormat d = new SimpleDateFormat("dd/MM/yyyy");
        Fecha_Actual = d.format(date);

        if(Fecha_Compra == "null" && Fecha_Expiracion == "null"){
            Fecha_Expiracion = Fecha_Actual;
            Fecha_Compra = Fecha_Actual;
        }

        ObtenenciondePauqetecomptado(iduser,PagoPaquete);

    }

    private void ObtenenciondePauqetecomptado(final String iduser, String pago){
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
                        Fecha_Compra = jsonRespuesta.getString("fechacompra");
                        Fecha_Expiracion = jsonRespuesta.getString("fechaexpira");
                        Fecha_Aviso = jsonRespuesta.getString("fechaaviso");

                        //Toast.makeText(getApplicationContext(),"Fecha expiracion "+Fecha_Expiracion,Toast.LENGTH_SHORT).show();

                        if(Fecha_Actual.equals(Fecha_Aviso)){
                            AlertDialog.Builder alerta = new AlertDialog.Builder(perfilestablecimiento.this);
                            alerta.setMessage("Tu paquete se vence el día de mañana, favor de realizar su pago para seguir con los beneficios de su paquete.").setNegativeButton("Entendido", null).create().show();

                        }

                        if(Fecha_Actual.equals(Fecha_Expiracion)){
                            PagoPaquete = "no";
                            ObtenenciondePauqetecomptado(iduser,"no");
                        }

                        Toast.makeText(getApplicationContext(),"Paquete: "+Paquetecomprado+", Pago de paquete: "+PagoPaquete,Toast.LENGTH_SHORT).show();

                      // Toast.makeText(getApplicationContext(),Fecha_Expiracion,Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"Entraste en modo de prueba",Toast.LENGTH_LONG).show();
                        //Paquetecomprado = "prueba";
                        //Toast.makeText(getApplicationContext(),Paquetecomprado,Toast.LENGTH_LONG).show();
                        //loading.dismiss();
                        //validaciodeusercontradb(User,username);
                        ///AlertDialog.Builder alerta = new AlertDialog.Builder(perfilestablecimiento.this);
                        ///alerta.setMessage("Usuario o Contraseña incorrecta").setNegativeButton("Reintentar", null).create().show();
                    }
                } catch (JSONException e) {
                    e.getMessage();
                }
            }
        };
        //Toast.makeText(getApplicationContext(),iduser,Toast.LENGTH_LONG).show();
        PerfilEstablecimientoPaqueteRequest r = new PerfilEstablecimientoPaqueteRequest(iduser.trim(),pago.trim(),respuesta);
        RequestQueue cola = Volley.newRequestQueue(perfilestablecimiento.this);
        cola.add(r);
    }

    private void ActualizarDatos(String cargo,String iduser,String nombre,String usuario,String contra,String email,String telefono){

        String imagen = getStringImagen(bitmaplogo);

        final ProgressDialog loading = ProgressDialog.show(this, "Actualizando su información...", "Espere por favor");

        Response.Listener<String> respuesta = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonRespuesta = null;
                try {
                    jsonRespuesta = new JSONObject(response);
                    boolean ok = jsonRespuesta.getBoolean("success");

                    if (ok == true){
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),"Se han actualizado sus datos correctamente",Toast.LENGTH_SHORT).show();
                    }else{
                        //validaciodeusercontradb(User,username);
                        AlertDialog.Builder alerta = new AlertDialog.Builder(perfilestablecimiento.this);
                        alerta.setMessage("Usuario o Contraseña incorrecta").setNegativeButton("Reintentar", null).create().show();
                    }
                } catch (JSONException e) {
                    loading.dismiss();
                    e.getMessage();
                }
            }
        };

        PerfilEstablecimientoRequest r = new PerfilEstablecimientoRequest(cargo,iduser,nombre,usuario,contra,email,telefono,imagen,DominioURL,respuesta);
        RequestQueue cola = Volley.newRequestQueue(perfilestablecimiento.this);
        cola.add(r);
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

                if(tipofoto == 1){
                    imgLogoEsta.setImageURI(miPath);
                }else if(tipofoto == 2){
                    imgFoto.setImageURI(miPath);
                }else if (tipofoto == 3){
                    imgFotoPromo.setImageURI(miPath);
                }

                try {

                    if(tipofoto == 1){
                        bitmaplogo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), miPath);
                        imgLogoEsta.setImageBitmap(bitmaplogo);
                    }else if(tipofoto == 2){
                        bitmappro = MediaStore.Images.Media.getBitmap(this.getContentResolver(), miPath);
                        imgFoto.setImageBitmap(bitmappro);
                    }else if(tipofoto == 3){
                        bitmappromo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), miPath);
                        imgFotoPromo.setImageBitmap(bitmappromo);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case COD_FOTO:
                bitmaplogo = (Bitmap) data.getExtras().get("data");
                imgLogoEsta.setImageBitmap(bitmaplogo);

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
        String encodedImage = null;

        if(bmp != null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        }
        
        return encodedImage;
    }

    private void CargarImagen(String URL){
        RequestQueue request = Volley.newRequestQueue(this);

        URL = URL.replace(" ","%20");

        ImageRequest imageRequest  = new ImageRequest(URL,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        imgLogoEsta.setImageBitmap(response);
                        bitmaplogo = response;
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
}
