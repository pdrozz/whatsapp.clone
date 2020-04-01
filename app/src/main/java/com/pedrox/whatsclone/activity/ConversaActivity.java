package com.pedrox.whatsclone.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.net.ConnectivityManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.pedrox.whatsclone.R;
import com.pedrox.whatsclone.adapter.AdapterMensagens;
import com.pedrox.whatsclone.helper.MyPreferences;
import com.pedrox.whatsclone.model.ContatosModel;
import com.pedrox.whatsclone.model.MensagensModel;
import com.pedrox.whatsclone.sqlite.MensagemContatoDAO;
import com.pedrox.whatsclone.utils.Datetime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class ConversaActivity extends AppCompatActivity {

    private Toolbar toolBar;
    private FloatingActionButton fab;
    private RecyclerView recyclerMensagens;
    private EditText campoTexto;
    private List<MensagensModel> listMensagens=new ArrayList<>();
    private DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
    private String Username;
    private ChildListenerMessage listener;
    private ChildListenerStats listenerStats;
    private String IDCONTATO;
    private AdapterMensagens adapter;
    private ProgressBar bar;
    private String idUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa);

        campoTexto=findViewById(R.id.txtCampoTexto);
        recyclerMensagens=findViewById(R.id.recyclerMessage);
        fab=findViewById(R.id.floatActionEnviar);



        //config toolbar
        toolBar=findViewById(R.id.toolbarConversa);
        setSupportActionBar(toolBar);
        //config ícone de voltar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Bundle dados = getIntent().getExtras();

        //nome do contato
        final String nameContato=dados.getString("nameContato");
        //id do contato
        IDCONTATO=dados.getString("idContato");
        getSupportActionBar().setTitle(nameContato);

        //TODO
        //set user online
        MyPreferences myPreferences=new MyPreferences(getSharedPreferences(MyPreferences.PREFERENCIASLOCAIS,0));
        idUser=myPreferences.recuperarPreferencia("idUser");
        //reference.child("contato").child(idUser).child("stats").setValue("offline");

        //configurar adapter
        final String table="table"+IDCONTATO;
        System.out.println("TABLE CONVERSA "+table);
        recuperarMensagens(IDCONTATO);
        adapter=new AdapterMensagens(listMensagens);

        //configurar recycler
        RecyclerView.LayoutManager layoutManager;
        layoutManager= new LinearLayoutManager(this);

        recyclerMensagens.setLayoutManager(layoutManager);
        //recyclerMensagens.setHasFixedSize(true);

        recyclerMensagens.setAdapter(adapter);
        //verifica se existe pelo menos uma mensagem na lista de mensagens, se existir scroolto
        if(listMensagens.size()>0){
            recyclerMensagens.smoothScrollToPosition(listMensagens.size()-1);
        }

        final String nameUser=myPreferences.recuperarPreferencia("nameUser");
        this.Username=nameUser;

        //floatActionButton send message
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //verifica se o usuário está logado
                if(nameUser!=null){
                    String mensagem=campoTexto.getText().toString();
                String tipo="enviada";
                MensagensModel model=new MensagensModel(mensagem,tipo);
                MensagemContatoDAO mensagemContatoDAO =new MensagemContatoDAO(getApplicationContext());
                model.setDate(Datetime.getDateToday());
                //verifica se conseguiu salvar a mensagem
                if(mensagemContatoDAO.inserirMensagem(model,table)){
                    Toast.makeText(getApplicationContext(), "Enviado com sucesso",Toast.LENGTH_SHORT).show();
                    listMensagens.add(new MensagensModel(mensagem,tipo));
                    //recupera as mensagem
                    recuperarMensagens(IDCONTATO);
                    recyclerMensagens.setAdapter(new AdapterMensagens(listMensagens));
                    if(listMensagens.size()>0){
                    recyclerMensagens.smoothScrollToPosition(listMensagens.size()-1);}
                    campoTexto.setText("");
                    //envia a mensagem
                    model.setTipo("recebida");
                    String mId=UUID.randomUUID().toString().substring(1,3);
                    model.setMid(mId);
                    reference.child("user").child(IDCONTATO).child(idUser).setValue(model);}
                    else{
                        Toast.makeText(getApplicationContext(),"Você precisa escolher um nome de usuário primeiro",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        listener=new ChildListenerMessage(getApplicationContext(),"CONVERSA ACTIVITY",0);
        listenerStats=new ChildListenerStats();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contato,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void recuperarMensagens(String contato){
        MensagemContatoDAO mensagemContatoDAO =new MensagemContatoDAO(getApplicationContext());
        listMensagens= mensagemContatoDAO.listarMensagens("table"+contato);
        //atualiza e preenche a lista de mensagens
    }

    public void setStatsContato(ChildEventListener childEventListener,int ifAction){
        DatabaseReference aux=reference.child("contato").child(IDCONTATO);
        Query stats=aux;

        if (ifAction==0){
            stats.addChildEventListener(childEventListener);
        }else {
            stats.removeEventListener(childEventListener);
        }
    }

    private void fireListener(ChildEventListener childEventListener,int i){
        //i=0 adicionar listener
        //i!=0 remover listener
        MyPreferences myPreferences=new MyPreferences(getSharedPreferences(MyPreferences.PREFERENCIASLOCAIS,0));
        String idUser=myPreferences.recuperarPreferencia("idUser");
        DatabaseReference r=reference.child("user").child(idUser);
        Query query=r;
        if(i==0){
        query.addChildEventListener(childEventListener);}
        else {
            query.removeEventListener(childEventListener);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //adicionando listener
        fireListener(listener,0);
        //setStatsContato(listenerStats,0);


        reference.child("contato").child(idUser).child("stats").setValue("online");
        reference.child("contato").child(idUser).child("last_login").setValue(Datetime.getDateToday());
    }

    @Override
    protected void onStop() {
        super.onStop();
        //removendo listener
        fireListener(listener,1);
        //setStatsContato(listenerStats,1);

        reference.child("contato").child(idUser).child("stats").setValue("offline");
    }

    private class ChildListenerStats implements ChildEventListener{
        private String last=null;
        private ContatosModel contato;
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            if(dataSnapshot.exists()){
                if (dataSnapshot.getKey().equals("last_login")){
                    last=dataSnapshot.getValue().toString();
                }
                if(dataSnapshot.getKey().equals("stats")){
                    String stats=dataSnapshot.getValue().toString();
                    if (stats.equals("offline")){
                        String[] aux=last.split("/");
                        last=aux[1]+"/"+aux[0]+"/"+aux[2];
                        getSupportActionBar().setSubtitle("Visto: "
                                +last);
                    }else {
                        getSupportActionBar().setSubtitle("Online");
                    }

                }
            }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            String last=null;
            if(dataSnapshot.exists()){
                if (dataSnapshot.getKey().equals("last_login")){
                    last=dataSnapshot.getValue().toString();
                }
                if(dataSnapshot.getKey().equals("stats")){
                    String stats=dataSnapshot.getValue().toString();
                    if (stats.equals("offline")){
                        String[] aux=last.split("/");
                        last=aux[1]+"/"+aux[0]+"/"+aux[2];
                        getSupportActionBar().setSubtitle("Offline - "
                                +last);
                    }else {
                        getSupportActionBar().setSubtitle("Online");
                    }

                }
            }
    }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }

    public class ChildListenerMessage implements ChildEventListener{

        private Context c;
        //who is the class what call this class
        private String who;
        private int action;

        public ChildListenerMessage(Context c,String who,int action) {
            this.c = c;
            this.who=who;
            this.action=action;

        }

        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            //verifica se a dataSnaphot existe
            if (dataSnapshot.exists()) {
                System.out.println(dataSnapshot.getRef().toString());

                    if (!dataSnapshot.getKey().equals("nome")) {
                        //conector
                        System.out.println("ERROR MODEL datasnapshotstring" + dataSnapshot.toString());
                        MensagemContatoDAO dao = new MensagemContatoDAO(c);
                        //getValue retorna o valor no model
                        MensagensModel mensagem = dataSnapshot.getValue(MensagensModel.class);
                        System.out.println("ERROR MODEL" + mensagem.getMensagem());
                        System.out.println("ERROR MODEL" + dataSnapshot.toString());
                        //key =table para inserir as mensagens recebidas
                        String key = "table" + dataSnapshot.getKey();
                        //mid= Message ID
                        String mid;
                        //recupera o mid da ultima mensagem no banco
                        if (dao.retornarLastMessage(key) == null) {
                            mid = UUID.randomUUID().toString().substring(1, 3);
                            //se for nulo é uma versão antiga do banco de dados
                        } else {
                            mid = dao.retornarLastMessage(key).getMid();
                            //caso contrario mid é igual o retorno
                        }
                        //verifica se a ultima mensagem recebida tem o mesmo id da nova mensagem recebida
                        //se tiver ele não insere novamento
                        //essa verificação é necessária pois o metodo onChildAdded é chamado toda vez que a Activity é iniciada
                        if (mid.equals(mensagem.getMid())) {
                            //se o mid for diferente insere a mensagem
                            dao.inserirMensagem(mensagem, key);
                            Toast.makeText(c, "mensagem recebida", Toast.LENGTH_SHORT).show();
                            //se a mensagem recebida for da pessoa que está na tela de contato
                            // atualiza a lista de mensagens para exibir a nova mensagem
                            if (dataSnapshot.getKey().equals(IDCONTATO)) {
                                //recupera a mensagem do banco
                                recuperarMensagens(IDCONTATO);
                                adapter.notifyDataSetChanged();
                                //deixa o recyclerView exibindo o último item
                                recyclerMensagens.smoothScrollToPosition(listMensagens.size() - 1);

                            }
                        }
                    }

                }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (dataSnapshot.exists()) {
                    System.out.println("IF KEY:" + dataSnapshot.getKey());
                    MensagemContatoDAO dao = new MensagemContatoDAO(c);
                    MensagensModel mensagem = dataSnapshot.getValue(MensagensModel.class);
                    System.out.println(dataSnapshot.getKey());
                    String key = "table" + dataSnapshot.getKey();
                    dao.inserirMensagem(mensagem, key);
                    System.out.println("MESSAGE LISTENER KEY:" + key + " MENSAGEM:" + mensagem.getMensagem());
                    Toast.makeText(c, "mensagem recebida", Toast.LENGTH_SHORT).show();

                    if (dataSnapshot.getKey().equals(IDCONTATO)) {
                        recuperarMensagens(IDCONTATO);
                        recyclerMensagens.setAdapter(new AdapterMensagens(listMensagens));
                        recyclerMensagens.smoothScrollToPosition(listMensagens.size() - 1);
                    }
                }

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
}}
