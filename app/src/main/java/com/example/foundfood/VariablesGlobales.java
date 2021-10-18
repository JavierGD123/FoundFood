
package com.example.foundfood;

public class VariablesGlobales {

   private String URL = "https://foundfood2019.000webhostapp.com/app";
    //private String URL = "http://foundfood.xyz/app";
    private String Sencillo = "https://www.mercadopago.com.mx/checkout/v1/redirect?pref_id=39654980-1686e956-d60d-47ce-9ae0-810f9f99dc39";
    private String Avanzado = "https://www.mercadopago.com.mx/checkout/v1/redirect?pref_id=39654980-983c3364-1be9-462f-a916-3b13ad1e8be5";
    private String Premium = "https://www.mercadopago.com.mx/checkout/v1/redirect?pref_id=39654980-c7b5360e-e00a-40b6-95d6-d2fc6b8810ff";


    public VariablesGlobales() {

    }

    public String getSencillo() {
        return Sencillo;
    }

    public String getAvanzado() {
        return Avanzado;
    }

    public String getPremium() {
        return Premium;
    }

    public String getURL() {
        return URL;
    }
}
