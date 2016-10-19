package com.limeri.leon;

import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.Toast;

import com.limeri.leon.Models.AdministradorJuegos;
import com.limeri.leon.Models.Navegacion;
import com.limeri.leon.common.DataBase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ClavesActivity extends AppCompatActivity implements OnGesturePerformedListener{

    public static final int TIEMPO_NIVEL = 120000; //En milisegundos
    private ImageView muestra;
    private ImageView imageclave;
    private GestureLibrary gestureLib;
    private int nivel =0;
    private String jsonString;
    private String pregunta;
    private String respuesta;
    private Chronometer crono;
    private long tiempo_ejecutado = 0;
    private long tiempo_inicio;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            guardar();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claves);

        //Configuro la base de datos
        cargarClavesDB();

        muestra = (ImageView) findViewById(R.id.muestra);
        crono = (Chronometer) findViewById(R.id.cronometro);
        gestureLib = GestureLibraries.fromRawResource(this, R.raw.gesture);
        if (!gestureLib.load()) {
            finish();
        }

        GestureOverlayView gestureOverlayView = (GestureOverlayView) findViewById(R.id.gestures);
        gestureOverlayView.addOnGesturePerformedListener(this);
        //gestureOverlayView.setGestureStrokeAngleThreshold(90.0f);

        Navegacion.agregarMenuJuego(this);
        AdministradorJuegos.getInstance().inicializarJuego();

        //Llamo una funcion que se encarga de leer el archivo JSON
        inicializarVariables();
        }

    private void leerJson() {

//        if (nivel == 0) {
//            jsonString = JSONLoader.loadJSON(getResources().openRawResource(R.raw.claves));
//        }

        try {
//            JSONObject jsonRootObject = new JSONObject(jsonString);
//
//            //Get the instance of JSONArray that contains JSONObjects
//            JSONArray jsonArray = jsonRootObject.getJSONArray("claves");

            JSONArray jsonArray = new JSONArray(jsonString);

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

    @Override
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
        ArrayList<Prediction> predictions = gestureLib.recognize(gesture);
        double scoreMax = 0;
        String predictionName = "";

        for (Prediction prediction : predictions) {
            if (prediction.score > scoreMax) {
                scoreMax = prediction.score;
                predictionName = prediction.name;
            }
        }
      //  String nombre = "clave" + predictionName;
       // int simb = getResources().getIdentifier(predictionName, "string", getPackageName());
//        Toast.makeText(this, predictionName , Toast.LENGTH_SHORT).show();
        guardarRespuesta(predictionName);
    }

    private void guardarRespuesta(String tipoRespuesta) {
        long local = SystemClock.elapsedRealtime();
        long tiempo = local - tiempo_inicio + tiempo_ejecutado;
        if (tiempo <= TIEMPO_NIVEL) {
            if (respuesta.equals(tipoRespuesta) ) {
                sumarPuntos(1);
            } else {
                sumarPuntos(0);
            }
            try {
                cargarSiguienteNivel();
            } catch (Exception ex) {
                guardar();
            }
        } else {
            guardar();
        }
    }

    private void cargarClavesDB() {
        jsonString = DataBase.getEntidad("claves");
    }

    private void sumarPuntos(Integer puntos) {
        AdministradorJuegos.getInstance().sumarPuntos(puntos);
    }

    private void guardar() {
        AdministradorJuegos.getInstance().guardarJuego(this);
    }

    private void cargarSiguienteNivel() {
        inicializarVariables();
    }

    private void inicializarVariables() {
        leerJson();
    }

    private void iniciarCronometro() {
        handler.postDelayed(runnable, TIEMPO_NIVEL - tiempo_ejecutado);
        tiempo_inicio = SystemClock.elapsedRealtime();
        crono.setBase(tiempo_inicio - tiempo_ejecutado);
        crono.start();
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void finish() {
        super.finish();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        iniciarCronometro();
    }

    @Override
    public void onPause() {
        super.onPause();
        pararCronometro();
    }

    private void pararCronometro() {
        handler.removeCallbacks(runnable);
        crono.stop();
        long local = SystemClock.elapsedRealtime();
        tiempo_ejecutado = local - tiempo_inicio + tiempo_ejecutado;
    }
}
