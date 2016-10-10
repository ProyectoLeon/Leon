package com.limeri.leon.Models;

import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.limeri.leon.common.Estados;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Evaluacion {

    private String fechaEvaluación;
    private Paciente paciente;
    private Estados estado = Estados.CREADO;
    private Integer puntosCompVerbal;
    private Integer puntosRazPercep;
    private Integer puntosMemOper;
    private Integer puntosVelocProc;
    private Integer coeficienteIntelectual;

    public List<Juego> getJuegos() {
        return juegos;
    }
    public List<Juego> getJuegosLibres() {
        return juegosLibres;
    }

    private List<Juego> juegos = new ArrayList<>();
    private List<Juego> juegosLibres = new ArrayList<>();

    private List<String> alternativas = new ArrayList<>();

    public List<String> getCategoriasDebiles() {
        return categoriasDebiles;
    }

    public void setCategoriasDebiles(List<String> categoriasDebiles) {
        this.categoriasDebiles = categoriasDebiles;
    }

    private List<String> categoriasDebiles = new ArrayList<>();

    public Paciente getPaciente() {
        return paciente;
    }

    public List<String> getAlternativas() {
        return alternativas;
    }

    public void addAlternativa(String categoria) {
        alternativas.add(categoria);
    }

    public void removeAlternativa(String categoria) {
        alternativas.remove(categoria);
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
            if (juego.isFinalizado() || juego.isCancelado()) {
                ultimo = juego;
            }
        }
        return ultimo;
    }

    public Juego getUltimoJuegoLibre() {
        Juego ultimo = null;
        for (Juego juego : juegosLibres) {
            if (juego.isFinalizado() || juego.isCancelado()) {
                ultimo = juego;
            }
        }
        return ultimo;
    }
    public String getFechaEvaluación() {
        return fechaEvaluación;
    }

    public void setFechaEvaluación(String fechaEvaluación) {
        this.fechaEvaluación = fechaEvaluación;
    }

    public Juego getJuegoActual() {
        for (Juego juego : juegos) {
            if (!juego.isFinalizado() && !juego.isCancelado()) {
                return juego;
            }
        }
        return null;
    }
    public Juego getJuegoLibreActual() {
        for (Juego juego : juegosLibres) {
            if (!juego.isFinalizado() && !juego.isCancelado()) {
                return juego;
            }
        }
        return null;
    }

    public void agregarJuego(Juego juego) {
        juegos.add(juego);
    }
    public void agregarJuegoLibre(Juego juego) {
        juegosLibres.add(juego);
    }

    public Boolean tieneJuegos() {
        return !juegos.isEmpty();
    }

    public void finalizar() {
        estado = Estados.FINALIZADO;
    }

    public Integer getPuntosCompVerbal() {
        return puntosCompVerbal;
    }

    public void setPuntosCompVerbal(Integer puntosCompVerbal) {
        this.puntosCompVerbal = puntosCompVerbal;
    }

    public Integer getPuntosRazPercep() {
        return puntosRazPercep;
    }

    public void setPuntosRazPercep(Integer puntosRazPercep) {
        this.puntosRazPercep = puntosRazPercep;
    }

    public Integer getPuntosMemOper() {
        return puntosMemOper;
    }

    public void setPuntosMemOper(Integer puntosMemOper) {
        this.puntosMemOper = puntosMemOper;
    }

    public Integer getPuntosVelocProc() {
        return puntosVelocProc;
    }

    public void setPuntosVelocProc(Integer puntosVelocProc) {
        this.puntosVelocProc = puntosVelocProc;
    }

    public Integer getCoeficienteIntelectual() {
        return coeficienteIntelectual;
    }

    public void setCoeficienteIntelectual(Integer coeficienteIntelectual) {
        this.coeficienteIntelectual = coeficienteIntelectual;
    }

    public Estados getEstado(){
        return estado;
    }


}
