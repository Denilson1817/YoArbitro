package com.denilsonperez.yoarbitro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.denilsonperez.yoarbitro.Inicio.IniciarSesionActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MenuPrincipalAdminActivity extends AppCompatActivity {
    Button cerrarSesionAdmin, registrarEquipos, cedulasGuardadasAdmin, consultarArbitros;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    TextView nombresPrincipalAdmin, correoPrincipalAdmin;
    ProgressBar progresoDatos;

    DatabaseReference Arbitros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal_admin);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Administrador de liga");

        nombresPrincipalAdmin = findViewById(R.id.nombresPrincipalAdmin);
        correoPrincipalAdmin = findViewById(R.id.correosPrincipalAdmin);
        progresoDatos = findViewById(R.id.progresoDatos);

        Arbitros = FirebaseDatabase.getInstance().getReference("Arbitros");
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        cerrarSesionAdmin = findViewById(R.id.btnCerrarSesionAdmin);
        registrarEquipos = findViewById(R.id.btnRegistrarEquipos);
        cedulasGuardadasAdmin = findViewById(R.id.btnCedulasGuardadas);
        consultarArbitros = findViewById(R.id.btnConsultarArbitros);

        cerrarSesionAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salirAplicacion();
            }
        });
        registrarEquipos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuPrincipalAdminActivity.this,RegistrarEquiposActivity.class));
            }
        });
        cedulasGuardadasAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuPrincipalAdminActivity.this, CedulasGuardadasAdminActivity.class));
            }
        });
        consultarArbitros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuPrincipalAdminActivity.this, ConsultarArbitrosActivity.class));
            }
        });
    }
    private void salirAplicacion() {
        firebaseAuth.signOut();
        startActivity(new Intent(MenuPrincipalAdminActivity.this, IniciarSesionActivity.class));
        Toast.makeText(this, "Sesión finalizada", Toast.LENGTH_SHORT).show();
    }
    private void cargaDeDAtos(){
        Arbitros.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Si el usuario existe
                if(snapshot.exists()){
                    progresoDatos.setVisibility(View.GONE);
                    //Mostrar los texview
                    nombresPrincipalAdmin.setVisibility(View.VISIBLE);
                    correoPrincipalAdmin.setVisibility(View.VISIBLE);

                    //Obtener los datos de firebase
                    String nombres = ""+snapshot.child("nombres").getValue();
                    String correo = ""+snapshot.child("correo").getValue();

                    //Setear los datos en los respectivos textview.
                    nombresPrincipalAdmin.setText(nombres);
                    correoPrincipalAdmin.setText(correo);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void comprobarInicioSesion(){
        if(user!=null){
            //El usuario a iniciado sesión
            cargaDeDAtos();
        }else{
            startActivity(new Intent(MenuPrincipalAdminActivity.this, IniciarSesionActivity.class));
            finish();
        }
    }
    @Override
    protected void onStart() {
        comprobarInicioSesion();
        super.onStart();
    }
}