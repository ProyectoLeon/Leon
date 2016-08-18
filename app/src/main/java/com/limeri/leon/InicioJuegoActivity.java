package com.limeri.leon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.limeri.leon.Models.AdministradorJuegos;
import com.limeri.leon.Models.Evaluacion;
import com.limeri.leon.Models.Juego;
import com.limeri.leon.Models.Navegacion;
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
//TODO: Revisar la lógica de ejecución de un TEST y finalizarlo, en lugar de volver a empezar.
        Paciente paciente = Paciente.getSelectedPaciente();
        if (paciente.tieneEvaluacionIniciada()) {
            evaluacion = paciente.getEvaluacionActual();
        } else {
            evaluacion = new Evaluacion();
            paciente.agregarEvaluacion(evaluacion);
            Paciente.saveCuenta(this,paciente);
        }

        juego = AdministradorJuegos.getInstance().getSiguienteJuego(evaluacion);

        ((TextView) findViewById(R.id.juego)).setText(juego.getNombre());

        findViewById(R.id.buttonStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    evaluacion.agregarJuego(juego);
                    Class activity = Class.forName(PREFIJO + juego.getNombreActividad());
                    Navegacion.irA(InicioJuegoActivity.this, activity);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Navegacion.irA(this, MainActivity.class);
    }
}

//TODO: Agregar lógica de cancelar (Opciones: Salir de la evaluación, seleccionar alternativo) o volver atrás.