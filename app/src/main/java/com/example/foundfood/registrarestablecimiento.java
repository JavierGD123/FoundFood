package com.example.foundfood;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;

import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class registrarestablecimiento extends AppCompatActivity {
    private EditText Nombre,Usuario, Email, Telefono, Latitud, Longitud,Contrasena,ContrasenaVerificacion;
    private TextView Coordenadas;
    private Button Registrar;
    private Spinner Correos;

    private String email;

    private String DominioURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_establecimiento);

        VariablesGlobales Url = new VariablesGlobales();
        DominioURL = Url.getURL();

        Nombre =(EditText)findViewById(R.id.txtNombre);
        Usuario =(EditText) findViewById(R.id.txtUsuario);
        Email =(EditText) findViewById(R.id.txtEmail);
        Contrasena =(EditText) findViewById(R.id.txtContrasena);
        Telefono =(EditText) findViewById(R.id.txtTelefono);
        Latitud =(EditText) findViewById(R.id.txtLatitud);
        Longitud =(EditText) findViewById(R.id.txtLongitud);
        ContrasenaVerificacion =(EditText) findViewById(R.id.txtContrasenaVerificarr);

        Registrar = (Button)findViewById(R.id.btnRegistrar);

        Correos = findViewById(R.id.spinnerCorreos);

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
            {

            }
        });

        Registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //final  String Correo = Email.getText().toString()+email;
                //Toast.makeText(getApplicationContext(),"Email: "+Correo,Toast.LENGTH_SHORT).show();
                validaciondecampos();
            }
        });
    }

    private void validaciondecampos(){
        if(Nombre.getText().toString().isEmpty()){
            Nombre.requestFocus();
            Toast.makeText(getApplicationContext(),"No ha ingresado su Nombre",Toast.LENGTH_LONG).show();
        }else if(Email.getText().toString().isEmpty()){
            Email.requestFocus();
            Toast.makeText(getApplicationContext(),"No ha ingresado su Email",Toast.LENGTH_LONG).show();
        }else if(Contrasena.getText().toString().isEmpty()){
            Contrasena.requestFocus();
            Toast.makeText(getApplicationContext(),"No ha ingresado su Contraseña",Toast.LENGTH_LONG).show();
        }else if(ContrasenaVerificacion.getText().toString().isEmpty()){
            ContrasenaVerificacion.requestFocus();
            Toast.makeText(getApplicationContext(),"No ha ingresado su verificación de contraseña",Toast.LENGTH_LONG).show();
        }else if(Usuario.getText().toString().isEmpty()){
            Usuario.requestFocus();
            Toast.makeText(getApplicationContext(),"No ha ingresado su Nombre de usuario",Toast.LENGTH_LONG).show();
        }else if(Telefono.getText().toString().isEmpty()){
            Telefono.requestFocus();
            Toast.makeText(getApplicationContext(),"No ha ingresado su Número de contacto",Toast.LENGTH_LONG).show();
        }else if(Longitud.getText().toString().isEmpty()){
            Longitud.requestFocus();
            Toast.makeText(getApplicationContext(),"No ha ingresado la Longitud",Toast.LENGTH_LONG).show();
        }else if(Latitud.getText().toString().isEmpty()){
            Latitud.requestFocus();
            Toast.makeText(getApplicationContext(),"No ha ingresado la Latitud",Toast.LENGTH_LONG).show();
        }else{
            if(Contrasena.getText().toString().equals(ContrasenaVerificacion.getText().toString())){
                Capturar();
            }else{
                AlertDialog.Builder alerta = new AlertDialog.Builder(this);
                alerta.setMessage("No son compatibles las contraseñas por favor verifique que sean iguales").setNegativeButton("Ok", null).create().show();
            }
            //Toast.makeText(getApplicationContext(),"Nombre: "+Nombre+"Usuario: "+Usuario+"Contraseña: "+Contrasena+"Latitud: "+Latitud+"Longitud: "+Longitud,Toast.LENGTH_LONG).show();
           // new CargarDatos().execute("http://192.168.1.67/FoundFood/RegistroVendedor.php?nombre="+Nombre.getText().toString()+"&usuario="+Usuario.getText().toString()+"&contra="+Contrasena.getText().toString()+"&email="+Email.getText().toString()+"&numerotel="+Telefono.getText().toString()+"&latitud="+Latitud.getText().toString()+"&longitud="+Longitud.getText().toString());
            //new CargarDatos().execute("https://fofo123.000webhostapp.com/RegistroVendedor.php?nombre="+Nombre+"&usuario="+Usuario+"&contra="+Contrasena+"&email="+Email+"&numerotel="+Telefono+"&latitud="+Latitud+"&longitud="+Longitud);
        }
    }

    private void Capturar(){

        final  String Name = Nombre.getText().toString();
        final  String User = Usuario.getText().toString();
        final  String Correo = Email.getText().toString()+email;
        final  String Pw = Contrasena.getText().toString();
        final  String Cel = Telefono.getText().toString();

        final  String lat = Latitud.getText().toString();
        final  String lon = Longitud.getText().toString();

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
        RegistroEstablecimientoRequest r = new RegistroEstablecimientoRequest(Name.trim(),User.trim(),Pw.trim(),Correo.trim(),Cel.trim(),lat.trim(),lon.trim(),DominioURL,respuesta);
        RequestQueue cola = Volley.newRequestQueue(registrarestablecimiento.this);
        cola.add(r);
    }
    //Metodo para regresar a la vista anterior
    public void ERROR(int val){
        if(val == 1){
            Toast.makeText(getApplicationContext(),"Correo invalido",Toast.LENGTH_SHORT).show();
            /*AlertDialog.Builder alertaa = new AlertDialog.Builder(registrarestablecimiento.this);
            alertaa.setMessage("Correo invalido").setNegativeButton("Reintentar", null).create().show();*/
        }else{
            AlertDialog.Builder alerta = new AlertDialog.Builder(registrarestablecimiento.this);
            alerta.setMessage("Usuario o Email existentes").setNegativeButton("Reintentar", null).create().show();
        }
    }

    //Metodo para abrir referencia a las Coordenasdas
    public void Agregar_LatitudLongitud(View view){
        Coordenadas = (TextView) findViewById(R.id.txtCoordenadas);
        Uri url = Uri.parse("https://www.coordenadas-gps.com/");
        Intent coordenadas = new Intent(Intent.ACTION_VIEW, url);
        startActivity(coordenadas);
    }

    //Metodo para regresar a la vista anterior
    public void regresar(View view){
        Intent regresar = new Intent(this, loginvendedor.class);
        startActivity(regresar);
        finish();
    }

    //Metodo para ir a pagina principal del usuario
    public void bienbenido(){
        Toast.makeText(getApplicationContext(),"Registro Exitoso",Toast.LENGTH_SHORT).show();
        Intent welcome = new Intent(this, loginvendedor.class);
        startActivity(welcome);
        finish();
    }
}
