package com.denilsonperez.yoarbitro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.denilsonperez.yoarbitro.Admin.AdapterJugadores;
import com.denilsonperez.yoarbitro.Admin.InformacionEquiposActivity;
import com.denilsonperez.yoarbitro.modelo.Jugador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class JugadoresSeleccionadosActivity extends AppCompatActivity {
    ListView listaDeJugadoresPreliminar;
    Button btnSiguiente, btnCancelar;
    Intent recibir;
    String jugadoresSeleccionados, idJuego, idSegundoEquipo,jugadorSeleccionado;
    int conteoElementos;
    String[] dataArray;
    List<Integer> elementosClickeados = new ArrayList<>();

    private int selectedItemPosition = -1;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jugadores_seleccionados);
        listaDeJugadoresPreliminar = findViewById(R.id.listaDeJugadoresPreliminar);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnSiguiente = findViewById(R.id.btnSiguiente);
        //Recibir la lista de jugadores que asistieron al partido
        recibir = getIntent();
        jugadoresSeleccionados = recibir.getStringExtra("jugadoresSeleccionados");
        String nombreEquipoSeleccionadoDos = recibir.getStringExtra("nombreSegundoEquipo");

        //Recibir el id del juego
        idJuego = recibir.getStringExtra("idJuego");
        //Recibir el id del segundo equipo para listar en la siguiente ventana
        idSegundoEquipo = recibir.getStringExtra("idSegundoEquipo");
        //Recibir el contreo de elementos
        conteoElementos = recibir.getIntExtra("conteoElementos",0);
        //Pasar la lista de jugadores a un Array
        dataArray = jugadoresSeleccionados.split("\n"); // --> Separa los elementos cada que se encuentre un salto de linea

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataArray);
        listaDeJugadoresPreliminar.setAdapter(adapter);

        //Contar el numero de elementos del array
        int numElementos = dataArray.length;
        System.out.println("Conteo jugadores seleccionados "+ conteoElementos);
        //contar el numero de clicks
        //numClick=0;
        listaDeJugadoresPreliminar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!elementosClickeados.contains(i)) {
                    elementosClickeados.add(i);
                    selectedItemPosition = i;
                    jugadoresSeleccionados = (String) adapterView.getItemAtPosition(i);
                    jugadorSeleccionado = dataArray[i];
                    mostrarDialogo();
                } else {
                    Toast.makeText(JugadoresSeleccionadosActivity.this, "Ya se ha dado clic en este elemento", Toast.LENGTH_SHORT).show();
                }
            }
        });



        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpannableString tituloAdvertencia = new SpannableString("Advertencia");
                tituloAdvertencia.setSpan(new ForegroundColorSpan(Color.RED), 0, tituloAdvertencia.length(), 0);
                AlertDialog.Builder builder = new AlertDialog.Builder(JugadoresSeleccionadosActivity.this);
                builder.setTitle(tituloAdvertencia)
                        .setMessage("Al confirmar tu cancelación se perderá todo el progreso hasta el momento. ¿Desea continuar?")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Acciones a realizar cuando se hace clic en el botón Aceptar
                                Toast.makeText(JugadoresSeleccionadosActivity.this, "Ya vendran mejores momentos para crear una cédula arbitral", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(JugadoresSeleccionadosActivity.this, MenuPrincipalActivity.class));
                                finish();
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(JugadoresSeleccionadosActivity.this, "Siempre es buena idea no dejar las cosas para otro momento", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }
        });
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(conteoElementos < numElementos){
                    Toast.makeText(JugadoresSeleccionadosActivity.this, "Introduce la información completa de tus jugadores", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(JugadoresSeleccionadosActivity.this, JugadoresEquipo2Activity.class);
                    intent.putExtra("idSegundoEquipo", idSegundoEquipo);
                    intent.putExtra("nombreEquipo2", nombreEquipoSeleccionadoDos);
                    intent.putExtra("idJuego",idJuego);
                    startActivity(intent);
                }
            }
        });
    }
    private void mostrarDialogo(){
        //Crear el dialogo para meter datos
        DialogDatosJugadores dialogDatosJugadores = new DialogDatosJugadores();
        Bundle bundle = new Bundle();
        conteoElementos = conteoElementos+1;
        //Pasar las variables al diaglo
        bundle.putStringArray("jugadoresSeleccionados",dataArray);
        bundle.putString("jugadorSeleccionado",jugadorSeleccionado);
        bundle.putString("idJuego",idJuego);
        bundle.putString("idSegundoEquipo",idSegundoEquipo);
        bundle.putInt("conteoElementos",conteoElementos);
        dialogDatosJugadores.setArguments(bundle);
        dialogDatosJugadores.show(getSupportFragmentManager(),"datosJugadores");
    }
}