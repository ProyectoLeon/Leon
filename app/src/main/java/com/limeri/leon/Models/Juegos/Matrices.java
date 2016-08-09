package com.limeri.leon.Models.Juegos;

import com.limeri.leon.MatricesActivity;

public class Matrices extends Juego {

    private static String nombre = "Matrices";

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public Class getActivityClass() {
        return MatricesActivity.class;
    }

    public static String getClassName() {
        return nombre;
    }
}
