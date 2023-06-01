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
import android.os.Bundle;
import android.os.Environment;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PdfActivity extends AppCompatActivity {
    Button btnGenerarCedula;
    String tituloCedula = "Benemerita Escuela Normal Veracruzana";
    String fecha, hora, campo, idJuego,datosDelPartido, arbitrosDelPartido;
    //String para arbitros
    String central, asistente1=" ", asistente2=" ";
    //Strings para jugadores
    String nombre, nombre2, nombresJugadores1, nombresJugadores2, amonestado, expulsado, goles,numero,amonestado2, expulsado2, goles2,numero2;
    Intent recibir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        btnGenerarCedula = findViewById(R.id.btnGenerarCedula);

        if(checkPermission()){
            Toast.makeText(this, "Permiso aceptado", Toast.LENGTH_SHORT).show();
        }else{
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
        TextPaint equipo2Partido = new TextPaint();
        Bitmap bitmap, bitmapEscala;

        //Crear el pdf con las medidas
        PdfDocument.PageInfo cedula = new PdfDocument.PageInfo.Builder(816, 1054, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(cedula);
        Canvas canvas = page.getCanvas();

        //Dibujar la imagen
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.logo);
        //Se escala la imagen para el tamaño
        bitmapEscala = Bitmap.createScaledBitmap(bitmap, 80, 80, false);
        canvas.drawBitmap(bitmapEscala,368, 20, paint);
        //Se pasa el titulo al pdf
        titulo.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titulo.setTextSize(25);
        canvas.drawText(tituloCedula, 230, 150, titulo);
        //Se pasa los datos del partido al pdf
        datosPartido.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        datosPartido.setTextSize(15);
        canvas.drawText(datosDelPartido,10, 200, datosPartido);
        //Se pasa los datos de abitros al pdf
        arbitrosPartido.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        arbitrosPartido.setTextSize(15);
        canvas.drawText(arbitrosDelPartido,10, 250, arbitrosPartido);

        //Datos del equipo1
        equipo1Partido.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        equipo1Partido.setTextSize(13);
        int y=300;
        String[] arrEquipo1 = nombresJugadores1.split("\n");
        for (int i=0;i<arrEquipo1.length;i++){
            canvas.drawText(arrEquipo1[i],10,y,equipo1Partido);
            y += 25;
        }
        //Datos del equipo2
        equipo2Partido.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        equipo2Partido.setTextSize(13);
        int x=300;
        String[] arrEquipo2 = nombresJugadores2.split("\n");
        for (int i=0;i<arrEquipo2.length;i++){
            canvas.drawText(arrEquipo2[i],500,x,equipo2Partido);
            x += 25;
        }

        pdfDocument.finishPage(page);

        File file = new File(Environment.getExternalStorageDirectory(), "Cedula.pdf");
        try{
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(this, "Cedula creada", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }

        pdfDocument.close();
    }

    private boolean checkPermission() {
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(),WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(),READ_EXTERNAL_STORAGE);
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
                if(snapshot.exists()){
                    fecha = ""+snapshot.child("Fecha").getValue();
                    campo = ""+snapshot.child("Campo").getValue();
                    hora = ""+snapshot.child("Hora").getValue();

                    datosDelPartido = "Fecha: "+fecha+"    Hora: "+hora+"    Campo de juego: "+campo;

                    arbitrosDelPartido();
                }else {
                    System.out.println("NO EXISTE");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void arbitrosDelPartido(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference parentRef = firebaseDatabase.getReference("Cedulas").child(idJuego);
        parentRef.child("Arbitros").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    central =""+snapshot.child("Central").getValue();
                    asistente1 =""+snapshot.child("Asistente1").getValue();
                    asistente2 =""+snapshot.child("Asistente2").getValue();

                    arbitrosDelPartido = "Central : "+central+ "     Asistente 1 : "+ asistente1+ "    Asistente 2 : "+ asistente2;
                    equipo1DelPartido();
                }else {
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

                for (DataSnapshot childSnapshot : snapshot.getChildren()){
                    // Obtener el valor del campo "nombre" de cada nodo hijo
                    nombre = childSnapshot.child("nombre").getValue(String.class);
                    amonestado = childSnapshot.child("amonestado").getValue(String.class);
                    goles = childSnapshot.child("goles").getValue(String.class);
                    expulsado = childSnapshot.child("expulsado").getValue(String.class);
                    numero = childSnapshot.child("numeroDeJugador").getValue(String.class);

                    if (nombre != null) {
                        nombresB.append(nombre+" Numero: "+ numero +" Amonestado: "+amonestado+ " Goles: "+goles+ " Expulsado: "+ expulsado).append("\n");
                    }
                }
                // Obtener la cadena final con los nombres separados por saltos de línea
                nombresJugadores1 = nombresB.toString();
                equipo2DelPartido();

                // Utilizar la variable 'nombres' en tu lógica
                System.out.println("Lista de nombres:\n" + nombresJugadores1);
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

                for (DataSnapshot childSnapshot : snapshot.getChildren()){
                    // Obtener el valor del campo "nombre" de cada nodo hijo
                    nombre2 = childSnapshot.child("nombre").getValue(String.class);

                    if (nombre2 != null) {
                        nombresB.append(nombre2).append("\n");
                    }
                }
                // Obtener la cadena final con los nombres separados por saltos de línea
                nombresJugadores2 = nombresB.toString();

                // Utilizar la variable 'nombres' en tu lógica
                System.out.println("Lista de nombres:\n" + nombresJugadores2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        equipo2Ref.addListenerForSingleValueEvent(valueEventListener);
    }

}