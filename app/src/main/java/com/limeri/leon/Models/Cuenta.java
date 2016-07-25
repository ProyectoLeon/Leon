package com.limeri.leon.Models;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by MIPc on 5/27/2016.
 */
public class Cuenta {

    private static ArrayList<Cuenta> mCuentas = new ArrayList<Cuenta>();
    private String mUsuario;
    private String mProvider;
    private String mContraseña;

    public String getUsuario() {
        return mUsuario;
    }


    public void setUsuario(String mUsuario) {
        this.mUsuario = mUsuario;
    }

    public String getmProvider() {
        return mProvider;
    }

    public void setProvider(String mProvider) {
        this.mProvider = mProvider;
    }

    public String getContraseña() {
        return mContraseña;
    }

    public void setContraseña(String mContraseña) {
        this.mContraseña = mContraseña;
    }

    public static void add(Cuenta cuenta) {

        boolean exists = false;
        for (int i = 0; i < mCuentas.size(); i++) {
            if (mCuentas.get(i).equals(cuenta)) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            mCuentas.add(cuenta);
        }
    }

    public static void saveCuentas(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        Gson gson = new Gson();
        String cuentas = gson.toJson(mCuentas);
        edit.putString("User", cuentas);
        edit.commit();
        edit.apply();

    }

    public static void saveCuenta(Activity activity, Cuenta cuenta) {

        Gson gson = new Gson();
        String cuentas = gson.toJson(cuenta);

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

        SharedPreferences prefs = activity.getSharedPreferences(User.getUserEmail(activity), Context.MODE_PRIVATE);

        Set<String> s = new HashSet<String>(prefs.getStringSet(User.getUserEmail(activity), new HashSet<String>()));

        s.clear();

        for (int i = 0; i < mCuentas.size(); i++) {

            String cuentaNueva = gson.toJson(mCuentas.get(i));
            s.add(cuentaNueva);

        }

    SharedPreferences.Editor edit = prefs.edit();


        edit.putStringSet(User.getUserEmail(activity), s);
        edit.apply();
        edit.commit();
    }


    public static Cuenta getCuentaByName(String cuentaNombre) {

        if (mCuentas != null) {
            for (int i = 0; i < mCuentas.size(); i++) {

                if (mCuentas.get(i).getUsuario().contains(cuentaNombre)) {
                    return mCuentas.get(i);
                }

            }
        }
        return null;
    }

    public static ArrayList<Cuenta> getCuentasByName(String cuentaNombre) {

        ArrayList<Cuenta> returnCuentas = new ArrayList<Cuenta>();

        if (mCuentas != null) {
            for (int i = 0; i < mCuentas.size(); i++) {

                if (mCuentas.get(i).getUsuario().contains(cuentaNombre) || mCuentas.get(i).getContraseña().contains(cuentaNombre) ) {
                    returnCuentas.add(mCuentas.get(i));
                }

            }
        }
        return returnCuentas;
    }

    public static void loadCuentas(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences("User", Context.MODE_PRIVATE);
        Set<String> myStrings = prefs.getStringSet("User", null);

        Gson gson = new Gson();

        if (myStrings != null) {

            //   for (int i = 0; i < myStrings.size(); i++) {
            Iterator iter = myStrings.iterator();

            while (iter.hasNext()) {

                Cuenta.add(gson.fromJson(iter.next().toString(), Cuenta.class));

            }


        }


    }

    public static ArrayList<Cuenta> getCuentas() {

        return mCuentas;
    }

    public static void borrarCuenta(Cuenta cuenta) {

        if (mCuentas != null) {
            for (int i = 0; i < mCuentas.size(); i++) {

                if (cuenta.getUsuario().equals(mCuentas.get(i).getUsuario())) {
                    mCuentas.remove(i);

                }
            }


        }
    }
}