package com.limeri.leon;

import android.app.Activity;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.limeri.leon.Models.Navegacion;
import com.limeri.leon.Models.Paciente;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Navegacion.irA(this, MainActivity.class);
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

    private View.OnClickListener clickSiguiente() {

        return new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Navegacion.irA(ResumenActivity.this, PuntuacionDirectaActivity.class);
            }
        };
    }
}
