package com.example.foundfood;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PerfilEstablecimientoPromocionesRequest extends StringRequest {

    private Map<String, String> parametros;

    public PerfilEstablecimientoPromocionesRequest(String iduser, String nombresta, String nombrepromo, String dia,String imagen,String paquete,String pagado,String ruta, Response.Listener<String> listener){
        super(Method.POST, ruta+"/SubirPromociones.php", listener, null);
        parametros = new HashMap<>();
        parametros.put("iduser",iduser);
        parametros.put("nombreesta",nombresta);
        parametros.put("nombrepromo",nombrepromo);
        parametros.put("dia",dia);
        parametros.put("nombreimg","imgPromo"+iduser);
        parametros.put("foto",imagen);
        parametros.put("tipopaquete",paquete);
        parametros.put("pagado",pagado);
    }

    protected Map<String, String> getParams() {
        return parametros;
    }
}
