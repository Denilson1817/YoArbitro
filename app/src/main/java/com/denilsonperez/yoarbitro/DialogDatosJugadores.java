package com.denilsonperez.yoarbitro;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SyncRequest;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.denilsonperez.yoarbitro.Admin.AgregarJugadoresActivity;
import com.denilsonperez.yoarbitro.Admin.AgregarJugadoresDosActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class DialogDatosJugadores extends DialogFragment {
    CheckBox amonestado, expulsado, jugadorDeInicio;
    EditText golesJugador;
    String[] jugadoresSeleccionados;
    int conteoElementos;
    String jugadorSeleccionado, fueAmonestado, fueExpulsado, idJuego, nombre, numeroDeJugador, goles, idSegundoEquipo, cadena, jugadorTitular;
    boolean pantallaDos;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference parentRef = firebaseDatabase.getReference("Cedulas");
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args != null){
            //Recibir las variables de la clase anterior
            jugadoresSeleccionados = args.getStringArray("jugadoresSeleccionados");
            jugadorSeleccionado = args.getString("jugadorSeleccionado");
            idJuego = args.getString("idJuego");
            idSegundoEquipo = args.getString("idSegundoEquipo");
            conteoElementos = args.getInt("conteoElementos");
            //Esta variable es para poder reutilizar el mismo dialog en ambas pantallas de jugadores del equipo 1 y 2.
            //lo que se hace es que si vienes de la pantalla de jugadores 2 es true, entonces cuando se finalice el uso de la pantalla y
            //Se presione "siguiente" te manda a la actividad de arbitros. Mientras que si estas en la pantalla del equipo 1 te mando a la del equipo 2.
            pantallaDos = args.getBoolean("pantallaDos",false);
            //Separar el nombre del jugador y el numero que vienen juntos
            String[] jugadorPartes = jugadorSeleccionado.split(",");
            nombre = jugadorPartes[0];

            if (jugadorPartes.length >= 2) {
                nombre = jugadorPartes[0];
                numeroDeJugador = jugadorPartes[1];
            } else {
                System.out.println("conteo dialog: "+conteoElementos);
                // Manejar el caso en el que el array no tenga suficientes elementos
            }
        }
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //Para pasar la lista de jugadores de nuevo al JugadoresSeleccionados. Ya que al abrir el Dialog la info se pierde
        StringBuilder stringBuilder = new StringBuilder();
        for(int j=0;j<jugadoresSeleccionados.length;j++){
            stringBuilder.append(jugadoresSeleccionados[j]);
            if(j< jugadoresSeleccionados.length-1){
                stringBuilder.append("\n");
            }
        }
        //Se pasan los datos a la clase anterior. El array de jugadores seleccionados se pasa como cadena
        cadena = stringBuilder.toString();
        builder.setView(getActivity().getLayoutInflater().inflate(R.layout.datos_jugadores,null))
                .setTitle("Datos de jugadores")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Aquí se define el intent a donde serás enviado según de donde vengas. Del equipo 1 o del equipo 2
                                Intent intent;
                                if(pantallaDos == true){
                                    intent = new Intent(getContext(), JugadoresSeleccionados2Activity.class);
                                }else {
                                    intent = new Intent(getContext(), JugadoresSeleccionadosActivity.class);
                                }

                                if(jugadorDeInicio.isChecked()==true){
                                    jugadorTitular = "1";
                                    fueAmonestado = "0";
                                    fueExpulsado = "0";
                                    goles = golesJugador.getText().toString();
                                    if(expulsado.isChecked()==true){
                                        fueAmonestado = "1";
                                        fueExpulsado = "1";
                                        goles = golesJugador.getText().toString();
                                        intent.putExtra("jugadoresSeleccionados",cadena);
                                        intent.putExtra("idJuego",idJuego);
                                        intent.putExtra("idSegundoEquipo", idSegundoEquipo);
                                        intent.putExtra("conteoElementos", conteoElementos);
                                        guardarDatosDelJugador();
                                        startActivity(intent);
                                        break;
                                    } else if (amonestado.isChecked()==true && expulsado.isChecked()==false) {
                                        fueAmonestado = "1";
                                        fueExpulsado = "0";
                                        goles = golesJugador.getText().toString();
                                        intent.putExtra("jugadoresSeleccionados",cadena);
                                        intent.putExtra("idJuego",idJuego);
                                        intent.putExtra("idSegundoEquipo", idSegundoEquipo);
                                        intent.putExtra("conteoElementos", conteoElementos);
                                        guardarDatosDelJugador();
                                        startActivity(intent);
                                        break;
                                    } else if (amonestado.isChecked()==false &&  expulsado.isChecked()==false && jugadorTitular.equals("0")) {
                                        fueAmonestado = "0";
                                        fueExpulsado = "0";
                                        goles = golesJugador.getText().toString();
                                        intent.putExtra("jugadoresSeleccionados",cadena);
                                        intent.putExtra("idJuego",idJuego);
                                        intent.putExtra("idSegundoEquipo", idSegundoEquipo);
                                        intent.putExtra("conteoElementos", conteoElementos);
                                        guardarDatosDelJugador();
                                        startActivity(intent);
                                        break;
                                    }
                                    intent.putExtra("jugadorInicial",jugadorTitular);
                                    intent.putExtra("jugadoresSeleccionados",cadena);
                                    intent.putExtra("idJuego",idJuego);
                                    intent.putExtra("idSegundoEquipo", idSegundoEquipo);
                                    intent.putExtra("conteoElementos", conteoElementos);
                                    guardarDatosDelJugador();
                                    startActivity(intent);
                                    break;
                                }else{
                                    jugadorTitular = "0";
                                    fueAmonestado = "0";
                                    fueExpulsado = "0";
                                    goles = golesJugador.getText().toString();
                                    if(expulsado.isChecked()==true){
                                        fueAmonestado = "1";
                                        fueExpulsado = "1";
                                        goles = golesJugador.getText().toString();
                                        intent.putExtra("jugadoresSeleccionados",cadena);
                                        intent.putExtra("idJuego",idJuego);
                                        intent.putExtra("idSegundoEquipo", idSegundoEquipo);
                                        intent.putExtra("conteoElementos", conteoElementos);
                                        guardarDatosDelJugador();
                                        startActivity(intent);
                                        break;
                                    } else if (amonestado.isChecked()==true && expulsado.isChecked()==false) {
                                        fueAmonestado = "1";
                                        fueExpulsado = "0";
                                        goles = golesJugador.getText().toString();
                                        intent.putExtra("jugadoresSeleccionados",cadena);
                                        intent.putExtra("idJuego",idJuego);
                                        intent.putExtra("idSegundoEquipo", idSegundoEquipo);
                                        intent.putExtra("conteoElementos", conteoElementos);
                                        guardarDatosDelJugador();
                                        startActivity(intent);
                                        break;
                                    } else if (amonestado.isChecked()==false &&  expulsado.isChecked()==false && jugadorTitular.equals("0")) {
                                        fueAmonestado = "0";
                                        fueExpulsado = "0";
                                        goles = golesJugador.getText().toString();
                                        intent.putExtra("jugadoresSeleccionados",cadena);
                                        intent.putExtra("idJuego",idJuego);
                                        intent.putExtra("idSegundoEquipo", idSegundoEquipo);
                                        intent.putExtra("conteoElementos", conteoElementos);
                                        guardarDatosDelJugador();
                                        startActivity(intent);
                                        break;
                                    }
                                }
                        }
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getContext(), JugadoresSeleccionadosActivity.class);
                        intent.putExtra("jugadoresSeleccionados",cadena);
                        intent.putExtra("idJuego",idJuego);
                        intent.putExtra("idSegundoEquipo", idSegundoEquipo);
                        conteoElementos = conteoElementos-1;
                        intent.putExtra("conteoElementos", conteoElementos);
                        startActivity(intent);
                    }
                });
        return builder.create();
    }
    private void guardarDatosDelJugador() {
        if(pantallaDos == false){
            String jugadorId = parentRef.child(idJuego).child("Equipo1").push().getKey();
            //Guardar dato en firebase
            System.out.println("NOMBRE ACTUAL"+ nombre);
            parentRef.child(idJuego).child("Equipo1").child(jugadorId).child("nombre").setValue(nombre);
            parentRef.child(idJuego).child("Equipo1").child(jugadorId).child("amonestado").setValue(fueAmonestado);
            parentRef.child(idJuego).child("Equipo1").child(jugadorId).child("numeroDeJugador").setValue(numeroDeJugador);
            if(goles.equals("0") || goles.isEmpty()){
                goles = "0";
            }
            parentRef.child(idJuego).child("Equipo1").child(jugadorId).child("goles").setValue(goles);
            parentRef.child(idJuego).child("Equipo1").child(jugadorId).child("expulsado").setValue(fueExpulsado);
            parentRef.child(idJuego).child("Equipo1").child(jugadorId).child("Titular").setValue(jugadorTitular);
        }else{
            String jugadorId = parentRef.child(idJuego).child("Equipo2").push().getKey();
            //Guardar dato en firebase
            parentRef.child(idJuego).child("Equipo2").child(jugadorId).child("nombre").setValue(nombre);
            parentRef.child(idJuego).child("Equipo2").child(jugadorId).child("amonestado").setValue(fueAmonestado);
            parentRef.child(idJuego).child("Equipo2").child(jugadorId).child("numeroDeJugador").setValue(numeroDeJugador);
            if(goles.equals("0") || goles.isEmpty()){
                goles = "0";
            }
            parentRef.child(idJuego).child("Equipo2").child(jugadorId).child("goles").setValue(goles);
            parentRef.child(idJuego).child("Equipo2").child(jugadorId).child("expulsado").setValue(fueExpulsado);
            parentRef.child(idJuego).child("Equipo2").child(jugadorId).child("Titular").setValue(jugadorTitular);
        }

    }
    @Override
    public void onStart() {
        super.onStart();
        amonestado = (CheckBox) getDialog().findViewById(R.id.checkboxAmonestado);
        expulsado = (CheckBox) getDialog().findViewById(R.id.checkboxExpulsado);
        golesJugador = (EditText) getDialog().findViewById(R.id.golesJugador);
        jugadorDeInicio = (CheckBox) getDialog().findViewById(R.id.checkboxJugadorDeInicio);

    }
}
