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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.denilsonperez.yoarbitro.Admin.AgregarJugadoresActivity;
import com.denilsonperez.yoarbitro.Admin.AgregarJugadoresDosActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class DialogDatosJugadores extends DialogFragment {
    CheckBox amonestado, expulsado;
    EditText golesJugador;
    String[] jugadoresSeleccionados;
    String jugadorSeleccionado, fueAmonestado, fueExpulsado, idJuego, nombre, numeroDeJugador, goles, idSegundoEquipo;
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
            //Separar el nombre del jugador y el numero que vienen juntos
            String[] jugadorPartes = jugadorSeleccionado.split(",");
            nombre = jugadorPartes[0];
            numeroDeJugador = jugadorPartes[1];
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
        String cadena = stringBuilder.toString();
        builder.setView(getActivity().getLayoutInflater().inflate(R.layout.datos_jugadores,null))
                .setTitle("Datos de jugadores")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case DialogInterface.BUTTON_POSITIVE:
                                Intent intent = new Intent(getContext(), JugadoresSeleccionadosActivity.class);
                                if(expulsado.isChecked()==true){
                                    fueAmonestado = "1";
                                    fueExpulsado = "1";
                                    goles = golesJugador.getText().toString();
                                    intent.putExtra("jugadoresSeleccionados",cadena);
                                    intent.putExtra("idJuego",idJuego);
                                    intent.putExtra("idSegundoEquipo", idSegundoEquipo);
                                    guardarDatosDelJugador();
                                    startActivity(intent);
                                } else if (amonestado.isChecked()==true && expulsado.isChecked()==false) {
                                    fueAmonestado = "1";
                                    fueExpulsado = "0";
                                    goles = golesJugador.getText().toString();
                                    intent.putExtra("jugadoresSeleccionados",cadena);
                                    intent.putExtra("idJuego",idJuego);
                                    intent.putExtra("idSegundoEquipo", idSegundoEquipo);
                                    guardarDatosDelJugador();
                                    startActivity(intent);
                                } else if (amonestado.isChecked()==false &&  expulsado.isChecked()==false) {
                                    fueAmonestado = "0";
                                    fueExpulsado = "0";
                                    goles = golesJugador.getText().toString();
                                    intent.putExtra("jugadoresSeleccionados",cadena);
                                    intent.putExtra("idJuego",idJuego);
                                    intent.putExtra("idSegundoEquipo", idSegundoEquipo);
                                    guardarDatosDelJugador();
                                    startActivity(intent);
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
                        startActivity(intent);
                    }
                });
        return builder.create();
    }
    private void guardarDatosDelJugador() {
        //Guardar dato en firebase
        parentRef.child(idJuego).child(nombre).child("amonestado").setValue(fueAmonestado);
        parentRef.child(idJuego).child(nombre).child("numeroDeJugador").setValue(numeroDeJugador);
        parentRef.child(idJuego).child(nombre).child("goles").setValue(goles);
        parentRef.child(idJuego).child(nombre).child("expulsado").setValue(fueExpulsado);

    }
    @Override
    public void onStart() {
        super.onStart();
        amonestado = (CheckBox) getDialog().findViewById(R.id.checkboxAmonestado);
        expulsado = (CheckBox) getDialog().findViewById(R.id.checkboxExpulsado);
        golesJugador = (EditText) getDialog().findViewById(R.id.golesJugador);

    }
}
