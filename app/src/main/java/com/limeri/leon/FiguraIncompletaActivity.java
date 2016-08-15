package com.limeri.leon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.limeri.leon.Models.AdministradorJuegos;
import com.limeri.leon.Models.Juego;
import com.limeri.leon.Models.Navegacion;
import com.limeri.leon.Models.Paciente;

public class FiguraIncompletaActivity extends AppCompatActivity {


    ImageView Figura;
    static final double PORC_PUNTO_CORRECTO_Y = 0.51;
    static final double PORC_PUNTO_CORRECTO_X = 0.55;
    static final double PORC_COTA_Y = 0.072;
    static final double PORC_COTA_X = 0.08;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_figura_incompleta);

        Navegacion.agregarMenuJuego(this);

    }

    private void sumarPuntos(Integer puntos) {
        Juego juego = Paciente.getSelectedPaciente().getEvaluacionActual().getJuegoActual();
        juego.setPuntosJuego(juego.getPuntosJuego() + puntos);
    }

    //TODO: Lógica del JUEGO - Iteración 3
    private void guardar() {
        AdministradorJuegos.getInstance().guardarJuego(this);
    }

}
