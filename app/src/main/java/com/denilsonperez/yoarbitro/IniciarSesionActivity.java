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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class IniciarSesionActivity extends AppCompatActivity {
    EditText correoInicioSesion, contrasenaInicioSesion;
    Button btnIniciarSesion;
    TextView nuevoUsuario;

    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;

    //Para validar los datos
    String correo = "", contrasena = "", correoAdmin="admin@gmail.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Iniciar Sesion");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        correoInicioSesion = findViewById(R.id.correoIniciarSesion);
        contrasenaInicioSesion = findViewById(R.id.contrasenaIniciarSesion);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        nuevoUsuario = findViewById(R.id.usuarioNuevo);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(IniciarSesionActivity.this);
        progressDialog.setTitle("Espere por favor");
        progressDialog.setCanceledOnTouchOutside(false);

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarDatos();
            }
        });
        nuevoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IniciarSesionActivity.this, RegistrarActivity.class));
            }
        });
    }
    private void validarDatos(){
        correo = correoInicioSesion.getText().toString();
        contrasena = contrasenaInicioSesion.getText().toString();

        if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            Toast.makeText(this, "correo inválido", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(contrasena)){
            Toast.makeText(this, "ingrese contraseña", Toast.LENGTH_SHORT).show();
        }else if(correo.equals(correoAdmin)){
            inicioDeAdmin();
        }else{
            inicioDeUsuario();
        }
    }
    private void inicioDeUsuario(){
        progressDialog.setMessage("Iniciando sesión ...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(correo, contrasena)
                .addOnCompleteListener(IniciarSesionActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            startActivity(new Intent(IniciarSesionActivity.this, MenuPrincipalActivity.class));
                            Toast.makeText(IniciarSesionActivity.this, "Bienvenido(a)"+user.getEmail(), Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(IniciarSesionActivity.this, "Verifique si su correo y contraseña son correctas", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(IniciarSesionActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void inicioDeAdmin(){
        progressDialog.setMessage("Iniciando sesión como administrador ...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(correo, contrasena)
                .addOnCompleteListener(IniciarSesionActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            startActivity(new Intent(IniciarSesionActivity.this, MenuPrincipalAdminActivity.class));
                            Toast.makeText(IniciarSesionActivity.this, "Bienvenido(a)"+user.getEmail(), Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(IniciarSesionActivity.this, "Verifique si su correo y contraseña son correctas", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(IniciarSesionActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}