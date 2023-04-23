package com.denilsonperez.yoarbitro.Inicio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.denilsonperez.yoarbitro.MenuPrincipalActivity;
import com.denilsonperez.yoarbitro.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegistrarseDosActivity extends AppCompatActivity {
    EditText correoEt, contrasenaEt, confirmarContrasenaEt;
    Button btnRegistrarUsuario,btnCancelar;
    TextView tengoUnaCuenta, tengounaCuentaSegunda;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    String correo= "", contrasena ="", confirmarContrasena ="", nombreEt="", edadEt="", localidadEt="", numeroEt="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse_dos);
        //Iniciarlizar las vistas
        correoEt = findViewById(R.id.correoEt);
        contrasenaEt = findViewById(R.id.contrasenaEt);
        confirmarContrasenaEt = findViewById(R.id.confirmarContrasenaEt);
        btnRegistrarUsuario = findViewById(R.id.btnRegistrarUsuario);
        tengoUnaCuenta = findViewById(R.id.tengoUnaCuenta);
        tengounaCuentaSegunda = findViewById(R.id.tengoUnaCuentaSegunda);
        btnCancelar = findViewById(R.id.btnCancelar);
        //Recibir datos de la anterior activity
        Bundle recibeDatos = getIntent().getExtras();
        nombreEt = recibeDatos.getString("nombreEt");
        edadEt = recibeDatos.getString("edadEt");
        localidadEt = recibeDatos.getString("localidadEt");
        numeroEt = recibeDatos.getString("numeroEt");

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(RegistrarseDosActivity.this);
        progressDialog.setTitle("Espere por favor");
        progressDialog.setCanceledOnTouchOutside(false);

        btnRegistrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarDatos();
                finish();
            }
        });
        tengoUnaCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrarseDosActivity.this, IniciarSesionActivity.class));
                finish();
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrarseDosActivity.this, IniciarSesionActivity.class));
                finish();
            }
        });
        tengounaCuentaSegunda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrarseDosActivity.this, IniciarSesionActivity.class));
            }
        });
    }

    public void validarDatos(){
        correo = correoEt.getText().toString();
        contrasena = contrasenaEt.getText().toString();
        confirmarContrasena = confirmarContrasenaEt.getText().toString();

        if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            Toast.makeText(this, "Ingrese correo", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(contrasena)){
            Toast.makeText(this, "Ingrese contraseña", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(confirmarContrasena)){
            Toast.makeText(this, "Confirme contraseña", Toast.LENGTH_SHORT).show();
        }else if(!contrasena.equals(confirmarContrasena)){
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
        }else{
            crearCuenta();
        }
    }
    private void crearCuenta() {
        progressDialog.setMessage("Creando su cuenta");
        progressDialog.show();
        //Crear un usuario en firebase
        firebaseAuth.createUserWithEmailAndPassword(correo, contrasena)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //
                        guardarInformacion();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(RegistrarseDosActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void guardarInformacion() {
        progressDialog.setMessage("Guardando su información");
        progressDialog.dismiss();

        //Obtener la identificación de usuario actual
        String uid = firebaseAuth.getUid();
        //Configurar datos para agregar en la base de datos
        HashMap<String, String> Datos = new HashMap<>();

        Datos.put("uid",uid);
        Datos.put("nombre", nombreEt);
        Datos.put("edad", edadEt);
        Datos.put("localidad", localidadEt);
        Datos.put("numero", numeroEt);
        Datos.put("correo",correo);
        Datos.put("contraseña", contrasena);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Arbitros");
        databaseReference.child(uid)
                .setValue(Datos)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(RegistrarseDosActivity.this, "Cuenta creada", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegistrarseDosActivity.this, MenuPrincipalActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(RegistrarseDosActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}