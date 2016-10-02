package com.limeri.leon;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.pdf.PdfDocument;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class PerfilEscalaresActivity extends Activity {


    Bitmap bitmap;
    Integer mWidth, mHeight;
    private XYPlot plot;
    List<Columna> columnas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        setContentView(R.layout.activity_perfil_escalares);
        cargarColumnas();
        generarTablaEdad();
        completarGraficoEscalar();
        completarGraficoCompuesto();
        completarTablaComparaciones();
        completarPtosFuertesyDebiles();
        ImageView imageView = (ImageView) findViewById(R.id.encabezado);
        imageView.setImageResource(R.drawable.encabezado);
        Button documentoPdf = (Button) findViewById(R.id.pdfButton);
        if (documentoPdf != null) {
            documentoPdf.setOnClickListener(clickPDF());
        }
    }

    public void generarTablaEdad() {

        TableLayout tablaedad = (TableLayout) findViewById(R.id.tableView);
        TableRow row0 = new TableRow(this);
        CompletarCelda(this, row0, "");
        CompletarCelda(this, row0, "Año");
        CompletarCelda(this, row0, "Mes");
        CompletarCelda(this, row0, "Día");
        tablaedad.addView(row0);

        TableRow row1 = new TableRow(this);
        CompletarCelda(this, row1, "Fecha de aplicación");
        Calendar cal = Calendar.getInstance();
        cal.getTime().getTime();
        cal.setTimeInMillis(System.currentTimeMillis());
        Integer añoev = cal.get(Calendar.YEAR);
        Integer mesev = cal.get(Calendar.MONTH) + 1;
        Integer diaev = cal.get(Calendar.DAY_OF_MONTH);
        String dateev = diaev.toString() + '/' + mesev.toString() + '/' + añoev.toString();
        CompletarCelda(this, row1, añoev.toString());
        CompletarCelda(this, row1, mesev.toString());
        CompletarCelda(this, row1, diaev.toString());
        tablaedad.addView(row1);

        TableRow row2 = new TableRow(this);
        CompletarCelda(this, row2, "Fecha de Nacimiento");
        String dia = Paciente.getSelectedPaciente().getFechaNac().toString().substring(0, 2);
        String mes = Paciente.getSelectedPaciente().getFechaNac().toString().substring(3, 5);
        String año = Paciente.getSelectedPaciente().getFechaNac().toString().substring(6, 10);
        ;
        CompletarCelda(this, row2, año);
        CompletarCelda(this, row2, mes);
        CompletarCelda(this, row2, dia);
        tablaedad.addView(row2);

        TableRow row3 = new TableRow(this);
        CompletarCelda(this, row3, "Edad cronológica");
        String diferencia = CompararFechas(dateev, Paciente.getSelectedPaciente().getFechaNac().toString());
        CompletarCelda(this, row3, diferencia);
        CompletarCelda(this, row3, diferencia);
        CompletarCelda(this, row3, diferencia);
        tablaedad.addView(row3);
    }

    public void CompletarCelda(Activity activity, TableRow row, String txt) {
        TextView col = new TextView(activity);
        col.setTextSize(10);
        col.setText(txt);
        row.addView(col);
    }

    public String CompararFechas(String DateEva, String DateBirth) {
        String dayDifference = null;

        try {

            Date date1;
            Date date2;

            SimpleDateFormat dates = new SimpleDateFormat("dd/mm/yyyy");

            //Setting dates
            date1 = dates.parse(DateEva);
            date2 = dates.parse(DateBirth);

            //TODO: Calcular diferencia entre fechas (dia, mes y año)
            long difference = Math.abs(date1.getTime() - date2.getTime());
            long differenceDates = difference / (24 * 60 * 60 * 1000);
            long años = differenceDates / 365;
            long meses = (differenceDates % 365) / 24;

            dayDifference = Long.toString(differenceDates);

        } catch (Exception exception) {
            Log.e("DIDN'T WORK", "exception " + exception);

        }
        return dayDifference;
    }
    public void completarGraficoEscalar(){
    GraphView graph;
        Paciente paciente = Paciente.getSelectedPaciente();
        Evaluacion evaluacion = paciente.getEvaluacionFinalizada();
        agregarPuntajeEscalar();
        graph = (GraphView) findViewById(R.id.graphEscalar);
        graph.setTitle("Perfil de puntuaciones escalares");
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(15);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(19);
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph) ;
        staticLabelsFormatter.setHorizontalLabels(new String[]
              {"","S","V","C","(I)","(Ad)","CC","Co","M","(FI)","D","LN","(A)","CI","BS","(An)"});
        staticLabelsFormatter.setVerticalLabels(new String[]
              {"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19"});
       // staticLabelsFormatter.formatLabel(5,true);
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
        graph.addSeries(crearSerie(0,4, Color.GREEN));
        graph.addSeries(crearSerie(5,8, Color.BLUE));
        graph.addSeries(crearSerie(9,11, Color.RED));
        graph.addSeries(crearSerie(12,14, Color.YELLOW));
    }

    public void agregarPuntajeEscalar(){
        Paciente paciente = Paciente.getSelectedPaciente();
        Evaluacion evaluacion = paciente.getEvaluacionFinalizada();
        List<Juego> listaJuegos = evaluacion.getJuegos();
        for (Juego juego : listaJuegos){
            for (Columna columna:columnas){
                if (juego.getNombre().equals(columna.nombre)){
                    columna.ptos=juego.getPuntajeEscalar();
                }
            }}

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
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph) ;
        staticLabelsFormatter.setHorizontalLabels(new String[]
                {"","CV","RP","MT","VP","CIT"});
        staticLabelsFormatter.setVerticalLabels(new String[]
                {"40","50","60","70","80","90","100","110","120","130","140","150","160"});
        graph.getGridLabelRenderer().setTextSize(6f);
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.BOTH);
        graph.getGridLabelRenderer().setLabelHorizontalHeight(10);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Índices");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Puntaje Compuesto");
        graph.getGridLabelRenderer().setLabelVerticalWidth(50);
        //graph.getGridLabelRenderer().setHorizontalLabelsVisible(true);
        //graph.getGridLabelRenderer().setVerticalLabelsVisible(true);
        //graph.getGridLabelRenderer().reloadStyles();

        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        Paciente paciente = Paciente.getSelectedPaciente();
        Evaluacion evaluacion = paciente.getEvaluacionFinalizada();
        LineGraphSeries serie = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0.5, evaluacion.getPuntosCompVerbal()),
                new DataPoint(1,evaluacion.getPuntosRazPercep()),
                new DataPoint(1.5,evaluacion.getPuntosMemOper()),
                new DataPoint(2,evaluacion.getPuntosVelocProc()),
                new DataPoint(2.5,evaluacion.getCoeficienteIntelectual())});
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
    CompletarCelda(this, row0, "Test");
    CompletarCelda(this, row0, "Puntuación escalar");
    CompletarCelda(this, row0, "Media de Pe");
    CompletarCelda(this, row0, "Distancia a la media");
    CompletarCelda(this, row0, "Valor Crítico");
    CompletarCelda(this, row0, "Punto Fuerte o Débil");
    CompletarCelda(this, row0, "Tasa Base");
    tablaFyD.addView(row0);

    TableRow row1 = new TableRow(this);
    CompletarCelda(this, row1, "Construcción con Cubos");
    validarFuerteDebil(this,row1,"Construcción con Cubos");
    tablaFyD.addView(row1);

    TableRow row2 = new TableRow(this);
    CompletarCelda(this, row2, "Semejanzas");
    validarFuerteDebil(this,row2,"Semejanzas");
    tablaFyD.addView(row2);

    TableRow row3 = new TableRow(this);
    CompletarCelda(this, row3, "Dígitos");
    validarFuerteDebil(this,row3,"Dígitos");
    tablaFyD.addView(row3);

    TableRow row4 = new TableRow(this);
    CompletarCelda(this, row4, "Conceptos");
    validarFuerteDebil(this,row4,"Conceptos");
    tablaFyD.addView(row4);

    TableRow row5 = new TableRow(this);
    CompletarCelda(this, row5, "Claves");
    validarFuerteDebil(this,row5,"Claves");
    tablaFyD.addView(row5);

    TableRow row6 = new TableRow(this);
    CompletarCelda(this, row6, "Vocabulario");
    validarFuerteDebil(this,row6,"Vocabulario");
    tablaFyD.addView(row6);

    TableRow row7 = new TableRow(this);
    CompletarCelda(this, row7, "Letras y Números");
    validarFuerteDebil(this,row7,"Letras y Números");
    tablaFyD.addView(row7);

    TableRow row8 = new TableRow(this);
    CompletarCelda(this, row8, "Matrices");
    validarFuerteDebil(this,row8,"Matrices");
    tablaFyD.addView(row8);

    TableRow row9 = new TableRow(this);
    CompletarCelda(this, row9, "Comprensión");
    validarFuerteDebil(this,row9,"Comprensión");
    tablaFyD.addView(row9);

    TableRow row10 = new TableRow(this);
    CompletarCelda(this, row10, "Búsqueda de Símbolos");
    validarFuerteDebil(this,row10,"Búsqueda de Símbolos");
    tablaFyD.addView(row10);

}

    public void validarFuerteDebil(Activity activity, TableRow row, String string){
        Paciente paciente = Paciente.getSelectedPaciente();
        Evaluacion evaluacion = paciente.getEvaluacionFinalizada();
        List<Juego> listaJuegos = evaluacion.getJuegos();
        for (Juego juego : listaJuegos){
            if (juego.getNombre().equals(string)){
                CompletarCelda(activity, row, juego.getPuntosJuego().toString());
                CompletarCelda(activity, row, "Media");
                CompletarCelda(activity, row, "Dif media");
                CompletarCelda(activity, row, "Pendiente tabla");
                CompletarCelda(activity, row, "Pendiente tabla");
                CompletarCelda(activity, row, "Pendiente tabla");
            }
        }

    }
    private View.OnClickListener clickPDF() {

        return new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // Create a object of PdfDocument
                PdfDocument document = new PdfDocument();
//Hoja numero 0 - Carátula
                View printArea0 = findViewById(R.id.caratula);
                PdfDocument.PageInfo pageInfo0 = new PdfDocument.PageInfo.Builder(printArea0.getWidth(),printArea0.getHeight(),1).create();
                PdfDocument.Page page0 = document.startPage(pageInfo0);
                printArea0.setVisibility(View.VISIBLE);
                printArea0.draw(page0.getCanvas());
                printArea0.setVisibility(View.INVISIBLE);
                document.finishPage(page0);

// Hoja1
                View printArea1 = findViewById(R.id.Hoja1);
                PdfDocument.PageInfo pageInfo1 = new PdfDocument.PageInfo.Builder(printArea1.getWidth(),printArea1.getHeight(),2).create();
                PdfDocument.Page page1 = document.startPage(pageInfo1);
                printArea1.setDrawingCacheEnabled(true);
                Bitmap bitmap1 = Bitmap.createBitmap(printArea1.getDrawingCache(true),0,0,printArea1.getWidth(),printArea1.getHeight());
                printArea1.setDrawingCacheEnabled(false);
                page1.getCanvas().drawBitmap(bitmap1,1,1,null);
                document.finishPage(page1);
// Hoja2
                View printArea2 = findViewById(R.id.Hoja2);
                PdfDocument.PageInfo pageInfo2 = new PdfDocument.PageInfo.Builder(printArea2.getWidth(),printArea2.getHeight(),1).create();
                PdfDocument.Page page2 = document.startPage(pageInfo2);
                printArea2.setVisibility(View.VISIBLE);
                printArea2.setDrawingCacheEnabled(true);
                Bitmap bitmap2 = Bitmap.createBitmap(printArea2.getDrawingCache(true),0,0,printArea2.getWidth(),printArea2.getHeight());
                printArea2.setDrawingCacheEnabled(false);
                page2.getCanvas().drawBitmap(bitmap2,1,1,null);
                printArea2.setVisibility(View.INVISIBLE);
                document.finishPage(page2);

// Hoja3
                View printArea3 = findViewById(R.id.Hoja3);
                PdfDocument.PageInfo pageInfo3 = new PdfDocument.PageInfo.Builder(printArea3.getWidth(),printArea3.getHeight(),1).create();
                PdfDocument.Page page3 = document.startPage(pageInfo3);
                printArea3.setVisibility(View.VISIBLE);
                printArea3.setDrawingCacheEnabled(true);
                Bitmap bitmap3 = Bitmap.createBitmap(printArea3.getDrawingCache(true),0,0,printArea3.getWidth(),printArea3.getHeight());
                printArea3.setDrawingCacheEnabled(false);
                page3.getCanvas().drawBitmap(bitmap3,1,1,null);
                printArea3.setVisibility(View.INVISIBLE);
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
// Hoja5
                View printArea5 = findViewById(R.id.Hoja4);
                PdfDocument.PageInfo pageInfo5 = new PdfDocument.PageInfo.Builder(printArea5.getWidth(),printArea5.getHeight(),2).create();
                PdfDocument.Page page5 = document.startPage(pageInfo5);
                printArea5.setDrawingCacheEnabled(true);
                Bitmap bitmap5 = Bitmap.createBitmap(printArea5.getDrawingCache(true),0,0,printArea5.getWidth(),printArea5.getHeight());
                printArea5.setDrawingCacheEnabled(false);
                page5.getCanvas().drawBitmap(bitmap5,1,1,null);
                document.finishPage(page5);
// saving pdf document to sdcard
                SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyhhmmss");
                String pdfName = "pdfdemo"
                        + sdf.format(Calendar.getInstance().getTime()) + ".pdf";

// all created files will be saved at path /sdcard/PDFDemo_AndroidSRC/
                File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                File outputFile = new File(dir.getPath(), pdfName);

                try {
                    outputFile.createNewFile();
                    OutputStream out = new FileOutputStream(outputFile);
                    document.writeTo(out);
                    document.close();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void cargarColumnas() {
        columnas = new ArrayList<>();
        columnas.add(new Columna("Semejanzas",0));
        columnas.add(new Columna("Vocabulario",1));
        columnas.add(new Columna("Comprensión", 2));
        columnas.add(new Columna("Información",3));
        columnas.add(new Columna("Adivinanzas",4));
        columnas.add(new Columna("Construcción con Cubos",5));
        columnas.add(new Columna("Conceptos",6));
        columnas.add(new Columna("Matrices",7));
        columnas.add(new Columna("Completamiento de Figuras",8));
        columnas.add(new Columna("Retención de Dígitos",8));
        columnas.add(new Columna("Letras y Números", 10));
        columnas.add(new Columna("Aritmética",11));
        columnas.add(new Columna("Claves", 12));
        columnas.add(new Columna("Búsqueda de Símbolos",13));
        columnas.add(new Columna("Animales",14));

//        columnas.add(new Columna("Semejanzas",1));
//        columnas.add(new Columna("Vocabulario",1.0));
//        columnas.add(new Columna("Comprensión", 1.5));
//        columnas.add(new Columna("Información", 2.0));
//        columnas.add(new Columna("Adivinanzas",2.5));
//        columnas.add(new Columna("Construcción con Cubos", 3.0));
//        columnas.add(new Columna("Conceptos", 3.5));
//        columnas.add(new Columna("Matrices",4.0));
//        columnas.add(new Columna("Completamiento de Figuras", 4.5));
//        columnas.add(new Columna("Retención de Dígitos",5.0));
//        columnas.add(new Columna("Letras y Números", 5.5));
//        columnas.add(new Columna("Aritmética",6.0));
//        columnas.add(new Columna("Claves", 6.5));
//        columnas.add(new Columna("Búsqueda de Símbolos",7.0));
//        columnas.add(new Columna("Animales",7.5));

    }

    public void completarTablaComparaciones(){
        TableLayout tablacomp = (TableLayout) findViewById(R.id.tablaCompar);
        TableRow row0 = new TableRow(this);
        CompletarCelda(this, row0, "Índice/Test");
        CompletarCelda(this, row0, "Puntuac.trans 1");
        CompletarCelda(this, row0, "Puntuac.trans 2");
        CompletarCelda(this, row0, "Díferencia");
        CompletarCelda(this, row0, "Valor Crítico");
        CompletarCelda(this, row0, "Diferencia Significativa");
        CompletarCelda(this, row0, "Tasa Base"
        );
        tablacomp.addView(row0);

            TableRow row1 = new TableRow(this);
            CompletarCelda(this, row1, "CV - RP");
            completarComparacion(this,row1,"CV","RP");
            tablacomp.addView(row1);

        TableRow row2 = new TableRow(this);
        CompletarCelda(this, row2, "CV - MT");
        completarComparacion(this,row2,"CV","MT");
        tablacomp.addView(row2);

        TableRow row3 = new TableRow(this);
        CompletarCelda(this, row3, "CV - VP");
        completarComparacion(this,row3,"CV","VP");
        tablacomp.addView(row3);

        TableRow row4 = new TableRow(this);
        CompletarCelda(this, row4, "RP - MT");
        completarComparacion(this,row4,"RP","MT");
        tablacomp.addView(row4);

        TableRow row5 = new TableRow(this);
        CompletarCelda(this, row5, "MT - VP");
        completarComparacion(this,row5,"MT","VP");
        tablacomp.addView(row5);

        TableRow row6 = new TableRow(this);
        CompletarCelda(this, row6, "RP - VP");
        completarComparacion(this,row6,"RP","VP");
        tablacomp.addView(row6);
   //TODO Agregar ultimas 3 filas con juegos
    }
    public void completarComparacion(Activity activity, TableRow row, String var1, String var2){
        Integer ptosComp1 = calcularCompuesto(var1);
        CompletarCelda(activity, row, ptosComp1.toString());
        Integer ptosComp2 = calcularCompuesto(var2);
        CompletarCelda(activity, row, ptosComp2.toString());
        Integer dif = ptosComp1-ptosComp2;
        CompletarCelda(activity, row, dif.toString());
        CompletarCelda(activity, row, "Pendiente tabla");
        CompletarCelda(activity, row, "Pendiente tabla");
        CompletarCelda(activity, row, "Pendiente tabla");
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
