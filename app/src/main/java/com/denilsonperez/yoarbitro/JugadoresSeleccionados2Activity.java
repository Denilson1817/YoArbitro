package com.denilsonperez.yoarbitro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class JugadoresSeleccionados2Activity extends AppCompatActivity {
    ListView listaDeJugadoresPreliminar;
    Button btnSiguiente, btnCancelar;
    Intent recibir;
    String jugadoresSeleccionados, jugadorSeleccionado, idJuego, idSegundoEquipo;
    String[] dataArray;
    private int selectedItemPosition = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jugadores_seleccionados2);
        listaDeJugadoresPreliminar = findViewById(R.id.listaDeJugadoresPreliminar);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnSiguiente = findViewById(R.id.btnSiguiente);
        //Recibir la lista de jugadores que asistieron al partido
        recibir = getIntent();
        jugadoresSeleccionados = recibir.getStringExtra("jugadoresSeleccionados");
        //Recibir el id del juego
        idJuego = recibir.getStringExtra("idJuego");
        //Recibir el id del segundo equipo para listar en la siguiente ventana
        idSegundoEquipo = recibir.getStringExtra("idSegundoEquipo");
        //Pasar la lista de jugadores a un Array
        dataArray = jugadoresSeleccionados.split("\n"); // --> Separa los elementos cada que se encuentre un salto de linea
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataArray);
        listaDeJugadoresPreliminar.setAdapter(adapter);

        listaDeJugadoresPreliminar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Codigo para saber que jugadores se seleccionan
                selectedItemPosition=i;
                jugadoresSeleccionados = (String) adapterView.getItemAtPosition(i);
                jugadorSeleccionado = dataArray[i];
                Toast.makeText(JugadoresSeleccionados2Activity.this, jugadorSeleccionado, Toast.LENGTH_SHORT).show();
                mostrarDialogo();
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(JugadoresSeleccionados2Activity.this, MenuPrincipalActivity.class));
                finish();
            }
        });
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //aquí se agrega la siguiente parte de la cedula
            }
        });
    }
    private void mostrarDialogo(){
        //Crear el dialogo para meter datos
        DialogDatosJugadores dialogDatosJugadores = new DialogDatosJugadores();
        Bundle bundle = new Bundle();
        //Pasar las variables al diaglo
        bundle.putStringArray("jugadoresSeleccionados",dataArray);
        bundle.putString("jugadorSeleccionado",jugadorSeleccionado);
        bundle.putString("idJuego",idJuego);
        bundle.putString("idSegundoEquipo",idSegundoEquipo);
        dialogDatosJugadores.setArguments(bundle);
        dialogDatosJugadores.show(getSupportFragmentManager(),"datosJugadores");
    }
}