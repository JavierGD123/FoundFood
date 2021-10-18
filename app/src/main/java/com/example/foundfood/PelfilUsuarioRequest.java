package com.example.foundfood;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PelfilUsuarioRequest extends StringRequest {

    private Map<String, String> parametros;

    public PelfilUsuarioRequest(String cargo,String iduser,String usuario,String contra,String email,String telefono,String ruta, Response.Listener<String> listener){
        super(Request.Method.POST, ruta+"/Consultar_y_ActualizarPerfilUsuario.php", listener, null);
        parametros = new HashMap<>();

        parametros.put("cargo",cargo);
        parametros.put("idusuario",iduser);

        parametros.put("usuario",usuario);
        parametros.put("contra",contra);

        parametros.put("email",email);
        parametros.put("telefono",telefono);
    }
    protected Map<String, String> getParams() {
        return parametros;
    }
}
