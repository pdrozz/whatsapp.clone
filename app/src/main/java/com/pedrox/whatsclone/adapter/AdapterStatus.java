package com.pedrox.whatsclone.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pedrox.whatsclone.R;
import com.pedrox.whatsclone.model.StatusModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterStatus extends RecyclerView.Adapter<AdapterStatus.MeuViewHolder> {

    List<StatusModel> listStatus;

    public AdapterStatus(List<StatusModel> listStatus) {
        this.listStatus = listStatus;
    }

    @NonNull
    @Override
    public MeuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_contatos,parent,false);
        return new MeuViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MeuViewHolder holder, int position) {
        StatusModel model=listStatus.get(position);
        String nomeContato=model.getNome();
        String dataStatus=model.getData();
        String urlFoto=model.getFoto();
        String UrlStatus=model.getUrlStatus();

        holder.data.setText(dataStatus);
        holder.nome.setText(nomeContato);
        if(urlFoto==null){
            holder.fotoPerfil.setImageResource(R.drawable.avatar1);
        }else {
            if (urlFoto.equals("zero")){holder.fotoPerfil.setImageResource(R.drawable.avatar1);}
        else {Picasso.get().load(urlFoto).into(holder.fotoPerfil);}}

    }

    @Override
    public int getItemCount() {
        return listStatus.size();
    }

    public class MeuViewHolder extends RecyclerView.ViewHolder{
        TextView nome,data;
        ImageView fotoPerfil;

        public MeuViewHolder(@NonNull View itemView) {
            super(itemView);
            nome=itemView.findViewById(R.id.txtnomeContato);
            data=itemView.findViewById(R.id.txtsignup);
            fotoPerfil=itemView.findViewById(R.id.imagemPerfilContato);
        }
    }


}


