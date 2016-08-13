package com.limeri.leon.Models;

import com.limeri.leon.Models.Juegos.Juego;
import com.limeri.leon.common.Estados;

import java.util.ArrayList;
import java.util.List;

public class Evaluacion {

    private Paciente paciente;
    private Estados estado = Estados.CREADO;

    public List<Juego> getJuegos() {
        return juegos;
    }

    private List<Juego> juegos = new ArrayList<>();

    public Evaluacion(Paciente paciente) {
        this.paciente = null;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Boolean isFinalizada() {
        return estado == Estados.FINALIZADO;
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

    public Boolean tieneJuegos() {
        return !juegos.isEmpty();
    }

    public void finalizar() {
        estado = Estados.FINALIZADO;
    }
}
