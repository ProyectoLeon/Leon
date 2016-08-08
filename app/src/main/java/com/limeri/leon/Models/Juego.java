package com.limeri.leon.Models;

import java.util.Map;

/**
 * Created by Nico on 06/08/2016.
 */
public abstract class Juego {

    private Integer puntos = 0;
    private Boolean finalizado = false;
    private Map<Integer, Integer> puntosNiveles;

    public static String getClassName() {
        return "Juego";
    }

    public abstract Class getActivityClass();

    public abstract String getNombre();

    public Integer getPuntosJuego() {
        return puntos;
    }

    public void setPuntosJuego(Integer puntos) {
        this.puntos = puntos;
    }

    public void setPuntosNiveles(Map<Integer, Integer> puntos) {
        this.puntosNiveles = puntos;
    }

    public Boolean isFinalizado() {
        return finalizado;
    }

    public void finalizar() {
        finalizado = true;
    }

}
