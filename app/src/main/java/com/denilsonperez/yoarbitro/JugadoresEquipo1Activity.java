package com.denilsonperez.yoarbitro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class JugadoresEquipo1Activity extends AppCompatActivity {
String equipoEnviado1;
String equipoEnviado2;
TextView tituloEquipo1;
TextView tituloEquipo2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jugadores_equipo1);
        //se recupera el texto de cada boton a traves de sus variables publicas
        MenuPrincipalActivity equipos = new MenuPrincipalActivity();
        equipoEnviado1=equipos.textoEquipo1;
        equipoEnviado2=equipos.textoEquipo2;

        //recuperamos el id del texto para mostrar el nombre del equipo
        tituloEquipo1 = findViewById(R.id.txtNombreEquipo1);
        tituloEquipo1.setText(equipoEnviado1);
        System.out.println(equipoEnviado1);

    }
}