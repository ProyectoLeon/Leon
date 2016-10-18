package com.limeri.leon.Models;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.limeri.leon.Models.Evaluacion;
import com.limeri.leon.Models.Juego;
import com.limeri.leon.R;

import java.util.List;

public class JuegoAdapter extends ArrayAdapter<Juego> {

    private Activity activity;
    List<Juego> juegos;

    public JuegoAdapter(Activity activity, List<Juego> juegos) {
        super(activity, R.layout.juego_item);
        this.activity = activity;
        this.juegos = juegos;
    }

    static class ViewHolder {
        protected TextView nombre;
        protected EditText puntos;
        int ref;

    }

    public Juego getItem(int position) {
        return juegos.get(position);
    }

    public int getCount() {
        return juegos.size();
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent)  {

        final ViewHolder holder;
        if (convertView == null) {

            holder = new ViewHolder();
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.juego_item, null);
            holder.nombre = (TextView) convertView.findViewById(R.id.nombre);
            holder.puntos = (EditText) convertView.findViewById(R.id.puntos);

            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();
        }


        // importante!!! establecemos el mensaje
        holder.nombre.setText(juegos.get(position).getNombre());
        // viewHolder.categoria.setText("Categoría: " + "categoría");
        //juegos.get(position).setPuntosJuego(Integer.parseInt(viewHolder.puntos.getText().toString()));

       holder.ref = position;

        holder.puntos.setText(juegos.get(position).getPuntosJuego().toString());
        holder.puntos.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {

            }
            @Override
            public void afterTextChanged(Editable arg0) {
                try {
                    Integer puntaje = Integer.valueOf(arg0.toString());

                    juegos.get(holder.ref).setPuntosJuego(puntaje);

                }
                catch (NumberFormatException e){}
            }
        });

       return convertView;
    }


}

