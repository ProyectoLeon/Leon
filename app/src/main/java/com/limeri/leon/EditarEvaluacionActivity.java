package com.limeri.leon;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.limeri.leon.Models.AdministradorJuegos;
import com.limeri.leon.Models.Evaluacion;
import com.limeri.leon.Models.Juego;
import com.limeri.leon.Models.JuegoAdapter;
import com.limeri.leon.Models.Navegacion;
import com.limeri.leon.Models.Paciente;

import java.util.ArrayList;
import java.util.List;

public class EditarEvaluacionActivity extends AppCompatActivity {

    Paciente paciente;
    JuegoAdapter juegoAdapter;
    List<AdministradorJuegos.JuegoWisc> juegosWISC;
    List<Juego> juegos = new ArrayList<>();
    ListView listViewOb;
    AdministradorJuegos adminjuego;
    Evaluacion evaluacion;
    private Integer position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carga_manual);

        listViewOb = (ListView) findViewById(R.id.listviewObligatorios);

        paciente = Paciente.getSelectedPaciente();
        adminjuego = AdministradorJuegos.getInstance();
        position = getIntent().getExtras().getInt("position");
        evaluacion = paciente.getEvaluacion(position);
        ActionBar AB = getSupportActionBar();
        if (AB != null) {
            AB.setTitle("Editar evaluación");

        }

        for(Juego juego : evaluacion.getJuegos()){
            Juego juegoAux = new Juego(juego);
            juegos.add(juegoAux);
        }

        FloatingActionButton buttonCargaManual = (FloatingActionButton) findViewById(R.id.cargar);
        if (buttonCargaManual != null) {
            buttonCargaManual.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    evaluacion.finalizar();
                    evaluacion.setJuegos(juegos);
                    adminjuego.editarResultados(evaluacion);
                    Paciente.saveCuenta(paciente);
                    Navegacion.irA(EditarEvaluacionActivity.this, ValorExamenActivity.class, position);

                }
            });
        };


        // creamos el listado
        juegoAdapter = new JuegoAdapter(this, juegos);

        // establecemos el adaptador en la lista
        listViewOb.setAdapter(juegoAdapter);


    }



    @Override
    public void onBackPressed() {
            showPopupConfirmar(EditarEvaluacionActivity.this);
        }


    private void showPopupConfirmar(final AppCompatActivity context) {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        String msje = "Los cambios no se guardarán ¿Desea continuar?";
        builder.setTitle(msje);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Navegacion.irA(EditarEvaluacionActivity.this, HistoricoActivity.class);
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

