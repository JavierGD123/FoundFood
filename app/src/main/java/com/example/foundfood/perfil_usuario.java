package com.example.foundfood;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class perfil_usuario extends AppCompatActivity {

    private EditText Usuario,Email,Contrasena,Telefono,EmailNombre;
    private ImageButton CargarDatos,Actualizar,VistaMapa;
    private Button CerrarSesion;
    private RadioButton CambiarEmail;
    private boolean isActivateRadioButton;

    private String usuario = "";
    private String telefono = "";
    private String contra = "";
    private String img = "";
    private String email = "", EmailCompleto = "", emailnombre;

    private String idusuario = "",DominioURL;

    private Spinner Correos;

    private static final String STRING_PREFERENCES_ID_USER="id";
    private static final String PREFERENCES_ESTADO_ID_USER= "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil__usuario);

        VariablesGlobales Url = new VariablesGlobales();
        DominioURL = Url.getURL();

        Usuario = (EditText) findViewById(R.id.txtUsuario);
        Email = (EditText) findViewById(R.id.txtEmail);
        EmailNombre = (EditText) findViewById(R.id.txtEmailNombre);
        Contrasena = (EditText) findViewById(R.id.txtContrasena);
        Telefono = (EditText) findViewById(R.id.txtTelefono);

        CerrarSesion= (Button) findViewById(R.id.BtnCerrarSesion);
        Actualizar = (ImageButton) findViewById(R.id.btnActualizar);
        VistaMapa = (ImageButton) findViewById(R.id.btnVistaMapa);

        CambiarEmail = (RadioButton) findViewById(R.id.rbtCambiatCorreo);

        ObtenerIDUsuario();
        ObtenemosIdDeLogin();

        Correos = findViewById(R.id.OpcionesCorreosPerfil);

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

        //Seccion de botonos//////////////
        CerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginusuario.cambiarEstadoButton(perfil_usuario.this,false);
                Intent i = new Intent(perfil_usuario.this, ContinuarComo.class);
                startActivity(i);
                finish();
            }
        });

        VistaMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VistaMapa(idusuario);
            }
        });

        Actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Actualizar();
            }
        });
        ////////////////////////////////

        isActivateRadioButton = false; //Desactivado boton se mantener sesion
        EmailNombre.setEnabled(false);
        Correos.setEnabled(false);

    }

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

    private void Actualizar(){

        if(Email.getText().toString().isEmpty()){
            Email.requestFocus();
            Toast.makeText(getApplicationContext(),"No ha ingresado su nuevo email",Toast.LENGTH_LONG).show();
        }else if(Contrasena.getText().toString().isEmpty()){
            Contrasena.requestFocus();
            Toast.makeText(getApplicationContext(),"No ha ingresado su Contraseña",Toast.LENGTH_LONG).show();
        }else if(Usuario.getText().toString().isEmpty()){
            Usuario.requestFocus();
            Toast.makeText(getApplicationContext(),"No ha ingresado su Nombre de usuario",Toast.LENGTH_LONG).show();
        }else if(Telefono.getText().toString().isEmpty()){
            Telefono.requestFocus();
            Toast.makeText(getApplicationContext(),"No ha ingresado su Número de contacto",Toast.LENGTH_LONG).show();
        }else{

            String User = Usuario.getText().toString();
            String Emaill = "";
            String Contra = Contrasena.getText().toString();
            String Cel = Telefono.getText().toString();

            if(isActivateRadioButton == true){
                if(EmailNombre.getText().toString().isEmpty()){
                    EmailNombre.requestFocus();
                    Toast.makeText(getApplicationContext(),"No ha ingresado su nuevo nombre de email",Toast.LENGTH_LONG).show();
                }else{
                    Emaill = EmailNombre.getText().toString() + emailnombre;
                    ObtenerIDUsuario();
                    ActualizarDatos("no",idusuario,User,Contra,Emaill,Cel);
                }
            }else{
                Emaill =  Email.getText().toString();
                ObtenerIDUsuario();
                ActualizarDatos("no",idusuario,User,Contra,Emaill,Cel);
            }
        }

       //Toast.makeText(getApplicationContext(),Nombree,Toast.LENGTH_LONG).show();

    }

    private void ObtenerIDUsuario(){
        SharedPreferences sharef = getSharedPreferences(STRING_PREFERENCES_ID_USER, Context.MODE_PRIVATE);
        idusuario = sharef.getString(PREFERENCES_ESTADO_ID_USER,"No existe el ID");
    }

    private void GardarIdUsuario(String idusuario){
        SharedPreferences sharef = getSharedPreferences(STRING_PREFERENCES_ID_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharef.edit();

        editor.putString(PREFERENCES_ESTADO_ID_USER,idusuario);
        editor.commit();
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

    private void MostrarDatos(String cargo,String iduser){

        final ProgressDialog loading = ProgressDialog.show(this, "Cargando sus datos ...", "Espere por favor");

        Response.Listener<String> respuesta = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonRespuesta = null;
                try {
                    jsonRespuesta = new JSONObject(response);
                    boolean ok = jsonRespuesta.getBoolean("success");
                    if (ok == true){
                        loading.dismiss();
                        usuario = jsonRespuesta.getString("usuario");
                        contra = jsonRespuesta.getString("contra");
                        email = jsonRespuesta.getString("email");
                        telefono = jsonRespuesta.getString("telefono");
                        //img = jsonRespuesta.getString("img");

                        Usuario.setText(usuario);
                        Email.setText(email);
                        Email.setEnabled(false);
                        Contrasena.setText(contra);
                        Telefono.setText(telefono);
                        //Toast.makeText(getApplicationContext(),nombre+apellido+usuario+telefono+contra,Toast.LENGTH_LONG).show();
                    }else{
                        loading.dismiss();
                        //validaciodeusercontradb(User,username);
                        AlertDialog.Builder alerta = new AlertDialog.Builder(perfil_usuario.this);
                        alerta.setMessage("Usuario o Contraseña incorrecta").setNegativeButton("Reintentar", null).create().show();
                    }
                } catch (JSONException e) {
                    e.getMessage();
                }
            }
        };

        PelfilUsuarioRequest r = new PelfilUsuarioRequest(cargo.trim(),iduser.trim(),usuario.trim(),contra.trim(),email.trim(),telefono.trim(),DominioURL,respuesta);
        RequestQueue cola = Volley.newRequestQueue(perfil_usuario.this);
        cola.add(r);
    }

    private void ActualizarDatos(final String cargo, final String iduser, String usuario, String contra, String email, String telefono){

        final ProgressDialog loading = ProgressDialog.show(this, "Actualizando sus datos...", "Espere por favor");

        Response.Listener<String> respuesta = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonRespuesta = null;
                try {
                    jsonRespuesta = new JSONObject(response);
                    boolean ok = jsonRespuesta.getBoolean("success");
                    if (ok == true){
                       // MostrarDatos(cargo,iduser);
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),"Se han actualizado sus datos correctamente",Toast.LENGTH_SHORT).show();
                    }else{
                        //validaciodeusercontradb(User,username);
                        AlertDialog.Builder alerta = new AlertDialog.Builder(perfil_usuario.this);
                        alerta.setMessage("Usuario o Contraseña incorrecta").setNegativeButton("Reintentar", null).create().show();
                    }
                } catch (JSONException e) {
                    loading.dismiss();
                    e.getMessage();
                }
            }
        };

        PelfilUsuarioRequest r = new PelfilUsuarioRequest(cargo,iduser,usuario,contra,email,telefono,DominioURL,respuesta);
        RequestQueue cola = Volley.newRequestQueue(perfil_usuario.this);
        cola.add(r);
    }

    //Metodo para pasar a otra vista
    public void VistaMapa(String idusuario){
        Intent mapa = new Intent(this, vistamapa.class);
        mapa.putExtra("idusuario",idusuario);
        startActivity(mapa);
        finish();
    }
}
