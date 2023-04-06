package com.denilsonperez.yoarbitro.Inicio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.denilsonperez.yoarbitro.R;

public class PantallaDeCargaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_de_carga);

        int tiempoDeCarga = 3000;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(PantallaDeCargaActivity.this, IniciarSesionActivity.class));
                finish();
            }
        }, tiempoDeCarga);
    }
}