package com.pedrox.whatsclone.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pedrox.whatsclone.R;
import com.pedrox.whatsclone.model.ConversasModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AdapterConversas extends RecyclerView.Adapter<AdapterConversas.MeuViewHolder> {

    private List<ConversasModel> list=new ArrayList<>();

    public AdapterConversas(List<ConversasModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MeuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.conversas_layout,parent,false);

        return new MeuViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MeuViewHolder holder, int position) {
        ConversasModel conversas=list.get(position);
        if (conversas.getImagemPerfil().equals("zero")){

            int randomInt=new Random().nextInt(3);
            switch (randomInt){
                case 0:
                    holder.imagePerfil.setImageResource(R.drawable.avatar1);
                    break;
                case 1:
                    holder.imagePerfil.setImageResource(R.drawable.avatar2);
                    break;
                case 2:
                    holder.imagePerfil.setImageResource(R.drawable.avatar3);
                    break;

            }



            }else {//holder.imagePerfil.setImageResource(conversas.getImagemPerfil());
             }
        if (conversas.getDate()==null){
            holder.txtDate.setText("18 mar");
        }else{holder.txtDate.setText(conversas.getDate());}
        if (conversas.getMessage()==null){
            holder.txtConteudo.setText(conversas.getName()+": Hello, it's a test message :D");
        }else{holder.txtConteudo.setText(conversas.getMessage());
        }
        if (conversas.getNewMessage()==null){
            holder.txtNewMessage.setText("1");
        }else{holder.txtNewMessage.setText(conversas.getNewMessage());}
        holder.txtName.setText(conversas.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MeuViewHolder extends  RecyclerView.ViewHolder{
        TextView txtName,txtConteudo,txtNewMessage,txtDate;
        ImageView imagePerfil;
    public MeuViewHolder(@NonNull View itemView) {
        super(itemView);
        txtName=itemView.findViewById(R.id.textName);
        txtConteudo=itemView.findViewById(R.id.txtConteudo);
        txtNewMessage=itemView.findViewById(R.id.txtCountNewMessage);
        txtDate=itemView.findViewById(R.id.txtDate);
        imagePerfil=itemView.findViewById(R.id.img_perfil);
    }
}

}
