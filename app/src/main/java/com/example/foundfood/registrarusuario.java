package com.example.foundfood;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class registrarusuario extends AppCompatActivity {

    private EditText Usuario, Email, Telefono,Contrasena,ContrasenaVerificacion;
    private Spinner Correos;

   private String email,DominioURL;

    Button Regresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);

        VariablesGlobales Url = new VariablesGlobales();
        DominioURL = Url.getURL();

        Usuario =(EditText) findViewById(R.id.txtUsuario);
        Email =(EditText) findViewById(R.id.txtEmail);
        Contrasena =(EditText) findViewById(R.id.txtContrasena);
        ContrasenaVerificacion =(EditText) findViewById(R.id.txtContrasenaverificar);
        Telefono =(EditText) findViewById(R.id.txtTelefono);

        Correos = findViewById(R.id.OpcionesCorreos);

        String[] correos = {"@gmail.com","@hotmail.com","@outlook.com","@yahoo.com","@icloud.com"};
        Correos.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, correos));

        Correos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id)
            {
                email = (String) adapterView.getItemAtPosition(pos);
                //Toast.makeText(adapterView.getContext(),(String) adapterView.getItemAtPosition(pos), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {    }
        });

        /*Regresar = (Button)findViewById(R.id.btnRegresar);

        Regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regresar();
            }
        });*/
    }

    //Metodo para registrar los datos
    public void Registro(View view){
        //final  String Correo = Email.getText().toString()+email;
        //Toast.makeText(getApplicationContext(),"Email: "+Correo,Toast.LENGTH_SHORT).show();
        validaciondecampos();
    }

    private void validaciondecampos(){
        if(Email.getText().toString().isEmpty()){
            Email.requestFocus();
            Toast.makeText(getApplicationContext(),"No ha ingresado su Email",Toast.LENGTH_LONG).show();
        }else if(Contrasena.getText().toString().isEmpty()){
            Contrasena.requestFocus();
            Toast.makeText(getApplicationContext(),"No ha ingresado su Contraseña",Toast.LENGTH_LONG).show();
        }else if(Usuario.getText().toString().isEmpty()){
            Usuario.requestFocus();
            Toast.makeText(getApplicationContext(),"No ha ingresado su Nombre de usuario",Toast.LENGTH_LONG).show();
        }else if(ContrasenaVerificacion.getText().toString().isEmpty()){
            ContrasenaVerificacion.requestFocus();
            Toast.makeText(getApplicationContext(),"No ha ingresado su Contraseña de verificación",Toast.LENGTH_LONG).show();
        }else if(Telefono.getText().toString().isEmpty()){
            Telefono.requestFocus();
            Toast.makeText(getApplicationContext(),"No ha ingresado su Número de contacto",Toast.LENGTH_LONG).show();
        }else{
            if(Contrasena.getText().toString().equals(ContrasenaVerificacion.getText().toString())){
                Capturar();
            }else{
                AlertDialog.Builder alerta = new AlertDialog.Builder(this);
                alerta.setMessage("No son compatibles las contraseñas por favor verifique que sean iguales").setNegativeButton("Ok", null).create().show();
            }

        }
    }

    public void Capturar(){

        final  String User = Usuario.getText().toString();
        final  String Correo = Email.getText().toString()+email;
        final  String Pw = Contrasena.getText().toString();
        final  String Cel = Telefono.getText().toString();

        final ProgressDialog loading = ProgressDialog.show(this, "Registrando...", "Espere por favor");

        Response.Listener<String> respuesta = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonRespuesta = null;
                try {
                    jsonRespuesta = new JSONObject(response);
                    boolean ok = jsonRespuesta.getBoolean("success");
                    String correonovalido = jsonRespuesta.getString("success");
                    if (ok == true){
                        loading.dismiss();
                        bienbenido();
                    }else if (correonovalido.equals("correoinvalido")){
                        loading.dismiss();
                        ERROR(1);
                    }else{
                        loading.dismiss();
                        ERROR(2);
                    }
                } catch (JSONException e) {
                    e.getMessage();
                }

            }
        };

        RegistroUsuarioRequest r = new RegistroUsuarioRequest(User.trim(),Pw.trim(),Correo.trim(),Cel.trim(),DominioURL,respuesta);
        RequestQueue cola = Volley.newRequestQueue(registrarusuario.this);
        cola.add(r);
    }

    public void ERROR(int val){
        if(val == 1){
            Toast.makeText(getApplicationContext(),"Correo invalido",Toast.LENGTH_SHORT).show();
            /*AlertDialog.Builder alertaa = new AlertDialog.Builder(registrarestablecimiento.this);
            alertaa.setMessage("Correo invalido").setNegativeButton("Reintentar", null).create().show();*/
        }else{
            AlertDialog.Builder alerta = new AlertDialog.Builder(registrarusuario.this);
            alerta.setMessage("Usuario o Email existentes").setNegativeButton("Reintentar", null).create().show();
        }
    }

    private  void validarUsuarioEmail(String NomUser, String Correo){

        if((Usuario.getText().toString()).equals(NomUser) && (Email.getText().toString()).equals(Correo)){
            Toast.makeText(getApplicationContext(),"El Usuario o Email ya existen",Toast.LENGTH_LONG).show();
        }

         if((Usuario.getText().toString()).equals(NomUser)){
            Toast.makeText(getApplicationContext(),"El Usuario ya existen",Toast.LENGTH_LONG).show();
        }

         if((Email.getText().toString()).equals(Correo)){
            Toast.makeText(getApplicationContext(),"El correo ya existen",Toast.LENGTH_LONG).show();
        }

    }

    //Metodo para regresar a la vista anterior
    public void regresar(View view){
        Intent regresar = new Intent(this, loginusuario.class);
        startActivity(regresar);
        finish();
    }

    //Metodo para ir a pagina principal del usuario
    public void bienbenido(){
        Toast.makeText(getApplicationContext(),"Registro Exitoso",Toast.LENGTH_SHORT).show();
        Intent welcome = new Intent(this, loginusuario.class);
        startActivity(welcome);
        finish();
    }
}
