package com.limeri.leon;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.limeri.leon.Models.Evaluacion;
import com.limeri.leon.Models.Navegacion;
import com.limeri.leon.Models.Paciente;

import java.util.List;

public class GraficoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        String value = null;
        if(b!= null){
            value = b.getString("key");
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        setContentView(R.layout.activity_grafico);
        Paciente  paciente = Paciente.getSelectedPaciente();
        List<Evaluacion> evaluaciones = paciente.getEvaluaciones();
        GraphView graph;
        graph = (GraphView) findViewById(R.id.graficoSelec);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(160);
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph) ;
        graph.getGridLabelRenderer().setTextSize(6f);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);
        staticLabelsFormatter.setVerticalLabels(new String[]
                {"0","10","20","30","40","50","60","70","80","90","100","110","120","130","140","150","160"});
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.BOTH);
        graph.getGridLabelRenderer().setLabelHorizontalHeight(10);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Evaluaciones");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Puntaje del Indice");
        graph.getGridLabelRenderer().setLabelVerticalWidth(50);
        //graph.getGridLabelRenderer().setHorizontalLabelsVisible(true);
        //graph.getGridLabelRenderer().setVerticalLabelsVisible(true);
        //graph.getGridLabelRenderer().reloadStyles();
        DataPoint[] values = new DataPoint[evaluaciones.size()];
        String[] stringList = new String[evaluaciones.size()+2];
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(evaluaciones.size()+1);
        int i =0;
        stringList[i] = "";
        String indice= null;
        for(Evaluacion evaluacion:evaluaciones){
            stringList[i+1] = evaluacion.getFechaEvaluación().substring(6,10);
            switch (value){
                case "1":
                    values[i] = new DataPoint(i+1, evaluacion.getPuntosCompVerbal());
                    indice = "Compr. Verbal";
                    break;
                case "2":
                    values[i] = new DataPoint(i+1, evaluacion.getPuntosRazPercep());
                    indice="Razonam. Perceptivo";
                    break;
                case "3":
                    values[i] = new DataPoint(i+1, evaluacion.getPuntosMemOper());
                    indice = "Memoria de Trabajo";
                    break;
                case "4":
                    values[i] = new DataPoint(i+1, evaluacion.getPuntosVelocProc());
                    indice = "Veloc. Procesamiento";
                    break;
                case "5":
                    values[i] = new DataPoint(i+1, evaluacion.getCoeficienteIntelectual());
                    indice= "Coeficiente Intelectual";
                    break;
            }
            i++;
        }
        stringList[i+1] = "";
        staticLabelsFormatter.setHorizontalLabels(stringList);
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        LineGraphSeries serie = new LineGraphSeries<>(values);
        serie.setColor(Color.RED);
        serie.setDrawDataPoints(true);
        serie.setDataPointsRadius(10);
        serie.setThickness(8);
        graph.setTitle("Progreso del paciente sobre el índice de" + indice);
        graph.addSeries(serie);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Navegacion.irA(this, EstadisticasActivity.class);
    }
}
