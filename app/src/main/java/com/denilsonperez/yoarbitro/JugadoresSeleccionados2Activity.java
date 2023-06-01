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
    Boolean pantallaDos=true;
    int conteoElementos;
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
        //Recibir el contreo de elementos
        conteoElementos = recibir.getIntExtra("conteoElementos",0);
        //Pasar la lista de jugadores a un Array
        dataArray = jugadoresSeleccionados.split("\n"); // --> Separa los elementos cada que se encuentre un salto de linea
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataArray);
        listaDeJugadoresPreliminar.setAdapter(adapter);
        //Contar el numero de elementos del array
        int numElementos = dataArray.length;
        listaDeJugadoresPreliminar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Codigo para saber que jugadores se seleccionan
                selectedItemPosition=i;
                jugadoresSeleccionados = (String) adapterView.getItemAtPosition(i);
                jugadorSeleccionado = dataArray[i];
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
                Intent intent = new Intent(JugadoresSeleccionados2Activity.this, DatosArbitrosActivity.class);
                //Enviar el id del juego
                intent.putExtra("idJuego",idJuego);
                startActivity(intent);
                finish();
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
        bundle.putBoolean("pantallaDos",pantallaDos);
        bundle.putInt("conteoElementos",conteoElementos);
        dialogDatosJugadores.setArguments(bundle);
        dialogDatosJugadores.show(getSupportFragmentManager(),"datosJugadores");
    }
}