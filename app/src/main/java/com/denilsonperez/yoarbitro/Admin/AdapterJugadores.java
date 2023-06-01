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

public class AdapterJugadores extends RecyclerView.Adapter<AdapterJugadores.ViewHolder> {
     List<Jugador> myList;
    List<Equipo> myListEquipo;

    //Equipo equipoSeleccionado;
    Intent recibir;

    String nombreDeEquipo="", nombreDeDelegado, numDeContacto, idEquipo;



    private InformacionEquiposActivity mActivity;
    private Context context;
    private LayoutInflater inflater;
    public AdapterJugadores(List<Jugador> myList, Context context, InformacionEquiposActivity activity) {
        this.inflater=LayoutInflater.from(context);
        this.myList = myList;
        this.context=context;
       this.mActivity= activity;

    }
    @NonNull
    @Override
    public AdapterJugadores.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_elementos_jugadores, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final AdapterJugadores.ViewHolder holder, final int position) {
        holder.binData(myList.get(position));
        Jugador jugadorSeleccionado = myList.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Datos recuperados de Firebase
                String nombreDeJugador = jugadorSeleccionado.getNombre();
                String numeroDeJugador = jugadorSeleccionado.getNumero();
                String idJugador = jugadorSeleccionado.getUid();
                String idEquipoJugador = jugadorSeleccionado.getIdEquipo();



                Intent intent = new Intent(view.getContext(), InformacionJugadoresActivity.class);
                //Enviar datos del equipo
                //Enviar datos del jugador a la pantalla para editar
                intent.putExtra("nombreDeJugador",nombreDeJugador);
                intent.putExtra("numeroDeJugador",numeroDeJugador);
                intent.putExtra("idJugador",idJugador);
                intent.putExtra("idEquipo",idEquipoJugador);
                mActivity.startActivity(intent);

            }

        });


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
