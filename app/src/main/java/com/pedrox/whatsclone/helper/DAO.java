package com.pedrox.whatsclone.helper;

import com.pedrox.whatsclone.model.ContatosModel;
import com.pedrox.whatsclone.model.MensagensModel;

import java.util.List;

public interface DAO {

    public boolean inserirMensagem(String dado, String tipo,String table,String data);

    public boolean inserirContato(String nome,int foto);

    public boolean remover();

    public List<MensagensModel> listarMensagens(String contato);
    public List<ContatosModel> listarContatos();

    //public boolean update();
}
