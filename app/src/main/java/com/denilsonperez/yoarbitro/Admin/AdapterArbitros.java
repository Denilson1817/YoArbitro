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
import com.denilsonperez.yoarbitro.modelo.Arbitro;
import com.denilsonperez.yoarbitro.modelo.Equipo;
import com.denilsonperez.yoarbitro.modelo.Jugador;

import java.util.List;

public class AdapterArbitros extends RecyclerView.Adapter<AdapterArbitros.ViewHolder> {
     List<Arbitro> myList;


    private ConsultarArbitrosActivity mActivity;
    private Context context;
    private LayoutInflater inflater;
    public AdapterArbitros(List<Arbitro> myList, Context context, ConsultarArbitrosActivity activity) {
        this.inflater=LayoutInflater.from(context);
        this.myList = myList;
        this.context=context;
       this.mActivity= activity;

    }
    @NonNull
    @Override
    public AdapterArbitros.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_elementos_arbitros, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final AdapterArbitros.ViewHolder holder, final int position) {
        holder.binData(myList.get(position));


    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {

         TextView textViewNombreArbitro;
         TextView textViewLocalidad;
         TextView textViewEdad;

         ViewHolder( View itemView) {
            super(itemView);
             textViewNombreArbitro = itemView.findViewById(R.id.nombreDelEquipo);
             textViewLocalidad = itemView.findViewById(R.id.nombreDelegado);
             textViewEdad = itemView.findViewById(R.id.edad);
        }

        void binData(final Arbitro item){
            textViewNombreArbitro.setText(item.getNombre());
            textViewLocalidad.setText(item.getLocalidad());
            textViewEdad.setText((item.getEdad()));

        }
    }
}
