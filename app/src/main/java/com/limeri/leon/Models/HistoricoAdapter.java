package com.limeri.leon.Models;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.limeri.leon.HistoricoActivity;
import com.limeri.leon.MainActivity;
import com.limeri.leon.PerfilEscalaresActivity;
import com.limeri.leon.R;
import com.limeri.leon.ValorExamenActivity;

import java.util.ArrayList;
import java.util.List;

public class HistoricoAdapter extends ArrayAdapter<Evaluacion> {

    private Activity activity;
    List<Evaluacion> evaluaciones;

    public HistoricoAdapter(Activity activity, List<Evaluacion> evaluaciones) {
        super(activity, R.layout.evaluacion_item);
        this.activity = activity;
        this.evaluaciones = evaluaciones;
    }

    static class ViewHolder {
        protected TextView FechaEvaluacion;
        protected TextView estadoEvaluacion;
        protected Button button1;
        protected Button button2;

    }

    public int getCount() {
        return evaluaciones.size();
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView,
                        final ViewGroup parent) {
        View view = null;
        LayoutInflater inflator = activity.getLayoutInflater();
        view = inflator.inflate(R.layout.evaluacion_item, null);
        final ViewHolder viewHolder = new ViewHolder();

        // *** instanciamos a los recursos

        viewHolder.FechaEvaluacion =(TextView) view.findViewById(R.id.FechaEvaluacion);
        viewHolder.estadoEvaluacion = (TextView) view.findViewById(R.id.estadoEvaluacion);
        viewHolder.button1 = (Button) view.findViewById(R.id.button1);
        viewHolder.button2 = (Button) view.findViewById(R.id.button2);

        // importante!!! establecemos el mensaje

        viewHolder.FechaEvaluacion.setText(evaluaciones.get(position).getFechaEvaluación());
        //viewHolder.FechaEvaluacion.setText(evaluaciones.get(position).getFecha);
        viewHolder.estadoEvaluacion.setText(evaluaciones.get(position).getEstado().toString());

        viewHolder.button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navegacion.irA (activity , ValorExamenActivity.class, position);
            }
            });

        viewHolder.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navegacion.irA (activity , PerfilEscalaresActivity.class, position);
            }
        });



        return view;
    }


}

