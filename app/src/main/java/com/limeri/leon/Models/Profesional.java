package com.limeri.leon.Models;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.limeri.leon.LoginActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by MIPc on 8/6/2016.
 */
public class Profesional {

    private String mNombre;
    private String mCorreo;
    private String mPassword;
    private String mMatricula;
    private String mProducto;
    private boolean mRegistrado;

    private static Profesional mSelectedProfesional;

    public String getmCorreo() {
        return mCorreo;
    }

    public void setmCorreo(String mCorreo) {
        this.mCorreo = mCorreo;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public String getmMatricula() {
        return mMatricula;
    }

    public void setmMatricula(String mMatricula) {
        this.mMatricula = mMatricula;
    }

    public String getNombre() {
        return mNombre;
    }

    public void setNombre(String mUsuario) {
        this.mNombre = mUsuario;
    }

    public String getProducto() {
        return mProducto;
    }

    public void setProducto(String mProducto) {
        this.mProducto = mProducto;
    }

    public boolean isRegistrado() {
        return mRegistrado;
    }

    public void setRegistrado(boolean mRegistrado) {
        this.mRegistrado = mRegistrado;
    }

    public static Profesional getProfesional() {
        return mSelectedProfesional;
    }

    public static Profesional getSavedProfesional(Activity activity, String id) {

        Gson gson = new Gson();

        SharedPreferences prefs = activity.getSharedPreferences("Profesional", Context.MODE_PRIVATE);
        Set<String> s = new HashSet<>(prefs.getStringSet("Profesional", new HashSet<String>()));

        if (s != null) {

            //   for (int i = 0; i < myStrings.size(); i++) {
            Iterator iter = s.iterator();

            while (iter.hasNext()) {

                Profesional profesionalGuardado = (gson.fromJson(iter.next().toString(), Profesional.class));

                if (profesionalGuardado.getmMatricula() != null && profesionalGuardado.getmMatricula().equals(id)) {

                   return profesionalGuardado;


                } else {

                }

            }


        }


        return null;
    }



    public static void setProfesional (Profesional profesional) {
        mSelectedProfesional = profesional;
    }
    /**
        public static void saveCuentas(Activity activity) {
            SharedPreferences prefs = activity.getSharedPreferences("User", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = prefs.edit();
            Gson gson = new Gson();
            String cuentas = gson.toJson(mPacientes);
            edit.putString("User", cuentas);
            edit.commit();
            edit.apply();

        }
    */
        public static void saveProfesional(Activity activity, Profesional profesional) {

            Gson gson = new Gson();
            String cuentas = gson.toJson(profesional);

            SharedPreferences prefs = activity.getSharedPreferences("Profesional", Context.MODE_PRIVATE);

            Set<String> s = new HashSet<>(prefs.getStringSet("Profesional", new HashSet<String>()));

            s.add(cuentas);

            SharedPreferences.Editor edit = prefs.edit();


            edit.putStringSet("Profesional", s);
            edit.apply();
            edit.commit();
        }

    /**
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



        public static void loadCuentas(Activity activity) {
            SharedPreferences prefs = activity.getSharedPreferences("User", Context.MODE_PRIVATE);
            Set<String> myStrings = prefs.getStringSet("User", null);

            Gson gson = new Gson();

            if (myStrings != null) {

                //   for (int i = 0; i < myStrings.size(); i++) {
                Iterator iter = myStrings.iterator();

                while (iter.hasNext()) {

                    Paciente.agregarPaciente(gson.fromJson(iter.next().toString(), Paciente.class));

                }


            }


        }

*/
    public static void borrarCuentaBase(Activity activity, Profesional profesional) {


        SharedPreferences prefs = activity.getSharedPreferences("Profesional", Context.MODE_PRIVATE);
        Set<String> s = new HashSet<>(prefs.getStringSet("Profesional", new HashSet<String>()));

        Gson gson = new Gson();

        if (s != null) {

            //   for (int i = 0; i < myStrings.size(); i++) {
            Iterator iter = s.iterator();

            while (iter.hasNext()) {

                Profesional profesionalGuardado = (gson.fromJson(iter.next().toString(), Profesional.class));

                if (profesionalGuardado.getmMatricula() != null && profesionalGuardado.getmMatricula().equals(profesional.getmMatricula())) {

                    iter.remove();
                    break;

                } else {

                }

            }


        }

        SharedPreferences.Editor edit = prefs.edit();

        edit.clear();
        edit.apply();
        edit.commit();
        edit.putStringSet("Profesional", s);
        edit.apply();
        edit.commit();


    }

    public static boolean existeMatricula(Activity activity, String matricula) {
        SharedPreferences prefs = activity.getSharedPreferences("Profesional", Context.MODE_PRIVATE);
        Set<String> s = new HashSet<>(prefs.getStringSet("Profesional", new HashSet<String>()));

        Gson gson = new Gson();

        boolean existe = false;
        for (String prof : s) {
            Profesional profesional = gson.fromJson(prof, Profesional.class);
            if (profesional.getmMatricula().equals(matricula)) {
                existe = true;
                break;
            }
        }
        return existe;
    }

    public static boolean isMatriculaRegistrada(Activity activity, String matricula) {
        SharedPreferences prefs = activity.getSharedPreferences("Profesional", Context.MODE_PRIVATE);
        Set<String> s = new HashSet<>(prefs.getStringSet("Profesional", new HashSet<String>()));

        Gson gson = new Gson();

        boolean registrada = false;
        for (String prof : s) {
            Profesional profesional = gson.fromJson(prof, Profesional.class);
            if (profesional.getmMatricula().equals(matricula) && profesional.isRegistrado()) {
                registrada = true;
                break;
            }
        }
        return registrada;
    }

    public static String getProductoMatricula(Activity activity, String matricula) {
        SharedPreferences prefs = activity.getSharedPreferences("Profesional", Context.MODE_PRIVATE);
        Set<String> s = new HashSet<>(prefs.getStringSet("Profesional", new HashSet<String>()));

        Gson gson = new Gson();

        String producto = "";
        for (String prof : s) {
            Profesional profesional = gson.fromJson(prof, Profesional.class);
            if (profesional.getmMatricula().equals(matricula)) {
                producto = profesional.getProducto();
                break;
            }
        }
        return producto;
    }

    public static List<Profesional> getProfesionales(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences("Profesional", Context.MODE_PRIVATE);
        Set<String> s = new HashSet<>(prefs.getStringSet("Profesional", new HashSet<String>()));

        Gson gson = new Gson();

        List<Profesional> profs = new ArrayList<>();
        for (String prof : s) {
            Profesional profesional = gson.fromJson(prof, Profesional.class);
            profs.add(profesional);
        }
        return profs;
    }
    /**
        public static void borrarCuenta(Paciente paciente) {

            if (mPacientes != null) {
                for (int i = 0; i < mPacientes.size(); i++) {

                    if (paciente.getDni().equals(mPacientes.get(i).getDni())) {
                        mPacientes.remove(i);

                    }
                }


            }
        }

        public static void borrarSelectedPaciente() {

            mSelectedPaciente = null;

        }
    }
 **/
}
