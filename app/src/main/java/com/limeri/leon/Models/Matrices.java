package com.limeri.leon.Models;

/**
 * Created by Nico on 06/08/2016.
 */
public class Matrices implements Juego{

    private static String nombre = "Matrices";
    private Integer puntos = 0;
    private Boolean finalizado = false;

    public String getNombre() {
        return nombre;
    }

    public static String getClassName() {
        return nombre;
    }

    public Integer getPuntos() {
        return puntos;
    }

    public void setPuntos(Integer puntos) {
        this.puntos = puntos;
    }

    public Boolean isFinalizado() {
        return finalizado;
    }

    @Override
    public void finalizar() {
        finalizado = true;
    }

    public void setFinalizado(Boolean finalizado) {
        this.finalizado = finalizado;
    }
}
