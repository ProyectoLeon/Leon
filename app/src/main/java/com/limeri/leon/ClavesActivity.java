package com.limeri.leon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.limeri.leon.Models.AdministradorJuegos;
import com.limeri.leon.Models.Navegacion;
import com.limeri.leon.Models.Profesional;
import com.limeri.leon.common.JSONLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ClavesActivity extends AppCompatActivity implements OnGesturePerformedListener{
    private ImageView muestra;
    private ImageView imageclave;
    private GestureLibrary gestureLib;
    private int nivel =0;
    private JSONArray jsonArray;
    private JSONObject jsonObject;
    private String jsonString;
    private String pregunta;
    private String respuesta;
    private Chronometer crono;
    private long tiempo_ejecutado;
    private boolean cronostop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claves);

        muestra = (ImageView) findViewById(R.id.muestra);
        gestureLib = GestureLibraries.fromRawResource(this, R.raw.gesture);
        if (!gestureLib.load()) {
            finish();
        }

        GestureOverlayView gestureOverlayView = (GestureOverlayView) findViewById(R.id.gestures);
        gestureOverlayView.addOnGesturePerformedListener(this);
        gestureOverlayView.setGestureStrokeAngleThreshold( 90.0f);
        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.action_bar,
                null);

        getSupportActionBar().setCustomView(actionBarLayout);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        Button boton = (Button) getSupportActionBar().getCustomView().findViewById(R.id.boton_actionbar);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crono.stop();
                cronostop=true;
                tiempo_ejecutado = tiempo_ejecutado + SystemClock.elapsedRealtime() - crono.getBase();
                showPopupPassword(ClavesActivity.this);

            }
        });

        //Llamo una funcion que se encarga de leer el archivo JSON
        crono = (Chronometer) findViewById(R.id.chronometer);
        crono.setBase(SystemClock.elapsedRealtime());
        crono.start();
        cronostop = false;

        //Navegacion.agregarMenuJuego(this);
        AdministradorJuegos.getInstance().inicializarJuego();

        //Llamo una funcion que se encarga de leer el archivo JSON
        leerJson();
    }

    private void leerJson() {

        if (nivel == 0) {
            jsonString = JSONLoader.loadJSON(getResources().openRawResource(R.raw.claves));
        }

        try {
            JSONObject jsonRootObject = new JSONObject(jsonString);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.getJSONArray("claves");

            if (nivel > jsonArray.length()) {
                guardar();
            }
            JSONObject jsonObject = jsonArray.getJSONObject(nivel);
            nivel++;
            pregunta = (jsonObject.getString("pregunta").toString());
            respuesta = (jsonObject.optString("respuesta").toString());
            imageclave = (ImageView) findViewById(R.id.itemsView);
            int simb = getResources().getIdentifier(pregunta, "drawable", getPackageName());
            imageclave.setImageResource(simb);

        } catch (JSONException e) {
            //Este metodo se tiene que llamar antes de salir del juego
            guardar();
        }

    }
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
        ArrayList<Prediction> predictions = gestureLib.recognize(gesture);
        double scoreMax = 0;
        String predictionName = "";

        for (Prediction prediction : predictions) {
            if (prediction.score > scoreMax) {
                scoreMax = prediction.score;
                predictionName = prediction.name;
            }}
                String nombre = "clave" + predictionName;
                int simb = getResources().getIdentifier(nombre, "string", getPackageName());
                Toast.makeText(this, getResources().getString(simb), Toast.LENGTH_SHORT).show();
                long elapsedMillis = SystemClock.elapsedRealtime() - crono.getBase() + tiempo_ejecutado;
                if (elapsedMillis / 1000 < 120) {
                //Está funcionando OK, solo que los primeros 4 niveles son de prueba.
                // if (nivel>4){
                if (respuesta.equals(predictionName) ) {
                    sumarPuntos(1);
                } else {
                    sumarPuntos(0);
                }

                try {
                    leerJson();;
                } catch (Exception ex) {
                    guardar();
                }
            } else {
                guardar();
            }
        }


    private void sumarPuntos(Integer puntos) {
        AdministradorJuegos.getInstance().sumarPuntos(puntos);
    }

    private void guardar() {
        AdministradorJuegos.getInstance().guardarJuego(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /** if (cronostop = true){
         crono = (Chronometer) findViewById(R.id.cronometro);
         crono.setBase(SystemClock.elapsedRealtime());
         crono.start();
         cronostop = false;
         }*/
    }

    public void showPopupPassword(final Activity activity) {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(activity);
        builder.setTitle("Ingrese contraseña");

// Set up the input
        final EditText input = new EditText(activity);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(Profesional.getProfesional().getmPassword().equals(input.getText().toString())){
                    showPopupSalir(activity);
                } else {
                    Toast.makeText(activity, "Contraseña incorrecta", Toast.LENGTH_LONG).show();
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
                onResume();
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public void showPopupSalir(final Activity context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = context.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog, null);

        builder.setView(dialogView);

        Button btn_positive = (Button) dialogView.findViewById(R.id.dialog_positive_btn);
        Button btn_negative = (Button) dialogView.findViewById(R.id.dialog_negative_btn);
        Button btn_neutral = (Button) dialogView.findViewById(R.id.dialog_neutral_btn);

        final AlertDialog dialog = builder.create();

        btn_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdministradorJuegos.getInstance().guardarJuego(context);
            }
        });

        btn_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdministradorJuegos.getInstance().cancelarJuego(context);
            }
        });

        btn_neutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResume();
                dialog.cancel();
            }
        });

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                onResume();
                dialog.dismiss();
            }
        });

        // Display the custom alert dialog on interface
        dialog.show();

    }
    public void onResume(){
        super.onResume();
        crono = (Chronometer) findViewById(R.id.chronometer);
        crono.setBase(SystemClock.elapsedRealtime());
        crono.start();
        cronostop = false;

    }

}
