package com.limeri.leon.Models;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.limeri.leon.BqSimbolosActivity;
import com.limeri.leon.ExamenActivity;
import com.limeri.leon.PopupActivity;
import com.limeri.leon.R;
import com.limeri.leon.ValorExamenActivity;

import java.util.Stack;

public class Navegacion {

    private static Class anterior;
    private static Stack<Class> anteriores = new Stack<>();

    public static void irA(Activity activity, Class clase) {
        anterior = activity.getClass();
        anteriores.push(activity.getClass());
        Intent mainIntent = new Intent(activity, clase);
        activity.startActivity(mainIntent);
        activity.finish();
    }

    public static void irA(Activity activity, Class clase, String value){
            anterior = activity.getClass();
            anteriores.push(activity.getClass());
            Intent mainIntent = new Intent(activity, clase);
            Bundle b = new Bundle();
            b.putString("key",value);
            mainIntent.putExtras(b);
            activity.startActivity(mainIntent);
            activity.finish();
        }

    public static void irA(Activity activity, Class clase, int position){
        Intent mainIntent = new Intent(activity, clase);
        Bundle b = new Bundle();
        b.putInt("position",position);
        mainIntent.putExtras(b);
        activity.startActivity(mainIntent);
        activity.finish();
    }


    public static void irA(Activity activity, Class clase, Class claseAnterior) {
        Class destino = clase;
        if (anterior.equals(claseAnterior)) {
            destino = anterior;
            if (anterior.equals(ExamenActivity.class)) {

                Paciente.getSelectedPaciente().getEvaluacionActual().finalizar();
                destino = ValorExamenActivity.class;
                Intent mainIntent = new Intent(activity, destino);
                Bundle b = new Bundle();
                b.putInt("position",Paciente.getSelectedPaciente().getEvaluaciones().size() - 1);
                mainIntent.putExtras(b);
                activity.startActivity(mainIntent);
                activity.finish();
            }
        }else {
            Intent mainIntent = new Intent(activity, destino);
            activity.startActivity(mainIntent);
            activity.finish();
        }
    }

    public static void volver(Activity activity) {
        Class anterior = anteriores.pop();
        Intent mainIntent = new Intent(activity, anterior);
        activity.startActivity(mainIntent);
        activity.finish();
    }

    public static void irAPopUp(Activity activity) {
        anteriores.push(activity.getClass());
        Intent i = new Intent(activity.getApplicationContext(), PopupActivity.class);
        PopupActivity.anterior = activity;
        activity.startActivity(i);
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

                //showPopupPassword(activity);
                irAPopUp(activity);

            }
        });

    }

/*
    private static void showPopupPassword(final AppCompatActivity context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Ingrese contraseña");

// Set up the input
        final EditText input = new EditText(context);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(Profesional.getProfesionalActual().getContrasena().equals(input.getText().toString())){
                    irAPopUp(context);
                } else {
                    Toast.makeText(context, "Contraseña incorrecta", Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                ((BqSimbolosActivity)context).onResume();
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private static void showPopupSalir(final AppCompatActivity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog,null);

        builder.setView(dialogView);

        Button btn_positive = (Button) dialogView.findViewById(R.id.dialog_positive_btn);
        Button btn_negative = (Button) dialogView.findViewById(R.id.dialog_negative_btn);
        Button btn_neutral = (Button) dialogView.findViewById(R.id.dialog_neutral_btn);

        final AlertDialog dialog = builder.create();
        btn_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdministradorJuegos.getInstance().guardarJuego(activity);
            }
        });

        if (AdministradorJuegos.getInstance().isUltimoJuegoCategoria()) {
            btn_negative.setText("Cancelar Juego");
            btn_negative.setOnClickListener(cancelarUltimoJuegoListener(activity));
        } else {
            btn_negative.setOnClickListener(cancelarJuegoListener(activity));
        }

        btn_neutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irA(activity, activity.getClass());
            }
        });

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                ((BqSimbolosActivity)activity).onResume();
                dialog.dismiss();
            }
        });
        // Display the custom alert dialog on interface
        dialog.show();
    }

    @NonNull
    private static View.OnClickListener cancelarUltimoJuegoListener(final AppCompatActivity activity) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelarUltimoJuego(activity);
            }
        };
    }

    @NonNull
    private static View.OnClickListener cancelarJuegoListener(final AppCompatActivity activity) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdministradorJuegos.getInstance().cancelarJuego(activity);
            }
        };
    }

    private static void cancelarUltimoJuego(final AppCompatActivity activity) {
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
*/
}
