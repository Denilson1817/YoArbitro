package com.denilsonperez.yoarbitro.Admin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.denilsonperez.yoarbitro.R;
import com.denilsonperez.yoarbitro.modelo.Equipo;
import com.denilsonperez.yoarbitro.modelo.Jugador;

import java.util.List;

public class AdapterAgregarJugadores extends RecyclerView.Adapter<AdapterAgregarJugadores.ViewHolder> {
     List<Jugador> myList;

   private AgregarJugadoresActivity mActivity;
    private Context context;
    private LayoutInflater inflater;
    public AdapterAgregarJugadores(List<Jugador> myList, Context context, AgregarJugadoresActivity activity) {
        this.inflater=LayoutInflater.from(context);
        this.myList = myList;
        this.context=context;
       this.mActivity= activity;

    }
    @NonNull
    @Override
    public AdapterAgregarJugadores.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_elementos_jugadores, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final AdapterAgregarJugadores.ViewHolder holder, final int position) {
        holder.binData(myList.get(position));
    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {

         TextView textViewNombreJugador;
         TextView textViewNumeroeJugador;

         ViewHolder( View itemView) {
            super(itemView);
             textViewNombreJugador = itemView.findViewById(R.id.nombreDelEquipo);
             textViewNumeroeJugador = itemView.findViewById(R.id.nombreDelegado);
        }

        void binData(final Jugador item){
            textViewNombreJugador.setText(item.getNombre());
            textViewNumeroeJugador.setText(item.getNumero());

        }
    }
}
