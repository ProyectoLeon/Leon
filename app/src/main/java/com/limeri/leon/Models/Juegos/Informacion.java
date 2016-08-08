package com.limeri.leon.Models.Juegos;

import com.limeri.leon.InformacionActivity;
import com.limeri.leon.Models.Juego;

public class Informacion extends Juego {

    private static String nombre = "Informacion";

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public Class getActivityClass() {
        return InformacionActivity.class;
    }

    public static String getClassName() {
        return nombre;
    }
}
