package com.example.foundfood;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PerfilEstablecimientoEstatusRequest extends StringRequest {

    private Map<String, String> parametros;

    public PerfilEstablecimientoEstatusRequest(String Estatus, String MesasDisponibles,String iduser,String Disponible,String horario,String ruta,Response.Listener<String> listener){
        super(Method.POST, ruta+"/SubirEstatus.php", listener, null);
        parametros = new HashMap<>();
        parametros.put("estatusesta",Estatus);
        parametros.put("mesasesta",MesasDisponibles);
        parametros.put("iduser",iduser);
        parametros.put("disponible",Disponible);
        parametros.put("horario",horario);
    }

    protected Map<String, String> getParams() {
        return parametros;
    }
}
