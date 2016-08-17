package com.limeri.leon.Models;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.limeri.leon.R;

public class Navegacion {

    private static Class anterior;

    public static void irA(Activity activity, Class clase) {
        anterior = activity.getClass();
        Intent mainIntent = new Intent(activity, clase);
        activity.startActivity(mainIntent);
        activity.finish();
    }

    public static void irA(Activity activity, Class clase, Class claseAnterior) {
        Class destino = clase;
        if (anterior.equals(claseAnterior)) {
            destino = anterior;
        }
        Intent mainIntent = new Intent(activity, destino);
        activity.startActivity(mainIntent);
        activity.finish();
    }

    public static void agregarMenuJuego(final AppCompatActivity activity) {
        ViewGroup actionBarLayout = (ViewGroup) activity.getLayoutInflater().inflate(R.layout.action_bar,null);

        ActionBar actionBar =  activity.getSupportActionBar();
        if (actionBar != null) {
            activity.getSupportActionBar().setCustomView(actionBarLayout);
            activity.getSupportActionBar().setDisplayShowCustomEnabled(true);
        }

        Button boton =(Button) activity.getSupportActionBar().getCustomView().findViewById(R.id.boton_actionbar);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);

                LayoutInflater inflater = activity.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alert_dialog,null);

                builder.setView(dialogView);

                Button btn_positive = (Button) dialogView.findViewById(R.id.dialog_positive_btn);
                Button btn_negative = (Button) dialogView.findViewById(R.id.dialog_negative_btn);
                Button btn_neutral = (Button) dialogView.findViewById(R.id.dialog_neutral_btn);

                final android.app.AlertDialog dialog = builder.create();

                btn_positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AdministradorJuegos.getInstance().guardarJuego(activity);
                    }
                });

                btn_negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AdministradorJuegos.getInstance().cancelarJuego(activity);
                    }
                });

                btn_neutral.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        irA(activity, activity.getClass());
                    }
                });

                // Display the custom alert dialog on interface
                dialog.show();
            }
        });

    }
}
