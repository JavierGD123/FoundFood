package com.example.foundfood;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class ContinuarComo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continuar_como);

    }
    public void IniciarUsuario(View view){
        Intent  establecimiento = new Intent(this, loginusuario.class);


        startActivity(establecimiento);
    }
    public void IniciarVendedor(View view){
        Intent  establecimiento = new Intent(this, loginvendedor.class);
        startActivity(establecimiento);
    }

    public void MostrarDialogoOpciones(View view){
        final CharSequence[] opciones ={"Evaluar como usuario","Evaluar como vendedor","Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Encusta de evaluación\nElije una Opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for (int j = 0; j < 3; j++) {
                        if (opciones[i].equals("Evaluar como usuario")) {
                            //Llamado almetodo para activar camara
                            EvaluacionUsuario();
                        } else {
                            if (opciones[i].equals("Evaluar como vendedor")) {
                                EvaluacionVendedor();
                                //Toast.makeText(getApplicationContext(), "Ubicación de este marcador", Toast.LENGTH_SHORT).show();
                            }else{
                                dialogInterface.dismiss();
                            }
                        }
                        break;
                }
            }
        });
        builder.show();

}
    //Metodo para abrir referencia a las encuestas
    public void EvaluacionUsuario(){
        Uri url = Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLSdbg7Cp5uv4TB3dgZlxidXNZdXJDmkuLi5_DP9qLBmL9MDOXg/viewform?usp=sf_link");
        Intent coordenadas = new Intent(Intent.ACTION_VIEW, url);
        startActivity(coordenadas);
    }

    //Metodo para abrir referencia a las encuestas
    public void EvaluacionVendedor(){
        Uri url = Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLSfJvpOqyri8QOMbMRgXfYuCOAt-3QdR7MogQ_OP3rVakRBF5g/viewform?usp=sf_link");
        Intent coordenadas = new Intent(Intent.ACTION_VIEW, url);
        startActivity(coordenadas);
    }

    //Metodo para aviso de privacidad
    public void AvisoPrivacidad(View view){
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setTitle("Aviso de Privacidad FoundFood");
        alerta.setMessage("La aplicación FoundFood jamás hará mal uso de los datos que proporcione por este medio.\n" +
                "\n" +
                "Sus datos personales serán utilizados con el fin de mantenerlo informado acerca de nuevas promociones, " +
                "así como un mejor servicio mediante encuestas de satisfacción, enviadas por correo electrónico o número telefónico.\n")
                .setNegativeButton("Entendido", null).create().show();
    }
}
