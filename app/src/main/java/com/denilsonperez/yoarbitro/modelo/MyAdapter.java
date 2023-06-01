package com.denilsonperez.yoarbitro.modelo;

import static android.app.Activity.RESULT_OK;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.denilsonperez.yoarbitro.MenuPrincipalActivity;
import com.denilsonperez.yoarbitro.R;
import com.denilsonperez.yoarbitro.SeleccionEquiposActivity;

import java.util.List;
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
     List<Equipo> myList;
    private SeleccionEquiposActivity mActivity;
    private Context context;
    private LayoutInflater inflater;
    public MyAdapter(List<Equipo> myList, Context context, SeleccionEquiposActivity activity) {
        this.inflater=LayoutInflater.from(context);
        this.myList = myList;
        this.context=context;
       this.mActivity= activity;

    }
    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_elementos_equipo, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final MyAdapter.ViewHolder holder,final int position) {
        holder.binData(myList.get(position));
        Equipo equipo = myList.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear un Intent para iniciar la nueva actividad
                Intent intent = new Intent(view.getContext(), MenuPrincipalActivity.class);
                // Agregar el texto del nombre del equipo como extra del Intent
                intent.putExtra("SELECTED_TEXT", equipo.getNombre());
                //Agregar id del equipo para recuperar los jugadores pertenecientes al equipo seleccionado
                intent.putExtra("idEquipo", equipo.getUid());
                // Iniciar la nueva actividad
                mActivity.setResult(RESULT_OK, intent);
                mActivity.finish();
            }

        });


    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {

         TextView textViewEquipo;
         TextView textViewDelegado;

         ViewHolder( View itemView) {
            super(itemView);
            textViewEquipo = itemView.findViewById(R.id.nombreDelEquipo);
            textViewDelegado = itemView.findViewById(R.id.nombreDelegado);
        }

        void binData(final Equipo item){
             textViewEquipo.setText(item.getNombre());
             textViewDelegado.setText(item.getDelegado());

        }
    }
}
