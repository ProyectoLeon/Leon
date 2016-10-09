package com.limeri.leon.Models;

import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.limeri.leon.ExamenActivity;
import com.limeri.leon.InicioJuegoActivity;
import com.limeri.leon.R;
import com.limeri.leon.ValorExamenActivity;
import com.limeri.leon.ValorJuegoLibreActivity;
import com.limeri.leon.common.JSONLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
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

        String jsonString = JSONLoader.loadJSON(applicationContext.getResources().openRawResource(R.raw.protocolo_posta));
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
                juego.juegaPaciente = jsonJuego.getBoolean("juegaPaciente");

                Type listType = new TypeToken<List<List<Integer>>>() {}.getType();
                JSONArray jsonEquivalencia = jsonJuego.getJSONArray("equivalencia");
                juego.puntaje = (List<List<Integer>>) new Gson().fromJson(jsonEquivalencia.toString(), listType);

                juegosWisc.add(juego);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Juego getJuegoInicial() {
        JuegoWisc juegoWisc = juegosWisc.get(0);
        return new Juego(juegoWisc.nombre, juegoWisc.categoria, juegoWisc.activity, juegoWisc.puntaje, juegoWisc.alternativo, juegoWisc.juegaPaciente);
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
                        juego = new Juego(juegoWisc.nombre, juegoWisc.categoria, juegoWisc.activity, juegoWisc.puntaje, juegoWisc.alternativo, juegoWisc.juegaPaciente);
                        break;
                    } else if (alternativas.contains(juegoWisc.categoria)) {
                        juego = new Juego(juegoWisc.nombre, juegoWisc.categoria, juegoWisc.activity, juegoWisc.puntaje, juegoWisc.alternativo, juegoWisc.juegaPaciente);
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

    private void sumarPuntosJuego(Integer puntos) {
        if (Paciente.getSelectedPaciente().tieneEvaluacionIniciada()) {
            // Evaluación
            Juego juego = Paciente.getSelectedPaciente().getEvaluacionActual().getJuegoActual();
            juego.setPuntosJuego(puntos);
        } else {
            // Juego Libre
            Juego juego = Paciente.getSelectedPaciente().getEvaluacionFinalizada().getJuegoLibreActual();
            juego.setPuntosJuego(puntos);
        }
    }

    public Integer getPuntosNivel(Integer nivel) {
        if (Paciente.getSelectedPaciente().tieneEvaluacionIniciada()) {
            // Evaluación
            Juego juego = Paciente.getSelectedPaciente().getEvaluacionActual().getJuegoActual();
            return juego.getPuntosNivel(nivel);
        } else {
            // Juego Libre
            Juego juego = Paciente.getSelectedPaciente().getEvaluacionFinalizada().getJuegoLibreActual();
            return juego.getPuntosNivel(nivel);
        }
    }

    public void guardarPuntosNivel(Integer nivel, Integer puntos) {
        if (Paciente.getSelectedPaciente().tieneEvaluacionIniciada()) {
            // Evaluación
            Juego juego = Paciente.getSelectedPaciente().getEvaluacionActual().getJuegoActual();
            juego.guardarPuntosNivel(nivel, puntos);
        } else {
            // Juego Libre
            Juego juego = Paciente.getSelectedPaciente().getEvaluacionFinalizada().getJuegoLibreActual();
            juego.guardarPuntosNivel(nivel, puntos);
        }
    }

    public void sumarPuntos(Integer puntos) {
        if (Paciente.getSelectedPaciente().tieneEvaluacionIniciada()) {
            // Evaluación
            sumarPuntosJuego(Paciente.getSelectedPaciente().getEvaluacionActual().getJuegoActual().getPuntosJuego() + puntos);
        } else {
            // Juego Libre
            sumarPuntosJuego(Paciente.getSelectedPaciente().getEvaluacionFinalizada().getJuegoLibreActual().getPuntosJuego() + puntos);
        }
    }

    public void restarPuntos(Integer puntos) {
        if (Paciente.getSelectedPaciente().tieneEvaluacionIniciada()) {
            // Evaluación
            sumarPuntosJuego(Paciente.getSelectedPaciente().getEvaluacionActual().getJuegoActual().getPuntosJuego() - puntos);
        } else {
            // Juego Libre
            sumarPuntosJuego(Paciente.getSelectedPaciente().getEvaluacionFinalizada().getJuegoLibreActual().getPuntosJuego() - puntos);
        }
    }

    public Integer obtenerPuntos() {
        if (Paciente.getSelectedPaciente().tieneEvaluacionIniciada()) {
            // Evaluación
            return Paciente.getSelectedPaciente().getEvaluacionActual().getJuegoActual().getPuntosJuego();
        } else {
            // Juego Libre
            return Paciente.getSelectedPaciente().getEvaluacionFinalizada().getJuegoLibreActual().getPuntosJuego();
        }
    }

    public void inicializarJuego() {
        sumarPuntosJuego(0);
    }

    public void guardarJuego(Activity activity) {
        try {
            Paciente paciente = Paciente.getSelectedPaciente();
            if (paciente.tieneEvaluacionIniciada()) {
                // Evaluación
                Evaluacion evaluacion = paciente.getEvaluacionActual();
                Juego juego = evaluacion.getJuegoActual();
                if (juego.getPuntosJuego() < 0) {
                    juego.setPuntosJuego(0);
                }
                juego.finalizar();
                if (isUltimoJuego(juego)) {
                    evaluacion.finalizar();
                    calcularPuntaje(evaluacion);
                    calcularFecha(evaluacion);
                    Navegacion.irA(activity, ValorExamenActivity.class);
                } else {
                    Navegacion.irA(activity, InicioJuegoActivity.class, ExamenActivity.class);
                }
                Paciente.saveCuenta(paciente);
            } else {
                // Juego Libre
                Evaluacion evaluacion = paciente.getEvaluacionFinalizada();
                Juego juego = evaluacion.getJuegoLibreActual();
                if (juego.getPuntosJuego() < 0) {
                    juego.setPuntosJuego(0);
                }
                juego.finalizar();
//                Navegacion.irA(activity, InicioJuegoActivity.class, ExamenActivity.class);
                Navegacion.irA(activity, ValorJuegoLibreActivity.class);
                Paciente.saveCuenta(paciente);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Navegacion.irA(activity, ExamenActivity.class);
        }
    }

    public void cancelarJuego(Activity activity) {
        try {
            Paciente paciente = Paciente.getSelectedPaciente();
            Evaluacion evaluacion = paciente.getEvaluacionActual();
            Juego juego = evaluacion.getJuegoActual();
            alternativas.add(juego.getCategoria());
            if (juego.getPuntosJuego() < 0) {
                juego.setPuntosJuego(0);
            }
            juego.cancelar();
            if (isUltimoJuego(juego)) {
                evaluacion.finalizar();
                calcularPuntaje(evaluacion);
                calcularFecha(evaluacion);
                Navegacion.irA(activity, ValorExamenActivity.class);
            } else {
                Navegacion.irA(activity, InicioJuegoActivity.class, ExamenActivity.class);
            }
            Paciente.saveCuenta(paciente);
        } catch (Exception e) {
            e.printStackTrace();
            Navegacion.irA(activity, ExamenActivity.class);
        }
    }

    public void cancelarJuegoLibre(Activity activity) {
        try {
            Paciente paciente = Paciente.getSelectedPaciente();
            Evaluacion evaluacion = paciente.getEvaluacionFinalizada();
            Juego juego = evaluacion.getJuegoLibreActual();
            alternativas.add(juego.getCategoria());
            if (juego.getPuntosJuego() < 0) {
                juego.setPuntosJuego(0);
            }
            juego.cancelar();
            Navegacion.irA(activity, InicioJuegoActivity.class, ExamenActivity.class);
            Paciente.saveCuenta(paciente);
        } catch (Exception e) {
            e.printStackTrace();
            Navegacion.irA(activity, ExamenActivity.class);
        }
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

    public Boolean isUltimoJuegoCategoria() {
        Boolean ultimo = true;
        Boolean siguiente = false;
        Paciente paciente = Paciente.getSelectedPaciente();
        Evaluacion evaluacion = paciente.getEvaluacionActual();
        Juego juego = evaluacion.getJuegoActual();
        for (JuegoWisc juegoWisc : juegosWisc) {
            if (siguiente) {
                if (juegoWisc.alternativo && juegoWisc.categoria.equals(juego.getCategoria())) {
                    ultimo = false;
                }
            } else if (juego.getNombre().equals(juegoWisc.nombre)) {
                siguiente = true;
            }
        }
        return ultimo;
    }

    public void cancelarUltimoJuego(Activity activity) {
        Paciente paciente = Paciente.getSelectedPaciente();
        Evaluacion evaluacion = paciente.getEvaluacionActual();
        Juego juego = evaluacion.getJuegoActual();
        if (juego.getPuntosJuego() < 0) {
            juego.setPuntosJuego(0);
        }
        juego.cancelar();
        evaluacion.finalizar();
        Navegacion.irA(activity, ValorExamenActivity.class);
        Paciente.saveCuenta(paciente);
    }

    public Boolean isUltimoJuegoProtocolo() {
        Paciente paciente = Paciente.getSelectedPaciente();
        Evaluacion evaluacion = paciente.getEvaluacionActual();
        Juego juego = evaluacion.getJuegoActual();
        return isUltimoJuego(juego);
    }

    class JuegoWisc {
        public String nombre;
        public String categoria;
        public String activity;
        public Boolean alternativo;
        public Boolean juegaPaciente;
        public List<List<Integer>> puntaje = new ArrayList<>();
    }

    public void calcularFecha (Evaluacion evaluacion){
        Calendar cal = Calendar.getInstance();
        cal.getTime().getTime();
        cal.setTimeInMillis(System.currentTimeMillis());
        Integer añoev = cal.get(Calendar.YEAR);
        Integer mesev = cal.get(Calendar.MONTH) + 1;
        Integer diaev = cal.get(Calendar.DAY_OF_MONTH);

// Para evitar que el día y el mes queden sin el cero adelante. Por ejemplo si es 9, queda 09.
        String strmesev = mesev.toString();
        if (strmesev.length() == 1)
            strmesev = 0 + strmesev;

        String strdiaev = diaev.toString();
        if (strdiaev.length() == 1)
            strdiaev = 0 + strdiaev;

        String dateev = strdiaev + '/' + strmesev + '/' + añoev.toString();
        evaluacion.setFechaEvaluación(dateev);
    }

    public void calcularPuntaje(Evaluacion evaluacion) {
        Integer puntosCompVerbal = 0;
        Integer puntosRazPercep = 0;
        Integer puntosMemOper = 0;
        Integer puntosVelocProc = 0;
        Integer coeficienteIntelectual = 0;
        Integer puntajeEscalar = 0;

        for (Juego juego : evaluacion.getJuegos()) {
            puntajeEscalar = juego.getPuntajeEscalar();
            coeficienteIntelectual = coeficienteIntelectual + puntajeEscalar;

            switch (juego.getCategoria()){
                case("Comprensión verbal"):
                    puntosCompVerbal = puntosCompVerbal + puntajeEscalar;
                    break;
                case("Razonamiento Perceptivo"):
                    puntosRazPercep = puntosRazPercep + puntajeEscalar;
                    break;
                case("Memoria Operativa"):
                    puntosMemOper = puntosMemOper + puntajeEscalar;
                    break;
                case("Velocidad de Procesamiento"):
                    puntosVelocProc = puntosVelocProc + puntajeEscalar;
                    break;
            }
        }

        evaluacion.setPuntosCompVerbal(puntosCompVerbal);
        evaluacion.setPuntosRazPercep(puntosRazPercep);
        evaluacion.setPuntosMemOper(puntosMemOper);
        evaluacion.setPuntosVelocProc(puntosVelocProc);
        evaluacion.setCoeficienteIntelectual(coeficienteIntelectual);
    }
}
