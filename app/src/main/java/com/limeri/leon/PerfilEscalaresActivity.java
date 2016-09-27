package com.limeri.leon;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.opengl.GLSurfaceView;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.GridLayoutAnimationController;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.CatmullRomInterpolator;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.limeri.leon.Models.Navegacion;
import com.limeri.leon.Models.Paciente;

import org.neuroph.util.random.GaussianRandomizer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class PerfilEscalaresActivity extends Activity {


    LineGraphSeries<DataPoint> series, series2;
    GraphView graph;
    Bitmap bitmap;
    Integer mWidth, mHeight;
    private XYPlot plot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        setContentView(R.layout.activity_perfil_escalares);
        generarTablaEdad();
        completarGraficoEscalar();

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
        //  row0.setBackgroundColor(Color.parseColor("#FFFFF"));
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
        graph = (GraphView) findViewById(R.id.graph);
        graph.setTitle("Perfil de puntuaciones escalares");
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph) ;
        staticLabelsFormatter.setHorizontalLabels(new String[]
                {"","S","V","C","(I)","(Ad)","CC","Co","M","(FI)","D","LN","(A)","CI","BS","(An)"});
        staticLabelsFormatter.setVerticalLabels(new String[]
                {"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19"});

        series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0.5,12),
                new DataPoint(1,13),
                new DataPoint(1.5,14)});
        graph.addSeries(series);

    }

    public static Bitmap loadBitmapFromView(Context context, View view) {

        Bitmap bitmap;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inInputShareable = true;

        bitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
                view.getMeasuredHeight(), Bitmap.Config.ARGB_4444);
        Canvas c = new Canvas(bitmap);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setLayerType(View.LAYER_TYPE_HARDWARE,null);
        view.draw(c);
       //view.onDrawForeground(c);
        c = null;

        return bitmap;
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

                // Create a object of PdfDocument
                PdfDocument document = new PdfDocument();

// content view is EditText for my case in which user enters pdf content
            View printArea = findViewById(R.id.View);
            //bitmap = loadBitmapFromView(getApplicationContext(),printArea);
// crate a page info with attributes as below
// page number, height and width
// i have used height and width to that of pdf content view
                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(printArea.getWidth(),printArea.getHeight(),1).create();
// create a new page from the PageInfo
                PdfDocument.Page page = document.startPage(pageInfo);
// repaint the user's text into the page
                //content.draw(page.getCanvas());

               printArea.setDrawingCacheEnabled(true);
                //  graph.layout(0, 0, 100, 100);
              //  graph.buildDrawingCache(true);

               bitmap = Bitmap.createBitmap(printArea.getDrawingCache(true));
               printArea.setDrawingCacheEnabled(false);

               page.getCanvas().drawBitmap(bitmap,1,1,null);
// do final processing of the page
                document.finishPage(page);

                //pageNumber++;
                //PdfDocument.PageInfo pageInfo1 = new PdfDocument.PageInfo.Builder(100,100,2).create();
                //PdfDocument.Page page1 = document.startPage(pageInfo1);

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
}
