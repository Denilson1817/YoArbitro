package com.denilsonperez.yoarbitro;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.denilsonperez.yoarbitro.Inicio.IniciarSesionActivity;
import com.denilsonperez.yoarbitro.Inicio.RegistrarUnoActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PdfActivity extends AppCompatActivity {
    Button btnGenerarCedula;
    String tituloCedula = "Benemerita Escuela Normal Veracruzana", equioposTitulo = "Equipos del encuentro", cambiosTitulo="Cambios", expulsadoTitulo="Expulsados";
    String fecha, hora, campo, idJuego, datosDelPartido, arbitrosDelPartido;
    //String para arbitros
    String central, asistente1 = " ", asistente2 = " ";
    //Strings para jugadores
    String nombre, nombre2, nombresJugadores1, nombresJugadores1Cambios, nombresJugadores2, nombresJugadores2Cambios, amonestado, expulsado, goles, numero, amonestado2, expulsado2, goles2, numero2, titular, titular2;
    int golesTotal, golesEquipo1, golesEquipo2;
    String golesCambio;
    Intent recibir;
    private static final int PERMISSION_REQUEST_CODE = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        btnGenerarCedula = findViewById(R.id.btnGenerarCedula);

        if (checkPermission()) {
            Toast.makeText(this, "Permiso aceptado", Toast.LENGTH_SHORT).show();
        } else {
            requestPermissions();
        }

        //recibir el id de la cedula
        recibir = getIntent();
        idJuego = recibir.getStringExtra("idJuego");
        datosDelPartido();

        btnGenerarCedula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generarPDF();
                startActivity(new Intent(PdfActivity.this, MenuPrincipalActivity.class));
            }
        });
    }

    private void generarPDF() {
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        TextPaint titulo = new TextPaint();
        TextPaint datosPartido = new TextPaint();
        TextPaint arbitrosPartido = new TextPaint();
        TextPaint equipo1Partido = new TextPaint();
        TextPaint tituloCambios = new TextPaint();
        TextPaint equipo1PartidoCambios = new TextPaint();
        TextPaint equipo2PartidoCambios = new TextPaint();
        TextPaint equipo2Partido = new TextPaint();
        TextPaint tituloEquipos = new TextPaint();
        TextPaint totalGoles = new TextPaint();
        Bitmap bitmap, bitmapEscala;

        //Crear el pdf con las medidas
        PdfDocument.PageInfo cedula = new PdfDocument.PageInfo.Builder(816, 1054, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(cedula);
        Canvas canvas = page.getCanvas();

        //Dibujar la imagen
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        //Se escala la imagen para el tamaño
        bitmapEscala = Bitmap.createScaledBitmap(bitmap, 80, 80, false);
        canvas.drawBitmap(bitmapEscala, 368, 20, paint);
        //Se pasa el titulo al pdf
        titulo.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titulo.setTextSize(20);
        canvas.drawText(tituloCedula, 10, 150, titulo);
        //Se pasa los datos del partido al pdf
        datosPartido.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        datosPartido.setTextSize(15);
        canvas.drawText(datosDelPartido, 10, 200, datosPartido);
        //Se pasa los datos de abitros al pdf
        arbitrosPartido.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        arbitrosPartido.setTextSize(15);
        canvas.drawText(arbitrosDelPartido, 10, 250, arbitrosPartido);
        //Titulo para equipos
        tituloEquipos.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        arbitrosPartido.setTextSize(20);
        canvas.drawText(equioposTitulo, 270, 300, arbitrosPartido);
        //Datos del equipo1
        equipo1Partido.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        equipo1Partido.setTextSize(11);
        int y = 350;
        String[] arrEquipo1 = nombresJugadores1.split("\n");
        for (int i = 0; i < arrEquipo1.length; i++) {
            canvas.drawText(arrEquipo1[i], 10, y, equipo1Partido);
            y += 18;
        }
        //Datos del equipo2
        equipo2Partido.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        equipo2Partido.setTextSize(11);
        int x = 350;
        String[] arrEquipo2 = nombresJugadores2.split("\n");
        for (int i = 0; i < arrEquipo2.length; i++) {
            canvas.drawText(arrEquipo2[i], 450, x, equipo2Partido);
            x += 18;
        }
        //Titulo para los cambios
        tituloCambios.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        tituloCambios.setTextSize(20);
        canvas.drawText(cambiosTitulo, 340, 665, tituloCambios);
        //Datos del equipo1 Cambios
        equipo1PartidoCambios.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        equipo1PartidoCambios.setTextSize(11);
        int j=700;
        String[] arrEquipo1Cambios = nombresJugadores1Cambios.split("\n");
        for (int i=0;i<arrEquipo1Cambios.length;i++){
        canvas.drawText(arrEquipo1Cambios[i],10,j,equipo1PartidoCambios);
        j += 18;
        }
        //Datos del equipo2 Cambios
        equipo2PartidoCambios.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        equipo2PartidoCambios.setTextSize(11);
        int g=700;
        String[] arrEquipo2Cambios = nombresJugadores2Cambios.split("\n");
        for (int i=0;i<arrEquipo2Cambios.length;i++){
            canvas.drawText(arrEquipo2Cambios[i],450,g,equipo2PartidoCambios);
            g +=18;
        }
        //Titulo total goles
        totalGoles.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        totalGoles.setTextSize(20);
        canvas.drawText(String.valueOf("Total de goles: "+golesTotal), 10, 880, totalGoles);

        pdfDocument.finishPage(page);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        // Obtén una referencia al directorio en el que deseas almacenar los archivos PDF
        StorageReference storageRef = storage.getReference().child("archivos/pdf");
// Genera un nombre único para el archivo PDF utilizando la fecha y hora actual
        String timestamp = String.valueOf(System.currentTimeMillis());
        String fileName = "cedula_" + timestamp + ".pdf";
// Crea la referencia al archivo con el nombre dinámico
        StorageReference pdfRef = storageRef.child(fileName);
// Obtener el OutputStream para el archivo PDF
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            pdfDocument.writeTo(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
// Obtener los bytes del archivo PDF
        byte[] pdfBytes = outputStream.toByteArray();

// Subir el archivo a Firebase Storage
        UploadTask uploadTask = pdfRef.putBytes(pdfBytes);
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    // El archivo PDF se subió exitosamente
                    Toast.makeText(PdfActivity.this, "PDF guardado", Toast.LENGTH_SHORT).show();
                    // Aquí puedes realizar otras operaciones, como guardar la URL del archivo en Firestore
                } else {
                    // Ocurrió un error al subir el archivo PDF a Firebase Storage
                    Toast.makeText(PdfActivity.this, "Error al guardar el archivo PDF", Toast.LENGTH_SHORT).show();
                }
            }
        });
        pdfDocument.close();
    }
    private boolean checkPermission() {
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 200);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 200) {
            if (grantResults.length > 0) {
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permiso aceptado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    private void datosDelPartido() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference parentRef = firebaseDatabase.getReference("Cedulas").child(idJuego);
        parentRef.child("DatosPartido").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    fecha = "" + snapshot.child("Fecha").getValue();
                    campo = "" + snapshot.child("Campo").getValue();
                    hora = "" + snapshot.child("Hora").getValue();

                    datosDelPartido = "Fecha: " + fecha + "    Hora: " + hora + "    Campo de juego: " + campo;

                    arbitrosDelPartido();
                } else {
                    System.out.println("NO EXISTE");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void arbitrosDelPartido() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference parentRef = firebaseDatabase.getReference("Cedulas").child(idJuego);
        parentRef.child("Arbitros").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    central = "" + snapshot.child("Central").getValue();
                    asistente1 = "" + snapshot.child("Asistente1").getValue();
                    asistente2 = "" + snapshot.child("Asistente2").getValue();

                    arbitrosDelPartido = "Central : " + central + "     Asistente 1 : " + asistente1 + "    Asistente 2 :" + asistente2;
                    equipo1DelPartido();
                } else {
                    System.out.println("NO EXISTE");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void equipo1DelPartido() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference parentRef = firebaseDatabase.getReference("Cedulas").child(idJuego);
        DatabaseReference equipo1Ref = parentRef.child("Equipo1");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                StringBuilder nombresB = new StringBuilder();
                StringBuilder nombresBCambios = new StringBuilder();

                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    // Obtener el valor del campo "nombre" de cada nodo hijo
                    nombre = childSnapshot.child("nombre").getValue(String.class);
                    amonestado = childSnapshot.child("amonestado").getValue(String.class);
                    goles = childSnapshot.child("goles").getValue(String.class);
                    expulsado = childSnapshot.child("expulsado").getValue(String.class);
                    numero = childSnapshot.child("numeroDeJugador").getValue(String.class);
                    titular = childSnapshot.child("Titular").getValue(String.class);

                    if (nombre != null) {
                        if (amonestado.equals("1")) {
                            amonestado = "Si";
                        } else {
                            amonestado = "No";
                        }
                        if(expulsado.equals("1")){
                            expulsado = "Si";
                        }else{
                            expulsado = "No";
                        }
                            golesEquipo1 = Integer.parseInt(goles);
                            golesTotal= golesEquipo1+golesTotal;

                        if (titular.equals("1")){
                            nombresB.append("#"+numero + "  " + nombre + "   Goles: " +goles + "     Amon. " + amonestado+ "     Exp. "+expulsado).append("\n");

                            // Obtener la cadena final con los nombres separados por saltos de línea
                            nombresJugadores1 = nombresB.toString();
                        }else{
                            nombresBCambios.append("#"+numero+"  "+nombre+ "   Goles: "+goles + "     Amon. "+ amonestado+ "     Exp. "+expulsado).append("\n");
                            // Obtener la cadena final con los nombres separados por saltos de línea
                            nombresJugadores1Cambios = nombresBCambios.toString();
                        }
                    }
                }
                equipo2DelPartido();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        equipo1Ref.addListenerForSingleValueEvent(valueEventListener);
    }

    private void equipo2DelPartido() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference parentRef = firebaseDatabase.getReference("Cedulas").child(idJuego);
        DatabaseReference equipo2Ref = parentRef.child("Equipo2");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                StringBuilder nombresB = new StringBuilder();
                StringBuilder nombresB2Cambios = new StringBuilder();

                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    // Obtener el valor del campo "nombre" de cada nodo hijo
                    nombre2 = childSnapshot.child("nombre").getValue(String.class);
                    numero2 = childSnapshot.child("numeroDeJugador").getValue(String.class);
                    goles2 = childSnapshot.child("goles").getValue(String.class);
                    amonestado2 = childSnapshot.child("amonestado").getValue(String.class);
                    titular2 = childSnapshot.child("Titular").getValue(String.class);
                    expulsado2 = childSnapshot.child("expulsado").getValue(String.class);

                    if (nombre2 != null) {
                        if (amonestado2.equals("1")) {
                            amonestado2 = "Si";
                        } else {
                            amonestado2 = "No";
                        }
                        if (expulsado2.equals("1")) {
                            expulsado2 = "Si";
                        }else{
                            expulsado2 = "No";
                        }
                            golesEquipo2 = Integer.parseInt(goles2);
                            golesTotal= golesEquipo2+golesTotal;
                            //golesCambio = "El total de goles es: "+golesTotal;

                        if (titular2.equals("1")) {
                            nombresB.append("#"+numero2 + "  " + nombre2 + "   Goles: " + goles2 + "     Amon. " + amonestado2+ "     Exp. "+expulsado2).append("\n");
                            // Obtener la cadena final con los nombres separados por saltos de línea
                            nombresJugadores2 = nombresB.toString();
                        }else{
                            nombresB2Cambios.append("#"+numero2+"  "+nombre2+ "   Goles: "+goles2+ "     Amon. "+ amonestado2+ "     Exp. "+expulsado2).append("\n");
                            // Obtener la cadena final con los nombres separados por saltos de línea
                            nombresJugadores2Cambios = nombresB2Cambios.toString();
                        }
                    }
                }
                // Obtener la cadena final con los nombres separados por saltos de línea
                nombresJugadores2 = nombresB.toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        equipo2Ref.addListenerForSingleValueEvent(valueEventListener);
    }
}