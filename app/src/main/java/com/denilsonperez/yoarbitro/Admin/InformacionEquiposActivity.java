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
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
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
    AdapterJugadores myAdapter;
    RecyclerView rvDatosJugadores;
    private List<Jugador> jugadorList = new ArrayList<>();
    ArrayAdapter<Jugador> jugadorArrayAdapter;
    ListView listv_jugadores;
    EditText txtNombreEquipo, txtNombreDelegado, txtNumeroDeContacto;
    Button btnAgregarJugadores;
    private int selectedItemPosition = -1;
    Jugador jugadorSeleccionado;
    //Variables para pasar y recibir datos del equipo
    Intent recibir;
    String nombreDeEquipo="", nombreDeDelegado, numDeContacto, idEquipo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_equipos);
        drawerLayout = findViewById(R.id.drawerLayout);
        rvDatosJugadores = findViewById(R.id.rv_DatosJugadores);
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
        //listv_jugadores = findViewById(R.id.listaJugadores);
        btnAgregarJugadores = findViewById(R.id.btnAgregarJgador);
        inicializarFirebase();

        //Recibir datos del equipo en los EditText
        recibir = getIntent();
        nombreDeEquipo = recibir.getStringExtra("nombreEquipo");
        txtNombreEquipo.setText(nombreDeEquipo);
        nombreDeDelegado = recibir.getStringExtra("nombreDelegado");
        txtNombreDelegado.setText(nombreDeDelegado);
        numDeContacto = recibir.getStringExtra("numDeContacto");
        txtNumeroDeContacto.setText(numDeContacto);
         idEquipo = recibir.getStringExtra("idEquipo");

        listarDatos();

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
                //Enviar datos del equipo
                intent.putExtra("nombreEquipo",nombreDeEquipo);
                intent.putExtra("nombreDelegado",nombreDeDelegado);
                intent.putExtra("numDeContacto",numDeContacto);
                intent.putExtra("idEquipo",idEquipo);
                startActivity(intent);
            }
        });
    }
    public void listarDatos(){
        //List<Equipo> myList = new ArrayList<>();

        databaseReference.child("Jugadores").addValueEventListener(new ValueEventListener()  {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jugadorList.clear();
                for (DataSnapshot objSnapshot : snapshot.getChildren()){
                    Jugador jugador = objSnapshot.getValue(Jugador.class);

                    if (idEquipo.equals(jugador.getIdEquipo())){
                        jugadorList.add(jugador);
                    }
                    myAdapter = new AdapterJugadores(jugadorList, InformacionEquiposActivity.this,InformacionEquiposActivity.this);
                    rvDatosJugadores.setAdapter(myAdapter);
                }
                // Llena el RecyclerView con los datos obtenidos
                //myAdapter = new AdapterJugadores(jugadorList, InformacionEquiposActivity.this,InformacionEquiposActivity.this);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("no hay datos");
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

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Confirmación")
                        .setMessage("Estas a punto de actualizar el equipo "+equipo.getNombre()+" ¿Desea continuar?")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Acciones a realizar cuando se hace clic en el botón Aceptar
                                databaseReference.child("Equipos").child(idEquipo).setValue(equipo);
                                Toast.makeText(InformacionEquiposActivity.this, "¡Tu equipo se actualizo correctamente!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(InformacionEquiposActivity.this, MenuPrincipalAdminActivity.class));
                                finish();
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(InformacionEquiposActivity.this, "Acción cancelada", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                break;
            }
            case R.id.icon_delete:{
                SpannableString tituloAdvertencia = new SpannableString("Advertencia");
                tituloAdvertencia.setSpan(new ForegroundColorSpan(Color.RED), 0, tituloAdvertencia.length(), 0);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(tituloAdvertencia)
                        .setMessage("Eliminar este equipo borrará permanentemente toda la información asociada. ¿Desea continuar?")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Acciones a realizar cuando se hace clic en el botón Aceptar
                                databaseReference.child("Equipos").child(idEquipo).removeValue();
                                Toast.makeText(InformacionEquiposActivity.this, "Equipo eliminado correctamente", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(InformacionEquiposActivity.this, MenuPrincipalAdminActivity.class));
                                finish();
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(InformacionEquiposActivity.this, "Acción cancelada", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                break;
            }
            case R.id.icon_cancel:{
                startActivity(new Intent(InformacionEquiposActivity.this, MenuPrincipalAdminActivity.class));

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