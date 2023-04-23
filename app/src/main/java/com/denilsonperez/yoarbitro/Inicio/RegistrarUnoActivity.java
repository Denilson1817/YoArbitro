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

public class RegistrarUnoActivity extends AppCompatActivity {
    EditText nombreEt, edadEt, localidadEt, numeroEt;
    Button btnSiguiente, btnCancelar;
    TextView tengoUnaCuenta, tengounaCuentaSegunda;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    String nombre = "", edad ="", localidad ="", numero="";
    boolean datosCorrectos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_uno);
        //inicializar las vistas
        nombreEt = findViewById(R.id.nombreEt);
        edadEt = findViewById(R.id.edadEt);
        localidadEt = findViewById(R.id.localidadEt);
        numeroEt = findViewById(R.id.numeroEt);
        btnSiguiente = findViewById(R.id.btnSiguiente);
        btnCancelar = findViewById(R.id.btnCancelar);
        tengoUnaCuenta = findViewById(R.id.tengoUnaCuenta);
        tengounaCuentaSegunda = findViewById(R.id.tengoUnaCuentaSegunda);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(RegistrarUnoActivity.this);
        progressDialog.setTitle("Espere por favor");
        progressDialog.setCanceledOnTouchOutside(false);

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrarUnoActivity.this, IniciarSesionActivity.class));
                finish();
            }
        });

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarDatos();
                if(datosCorrectos == true){
                    Bundle enviarDatos = new Bundle();
                    enviarDatos.putString("nombreEt", nombre);
                    enviarDatos.putString("edadEt", edad);
                    enviarDatos.putString("localidadEt",localidad);
                    enviarDatos.putString("numeroEt",numero);
                    Intent intent = new Intent(RegistrarUnoActivity.this, RegistrarseDosActivity.class);
                    intent.putExtras(enviarDatos);
                    startActivity(intent);
                    finish();
                }
            }
        });

        tengoUnaCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrarUnoActivity.this, IniciarSesionActivity.class));
                finish();
            }
        });
        tengounaCuentaSegunda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrarUnoActivity.this, IniciarSesionActivity.class));
                finish();
            }
        });
    }

    public void validarDatos(){
        nombre = nombreEt.getText().toString();
        edad = edadEt.getText().toString();
        localidad = localidadEt.getText().toString();
        numero = numeroEt.getText().toString();

        if(TextUtils.isEmpty(nombre)){
            Toast.makeText(this, "Ingrese nombre", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(edad)){
            Toast.makeText(this, "Ingrese edad", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(localidad)){
            Toast.makeText(this, "Ingrese su localidad", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(numero)){
            Toast.makeText(this, "Ingrese su numero", Toast.LENGTH_SHORT).show();
        }else{
            datosCorrectos = true;
        }
        //else{
            //startActivity(new Intent(RegistrarUnoActivity.this, RegistrarseDosActivity.class));
        //}
    }
    private void guardarInformacion() {
        progressDialog.setMessage("Guardando su información");
        progressDialog.dismiss();

        //Obtener la identificación de usuario actual
        String uid = firebaseAuth.getUid();
        //Configurar datos para agregar en la base de datos
        HashMap<String, String> Datos = new HashMap<>();
        Datos.put("uid",uid);
        Datos.put("nombres",nombre);
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
                        Toast.makeText(RegistrarUnoActivity.this, "Cuenta creada", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegistrarUnoActivity.this, MenuPrincipalActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(RegistrarUnoActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}