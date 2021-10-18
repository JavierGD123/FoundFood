package com.example.foundfood;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class loginvendedor extends AppCompatActivity {

    private RadioButton RBSesion;
    EditText Usuario,Contrasena;

    private String idusuario = "", DominioURL;

    private boolean isActivateRadioButton;

    private static final String STRING_PREFERENCES="agarrar.valores2";
    private static final String PREFERENCES_ESTADO_BUTTON_SECTION= "estado2";

    private static final String STRING_PREFERENCES_ID_Esta="IDEsta";
    private static final String PREFERENCES_ESTADO_ID_Esta= "iduser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_vendedor);

        VariablesGlobales Url = new VariablesGlobales();
        DominioURL = Url.getURL();

        Usuario =(EditText)findViewById(R.id.txtUsuario);
        Contrasena =(EditText)findViewById(R.id.txtContrasena);
        RBSesion =(RadioButton)findViewById(R.id.RBSesionUsuario);

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

    //Metodo para iniciar
    public void IniciarVendedor(View view){
        validacioncampos();
    }

    public static void cambiarEstadoButton(Context c, boolean b){ //Cambiar estado para el cerrar sesion
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
        //Toast.makeText(getApplicationContext(),"Aquiestoy "+idusuario,Toast.LENGTH_SHORT).show();
        Intent esta = new Intent(this, perfilestablecimiento.class);
        esta.putExtra("idusuario",idusuario);
        GardarIdUsuario(idusuario);
        startActivity(esta);
        finish();
    }

    private void AccesoPerfilManteniendoSesionIniciada(String idusuario){
        Intent esta = new Intent(this, perfilestablecimiento.class);
        esta.putExtra("idusuario",idusuario);
        startActivity(esta);
        finish();

    }

    private void GardarIdUsuario(String idusuario){
        SharedPreferences sharef = getSharedPreferences(STRING_PREFERENCES_ID_Esta, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharef.edit();
        editor.putString(PREFERENCES_ESTADO_ID_Esta,idusuario);
        editor.commit();
    }

    private String ObtenerIDUsuario(){
        SharedPreferences sharef = getSharedPreferences(STRING_PREFERENCES_ID_Esta, Context.MODE_PRIVATE);
        idusuario = sharef.getString(PREFERENCES_ESTADO_ID_Esta,"No existe el ID");
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
            AccesarVendedor();
        }
    }

    private void AccesarVendedor(){

        final  String UserVende = Usuario.getText().toString();
        final  String PwVende = Contrasena.getText().toString();

        final ProgressDialog loading = ProgressDialog.show(this, "Verificando usuario y contraseña...", "Espere por favor");

        Response.Listener<String> respuesta = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonRespuesta = null;
                GuardarEstadoButton();
                try {
                    jsonRespuesta = new JSONObject(response);
                    boolean ok = jsonRespuesta.getBoolean("success");
                    if (ok == true){
                        loading.dismiss();
                        idusuario = jsonRespuesta.getString("id");
                        AccesoPerfilSinMantenerSesionIniciada(idusuario);
                    }else{
                        loading.dismiss();
                        AlertDialog.Builder alerta = new AlertDialog.Builder(loginvendedor.this);
                        alerta.setMessage("Usuario o Contraseña incorrecta").setNegativeButton("Ok", null).create().show();
                    }
                } catch (JSONException e) {
                    e.getMessage();
                }
            }
        };

        LoginVendedorRequest r = new LoginVendedorRequest(UserVende.trim(),PwVende.trim(),DominioURL,respuesta);
        RequestQueue cola = Volley.newRequestQueue(loginvendedor.this);
        cola.add(r);
    }

    private void validaciodeusercontradb(String UserVende,String username){
        if(UserVende.equals(username)){
            Toast.makeText(getApplicationContext(),"Ups **Contraseña incorrecta**",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(),"Ups **Usuario incorrecto**",Toast.LENGTH_LONG).show();
        }
    }

    //Metodo para pasar a otra vista
    public void Registrar_Establecimiento(View view){
        Intent  establecimiento = new Intent(this, registrarestablecimiento.class);
        startActivity(establecimiento);
        finish();
    }

    //Metodo para abrir referencia al cambio de contraseña
    public void CAMBIARCONTRA(View view){
        Uri url = Uri.parse("https://foundfood2019.000webhostapp.com/recupera.php");
        Intent coordenadas = new Intent(Intent.ACTION_VIEW, url);
        startActivity(coordenadas);
    }
}
