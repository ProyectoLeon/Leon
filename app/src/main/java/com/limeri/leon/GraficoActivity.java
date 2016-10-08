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
private int tipoGrafico;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        setContentView(R.layout.activity_grafico);
        Paciente  paciente = Paciente.getSelectedPaciente();
        List<Evaluacion> evaluaciones = paciente.getEvaluaciones();
        GraphView graph;
        graph = (GraphView) findViewById(R.id.graficoSelec);
        graph.setTitle("Progreeso del paciente");
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(15);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(19);
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph) ;
        graph.getGridLabelRenderer().setTextSize(6f);

        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.BOTH);
        graph.getGridLabelRenderer().setLabelHorizontalHeight(10);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Subtests");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Puntaje Escalar");
        graph.getGridLabelRenderer().setLabelVerticalWidth(50);
        //graph.getGridLabelRenderer().setHorizontalLabelsVisible(true);
        //graph.getGridLabelRenderer().setVerticalLabelsVisible(true);
        //graph.getGridLabelRenderer().reloadStyles();
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        DataPoint[] values = new DataPoint[evaluaciones.size()];
        int i =0;
        for(Evaluacion evaluacion:evaluaciones){
            values[i] = new DataPoint(i, evaluacion.getCoeficienteIntelectual());
            i++;
        }
        LineGraphSeries serie = new LineGraphSeries<DataPoint>(values);
        serie.setColor(Color.RED);
        serie.setDrawDataPoints(true);
        serie.setDataPointsRadius(10);
        serie.setThickness(8);
        graph.addSeries(serie);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Navegacion.irA(this, MainActivity.class);
    }
}
