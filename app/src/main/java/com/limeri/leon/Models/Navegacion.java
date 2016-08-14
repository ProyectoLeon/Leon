package com.limeri.leon.Models;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.limeri.leon.InicioJuegoActivity;

/**
 * Created by Nico on 13/8/2016.
 */
public class Navegacion {

    public static void volver(Activity actividad, Class clase) {
        Intent mainIntent = new Intent(actividad, clase);
        actividad.startActivity(mainIntent);
        actividad.finish();
    }

}
