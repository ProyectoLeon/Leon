package com.limeri.leon;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.androidplot.xy.XYPlot;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.limeri.leon.Models.Evaluacion;
import com.limeri.leon.Models.Juego;
import com.limeri.leon.Models.Navegacion;
import com.limeri.leon.Models.Paciente;
import com.limeri.leon.Models.Profesional;
import com.limeri.leon.Models.PuntuacionCompuesta;
import com.limeri.leon.common.MailSender;

import org.apache.pdfbox.contentstream.operator.graphics.StrokePath;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PerfilCompuestasActivity extends AppCompatActivity {

    private Integer compVerbal;
    private Integer razPercep;
    private Integer memOper;
    private Integer velProc;
    private Integer CI;
    Bitmap bitmap;
    Integer mWidth, mHeight;
    private XYPlot plot;
    List<Columna> columnas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        setContentView(R.layout.activity_perfil_compuestas);
        completarGraficoCompuesto();
        completarTablaComparaciones();
        completarPtosFuertesyDebiles();
        completarTablaPromedio();
        Button documentoPdf = (Button) findViewById(R.id.pdfButton);
        if (documentoPdf != null) {
            documentoPdf.setOnClickListener(clickPDF());
        }
    }



    public void CompletarCelda(Activity activity, TableRow row, String txt) {
        TextView col = new TextView(activity);
        col.setTextSize(4);
        col.setBackground(getResources().getDrawable(R.drawable.cell_shape));
        col.setText(txt);
        col.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        col.setGravity(Gravity.CENTER_HORIZONTAL);
        col.setSingleLine(false);
        row.addView(col);

    }

    public void CompletarTitulos(Activity activity, TableRow row, String txt) {
        TextView col = new TextView(activity);
        col.setTextSize(6);
        col.setBackgroundColor(getResources().getColor(R.color.verde_claro));
        col.setPadding(1,1,1,1);
        col.setTextColor(Color.WHITE);
        col.setLines(2);
        col.setGravity(Gravity.CENTER_VERTICAL);
        col.setText(txt);
        row.addView(col);
    }

    public void CompletarTitulos2(Activity activity, TableRow row, String txt) {
        TextView col = new TextView(activity);
        col.setTextSize(5);
        col.setBackgroundColor(getResources().getColor(R.color.verde_mas_claro));
        col.setPadding(1,1,1,1);
        col.setTextColor(Color.DKGRAY);
        col.setText(txt);
        row.addView(col);
    }





    public LineGraphSeries crearSerie(int min, int max, int color){
        DataPoint[] values = new DataPoint[max-min+1];
        for(int i = 0;i<=(max-min);i++){
            values[i] = new DataPoint(columnas.get(i+min).pos, columnas.get(i+min).ptos);
        }
        LineGraphSeries serie = new LineGraphSeries<DataPoint>(values);
        serie.setColor(color);
        serie.setDrawDataPoints(true);
        serie.setDataPointsRadius(10);
        serie.setThickness(8);
        return serie;
    }

    public void completarGraficoCompuesto(){
        GraphView graph = (GraphView) findViewById(R.id.graphCompuesto);
        graph.setTitle("Perfil de puntuaciones compuestas");
        graph.setTitleColor(getResources().getColor(R.color.verde_claro));
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph) ;
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(5);
        graph.getViewport().setMinY(40);
        graph.getViewport().setMaxY(160);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);
        staticLabelsFormatter.setHorizontalLabels(new String[]
                {"","CV","RP","MT","VP","CIT"});
        staticLabelsFormatter.setVerticalLabels(new String[]
                {"40","50","60","70","80","90","100","110","120","130","140","150","160"});
        graph.getGridLabelRenderer().setTextSize(10f);
        graph.getGridLabelRenderer().setLabelsSpace(10);
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.BOTH);
        graph.getGridLabelRenderer().setLabelHorizontalHeight(10);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Índices");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Puntaje Compuesto");
        graph.getGridLabelRenderer().setLabelVerticalWidth(50);

        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        Paciente paciente = Paciente.getSelectedPaciente();
        Evaluacion evaluacion = paciente.getEvaluacionFinalizada();
        Integer puntuacionCompuestaCV = null;
        puntuacionCompuestaCV = Integer.parseInt(PuntuacionCompuesta.getPuntuacionCompuesta("ICV",evaluacion.getPuntosCompVerbal()).getEquivalencia());
        Integer puntuacionCompuestaRP = null;
        puntuacionCompuestaRP = Integer.parseInt(PuntuacionCompuesta.getPuntuacionCompuesta("IRP",evaluacion.getPuntosRazPercep()).getEquivalencia());
        Integer puntuacionCompuestaMO = null;
        puntuacionCompuestaMO = Integer.parseInt(PuntuacionCompuesta.getPuntuacionCompuesta("IMO",evaluacion.getPuntosMemOper()).getEquivalencia());
        Integer puntuacionCompuestaVP = null;
        puntuacionCompuestaVP = Integer.parseInt(PuntuacionCompuesta.getPuntuacionCompuesta("IVP",evaluacion.getPuntosVelocProc()).getEquivalencia());
        Integer puntuacionCompuestaCI = null;
        puntuacionCompuestaCI = Integer.parseInt(PuntuacionCompuesta.getPuntuacionCompuesta("CIT",evaluacion.getCoeficienteIntelectual()).getEquivalencia());

        LineGraphSeries serie = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(1,puntuacionCompuestaCV),
                new DataPoint(2,puntuacionCompuestaRP),
                new DataPoint(3,puntuacionCompuestaMO),
                new DataPoint(4,puntuacionCompuestaVP),
                new DataPoint(5,puntuacionCompuestaCI)});
        serie.setColor(Color.GREEN);
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

    public void completarPtosFuertesyDebiles(){
        TableLayout tablaFyD = (TableLayout) findViewById(R.id.tablaPtosFYD);
        TableRow row0 = new TableRow(this);
        CompletarTitulos(this, row0, "Test");
        CompletarTitulos(this, row0, "Punt. escalar");
        CompletarTitulos(this, row0, "Media de Pe");
        CompletarTitulos(this, row0, "Dist. a la media");
        CompletarTitulos(this, row0, "Valor Crítico");
        CompletarTitulos(this, row0, "Fuerte o Débil");
        tablaFyD.addView(row0);

        TableRow row1 = new TableRow(this);
        CompletarTitulos2(this, row1, "Constr. con Cubos");
        validarFuerteDebil(this,row1,"Construcción con Cubos");
        tablaFyD.addView(row1);

        TableRow row2 = new TableRow(this);
        CompletarTitulos2(this, row2, "Semejanzas");
        validarFuerteDebil(this,row2,"Semejanzas");
        tablaFyD.addView(row2);

        TableRow row3 = new TableRow(this);
        CompletarTitulos2(this, row3, "Dígitos");
        validarFuerteDebil(this,row3,"Retención de Dígitos");
        tablaFyD.addView(row3);

        TableRow row4 = new TableRow(this);
        CompletarTitulos2(this, row4, "Conceptos");
        validarFuerteDebil(this,row4,"Conceptos");
        tablaFyD.addView(row4);

        TableRow row5 = new TableRow(this);
        CompletarTitulos2(this, row5, "Claves");
        validarFuerteDebil(this,row5,"Claves");
        tablaFyD.addView(row5);

        TableRow row6 = new TableRow(this);
        CompletarTitulos2(this, row6, "Vocabulario");
        validarFuerteDebil(this,row6,"Vocabulario");
        tablaFyD.addView(row6);

        TableRow row7 = new TableRow(this);
        CompletarTitulos2(this, row7, "Letras y Números");
        validarFuerteDebil(this,row7,"Letras y Números");
        tablaFyD.addView(row7);

        TableRow row8 = new TableRow(this);
        CompletarTitulos2(this, row8, "Matrices");
        validarFuerteDebil(this,row8,"Matrices");
        tablaFyD.addView(row8);

        TableRow row9 = new TableRow(this);
        CompletarTitulos2(this, row9, "Comprensión");
        validarFuerteDebil(this,row9,"Comprensión");
        tablaFyD.addView(row9);

        TableRow row10 = new TableRow(this);
        CompletarTitulos2(this, row10, "Búsq.Símbolos");
        validarFuerteDebil(this,row10,"Búsqueda de Símbolos");
        tablaFyD.addView(row10);
        tablaFyD.setShrinkAllColumns(true);
    }

    public void validarFuerteDebil(Activity activity, TableRow row, String string){
        Paciente paciente = Paciente.getSelectedPaciente();
        Evaluacion evaluacion = paciente.getEvaluacionFinalizada();
        List<Juego> listaJuegos = evaluacion.getJuegos();
        for (Juego juego : listaJuegos) {
            if (juego.getNombre().equals(string)) {
                CompletarCelda(activity, row, juego.getPuntajeEscalar().toString());
                CompletarCelda(activity, row, String.valueOf(juego.getMedia()));
                double diferencia = juego.getPuntajeEscalar() - juego.getMedia();
                CompletarCelda(activity, row, String.valueOf(diferencia));
                CompletarCelda(activity, row, String.valueOf(juego.getValorCritico()));
                if (diferencia > juego.getValorCritico()) {
                    CompletarCelda(activity, row, "Fuerte");
                } else if (diferencia < -(juego.getValorCritico())) {
                    CompletarCelda(activity, row, "Débil");
                } else {
                    CompletarCelda(activity, row, " ");
                }
            }
        }

    }

    public void completarTablaPromedio() {
        Paciente paciente = Paciente.getSelectedPaciente();
        Evaluacion evaluacion = paciente.getEvaluacionFinalizada();
        TableLayout tablaProm = (TableLayout) findViewById(R.id.tablaPromedio);
        TableRow row0 = new TableRow(this);
        CompletarTitulos(this, row0, "");
        CompletarTitulos(this, row0, "Todos los Tests (10)");
        CompletarTitulos(this, row0, "Comprensión Verbal (3)");
        CompletarTitulos(this, row0, "Razonamiento Perceptivo (3)");
        tablaProm.addView(row0);

        TableRow row1 = new TableRow(this);
        CompletarTitulos2(this, row1, "Suma puntos escalares");
        CompletarCelda(this, row1, evaluacion.getCoeficienteIntelectual().toString() );
        CompletarCelda(this, row1, evaluacion.getPuntosCompVerbal().toString());
        CompletarCelda(this, row1, evaluacion.getPuntosRazPercep().toString());
        tablaProm.addView(row1);

        TableRow row2 = new TableRow(this);
        CompletarTitulos2(this, row2, "Número de pruebas");
        CompletarCelda(this, row2, "10" );
        CompletarCelda(this, row2, "3");
        CompletarCelda(this, row2, "3");
        tablaProm.addView(row2);

        TableRow row3 = new TableRow(this);
        CompletarTitulos2(this, row3, "Media");
        Double divCI = (double)evaluacion.getCoeficienteIntelectual()/(double)10;
        CompletarCelda(this, row3, divCI.toString() );
        Double divCV = (double)evaluacion.getPuntosCompVerbal()/(double)3;
        CompletarCelda(this, row3, divCV.toString());
        Double divRP = (double)evaluacion.getPuntosRazPercep()/(double)3;
        CompletarCelda(this, row3, divRP.toString());
        tablaProm.addView(row3);
        tablaProm.setShrinkAllColumns(true);
    }

    private View.OnClickListener clickPDF() {

        return new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String fecha = null;
                SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
                fecha = sdf.format(Calendar.getInstance().getTime());

                // Create a object of PdfDocument
                PdfDocument document = new PdfDocument();
                String pdfName = "analisis" + fecha + ".pdf";
// all created files will be saved at path /sdcard/PDFDemo_AndroidSRC/
                File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                File outputFile = new File(dir.getPath(), pdfName);

// Hoja3
                View printArea3 = findViewById(R.id.Hoja3);
                PdfDocument.PageInfo pageInfo3 = new PdfDocument.PageInfo.Builder(printArea3.getWidth(),printArea3.getHeight(),1).create();
                PdfDocument.Page page3 = document.startPage(pageInfo3);
                printArea3.setVisibility(View.VISIBLE);
                printArea3.setDrawingCacheEnabled(true);
                Bitmap bitmap3 = Bitmap.createBitmap(printArea3.getDrawingCache(true),0,0,printArea3.getWidth(),printArea3.getHeight());
                printArea3.setDrawingCacheEnabled(false);
                page3.getCanvas().drawBitmap(bitmap3,1,1,null);
               // printArea3.setVisibility(View.INVISIBLE);
                document.finishPage(page3);
// Hoja4
                View printArea4 = findViewById(R.id.Hoja4);
                PdfDocument.PageInfo pageInfo4 = new PdfDocument.PageInfo.Builder(printArea4.getWidth(),printArea4.getHeight(),2).create();
                PdfDocument.Page page4 = document.startPage(pageInfo4);
                printArea4.setDrawingCacheEnabled(true);
                Bitmap bitmap4 = Bitmap.createBitmap(printArea4.getDrawingCache(true),0,0,printArea4.getWidth(),printArea4.getHeight());
                printArea4.setDrawingCacheEnabled(false);
                page4.getCanvas().drawBitmap(bitmap4,1,1,null);
                document.finishPage(page4);
//// Hoja5
//                View printArea5 = findViewById(R.id.Hoja4);
//                PdfDocument.PageInfo pageInfo5 = new PdfDocument.PageInfo.Builder(printArea5.getWidth(),printArea5.getHeight(),2).create();
//                PdfDocument.Page page5 = document.startPage(pageInfo5);
//                printArea5.setDrawingCacheEnabled(true);
//                Bitmap bitmap5 = Bitmap.createBitmap(printArea5.getDrawingCache(true),0,0,printArea5.getWidth(),printArea5.getHeight());
//                printArea5.setDrawingCacheEnabled(false);
//                page5.getCanvas().drawBitmap(bitmap5,1,1,null);
//                document.finishPage(page5);
// saving pdf document to sdcard
// all created files will be saved at path /sdcard/PDFDemo_AndroidSRC/
                try {
                    outputFile.createNewFile();
                    OutputStream out = new FileOutputStream(outputFile);
                    document.writeTo(out);
                    document.close();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
//cOMBINAR PDFS
                PDFMergerUtility ut = new PDFMergerUtility();
                try {
                    ut.addSource(dir.getPath() +"/resumen" +fecha+".pdf");
                    ut.addSource(dir.getPath() + "/" + pdfName);
                    final File file = new File(dir.getPath(), System.currentTimeMillis() + ".pdf");
                    final FileOutputStream fileOutputStream = new FileOutputStream(file);
                    ut.setDestinationStream(fileOutputStream);
                    ut.mergeDocuments(MemoryUsageSetting.setupTempFileOnly());
                    fileOutputStream.close();
                    MailSender.sendMail(Profesional.getProfesionalActual(), "Asunto", "Cuerpo", file);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
    }

    public void completarTablaComparaciones(){
        TableLayout tablacomp = (TableLayout) findViewById(R.id.tablaCompar);
        TableRow row0 = new TableRow(this);
        CompletarTitulos(this, row0, "Índice/Test");
        CompletarTitulos(this, row0, "Puntuac.trans 1");
        CompletarTitulos(this, row0, "Puntuac.trans 2");
        CompletarTitulos(this, row0, "Díferencia");
        CompletarTitulos(this, row0, "Valor Crítico");
        CompletarTitulos(this, row0, "Diferencia Significativa");
        tablacomp.addView(row0);

        TableRow row1 = new TableRow(this);
        CompletarTitulos2(this, row1, "CV - RP");
        completarComparacion(this,row1,"CV","RP");
        tablacomp.addView(row1);

        TableRow row2 = new TableRow(this);
        CompletarTitulos2(this, row2, "CV - MT");
        completarComparacion(this,row2,"CV","MT");
        tablacomp.addView(row2);

        TableRow row3 = new TableRow(this);
        CompletarTitulos2(this, row3, "CV - VP");
        completarComparacion(this,row3,"CV","VP");
        tablacomp.addView(row3);

        TableRow row4 = new TableRow(this);
        CompletarTitulos2(this, row4, "RP - MT");
        completarComparacion(this,row4,"RP","MT");
        tablacomp.addView(row4);

        TableRow row5 = new TableRow(this);
        CompletarTitulos2(this, row5, "MT - VP");
        completarComparacion(this,row5,"MT","VP");
        tablacomp.addView(row5);

        TableRow row6 = new TableRow(this);
        CompletarTitulos2(this, row6, "RP - VP");
        completarComparacion(this,row6,"RP","VP");
        tablacomp.addView(row6);
        tablacomp.setShrinkAllColumns(true);
        //TODO Agregar ultimas 3 filas con juegos
    }
    public void completarComparacion(Activity activity, TableRow row, String var1, String var2){
        Integer ptosComp1 = calcularCompuesto(var1);
        CompletarCelda(activity, row, ptosComp1.toString());
        Integer ptosComp2 = calcularCompuesto(var2);
        CompletarCelda(activity, row, ptosComp2.toString());
        Integer dif = ptosComp1-ptosComp2;
        CompletarCelda(activity, row, dif.toString());
        CompletarCelda(activity, row, "TBD");
        CompletarCelda(activity, row, "TBD");
    }

    public Integer calcularCompuesto(String var){
        Paciente paciente = Paciente.getSelectedPaciente();
        Evaluacion evaluacion = paciente.getEvaluacionFinalizada();
        Integer ptos = 0;
        switch (var){
            case "CV":
            {
                ptos = evaluacion.getPuntosCompVerbal();
                break;}
            case "RP":
            {
                ptos = evaluacion.getPuntosRazPercep();
                break;}
            case "MT":{
                ptos = evaluacion.getPuntosMemOper();
                break; }
            case "VP":{
                ptos = evaluacion.getPuntosVelocProc();
                break;}
        } return ptos;
    }


    class Columna {
        String nombre;
        Integer pos;
        Integer ptos;

        public Columna(String nombre, Integer pos){
            this.nombre = nombre;
            this.pos = pos;
            this.ptos = 0;
        }
    }

}
