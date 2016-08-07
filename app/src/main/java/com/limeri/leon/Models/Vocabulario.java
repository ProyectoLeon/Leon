package com.limeri.leon.Models;

/**
 * Created by Nico on 06/08/2016.
 */
public class Vocabulario implements Juego {

    private static String nombre = "Vocabulario";
    private Boolean finalizado = false;
    private Integer puntos = 0;

    public String getNombre() {
        return nombre;
    }

    @Override
    public Integer getPuntos() {
        return puntos;
    }

    @Override
    public void setPuntos(Integer puntos) {

    }

    @Override
    public Boolean isFinalizado() {
        return finalizado;
    }

    @Override
    public void finalizar() {
        finalizado = true;
    }

    public static String getClassName() {
        return nombre;
    }
}
