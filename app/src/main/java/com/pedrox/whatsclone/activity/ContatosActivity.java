package com.pedrox.whatsclone.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pedrox.whatsclone.R;
import com.pedrox.whatsclone.adapter.AdapterContatos;
import com.pedrox.whatsclone.helper.MyPreferences;
import com.pedrox.whatsclone.helper.RecyclerItemClickListener;
import com.pedrox.whatsclone.model.ContatosModel;
import com.pedrox.whatsclone.sqlite.MensagemContatoDAO;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ContatosActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference reference= FirebaseDatabase.getInstance().getReference("contato");
    private List<ContatosModel> listContatos=new ArrayList<>();
    private ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contatos);

        MyPreferences myPreferences=new MyPreferences(getSharedPreferences(MyPreferences.PREFERENCIASLOCAIS,0));
        String idUser=myPreferences.recuperarPreferencia("idUser");


        //config inicial
        toolbar=findViewById(R.id.toolbarContatos);
        recyclerView=findViewById(R.id.recyclerContatos);
        bar=findViewById(R.id.progressBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Contatos");

        //listener Firebase
        gerarListContatos();


        layoutManager= new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);






    }

    private void criarTableContato(List<ContatosModel> list){
        MensagemContatoDAO dao=new MensagemContatoDAO(getApplicationContext());
        dao.inserirContatos(list);
    }

    private void recyclerListener(final List<ContatosModel> list){

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(),
                recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent i=new Intent(getApplicationContext(),ConversaActivity.class);
                i.putExtra("nameContato",list.get(position).getNome());
                i.putExtra("idContato",list.get(position).getId());
                startActivity(i);
                finish();

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }
        ));
    }

    private void gerarListContatos() {
        Query q=reference;


        q.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                MyPreferences myPreferences=new MyPreferences(getSharedPreferences(MyPreferences.PREFERENCIASLOCAIS,0));
                String idUser=myPreferences.recuperarPreferencia("idUser");

                ContatosModel contatosModel=dataSnapshot.getValue(ContatosModel.class);
                if (contatosModel.getId().equals(idUser)){}else{
                listContatos.add(contatosModel);}
                AdapterContatos adapter=new AdapterContatos(listContatos);
                recyclerView.setAdapter(adapter);
                criarTableContato(listContatos);
                recyclerListener(listContatos);
                bar.setVisibility(View.GONE);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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
        });



    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
