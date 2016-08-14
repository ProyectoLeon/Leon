package com.limeri.leon.Models;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by MIPc on 5/27/2016.
 */
public class Paciente {

    private static ArrayList<Paciente> mPacientes = new ArrayList<Paciente>();
    private String mNombre;
    private String mDNI;
    private String mApellido;
    private List<Evaluacion> evaluaciones = new ArrayList<Evaluacion>();

    public String getmFechaNac() {
        return mFechaNac;
    }

    public void setmFechaNac(String mFechaNac) {
        this.mFechaNac = mFechaNac;
    }

    private String mFechaNac;

    private static Paciente mSelectedPaciente;

    public String getNombre() {
        return mNombre;
    }

    public void setNombre(String mUsuario) {
        this.mNombre = mUsuario;
    }

    public String getmDNI() {
        return mDNI;
    }

    public void setmDNI(String mDNI) {
        this.mDNI = mDNI;
    }

    public String getApellido() {
        return mApellido;
    }

    public void setApellido(String mContraseña) {
        this.mApellido = mContraseña;
    }

    public String getNombreCompleto() {
        return mNombre + " " + mApellido;
    }

    public static void add(Paciente paciente) {

        boolean exists = false;
        for (int i = 0; i < mPacientes.size(); i++) {

            String nombreapellido = mPacientes.get(i).getNombre() + " " + mPacientes.get(i).getApellido();

            String nombreapellidoCuentaNueva = paciente.getNombre() + " " + paciente.getApellido();
            if (nombreapellido.equals(nombreapellidoCuentaNueva)) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            mPacientes.add(paciente);
        }
    }



    public static void saveCuentas(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        Gson gson = new Gson();
        String cuentas = gson.toJson(mPacientes);
        edit.putString("User", cuentas);
        edit.commit();
        edit.apply();

    }

    public static void saveCuenta(Activity activity, Paciente paciente) {

        Gson gson = new Gson();
        Type listType = new TypeToken<Paciente>() {}.getType();
        String cuentas = gson.toJson(paciente, listType);

        SharedPreferences prefs = activity.getSharedPreferences("User", Context.MODE_PRIVATE);

        Set<String> s = new HashSet<String>(prefs.getStringSet("User", new HashSet<String>()));

        s.add(cuentas);

        SharedPreferences.Editor edit = prefs.edit();


        edit.putStringSet("User", s);
        edit.apply();
        edit.commit();
    }

    public static void borrarCuentas(Activity activity) {

        Gson gson = new Gson();
        // String cuentas = gson.toJson(cuenta);

        SharedPreferences prefs = activity.getSharedPreferences("Usuer", Context.MODE_PRIVATE);

        Set<String> s = new HashSet<String>(prefs.getStringSet("User", new HashSet<String>()));

        s.clear();

        SharedPreferences.Editor edit = prefs.edit();


        edit.putStringSet("User", s);
        edit.apply();
        edit.commit();
    }


    public static Paciente getCuentaByName(String cuentaNombre) {

        if (mPacientes != null) {
            for (int i = 0; i < mPacientes.size(); i++) {

                String nombreapellido = new String(mPacientes.get(i).getNombre() + " " + mPacientes.get(i).getApellido());

                if (nombreapellido.contains(cuentaNombre)) {
                    return mPacientes.get(i);
                }

            }
        }
        return null;
    }

    public static ArrayList<Paciente> getCuentasByName(String cuentaNombre) {

        ArrayList<Paciente> returnPacientes = new ArrayList<Paciente>();

        if (mPacientes != null) {
            for (int i = 0; i < mPacientes.size(); i++) {

                if (mPacientes.get(i).getNombre().contains(cuentaNombre) || mPacientes.get(i).getApellido().contains(cuentaNombre)) {
                    returnPacientes.add(mPacientes.get(i));
                }

            }
        }
        return returnPacientes;
    }

    public static Paciente getmSelectedPaciente() {
        return mSelectedPaciente;
    }

    public static void setmSelectedPaciente (Paciente paciente) {
        mSelectedPaciente = paciente;
    }

    public static void loadCuentas(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences("User", Context.MODE_PRIVATE);
        Set<String> myStrings = prefs.getStringSet("User", null);

        Gson gson = new Gson();

        if (myStrings != null) {

            //   for (int i = 0; i < myStrings.size(); i++) {
            Iterator iter = myStrings.iterator();

            while (iter.hasNext()) {

                Paciente.add(gson.fromJson(iter.next().toString(), Paciente.class));

            }


        }


    }

    public static ArrayList<Paciente> getCuentas() {

        return mPacientes;
    }

    public static void borrarCuentaBase(Activity activity, Paciente paciente) {


        SharedPreferences prefs = activity.getSharedPreferences("User", Context.MODE_PRIVATE);
        Set<String> s = new HashSet<String>(prefs.getStringSet("User", new HashSet<String>()));

        Gson gson = new Gson();

        if (s != null) {

            //   for (int i = 0; i < myStrings.size(); i++) {
            Iterator iter = s.iterator();

            while (iter.hasNext()) {

                Paciente pacienteGuardado = (gson.fromJson(iter.next().toString(), Paciente.class));

                if (pacienteGuardado.getmDNI().equals(paciente.getmDNI())) {

                  iter.remove();


                } else {

                }

            }


        }

        SharedPreferences.Editor edit = prefs.edit();

        edit.clear();
        edit.apply();
        edit.commit();
        edit.putStringSet("User", s);
        edit.apply();
        edit.commit();


    }

    public static void borrarCuenta(Paciente paciente) {

        if (mPacientes != null) {
            for (int i = 0; i < mPacientes.size(); i++) {

                if (paciente.getmDNI().equals(mPacientes.get(i).getmDNI())) {
                    mPacientes.remove(i);

                }
            }


        }
    }

    public static void borrarSelectedPaciente() {

        mSelectedPaciente = null;

    }

    public List<Evaluacion> getEvaluaciones() {
        return evaluaciones;
    }

    public void setEvaluaciones(List<Evaluacion> evaluaciones) {
        this.evaluaciones = evaluaciones;
    }

    public Evaluacion getEvaluacionFinalizada(){
       Evaluacion ultimaEval = null;
        for(Evaluacion eval : evaluaciones) {
            if (eval.isFinalizada()){
                ultimaEval = eval;
            }
        }
        return ultimaEval;
    }

    public Evaluacion getEvaluacionActual() {
        for (Evaluacion eval : evaluaciones) {
            if (!eval.isFinalizada())
                return eval;
        }
        return null;
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
}