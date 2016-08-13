package com.limeri.leon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.limeri.leon.Models.AdministradorJuegos;
import com.limeri.leon.Models.Evaluacion;
import com.limeri.leon.Models.Juegos.Juego;
import com.limeri.leon.Models.Paciente;

import java.util.List;

public class ValorExamenActivity extends AppCompatActivity {
public AdministradorJuegos administradorJuegos;
    public List<Juego> listaJuegos;
    public List<String> llistadoPuntos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valor_examen);

        Paciente paciente = Paciente.getmSelectedPaciente();
        Evaluacion evaluacion = paciente.getEvaluacion();
        listaJuegos = evaluacion.getJuegos();
        for (Juego juego : listaJuegos){
            String puntaje = juego.getPuntosJuego().toString();
            llistadoPuntos.add(puntaje);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, llistadoPuntos);

        ListView listado = (ListView) findViewById(R.id.listadoP);
        listado.setAdapter(adapter);
    }
}
