package com.example.foundfood;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PerfilEstablecimientoRequest extends StringRequest {
    private Map<String, String> parametros;

    private static final String archivo = "/Consultar_y_ActualizarPerfilEstablecimiento.php";

    public PerfilEstablecimientoRequest(String cargo,String iduser,String nombre,String usuario,String contra,String email,String telefono,String imagen,String ruta, Response.Listener<String> listener){
        super(Request.Method.POST, ruta+archivo, listener, null);
        parametros = new HashMap<>();
        parametros.put("cargo",cargo);
        parametros.put("idusuario",iduser);

        parametros.put("nombre",nombre);
        parametros.put("usuario",usuario);

        parametros.put("contra",contra);
        parametros.put("email",email);

        parametros.put("telefono",telefono);

        parametros.put("foto",imagen);
        parametros.put("nombreimg","imgLogo"+iduser);
    }

    protected Map<String, String> getParams() {
        return parametros;
    }
}
