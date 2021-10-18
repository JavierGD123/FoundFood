package com.example.foundfood;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PerfilEstablecimientoMisPromocionesRequest extends StringRequest {

    private Map<String, String> parametros;

    public PerfilEstablecimientoMisPromocionesRequest(String NombreProducto,String ruta, Response.Listener<String> listener){
        super(Method.POST, ruta+"/ObtenciondeInfodeCadaPromocionponombre.php", listener, null);
        parametros = new HashMap<>();
        parametros.put("nombrepro",NombreProducto);
    }

    protected Map<String, String> getParams() {
        return parametros;
    }
}
