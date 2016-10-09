package com.limeri.leon.Models;

import com.limeri.leon.common.DataBase;

import java.util.ArrayList;
import java.util.List;

public class Paciente {

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

    public List<Evaluacion> getEvaluaciones() {
        return evaluaciones;
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
        return añoActual - Integer.parseInt(año);
    }

/*
    private static boolean existePaciente(Paciente pacienteNuevo) {
        boolean exists = false;
        for (Paciente paciente : getCuentas()) {
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
        for (Paciente p : getCuentas()) {
            s.add(gson.toJson(p,listType));
        }

        SharedPreferences.Editor edit = prefs.edit();
        edit.putStringSet(User.getUserEmail(activity), s);
        edit.apply();
    }
*/

    public static void saveCuenta(Paciente paciente) {
        if (!getCuentas().contains(paciente))
            getCuentas().add(paciente);
//        saveCuentas(activity);
        DataBase.savePacientes();
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
        if (!getCuentas().isEmpty()) {
            for (Paciente paciente : getCuentas()) {
                if (paciente.getNombreCompleto().contains(cuentaNombre)) {
                    return paciente;
                }
            }
        }
        return null;
    }

    public static List<Paciente> getCuentasByName(String cuentaNombre) {
        List<Paciente> returnPacientes = new ArrayList<>();
        if (!getCuentas().isEmpty()) {
            for (Paciente paciente : getCuentas()) {
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

    public static List<Paciente> getCuentas() {
        return Profesional.getProfesionalActual().getPacientes();
    }

    public static void borrarCuenta(Paciente paciente) {
        if (!getCuentas().isEmpty()) {
            for (Paciente p : getCuentas()) {
                if (p.getNombreCompleto().equals(paciente.getNombreCompleto())) {
                    getCuentas().remove(p);
                    break;
                }
            }
        }
//        saveCuentas(activity);
        DataBase.savePacientes();
    }

    public static void borrarSelectedPaciente() {
        selectedPaciente = null;
    }

    public void borrarEvaluacionActual() {
        Evaluacion evaluacion = getEvaluacionActual();
        evaluaciones.remove(evaluacion);
    }
}