package com.limeri.leon.Models;

import com.limeri.leon.BqSimbolosActivity;
import com.limeri.leon.FiguraIncompletaActivity;
import com.limeri.leon.InformacionActivity;
import com.limeri.leon.MatricesActivity;
import com.limeri.leon.VocabularioActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdministradorJuegos {

    private static Map<String,Class> juegos;
    static {
        juegos = new HashMap<>();
        juegos.put(Matrices.getClassName(),MatricesActivity.class);
        juegos.put(Informacion.getClassName(),InformacionActivity.class);
        juegos.put(BusquedaSimbolos.getClassName(),BqSimbolosActivity.class);
        juegos.put(FigurasIncompleta.getClassName(), FiguraIncompletaActivity.class);
        juegos.put(Vocabulario.getClassName(),VocabularioActivity.class);
    }

    private static Map<Class,List<Class>> juegosAlternativos;
    static {
        juegosAlternativos = new HashMap<>();
        juegosAlternativos.put(Matrices.class, Collections.singletonList((Class) Matrices.class));
        juegosAlternativos.put(Informacion.class, Collections.singletonList((Class) Informacion.class));
        juegosAlternativos.put(BusquedaSimbolos.class, Collections.singletonList((Class) BusquedaSimbolos.class));
        juegosAlternativos.put(FigurasIncompleta.class, Collections.singletonList((Class) FigurasIncompleta.class));
        juegosAlternativos.put(Vocabulario.class, Collections.singletonList((Class) Vocabulario.class));
    }

    private static List<Class> juegosWisc = Arrays.asList((Class) Matrices.class,(Class) Informacion.class,(Class) BusquedaSimbolos.class,(Class) FigurasIncompleta.class,(Class) Vocabulario.class);

    public static Juego getJuegoSiguiente(Juego juego) {
        Juego siguiente = null;
        try {
            Boolean anterior = false;
            for (Class clase : juegosWisc) {
                if (anterior) {
                    siguiente = (Juego) clase.newInstance();
                } else if (clase.equals(juego.getClass())) {
                    anterior = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return siguiente;
    }

    public static Juego getJuegoInicial() {
        Juego juego = null;
        try {
            juego = (Juego) juegosWisc.get(0).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return juego;
    }

    public static List<Juego> getJuegosAlternativos(Juego juego) {
        List<Juego> juegosAlt = null;
        try {
            juegosAlt = new ArrayList<>();
            for (Class clase: juegosAlternativos.get(juego.getClass())) {
                juegosAlt.add((Juego)clase.newInstance());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return juegosAlt;
    }

    public static Class getJuegoActivity(String juego) {
        return juegos.get(juego);
    }
}
