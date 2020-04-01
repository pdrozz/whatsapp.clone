package com.pedrox.whatsclone.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SqlHelper extends SQLiteOpenHelper {

    public static String DB_NAME="DATABASEwhatsclone";
    public static int VERSION=5;
    public static String TABELA_CONTATOS="contatosTable";
    public static String TABELA_MENSAGENS="MensagensTable";
    public static String TABELA_MENSAGENS_CONTATO="MensagensContatoTable";


    public SqlHelper(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        String sqlContatos="CREATE TABLE IF NOT EXISTS "+TABELA_CONTATOS+
                " (id VARCHAR PRIMARY KEY,nome VARCHAR NOT NULL, urlfoto VARCHAR)";

        //table para separar mensagens


        //table mensagens teste
        String sqlMensagens="CREATE TABLE IF NOT EXISTS "+TABELA_MENSAGENS+
                " (id INTEGER PRIMARY KEY AUTOINCREMENT, mensagem VARCHAR NOT NULL, tipo VARCHAR NOT NULL)";

        try{
            db.execSQL(sqlContatos);
            db.execSQL(sqlMensagens);

        }catch (Exception e){}
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table "+TABELA_CONTATOS+";");
        db.execSQL("drop table "+TABELA_MENSAGENS+";");
        onCreate(db);
    }
}
