package com.limeri.leon.Models.Juegos;

import com.limeri.leon.AdivinanzasActivity;
import com.limeri.leon.Models.Juego;

public class Adivinanzas extends Juego {

    private static String nombre = "Adivinanzas";

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public Class getActivityClass() {
        return AdivinanzasActivity.class;
    }

    public static String getClassName() {
        return nombre;
    }
}
