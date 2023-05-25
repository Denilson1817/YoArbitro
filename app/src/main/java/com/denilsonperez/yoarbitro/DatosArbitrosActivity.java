package com.denilsonperez.yoarbitro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DatosArbitrosActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    EditText nombreArbitroCentral, nombreArbitroAsistente1, nombreArbitroAsistente2;
    Button btnSiguiente, btnCancelar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_arbitros);
        drawerLayout = findViewById(R.id.drawerLayout);
        nombreArbitroCentral = findViewById(R.id.nombreArbitro);
        nombreArbitroAsistente1 = findViewById(R.id.nombreArbitroAsistente1);
        nombreArbitroAsistente2 = findViewById(R.id.nombreArbitroAsistente2);
        btnSiguiente = findViewById(R.id.btnSiguiente);
        btnCancelar = findViewById(R.id.btnCancelar);

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DatosArbitrosActivity.this, MenuPrincipalActivity.class));
                finish();
            }
        });
    }
}