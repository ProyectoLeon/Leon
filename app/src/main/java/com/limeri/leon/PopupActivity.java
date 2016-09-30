package com.limeri.leon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.limeri.leon.Models.AdministradorJuegos;
import com.limeri.leon.Models.Navegacion;
import com.limeri.leon.Models.Paciente;
import com.limeri.leon.Models.Profesional;

public class PopupActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.alert_dialog);

        Button btn_positive = (Button) findViewById(R.id.dialog_positive_btn);
        Button btn_negative = (Button) findViewById(R.id.dialog_negative_btn);
        Button btn_negative2 = (Button) findViewById(R.id.dialog_negative_btn2);
        Button btn_neutral = (Button) findViewById(R.id.dialog_neutral_btn);

        btn_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdministradorJuegos.getInstance().guardarJuego(PopupActivity.this);
            }
        });

        if (Paciente.getSelectedPaciente().tieneEvaluacionIniciada()) {
            // Evaluación
            btn_negative.setVisibility(View.VISIBLE);
            btn_negative2.setVisibility(View.GONE);
            if (AdministradorJuegos.getInstance().isUltimoJuegoCategoria()) {
                btn_negative.setText("Cancelar Juego");
                btn_negative.setOnClickListener(cancelarUltimoJuegoListener(PopupActivity.this));
            } else {
                btn_negative.setOnClickListener(cancelarJuegoListener(PopupActivity.this));
            }
        } else {
            // Juego Libre
            btn_negative2.setVisibility(View.VISIBLE);
            btn_negative.setVisibility(View.GONE);
            btn_negative2.setOnClickListener(cancelarJuegoLibreListener(PopupActivity.this));
        }

        btn_neutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navegacion.volver(PopupActivity.this);
            }
        });

        showPopUpPassword();
    }

    private void showPopUpPassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ingrese contraseña");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(Profesional.getProfesional().getmPassword().equals(input.getText().toString())){
                    dialog.cancel();
                } else {
                    PopupActivity.this.finish();
                }
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PopupActivity.this.finish();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    @NonNull
    private static View.OnClickListener cancelarUltimoJuegoListener(final Activity activity) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelarUltimoJuego(activity);
            }
        };
    }

    @NonNull
    private static View.OnClickListener cancelarJuegoListener(final Activity activity) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdministradorJuegos.getInstance().cancelarJuego(activity);
            }
        };
    }

    @NonNull
    private static View.OnClickListener cancelarJuegoLibreListener(final Activity activity) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdministradorJuegos.getInstance().cancelarJuegoLibre(activity);
            }
        };
    }

    private static void cancelarUltimoJuego(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog,null);

        builder.setView(dialogView);

        Button btn_positive = (Button) dialogView.findViewById(R.id.dialog_positive_btn);
        Button btn_negative = (Button) dialogView.findViewById(R.id.dialog_negative_btn);
        Button btn_neutral = (Button) dialogView.findViewById(R.id.dialog_neutral_btn);
        TextView tv = (TextView) dialogView.findViewById(R.id.dialog_tv);

        final AlertDialog dialog = builder.create();

        tv.setText("No quedan juegos alternativos para esta categoría, ¿Desea continuar?");
        btn_negative.setText("No");
        btn_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        btn_neutral.setVisibility(View.GONE);

        btn_positive.setText("Si");
        btn_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdministradorJuegos.getInstance().cancelarJuego(activity);

            }
        });

        // Display the custom alert dialog on interface
        dialog.show();
    }
}
