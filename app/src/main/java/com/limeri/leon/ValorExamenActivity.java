package com.limeri.leon;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.limeri.leon.Models.AdministradorJuegos;
import com.limeri.leon.Models.Evaluacion;
import com.limeri.leon.Models.Juego;
import com.limeri.leon.Models.Navegacion;
import com.limeri.leon.Models.Paciente;

import java.util.ArrayList;
import java.util.List;

public class ValorExamenActivity extends AppCompatActivity {

    private Integer position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int coefIntelectual = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valor_examen);

        Paciente paciente = Paciente.getSelectedPaciente();
        position = getIntent().getExtras().getInt("position");
        Evaluacion evaluacion = paciente.getEvaluacion(position);
        TextView puntajeTotal = (TextView) findViewById(R.id.puntaje);
        ListView listado = (ListView) findViewById(R.id.listadoP);
        List<Juego> listaJuegos = evaluacion.getJuegos();
        List<String> llistadoPuntos = new ArrayList<>();
        for (Juego juego : listaJuegos) {
            String puntaje = juego.getNombre() + ": " + juego.getPuntosJuego().toString();
            llistadoPuntos.add(puntaje);
            coefIntelectual = coefIntelectual + juego.getPuntosJuego();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, llistadoPuntos);
        listado.setAdapter(adapter);
        try {

            //AdministradorJuegos.getInstance().calcularPuntaje(evaluacion);
            //  puntaje.setText("Puntaje:  "+evaluacion.getCoeficienteIntelectual());
            puntajeTotal.setText("Coeficiente Intelectual: " + evaluacion.getCoeficienteIntelectual());
            puntajeTotal.setTextSize(30);
            puntajeTotal.setTextColor(Color.DKGRAY);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Button siguiente = (Button) findViewById(R.id.buttonSig);
        if (siguiente != null) {
            siguiente.setOnClickListener(clickSiguiente());
        }
    }


    private View.OnClickListener clickSiguiente() {

        return new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Navegacion.irA(ValorExamenActivity.this, PerfilEscalaresActivity.class,position);
            }
        };
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Navegacion.irA(this, MainActivity.class);
    }
}
