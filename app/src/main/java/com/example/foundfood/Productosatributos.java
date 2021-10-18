package com.example.foundfood;

public class Productosatributos {

    private String NombrePro;
    private String CostoPro;
    private int ImagensPro;

    public Productosatributos() {
    }

    public Productosatributos(String nombrePro, String costoPro, int imagensPro) {
        NombrePro = nombrePro;
        CostoPro = costoPro;
        ImagensPro = imagensPro;
    }

    public String getNombrePro() {
        return NombrePro;
    }

    public void setNombrePro(String nombrePro) {
        NombrePro = nombrePro;
    }

    public String getCostoPro() {
        return CostoPro;
    }

    public void setCostoPro(String costoPro) {
        CostoPro = costoPro;
    }

    public int getImagensPro() {
        return ImagensPro;
    }

    public void setImagensPro(int imagensPro) {
        ImagensPro = imagensPro;
    }
}
