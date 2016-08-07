package com.limeri.leon.Models;

import com.limeri.leon.common.EstadosEvaluacion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nico on 06/08/2016.
 */
public class Evaluacion {

    private Paciente paciente;
    private EstadosEvaluacion estado = EstadosEvaluacion.CREADA;
    private List<Juego> juegos = new ArrayList<>();

    public Evaluacion(Paciente paciente) {
        this.paciente = paciente;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public EstadosEvaluacion getEstado() {
        return estado;
    }

    public void setEstado(EstadosEvaluacion estado) {
        this.estado = estado;
    }

    public List<Juego> getJuegos() {
        return juegos;
    }

    public void setJuegos(List<Juego> juegos) {
        this.juegos = juegos;
    }

    public Boolean isFinalizada() {
        return estado == EstadosEvaluacion.FINALIZADA;
    }

    public Juego getUltimoJuego() {
        Juego ultimo = null;
        for (Juego juego : juegos) {
            if (juego.isFinalizado()) {
                ultimo = juego;
            }
        }
        return ultimo;
    }

    public Juego getJuegoActual() {
        for (Juego juego : juegos) {
            if (!juego.isFinalizado()) {
                return juego;
            }
        }
        return null;
    }

    public void agregarJuego(Juego juego) {
        juegos.add(juego);
    }
}
