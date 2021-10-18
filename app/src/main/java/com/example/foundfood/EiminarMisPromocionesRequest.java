package com.example.foundfood;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class EiminarMisPromocionesRequest extends StringRequest {

    private Map<String, String> parametros;

    public EiminarMisPromocionesRequest(String idPro, String iduser, String img,String ruta, Response.Listener<String> listener){
        super(Method.POST, ruta+"/EliminaciondeCadaPromocion.php", listener, null);
        parametros = new HashMap<>();
        parametros.put("idpromocion",idPro);
        parametros.put("idusuarioesta",iduser);
        parametros.put("img",img);
    }

    protected Map<String, String> getParams() {
        return parametros;
    }
}
