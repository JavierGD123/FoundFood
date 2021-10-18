package com.example.foundfood;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PerfilEstablecimientoMisProductosRequest extends StringRequest {

    private Map<String, String> parametros;

    public PerfilEstablecimientoMisProductosRequest(String tipo, String NombreProducto,String ruta, Response.Listener<String> listener){
        super(Method.POST, ruta+"/ObtenciondeInfodeCadaProducto.php", listener, null);
        parametros = new HashMap<>();
        parametros.put("nombrepro",NombreProducto);
        parametros.put("tipopro",tipo);
    }

    protected Map<String, String> getParams() {
        return parametros;
    }
}
