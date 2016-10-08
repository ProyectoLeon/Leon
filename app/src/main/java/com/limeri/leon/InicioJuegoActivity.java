package com.limeri.leon;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.limeri.leon.Models.AdministradorJuegos;
import com.limeri.leon.Models.Evaluacion;
import com.limeri.leon.Models.Juego;
import com.limeri.leon.Models.Navegacion;
import com.limeri.leon.Models.Paciente;

public class InicioJuegoActivity extends AppCompatActivity {

    private Evaluacion evaluacion;
    private Juego juego;
    private final String PREFIJO = this.getClass().getPackage().getName() + ".";
    private TextView txtJuego;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_juego);
        Paciente paciente = Paciente.getSelectedPaciente();
        if (paciente.tieneEvaluacionIniciada()) {
            evaluacion = paciente.getEvaluacionActual();
        } else {
            evaluacion = new Evaluacion();
            paciente.agregarEvaluacion(evaluacion);
            Paciente.saveCuenta(paciente);
        }

        juego = AdministradorJuegos.getInstance().getSiguienteJuego(evaluacion);

        txtJuego = (TextView) findViewById(R.id.juego);
        if (txtJuego != null) {
            txtJuego.setText(juego.getNombre());
        }

        Button buttonStart = (Button) findViewById(R.id.buttonStart);
        if (buttonStart != null) {
            buttonStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        evaluacion.agregarJuego(juego);
                        Class activity = Class.forName(PREFIJO + juego.getNombreActividad());
                        Navegacion.irA(InicioJuegoActivity.this, activity);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.action_bar,
                null);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setCustomView(actionBarLayout);
            actionBar.setDisplayShowCustomEnabled(true);
        }

        Button boton = (Button) getSupportActionBar().getCustomView().findViewById(R.id.boton_actionbar);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(InicioJuegoActivity.this);

                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alert_dialog, null);

                builder.setView(dialogView);

                Button btn_positive = (Button) dialogView.findViewById(R.id.dialog_positive_btn);
                Button btn_negative = (Button) dialogView.findViewById(R.id.dialog_negative_btn);
                Button btn_neutral = (Button) dialogView.findViewById(R.id.dialog_neutral_btn);

                final AlertDialog dialog = builder.create();
                btn_positive.setText("Detener evaluación");
                btn_positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Navegacion.irA(InicioJuegoActivity.this, MainActivity.class);
                    }
                });

                btn_neutral.setText("Anular evaluación");
                btn_neutral.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopupConfirmar(InicioJuegoActivity.this);
                    }
                });

                btn_negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        evaluacion.agregarJuego(juego);
                        Boolean ultimoJuego = AdministradorJuegos.getInstance().isUltimoJuegoProtocolo();
                        AdministradorJuegos.getInstance().cancelarJuego(InicioJuegoActivity.this);
                        if (!ultimoJuego) {
                            juego = AdministradorJuegos.getInstance().getSiguienteJuego(evaluacion);

                            txtJuego = (TextView) findViewById(R.id.juego);
                            if (txtJuego != null) {
                                txtJuego.setText(juego.getNombre());
                            }
                        }
                    }
                });

                // Display the custom alert dialog on interface
                dialog.show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        Juego juegoActual = evaluacion.getJuegoActual();
        Juego juegoUltimo = evaluacion.getUltimoJuego();
        if ((juegoActual != null) || (juegoUltimo != null)) {
            super.onBackPressed();
            Navegacion.irA(this, MainActivity.class);
        } else {
            showPopupConfirmar(InicioJuegoActivity.this);
        }
    }

    private void showPopupConfirmar(final AppCompatActivity context) {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        String msje = "¿Está seguro que desea eliminar la evaluación actual?";
        builder.setTitle(msje);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Paciente paciente = Paciente.getSelectedPaciente();
                paciente.borrarEvaluacionActual();
                Paciente.saveCuenta(paciente);
                Navegacion.irA(InicioJuegoActivity.this, MainActivity.class);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}