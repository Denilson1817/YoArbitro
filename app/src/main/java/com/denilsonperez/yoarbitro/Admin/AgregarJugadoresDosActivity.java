package com.denilsonperez.yoarbitro.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.denilsonperez.yoarbitro.Inicio.IniciarSesionActivity;
import com.denilsonperez.yoarbitro.R;
import com.denilsonperez.yoarbitro.modelo.Jugador;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class AgregarJugadoresDosActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    FirebaseAuth firebaseAuth;
    Button btnRegistrarJugador, btnCancelar;
    EditText nombreJugadorEt, numeroJugadorEt;
    String nombreDeJugador="", numeroDeJugador="";

    Intent recibir;
    String uideEquipo;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_jugadores_dos);


        //jorge
        recibir = getIntent();
        uideEquipo = recibir.getStringExtra("UUID");
        Toast.makeText(AgregarJugadoresDosActivity.this, uideEquipo, Toast.LENGTH_SHORT).show();


        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.abrirNav, R.string.cerrarNav);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.bringToFront();
        firebaseAuth = FirebaseAuth.getInstance();
        btnCancelar = findViewById(R.id.btnCancelar);
        btnRegistrarJugador = findViewById(R.id.btnRegistrarJugador);
        nombreJugadorEt = findViewById(R.id.nombreJugador);
        numeroJugadorEt = findViewById(R.id.nomDeleEquipo);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home: {
                        startActivity(new Intent(AgregarJugadoresDosActivity.this, MenuPrincipalAdminActivity.class));
                        finish();
                        break;
                    }
                    case R.id.registrarEquipos:{
                        startActivity(new Intent(AgregarJugadoresDosActivity.this, RegistrarEquiposActivity.class));
                        finish();
                        break;
                    }
                    case R.id.consultarArbitros:{
                        startActivity(new Intent(AgregarJugadoresDosActivity.this, ConsultarArbitrosActivity.class));
                        finish();
                        break;
                    }
                    case R.id.consultarCedulas:{
                        startActivity(new Intent(AgregarJugadoresDosActivity.this, ConsultarCedulasActivity.class));
                        finish();
                        break;
                    }
                    case R.id.cerrarSesion:{
                        firebaseAuth.signOut();
                        startActivity(new Intent(AgregarJugadoresDosActivity.this, IniciarSesionActivity.class));
                        Toast.makeText(AgregarJugadoresDosActivity.this, "Sesión finalizada", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    }
                }
                return false;
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AgregarJugadoresDosActivity.this, MenuPrincipalAdminActivity.class));
                finish();
            }
        });
        btnRegistrarJugador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarDatos();
            }
        });
    }

    private void validarDatos() {
        nombreDeJugador = nombreJugadorEt.getText().toString();
        numeroDeJugador = numeroJugadorEt.getText().toString();
        if(TextUtils.isEmpty(nombreDeJugador)){
            Toast.makeText(this, "Ingrese nombre", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(numeroDeJugador)) {
            Toast.makeText(this, "Ingrese el numero del jugador", Toast.LENGTH_SHORT).show();
        }else{
            guardarInformacion();
        }
    }
    private void guardarInformacion() {

        Jugador datosJugador = new Jugador();
        datosJugador.setUid(UUID.randomUUID().toString());
        datosJugador.setNombre(nombreDeJugador);
        datosJugador.setNumero(numeroDeJugador);

        // jorge
        datosJugador.setIdEquipo(uideEquipo);


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Jugadores");
        databaseReference.child(datosJugador.getUid())
                .setValue(datosJugador)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AgregarJugadoresDosActivity.this, "Jugador registrado", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AgregarJugadoresDosActivity.this, AgregarJugadoresActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AgregarJugadoresDosActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return super.onSupportNavigateUp();
    }
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }
}