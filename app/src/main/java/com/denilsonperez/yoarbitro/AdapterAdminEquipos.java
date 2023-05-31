package com.denilsonperez.yoarbitro;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.denilsonperez.yoarbitro.Admin.InformacionEquiposActivity;
import com.denilsonperez.yoarbitro.Admin.MenuPrincipalAdminActivity;
import com.denilsonperez.yoarbitro.modelo.Equipo;

import java.util.List;

public class AdapterAdminEquipos extends RecyclerView.Adapter<AdapterAdminEquipos.ViewHolder> {
     List<Equipo> myList;
    //Equipo equipoSeleccionado;

    private MenuPrincipalAdminActivity mActivity;
    private Context context;
    private LayoutInflater inflater;
    public AdapterAdminEquipos(List<Equipo> myList, Context context,MenuPrincipalAdminActivity activity) {
        this.inflater=LayoutInflater.from(context);
        this.myList = myList;
        this.context=context;
       this.mActivity= activity;

    }
    @NonNull
    @Override
    public AdapterAdminEquipos.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_elementos_equipo, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final AdapterAdminEquipos.ViewHolder holder, final int position) {
        holder.binData(myList.get(position));
        Equipo equipoSeleccionado = myList.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Datos recuperados de Firebase
                String nombreEquipo = equipoSeleccionado.getNombre();
                String nombreDelegado = equipoSeleccionado.getDelegado();
                String numDeContacto = equipoSeleccionado.getNumContacto();
                String idEquipo = equipoSeleccionado.getUid();

                Intent intent = new Intent(view.getContext(), InformacionEquiposActivity.class);

                //Enviar datos del equipo
                intent.putExtra("nombreEquipo",nombreEquipo);
                intent.putExtra("nombreDelegado",nombreDelegado);
                intent.putExtra("numDeContacto",numDeContacto);
                intent.putExtra("idEquipo",idEquipo);
                mActivity.startActivity(intent);
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
