package com.example.foundfood;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Adaptador extends BaseAdapter {

    private static LayoutInflater inflater = null;

    Context contexto;
    String[][] datos;
    int[] datosImg;
   /* Bitmap imagenes[];*/

    public Adaptador(Context contexto, String[][] datos, int[] datosImg/*, Bitmap imagenes[]*/) {
        this.contexto = contexto;
        this.datos = datos;
        this.datosImg = datosImg;
       /* this.imagenes = imagenes;*/

        inflater = (LayoutInflater)contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final View vista = inflater.inflate(R.layout.elemento_lista, null);

        TextView titulo = (TextView) vista.findViewById(R.id.txtTNombreProduct);
        TextView nombre = (TextView) vista.findViewById(R.id.txtCostoProduct);
       // TextView director = (TextView) vista.findViewById(R.id.tvDirector);

        ImageView imagen = (ImageView) vista.findViewById(R.id.imgVistaPromo);
       // RatingBar calificacion = (RatingBar) vista.findViewById(R.id.ratingBarPel);

        titulo.setText(datos[i][0]);
        nombre.setText(datos[i][1]);
       // duracion.setText("Duración " + datos[i][2]);
        //imagen.setImageBitmap(imagenes[i]);
        imagen.setImageResource(datosImg[i]);
        //calificacion.setProgress(Integer.valueOf(datos[i][3]));

        //imagen.setImageBitmap(imagenes[i]);
        imagen.setTag(i);

       imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent visorImagen = new Intent(contexto, VariablesGlobales.class);
                //visorImagen.putExtra("IMG", imagenes(Integer)v.);
                visorImagen.putExtra("IMG", datosImg[(Integer)v.getTag()]);
                contexto.startActivity(visorImagen);
            }
        });


        return vista;
        //return null;
    }

    @Override
    public int getCount() {
        return datosImg.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


}
