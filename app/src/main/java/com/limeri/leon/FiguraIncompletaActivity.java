package com.limeri.leon;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class FiguraIncompletaActivity extends AppCompatActivity {


    ImageView Figura;
    static final double PORC_PUNTO_CORRECTO_Y = 0.51;
    static final double PORC_PUNTO_CORRECTO_X = 0.55;
    static final double PORC_COTA_Y = 0.072;
    static final double PORC_COTA_X = 0.08;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_figura_incompleta);

        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.action_bar,
                null);


        getSupportActionBar().setCustomView(actionBarLayout);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        Button boton =(Button) getSupportActionBar().getCustomView().findViewById(R.id.boton_actionbar);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(FiguraIncompletaActivity.this)
                        .setTitle("Popup")
                        .setMessage("Por favor seleccione alguna opción")
                        .setPositiveButton("Guardar y finalizar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent mainIntent = new Intent(FiguraIncompletaActivity.this, ExamenActivity.class);
                                FiguraIncompletaActivity.this.startActivity(mainIntent);
                            }
                        })
                        .setNegativeButton("Reiniciar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        })
                        .setNeutralButton("Activar Juego Alternativo", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

    }

}
