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
import com.limeri.leon.Models.PuntuacionCompuesta;
import com.limeri.leon.common.DataBase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.validation.Validator;

public class PuntuacionDirectaActivity extends AppCompatActivity {

    private Integer compVerbal;
    private Integer razPercep;
    private Integer memOper;
    private Integer velProc;
    private Integer CI;
    private Integer position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntuacion_directa);
        position = getIntent().getExtras().getInt("position");
        generarPuntuacionDirecta();
        generarSumas();

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
        //    row0.setBackgroundColor(Color.parseColor("#FFFFFFF"));
        tablaPD.addView(row0);
        tablaPD.setShrinkAllColumns(true);
        CompletarPuntajeJuego(tablaPD);
    }

    public void generarSumas() {

        TableLayout tablaSumas = (TableLayout) findViewById(R.id.tableViewSumas);
        TableRow row0 = new TableRow(this);
        CompletarCelda(this, row0, "Escalas");
        CompletarCelda(this, row0, "Suma Escalares");
        CompletarCelda(this, row0, "Punt. Compuesta");
        CompletarCelda(this, row0, "Percentil");
        CompletarCelda(this, row0, "% IC");
        //    row0.setBackgroundColor(Color.parseColor("#FFFFFFF"));
        tablaSumas.addView(row0);
        CompletarSumas(tablaSumas);
    }

    public void CompletarCelda(Activity activity, TableRow row, String txt) {
        TextView col = new TextView(activity);
        //col.setPadding(2, 2, 2, 2);
        col.setTextSize(10);
        col.setBackground(getResources().getDrawable(R.drawable.cell_shape));
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
                Navegacion.irA(PuntuacionDirectaActivity.this, PerfilEscalaresActivity.class, position);
            }
        };
    }

    public void CompletarPuntajeJuego(TableLayout tabla) {
        compVerbal = 0;
        razPercep = 0;
        memOper = 0;
        velProc = 0;
        CI = 0;

        Paciente paciente = Paciente.getSelectedPaciente();
        Evaluacion evaluacion = paciente.getEvaluacion(position);
        List<Juego> listaJuegos = evaluacion.getJuegos();
        for (Juego juego : listaJuegos) {
            TableRow row = new TableRow(this);
            CompletarCelda(this, row, juego.getNombre());
            CompletarCelda(this, row, juego.getPuntosJuego().toString());
            String escalar = juego.getPuntajeEscalar().toString();
            TextView col = new TextView(this);
            col.setText(escalar);
            switch (juego.getCategoria()) {
                case "Comprensión verbal": {
                    CompletarCelda(this, row, escalar);
                    CompletarCelda(this, row, "");
                    CompletarCelda(this, row, "");
                    CompletarCelda(this, row, "");
                    CompletarCelda(this, row, escalar);
                    tabla.addView(row);

                    compVerbal = compVerbal + juego.getPuntajeEscalar();
                    break;
                }
                case "Razonamiento Perceptivo": {
                    CompletarCelda(this, row, "");
                    CompletarCelda(this, row, escalar);
                    CompletarCelda(this, row, "");
                    CompletarCelda(this, row, "");
                    CompletarCelda(this, row, escalar);
                    tabla.addView(row);

                    razPercep = razPercep + juego.getPuntajeEscalar();
                    break;
                }
                case "Memoria Operativa": {
                    CompletarCelda(this, row, "");
                    CompletarCelda(this, row, "");
                    CompletarCelda(this, row, escalar);
                    CompletarCelda(this, row, "");
                    CompletarCelda(this, row, escalar);
                    tabla.addView(row);

                    memOper = memOper + juego.getPuntajeEscalar();
                    break;
                }
                case "Velocidad de Procesamiento": {
                    CompletarCelda(this, row, "");
                    CompletarCelda(this, row, "");
                    CompletarCelda(this, row, "");
                    CompletarCelda(this, row, escalar);
                    CompletarCelda(this, row, escalar);
                    tabla.addView(row);

                    velProc = velProc + juego.getPuntajeEscalar();
                    break;
                }
            }
        }
        //Paciente.getSelectedPaciente().getEvaluacionFinalizada().setPuntosCompVerbal(compVerbal);
        //Paciente.getSelectedPaciente().getEvaluacionFinalizada().setPuntosMemOper(memOper);
        //Paciente.getSelectedPaciente().getEvaluacionFinalizada().setPuntosRazPercep(razPercep);
        //Paciente.getSelectedPaciente().getEvaluacionFinalizada().setPuntosVelocProc(velProc);
        CI = compVerbal + razPercep + memOper + velProc;
        //Paciente.getSelectedPaciente().getEvaluacionFinalizada().setCoeficienteIntelectual(CI);
        TableRow row = new TableRow(this);
        CompletarCelda(this, row, "Suma puntuaciones escalares");
        CompletarCelda(this, row, "");
        CompletarCelda(this, row, compVerbal.toString());
        CompletarCelda(this, row, razPercep.toString());
        CompletarCelda(this, row, memOper.toString());
        CompletarCelda(this, row, velProc.toString());
        CompletarCelda(this, row, CI.toString());
        tabla.addView(row);

    }

    public void CompletarSumas(TableLayout tabla) {
        Paciente paciente = Paciente.getSelectedPaciente();
        Evaluacion evaluacion = paciente.getEvaluacion(position);
        PuntuacionCompuesta puntuacionCompuesta = null;

        puntuacionCompuesta = PuntuacionCompuesta.getPuntuacionCompuesta("ICV", compVerbal);
        TableRow row1 = new TableRow(this);
        CompletarCelda(this, row1, "Comprensión Verbal");
        CompletarCelda(this, row1, compVerbal.toString());
        CompletarCelda(this, row1, puntuacionCompuesta.getEquivalencia().toString());
        CompletarCelda(this, row1, puntuacionCompuesta.getpercentil().toString());
        CompletarCelda(this, row1, puntuacionCompuesta.getNivelConfianza().toString());
        tabla.addView(row1);

        puntuacionCompuesta = PuntuacionCompuesta.getPuntuacionCompuesta("IRP", razPercep);
        TableRow row2 = new TableRow(this);
        CompletarCelda(this, row2, "Razonamiento Perceptivo");
        CompletarCelda(this, row2, razPercep.toString());
        CompletarCelda(this, row2, puntuacionCompuesta.getEquivalencia().toString());
        CompletarCelda(this, row2, puntuacionCompuesta.getpercentil().toString());
        CompletarCelda(this, row2, puntuacionCompuesta.getNivelConfianza().toString());
        tabla.addView(row2);

        puntuacionCompuesta = PuntuacionCompuesta.getPuntuacionCompuesta("IMO", memOper);
        TableRow row3 = new TableRow(this);
        CompletarCelda(this, row3, "Memoria Operativa");
        CompletarCelda(this, row3, memOper.toString());
        CompletarCelda(this, row3, puntuacionCompuesta.getEquivalencia().toString());
        CompletarCelda(this, row3, puntuacionCompuesta.getpercentil().toString());
        CompletarCelda(this, row3, puntuacionCompuesta.getNivelConfianza().toString());
        tabla.addView(row3);

        puntuacionCompuesta = PuntuacionCompuesta.getPuntuacionCompuesta("IVP", velProc);
        TableRow row4 = new TableRow(this);
        CompletarCelda(this, row4, "Velocidad de Procesamiento");
        CompletarCelda(this, row4, velProc.toString());
        CompletarCelda(this, row4, puntuacionCompuesta.getEquivalencia().toString());
        CompletarCelda(this, row4, puntuacionCompuesta.getpercentil().toString());
        CompletarCelda(this, row4, puntuacionCompuesta.getNivelConfianza().toString());
        tabla.addView(row4);

        puntuacionCompuesta = PuntuacionCompuesta.getPuntuacionCompuesta("CIT", CI);
        TableRow row5 = new TableRow(this);
        CompletarCelda(this, row5, "CI Total");
        CompletarCelda(this, row5, CI.toString());
        CompletarCelda(this, row5, puntuacionCompuesta.getEquivalencia().toString());
        CompletarCelda(this, row5, puntuacionCompuesta.getpercentil().toString());
        CompletarCelda(this, row5, puntuacionCompuesta.getNivelConfianza().toString());
        tabla.addView(row5);
    }



}
