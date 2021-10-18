package com.example.foundfood;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ActualizarMisPromocionesRequest extends StringRequest {

    private Map<String, String> parametros;

    public ActualizarMisPromocionesRequest(String idPro, String iduser, String nombre, String dia, String img,String ruta, Response.Listener<String> listener){
        super(Method.POST, ruta+"/ActualizarMisPromociones.php", listener, null);
        parametros = new HashMap<>();
        parametros.put("idpromocion",idPro);
        parametros.put("idusuario",iduser);

        parametros.put("nombreproducto",nombre);
        parametros.put("dia",dia);

        parametros.put("foto",img);
        parametros.put("nombreimg","imgPromo"+iduser+idPro);
    }

    protected Map<String, String> getParams() {
        return parametros;
    }
}
