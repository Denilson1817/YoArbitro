package com.denilsonperez.yoarbitro;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.denilsonperez.yoarbitro.modelo.Jugador;

import java.util.ArrayList;
import java.util.List;

public class AdapterJugadoresEquipo2 extends RecyclerView.Adapter<AdapterJugadoresEquipo2.ViewHolder> {
    private List<Jugador> myList;
    private JugadoresEquipo2Activity mActivity;
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



    public AdapterJugadoresEquipo2(List<Jugador> myList, Context context, JugadoresEquipo2Activity activity) {
        this.myList = myList;
        this.context = context;
        this.mActivity = activity;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public AdapterJugadoresEquipo2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.lista_elementos_jugadores_multiple_choice, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdapterJugadoresEquipo2.ViewHolder holder, int position) {
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
