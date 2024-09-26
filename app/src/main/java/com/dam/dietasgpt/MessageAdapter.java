package com.dam.dietasgpt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> listaMensajes;

    public MessageAdapter(List<Message> listaMensajes) {
        this.listaMensajes = listaMensajes;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message mensaje = listaMensajes.get(position);
        holder.bind(mensaje);
    }

    @Override
    public int getItemCount() {
        return listaMensajes.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView textoMensajeUsuario, textoMensajeChatbot;

        public MessageViewHolder(View itemView) {
            super(itemView);
            textoMensajeUsuario = itemView.findViewById(R.id.textoMensajeUsuario);
            textoMensajeChatbot = itemView.findViewById(R.id.textoMensajeChatbot);
        }

        public void bind(Message mensaje) {
            if (mensaje.getRole().equals("user")) {
                textoMensajeUsuario.setVisibility(View.VISIBLE);
                textoMensajeUsuario.setText(mensaje.getContent());
                textoMensajeChatbot.setVisibility(View.GONE);
            } else {
                textoMensajeChatbot.setVisibility(View.VISIBLE);
                textoMensajeChatbot.setText(mensaje.getContent());
                textoMensajeUsuario.setVisibility(View.GONE);
            }
        }
    }
}

