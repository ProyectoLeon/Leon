package com.limeri.leon.Models;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.limeri.leon.InformacionActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Paciente {

    private static List<Paciente> pacientes = new ArrayList<>();
    private String nombre;
    private String dni;
    private String apellido;
    private String fechaNac;
    private List<Evaluacion> evaluaciones = new ArrayList<>();

    public String getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(String fechaNac) {
        this.fechaNac = fechaNac;
    }

    private static Paciente selectedPaciente;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    private static void agregarPaciente(Paciente paciente) {
        if (!existePaciente(paciente)) {
            pacientes.add(paciente);
        }
    }

    private static boolean existePaciente(Paciente pacienteNuevo) {
        boolean exists = false;
        for (Paciente paciente : pacientes) {
            if (paciente.getNombreCompleto().equals(pacienteNuevo.getNombreCompleto())) {
                exists = true;
                break;
            }
        }
        return exists;
    }

    private static void saveCuentas(Activity activity) {
        Gson gson = new Gson();
        Type listType = new TypeToken<Paciente>() {}.getType();

        SharedPreferences prefs = activity.getSharedPreferences(User.getUserEmail(activity), Context.MODE_PRIVATE);
        Set<String> s = new HashSet<>();
        for (Paciente p : pacientes) {
            s.add(gson.toJson(p,listType));
        }

        SharedPreferences.Editor edit = prefs.edit();
        edit.putStringSet(User.getUserEmail(activity), s);
        edit.apply();
    }

    public static void saveCuenta(Activity activity, Paciente paciente) {
        if (!pacientes.contains(paciente))
            pacientes.add(paciente);
        saveCuentas(activity);
    }

/*
    public static void borrarCuentas(Activity activity) {

        Gson gson = new Gson();
        SharedPreferences prefs = activity.getSharedPreferences("Usuer", Context.MODE_PRIVATE);
        Set<String> s = new HashSet<>(prefs.getStringSet("User", new HashSet<String>()));
        s.clear();

        SharedPreferences.Editor edit = prefs.edit();
        edit.putStringSet("User", s);
        edit.apply();
    }
*/

    public static Paciente getCuentaByName(String cuentaNombre) {
        if (!pacientes.isEmpty()) {
            for (Paciente paciente : pacientes) {
                if (paciente.getNombreCompleto().contains(cuentaNombre)) {
                    return paciente;
                }
            }
        }
        return null;
    }

    public static List<Paciente> getCuentasByName(String cuentaNombre) {
        List<Paciente> returnPacientes = new ArrayList<>();
        if (!pacientes.isEmpty()) {
            for (Paciente paciente : pacientes) {
                if (paciente.getNombre().contains(cuentaNombre) || paciente.getApellido().contains(cuentaNombre)) {
                    returnPacientes.add(paciente);
                }
            }
        }
        return returnPacientes;
    }

    public static Paciente getSelectedPaciente() {
        return selectedPaciente;
    }

    public static void setSelectedPaciente(Paciente paciente) {
        selectedPaciente = paciente;
    }

    public static void loadCuentas(Activity activity) {
        Gson gson = new Gson();
        pacientes = new ArrayList<>();
        SharedPreferences prefs = activity.getSharedPreferences(User.getUserEmail(activity), Context.MODE_PRIVATE);
        Set<String> myStrings = prefs.getStringSet(User.getUserEmail(activity), null);

        if (myStrings != null) {
            for (String paciente : myStrings) {
                Paciente.agregarPaciente(gson.fromJson(paciente, Paciente.class));
            }

        }
    }

    public static List<Paciente> getCuentas() {
        return pacientes;
    }

    public static void borrarCuenta(Activity activity, Paciente paciente) {
        if (!pacientes.isEmpty()) {
            for (Paciente p : pacientes) {
                if (p.getNombreCompleto().equals(paciente.getNombreCompleto())) {
                    pacientes.remove(p);
                    break;
                }
            }
        }
        saveCuentas(activity);
    }

    public static void borrarSelectedPaciente() {
        selectedPaciente = null;
    }

    public Evaluacion getEvaluacionFinalizada(){
       Evaluacion evaluacionFinalizada = null;
        for(Evaluacion eval : evaluaciones) {
            if (eval.isFinalizada()){
                evaluacionFinalizada = eval;
            }
        }
        return evaluacionFinalizada;
    }

    public Evaluacion getEvaluacionActual() {
        Evaluacion evaluacionActual = null;
        for (Evaluacion eval : evaluaciones) {
            if (!eval.isFinalizada())
                evaluacionActual = eval;
        }
        return evaluacionActual;
    }

    public void agregarEvaluacion(Evaluacion evaluacion) {
        evaluaciones.add(evaluacion);
    }

    public Boolean tieneEvaluacionIniciada() {
        for (Evaluacion eval : evaluaciones) {
            if (!eval.isFinalizada())
                return true;
        }
        return false;
    }

    public Boolean tieneEvaluacionFinalizada() {
        for (Evaluacion eval : evaluaciones) {
            if (eval.isFinalizada())
                return true;
        }
        return false;
    }

    public int cantidadAños(int añoActual){
        String año = fechaNac.substring(6,10);
        int diff = añoActual - Integer.parseInt(año);
        return diff;
    }

    public void borrarEvaluaciones() {
        evaluaciones.clear();
    }
}