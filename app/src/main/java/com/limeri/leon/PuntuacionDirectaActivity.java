package com.limeri.leon;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.limeri.leon.Models.Evaluacion;
import com.limeri.leon.Models.Juego;
import com.limeri.leon.Models.Navegacion;
import com.limeri.leon.Models.Paciente;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.validation.Validator;

public class PuntuacionDirectaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntuacion_directa);
        generarPuntuacionDirecta();

        Button siguiente = (Button) findViewById(R.id.siguienteButton);
        if (siguiente != null) {
            siguiente.setOnClickListener(clickSiguiente());
        }
    }


    public void generarPuntuacionDirecta() {

        TableLayout tablaPD = (TableLayout) findViewById(R.id.tableView);
        TableRow row0 = new TableRow(this);
        CompletarCelda(this, row0, "Subtest");
        CompletarCelda(this, row0, "PD");
        CompletarCelda(this, row0, "PE");
        CompletarCelda(this, row0, "PE");
        CompletarCelda(this, row0, "PE");
        CompletarCelda(this, row0, "PE");
        CompletarCelda(this, row0, "PE");
        CompletarCelda(this, row0, "PE");
    //    row0.setBackgroundColor(Color.parseColor("#FFFFFFF"));
        tablaPD.addView(row0);
        CompletarPuntajeJuego(tablaPD);
    }

    public void CompletarCelda(Activity activity, TableRow row, String txt) {
        TextView col = new TextView(activity);
        col.setPadding(2, 2, 2, 2);
        col.setText(txt);
        row.addView(col);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Navegacion.irA(this, MainActivity.class);
    }

    private View.OnClickListener clickSiguiente() {

        return new View.OnClickListener() {

            @Override
            public void onClick(View view) {

//                Navegacion.irA(PuntuacionDirectaActivity.this, MainActivity.class);
                Navegacion.irA(PuntuacionDirectaActivity.this, PerfilEscalaresActivity.class);
            }
        };
    }

    public void CompletarPuntajeJuego(TableLayout tabla){
      Integer index = 0;
        Paciente paciente = Paciente.getSelectedPaciente();
        Evaluacion evaluacion = paciente.getEvaluacionFinalizada();
        List<Juego> listaJuegos = evaluacion.getJuegos();
        for (Juego juego : listaJuegos){
            TableRow row = new TableRow(this);
            CompletarCelda(this, row, juego.getNombre());
            CompletarCelda(this, row, juego.getPuntosJuego().toString());
            String escalar = CalcularEscalar(juego);
            TextView col = new TextView(this);
            col.setText(escalar);
            switch (juego.getCategoria()){
                case "Comprensión verbal":{
                    CompletarCelda(this, row, escalar);
                    CompletarCelda(this, row, "");
                    CompletarCelda(this, row, "");
                    CompletarCelda(this, row, "");
                    CompletarCelda(this, row, escalar);
                    tabla.addView(row);
                    break;
                }
                case "Razonamiento Perceptivo":{
                    CompletarCelda(this, row, "");
                    CompletarCelda(this, row, escalar);
                    CompletarCelda(this, row, "");
                    CompletarCelda(this, row, "");
                    CompletarCelda(this, row, escalar);
                    tabla.addView(row);
                    break;
                }
                case "Memoria Operativa":{
                    CompletarCelda(this, row, "");
                    CompletarCelda(this, row, "");
                    CompletarCelda(this, row, escalar);
                    CompletarCelda(this, row, "");
                    CompletarCelda(this, row, escalar);
                    tabla.addView(row);
                    break;
                }
                case "Velocidad de Procesamiento":{
                    CompletarCelda(this, row, "");
                    CompletarCelda(this, row, "");
                    CompletarCelda(this, row, "");
                    CompletarCelda(this, row, escalar);
                    CompletarCelda(this, row, escalar);
                    tabla.addView(row);
                    break;
                }
            }
//            CompletarCelda(this, row, escalar);

//            TextView col = new TextView(this);
//            col.setText(escalar);
//            row.addView(col, index);
//            row.addView(col,index);
//            row.addView(col,2);
//            row.addView(col, 7);
//            tabla.addView(row);
        }


    }

    private String CalcularEscalar(Juego juego){
        //TODO: Agregar lógica de calculo de escalares segun protocolo
        return juego.getPuntosJuego().toString();
    }
}
