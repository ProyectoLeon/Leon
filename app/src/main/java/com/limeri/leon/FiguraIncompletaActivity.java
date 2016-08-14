package com.limeri.leon;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.limeri.leon.Models.AdministradorJuegos;
import com.limeri.leon.Models.Navegacion;

public class FiguraIncompletaActivity extends AppCompatActivity {


    ImageView Figura;
    static final double PORC_PUNTO_CORRECTO_Y = 0.51;
    static final double PORC_PUNTO_CORRECTO_X = 0.55;
    static final double PORC_COTA_Y = 0.072;
    static final double PORC_COTA_X = 0.08;
    private Integer puntaje = 0;

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
                AlertDialog.Builder builder = new AlertDialog.Builder(FiguraIncompletaActivity.this);

                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alert_dialog,null);

                builder.setView(dialogView);

                Button btn_positive = (Button) dialogView.findViewById(R.id.dialog_positive_btn);
                Button btn_negative = (Button) dialogView.findViewById(R.id.dialog_negative_btn);
                Button btn_neutral = (Button) dialogView.findViewById(R.id.dialog_neutral_btn);

                final AlertDialog dialog = builder.create();

                btn_positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        guardar();
                    }
                });

                btn_negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelar();
                    }
                });

                btn_neutral.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                // Display the custom alert dialog on interface
                dialog.show();
            }
        });

    }

    //TODO: Lógica del JUEGO - Iteración 3
    private void guardar() {
        try {
            AdministradorJuegos.getInstance().guardarJuego(puntaje, this);
            Navegacion.irA(this, InicioJuegoActivity.class);
        } catch (Exception e) {
            Navegacion.irA(this, ExamenActivity.class);
        }
    }

    private void cancelar() {
        try {
            AdministradorJuegos.getInstance().cancelarJuego(this);
            Navegacion.irA(this, InicioJuegoActivity.class);
        } catch (Exception e) {
            Navegacion.irA(this, ExamenActivity.class);
        }
    }
}
