package com.pedrox.whatsclone.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pedrox.whatsclone.R;
import com.pedrox.whatsclone.model.MensagensModel;
import java.util.List;

public class AdapterMensagens extends RecyclerView.Adapter<AdapterMensagens.MeuViewHolder> {

    private List<MensagensModel> listMensagens;

    public AdapterMensagens(List<MensagensModel> listMensagens) {
        this.listMensagens = listMensagens;
    }

    @NonNull
    @Override
    public MeuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.message_layout,parent,false);

        return new MeuViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MeuViewHolder holder, int position) {
        MensagensModel model=listMensagens.get(position);
        if(model.getTipo().equals("enviada")){
            //se a mensagem for do tipo enviada ele configura o layout de enviado
            holder.txtRecebida.setVisibility(View.GONE);
            holder.txtEnviada.setText(model.getMensagem());
        }else{
            //se for recebido configura o layout recebido
            holder.txtEnviada.setVisibility(View.GONE);
            holder.txtRecebida.setText(model.getMensagem());
        }


    }

    @Override
    public int getItemCount() {
        return listMensagens.size();
    }

    public class MeuViewHolder extends RecyclerView.ViewHolder{
            TextView txtEnviada,txtRecebida;
        public MeuViewHolder(@NonNull View itemView) {
            super(itemView);
            txtEnviada=itemView.findViewById(R.id.txtMensagemEnviada);
            txtRecebida=itemView.findViewById(R.id.txtMensagemRecebida);


        }
    }

}
