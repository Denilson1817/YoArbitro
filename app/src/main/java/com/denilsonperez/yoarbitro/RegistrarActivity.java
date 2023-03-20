package com.denilsonperez.yoarbitro;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegistrarActivity extends AppCompatActivity {
    EditText nombreEt, correoEt, contrasenaEt, confirmarContrasenaEt, edadEt, localidadEt, numeroEt;
    Button btnRegistrarUsuario;
    TextView tengoUnaCuenta;

    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    String nombre = "", correo = "", password = "", confirmarPassword = "", edad ="", localidad ="", numero="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Registrar");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //inicializar las vistas
        nombreEt = findViewById(R.id.nombreEt);
        correoEt = findViewById(R.id.correoEt);
        contrasenaEt = findViewById(R.id.contrasenaEt);
        edadEt = findViewById(R.id.edadEt);
        localidadEt = findViewById(R.id.localidadEt);
        numeroEt = findViewById(R.id.numeroEt);
        confirmarContrasenaEt = findViewById(R.id.confirmarContrasenaEt);
        btnRegistrarUsuario = findViewById(R.id.btnRegistrarUsuario);
        tengoUnaCuenta = findViewById(R.id.tengoUnaCuenta);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(RegistrarActivity.this);
        progressDialog.setTitle("Espere por favor");
        progressDialog.setCanceledOnTouchOutside(false);

        btnRegistrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarDatos();
            }
        });
        tengoUnaCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrarActivity.this, IniciarSesionActivity.class));
            }
        });
    }
    private void validarDatos(){
        nombre = nombreEt.getText().toString();
        correo = correoEt.getText().toString();
        password = contrasenaEt.getText().toString();
        edad = edadEt.getText().toString();
        localidad = localidadEt.getText().toString();
        numero = numeroEt.getText().toString();
        confirmarPassword = confirmarContrasenaEt.getText().toString();

        if(TextUtils.isEmpty(nombre)){
            Toast.makeText(this, "Ingrese nombre", Toast.LENGTH_SHORT).show();
        }else if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            Toast.makeText(this, "Ingrese correo", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(edad)){
            Toast.makeText(this, "Ingrese edad", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(localidad)){
            Toast.makeText(this, "Ingrese su localidad", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(numero)){
            Toast.makeText(this, "Ingrese su numero", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Ingrese contraseña", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(confirmarPassword)){
            Toast.makeText(this, "Confirme contraseña", Toast.LENGTH_SHORT).show();
        }else if(!password.equals(confirmarPassword)){
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
        }else{
            crearCuenta();
        }
    }
    private void crearCuenta() {
        progressDialog.setMessage("Creando su cuenta");
        progressDialog.show();
        //Crear un usuario en firebase
        firebaseAuth.createUserWithEmailAndPassword(correo, password)
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
                        Toast.makeText(RegistrarActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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
        Datos.put("correo",correo);
        Datos.put("nombres",nombre);
        Datos.put("contraseña", password);
        Datos.put("edad",edad);
        Datos.put("localidad",localidad);
        Datos.put("numero",numero);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Arbitros");
        databaseReference.child(uid)
                .setValue(Datos)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(RegistrarActivity.this, "Cuenta creada", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegistrarActivity.this, MenuPrincipalActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(RegistrarActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}