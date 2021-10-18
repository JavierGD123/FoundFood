package com.example.foundfood;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ActualizarMisProductosRequest extends StringRequest {

    private Map<String, String> parametros;

    public ActualizarMisProductosRequest(String idPro, String iduser, String nombre, String costo, String img, String tipo,String ruta, Response.Listener<String> listener){
        super(Method.POST, ruta+"/Consultar_y_ActualizarMisProductos.php", listener, null);
        parametros = new HashMap<>();
        parametros.put("idproducto",idPro);
        parametros.put("idusuario",iduser);

        parametros.put("nombreproducto",nombre);
        parametros.put("costoproducto",costo);

        parametros.put("foto",img);
        parametros.put("nombreimg","img"+tipo+iduser+idPro);

        parametros.put("tipo",tipo);
    }

    protected Map<String, String> getParams() {
        return parametros;
    }
}
