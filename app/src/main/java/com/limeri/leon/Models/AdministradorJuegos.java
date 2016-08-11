package com.limeri.leon.Models;

import android.content.Context;

import com.limeri.leon.Models.Juegos.Juego;
import com.limeri.leon.R;
import com.limeri.leon.common.JSONLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class AdministradorJuegos {

    private static AdministradorJuegos instance = null;
    private static Context applicationContext = null;

    public static AdministradorJuegos getInstance() {
        if(instance == null) {
            instance = new AdministradorJuegos();
        }
        return instance;
    }

    private Map<String, List<JuegoWisc>> juegosWisc;
    private List<String> categorias;

    private AdministradorJuegos() {

        juegosWisc = new TreeMap<>();
        categorias = new ArrayList<>();

        String jsonString = JSONLoader.loadJSON(applicationContext.getResources().openRawResource(R.raw.protocolo));
        try {
            JSONObject jsonRootObject = new JSONObject(jsonString);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.getJSONArray("juegos");

            //Juegos
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonJuego = jsonArray.getJSONObject(i);
                JuegoWisc juego = new JuegoWisc();
                juego.nombre = jsonJuego.getString("nombre");
                juego.clase = jsonJuego.getString("clase");
                juego.categoria = jsonJuego.getString("categoria");
                juego.activity = jsonJuego.getString("activity");
                juego.alternativo = jsonJuego.getBoolean("alternativo");
                if (!juegosWisc.containsKey(juego.categoria)){
                    juegosWisc.put(juego.categoria, new ArrayList<JuegoWisc>());
                }
                juegosWisc.get(juego.categoria).add(juego);
                if (!categorias.contains(juego.categoria)) {
                    categorias.add(juego.categoria);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Juego getJuegoSiguiente(Juego juego) {
        Juego siguiente = null;
        Boolean anterior = false;
        for (String categoria : categorias) {
            if (anterior) {
                JuegoWisc juegoWisc = juegosWisc.get(categoria).get(0);
                siguiente = new Juego(juegoWisc.nombre, juegoWisc.categoria, juegoWisc.activity);
                break;
            } else if (categoria.equals(juego.getCategoria())) {
                anterior = true;
            }
        }
        return siguiente;
    }

    public Juego getJuegoInicial() {
        JuegoWisc juegoWisc = juegosWisc.get(categorias.get(0)).get(0);
        return new Juego(juegoWisc.nombre, juegoWisc.categoria, juegoWisc.activity);
    }

    public Juego getJuegoAlternativo(Juego juego) {
        Juego juegosAlt = null;
        Boolean anterior = false;
        for (JuegoWisc juegoWisc : juegosWisc.get(juego.getCategoria())) {
            if (anterior) {
                juegosAlt = new Juego(juegoWisc.nombre, juegoWisc.categoria, juegoWisc.activity);
            } else if (juegoWisc.nombre.equals(juego.getNombre())) {
                anterior = true;
            }
        }
        return juegosAlt;
    }

    public static void setContext(Context context) {
        applicationContext = context;
    }

    class JuegoWisc {
        public String nombre;
        public String clase;
        public String categoria;
        public String activity;
        public Boolean alternativo;
    }
}
