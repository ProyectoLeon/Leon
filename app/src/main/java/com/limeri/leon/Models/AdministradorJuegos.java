package com.limeri.leon.Models;

import android.app.Activity;
import android.content.Context;

import com.limeri.leon.R;
import com.limeri.leon.ValorExamenActivity;
import com.limeri.leon.common.JSONLoader;

import org.json.JSONArray;
import org.json.JSONException;
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
    private List<String> alternativas;

    private AdministradorJuegos() {

        juegosWisc = new ArrayList<>();
        alternativas = new ArrayList<>();

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
                juego.categoria = jsonJuego.getString("categoria");
                juego.activity = jsonJuego.getString("activity");
                juego.alternativo = jsonJuego.getBoolean("alternativo");
                juegosWisc.add(juego);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Juego getJuegoInicial() {
        JuegoWisc juegoWisc = juegosWisc.get(0);
        return new Juego(juegoWisc.nombre, juegoWisc.categoria, juegoWisc.activity);
    }

    public void guardarJuego(Integer puntosJuego, Activity activity) {
        Paciente paciente = Paciente.getSelectedPaciente();
        Evaluacion evaluacion = paciente.getEvaluacionActual();
        Juego juego = evaluacion.getJuegoActual();
        juego.setPuntosJuego(puntosJuego);
        juego.finalizar();
        if (isUltimoJuego(juego)) {
            evaluacion.finalizar();
            Navegacion.volver(activity, ValorExamenActivity.class);
        }
        Paciente.saveCuenta(activity, paciente);
    }

    private Boolean isUltimoJuego(Juego juego) {
        Boolean ultimo = false;
        Boolean siguiente = false;
        for (JuegoWisc juegoWisc : juegosWisc) {
            if (siguiente) {
                if (juegoWisc.alternativo && alternativas.isEmpty()) {
                    ultimo = true;
                }
                break;
            } else if (juego.getNombre().equals(juegoWisc.nombre)) {
                if (juegosWisc.indexOf(juegoWisc) == juegosWisc.size() - 1) {
                    ultimo = true;
                    break;
                } else {
                    siguiente = true;
                }
            }
        }
        return ultimo;
    }

    public static void setContext(Context context) {
        applicationContext = context;
    }

    public Juego getSiguienteJuego(Evaluacion evaluacion) {
        Juego juego = null;
        if (evaluacion.tieneJuegos()) {
            Boolean anterior = false;
            Juego ultimoJuego = evaluacion.getUltimoJuego();
            for (JuegoWisc juegoWisc : juegosWisc) {
                if (anterior) {
                    if (!juegoWisc.alternativo) {
                        juego = new Juego(juegoWisc.nombre, juegoWisc.categoria, juegoWisc.activity);
                        break;
                    } else if (alternativas.contains(juegoWisc.categoria)) {
                        juego = new Juego(juegoWisc.nombre, juegoWisc.categoria, juegoWisc.activity);
                        alternativas.remove(juegoWisc.categoria);
                        break;
                    }
                } else if (ultimoJuego.getNombre().equals(juegoWisc.nombre)) {
                    anterior = true;
                }
            }
        } else {
            juego = getJuegoInicial();
        }
        return juego;
    }

    public void cancelarJuego(Activity activity) {
        Paciente paciente = Paciente.getSelectedPaciente();
        Evaluacion evaluacion = paciente.getEvaluacionActual();
        Juego juego = evaluacion.getJuegoActual();
        alternativas.add(juego.getCategoria());
        juego.setPuntosJuego(0);
        juego.setPuntosNiveles(null);
        juego.cancelar();
        if (isUltimoJuego(juego)) {
            //evaluacion.finalizar();
        }
        Paciente.saveCuenta(activity, paciente);
    }

    class JuegoWisc {
        public String nombre;
        public String categoria;
        public String activity;
        public Boolean alternativo;
    }
}
