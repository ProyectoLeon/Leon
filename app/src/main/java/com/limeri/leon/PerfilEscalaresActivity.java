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
import android.view.Gravity;
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
import com.limeri.leon.Models.Profesional;
import com.limeri.leon.Models.PuntuacionCompuesta;

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
import java.util.StringTokenizer;

public class PerfilEscalaresActivity extends Activity {

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
        setContentView(R.layout.activity_perfil_escalares);
        cargarCaratula();
        cargarColumnas();
        generarTablaEdad();
        generarPuntuacionDirecta();
        generarTablaSumas();
        completarGraficoEscalar();
        Button documentoPdf = (Button) findViewById(R.id.pdfButton);
            if (documentoPdf != null) {
            documentoPdf.setOnClickListener(clickPDF());
        }
    }


    public void cargarCaratula(){
        TextView NombreProf = (TextView) findViewById(R.id.nombreProf);
        TextView NombrePaciente = (TextView) findViewById(R.id.nombrePac);
        TextView FechaNac = (TextView) findViewById(R.id.Edad);
        TextView FechaEval = (TextView) findViewById(R.id.FechaEval);

        Paciente paciente = Paciente.getSelectedPaciente();
        Profesional profesional = Profesional.getProfesionalActual();
        NombreProf.setText(profesional.getNombre());
        NombrePaciente.setText(paciente.getNombre());
        int año = Calendar.getInstance().get(Calendar.YEAR);
        Integer edad = paciente.cantidadAños(año);
        FechaNac.setText(edad.toString());
        FechaEval.setText(paciente.getEvaluacionFinalizada().getFechaEvaluación().toString());

    }
    public void generarPuntuacionDirecta() {

        TableLayout tablaPD = (TableLayout) findViewById(R.id.tablePuntDir);
        TableRow row0 = new TableRow(this);
        CompletarTitulos(this, row0, "Subtest");
        CompletarTitulos(this, row0, " PD ");
        CompletarTitulos(this, row0, " PE ");
        CompletarTitulos(this, row0, " PE ");
        CompletarTitulos(this, row0, " PE ");
        CompletarTitulos(this, row0, " PE ");
        CompletarTitulos(this, row0, " PE ");
        tablaPD.addView(row0);
        CompletarPuntajeJuego(tablaPD);
    }

    public void CompletarPuntajeJuego(TableLayout tabla){
        compVerbal = 0;
        razPercep = 0;
        memOper = 0;
        velProc = 0;
        CI = 0;

        Paciente paciente = Paciente.getSelectedPaciente();
        Evaluacion evaluacion = paciente.getEvaluacionFinalizada();
        List<Juego> listaJuegos = evaluacion.getJuegos();
        for (Juego juego : listaJuegos) {
            TableRow row = new TableRow(this);
            CompletarTitulos2(this, row, juego.getNombre());
            CompletarCelda(this, row, juego.getPuntosJuego().toString());
            String escalar = juego.getPuntajeEscalar().toString();
            TextView col = new TextView(this);
            col.setText(escalar);
            col.setTextSize(10);
            tabla.setShrinkAllColumns(true);
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
        CI = compVerbal + razPercep + memOper + velProc;
        TableRow row = new TableRow(this);
        CompletarTitulos2(this,row,"Suma puntuaciones ecalares");
        CompletarTitulos2(this,row,"");
        CompletarCelda(this,row,compVerbal.toString());
        CompletarCelda(this,row,razPercep.toString());
        CompletarCelda(this,row,memOper.toString());
        CompletarCelda(this,row,velProc.toString());
        CompletarCelda(this,row,CI.toString());
        tabla.addView(row);
    }

    public String CompararFechas(String DateEva, String DateBirth) {
        String dayDifference = null;

        try {

            Integer mesRestado = 0;
            Integer añoRestado = 0;
            String strdia;
            String strmes;
            String straño;
            Integer diadif;
            Integer mesdif;
            Integer añodif;


            SimpleDateFormat dates = new SimpleDateFormat("dd/mm/yyyy");

// Obtengo substring de cada parte de la fecha (día, mes y año)
            strdia = DateEva.toString().substring(0, 2);
            strmes = DateEva.toString().substring(3, 5);
            straño = DateEva.toString().substring(6, 10);

            Integer diaeval = Integer.valueOf(strdia);
            Integer meseval = Integer.valueOf(strmes);
            Integer añoeval = Integer.valueOf(straño);

            strdia = DateBirth.toString().substring(6, 8);
            strmes = DateBirth.toString().substring(3, 5);
            straño = DateBirth.toString().substring(0, 2);

            Integer dianac = Integer.valueOf(strdia);
            Integer mesnac = Integer.valueOf(strmes);
            Integer añonac = Integer.valueOf(straño);

// Calcular la diferencia en años, meses y días.
            if (diaeval < dianac) {
                mesRestado = 1;
                diadif = diaeval + 30 - dianac;
            } else {
                diadif = diaeval - dianac;
            }

            if (mesRestado == 1)
                meseval = meseval - 1;

            if (meseval < mesnac) {
                añoRestado = 1;
                mesdif = meseval + 30 - mesnac;
            } else {
                mesdif = meseval - mesnac;
            }

            if (añoRestado == 1)
                añoeval = añoeval - 1;

            añodif = añoeval - añonac;


// Lo volvemos a pasar a String
            strdia = diadif.toString();
            strmes = mesdif.toString();
            straño = añodif.toString();

// Para evitar que el día, el mes y el año queden sin el cero adelante. Por ejemplo si es 9, queda 09.
            if (strdia.length() == 1)
                strdia = 0 + strdia;

            if (strmes.length() == 1)
                strmes = 0 + strmes;

            if (straño.length() == 1)
                straño = 0 + straño;

// Lo devolvemos en AA,MM,DD
            dayDifference = strdia + ',' + strmes + ',' + straño;

        } catch (Exception exception) {
            Log.e("DIDN'T WORK", "exception " + exception);

        }
        return dayDifference;
    }

    public void generarTablaEdad() {

        TableLayout tablaedad = (TableLayout) findViewById(R.id.tableEdad);
        TableRow row0 = new TableRow(this);
        CompletarTitulos(this, row0, "");
        CompletarTitulos(this, row0, " Año ");
        CompletarTitulos(this, row0, " Mes ");
        CompletarTitulos(this, row0, " Día ");
        tablaedad.addView(row0);

        TableRow row1 = new TableRow(this);
        CompletarTitulos2(this, row1, "Fecha de aplicación  ");
        Calendar cal = Calendar.getInstance();
        cal.getTime().getTime();
        cal.setTimeInMillis(System.currentTimeMillis());
        Integer añoev = cal.get(Calendar.YEAR);
        Integer mesev = cal.get(Calendar.MONTH) + 1;
        Integer diaev = cal.get(Calendar.DAY_OF_MONTH);
        // Para evitar que el día y el mes queden sin el cero adelante. Por ejemplo si es 9, queda 09.
        String strmesev = mesev.toString();
        if (strmesev.length() == 1)
            strmesev = 0 + strmesev;

        String strdiaev = diaev.toString();
        if (strdiaev.length() == 1)
            strdiaev = 0 + strdiaev;

        String dateev = strdiaev + '/' + strmesev + '/' + añoev.toString();
        Paciente.getSelectedPaciente().getEvaluacionFinalizada().setFechaEvaluación(dateev);
        CompletarCelda(this, row1, añoev.toString());
        CompletarCelda(this, row1, strmesev);
        CompletarCelda(this, row1, strdiaev);
        tablaedad.addView(row1);


        TableRow row2 = new TableRow(this);
        CompletarTitulos2(this, row2, "Fecha de Nacimiento ");
        String dia = Paciente.getSelectedPaciente().getFechaNac().toString().substring(0, 2);
        String mes = Paciente.getSelectedPaciente().getFechaNac().toString().substring(3, 5);
        String año = Paciente.getSelectedPaciente().getFechaNac().toString().substring(6, 10);
        ;
        CompletarCelda(this, row2, año);
        CompletarCelda(this, row2, mes);
        CompletarCelda(this, row2, dia);
        tablaedad.addView(row2);

        TableRow row3 = new TableRow(this);
        CompletarTitulos2(this, row3, "Edad cronológica");
        String diferencia = CompararFechas(dateev, Paciente.getSelectedPaciente().getFechaNac().toString());
        CompletarCelda(this, row3, diferencia.substring(0,2));
        CompletarCelda(this, row3, diferencia.substring(3,5));
        CompletarCelda(this, row3, diferencia.substring(6,8));
        tablaedad.addView(row3);
    }

    public void CompletarTitulos(Activity activity, TableRow row, String txt) {
        TextView col = new TextView(activity);
        col.setTextSize(8);
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
        col.setTextSize(7);
        col.setBackgroundColor(getResources().getColor(R.color.verde_mas_claro));
        col.setPadding(1,1,1,1);
        col.setTextColor(Color.DKGRAY);
        col.setText(txt);
        row.addView(col);
    }


    public void CompletarCelda(Activity activity, TableRow row, String txt) {
        TextView col = new TextView(activity);
        col.setTextSize(6);
        col.setBackground(getResources().getDrawable(R.drawable.cell_shape));
 //       col.setBackground(getDrawable(R.drawable.cell_shape));
        col.setText(txt);
        col.setGravity(Gravity.CENTER_HORIZONTAL);
        row.addView(col);
    }

    public void completarGraficoEscalar(){
    GraphView graph;
        Paciente paciente = Paciente.getSelectedPaciente();
        Evaluacion evaluacion = paciente.getEvaluacionFinalizada();
        agregarPuntajeEscalar();
        graph = (GraphView) findViewById(R.id.graphEscalar);
        //graph.setTitle("Perfil de puntuaciones escalares");
        graph.setTitleColor(getResources().getColor(R.color.verde_claro));

        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(15);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(19);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph) ;
        staticLabelsFormatter.setHorizontalLabels(new String[]
              {"","S","V","C","(I)","(Ad)","CC","Co","M","(FI)","D","LN","(A)","CI","BS","(An)"});
        staticLabelsFormatter.setVerticalLabels(new String[]
              {"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19"});
       // staticLabelsFormatter.formatLabel(5,true);
        graph.getGridLabelRenderer().setTextSize(5f);
        graph.getGridLabelRenderer().setLabelsSpace(12);
        graph.getGridLabelRenderer().setGridColor(getResources().getColor(R.color.verde_mas_claro));
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.BOTH);
        //graph.getGridLabelRenderer().setLabelHorizontalHeight(80);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Subtests");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Puntaje Escalar");
        //graph.getGridLabelRenderer().setLabelVerticalWidth(80);
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(true);
        graph.getGridLabelRenderer().setVerticalLabelsVisible(true);
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


    public void CompletarSumas(TableLayout tabla) {
        Paciente paciente = Paciente.getSelectedPaciente();
        Evaluacion evaluacion = paciente.getEvaluacionFinalizada();
        PuntuacionCompuesta puntuacionCompuesta = null;

        puntuacionCompuesta = PuntuacionCompuesta.getPuntuacionCompuesta("ICV",evaluacion.getPuntosCompVerbal());
        TableRow row1 = new TableRow(this);
        CompletarTitulos2(this, row1, "Compren. Verbal");
        CompletarCelda(this, row1, compVerbal.toString());
        CompletarCelda(this, row1, puntuacionCompuesta.getEquivalencia().toString());
        CompletarCelda(this, row1, puntuacionCompuesta.getpercentil().toString());
        CompletarCelda(this, row1, puntuacionCompuesta.getNivelConfianza().toString());
        tabla.addView(row1);

        puntuacionCompuesta = PuntuacionCompuesta.getPuntuacionCompuesta("IRP", evaluacion.getPuntosRazPercep());
        TableRow row2 = new TableRow(this);
        CompletarTitulos2(this, row2, "Razonam.Perceptivo");
        CompletarCelda(this, row2, razPercep.toString());
        CompletarCelda(this, row2, puntuacionCompuesta.getEquivalencia().toString());
        CompletarCelda(this, row2, puntuacionCompuesta.getpercentil().toString());
        CompletarCelda(this, row2, puntuacionCompuesta.getNivelConfianza().toString());
        tabla.addView(row2);

        puntuacionCompuesta = PuntuacionCompuesta.getPuntuacionCompuesta("IMO",evaluacion.getPuntosMemOper());
        TableRow row3 = new TableRow(this);
        CompletarTitulos2(this, row3, "Memoria Operativa");
        CompletarCelda(this, row3, memOper.toString());
        CompletarCelda(this, row3, puntuacionCompuesta.getEquivalencia().toString());
        CompletarCelda(this, row3, puntuacionCompuesta.getpercentil().toString());
        CompletarCelda(this, row3, puntuacionCompuesta.getNivelConfianza().toString());
        tabla.addView(row3);

        puntuacionCompuesta = PuntuacionCompuesta.getPuntuacionCompuesta("IVP",evaluacion.getPuntosVelocProc());
        TableRow row4 = new TableRow(this);
        CompletarTitulos2(this, row4, "Veloc.Procesam.i ");
        CompletarCelda(this, row4, velProc.toString());
        CompletarCelda(this, row4, puntuacionCompuesta.getEquivalencia().toString());
        CompletarCelda(this, row4, puntuacionCompuesta.getpercentil().toString());
        CompletarCelda(this, row4, puntuacionCompuesta.getNivelConfianza().toString());
        tabla.addView(row4);

        puntuacionCompuesta = PuntuacionCompuesta.getPuntuacionCompuesta("CIT", evaluacion.getCoeficienteIntelectual());
        TableRow row5 = new TableRow(this);
        CompletarTitulos2(this, row5, "CI Total");
        CompletarCelda(this, row5, CI.toString());
        CompletarCelda(this, row5, puntuacionCompuesta.getEquivalencia().toString());
        CompletarCelda(this, row5, puntuacionCompuesta.getpercentil().toString());
        CompletarCelda(this, row5, puntuacionCompuesta.getNivelConfianza().toString());
        tabla.addView(row5);
    }


    public void generarTablaSumas() {

        TableLayout tablaSumas = (TableLayout) findViewById(R.id.tablaSumas);
        TableRow row0 = new TableRow(this);
        CompletarTitulos(this, row0, "Escalas");
        CompletarTitulos(this, row0, "Escalares");
        CompletarTitulos(this, row0, "Compuesta");
        CompletarTitulos(this, row0, "Percentil");
        CompletarTitulos(this, row0, "% IC");
        //    row0.setBackgroundColor(Color.parseColor("#FFFFFFF"));
        tablaSumas.setShrinkAllColumns(true);
        tablaSumas.addView(row0);
        CompletarSumas(tablaSumas);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Navegacion.irA(this, MainActivity.class);
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
                PdfDocument.PageInfo pageInfo2 = new PdfDocument.PageInfo.Builder(printArea2.getWidth(),printArea2.getHeight(),3).create();
                PdfDocument.Page page2 = document.startPage(pageInfo2);
                printArea2.setVisibility(View.VISIBLE);
                printArea2.setDrawingCacheEnabled(true);
                Bitmap bitmap2 = Bitmap.createBitmap(printArea2.getDrawingCache(true),0,0,printArea2.getWidth(),printArea2.getHeight());
                printArea2.setDrawingCacheEnabled(false);
                page2.getCanvas().drawBitmap(bitmap2,1,1,null);
                //printArea2.setVisibility(View.INVISIBLE);
                document.finishPage(page2);

//// Hoja3
//                View printArea3 = findViewById(R.id.Hoja3);
//                PdfDocument.PageInfo pageInfo3 = new PdfDocument.PageInfo.Builder(printArea3.getWidth(),printArea3.getHeight(),1).create();
//                PdfDocument.Page page3 = document.startPage(pageInfo3);
//                printArea3.setVisibility(View.VISIBLE);
//                printArea3.setDrawingCacheEnabled(true);
//                Bitmap bitmap3 = Bitmap.createBitmap(printArea3.getDrawingCache(true),0,0,printArea3.getWidth(),printArea3.getHeight());
//                printArea3.setDrawingCacheEnabled(false);
//                page3.getCanvas().drawBitmap(bitmap3,1,1,null);
//                printArea3.setVisibility(View.INVISIBLE);
//                document.finishPage(page3);
//// Hoja4
//                View printArea4 = findViewById(R.id.Hoja4);
//                PdfDocument.PageInfo pageInfo4 = new PdfDocument.PageInfo.Builder(printArea4.getWidth(),printArea4.getHeight(),2).create();
//                PdfDocument.Page page4 = document2.startPage(pageInfo4);
//                printArea4.setDrawingCacheEnabled(true);
//                Bitmap bitmap4 = Bitmap.createBitmap(printArea4.getDrawingCache(true),0,0,printArea4.getWidth(),printArea4.getHeight());
//                printArea4.setDrawingCacheEnabled(false);
//                page4.getCanvas().drawBitmap(bitmap4,1,1,null);
//                document2.finishPage(page4);
////// Hoja5
//                View printArea5 = findViewById(R.id.Hoja4);
//                PdfDocument.PageInfo pageInfo5 = new PdfDocument.PageInfo.Builder(printArea5.getWidth(),printArea5.getHeight(),2).create();
//                PdfDocument.Page page5 = document.startPage(pageInfo5);
//                printArea5.setDrawingCacheEnabled(true);
//                Bitmap bitmap5 = Bitmap.createBitmap(printArea5.getDrawingCache(true),0,0,printArea5.getWidth(),printArea5.getHeight());
//                printArea5.setDrawingCacheEnabled(false);
//                page5.getCanvas().drawBitmap(bitmap5,1,1,null);
//                document.finishPage(page5);
// saving pdf document to sdcard

                String pdfName = "resumen" + fecha + ".pdf";
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
            Navegacion.irA(PerfilEscalaresActivity.this,PerfilCompuestasActivity.class);
            }
        };
    }

    private void cargarColumnas() {
        columnas = new ArrayList<>();
        columnas.add(new Columna("Semejanzas",1));
        columnas.add(new Columna("Vocabulario",2));
        columnas.add(new Columna("Comprensión", 3));
        columnas.add(new Columna("Información",4));
        columnas.add(new Columna("Adivinanzas",5));
        columnas.add(new Columna("Construcción con Cubos",6));
        columnas.add(new Columna("Conceptos",7));
        columnas.add(new Columna("Matrices",8));
        columnas.add(new Columna("Completamiento de Figuras",9));
        columnas.add(new Columna("Retención de Dígitos",10));
        columnas.add(new Columna("Letras y Números", 11));
        columnas.add(new Columna("Aritmética",12));
        columnas.add(new Columna("Claves", 13));
        columnas.add(new Columna("Búsqueda de Símbolos",14));
        columnas.add(new Columna("Animales",15));

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
