package com.denilsonperez.yoarbitro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.denilsonperez.yoarbitro.CedulasGuardadasActivity;
import com.denilsonperez.yoarbitro.Inicio.IniciarSesionActivity;
import com.denilsonperez.yoarbitro.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class CedulasGuardadasActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    FirebaseAuth firebaseAuth;

    private StorageReference storageReference;
    private ListView listView;
    private ArrayAdapter<String> adapter;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cedulas_guardadas);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.abrirNav, R.string.cerrarNav);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.bringToFront();
        firebaseAuth = FirebaseAuth.getInstance();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home: {
                        startActivity(new Intent(CedulasGuardadasActivity.this, MenuPrincipalActivity.class));
                        finish();
                        break;
                    }
                    case R.id.consultarCedulas: {
                        startActivity(new Intent(CedulasGuardadasActivity.this, CedulasGuardadasActivity.class));
                        finish();
                        break;
                    }
                    case R.id.cerrarSesion: {
                        firebaseAuth.signOut();
                        startActivity(new Intent(CedulasGuardadasActivity.this, IniciarSesionActivity.class));
                        Toast.makeText(CedulasGuardadasActivity.this, "Sesión finalizada", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    }
                }
                return false;
            }
        });

        //lineas para visualizar cedulas
        listView = findViewById(R.id.lv_CedulasG);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

        // Obtiene la referencia al Firebase Storage
        storageReference = FirebaseStorage.getInstance().getReference();

        // Llama a un método para obtener la lista de archivos
        obtenerArchivosDesdeFirebaseStorage();

        //
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String nombreArchivo = adapter.getItem(position);
                abrirArchivo(nombreArchivo);
            }
        });
    }

    private void obtenerArchivosDesdeFirebaseStorage() {
        // Obtén una referencia a la carpeta deseada en Firebase Storage
        StorageReference carpetaReferencia = storageReference.child("archivos/pdf");

        // Obtiene la lista de archivos dentro de la carpeta
        carpetaReferencia.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                // La lista de archivos se ha obtenido con éxito
                List<StorageReference> archivos = listResult.getItems();

                // Realiza las operaciones que necesites con los archivos obtenidos
                for (StorageReference archivo : archivos) {
                    // Accede a la información de cada archivo (por ejemplo, nombre)
                    String nombreArchivo = archivo.getName();
                    adapter.add(nombreArchivo);
                }
                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Ocurrió un error al obtener la lista de archivos
                if (e instanceof StorageException) {
                    StorageException storageException = (StorageException) e;
                    int errorCode = storageException.getErrorCode();
                    // Maneja el error según sea necesario
                }
            }
        });
    }

    private void abrirArchivo(String nombreArchivo) {
        StorageReference archivoReferencia = storageReference.child("archivos/pdf/" + nombreArchivo);


        archivoReferencia.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Obtiene la URL de descarga del archivo
                String urlDescarga = uri.toString();

                // Abre el archivo utilizando una aplicación adecuada
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(urlDescarga), "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    // Maneja la excepción si no se encuentra una aplicación adecuada para abrir el archivo
                    Toast.makeText(CedulasGuardadasActivity.this, "No se encontró una aplicación para abrir el archivo", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Maneja el error si ocurre al obtener la URL de descarga del archivo
                Toast.makeText(CedulasGuardadasActivity.this, "Error al obtener la URL de descarga del archivo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}