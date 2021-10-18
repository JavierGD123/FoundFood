package com.example.foundfood;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginVendedorRequest extends StringRequest {

    private Map<String, String> parametros;

    public LoginVendedorRequest(String usuario, String contra, String ruta,Response.Listener<String> listener){
        super(Request.Method.POST, ruta+"/LoginEstablecimiento.php", listener, null);
        parametros = new HashMap<>();
        parametros.put("usuario",usuario);
        parametros.put("contra",contra);
    }
    protected Map<String, String> getParams() {
        return parametros;
    }
}
