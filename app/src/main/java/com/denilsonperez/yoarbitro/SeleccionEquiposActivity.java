package com.denilsonperez.yoarbitro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.denilsonperez.yoarbitro.Inicio.IniciarSesionActivity;
import com.denilsonperez.yoarbitro.modelo.Equipo;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class SeleccionEquiposActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    FirebaseAuth firebaseAuth;
    private List<Equipo> listaEquipos = new ArrayList<Equipo>();
    MyAdapter myAdapter;
    ListView lvDatosEquipos;
    RecyclerView rvDatosEquipos;
    //Para el manejo de datos en firebase
    FirebaseDatabase firebaseDataBase;
    DatabaseReference databaseReference;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_equipos);
        lvDatosEquipos = findViewById(R.id.listaEquipos);
        rvDatosEquipos= findViewById(R.id.rvDatosEquipos);
        drawerLayout = findViewById(R.id.drawerLayout);
        firebaseAuth = FirebaseAuth.getInstance();
        inicializarFirebase();
        ListarDatos();

    }

    public void ListarDatos(){
        //List<Equipo> myList = new ArrayList<>();

        databaseReference.child("Equipos").addValueEventListener(new ValueEventListener()  {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaEquipos.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Equipo myObject = dataSnapshot.getValue(Equipo.class);
                    listaEquipos.add(myObject);
                     myAdapter = new MyAdapter(listaEquipos,SeleccionEquiposActivity.this,SeleccionEquiposActivity.this);
                    rvDatosEquipos.setAdapter(myAdapter);
                }
                // Llena el RecyclerView con los datos obtenidos
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("no hay datos");
            }
        });

    }
    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDataBase = FirebaseDatabase.getInstance();
        //firebaseDataBase.setPersistenceEnabled(true); --> Esto es una mala practica para la persistencia de la BD.
        databaseReference =  firebaseDataBase.getReference();

    }
}