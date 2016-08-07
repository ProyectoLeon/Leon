package com.limeri.leon.Models;

/**
 * Created by Nico on 06/08/2016.
 */
public interface Juego {

    public String getNombre();
    public Integer getPuntos();
    public void setPuntos(Integer puntos);
    public Boolean isFinalizado();
    public void finalizar();
}
