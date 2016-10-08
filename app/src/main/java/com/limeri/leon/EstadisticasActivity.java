package com.limeri.leon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.jjoe64.graphview.GraphView;
import com.limeri.leon.Models.Evaluacion;
import com.limeri.leon.Models.Juego;
import com.limeri.leon.Models.Navegacion;
import com.limeri.leon.Models.Paciente;

import java.util.List;

public class EstadisticasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);

        RadioButton alt1 = (RadioButton) findViewById(R.id.CompVerbal);
        RadioButton alt2 = (RadioButton) findViewById(R.id.RazPerc);
        RadioButton alt3 = (RadioButton) findViewById(R.id.MemTrab);
        RadioButton alt4 = (RadioButton) findViewById(R.id.VelProc);
        Button estad = (Button) findViewById(R.id.buttonEst);

        estad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Paciente  paciente = Paciente.getSelectedPaciente();
//                List<Evaluacion> evaluaciones = paciente.getEvaluaciones();
                Navegacion.irA(EstadisticasActivity.this, GraficoActivity.class);
            }
        });
    }
}
