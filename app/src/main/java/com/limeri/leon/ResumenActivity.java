package com.limeri.leon;

import android.app.Activity;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.limeri.leon.Models.Navegacion;
import com.limeri.leon.Models.Paciente;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ResumenActivity extends AppCompatActivity {
    private int tabla = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen);
        generarTablaEdad();

        Button siguiente = (Button) findViewById(R.id.siguienteButton);
        if (siguiente != null) {
            siguiente.setOnClickListener(clickSiguiente());
        }
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

// Para evitar que el día y el mes queden sin el cero adelante. Por ejemplo si es 9, queda 09.
        String strmesev = mesev.toString();
        if (strmesev.length() == 1)
            strmesev = 0 + strmesev;

        String strdiaev = diaev.toString();
        if (strdiaev.length() == 1)
            strdiaev = 0 + strdiaev;

        String dateev = strdiaev + '/' + strmesev + '/' + añoev.toString();
        CompletarCelda(this, row1, añoev.toString());
        CompletarCelda(this, row1, strmesev);
        CompletarCelda(this, row1, strdiaev);
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
        CompletarCelda(this, row3, diferencia.substring(6, 8));
        CompletarCelda(this, row3, diferencia.substring(3, 5));
        CompletarCelda(this, row3, diferencia.substring(0, 2));
        tablaedad.addView(row3);
    }

    public void CompletarCelda(Activity activity, TableRow row, String txt) {
        TextView col = new TextView(activity);
        col.setText(txt);
        row.addView(col);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Navegacion.irA(this, MainActivity.class);
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

            strdia = DateBirth.toString().substring(0, 2);
            strmes = DateBirth.toString().substring(3, 5);
            straño = DateBirth.toString().substring(6, 10);

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

// Para evitar que el día y el mes queden sin el cero adelante. Por ejemplo si es 9, queda 09.
            if (strdia.length() == 1)
                strdia = 0 + strdia;

            if (strmes.length() == 1)
                strmes = 0 + strmes;

// Lo devolvemos en AA,MM,DD
            dayDifference = strdia + ',' + strmes + ',' + straño;

        } catch (Exception exception) {
            Log.e("DIDN'T WORK", "exception " + exception);

        }
        return dayDifference;
    }

    private View.OnClickListener clickSiguiente() {

        return new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Navegacion.irA(ResumenActivity.this, MainActivity.class);
            }
        };
    }

    private View.OnClickListener clickPDF() {

        return new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // Create a object of PdfDocument
                PdfDocument document = new PdfDocument();

// content view is EditText for my case in which user enters pdf content
                View content = findViewById(R.id.tableView);

// crate a page info with attributes as below
// page number, height and width
// i have used height and width to that of pdf content view
                int pageNumber = 1;
                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(content.getWidth(),
                        content.getHeight() - 20, pageNumber).create();

// create a new page from the PageInfo
                PdfDocument.Page page = document.startPage(pageInfo);

// repaint the user's text into the page
                content.draw(page.getCanvas());

// do final processing of the page
                document.finishPage(page);

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
