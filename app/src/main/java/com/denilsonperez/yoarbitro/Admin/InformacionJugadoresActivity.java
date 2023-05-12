package com.denilsonperez.yoarbitro.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.denilsonperez.yoarbitro.Inicio.IniciarSesionActivity;
import com.denilsonperez.yoarbitro.R;
import com.denilsonperez.yoarbitro.modelo.Equipo;
import com.denilsonperez.yoarbitro.modelo.Jugador;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InformacionJugadoresActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    EditText txtNombreJugador, txtNumeroJugador;
    //Variables para pasar y recibir datos del equipo
    Intent recibir;
    String nombreDeJugador, numDeJugador, idEquipo, idJugador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_jugadores);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.abrirNav, R.string.cerrarNav);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.bringToFront();
        firebaseAuth = FirebaseAuth.getInstance();
        txtNombreJugador = findViewById(R.id.txtNombreJugador);
        txtNumeroJugador = findViewById(R.id.txtNumeroJugador);
        inicializarFirebase();

        //Recibir datos del equipo en los EditText
        recibir = getIntent();
        nombreDeJugador = recibir.getStringExtra("nombreDeJugador");
        txtNombreJugador.setText(nombreDeJugador);
        numDeJugador = recibir.getStringExtra("numeroDeJugador");
        txtNumeroJugador.setText(numDeJugador);
        idEquipo = recibir.getStringExtra("idEquipo");
        idJugador = recibir.getStringExtra("idJugador");

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home: {
                        startActivity(new Intent(InformacionJugadoresActivity.this, MenuPrincipalAdminActivity.class));
                        finish();
                        break;
                    }
                    case R.id.registrarEquipos:{
                        startActivity(new Intent(InformacionJugadoresActivity.this, RegistrarEquiposActivity.class));
                        finish();
                        break;
                    }
                    case R.id.consultarArbitros:{
                        startActivity(new Intent(InformacionJugadoresActivity.this, ConsultarArbitrosActivity.class));
                        finish();
                        break;
                    }
                    case R.id.consultarCedulas:{
                        startActivity(new Intent(InformacionJugadoresActivity.this, ConsultarCedulasActivity.class));
                        finish();
                        break;
                    }
                    case R.id.cerrarSesion:{
                        firebaseAuth.signOut();
                        startActivity(new Intent(InformacionJugadoresActivity.this, IniciarSesionActivity.class));
                        Toast.makeText(InformacionJugadoresActivity.this, "Sesión finalizada", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    }
                }
                return false;
            }
        });
    }
    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        switch (item.getItemId()){
            case R.id.icon_actualizar:{
                Jugador jugador = new Jugador();
                jugador.setUid(idJugador);
                jugador.setIdEquipo(idEquipo);
                jugador.setNombre(txtNombreJugador.getText().toString().trim());
                jugador.setNumero(txtNumeroJugador.getText().toString().trim());
                databaseReference.child("Jugadores").child(idJugador).setValue(jugador);
                Toast.makeText(this, "Actualizado", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.icon_delete:{
                databaseReference.child("Jugadores").child(idJugador).removeValue();
                Toast.makeText(this, "Eliminado", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(InformacionJugadoresActivity.this, MenuPrincipalAdminActivity.class));
                finish();
                break;
            }
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_crud_equipos,menu);
        return super.onCreateOptionsMenu(menu);
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