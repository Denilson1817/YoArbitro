package com.denilsonperez.yoarbitro;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.denilsonperez.yoarbitro.modelo.Equipo;
import com.denilsonperez.yoarbitro.modelo.Jugador;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdapterJugadoresEquipo1 extends RecyclerView.Adapter<AdapterJugadoresEquipo1.ViewHolder> {
    private List<Jugador> myList;
    private JugadoresEquipo1Activity mActivity;
    private Context context;
    private LayoutInflater inflater;
    private int selectedItemPosition = -1;
    private OnCheckedChangeListener onCheckedChangeListener;

    private SparseBooleanArray checkedItems = new SparseBooleanArray();

    public interface OnCheckedChangeListener {
        void onItemCheckedChange(int position, boolean isChecked);
    }

    public List<Jugador> getJugadoresSeleccionados() {
        List<Jugador> jugadoresSeleccionados = new ArrayList<>();
        for (int i = 0; i < myList.size(); i++) {
            if (isChecked(i)) {
                jugadoresSeleccionados.add(myList.get(i));
            }
        }
        return jugadoresSeleccionados;
    }



    public AdapterJugadoresEquipo1(List<Jugador> myList, Context context, JugadoresEquipo1Activity activity) {
        this.myList = myList;
        this.context = context;
        this.mActivity = activity;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public AdapterJugadoresEquipo1.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.lista_elementos_jugadores_multiple_choice, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdapterJugadoresEquipo1.ViewHolder holder,int position) {
        holder.bindData(myList.get(position));

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(checkedItems.get(position, false));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                checkedItems.put(position, isChecked);
                if (onCheckedChangeListener != null) {
                    onCheckedChangeListener.onItemCheckedChange(position, isChecked);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return myList.size();
    }
    public Jugador getItem(int position) {
        return myList.get(position);
    }


    public boolean isChecked(int position) {
        return checkedItems.get(position, false);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombreJugador;
        TextView textViewNumeroeJugador;
        CheckBox checkBox;

        ViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.seleccion);
            textViewNombreJugador = itemView.findViewById(R.id.nombreDelEquipo);
            textViewNumeroeJugador = itemView.findViewById(R.id.nombreDelegado);
        }

        void bindData(final Jugador item) {
            textViewNombreJugador.setText(item.getNombre());
            textViewNumeroeJugador.setText(item.getNumero());
        }
    }
}
