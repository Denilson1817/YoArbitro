package com.denilsonperez.yoarbitro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.denilsonperez.yoarbitro.modelo.Jugador;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class JugadoresEquipo2Activity extends AppCompatActivity {
    String idSegundoEquipo,idJuego;
    Button btnSiguiente, btnCancelar;
    //Conectar con firebase
    FirebaseDatabase firebaseDatabase;
    JugadoresEquipo1Activity jugadores1;
    AdapterJugadoresEquipo2 myAdapter;
    DatabaseReference databaseReference;
    //Lista de jugadores Presentes en el partido
    private List<Jugador> jugadorPList = new ArrayList<>();
    RecyclerView listvJugadoresP;
    //Recibir datos
    Intent recibir;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jugadores_equipo2);
        //Recibir id's del equipo 2 para el if y poder recuperar los jugadores pertenecientes a ese id
        recibir = getIntent();
        idSegundoEquipo = recibir.getStringExtra("idSegundoEquipo");
        //Recibimos el id del juego creado por firebase para crear un nodo nuevo cada que se haga una nueva cedula
        //Este id se usará para asirgnar los amonestados y goles de jugadores en las siguientes clases.
        idJuego = recibir.getStringExtra("idJuego");
        String nombreEquipoSeleccionadoDos = recibir.getStringExtra("nombreEquipo2");
        System.out.println("ID SEGUNDO EQUIPO "+idSegundoEquipo);
        System.out.println("ID JUEGO "+idJuego);

        btnCancelar = findViewById(R.id.btnCancelar);
        btnSiguiente = findViewById(R.id.btnSiguiente);
        listvJugadoresP = findViewById(R.id.listaDeJugadoresPresentados);
        inicializarFirebase();
        listarDatos();
        jugadores1 = new JugadoresEquipo1Activity();

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpannableString tituloAdvertencia = new SpannableString("Advertencia");
                tituloAdvertencia.setSpan(new ForegroundColorSpan(Color.RED), 0, tituloAdvertencia.length(), 0);
                AlertDialog.Builder builder = new AlertDialog.Builder(JugadoresEquipo2Activity.this);
                builder.setTitle(tituloAdvertencia)
                        .setMessage("Al confirmar tu cancelación se perderá todo el progreso hasta el momento. ¿Desea continuar?")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Acciones a realizar cuando se hace clic en el botón Aceptar
                                Toast.makeText(JugadoresEquipo2Activity.this, "Ya vendran mejores momentos para crear una cédula arbitral", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(JugadoresEquipo2Activity.this, MenuPrincipalActivity.class));
                                finish();
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(JugadoresEquipo2Activity.this, "Siempre es buena idea no dejar las cosas para otro momento", Toast.LENGTH_SHORT).show();
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
                myAdapter = (AdapterJugadoresEquipo2) listvJugadoresP.getAdapter();
                if (myAdapter != null) {
                    List<Jugador> jugadoresSeleccionados = myAdapter.getJugadoresSeleccionados();
                    if (jugadoresSeleccionados.size()>=7) {
                        StringBuilder jugadoresSeleccionadosStringBuilder = new StringBuilder();
                        for (Jugador jugador : jugadoresSeleccionados) {
                            jugadoresSeleccionadosStringBuilder.append(jugador.getNombre()).append("\n");
                        }
                        String jugadoresSeleccionadosString = jugadoresSeleccionadosStringBuilder.toString();

                        Intent intent = new Intent(JugadoresEquipo2Activity.this, JugadoresSeleccionados2Activity.class);
                        intent.putExtra("jugadoresSeleccionados", jugadoresSeleccionadosString);
                        intent.putExtra("idJuego", idJuego);
                        intent.putExtra("idSegundoEquipo", idSegundoEquipo);
                        intent.putExtra("nombreSegundoEquipo", nombreEquipoSeleccionadoDos);
                        startActivity(intent);
                    } else {
                        Toast.makeText(JugadoresEquipo2Activity.this, "Desbes seleccionar al menos 7 jugadores para continuar", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    private void listarDatos(){
        databaseReference.child("Jugadores").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jugadorPList.clear();
                for(DataSnapshot objSnapshot:snapshot.getChildren()){
                    Jugador jugador = objSnapshot.getValue(Jugador.class);
                    if (idSegundoEquipo.equals(jugador.getIdEquipo())){
                        jugadorPList.add(jugador);
                    }
                    //Aquí se imprime el listView con el multipleChoice para seleccionar varios jugadores.
                    //En la vista también se debe agregar el MultipleChoice.
                    myAdapter = new AdapterJugadoresEquipo2(jugadorPList, JugadoresEquipo2Activity.this,JugadoresEquipo2Activity.this);
                    listvJugadoresP.setAdapter(myAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
}