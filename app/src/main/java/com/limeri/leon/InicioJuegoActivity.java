package com.limeri.leon;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.limeri.leon.Models.AdministradorJuegos;
import com.limeri.leon.Models.Evaluacion;
import com.limeri.leon.Models.Juego;
import com.limeri.leon.Models.Paciente;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InicioJuegoActivity extends AppCompatActivity {

    private Evaluacion evaluacion;
    private TextView seleccion;
    private Map<String,Juego> mapJuegos = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_juego);
        AdministradorJuegos.setContext(getApplicationContext());

        Paciente paciente = Paciente.getSelectedCuenta();
        Juego juego;
/*        if (paciente.tieneEvaluacionIniciada()) {
            evaluacion = paciente.getEvaluacion();
        } else {
            evaluacion = new Evaluacion(paciente);
            paciente.agregarEvaluacion(evaluacion);
        }
        if (evaluacion.realizoAlgunJuego()) {
            juego = AdministradorJuegos.getInstance().getJuegoSiguiente(evaluacion.getUltimoJuego());
        } else {*/
            juego = AdministradorJuegos.getInstance().getJuegoInicial();
//        }

        mapJuegos.put(juego.getNombre(), juego);
        List<Juego> juegosAlternativos = AdministradorJuegos.getInstance().getJuegosAlternativos(juego);
        List<String> nombreJuegosAlt = new ArrayList<>();
        for (Juego juegoAlt : juegosAlternativos) {
            nombreJuegosAlt.add(juegoAlt.getNombre());
            mapJuegos.put(juegoAlt.getNombre(),juegoAlt);
        }

        agregarJuegos(Collections.singletonList(juego.getNombre()), (ListView) findViewById(R.id.juegos));
        agregarJuegos(nombreJuegosAlt, (ListView) findViewById(R.id.juegosAlternativos));

        findViewById(R.id.buttonStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seleccion != null) {
                    String strJuego = (String) seleccion.getText();
                    Juego juego = mapJuegos.get(strJuego);
//                    evaluacion.agregarJuego(juego);
                    Intent mainIntent = new Intent(InicioJuegoActivity.this, juego.getActivityClass());
                    InicioJuegoActivity.this.startActivity(mainIntent);
                    InicioJuegoActivity.this.finish();
                }
            }
        });
    }

    private void agregarJuegos(List<String> juegos, ListView listView) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, juegos);
        listView.setOnItemClickListener(opcionSeleccionada());
        listView.setAdapter(adapter);
    }

    private AdapterView.OnItemClickListener opcionSeleccionada() {

        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (seleccion != null) blanquear(seleccion);
                seleccion = ((TextView) view);
                seleccionar(seleccion);
            }
        };
    }

    private void seleccionar(TextView view) {
        view.setTextColor(Color.RED);
    }

    private void blanquear(TextView view) {
        view.setTextColor(Color.BLACK);
    }
}
