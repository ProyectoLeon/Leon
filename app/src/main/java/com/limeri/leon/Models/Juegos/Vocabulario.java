package com.limeri.leon.Models.Juegos;

import com.limeri.leon.Models.Juego;
import com.limeri.leon.VocabularioActivity;

import java.util.Map;

public class Vocabulario extends Juego {

    private static String nombre = "Vocabulario";

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public Class getActivityClass() {
        return VocabularioActivity.class;
    }

    public static String getClassName() {
        return nombre;
    }
}
