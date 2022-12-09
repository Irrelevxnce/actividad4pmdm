package com.irrelevxnce.actividad4.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    private static final int database_version = 1;
    public static final String database_name = "clientes.db";
    public static final String table_clientes = "clientes";
    public static final String table_tarjetas = "tarjetas";
    String create;
    public static final String ifNotExists = "create table if not exists tarjetas (\n" +
            "    nif char(9),\n" +
            "    numero char(16) not null,\n" +
            "    mes integer not null,\n" +
            "    año integer not null,\n" +
            "    cvc integer not null,\n" +
            "    primary key (nif, numero),\n" +
            "    foreign key (nif) references clientes (nif)\n" +
            ");";

    public DBHelper(@Nullable Context context, String s) {
        super(context, database_name, null, database_version);
        create = s;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(create);
        sqLiteDatabase.execSQL(ifNotExists);
    }

    public void addCliente (String nif, String nombre, String fecha, int estudiante, String sexo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues ct = new ContentValues();
        ct.put("nif", nif);
        ct.put("nombre", nombre);
        ct.put("fecha", fecha);
        ct.put("estudiante", estudiante);
        ct.put("sexo", sexo);
        db.insert(table_clientes, null, ct);
    }

    public void addTarjeta (String nif, String num, int mes, int a, int cvc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues ct = new ContentValues();
        ct.put("nif", nif);
        ct.put("numero", num);
        ct.put("mes", mes);
        ct.put("año", a);
        ct.put("cvc", cvc);
        db.insert(table_tarjetas, null, ct);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE " + table_clientes);
        sqLiteDatabase.execSQL("DROP TABLE " + table_tarjetas);
        onCreate(sqLiteDatabase);
    }
}
