package com.denilsonperez.yoarbitro.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.denilsonperez.yoarbitro.AdapterAdminEquipos;
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

import java.util.HashSet;

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

    private HashSet<String> numerosJugadores;
    private boolean toastMostrado = false; // Variable para controlar si el Toast se mostró


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
        numerosJugadores = new HashSet<>();
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
                String numeroJugador =txtNumeroJugador.getText().toString();
                //para recuperar todos los numeros existentes
                databaseReference.child("Jugadores").addValueEventListener(new ValueEventListener()  {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //listaEquipos.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Jugador jugador = dataSnapshot.getValue(Jugador.class);
                            if (idEquipo.equals(jugador.getIdEquipo())){
                                numerosJugadores.add(jugador.getNumero().toString());
                            }
                        }
                        numerosJugadores.remove(numDeJugador);
                        if (!numerosJugadores.contains(numeroJugador)) {

                            jugador.setNombre(txtNombreJugador.getText().toString().trim());
                            jugador.setNumero(txtNumeroJugador.getText().toString().trim());
                            databaseReference.child("Jugadores").child(idJugador).setValue(jugador);
                            if (!toastMostrado) {
                                Toast.makeText(InformacionJugadoresActivity.this, "Datos del jugador actualizados correctamente", Toast.LENGTH_SHORT).show();
                                toastMostrado = true; // Marcar el Toast como mostrado
                            }
                            finish();

                        } else {
                            if (!toastMostrado) {
                                Toast.makeText(InformacionJugadoresActivity.this, "El número elejido ya le pertenece a otro jugador", Toast.LENGTH_SHORT).show();
                                toastMostrado = true; // Marcar el Toast como mostrado
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        System.out.println("no hay datos");
                    }
                });

                break;
            }
            case R.id.icon_delete:{

                SpannableString tituloAdvertencia = new SpannableString("Advertencia");
                tituloAdvertencia.setSpan(new ForegroundColorSpan(Color.RED), 0, tituloAdvertencia.length(), 0);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(tituloAdvertencia)
                        .setMessage("Eliminar este Jugador borrará permanentemente toda la información asociada. ¿Desea continuar?")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Acciones a realizar cuando se hace clic en el botón Aceptar
                                databaseReference.child("Jugadores").child(idJugador).removeValue();
                                finish();
                                Toast.makeText(InformacionJugadoresActivity.this, "Jugador eliminado correctamente", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(InformacionJugadoresActivity.this, "Acción cancelada", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                break;
            }
            case R.id.icon_cancel:{
                finish();

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