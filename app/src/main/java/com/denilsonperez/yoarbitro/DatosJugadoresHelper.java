package com.denilsonperez.yoarbitro;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import androidx.annotation.Nullable;

public class DatosJugadoresHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
    public  static final String DATABASE_NAME = "DatosJugadores.db";
    public static final String SQL_CREATE_DATOSJUGADORES = "CREATE TABLE "+
            DatosJugadoresContract.DatosJugadoresTab.TABLE_NAME+
            " (" + BaseColumns._ID + " INTEGER PRIMARY KEY," +
            DatosJugadoresContract.DatosJugadoresTab.COLUMN_Amonestado+ " INTEGER," +
            //DatosJugadoresContract.DatosJugadoresTab.COLUMN_Expulsado+ " INTEGER," +
            DatosJugadoresContract.DatosJugadoresTab.COLUMN_ID+ " INTEGER)";
    public static final String SQL_DELETE_DATOSJUGADORES = "DROP TABLE IF EXISTS "+
            DatosJugadoresContract.DatosJugadoresTab.TABLE_NAME;

    public DatosJugadoresHelper(@Nullable Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_DATOSJUGADORES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_DATOSJUGADORES);
        onCreate(sqLiteDatabase);
    }
}
