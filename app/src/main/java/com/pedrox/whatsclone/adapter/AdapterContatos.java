package com.pedrox.whatsclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pedrox.whatsclone.R;
import com.pedrox.whatsclone.model.ContatosModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterContatos extends RecyclerView.Adapter<AdapterContatos.MeuViewHolder> {

    List<ContatosModel> listContatos;
    Context c;

    public AdapterContatos(List<ContatosModel> listContatos) {
        this.listContatos = listContatos;
    }

    @NonNull
    @Override
    public MeuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_contatos,parent,false);
        this.c=parent.getContext();
        return new MeuViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MeuViewHolder holder, int position) {
            ContatosModel contato=listContatos.get(position);
            String urlfoto=contato.getFoto();
            String nomeContato=contato.getNome();

            if(urlfoto.equals("zero")){
                holder.fotoPerfil.setImageResource(R.drawable.avatar1);
            }else {
                System.out.println(nomeContato+" foto no link"+urlfoto);
                Picasso.get().load(urlfoto).into(holder.fotoPerfil);

            }
            holder.signup.setText(contato.getSign_up());
            holder.nomeContato.setText(nomeContato);
    }

    @Override
    public int getItemCount() {
        return listContatos.size();
    }

    public class MeuViewHolder extends RecyclerView.ViewHolder{
        TextView nomeContato;
        TextView signup;
        ImageView fotoPerfil;

        public MeuViewHolder(@NonNull View itemView) {
            super(itemView);

            nomeContato=itemView.findViewById(R.id.txtnomeContato);
            fotoPerfil=itemView.findViewById(R.id.imagemPerfilContato);
            signup=itemView.findViewById(R.id.txtsignup);

        }
    }
}
