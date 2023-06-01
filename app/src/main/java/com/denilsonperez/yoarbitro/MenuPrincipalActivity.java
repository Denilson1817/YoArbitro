package com.denilsonperez.yoarbitro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.denilsonperez.yoarbitro.Admin.InformacionEquiposActivity;
import com.denilsonperez.yoarbitro.Admin.InformacionJugadoresActivity;
import com.denilsonperez.yoarbitro.Inicio.IniciarSesionActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashSet;

public class MenuPrincipalActivity extends AppCompatActivity{
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    FirebaseAuth firebaseAuth;
    DatabaseReference Arbitros;
    Button cbtnSeleccionEquipoUno;
    Button cbtnSeleccionEquipoDos;
    Button cbtncrearJuego;
    public String textoEquipo1;
    public String textoEquipo2;
    private HashSet<String> equiposSeleccionados;
    //Variables para pasar y recibir datos del equipo
    Intent recibir;
    String idEquipo;
    String idPrimerEquipo,idSegundoEquipo;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference parentRef = firebaseDatabase.getReference("Cedulas");
    DatabaseReference subRef = parentRef.push(); //Se crea un nodo con un idUnico
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
        setContentView(R.layout.activity_menu_principal);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.abrirNav, R.string.cerrarNav);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.bringToFront();
        Arbitros = FirebaseDatabase.getInstance().getReference("Arbitros");
        firebaseAuth = FirebaseAuth.getInstance();
        cbtnSeleccionEquipoUno = findViewById(R.id.btnSeleccionEquipoUno);
        cbtnSeleccionEquipoDos= findViewById(R.id.btnSeleccionEquipoDos);
        equiposSeleccionados = new HashSet<>();
        cbtncrearJuego = findViewById(R.id.btnContiuar);

        NavigationView navigationView = findViewById(R.id.navView);
        View headerView = navigationView.getHeaderView(0);
        TextView usernameTextView = headerView.findViewById(R.id.txtUserName);

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String username = currentUser.getEmail();
            usernameTextView.setText(username);
        }

        //actividades de los botones principales
        cbtnSeleccionEquipoUno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuPrincipalActivity.this, SeleccionEquiposActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        cbtnSeleccionEquipoDos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuPrincipalActivity.this, SeleccionEquiposActivity.class);
                startActivityForResult(intent, 2);
            }
        });

        cbtncrearJuego.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (textoEquipo1==null&&textoEquipo2==null){
                    Toast.makeText(MenuPrincipalActivity.this, "Debes seleccionar equipos para comenzar un nuevo juego", Toast.LENGTH_SHORT).show();

                }else{
                    mostrarAlertaEquipoCreado();
                }
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home: {
                        startActivity(new Intent(MenuPrincipalActivity.this, MenuPrincipalActivity.class));
                        finish();
                        break;
                    }
                    case R.id.consultarCedulas:{
                        startActivity(new Intent(MenuPrincipalActivity.this, CedulasGuardadasActivity.class));
                        finish();
                        break;
                    }
                    case R.id.cerrarSesion:{
                        firebaseAuth.signOut();
                        startActivity(new Intent(MenuPrincipalActivity.this, IniciarSesionActivity.class));
                        Toast.makeText(MenuPrincipalActivity.this, "Sesión finalizada", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    }
                }
                return false;
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            //Recuperar nombre e id de los equipos seleccionados
            String selectedText = data.getStringExtra("SELECTED_TEXT");
            idEquipo = data.getStringExtra("idEquipo");
            if (!equiposSeleccionados.contains(selectedText)) {
                if (requestCode == 1) {
                    textoEquipo1=selectedText;
                    //Se obtiene el id del primer equipo
                    idPrimerEquipo=idEquipo;
                    cbtnSeleccionEquipoUno.setText(textoEquipo1);
                    cbtnSeleccionEquipoUno.setBackgroundColor(getResources().getColor(R.color.verde));
                } else if (requestCode == 2) {
                    textoEquipo2=selectedText;
                    //Se obtiene el id del segundo equipo
                    idSegundoEquipo=idEquipo;
                    cbtnSeleccionEquipoDos.setText(textoEquipo2);
                    idEquipo = idSegundoEquipo;
                    cbtnSeleccionEquipoDos.setBackgroundColor(getResources().getColor(R.color.verde));

                }
                equiposSeleccionados.add(selectedText);
            } else {
                Toast.makeText(this, "No puedes elegir el mismo equipo", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void mostrarAlertaEquipoCreado(){
        new AlertDialog.Builder(MenuPrincipalActivity.this)
                .setTitle("Confirmación")
                .setMessage("Estas a punto de crear el siguiente juego:\n"+textoEquipo1+" VS "+textoEquipo2+ " ¿Estas seguro de continuar?")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String idJuego = subRef.getKey();
                        Intent intent = new Intent(MenuPrincipalActivity.this, JugadoresEquipo1Activity.class);
                        intent.putExtra("idPrimerEquipo", idPrimerEquipo);
                        intent.putExtra("idSegundoEquipo", idSegundoEquipo);
                        intent.putExtra("nombreEquipo1", textoEquipo1);
                        intent.putExtra("nombreEquipo2", textoEquipo2);
                        intent.putExtra("idJuego",idJuego);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

}