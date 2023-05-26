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

public class DatosDelPartidoActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    EditText txtCampo, txtFecha, txtHora;
    Button btnSiguiente, btnCancelar;
    String nombreCampo, fecha, hora, idJuego;
    Intent recibir;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference parentRef = firebaseDatabase.getReference("Cedulas");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_del_partido);
        drawerLayout = findViewById(R.id.drawerLayout);
        txtCampo = findViewById(R.id.txtCampo);
        txtFecha = findViewById(R.id.txtFecha);
        txtHora = findViewById(R.id.txtHora);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnSiguiente = findViewById(R.id.btnSiguiente);

        //recibir el id de la cedula
        recibir = getIntent();
        idJuego = recibir.getStringExtra("idJuego");
        System.out.println("ESTE ES EL ID DEL JUEGO: "+idJuego);

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarDatos();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DatosDelPartidoActivity.this, MenuPrincipalActivity.class));
                finish();
            }
        });
    }

    private void validarDatos() {
        nombreCampo = txtCampo.getText().toString();
        fecha = txtFecha.getText().toString();
        hora = txtHora.getText().toString();
        if(TextUtils.isEmpty(nombreCampo)){
            Toast.makeText(this, "ingrese el nombre del campo", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(fecha)) {
            Toast.makeText(this, "ingresa una fecha", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(hora)) {
            Toast.makeText(this, "ingresa una hora", Toast.LENGTH_SHORT).show();
        }else{
            subirDatos();
        }
    }

    private void subirDatos() {
        parentRef.child(idJuego).child("DatosPartido").child("Campo").setValue(nombreCampo);
        parentRef.child(idJuego).child("DatosPartido").child("Fecha").setValue(fecha);
        parentRef.child(idJuego).child("DatosPartido").child("Hora").setValue(hora);
    }
}