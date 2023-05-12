package com.denilsonperez.yoarbitro.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
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

public class JugadoresActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    FirebaseAuth firebaseAuth;
    Button btnRegistrarJugador, btnCancelar;
    EditText nombreJugadorEt, numeroJugadorEt;
    String nombreDeJugador="", numeroDeJugador="";
    //Variables para recibir datos
    Intent recibir;
    String nombreDeEquipo="", nombreDeDelegado, numDeContacto, idEquipo;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jugadores);

        //Recibir id para agregar jugador
        recibir = getIntent();
        idEquipo = recibir.getStringExtra("idEquipo");
        nombreDeEquipo = recibir.getStringExtra("nombreEquipo");
        nombreDeDelegado = recibir.getStringExtra("nombreDelegado");
        numDeContacto = recibir.getStringExtra("numDeContacto");

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
        numeroJugadorEt = findViewById(R.id.numDeJugador);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home: {
                        startActivity(new Intent(JugadoresActivity.this, MenuPrincipalAdminActivity.class));
                        finish();
                        break;
                    }
                    case R.id.registrarEquipos:{
                        startActivity(new Intent(JugadoresActivity.this, RegistrarEquiposActivity.class));
                        finish();
                        break;
                    }
                    case R.id.consultarArbitros:{
                        startActivity(new Intent(JugadoresActivity.this, ConsultarArbitrosActivity.class));
                        finish();
                        break;
                    }
                    case R.id.consultarCedulas:{
                        startActivity(new Intent(JugadoresActivity.this, ConsultarCedulasActivity.class));
                        finish();
                        break;
                    }
                    case R.id.cerrarSesion:{
                        firebaseAuth.signOut();
                        startActivity(new Intent(JugadoresActivity.this, IniciarSesionActivity.class));
                        Toast.makeText(JugadoresActivity.this, "Sesión finalizada", Toast.LENGTH_SHORT).show();
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
                startActivity(new Intent(JugadoresActivity.this, InformacionEquiposActivity.class));
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
        datosJugador.setIdEquipo(idEquipo);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Jugadores");
        databaseReference.child(datosJugador.getUid())
                .setValue(datosJugador)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(JugadoresActivity.this, "Jugador registrado", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(JugadoresActivity.this, InformacionEquiposActivity.class );
                        //Enviar datos del equipo
                        intent.putExtra("nombreEquipo",nombreDeEquipo);
                        intent.putExtra("nombreDelegado",nombreDeDelegado);
                        intent.putExtra("numDeContacto",numDeContacto);
                        intent.putExtra("idEquipo",idEquipo);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(JugadoresActivity.this, "Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
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