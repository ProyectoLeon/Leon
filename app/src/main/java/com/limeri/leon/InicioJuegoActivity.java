package com.limeri.leon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.limeri.leon.Models.AdministradorJuegos;
import com.limeri.leon.Models.Evaluacion;
import com.limeri.leon.Models.Juego;
import com.limeri.leon.Models.Paciente;

public class InicioJuegoActivity extends AppCompatActivity {

    private Evaluacion evaluacion;
    private Juego juego;
    private final String PREFIJO = this.getClass().getPackage().getName() + ".";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_juego);
        AdministradorJuegos.setContext(getApplicationContext());

        Paciente paciente = Paciente.getSelectedPaciente();
        if (paciente.tieneEvaluacionIniciada()) {
            evaluacion = paciente.getEvaluacionActual();
        } else {
            evaluacion = new Evaluacion(paciente);
            paciente.agregarEvaluacion(evaluacion);
        }

        juego = AdministradorJuegos.getInstance().getSiguienteJuego(evaluacion);

        ((TextView) findViewById(R.id.juego)).setText(juego.getNombre());

        findViewById(R.id.buttonStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    evaluacion.agregarJuego(juego);
                    Class activity = Class.forName(PREFIJO + juego.getNombreActividad());
                    Intent mainIntent = new Intent(InicioJuegoActivity.this, activity);
                    InicioJuegoActivity.this.startActivity(mainIntent);
                    InicioJuegoActivity.this.finish();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
