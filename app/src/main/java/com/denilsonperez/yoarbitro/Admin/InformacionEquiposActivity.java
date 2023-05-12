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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.denilsonperez.yoarbitro.Inicio.IniciarSesionActivity;
import com.denilsonperez.yoarbitro.R;
import com.denilsonperez.yoarbitro.modelo.Equipo;
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

public class InformacionEquiposActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private List<Jugador> jugadorList = new ArrayList<>();
    ArrayAdapter<Jugador> jugadorArrayAdapter;
    ListView listv_jugadores;
    EditText txtNombreEquipo, txtNombreDelegado, txtNumeroDeContacto;
    Button btnAgregarJugadores;
    Intent recibir, enviar;
    String nombreDeEquipo="", nombreDeDelegado, numDeContacto, idEquipo;
   //String uideEquipo="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_equipos);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.abrirNav, R.string.cerrarNav);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.bringToFront();
        firebaseAuth = FirebaseAuth.getInstance();
        txtNombreEquipo = findViewById(R.id.txtNombreEquipo);
        txtNombreDelegado = findViewById(R.id.txtNombreDelegado);
        txtNumeroDeContacto = findViewById(R.id.txtNumeroDeContacto);
        listv_jugadores = findViewById(R.id.listaJugadores);
        btnAgregarJugadores = findViewById(R.id.btnAgregarJugadores);
        //Para el manejo de datos en firebase
        FirebaseDatabase firebaseDataBase;
        DatabaseReference databaseReference;
        Equipo equipoSeleccionado;
        inicializarFirebase();
        listarDatos();

        //Recibir datos del equipo en los EditText
        recibir = getIntent();
        nombreDeEquipo = recibir.getStringExtra("nombreEquipo");
        txtNombreEquipo.setText(nombreDeEquipo);
        nombreDeDelegado = recibir.getStringExtra("nombreDelegado");
        txtNombreDelegado.setText(nombreDeDelegado);
        numDeContacto = recibir.getStringExtra("numDeContacto");
        txtNumeroDeContacto.setText(numDeContacto);
        idEquipo = recibir.getStringExtra("idEquipo");

        Toast.makeText(InformacionEquiposActivity.this, idEquipo, Toast.LENGTH_SHORT).show();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home: {
                        startActivity(new Intent(InformacionEquiposActivity.this, MenuPrincipalAdminActivity.class));
                        finish();
                        break;
                    }
                    case R.id.registrarEquipos:{
                        startActivity(new Intent(InformacionEquiposActivity.this, RegistrarEquiposActivity.class));
                        finish();
                        break;
                    }
                    case R.id.consultarArbitros:{
                        startActivity(new Intent(InformacionEquiposActivity.this, ConsultarArbitrosActivity.class));
                        finish();
                        break;
                    }
                    case R.id.consultarCedulas:{
                        startActivity(new Intent(InformacionEquiposActivity.this, ConsultarCedulasActivity.class));
                        finish();
                        break;
                    }
                    case R.id.cerrarSesion:{
                        firebaseAuth.signOut();
                        startActivity(new Intent(InformacionEquiposActivity.this, IniciarSesionActivity.class));
                        Toast.makeText(InformacionEquiposActivity.this, "Sesión finalizada", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(InformacionEquiposActivity.this, JugadoresActivity.class );
                // Jorge
                intent.putExtra("UUID",idEquipo);
                startActivity(intent);
            }
        });
    }
    private void listarDatos() {
        databaseReference.child("Jugadores").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jugadorList.clear();
                for (DataSnapshot objSnapshot : snapshot.getChildren()){
                    Jugador jugador = objSnapshot.getValue(Jugador.class);
                    System.out.println(idEquipo);
                    System.out.println(jugador.getIdEquipo());
                    if (idEquipo.equals(jugador.getIdEquipo())){
                        jugadorList.add(jugador);
                    }
                    jugadorArrayAdapter = new ArrayAdapter<Jugador>(InformacionEquiposActivity.this, android.R.layout.simple_list_item_activated_1, jugadorList);
                    listv_jugadores.setAdapter(jugadorArrayAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
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
                Equipo equipo = new Equipo();
                equipo.setUid(idEquipo);
                equipo.setNombre(txtNombreEquipo.getText().toString().trim());
                equipo.setDelegado(txtNombreDelegado.getText().toString().trim());
                equipo.setNumContacto(txtNumeroDeContacto.getText().toString().trim());
                databaseReference.child("Equipos").child(idEquipo).setValue(equipo);
                Toast.makeText(this, "Actualizado", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.icon_delete:{
                Equipo equipo = new Equipo();
                databaseReference.child("Equipos").child(idEquipo).removeValue();
                Toast.makeText(this, "Eliminado", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(InformacionEquiposActivity.this, MenuPrincipalAdminActivity.class));
                finish();
                break;
            }
            default:break;
        }
        //return true;
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