package com.limeri.leon;

import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jjoe64.graphview.GraphView;
import com.limeri.leon.Models.Evaluacion;
import com.limeri.leon.Models.Juego;
import com.limeri.leon.Models.Navegacion;
import com.limeri.leon.Models.Paciente;

import java.util.List;

public class EstadisticasActivity extends AppCompatActivity {
    private RadioGroup grupo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);

        RadioButton alt1 = (RadioButton) findViewById(R.id.CompVerbal);
        RadioButton alt2 = (RadioButton) findViewById(R.id.RazPerc);
        RadioButton alt3 = (RadioButton) findViewById(R.id.MemTrab);
        RadioButton alt4 = (RadioButton) findViewById(R.id.VelProc);
        RadioButton alt5 = (RadioButton) findViewById(R.id.CoefInt);
        Button estad = (Button) findViewById(R.id.buttonEst);
        grupo = (RadioGroup) findViewById(R.id.Rgroup);

        estad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Paciente  paciente = Paciente.getSelectedPaciente();
//                List<Evaluacion> evaluaciones = paciente.getEvaluaciones();
                String value = onRadioButtonClicked(v);
                Navegacion.irA(EstadisticasActivity.this, GraficoActivity.class, value);
            }
        });
    }
    public String onRadioButtonClicked(View view) {
        // Is the button now checked?
        //boolean checked = ((RadioButton) view).isChecked();
        String value = null;
        // Check which radio button was clicked
        switch(grupo.getCheckedRadioButtonId()) {
            case R.id.CompVerbal:
                    value = "1";
                break;
            case R.id.RazPerc:
                    value = "2";
                    break;
            case R.id.MemTrab:
                    value = "3";
                break;
            case R.id.VelProc:
                    value = "4";
                break;
            case R.id.CoefInt:
                    value = "5";
                break;
        }
        return value;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Navegacion.irA(this, MainActivity.class);
    }
}
