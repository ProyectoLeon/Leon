package com.limeri.leon.Models.Juegos;

import com.limeri.leon.BqSimbolosActivity;

public class BusquedaSimbolos extends Juego {

    private static String nombre = "Busqueda de Simbolos";

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public Class getActivityClass() {
        return BqSimbolosActivity.class;
    }

    public static String getClassName() {
        return nombre;
    }
}
