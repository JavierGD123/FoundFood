package com.example.foundfood;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class loginusuario extends AppCompatActivity {

    private RadioButton RBSesion;
    EditText Usuario,Contrasena,IniciarInvitado;

    private boolean isActivateRadioButton;
    private static final String STRING_PREFERENCES="agarrar.valores1";
    private static final String PREFERENCES_ESTADO_BUTTON_SECTION= "estado1";

    private static final String STRING_PREFERENCES_ID="iduuseresta";
    private static final String PREFERENCES_ESTADO_ID= "iduseresta";

    private static final String STRING_PREFERENCES_ID_USER="id";
    private static final String PREFERENCES_ESTADO_ID_USER= "user";

    private String idusuario = "";
    private String usuario = "";
    private String telefono = "";
    private String contra = "";
    private String img = "";
    private String email = "",DominioURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_usuario);

        VariablesGlobales Url = new VariablesGlobales();
        DominioURL = Url.getURL();

        PermisosUbicacion();

        Usuario =(EditText)findViewById(R.id.txtUsuario);
        Contrasena =(EditText)findViewById(R.id.txtContrasena);
        RBSesion =(RadioButton)findViewById(R.id.RBSesionUsuario);

        IniciarInvitado = findViewById(R.id.txtContrasenaverificar);

        if(ObtenerEstadoButton()) {
            if(ObtenerIDUsuario().equals("No existe el ID")){
                Toast.makeText(getApplicationContext(),"No existe ID",Toast.LENGTH_SHORT);
            }else{
                AccesoPerfilManteniendoSesionIniciada(idusuario);
            }
        }
        isActivateRadioButton = RBSesion.isChecked(); //Desactivado boton se mantener sesion

        RBSesion.setOnClickListener(new View.OnClickListener() {
            @Override

            //Activado botton mantener sesion activado
            public void onClick(View v) {
                if(isActivateRadioButton){
                    RBSesion.setChecked(false);
                }
                isActivateRadioButton = RBSesion.isChecked();
            }
        });

       }

       private void PermisosUbicacion(){
           ///Localizacion actual
           if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                   && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)  != PackageManager.PERMISSION_GRANTED){

               if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                       Manifest.permission.ACCESS_COARSE_LOCATION)) {
                   // Show an explanation to the user *asynchronously* -- don't block
                   // this thread waiting for the user's response! After the user
                   // sees the explanation, try again to request the permission.
               } else {
                   // No explanation needed; request the permission
                   ActivityCompat.requestPermissions(this,
                           new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                           1);
                   // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                   // app-defined int constant. The callback method gets the
                   // result of the request.
               }

               if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                       Manifest.permission.ACCESS_FINE_LOCATION)){
                   // Show an explanation to the user *asynchronously* -- don't block
                   // this thread waiting for the user's response! After the user
                   // sees the explanation, try again to request the permission.
               }else{
                   // No explanation needed; request the permission
                   ActivityCompat.requestPermissions(this,
                           new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                           1);
                   // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                   // app-defined int constant. The callback method gets the
                   // result of the request.
               }
               return;
           }
       }


    //Metodo para iniciar
    public void IniciarUsuario(View view){
        validacioncampos();
    }

    //Metodo para iniciar
    public void IniciarUsuarioInvitado(View view){
        Intent  mapa = new Intent(this, vistamapa.class);
        mapa.putExtra("idusuario","invitado");
        GardarIdInvitado("invitado");
        startActivity(mapa);
        finish();
    }

    private void GardarIdInvitado(String idusuario){
        SharedPreferences sharef = getSharedPreferences(STRING_PREFERENCES_ID_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharef.edit();

        editor.putString(PREFERENCES_ESTADO_ID_USER,idusuario);
        editor.commit();
    }


    public static void cambiarEstadoButton(Context c,boolean b){ //Cambiar estado para el cerrar sesion
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES,MODE_PRIVATE);
        preferences.edit().putBoolean(PREFERENCES_ESTADO_BUTTON_SECTION,b).apply();
    }

    public void GuardarEstadoButton(){  // guardar estado del botton
        SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCES,MODE_PRIVATE);
        preferences.edit().putBoolean(PREFERENCES_ESTADO_BUTTON_SECTION,RBSesion.isChecked()).apply();
    }

    public boolean ObtenerEstadoButton(){ //obtener estado del botton
        SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCES,MODE_PRIVATE);
        return preferences.getBoolean(PREFERENCES_ESTADO_BUTTON_SECTION,false);
    }

    private void AccesoPerfilSinMantenerSesionIniciada(String idusuario){
        Intent esta = new Intent(this, perfil_usuario.class);
        esta.putExtra("idusuario",idusuario);

        GardarIdUsuario(idusuario);

        startActivity(esta);
        finish();
    }

    private void AccesoPerfilManteniendoSesionIniciada(String idusuario){
        Intent esta = new Intent(this, perfil_usuario.class);
        esta.putExtra("idusuario",idusuario);
        startActivity(esta);
        finish();

    }

    private void GardarIdUsuario(String idusuario){
        SharedPreferences sharef = getSharedPreferences(STRING_PREFERENCES_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharef.edit();
        editor.putString(PREFERENCES_ESTADO_ID,idusuario);
        editor.commit();
    }

    private String ObtenerIDUsuario(){
        SharedPreferences sharef = getSharedPreferences(STRING_PREFERENCES_ID, Context.MODE_PRIVATE);
        idusuario = sharef.getString(PREFERENCES_ESTADO_ID,"No existe el ID");
        return  idusuario;
    }

    private void validacioncampos(){
        if(Usuario.getText().toString().isEmpty() && Contrasena.getText().toString().isEmpty()){
            Usuario.requestFocus();
            Toast.makeText(getApplicationContext(),"No ha ingresado su nombre de Usuario o Contraseña",Toast.LENGTH_LONG).show();
        }else if(Contrasena.getText().toString().isEmpty()){
            Contrasena.requestFocus();
            Toast.makeText(getApplicationContext(),"No ha ingresado su Contraseña",Toast.LENGTH_LONG).show();
        }else if(Usuario.getText().toString().isEmpty()){
            Usuario.requestFocus();
            Toast.makeText(getApplicationContext(),"No ha ingresado su nombre de Usuario",Toast.LENGTH_LONG).show();
        }else{
            AccesarUsuario();
        }
    }

    private void AccesarUsuario(){

        final  String User = Usuario.getText().toString();
        final  String Pw = Contrasena.getText().toString();

        final ProgressDialog loading = ProgressDialog.show(this, "Verificando usuario y contraseña...", "Espere por favor");

        Response.Listener<String> respuesta = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonRespuesta = null;
                GuardarEstadoButton();  // comenzar dependiendo del botton mantener sesion
                try {
                    jsonRespuesta = new JSONObject(response);
                    boolean ok = jsonRespuesta.getBoolean("success");
                    if (ok == true){
                        loading.dismiss();
                        idusuario = jsonRespuesta.getString("iduser");
                        AccesoPerfilSinMantenerSesionIniciada(idusuario);
                    }else{
                        loading.dismiss();
                        //validaciodeusercontradb(User,username);
                        AlertDialog.Builder alerta = new AlertDialog.Builder(loginusuario.this);
                        alerta.setMessage("Usuario o Contraseña incorrecta").setNegativeButton("Ok", null).create().show();
                    }
                } catch (JSONException e) {
                    e.getMessage();
                }
            }
        };

        LoginUsuarioRequest r = new LoginUsuarioRequest(User.trim(),Pw.trim(),DominioURL,respuesta);
        RequestQueue cola = Volley.newRequestQueue(loginusuario.this);
        cola.add(r);
    }

    private void validaciodeusercontradb(String UserVende,String username){
        if(UserVende.equals(username)){
            Toast.makeText(getApplicationContext(),"Ups **Contraseña incorrecta**",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(),"Ups **Usuario incorrecto**",Toast.LENGTH_LONG).show();
        }
    }

    //Metodo para abrir referencia al cambio de contraseña
    public void CAMBIARCONTRA(View view){
        Uri url = Uri.parse("https://foundfood2019.000webhostapp.com/recuperauser.php");
        Intent coordenadas = new Intent(Intent.ACTION_VIEW, url);
        startActivity(coordenadas);
    }

    //Metodo para pasar a otra vista
    public void RegistrarUsuario(View view){
       Intent  establecimiento = new Intent(this, registrarusuario.class);
       startActivity(establecimiento);
       finish();
    }
}
