package com.example.foundfood;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PerfilEstablecimientoPaqueteRequest extends StringRequest {

    //private String URL = "http://foundfood.xyz/app";
   // private static final String ruta = "http://foundfood.xyz/app/Obtenciondepaquete.php";
    private static final String ruta = "https://foundfood2019.000webhostapp.com/app/Obtenciondepaquete.php";
    //private static final String ruta = "http://foundfood.xyz/app";
    private Map<String, String> parametros;

    public PerfilEstablecimientoPaqueteRequest(String iduser,String pagado,Response.Listener<String> listener){
        super(Method.POST, ruta, listener, null);
        parametros = new HashMap<>();
        parametros.put("iduser",iduser);
        parametros.put("pagado",pagado);
    }

    protected Map<String, String> getParams() {
        return parametros;
    }
}
