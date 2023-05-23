package com.denilsonperez.yoarbitro;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class JugadoresSeleccionadosActivity extends AppCompatActivity {
    ListView listaDeJugadoresPreliminar;
    Button btnSiguiente, btnCancelar;
    Intent recibir;
    String jugadoresSeleccionados, jugadorSeleccionado, fueAmonestado="0";
    String[] dataArray;
    private int selectedItemPosition = -1;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jugadores_seleccionados);
        listaDeJugadoresPreliminar = findViewById(R.id.listaDeJugadoresPreliminar);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnSiguiente = findViewById(R.id.btnSiguiente);
        //Instacia de la BD
        DatosJugadoresHelper datosJugadoresHelper = new DatosJugadoresHelper(getApplicationContext());
        SQLiteDatabase database = datosJugadoresHelper.getReadableDatabase();
        Context context = getApplicationContext();

        //Recibir la lista de jugadores que asistieron al partido
        recibir = getIntent();
        jugadoresSeleccionados = recibir.getStringExtra("jugadoresSeleccionados");
        fueAmonestado = recibir.getStringExtra("amonestado");
        System.out.println("FUE AMONESTADO: "+fueAmonestado);

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
                Toast.makeText(JugadoresSeleccionadosActivity.this, jugadorSeleccionado, Toast.LENGTH_SHORT).show();
                mostrarDialogo();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(JugadoresSeleccionadosActivity.this, MenuPrincipalActivity.class));
                //Si se cancela el juego creado se eliminará la información de la base de datos local y se cierra la conexión
                database.execSQL("DELETE FROM " + DatosJugadoresContract.DatosJugadoresTab.TABLE_NAME);
                context.deleteDatabase("DatosJugadores.db");
                database.close();
                finish();
            }
        });
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Estas lineas hacen que se imprima la base de datos completa. El metodo Cursor funciona como un for para recorrer toda la BD
                Cursor cursor = database.query(DatosJugadoresContract.DatosJugadoresTab.TABLE_NAME, null, null, null, null, null, null);
                String datosJugadores="";
                while (cursor.moveToNext()){
                    datosJugadores = cursor.getString(cursor.getColumnIndexOrThrow(DatosJugadoresContract.DatosJugadoresTab._ID))+" "+
                            cursor.getString(cursor.getColumnIndexOrThrow(DatosJugadoresContract.DatosJugadoresTab.COLUMN_Amonestado))+" "+
                            //cursor.getString(cursor.getColumnIndexOrThrow(DatosJugadoresContract.DatosJugadoresTab.COLUMN_Expulsado))+ " "+
                            cursor.getString(cursor.getColumnIndexOrThrow(DatosJugadoresContract.DatosJugadoresTab.COLUMN_ID));
                }
                System.out.println(datosJugadores);

            }
        });
    }
    private void mostrarDialogo(){
        //Crear el dialogo para meter datos
        DialogDatosJugadores dialogDatosJugadores = new DialogDatosJugadores();
        Bundle bundle = new Bundle();
        bundle.putStringArray("jugadoresSeleccionados",dataArray);
        bundle.putString("jugadorSeleccionado",jugadorSeleccionado);
        dialogDatosJugadores.setArguments(bundle);
        dialogDatosJugadores.show(getSupportFragmentManager(),"datosJugadores");
    }
}