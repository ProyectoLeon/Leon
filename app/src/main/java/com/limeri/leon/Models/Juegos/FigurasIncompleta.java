package com.limeri.leon.Models.Juegos;

import com.limeri.leon.FiguraIncompletaActivity;


public class FigurasIncompleta extends Juego {

    private static String nombre = "Figuras Incompleta";

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public Class getActivityClass() {
        return FiguraIncompletaActivity.class;
    }

    public static String getClassName() {
        return nombre;
    }
}
