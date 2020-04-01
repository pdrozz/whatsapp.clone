package com.pedrox.whatsclone.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pedrox.whatsclone.R;
import com.pedrox.whatsclone.activity.StatusActivity;
import com.pedrox.whatsclone.adapter.AdapterStatus;
import com.pedrox.whatsclone.helper.MyPreferences;
import com.pedrox.whatsclone.helper.RecyclerItemClickListener;
import com.pedrox.whatsclone.model.StatusModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatusFragment extends Fragment {


    public StatusFragment() {
        // Required empty public constructor
    }

    private ChildEventListener EventListener;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<StatusModel> listStatus=new ArrayList<>();
    private MyPreferences myPreferences;
    private String userName;
    private DatabaseReference reference= FirebaseDatabase.getInstance().getReference("status");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_status, container, false);
        recyclerView=v.findViewById(R.id.recyclerStatus);

        myPreferences=new MyPreferences(getActivity());
        userName=myPreferences.recuperarPreferencia("nameUser");


        ConfigInicial();
        InicializarListener();
        RecyclerClick();
        gerarLista();

        AdapterStatus adapter=new AdapterStatus(listStatus);

        layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        //recyclerView.addItemDecoration(new RecyclerView.ItemDecoration);





        return v;
    }
    private void gerarLista(){

        reference.addChildEventListener(EventListener);
    }

    private void RecyclerClick(){

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity()
                , recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position==0){
                    Intent i=new Intent(getActivity(), StatusActivity.class);
                    i.putExtra("myStatus",true);
                    startActivity(i);
                }
                else {
                    Intent i=new Intent(getActivity(), StatusActivity.class);
                    i.putExtra("obj",listStatus.get(position));
                    startActivity(i);

                }
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

    private void ConfigInicial(){
        String foto=myPreferences.recuperarPreferencia("urlFoto");
        if(foto!=null) {
            StatusModel statusModel = new StatusModel("Meu Status", "Toque para atualizar seu status", foto, null, null);
            listStatus.add(statusModel);
        }else {
            StatusModel statusModel = new StatusModel("Meu Status", "Toque para atualizar seu status", "zero", null, null);
            listStatus.add(statusModel);
        }


    }

    private void InicializarListener(){

        EventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()){
                    StatusModel status=dataSnapshot.getValue(StatusModel.class);
                    listStatus.add(status);

                    recyclerView.setAdapter(new AdapterStatus(listStatus));
                }
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
        };
    }

}
