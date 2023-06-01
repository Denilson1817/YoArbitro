package com.denilsonperez.yoarbitro;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.denilsonperez.yoarbitro.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;

import java.util.List;


// ...

public class CedulasGuardadasActivity extends AppCompatActivity {

    private StorageReference storageReference;
    private ListView listView;
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cedulas_guardadas);

        listView = findViewById(R.id.listView);
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
                    Toast.makeText(CedulasGuardadasActivity.this, "Jalando pdfs", Toast.LENGTH_SHORT).show();

                    // Aquí puedes implementar la lógica para mostrar los archivos en tu actividad
                    // Puedes mostrarlos en un ListView, RecyclerView o cualquier otro componente visual
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


}