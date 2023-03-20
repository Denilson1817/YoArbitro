package com.denilsonperez.yoarbitro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MenuPrincipalActivity extends AppCompatActivity {
    Button cerrarSesion, generarCedula, cedulasGuardadas;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    TextView nombresPrincipal, correoPrincipal;
    ProgressBar progresoDatos;

    DatabaseReference Arbitros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("YoArbitro");

        nombresPrincipal = findViewById(R.id.nombresPrincipal);
        correoPrincipal = findViewById(R.id.correosPrincipal);
        progresoDatos = findViewById(R.id.progresoDatos);

        Arbitros = FirebaseDatabase.getInstance().getReference("Arbitros");
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        cerrarSesion = findViewById(R.id.btnCerrarSesion);
        generarCedula = findViewById(R.id.btnGenerarCedula);
        cedulasGuardadas = findViewById(R.id.btnCedulasGuardadas);

        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salirAplicacion();
            }
        });
        generarCedula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuPrincipalActivity.this, GenerarCedulaActivity.class));
            }
        });
        cedulasGuardadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuPrincipalActivity.this, CedulasGuardadasActivity.class));
            }
        });
    }
    private void salirAplicacion() {
        firebaseAuth.signOut();
        startActivity(new Intent(MenuPrincipalActivity.this, MainActivity.class));
        Toast.makeText(this, "Sesión finalizada", Toast.LENGTH_SHORT).show();
    }

    private void cargaDeDAtos(){
        Arbitros.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Si el usuario existe
                if(snapshot.exists()){
                    progresoDatos.setVisibility(View.GONE);
                    //Mostrar los texview
                    nombresPrincipal.setVisibility(View.VISIBLE);
                    correoPrincipal.setVisibility(View.VISIBLE);

                    //Obtener los datos de firebase
                    String nombres = ""+snapshot.child("nombres").getValue();
                    String correo = ""+snapshot.child("correo").getValue();

                    //Setear los datos en los respectivos textview.
                    nombresPrincipal.setText(nombres);
                    correoPrincipal.setText(correo);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void comprobarInicioSesion(){
        if(user!=null){
            //El usuario a iniciado sesión
            cargaDeDAtos();
        }else{
            startActivity(new Intent(MenuPrincipalActivity.this, MainActivity.class));
            finish();
        }
    }
    @Override
    protected void onStart() {
        comprobarInicioSesion();
        super.onStart();
    }
}