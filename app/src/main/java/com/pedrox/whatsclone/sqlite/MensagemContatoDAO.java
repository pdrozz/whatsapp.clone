package com.pedrox.whatsclone.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pedrox.whatsclone.helper.DAO;
import com.pedrox.whatsclone.helper.SqlHelper;
import com.pedrox.whatsclone.model.ContatosModel;
import com.pedrox.whatsclone.model.MensagensModel;

import java.util.ArrayList;
import java.util.List;

public class MensagemContatoDAO implements DAO {

    private Context context;
    private SQLiteDatabase ler,escrever;
    private SqlHelper helper;

    public SQLiteDatabase getEscrever() {
        return escrever;
    }

    public void inserirContatos(List<ContatosModel> listContato){
        for (int i=0;i<listContato.size();i++){
            String id=listContato.get(i).getId();
            String nome=listContato.get(i).getNome();
            String foto=listContato.get(i).getFoto();

            ContentValues cv=new ContentValues();
            cv.put("id",id);
            cv.put("nome",nome);
            cv.put("urlfoto",foto);
            try{
            getEscrever().insert(SqlHelper.TABELA_CONTATOS,null,cv);}
            catch (Exception e){
                e.printStackTrace();
            }


        }
        gerarTableContatosMensagem(listarContatos());
    }

    public boolean gerarTableContatosMensagem(List<ContatosModel> listContato){

        try {
            for (int i = 0; i < listContato.size(); i++) {
                String id = listContato.get(i).getId();
                getEscrever().execSQL("CREATE TABLE IF NOT EXISTS table" + id + " (" +
                      "id INTEGER PRIMARY KEY AUTOINCREMENT, "+  "mensagem VARCHAR not null, tipo VARCHAR not null, data DATE not null," +
                        " mid VARCHAR);");
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    public boolean inserirMensagem(MensagensModel mensagem,String table){
        try{
            ContentValues cv=new ContentValues();
            cv.put("mensagem",mensagem.getMensagem());
            cv.put("tipo",mensagem.getTipo());
            cv.put("data",mensagem.getDate());
            cv.put("mid",mensagem.getMid());


            escrever.insert(table,null,cv);
            return true;
        }catch (Exception e){
            e.printStackTrace();}


        return false;
    }



    public MensagemContatoDAO(Context context) {
        this.context = context;
        this.helper=new SqlHelper(this.context);
        ler=helper.getReadableDatabase();
        escrever=helper.getWritableDatabase();
    }

    @Override
    public boolean inserirMensagem(String mensagem, String tipo,String table,String data) {
        try{
        ContentValues cv=new ContentValues();
        cv.put("mensagem",mensagem);
        cv.put("tipo",tipo);
        cv.put("data",data);
        escrever.insert(table,null,cv);
        return true;
        }
        catch (Exception e){
            return false;}


    }

    @Override
    public boolean inserirContato(String nome,int foto) {
        return false;
    }

    @Override
    public boolean remover() {
        return false;
    }

    @Override
    public List<MensagensModel> listarMensagens(String contato) {
        List<MensagensModel> mensagens=new ArrayList<>();
        String sql="SELECT id,mensagem,tipo,data FROM "+contato+";";

        Cursor c=ler.rawQuery(sql,null);

        while(c.moveToNext()){
            MensagensModel mensagem=new MensagensModel();
            //String idString=c.getString(c.getColumnIndex("id"));
            String mensagemString=c.getString(c.getColumnIndex("mensagem"));
            String tipoString=c.getString(c.getColumnIndex("tipo"));
            String dataString=c.getString(c.getColumnIndex("data"));
            int idMensagem=c.getInt(c.getColumnIndex("id"));

            mensagem.setMensagem(mensagemString);
            mensagem.setTipo(tipoString);
            mensagem.setDate(dataString);
            mensagem.setId(idMensagem);
            mensagens.add(mensagem);


        }
        c.close();
        System.out.println(mensagens);
        return mensagens;
    }

    public MensagensModel retornarLastMessage(String contato) {
        String sql="SELECT id,mensagem,tipo,data,mid FROM table"+contato +" WHERE id = (SELECT MAX( id ) FROM table"+contato+");";
        System.out.println("RETORNAR ULTIMA MENSAGEM SQL: "+sql);
        try{
            String mensagemString;
            Cursor c=ler.rawQuery(sql,null);
            if (c.getCount()>0){
                c.moveToFirst();
                MensagensModel mensagem=new MensagensModel();
                mensagemString=c.getString(c.getColumnIndex("mensagem"));
                String tipoString=c.getString(c.getColumnIndex("tipo"));
                String dataString=c.getString(c.getColumnIndex("data"));
                mensagem.setMensagem(mensagemString);
                mensagem.setTipo(tipoString);
                mensagem.setDate(dataString);

                c.close();
                return mensagem;
            }
            else {
                c.close();
                return null;
            }
        }
        catch (Exception e){
            return null;
        }
    }



    public List<ContatosModel> listarContatos() {
        List<ContatosModel> contatos=new ArrayList<>();
        String sql="SELECT id,nome,urlfoto FROM "+helper.TABELA_CONTATOS+";";

        Cursor c=ler.rawQuery(sql,null);

        while(c.moveToNext()){
            ContatosModel contato=new ContatosModel();
            String id=c.getString(c.getColumnIndex("id"));
            String nome=c.getString(c.getColumnIndex("nome"));
            String foto =c.getString(c.getColumnIndex("urlfoto"));

            contato.setId(id);
            contato.setNome(nome);
            contato.setFoto(foto);
            contatos.add(contato);
        }

        System.out.println("LISTADECONTATOS"+contatos);
        c.close();
        return contatos;
    }


}
