package com.limeri.leon;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.limeri.leon.Models.AdministradorJuegos;
import com.limeri.leon.Models.Evaluacion;
import com.limeri.leon.Models.Juego;
import com.limeri.leon.Models.Navegacion;
import com.limeri.leon.Models.Paciente;

import java.util.ArrayList;
import java.util.List;

public class ExamenActivity extends AppCompatActivity {

    Paciente paciente;
    private ListView lvJuegos;
    private ArrayAdapter<String> adapter;
    List<AdministradorJuegos.JuegoWisc> juegosLibres;
    List<String> categoriasDebiles;
    AdministradorJuegos.JuegoWisc juegoLibre;
    private final String PREFIJO = this.getClass().getPackage().getName() + ".";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examen);

        lvJuegos = (ListView) findViewById(R.id.listJuegos);
        adapter = new ArrayAdapter<String>(this, R.layout.paciente_item);
        adapter.clear();
        juegosLibres = new ArrayList<AdministradorJuegos.JuegoWisc>();
        categoriasDebiles = Paciente.getSelectedPaciente().getEvaluacionFinalizada().getCategoriasDebiles();
        for (AdministradorJuegos.JuegoWisc juego : AdministradorJuegos.getInstance().getJuegosWisc()) {
            if (categoriasDebiles.contains(juego.categoria)) {
                juegosLibres.add(juego);
                adapter.add(juego.nombre);
            }
        }
        lvJuegos.setAdapter(adapter);

        AdministradorJuegos.setContext(getApplicationContext());
        paciente = Paciente.getSelectedPaciente();
        ActionBar AB = getSupportActionBar();
        if (AB != null) {
            AB.setTitle(paciente.getNombreCompleto());
        }

        lvJuegos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView juego = (TextView) view.findViewById(R.id.tvLinea);
                juegoLibre = juegosLibres.get(position);
                paciente.getEvaluacionFinalizada().agregarJuegoLibre(new Juego(juegoLibre.nombre,juegoLibre.categoria,juegoLibre.activity,null,false,0.0,0.0,juegoLibre.juegaPaciente));
                try {
                    Class activity = Class.forName(PREFIJO + juegoLibre.activity);
                    Navegacion.irA(ExamenActivity.this, activity);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Navegacion.irA(ExamenActivity.this, MainActivity.class);
    }


}