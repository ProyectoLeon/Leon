package com.limeri.leon.Models;

/**
 * Created by Nico on 06/08/2016.
 */
public class BusquedaSimbolos implements Juego {

    private static String nombre = "Busqueda de Simbolos";
    private Integer puntos = 0;
    private Boolean finalizado = false;

    public String getNombre() {
        return nombre;
    }

    @Override
    public Integer getPuntos() {
        return puntos;
    }

    @Override
    public void setPuntos(Integer puntos) {
        this.puntos = puntos;
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
