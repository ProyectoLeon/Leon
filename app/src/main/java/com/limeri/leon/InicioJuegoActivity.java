package com.limeri.leon;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InicioJuegoActivity extends AppCompatActivity {

    private Evaluacion evaluacion;
    private Juego juego;
    private List<Juego> juegosAlternativos = new ArrayList<>();
    private TextView seleccion;
    private Map<String,Juego> mapJuegos = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_juego);

        Paciente paciente = Paciente.getSelectedCuenta();
        if (paciente.tieneEvaluacionIniciada()) {
            evaluacion = paciente.getEvaluacion();
            Juego ultimoJuego = evaluacion.getUltimoJuego();
            juego = AdministradorJuegos.getJuegoSiguiente(ultimoJuego);
        } else {
            evaluacion = new Evaluacion(paciente);
            paciente.agregarEvaluacion(evaluacion);
            juego = AdministradorJuegos.getJuegoInicial();
        }

        mapJuegos.put(juego.getNombre(),juego);
        juegosAlternativos = AdministradorJuegos.getJuegosAlternativos(juego);
        List<String> nombreJuegosAlt = new ArrayList<>();
        for (Juego juegoAlt : juegosAlternativos) {
            nombreJuegosAlt.add(juegoAlt.getNombre());
            mapJuegos.put(juegoAlt.getNombre(),juegoAlt);
        }

        agregarJuegos(Arrays.asList(juego.getNombre()), (ListView) findViewById(R.id.juegos));
        agregarJuegos(nombreJuegosAlt, (ListView) findViewById(R.id.juegosAlternativos));

        findViewById(R.id.buttonStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seleccion != null) {
                    String strJuego = (String) seleccion.getText();
                    evaluacion.agregarJuego(mapJuegos.get(strJuego));
                    Intent mainIntent = new Intent(InicioJuegoActivity.this, AdministradorJuegos.getJuegoActivity(strJuego));
                    InicioJuegoActivity.this.startActivity(mainIntent);
                    InicioJuegoActivity.this.finish();
                }
            }
        });
    }


    private void agregarJuegos(List<String> juegos, ListView listView) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, juegos);
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
