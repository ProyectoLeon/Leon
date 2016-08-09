package com.limeri.leon.Models;

import android.content.Context;

import com.limeri.leon.Models.Juegos.Juego;
import com.limeri.leon.Models.Juegos.Matrices;
import com.limeri.leon.R;
import com.limeri.leon.common.JSONLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdministradorJuegos {

    private static AdministradorJuegos instance = null;
    private static Context applicationContext = null;

    public static AdministradorJuegos getInstance() {
        if(instance == null) {
            instance = new AdministradorJuegos();
        }
        return instance;
    }

    private List<JuegoWisc> juegosWisc;

    private AdministradorJuegos() {

        juegosWisc = new ArrayList<>();

        String jsonString = JSONLoader.loadJSON(applicationContext.getResources().openRawResource(R.raw.juegos));
        try {
            String packageName = Matrices.class.getPackage().getName() + ".";
            JSONObject jsonRootObject = new JSONObject(jsonString);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.getJSONArray("juegos");

            //Juegos
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonJuego = jsonArray.getJSONObject(i);
                JuegoWisc juego = new JuegoWisc();
                juego.nombre = jsonJuego.getString("nombre");
                juego.clase = Class.forName(packageName + jsonJuego.getString("clase"));
                juego.alternativos = new ArrayList<>();
                JSONArray jsonAlternativos = jsonJuego.getJSONArray("alternativos");
                for (int j = 0; j < jsonAlternativos.length(); j++) {
                    JSONObject jsonAlter = jsonAlternativos.getJSONObject(j);
                    JuegoWisc alter = new JuegoWisc();
                    alter.nombre = jsonAlter.getString("nombre");
                    alter.clase = Class.forName(packageName + jsonAlter.getString("clase"));
                    juego.alternativos.add(alter);
                }
                juegosWisc.add(juego);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Juego getJuegoSiguiente(Juego juego) {
        Juego siguiente = null;
        try {
            Boolean anterior = false;
            for (JuegoWisc juegoWisc : juegosWisc) {
                if (anterior) {
                    siguiente = (Juego) juegoWisc.clase.newInstance();
                } else if (juegoWisc.nombre.equals(juego.getNombre())) {
                    anterior = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return siguiente;
    }

    public Juego getJuegoInicial() {
        Juego juego = null;
        try {
            juego = (Juego) juegosWisc.get(0).clase.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return juego;
    }

    public List<Juego> getJuegosAlternativos(Juego juego) {
        List<Juego> juegosAlt = null;
        try {
            juegosAlt = new ArrayList<>();
            for (JuegoWisc juegoWisc : juegosWisc) {
                if (juegoWisc.nombre.equals(juego.getNombre())) {
                    for (JuegoWisc juegoAlt : juegoWisc.alternativos)
                        juegosAlt.add((Juego) juegoAlt.clase.newInstance());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return juegosAlt;
    }

    public static void setContext(Context context) {
        applicationContext = context;
    }

    class JuegoWisc {
        public String nombre;
        public Class clase;
        public List<JuegoWisc> alternativos;
    }
}
