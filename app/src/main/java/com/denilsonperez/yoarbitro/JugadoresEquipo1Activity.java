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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.denilsonperez.yoarbitro.Admin.AgregarJugadoresActivity;
import com.denilsonperez.yoarbitro.Admin.MenuPrincipalAdminActivity;
import com.denilsonperez.yoarbitro.modelo.Jugador;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class JugadoresEquipo1Activity extends AppCompatActivity {
String idPrimerEquipo, idJuego,idSegundoEquipo;
//TextView tituloEquipo1, tituloEquipo2;
Button btnSiguiente, btnCancelar;
//Conectar con firebase
 FirebaseDatabase firebaseDatabase;
 DatabaseReference databaseReference;
//Lista de jugadores Presentes en el partido
    private List<Jugador> jugadorPList = new ArrayList<>();
    ArrayAdapter<Jugador> jugadorPArrayAdapter;
    RecyclerView listvJugadoresP;
    TextView leyendaAndNombreEquipo;
    private int selectedItemPosition = -1;
    Jugador jugadorSeleccionado;
    AdapterJugadoresEquipo1 myAdapter;
    JugadoresEquipo2Activity j2;
    private String nombreEquipoSeleccionadoDos;

//Recibir datos
Intent recibir;

    //int totalListaJugadores = myAdapter.getItemCount();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jugadores_equipo1);
        //recuperamos el texto para mostrar el nombre del equipo a traves de un intent

        //Recibir id's del equipo 1 y 2 para el if y poder recuperar los jugadores pertenecientes a ese id
        recibir = getIntent();
        idPrimerEquipo = recibir.getStringExtra("idPrimerEquipo");
        idSegundoEquipo = recibir.getStringExtra("idSegundoEquipo");
        String nombreEquipoSeleccionadoUno = recibir.getStringExtra("nombreEquipo1");
        nombreEquipoSeleccionadoDos = recibir.getStringExtra("nombreEquipo2");



        //Recibimos el id del juego creado por firebase para crear un nodo nuevo cada que se haga una nueva cedula
        //Este id se usará para asirgnar los amonestados y goles de jugadores en las siguientes clases.
        idJuego = recibir.getStringExtra("idJuego");
        System.out.println("ID SEGUNDO EQUIPO "+idSegundoEquipo);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnSiguiente = findViewById(R.id.btnSiguiente);
        listvJugadoresP = findViewById(R.id.listaDeJugadoresPresentados);
        leyendaAndNombreEquipo = findViewById(R.id.txtLeyendaEquipo2);
        inicializarFirebase();
        listarDatos();
        leyendaAndNombreEquipo.setText("Es momento de seleccionar los jugadores del equipo " +nombreEquipoSeleccionadoUno+" que participaran en el juego");

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpannableString tituloAdvertencia = new SpannableString("Advertencia");
                tituloAdvertencia.setSpan(new ForegroundColorSpan(Color.RED), 0, tituloAdvertencia.length(), 0);
                AlertDialog.Builder builder = new AlertDialog.Builder(JugadoresEquipo1Activity.this);
                builder.setTitle(tituloAdvertencia)
                        .setMessage("Al confirmar tu cancelación se perderá todo el progreso hasta el momento. ¿Desea continuar?")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Acciones a realizar cuando se hace clic en el botón Aceptar
                                Toast.makeText(JugadoresEquipo1Activity.this, "Ya vendran mejores momentos para crear una cédula arbitral", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(JugadoresEquipo1Activity.this, MenuPrincipalActivity.class));
                                finish();
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(JugadoresEquipo1Activity.this, "Siempre es buena idea no dejar las cosas para otro momento", Toast.LENGTH_SHORT).show();
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
                int numJugadoresEquipo1 = myAdapter.getItemCount();
                 myAdapter = (AdapterJugadoresEquipo1) listvJugadoresP.getAdapter();
                if (myAdapter !=null) {
                    List<Jugador> jugadoresSeleccionados = myAdapter.getJugadoresSeleccionados();
                    if (jugadoresSeleccionados.size()>=7) {
                        StringBuilder jugadoresSeleccionadosStringBuilder = new StringBuilder();
                        for (Jugador jugador : jugadoresSeleccionados) {
                            jugadoresSeleccionadosStringBuilder.append(jugador.getNombre()).append(", ");
                            jugadoresSeleccionadosStringBuilder.append(jugador.getNumero()).append("\n");
                            //jugadoresSeleccionadosStringBuilder.append(jugador.getNombre()).append(jugador.getNumero()).append("\n");
                            //jugadoresSeleccionadosStringBuilder.append("Número: ").append(jugador.getNumero()).append("\n");
                            //jugadoresSeleccionadosStringBuilder.append(jugador.getNombre()).append("\n");
                        }
                        String jugadoresSeleccionadosString = jugadoresSeleccionadosStringBuilder.toString();

                        Intent intent = new Intent(JugadoresEquipo1Activity.this, JugadoresSeleccionadosActivity.class);
                        intent.putExtra("jugadoresSeleccionados", jugadoresSeleccionadosString);
                        intent.putExtra("idJuego", idJuego);
                        intent.putExtra("idSegundoEquipo", idSegundoEquipo);
                        intent.putExtra("nombreSegundoEquipo", nombreEquipoSeleccionadoDos);
                        startActivity(intent);
                    } else {
                        Toast.makeText(JugadoresEquipo1Activity.this, "Desbes seleccionar al menos 7 jugadores para continuar", Toast.LENGTH_SHORT).show();
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
                    if (idPrimerEquipo.equals(jugador.getIdEquipo())){
                        jugadorPList.add(jugador);
                    }
                    myAdapter = new AdapterJugadoresEquipo1(jugadorPList, JugadoresEquipo1Activity.this,JugadoresEquipo1Activity.this);
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