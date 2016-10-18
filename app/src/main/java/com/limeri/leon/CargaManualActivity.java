package com.limeri.leon;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.limeri.leon.Models.AdministradorJuegos;
import com.limeri.leon.Models.Evaluacion;
import com.limeri.leon.Models.HistoricoAdapter;
import com.limeri.leon.Models.Juego;
import com.limeri.leon.Models.JuegoAdapter;
import com.limeri.leon.Models.Navegacion;
import com.limeri.leon.Models.Paciente;

import java.util.ArrayList;
import java.util.List;

public class CargaManualActivity extends AppCompatActivity {

    Paciente paciente;
    JuegoAdapter juegoAdapter;
    List<AdministradorJuegos.JuegoWisc> juegosWISC;
    List<Juego>juegos;
    ListView listViewOb;
    AdministradorJuegos adminjuego;
    Evaluacion evaluacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carga_manual);

        listViewOb = (ListView) findViewById(R.id.listviewObligatorios);

        AdministradorJuegos.setContext(getApplicationContext());
        paciente = Paciente.getSelectedPaciente();
        adminjuego = AdministradorJuegos.getInstance();

        ActionBar AB = getSupportActionBar();
        if (AB != null) {
            AB.setTitle("Carga manual de evaluaciones");

        }

        evaluacion = new Evaluacion();
        paciente.agregarEvaluacion(evaluacion);
        Paciente.saveCuenta(paciente);

        FloatingActionButton buttonCargaManual = (FloatingActionButton) findViewById(R.id.cargar);
        if (buttonCargaManual != null) {
            buttonCargaManual.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    evaluacion.finalizar();
                    adminjuego.completarResultados(evaluacion);
                    Navegacion.irA(CargaManualActivity.this, ValorExamenActivity.class, paciente.getEvaluaciones().size() - 1);
                Paciente.saveCuenta(paciente);
                }
            });
        };


        // creamos nuestra coleccion de datos
        juegosWISC = new ArrayList<AdministradorJuegos.JuegoWisc>();
        juegosWISC = AdministradorJuegos.getInstance().getJuegosWisc();
        for (AdministradorJuegos.JuegoWisc juegoWisc : juegosWISC ) {
            Juego juego = new Juego(juegoWisc.nombre, juegoWisc.categoria, juegoWisc.activity, juegoWisc.puntaje, juegoWisc.alternativo, juegoWisc.media, juegoWisc.valorCritico, juegoWisc.juegaPaciente);
            evaluacion.agregarJuego(juego);
        }

        // creamos el listado
        juegoAdapter = new JuegoAdapter(this, evaluacion.getJuegos());

        // establecemos el adaptador en la lista
        listViewOb.setAdapter(juegoAdapter);


    }



    @Override
    public void onBackPressed() {
            showPopupConfirmar(CargaManualActivity.this);
        }


    private void showPopupConfirmar(final AppCompatActivity context) {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        String msje = "¿Está seguro que desea eliminar la evaluación actual?";
        builder.setTitle(msje);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Paciente paciente = Paciente.getSelectedPaciente();
                paciente.borrarEvaluacionActual();
                Paciente.saveCuenta(paciente);
                Navegacion.irA(CargaManualActivity.this, MainActivity.class);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}

