package com.limeri.leon.Models;

import com.limeri.leon.common.Estados;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Juego {

    private String nombre;
    private String categoria;
    private Integer puntos = 0;
    private Estados estado = Estados.CREADO;
    private transient Map<Integer, Integer> puntosNiveles;
    private String nombreActividad;
    private Boolean alternativo;
    private Boolean juegaPaciente;
    private double media;
    private double valorCritico;
    private List<List<Integer>> puntajesEquivalentes;

    public Juego(String nombre, String categoria, String activity, List<List<Integer>> puntajesEquivalentes, Boolean alternativo, Double media, Double valorCritico, Boolean juegaPaciente) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.nombreActividad = activity;
        this.puntajesEquivalentes = puntajesEquivalentes;
        this.alternativo = alternativo;
        this.juegaPaciente = juegaPaciente;
        this.media = media;
        this.valorCritico = valorCritico;
        this.puntosNiveles = new HashMap<>();
    }

    public Juego(Juego juegoBase) {
        this.nombre = juegoBase.getNombre();
        this.categoria = juegoBase.getCategoria();
        this.nombreActividad = juegoBase.getNombreActividad();
        this.puntajesEquivalentes = juegoBase.getPuntajesEquivalentes();
        this.alternativo = juegoBase.getAlternativo();
        this.juegaPaciente = juegoBase.getJuegaPaciente();
        this.media = juegoBase.getMedia();
        this.valorCritico = juegoBase.getValorCritico();
        this.puntosNiveles = juegoBase.getPuntosNiveles();
        this.puntos = juegoBase.getPuntosJuego();
    }

    public String getNombreActividad(){
        return nombreActividad;
    }

    public String getNombre() {
        return nombre;
    }

    public Integer getPuntosJuego() {
        return puntos;
    }

    public void setPuntosJuego(Integer puntos) {
        this.puntos = puntos;
    }

    public Boolean getAlternativo() {
        return alternativo;
    }

    public Boolean getJuegaPaciente() {
        return juegaPaciente;
    }

    public void setPuntosNiveles(Map<Integer, Integer> puntos) {
        this.puntosNiveles = puntos;
    }

    public Boolean isFinalizado() {
        return estado == Estados.FINALIZADO;
    }

    public void finalizar() {
        estado = Estados.FINALIZADO;
    }

    public String getCategoria() {
        return categoria;
    }

    public Boolean isCancelado() {
        return estado == Estados.CANCELADO;
    }

    public void cancelar() {
        estado = Estados.CANCELADO;
    }

    public Integer getPuntajeEscalar () {

        Integer puntajeEquivalente = 0;

        for (List<Integer> puntajeDirecto : puntajesEquivalentes){
            if (puntajeDirecto.contains(puntos)){
                puntajeEquivalente = puntajesEquivalentes.indexOf(puntajeDirecto) + 1;
                break;
            }
        }

        return puntajeEquivalente;
    }

    public void guardarPuntosNivel(Integer nivel, Integer puntos) {
        puntosNiveles.put(nivel,puntos);
    }

    public Integer getPuntosNivel(Integer nivel) {
        return puntosNiveles.get(nivel);
    }

    public double getMedia() {
        return media;
    }

    public double getValorCritico() {
        return valorCritico;
    }

    public List<List<Integer>> getPuntajesEquivalentes() {
        return puntajesEquivalentes;
    }

    public Map<Integer,Integer> getPuntosNiveles() {
        return puntosNiveles;
    }
}
