package com.example.foundfood;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegistroEstablecimientoRequest extends StringRequest {

    private Map<String, String> parametros;

    public RegistroEstablecimientoRequest(String Nombre,String Usuario, String Contra, String Email,String Telefono,String Latitud,String Longitud,String url, Response.Listener<String> listener){
        super(Request.Method.POST, url+"/RegistroVendedor.php", listener, null);
        parametros = new HashMap<>();
        parametros.put("nombre", Nombre);
        parametros.put("usuario",Usuario);
        parametros.put("contra",Contra);
        parametros.put("email",Email);

        parametros.put("numerotel",Telefono);

        parametros.put("latitud",Latitud);
        parametros.put("longitud",Longitud);
    }

    protected Map<String, String> getParams() {
        return parametros;
    }
}
