package com.denilsonperez.yoarbitro;

import androidx.annotation.NonNull;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.denilsonperez.yoarbitro.Inicio.IniciarSesionActivity;
import com.denilsonperez.yoarbitro.Inicio.RegistrarUnoActivity;
import com.denilsonperez.yoarbitro.modelo.Equipo;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import android.view.MenuItem;
import android.widget.Toast;

import com.denilsonperez.yoarbitro.Inicio.IniciarSesionActivity;
import com.google.android.material.navigation.NavigationView;

public class SeleccionEquiposActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_equipos);
        lvDatosEquipos = findViewById(R.id.listaEquipos);
        inicializarFirebase();
        ListarDatos();



        lvDatosEquipos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Obtener el texto del elemento seleccionado
                String selectedText = adapterView.getItemAtPosition(i).toString();
                // Crear un Intent y agregar el texto seleccionado como un extra
                Intent intent = new Intent(SeleccionEquiposActivity.this, MenuPrincipalActivity.class);
                intent.putExtra("selectedText", selectedText);
                // Iniciar la segunda actividad
                startActivity(intent);
            }
        });
    }

    private void ListarDatos() {
        databaseReference.child("Equipo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaEquipos.clear();
                for(DataSnapshot objSnapShot : snapshot.getChildren()){
                    Equipo p = objSnapShot.getValue(Equipo.class);
                    listaEquipos.add(p);
                    arrayAdapterEquipo = new ArrayAdapter<Equipo>(SeleccionEquiposActivity.this, android.R.layout.simple_list_item_1, listaEquipos);
                    lvDatosEquipos.setAdapter(arrayAdapterEquipo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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