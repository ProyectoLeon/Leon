package com.limeri.leon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.limeri.leon.Models.AdministradorJuegos;
import com.limeri.leon.Models.Evaluacion;
import com.limeri.leon.Models.Juego;
import com.limeri.leon.Models.Navegacion;
import com.limeri.leon.Models.Paciente;

import java.util.ArrayList;
import java.util.List;

public class ValorJuegoLibreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valor_examen);

        Paciente paciente = Paciente.getSelectedPaciente();
        Evaluacion evaluacion = paciente.getEvaluacionFinalizada();
        Juego juego = evaluacion.getUltimoJuegoLibre();
        List<String> llistadoPuntos = new ArrayList<>();
        String puntaje = juego.getNombre() + ": " + juego.getPuntosJuego().toString();
        llistadoPuntos.add(puntaje);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, llistadoPuntos);

        ListView listado = (ListView) findViewById(R.id.listadoP);
        listado.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Navegacion.irA(this, ExamenActivity.class);
    }
}
