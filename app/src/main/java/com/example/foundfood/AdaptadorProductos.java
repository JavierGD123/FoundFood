package com.example.foundfood;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdaptadorProductos extends RecyclerView.Adapter<AdaptadorProductos.ViewhoslderProductos> {

    ArrayList<Productosatributos> listaproductos;

    public AdaptadorProductos(ArrayList<Productosatributos> listaproductos) {
        this.listaproductos = listaproductos;
    }

    @Override
    public ViewhoslderProductos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.elemento_lista,null,false);
        return new ViewhoslderProductos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewhoslderProductos holder, int position) {
        holder.etiNombre.setText(listaproductos.get(position).getNombrePro());
        holder.etiCosto.setText(listaproductos.get(position).getCostoPro());
        holder.etiImagen.setImageResource(listaproductos.get(position).getImagensPro());
    }

    @Override
    public int getItemCount() {
        return listaproductos.size();
    }

    public class ViewhoslderProductos extends RecyclerView.ViewHolder {
            TextView etiNombre,etiCosto;
            ImageView etiImagen;

        public ViewhoslderProductos(@NonNull View itemView) {
            super(itemView);
            etiNombre = (TextView) itemView.findViewById(R.id.txtTNombreProduct);
            etiCosto = (TextView) itemView.findViewById(R.id.txtCostoProduct);
            etiImagen = (ImageView) itemView.findViewById(R.id.imgVistaPromo);

        }
    }
}
