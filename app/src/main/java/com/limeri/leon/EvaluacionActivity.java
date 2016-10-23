package com.limeri.leon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.limeri.leon.Models.Navegacion;
import com.limeri.leon.Models.Paciente;

public class EvaluacionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluacion);

        Paciente paciente = Paciente.getSelectedPaciente();
        Button buttonEvaluacion = (Button) findViewById(R.id.buttonEvaluacion);
        if (buttonEvaluacion != null) {
            if (paciente.tieneEvaluacionIniciada()) {
                buttonEvaluacion.setText("Continuar Evaluación");
            }
            buttonEvaluacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navegacion.irA(EvaluacionActivity.this, InicioJuegoActivity.class);
                }
            });
        }

        Button buttonCargaManual = (Button) findViewById(R.id.buttonCargaManual);
        if ((!paciente.tieneEvaluacionIniciada())) {
            if (buttonCargaManual != null) {
                buttonCargaManual.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Navegacion.irA(EvaluacionActivity.this, CargaManualActivity.class);
                    }
                });
            }
        }
        else {
            buttonCargaManual.setEnabled(false);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Navegacion.irA(EvaluacionActivity.this, MainActivity.class);
    }
}
