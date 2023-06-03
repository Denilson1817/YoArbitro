package com.denilsonperez.yoarbitro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
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
    boolean respuesta = false;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference parentRef = firebaseDatabase.getReference("Cedulas");
    @SuppressLint("MissingInflatedId")
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

        //recuperar los datos del form de datos del arbitro


        btnSiguiente.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                System.out.println("ulo"+nombreAsistente1);

                if (validarDatos()==true){
                    Intent intent = new Intent(DatosArbitrosActivity.this, DatosDelPartidoActivity.class);
                    intent.putExtra("idJuego",idJuego);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(DatosArbitrosActivity.this, "Debes proporcionar la información requerida para poder continuar", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpannableString tituloAdvertencia = new SpannableString("Advertencia");
                tituloAdvertencia.setSpan(new ForegroundColorSpan(Color.RED), 0, tituloAdvertencia.length(), 0);
                AlertDialog.Builder builder = new AlertDialog.Builder(DatosArbitrosActivity.this);
                builder.setTitle(tituloAdvertencia)
                        .setMessage("Al confirmar tu cancelación se perderá todo el progreso hasta el momento. ¿Desea continuar?")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Acciones a realizar cuando se hace clic en el botón Aceptar
                                Toast.makeText(DatosArbitrosActivity.this, "Ya vendran mejores momentos para crear una cédula arbitral", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(DatosArbitrosActivity.this, MenuPrincipalActivity.class));
                                finish();
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(DatosArbitrosActivity.this, "Siempre es buena idea no dejar las cosas para otro momento", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }
        });
    }

    private boolean validarDatos() {
        nombreCentral = nombreArbitroCentral.getText().toString();
        if (TextUtils.isEmpty(nombreCentral)) {
            Toast.makeText(this, "Es necesario ingresar el nombre del árbitro central", Toast.LENGTH_SHORT).show();
            respuesta =false;
        } else{
            subirdatos();
            respuesta=true;
        }
        return respuesta;
    }

    private void subirdatos() {
        nombreAsistente1 = nombreArbitroAsistente1.getText().toString();
        nombreAsistente2 = nombreArbitroAsistente2.getText().toString();
        parentRef.child(idJuego).child("Arbitros").child("Central").setValue(nombreCentral);


        if(TextUtils.isEmpty(nombreAsistente1) && TextUtils.isEmpty(nombreAsistente2)){

        } else{
            parentRef.child(idJuego).child("Arbitros").child("Asistente1").setValue(nombreAsistente1);
            parentRef.child(idJuego).child("Arbitros").child("Asistente2").setValue(nombreAsistente2);
        }

    }
}