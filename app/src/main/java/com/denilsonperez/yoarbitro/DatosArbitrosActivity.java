package com.denilsonperez.yoarbitro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatosArbitrosActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    EditText nombreArbitroCentral, nombreArbitroAsistente1, nombreArbitroAsistente2;
    Button btnSiguiente, btnCancelar;
    String nombreCentral, nombreAsistente1, nombreAsistente2 ,idJuego;
    Intent recibir;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference parentRef = firebaseDatabase.getReference("Cedulas");
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

        //recibir el id de la cedula
        recibir = getIntent();
        idJuego = recibir.getStringExtra("idJuego");

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarDatos();
                Intent intent = new Intent(DatosArbitrosActivity.this, DatosDelPartidoActivity.class);
                intent.putExtra("idJuego",idJuego);
                startActivity(intent);
                finish();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DatosArbitrosActivity.this, MenuPrincipalActivity.class));
                finish();
            }
        });
    }

    private void validarDatos() {
        nombreCentral = nombreArbitroCentral.getText().toString();
        if(TextUtils.isEmpty(nombreCentral)){
            Toast.makeText(this, "ingrese un arbitro central", Toast.LENGTH_SHORT).show();
        }else{
            subirdatos();
        }
    }

    private void subirdatos() {
        parentRef.child(idJuego).child("Arbitros").child("Central").setValue(nombreCentral);

        nombreAsistente1 = nombreArbitroAsistente1.getText().toString();
        nombreAsistente2 = nombreArbitroAsistente2.getText().toString();
        if(TextUtils.isEmpty(nombreAsistente1) && TextUtils.isEmpty(nombreAsistente2)){

        } else{
            parentRef.child(idJuego).child("Arbitros").child("Asistente 1").setValue(nombreAsistente1);
            parentRef.child(idJuego).child("Arbitros").child("Asistente 2").setValue(nombreAsistente2);
        }

    }
}