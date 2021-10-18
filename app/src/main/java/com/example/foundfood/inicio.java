package com.example.foundfood;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.telecom.Connection;
import android.view.WindowManager;
import android.os.Handler;

import com.felipecsl.gifimageview.library.GifImageView;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class inicio extends Activity {
    private final int DURACION_SPLASH = 3000;
    private GifImageView imagen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_inicio);

        imagen = (GifImageView)findViewById(R.id.imgAnimacion);

        try {
            InputStream inputstream = getAssets().open("inicio.gif");
            byte[] bytes = IOUtils.toByteArray(inputstream);
            imagen.setBytes(bytes);
            imagen.startAnimation();
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Handler().postDelayed(new Runnable(){
            public void run(){
                iralogin();
            };

        }, DURACION_SPLASH);
    }

    private void iralogin(){
        Intent intent = new Intent(this, ContinuarComo.class);
        startActivity(intent);
        finish();
    }


}
