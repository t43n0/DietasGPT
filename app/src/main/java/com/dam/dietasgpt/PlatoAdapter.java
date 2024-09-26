package com.dam.dietasgpt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PlatoAdapter extends BaseAdapter {

    private Context context;
    private int resource;
    private List<Plato> items;

    public PlatoAdapter(Context context, int resource, List<Plato> items) {
        this.context = context;
        this.resource = resource;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Plato getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, parent, false);
        }

        ImageView imagenPlato = convertView.findViewById(R.id.imagenPlato);
        TextView nombrePlato = convertView.findViewById(R.id.nombrePlato);
        TextView descripcionPlato = convertView.findViewById(R.id.descripcionPlato);

        Plato plato = getItem(position);

        nombrePlato.setText(plato.getNombre());
        descripcionPlato.setText(plato.getDescripcion());

        // Cargar imagen con Glide
        Glide.with(context)
                .load(plato.getImagenUrl())
                .into(imagenPlato);

        return convertView;
    }
}
