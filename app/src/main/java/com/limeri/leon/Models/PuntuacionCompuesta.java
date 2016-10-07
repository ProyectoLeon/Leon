package com.limeri.leon.Models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.limeri.leon.common.DataBase;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PuntuacionCompuesta {

    private String equivalencia;
    private String nivelConfianza;
    private String percentil;


    public String getEquivalencia() { return equivalencia; }

    public void setEquivalencia(String equivalencia) {
        this.equivalencia = equivalencia;
    }

    public String getNivelConfianza() {
        return nivelConfianza;
    }

    public void setNivelConfianza(String nivelConfianza) { this.nivelConfianza = nivelConfianza; }

    public String getpercentil() { return percentil; }

    public void setPercentil(String percentil) { this.percentil = percentil; }


    public static PuntuacionCompuesta getPuntuacionCompuesta(String nombre) {
        String strPuntCompuesta = DataBase.getpuntuacionCompuesta(nombre);

        PuntuacionCompuesta puntuacionCompuesta = null;

        if (!strPuntCompuesta.equals("")){
            try{
                JSONObject jsonPuntCompuesta = new JSONObject(strPuntCompuesta);
                puntuacionCompuesta = new PuntuacionCompuesta();

                puntuacionCompuesta.setEquivalencia("21");
                puntuacionCompuesta.setNivelConfianza("31-51");
                puntuacionCompuesta.setPercentil("41");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return puntuacionCompuesta;
    }
}
