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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.denilsonperez.yoarbitro.Admin.AgregarJugadoresActivity;
import com.denilsonperez.yoarbitro.Admin.AgregarJugadoresDosActivity;

import java.util.Arrays;

public class DialogDatosJugadores extends DialogFragment {
    CheckBox amonestado;
    String[] jugadoresSeleccionados;
    String jugadorSeleccionado, fueAmonestado;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args != null){
            jugadoresSeleccionados = args.getStringArray("jugadoresSeleccionados");
            jugadorSeleccionado = args.getString("jugadorSeleccionado");
            System.out.println("ESTE ES EL ID DEL JUGADOR SELECCIONADO"+ jugadorSeleccionado);
        }
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(getActivity().getLayoutInflater().inflate(R.layout.datos_jugadores,null))
                .setTitle("Datos de jugadores")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case DialogInterface.BUTTON_POSITIVE:
                                Intent intent = new Intent(getContext(), JugadoresSeleccionadosActivity.class);
                                if(amonestado.isChecked()==true){
                                    fueAmonestado = "1";
                                    //Para pasar la lista de jugadores de nuevo al JugadoresSeleccionados. Ya que al abrir el Dialog la info se pierde
                                    StringBuilder stringBuilder = new StringBuilder();
                                    for(int j=0;j<jugadoresSeleccionados.length;j++){
                                        stringBuilder.append(jugadoresSeleccionados[j]);
                                        if(j< jugadoresSeleccionados.length-1){
                                            stringBuilder.append("\n");
                                        }
                                    }
                                    //Se pasan los datos a la clase anterior. El array de jugadores seleccionados se pasa como cadena.
                                    String cadena = stringBuilder.toString();
                                    intent.putExtra("amonestado",fueAmonestado);
                                    intent.putExtra("jugadoresSeleccionados",cadena);
                                    guardarDatosDelJugador();
                                    startActivity(intent);
                                }
                        }
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getContext(), JugadoresSeleccionadosActivity.class);
                        StringBuilder stringBuilder = new StringBuilder();
                        for(int j=0;j<jugadoresSeleccionados.length;j++){
                            stringBuilder.append(jugadoresSeleccionados[j]);
                            if(j< jugadoresSeleccionados.length-1){
                                stringBuilder.append("\n");
                            }
                        }
                        String cadena = stringBuilder.toString();
                        intent.putExtra("jugadoresSeleccionados",cadena);
                        startActivity(intent);
                    }
                });
        return builder.create();
    }
    private void guardarDatosDelJugador() {
        //Instancia de la BD
        DatosJugadoresHelper datosJugadoresHelper = new DatosJugadoresHelper(getContext().getApplicationContext());
        SQLiteDatabase database = datosJugadoresHelper.getWritableDatabase();
        //Pasar los valres recolectados del Dialog a la BD
        ContentValues valores = new ContentValues();
        valores.put(DatosJugadoresContract.DatosJugadoresTab.COLUMN_ID, jugadorSeleccionado);
        valores.put(DatosJugadoresContract.DatosJugadoresTab.COLUMN_Amonestado, fueAmonestado);
        //NI IDEA PARA QUE SIRVE PERO LO HIZO LA PROFE Y SI YA SIRVE MEJOR NI LE MUEVAN
        long idNuevoDJ = database.insert(DatosJugadoresContract.DatosJugadoresTab.TABLE_NAME, null,valores);
        String idDJ = Long.toString(idNuevoDJ);
    }
    @Override
    public void onStart() {
        super.onStart();
        amonestado = (CheckBox) getDialog().findViewById(R.id.checkboxAmonestado);
    }
}
