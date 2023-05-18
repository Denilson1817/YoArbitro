package com.denilsonperez.yoarbitro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.denilsonperez.yoarbitro.Admin.AgregarJugadoresDosActivity;
import com.denilsonperez.yoarbitro.Admin.ConsultarCedulasActivity;
import com.denilsonperez.yoarbitro.modelo.Jugador;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class JugadoresEquipo1Activity extends AppCompatActivity {
String equipoEnviado1, equipoEnviado2,idPrimerEquipo;
TextView tituloEquipo1, tituloEquipo2;
Button btnSiguiente, btnCancelar;
//Conectar con firebase
 FirebaseAuth firebaseAuth;
 FirebaseDatabase firebaseDatabase;
 DatabaseReference databaseReference;
//Lista de jugadores Presentes en el partido
    private List<Jugador> jugadorPList = new ArrayList<>();
    ArrayAdapter<Jugador> jugadorPArrayAdapter;
    ListView listvJugadoresP;
    private int selectedItemPosition = -1;
    Jugador jugadorSeleccionado;
//Recibir datos
Intent recibir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jugadores_equipo1);
        //se recupera el texto de cada boton a traves de sus variables publicas
        //MenuPrincipalActivity equipos = new MenuPrincipalActivity();
        //equipoEnviado1=equipos.textoEquipo1;
        //equipoEnviado2=equipos.textoEquipo2;
        //recuperamos el id del texto para mostrar el nombre del equipo
        //tituloEquipo1 = findViewById(R.id.txtNombreEquipo1);
        //tituloEquipo1.setText(equipoEnviado1);
        //System.out.println(equipoEnviado1);

        //Recibir id's del equipo 1 para el if y poder recuperar los jugadores pertenecientes a ese id
        recibir = getIntent();
        idPrimerEquipo = recibir.getStringExtra("idPrimerEquipo");

        btnCancelar = findViewById(R.id.btnCancelar);
        btnSiguiente = findViewById(R.id.btnSiguiente);
        listvJugadoresP = findViewById(R.id.listaDeJugadoresPresentados);
        inicializarFirebase();
        listarDatos();
        listvJugadoresP.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Codigo para saber que jugadores se seleccionan
                selectedItemPosition=i;
                jugadorSeleccionado = (Jugador) adapterView.getItemAtPosition(i);
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(JugadoresEquipo1Activity.this, MenuPrincipalActivity.class));
                finish();
            }
        });
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Se imprime la lista de jugadores seleccionados
                if(selectedItemPosition != 1){
                    String itemSelected = "Jugadores seleccionados: \n";
                    for(int i=0;i<listvJugadoresP.getCount();i++){
                        if(listvJugadoresP.isItemChecked(i)){
                            itemSelected += listvJugadoresP.getItemAtPosition(i) + "\n";
                        }
                    }
                    Intent intent = new Intent(JugadoresEquipo1Activity.this, JugadoresSeleccionadosActivity.class);
                    //Enviar la lista de jugadores que asistieron al partido
                    intent.putExtra("jugadoresSeleccionados", itemSelected);
                    startActivity(intent);
                }else {
                    Toast.makeText(JugadoresEquipo1Activity.this, "Selecciona más elementos", Toast.LENGTH_SHORT).show();
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
                    //Aquí se imprime el listView con el multipleChoice para seleccionar varios jugadores.
                    //En la vista también se debe agregar el MultipleChoice.
                    jugadorPArrayAdapter = new ArrayAdapter<Jugador>(JugadoresEquipo1Activity.this, android.R.layout.simple_list_item_multiple_choice, jugadorPList);
                    listvJugadoresP.setAdapter(jugadorPArrayAdapter);
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