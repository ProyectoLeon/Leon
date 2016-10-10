package com.limeri.leon.Models;

import android.app.Activity;
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

public class JuegoAdapter extends ArrayAdapter<Evaluacion> {

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

    }

    public int getCount() {
        return juegos.size();
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView,
                        final ViewGroup parent) {
        View view = null;
        LayoutInflater inflator = activity.getLayoutInflater();
        view = inflator.inflate(R.layout.juego_item, null);
        final ViewHolder viewHolder = new ViewHolder();

        // *** instanciamos a los recursos

        viewHolder.nombre =(TextView) view.findViewById(R.id.nombre);
        // viewHolder.categoria = (TextView) view.findViewById(R.id.categoria);
        viewHolder.puntos = (EditText) view.findViewById(R.id.puntos);

        // importante!!! establecemos el mensaje
        viewHolder.nombre.setText(juegos.get(position).getNombre());
        // viewHolder.categoria.setText("Categoría: " + "categoría");
        //juegos.get(position).setPuntosJuego(Integer.parseInt(viewHolder.puntos.getText().toString()));

        return view;
    }


}

