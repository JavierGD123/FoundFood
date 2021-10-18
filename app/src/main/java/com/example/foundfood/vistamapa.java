package com.example.foundfood;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class vistamapa extends FragmentActivity implements GoogleMap.OnInfoWindowClickListener,OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private ImageButton BotonPerfil;
    private GoogleMap mMap;
    private Boolean actualPosition = true;
    double latitudOrigen, longitudOrigen;
    float zoomincial = 16;
    JSONObject jso;

    private Marker MarcadorNuevoEsta[];

    private SearchView Buscador;

    private LatLng Ubicacion[], mylocation;

    private String iduser,iduseresta,Resultado[],NombreRefe;
    private String NombreEstablecimiento[];
    //private double Latitud = 18.0220407,Longitud = -102.2122602;
    private double Latitud[], Longitud[];
    private int TotalRegistros = 0;
    private TextView ResultadoBusqueda;

    private AutoCompleteTextView NombreReferencia;

    private String DominioURL;

    private static final String STRING_PREFERENCES_ID_USER="id";
    private static final String PREFERENCES_ESTADO_ID_USER= "user";

    private Boolean buscadordesabilitado = false, sinestablecimientos = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_mapa);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        VariablesGlobales Url = new VariablesGlobales();
        DominioURL = Url.getURL();

        //Intent login = this.getIntent();
       // String iduser = login.getStringExtra("idusuario");

        NombreReferencia = (AutoCompleteTextView)findViewById(R.id.txtNombreReferencia);

        BotonPerfil = (ImageButton) findViewById(R.id.btnPerfilUsuario);

        BotonPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IrPerfil();
            }
        });

        EnviarNombre(DominioURL+"/Buscador_de_Establecimientos.php");

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        if (status == ConnectionResult.SUCCESS) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        } else {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, (Activity) getApplicationContext(), 10);
            dialog.show();
        }

        ObtenerIDUsuario();

        if(iduser.equals("invitado") || iduser.equals("No existe el ID") ){
            AlertDialog.Builder alerta = new AlertDialog.Builder(this);
            alerta.setMessage("Si desesa recibir notificaciones de nuevos productos, promociones, así como un mejor servicio debe contar con una cuenta de usuario. Gracias por usar FoundFood ").setNegativeButton("Entendido", null).create().show();
        }

        Contadordetiempo();
    }

    private void ObtenerIDUsuario(){
        SharedPreferences sharef = getSharedPreferences(STRING_PREFERENCES_ID_USER, Context.MODE_PRIVATE);
        iduser = sharef.getString(PREFERENCES_ESTADO_ID_USER,"No existe el ID");
    }

    public void PROMOCIONES(View view){
        ObtenerIDUsuario();

        if(iduser.equals("No existe el ID") || iduser.equals("invitado")){
            AlertaPromciones();
        }else{
            //Se reenvia los datos a la vista perfilusuasio
            Intent promociones = new Intent(this, promociones.class);
            startActivity(promociones);
            finish(); //Terminar activity
        }


    }

    public void AlertaPromciones(){
        final CharSequence[] opciones ={"Registrate","Entendido"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Necesitas una cuenta para poder ver las promociones.");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for (int j = 0; j < 2; j++) {
                    if (opciones[i].equals("Registrate")) {
                        //Llamado almetodo para activar camara
                        RegistrarUsuario();
                    } else {
                        dialogInterface.dismiss();
                    }
                    break;
                }
            }
        });
        builder.show();

    }

    public void BuscarEsta(View view){
        Buscar(NombreReferencia.getText().toString().trim(),NombreEstablecimiento);
    }

    private void Buscar(String  NombreRefe, String ProductoaBuscar[]){
        int encontrado = 0;
        // Toast.makeText(getApplicationContext(),Promociones[0],Toast.LENGTH_SHORT).show();
        if(NombreRefe.equals("")){
            Toast.makeText(getApplicationContext(),"No ha ingresado alguna referecia.",Toast.LENGTH_SHORT).show();
        }else{
            for(int i = 0; i < ProductoaBuscar.length; i++){
                //Toast.makeText(getApplicationContext(),NombreRefe,Toast.LENGTH_SHORT).show();
                if(NombreRefe.equals(ProductoaBuscar[i])){
                    MostrarDialogoOpciones(i);
                    encontrado = 1;
                    break;
                }

            }
        }

        if(encontrado == 0){
            Toast.makeText(getApplicationContext(),"No se encuentra ese establecimiento en la app",Toast.LENGTH_SHORT).show();
        }
    }

    private void EnviarNombre(String URL) {
        final int[] existennombres = {0};

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                Resultado = new String[response.length()];

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        Resultado[i]  = jsonObject.getString("Nombre_de_Establecimiento");
                        //Toast.makeText(vistamapa.this, jsonObject.getString("Nombre_de_Establecimiento"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                if(Resultado != null){
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(vistamapa.this,android.R.layout.simple_dropdown_item_1line,Resultado);
                    NombreReferencia.setAdapter(adapter);
                    existennombres[0] = 1;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(existennombres[0] == 0){
                    Toast.makeText(getApplicationContext(), "No hay establecimientos registrados", Toast.LENGTH_SHORT).show();
                }
            }
        });

        RequestQueue cola = Volley.newRequestQueue(vistamapa.this);
        cola.add(jsonArrayRequest);

    }

    private int nuevoesta = 0;

    private void ObtenerEstayUbicacion(String URL) {
        final int[] conestablecimientos = {0};
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                if(TotalRegistros < response.length()){
                    TotalRegistros = response.length();
                    nuevoesta = 1;
                    mMap.clear();
                }
               // Toast.makeText(getApplicationContext(),"Total registros",Toast.LENGTH_SHORT).show();
                if(nuevoesta == 1){
                    //Toast.makeText(getApplicationContext(),"Total de Registros: "+response.length(),Toast.LENGTH_SHORT).show();
                    MarcadorNuevoEsta = new Marker[TotalRegistros];
                    Longitud = new double[TotalRegistros];
                    Latitud = new double[TotalRegistros];
                    NombreEstablecimiento = new String[TotalRegistros];
                    Ubicacion = new LatLng[TotalRegistros];

                    //Toast.makeText(getApplicationContext(),TotalRegistros,Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < TotalRegistros; i++) {
                        try {
                            jsonObject = response.getJSONObject(i);
                            //Se almacena cada dato del establecimiento
                            NombreEstablecimiento[i]  = jsonObject.getString("Nombre_de_Establecimiento");
                            Latitud[i] = jsonObject.getDouble("Latitud");
                            Longitud[i] = jsonObject.getDouble("Longitud");

                            //Creacion de Marcadores para cada establecimiento
                            Ubicacion[i] = new LatLng(Latitud[i], Longitud[i]);
                            MarcadorNuevoEsta[i] = mMap.addMarker(new MarkerOptions()
                                    .position(Ubicacion[i])
                                     //       .title(NombreEstablecimiento[i]).icon(BitmapDescriptorFactory.fromResource(R.drawable.almacenaricono))
                                    .title(NombreEstablecimiento[i]).icon(BitmapDescriptorFactory.fromResource(R.drawable.esta2))
                            );

                            //Toast.makeText(vistamapa.this,NombreEstablecimiento[i]+" : "+Latitud[i]+" : "+Longitud[i], Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    conestablecimientos[0] = 1;
                    nuevoesta = 0;
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(conestablecimientos[0] == 0){
                    Toast.makeText(getApplicationContext(), "No hay establecimientos registrados", Toast.LENGTH_SHORT).show();
                    MarcadorNuevoEsta = new Marker[1];
                    Longitud = new double[1];
                    Latitud = new double[1];
                    NombreEstablecimiento = new String[1];
                    Ubicacion = new LatLng[1];
                    sinestablecimientos = true;
                    //buscadordesabilitado = true;
                    //NombreReferencia.setEnabled(false);
                }
            }
        });

        RequestQueue cola = Volley.newRequestQueue(vistamapa.this);
        cola.add(jsonArrayRequest);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);

        // Marcadores para cada establecimiento
       // if(sinestablecimientos == false){
            ObtenerEstayUbicacion(DominioURL+"/ObtenciondeUbicacionEstablecimiento.php");
        //}

        //Toast.makeText(getApplicationContext(), ""+buscadordesabilitado, Toast.LENGTH_SHORT).show();

        //if(buscadordesabilitado == true){
          //  NombreReferencia.setEnabled(false);
        //}

        mMap.setOnMarkerClickListener(this);//Metodo para escuchar los clic que se den acad marcador

        mMap.setOnInfoWindowClickListener(this);//Metodo para escuchar los clics que seden acada info de marcador
        ////////////////////////////

        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

                if(actualPosition){
                    latitudOrigen = location.getLatitude();
                    longitudOrigen = location.getLongitude();
                    actualPosition=false;

                    /*LatLng*/ mylocation = new LatLng( latitudOrigen, longitudOrigen);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mylocation,zoomincial));


                }
            }
        });

       Contadordetiempo();
    }

    private void Contadordetiempo(){

        new CountDownTimer(1000,1000) {
            @Override

            public void onTick(long l) {
                Log.e("segundos restantes",""+l/1000);
            }

            @Override
            public void onFinish() {
               // Toast.makeText(getApplicationContext(),"Puntos actualizados",Toast.LENGTH_SHORT).show();
                //mMap.clear();
                //onMapReady(mMap);
            }
        }.start();
    }

    private void IrPerfil(){
        ObtenerIDUsuario();

        if(iduser.equals("No existe el ID") || iduser.equals("invitado")){
            AlertaInvitado();
        }else{
            //Se reenvia los datos a la vista perfilusuasio
            Intent usuarioPerfil = new Intent(this, perfil_usuario.class);
            usuarioPerfil.putExtra("idusuario",iduser);
            startActivity(usuarioPerfil);
            finish(); //Terminar activity
        }


    }

    public void AlertaInvitado(){
        final CharSequence[] opciones ={"Registrate","Entendido"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¡Aún no te haz registrado!");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for (int j = 0; j < 2; j++) {
                    if (opciones[i].equals("Registrate")) {
                        //Llamado almetodo para activar camara
                        RegistrarUsuario();
                    } else {
                        dialogInterface.dismiss();
                    }
                    break;
                }
            }
        });
        builder.show();

    }

    //Metodo para pasar a otra vista
    public void RegistrarUsuario(){
        Intent  establecimiento = new Intent(this, registrarusuario.class);
        startActivity(establecimiento);
        finish();
    }

   // private int Trazo = 0;
    Polyline polyline;

    private void trazarRuta(/*double latOrigene, double lonOrigen,*/ double latDestino, final double lonDestino){

        //Consulta con el js para calcular la ruta
        String url ="https://maps.googleapis.com/maps/api/directions/json?origin="+latitudOrigen+","+longitudOrigen+"&destination="+latDestino+","+lonDestino+"&key=AIzaSyAu--rBeTGe8_oC7crmAAuj6n4WLhdYNCY";
        //polyline = new Polyline[2];

        RequestQueue queue = Volley.newRequestQueue(vistamapa.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("routes");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("overview_polyline");
                    String points = jsonObject2.getString("points");
                    //mMap.removeOverlay(polyline);
                    PolylineOptions lineOptions = new PolylineOptions().addAll(PolyUtil.decode(points));
                    polyline = mMap.addPolyline(lineOptions);
                    Log.i("jsonRuta: ",""+response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
        //////////////////////////////////////
       // Trazo = 0;
    }

    private void LimpiarRutas(){
        //polyline.remove();
        TotalRegistros = 0;
        mMap.clear();
        onMapReady(mMap);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    //Metodo para acceder a otra vista hacieno clic en un punto en el mapa
    @Override
    public boolean onMarkerClick(Marker marker) {
       LimpiarRutas();
       for(int i = 0; i<MarcadorNuevoEsta.length; i++){
            if (marker.equals(MarcadorNuevoEsta[i])){
                OpcinesRutas(Latitud[i],Longitud[i], NombreEstablecimiento[i]);
                //Toast.makeText(getApplicationContext(),NombreEstablecimiento[i],Toast.LENGTH_LONG).show();
                break;
            }
        }
        return false;
    }

    public void OpcinesRutas(final double lat, final double lon, final String nombre){
        final CharSequence[] opciones ={"Mostrar como llegar","Abrir información","Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("**"+nombre+"**\nSeleccione una opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for (int j = 0; j < 3; j++) {
                    if (opciones[i].equals("Mostrar como llegar")) {
                        //Llamado almetodo para activar camara
                        trazarRuta(lat,lon);
                    } else if (opciones[i].equals("Abrir información")) {
                        //Llamado almetodo para activar camara
                        Abrirmenus(nombre);
                    }else {
                        dialogInterface.dismiss();
                    }
                    break;
                }
            }
        });
        builder.show();
    }

    //Metodo para pasar a la vista de menus
    private void Abrirmenus(String Nombre) {
       // ObtenemosIdDePerfilUsuario();
        Intent menu = new Intent(this, menuparaclientes.class);
        //menu.putExtra("idusuario",iduser);
        menu.putExtra("nombreesta",Nombre);
        startActivity(menu);
        finish();
    }

    //Metodo paara infodesdetexto de marcdores
    @Override
    public void onInfoWindowClick(Marker marker) {
        for(int i = 0; i<MarcadorNuevoEsta.length; i++){
            if (marker.equals(MarcadorNuevoEsta[i])){
                //Toast.makeText(getApplicationContext(),NombreEstablecimiento[i],Toast.LENGTH_LONG).show();
                Abrirmenus(NombreEstablecimiento[i]);
                break;
            }
        }
    }

    private void MostrarDialogoOpciones(final int esta){
            final CharSequence[] opciones ={"Información","Ubicación","Cancelar"};
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Establecimiento \n"+NombreReferencia.getText().toString()+"\nElije una Opción");
            builder.setItems(opciones, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    for (int j = 0; j < Ubicacion.length; j++) {
                         if (j == esta) {
                            if (opciones[i].equals("Información")) {
                                //Llamado almetodo para activar camara
                                Abrirmenus(NombreEstablecimiento[j]);
                            } else {
                                if (opciones[i].equals("Ubicación")) {
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Ubicacion[j], 18));
                                    //Toast.makeText(getApplicationContext(), "Ubicación de este marcador", Toast.LENGTH_SHORT).show();
                                }else{
                                    dialogInterface.dismiss();
                                }
                            }
                         break;
                         }
                    }
                }
            });
            builder.show();
    }

}
