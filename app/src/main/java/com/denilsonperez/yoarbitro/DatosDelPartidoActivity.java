package com.denilsonperez.yoarbitro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class DatosDelPartidoActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    EditText txtCampo, txtFecha, txtHora;
    Button btnSiguiente, btnCancelar;
    String nombreCampo, fecha, hora, idJuego;
    Intent recibir;
    //para la fecha
    TextInputLayout textInputLayoutFecha;
    private Calendar calendar;

    //para la hora
     TextInputLayout textInputLayoutHora;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference parentRef = firebaseDatabase.getReference("Cedulas");
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_del_partido);
        drawerLayout = findViewById(R.id.drawerLayout);
        txtCampo = findViewById(R.id.txtCampo);
        textInputLayoutFecha = findViewById(R.id.textInputLayoutFecha);
        txtFecha = findViewById(R.id.txtFecha);
        calendar = Calendar.getInstance();
        txtHora = findViewById(R.id.txtHora);
        textInputLayoutHora = findViewById(R.id.textInputLayoutHora);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnSiguiente = findViewById(R.id.btnSiguiente);

        //recibir el id de la cedula
        recibir = getIntent();
        idJuego = recibir.getStringExtra("idJuego");
        System.out.println("ESTE ES EL ID DEL JUEGO: "+idJuego);

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarDatos();
                Intent intent = new Intent(DatosDelPartidoActivity.this, PdfActivity.class);
                intent.putExtra("idJuego",idJuego);
                startActivity(intent);
                finish();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpannableString tituloAdvertencia = new SpannableString("Advertencia");
                tituloAdvertencia.setSpan(new ForegroundColorSpan(Color.RED), 0, tituloAdvertencia.length(), 0);
                AlertDialog.Builder builder = new AlertDialog.Builder(DatosDelPartidoActivity.this);
                builder.setTitle(tituloAdvertencia)
                        .setMessage("Al confirmar tu cancelación se perderá todo el progreso hasta el momento. ¿Desea continuar?")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Acciones a realizar cuando se hace clic en el botón Aceptar
                                Toast.makeText(DatosDelPartidoActivity.this, "Ya vendran mejores momentos para crear una cédula arbitral", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(DatosDelPartidoActivity.this, MenuPrincipalActivity.class));
                                finish();
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(DatosDelPartidoActivity.this, "Siempre es buena idea no dejar las cosas para otro momento", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }
        });
    }

    //para mostrar la fecha
    public void mostrarDatePickerDialog(View view) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Se selecciona una fecha
                String fecha = dayOfMonth + "/" + (month + 1) + "/" + year;
                txtFecha.setText(fecha);
            }
        }, year, month, dayOfMonth);

        datePickerDialog.show();
    }
//para mostrar la hora
    public void mostrarTimePickerDialog(View view) {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // Se selecciona una hora
                String hora = hourOfDay + ":" + minute;
                txtHora.setText(hora);
            }
        }, hour, minute, false);

        timePickerDialog.show();
    }


    private void validarDatos() {
        nombreCampo = txtCampo.getText().toString();
        fecha = txtFecha.getText().toString();
        hora = txtHora.getText().toString();
        if(TextUtils.isEmpty(nombreCampo) && TextUtils.isEmpty(fecha) && TextUtils.isEmpty(hora)){
            Toast.makeText(this, "No has proporcionado ningúna información, por favor completa los campos", Toast.LENGTH_SHORT).show();

        } else if(TextUtils.isEmpty(nombreCampo)){
            Toast.makeText(this, "ingrese el nombre del campo", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(fecha)) {
            Toast.makeText(this, "ingresa una fecha", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(hora)) {
            Toast.makeText(this, "ingresa una hora", Toast.LENGTH_SHORT).show();
        }else{
            subirDatos();
        }
    }

    private void subirDatos() {
        parentRef.child(idJuego).child("DatosPartido").child("Campo").setValue(nombreCampo);
        parentRef.child(idJuego).child("DatosPartido").child("Fecha").setValue(fecha);
        parentRef.child(idJuego).child("DatosPartido").child("Hora").setValue(hora);
    }
}