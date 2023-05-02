package com.denilsonperez.yoarbitro.Inicio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.denilsonperez.yoarbitro.MenuPrincipalActivity;
import com.denilsonperez.yoarbitro.MenuPrincipalAdminActivity;
import com.denilsonperez.yoarbitro.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class PantallaDeCargaActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_de_carga);

        int tiempoDeCarga = 2000;
        // Obtener una instancia de FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        // Obtener una instancia de DatabaseReference para la base de datos de Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // Verificar el estado de la sesión del usuario
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String idAdmin="QQ2V6OHc25aIHMOuIrFiBKpMJc92";
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                if (currentUser != null) {
                    // El usuario ya ha iniciado sesión
                    String userId = currentUser.getUid();

                    // Guardar el historial de inicio de sesión en la base de datos
                    mDatabase.child("Arbitros").child(userId).child("last_login").setValue(ServerValue.TIMESTAMP);
                    if (userId.equals(idAdmin)){
                        startActivity(new Intent(PantallaDeCargaActivity.this, MenuPrincipalAdminActivity.class));
                        finish();
                    }else{
                        startActivity(new Intent(PantallaDeCargaActivity.this, MenuPrincipalActivity.class));
                        finish();
                    }

                } else {
                    // El usuario no ha iniciado sesión
                    startActivity(new Intent(PantallaDeCargaActivity.this, IniciarSesionActivity.class));
                    finish();
                }

            }
        }, tiempoDeCarga);
    }
}