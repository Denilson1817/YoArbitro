package com.denilsonperez.yoarbitro;

import android.provider.BaseColumns;

public class DatosJugadoresContract {
    private DatosJugadoresContract(){

    }
    public static class DatosJugadoresTab implements BaseColumns{
        public static final String TABLE_NAME = "DatosJugadores";
        public static final String COLUMN_ID = "ID";
        public static final String COLUMN_Amonestado = "Amonestado";
        //DATOS A AGREGAR EN UN FUTURO
        //public static final String COLUMN_Expulsado = "Expulsado";
        //public static final String COLUMN_Goles = "Goles";
    }
}
