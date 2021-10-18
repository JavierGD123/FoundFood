package com.example.foundfood;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class EiminarMisProductosRequest extends StringRequest {

    private Map<String, String> parametros;

    public EiminarMisProductosRequest(String tipo, String idPro,String iduser,String img,String ruta, Response.Listener<String> listener){
        super(Method.POST, ruta+"/EliminaciondeCadaProducto.php", listener, null);
        parametros = new HashMap<>();
        parametros.put("idproducto",idPro);
        parametros.put("idusuario",iduser);
        parametros.put("tipo",tipo);
        parametros.put("img",img);
    }

    protected Map<String, String> getParams() {
        return parametros;
    }
}
