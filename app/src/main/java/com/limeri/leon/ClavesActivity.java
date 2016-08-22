package com.limeri.leon;

import android.app.Activity;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.Toast;

import com.limeri.leon.Models.AdministradorJuegos;
import com.limeri.leon.Models.Navegacion;
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
    private int tiempo_ejecutado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claves);

     /**   if (siguiente != null) {
            siguiente.setOnClickListener(clickSiguiente());
        }
*/
        muestra = (ImageView) findViewById(R.id.muestra);
        gestureLib = GestureLibraries.fromRawResource(this, R.raw.gesture);
        if (!gestureLib.load()) {
            finish();
        }

        GestureOverlayView gestureOverlayView = (GestureOverlayView) findViewById(R.id.gestures);
        gestureOverlayView.addOnGesturePerformedListener(this);

        Navegacion.agregarMenuJuego(this);
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
                //guardar();
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
        for (Prediction prediction : predictions) {
            if (prediction.score > 5.0) {
                Toast.makeText(this, prediction.name, Toast.LENGTH_SHORT).show();
               // long elapsedMillis = SystemClock.elapsedRealtime() - crono.getBase() + tiempo_ejecutado;
                //if (elapsedMillis / 1000 < 120) {
                    //Está funcionando OK, solo que los primeros 4 niveles son de prueba.
                   // if (nivel>4){
                        if (respuesta.equals(prediction.name) ) {
                            sumarPuntos(1);
                        } else {
                            sumarPuntos(0);
                        }

                    try {
                        leerJson();
                        break;
                    } catch (Exception ex) {
                        guardar();
                    }
                } else {
                    guardar();
                }


            //}
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

}
