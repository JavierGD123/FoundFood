package com.example.foundfood;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PerfilEstablecimientoProductosRequest extends StringRequest {

    private Map<String, String> parametros;

    public PerfilEstablecimientoProductosRequest(String TipoProducto, String Nombre, String Costo, String Img,String iduser,String tipopaquete, String Pagado,String ruta,Response.Listener<String> listener){
        super(Method.POST, ruta+"/SubirProductos.php", listener, null);
        parametros = new HashMap<>();
        parametros.put("tipoproducto",TipoProducto);
        parametros.put("nombreproducto",Nombre);
        parametros.put("costoproducto",Costo);
        parametros.put("iduser",iduser);
        parametros.put("nombreimg","img"+TipoProducto+iduser);
        parametros.put("foto",Img);
        parametros.put("tipopaquete",tipopaquete);
        parametros.put("pagado",Pagado);
    }

    protected Map<String, String> getParams() {
        return parametros;
    }
}
