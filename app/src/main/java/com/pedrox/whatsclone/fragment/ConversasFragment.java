package com.pedrox.whatsclone.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;
import com.pedrox.whatsclone.R;
import com.pedrox.whatsclone.activity.ConversaActivity;
import com.pedrox.whatsclone.adapter.AdapterConversas;
import com.pedrox.whatsclone.adapter.AdapterMensagens;
import com.pedrox.whatsclone.helper.MyPreferences;
import com.pedrox.whatsclone.helper.RecyclerItemClickListener;
import com.pedrox.whatsclone.model.ContatosModel;
import com.pedrox.whatsclone.model.ConversasModel;
import com.pedrox.whatsclone.model.MensagensModel;
import com.pedrox.whatsclone.sqlite.MensagemContatoDAO;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversasFragment extends Fragment {


    public ConversasFragment() {
        // Required empty public constructor
    }
    private List<ConversasModel> listContatos=new ArrayList<>();
    private Button b;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MensagemContatoDAO dao;
    private AdapterConversas recyclerConversasAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_conversas, container, false);
        recyclerView=v.findViewById(R.id.recyclerViewConversas);


        //configurar adapter
        recyclerConversasAdapter= new AdapterConversas(listContatos);

        //configurar recycler
        layoutManager= new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerConversasAdapter);

        //setOnRecyclerClickListener
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String nameContato=listContatos.get(position).getName();
                        String id=listContatos.get(position).getId();
                        Intent i=new Intent(getActivity(),ConversaActivity.class);
                        i.putExtra("nameContato",nameContato);
                        i.putExtra("idContato",id);
                        startActivity(i);
                    }
                    @Override
                    public void onLongItemClick(View view, int position) {
                        String id=listContatos.get(position).getId();
                        String nameContato=listContatos.get(position).getName();
                        //Toast.makeText(getActivity(),id,Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(),id+"",Toast.LENGTH_SHORT).show();
                        alertDialog(nameContato,id,position);


                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }));

        return v;
    }

    private void alertDialog(final String nameContato,final String id,final int position){
        Context c=getActivity();

        //Dao
        final MensagemContatoDAO dao=new MensagemContatoDAO(c);

        AlertDialog.Builder builder=new AlertDialog.Builder(c);
        builder.setTitle("Deseja excluir a conversa com "+nameContato);
        builder.setMessage("Essa ação não pode ser desfeita");
        builder.setCancelable(false);
        builder.setPositiveButton("sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    dao.getEscrever().execSQL("DELETE FROM table"+id+" WHERE 1=1;");

                    listContatos.remove(position);
                    AdapterConversas adapter=new AdapterConversas(listContatos);
                    recyclerView.setAdapter(adapter);

            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alert=builder.create();
        alert.show();


    }

    private void gerarListaContatos() {
        //sqlite Conector
        dao=new MensagemContatoDAO(getActivity());
        //List of Contatos Model
        List<ContatosModel> contatos;
        contatos=dao.listarContatos();
        ContatosModel contatosModel;
        //current user name
        MyPreferences preference=new MyPreferences(getActivity().getSharedPreferences(MyPreferences.PREFERENCIASLOCAIS,0));;
        String idUser=preference.recuperarPreferencia("idUser");

        for (int a=0;a<contatos.size();a++){
            contatosModel=contatos.get(a);
            String name=contatosModel.getNome();
            String foto=contatosModel.getFoto();
            String id=contatosModel.getId();
            System.out.println("CONTATOS1 "+name+"ID:"+id);
            //só adiciona o contato na lista se o id do contato for != do current id user
            if(!id.equals(idUser)){
                System.out.println("LISTALISTA CONVERSASFRAGMENT NAME:"+name+"ID:"+id);
                MensagensModel ultimaMensagemModel=dao.retornarLastMessage(id);
                if (ultimaMensagemModel!=null){

                //se o contato já estiver na lista ele não adiciona de novo
                if (!listContatos.contains(contatosModel)){

                    String mensagem;
                    if (ultimaMensagemModel==null){
                        mensagem="Envie uma mensagem para iniciar a conversa";
                    }else {
                        if (ultimaMensagemModel.getTipo().equals("recebida")){
                            mensagem=ultimaMensagemModel.getMensagem();
                        }else {

                            mensagem="Você: "+ultimaMensagemModel.getMensagem();
                        }

                        }
                    System.out.println("ULTIMA MENSAGEM:  "+mensagem);
            listContatos.add(new ConversasModel(name,"yesterday","2",mensagem,foto,id));
                    AdapterConversas recyclerConversasAdapter= new AdapterConversas(listContatos);
                    recyclerView.setAdapter(recyclerConversasAdapter);
                }
            }else System.out.println("TABLE ListContatos já contem:"+name+"ID:"+id);}

        }

    }

    @Override
    public void onStart() {
        super.onStart();
        gerarListaContatos();
        System.out.println("ONSTART FRAGMENT");
    }

    @Override
    public void onStop() {
        super.onStop();
        listContatos.clear();
        System.out.println("ONSTOP FRAGMENT");
    }
}
