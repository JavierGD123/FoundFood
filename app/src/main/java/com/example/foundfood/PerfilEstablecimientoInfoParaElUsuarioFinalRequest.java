package com.example.foundfood;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PerfilEstablecimientoInfoParaElUsuarioFinalRequest extends StringRequest {

    private Map<String, String> parametros;

    public PerfilEstablecimientoInfoParaElUsuarioFinalRequest(String Nombre,String ruta, Response.Listener<String> listener){
        super(Method.POST, ruta+"/ObtenciondeInfodecadaEsta.php", listener, null);
        parametros = new HashMap<>();
        parametros.put("nombreesta",Nombre);
    }

    protected Map<String, String> getParams() {
        return parametros;
    }
}
