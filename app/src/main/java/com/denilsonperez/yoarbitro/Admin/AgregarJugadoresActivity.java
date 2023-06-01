package com.denilsonperez.yoarbitro.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.denilsonperez.yoarbitro.Inicio.IniciarSesionActivity;
import com.denilsonperez.yoarbitro.R;
import com.denilsonperez.yoarbitro.modelo.Jugador;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AgregarJugadoresActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    AdapterAgregarJugadores myAdapter;
    ActionBarDrawerToggle drawerToggle;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private List<Jugador> jugadorList = new ArrayList<>();
    ArrayAdapter<Jugador> jugadorArrayAdapter;
    RecyclerView listv_jugadores;
    Button btnAgregarJugadores;
    Intent recibir;
    String uideEquipo="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_jugadores);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.abrirNav, R.string.cerrarNav);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.bringToFront();
        btnAgregarJugadores = findViewById(R.id.btnAgregarJugadores);
        listv_jugadores = findViewById(R.id.listaJugadores);
        inicializarFirebase();
        listarDatos();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home: {
                        startActivity(new Intent(AgregarJugadoresActivity.this, MenuPrincipalAdminActivity.class));
                        finish();
                        break;
                    }
                    case R.id.registrarEquipos:{
                        startActivity(new Intent(AgregarJugadoresActivity.this, RegistrarEquiposActivity.class));
                        finish();
                        break;
                    }
                    case R.id.consultarArbitros:{
                        startActivity(new Intent(AgregarJugadoresActivity.this, ConsultarArbitrosActivity.class));
                        finish();
                        break;
                    }
                    case R.id.consultarCedulas:{
                        startActivity(new Intent(AgregarJugadoresActivity.this, ConsultarCedulasActivity.class));
                        finish();
                        break;
                    }
                    case R.id.cerrarSesion:{
                        firebaseAuth.signOut();
                        startActivity(new Intent(AgregarJugadoresActivity.this, IniciarSesionActivity.class));
                        Toast.makeText(AgregarJugadoresActivity.this, "Sesión finalizada", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    }
                }
                return false;
            }
        });
        btnAgregarJugadores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AgregarJugadoresActivity.this, AgregarJugadoresDosActivity.class );
                intent.putExtra("UUID",uideEquipo);
                startActivity(intent);
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
                SpannableString tituloAdvertencia = new SpannableString("El equipo se creo correctamente");
                tituloAdvertencia.setSpan(new ForegroundColorSpan(Color.BLUE), 0, tituloAdvertencia.length(), 0);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(tituloAdvertencia)
                        .setMessage("Sino terminaste de registrar todos los jugadores, en el menu principal puedes seguir agregando mas jugadores . ¿Desea continuar?")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Acciones a realizar cuando se hace clic en el botón Aceptar
                                Intent intent = new Intent(AgregarJugadoresActivity.this, MenuPrincipalAdminActivity.class );
                                Toast.makeText(AgregarJugadoresActivity.this, "El equipo a sido registrado", Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(AgregarJugadoresActivity.this, "Acción cancelada", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();


                break;
            }
            
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_agregar_equipo,menu);
        return super.onCreateOptionsMenu(menu);
    }
    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
    private void listarDatos() {
        //jorge
        recibir = getIntent();
        uideEquipo = recibir.getStringExtra("UUID");
        databaseReference.child("Jugadores").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jugadorList.clear();
                for (DataSnapshot objSnapshot : snapshot.getChildren()){
                    Jugador jugador = objSnapshot.getValue(Jugador.class);
                    if (uideEquipo.equals(jugador.getIdEquipo())){
                        jugadorList.add(jugador);
                    }
                    myAdapter = new AdapterAgregarJugadores(jugadorList, AgregarJugadoresActivity.this,AgregarJugadoresActivity.this);
                    listv_jugadores.setAdapter(myAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
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