package com.example.foundfood;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegistroUsuarioRequest extends StringRequest {
    //private static final String ruta = "https://foundfood2019.000webhostapp.com/app";
    private Map<String, String> parametros;

    public RegistroUsuarioRequest(String Usuario, String Contra, String Email,String Telefono,String ruta, Response.Listener<String> listener){
        super(Request.Method.POST, ruta+"/RegistroUsuarios.php", listener, null);
        parametros = new HashMap<>();
        parametros.put("usuario",Usuario);
        parametros.put("contra",Contra);
        parametros.put("email",Email);
        parametros.put("numerotel",Telefono);
        parametros.put("img","");


    }

    protected Map<String, String> getParams() {
        return parametros;
    }
}
