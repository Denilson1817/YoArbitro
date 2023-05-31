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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.denilsonperez.yoarbitro.CedulasGuardadasActivity;
import com.denilsonperez.yoarbitro.Inicio.IniciarSesionActivity;
import com.denilsonperez.yoarbitro.MenuPrincipalActivity;
import com.denilsonperez.yoarbitro.R;
import com.denilsonperez.yoarbitro.modelo.Equipo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class RegistrarEquiposActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    EditText nombreEqui, nomDeleg, numeroCont;
    Button btnSiguiente, btnCancelar;
    FirebaseAuth firebaseAuth;
    String nombre = "", delegado ="", numero="";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_equipos);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.abrirNav, R.string.cerrarNav);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.bringToFront();
        // Inicializar vistas
        nombreEqui = findViewById(R.id.nombreEquipo);
        nomDeleg = findViewById(R.id.nomDeleEquipo);
        numeroCont = findViewById(R.id.numDelContacto);
        //btnSiguiente = findViewById(R.id.btnRegistrarEquipos);
        btnCancelar = findViewById(R.id.btnCancelar);
        firebaseAuth = FirebaseAuth.getInstance();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home: {
                        startActivity(new Intent(RegistrarEquiposActivity.this, MenuPrincipalAdminActivity.class));
                        finish();
                        break;
                    }
                    case R.id.consultarCedulas:{
                        startActivity(new Intent(RegistrarEquiposActivity.this, ConsultarCedulasActivity.class));
                        finish();
                        break;
                    }
                    case R.id.registrarEquipos:{
                        startActivity(new Intent(RegistrarEquiposActivity.this, RegistrarEquiposActivity.class));
                        finish();
                        break;
                    }
                    case R.id.consultarArbitros:{
                        startActivity(new Intent(RegistrarEquiposActivity.this, ConsultarArbitrosActivity.class));
                        finish();
                        break;
                    }
                    case R.id.cerrarSesion:{
                        firebaseAuth.signOut();
                        startActivity(new Intent(RegistrarEquiposActivity.this, IniciarSesionActivity.class));
                        Toast.makeText(RegistrarEquiposActivity.this, "Sesión finalizada", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    }
                }
                return false;
            }
        });



    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        switch (item.getItemId()){
            case R.id.icon_agregar:{
                validarDatos();

                break;
            }

            case R.id.icon_cancelar:{
                startActivity(new Intent(RegistrarEquiposActivity.this, MenuPrincipalAdminActivity.class));
                finish();
            }
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_agregar_jugador,menu);
        return super.onCreateOptionsMenu(menu);
    }

        public void validarDatos(){
            nombre = nombreEqui.getText().toString();
            delegado = nomDeleg.getText().toString();
            numero = numeroCont.getText().toString();

            if(TextUtils.isEmpty(nombre)){
                Toast.makeText(this, "Ingrese nombre", Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(delegado)){
                Toast.makeText(this, "Ingrese nombre del delegado", Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(numero)){
                Toast.makeText(this, "Ingrese numero de contacto", Toast.LENGTH_SHORT).show();
            }else{
                guardarInformacion();
            }
        }

    private void guardarInformacion() {

            Equipo datos = new Equipo();
            datos.setUid(UUID.randomUUID().toString());
            datos.setNombre(nombre);
            datos.setDelegado(delegado);
            datos.setNumContacto(numero);
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Equipos");
            databaseReference.child(datos.getUid())
                    .setValue(datos)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(RegistrarEquiposActivity.this, "Equipo registrado", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegistrarEquiposActivity.this, AgregarJugadoresActivity.class );
                            intent.putExtra("UUID", datos.getUid());
                            startActivity(intent);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegistrarEquiposActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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
