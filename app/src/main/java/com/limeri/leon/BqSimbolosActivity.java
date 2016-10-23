package com.limeri.leon;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;

import com.limeri.leon.Models.AdministradorJuegos;
import com.limeri.leon.Models.Navegacion;
import com.limeri.leon.common.DataBase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BqSimbolosActivity extends AppCompatActivity {

    public static final int TIEMPO_NIVEL = 120000; //En milisegundos
    private String respuesta;
    private int nivel = 0;
    private String jsonString;
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
        setContentView(R.layout.activity_bq_simbolos);

        //Configuro la base de datos
        cargarSimbolosDB();

        crono = (Chronometer) findViewById(R.id.cronometro);

        Navegacion.agregarMenuJuego(this);
        AdministradorJuegos.getInstance().inicializarJuego();

        Button respuestaSi = (Button) findViewById(R.id.buttonYes);
        if (respuestaSi != null) {
            respuestaSi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    guardarRespuesta("true");
                }
            });
        }
        Button respuestaNo = (Button) findViewById(R.id.buttonNo);
        if (respuestaNo != null) {
            respuestaNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    guardarRespuesta("false");
                }
            });
        }

        inicializarVariables();
    }

    private void guardarRespuesta(String tipoRespuesta) {
        long local = SystemClock.elapsedRealtime();
        long tiempo = local - tiempo_inicio + tiempo_ejecutado;
        if (tiempo <= TIEMPO_NIVEL) {
//            if (nivel > 4) {
                if (respuesta.equals(tipoRespuesta)) {
                    sumarPuntos(1);
                } else {
                    sumarPuntos(-1);
                }
//            }
            try {
                cargarSiguienteNivel();
            } catch (Exception ex) {
                guardar();
            }
        } else {
            guardar();
        }
    }

    private void leerJson() {

//        if (nivel == 0) {
//            jsonString = JSONLoader.loadJSON(getResources().openRawResource(R.raw.busquedasimbolos));
//        }

        try {
//            JSONObject jsonRootObject = new JSONObject(jsonString);
//
//            //Get the instance of JSONArray that contains JSONObjects
//            JSONArray jsonArray = jsonRootObject.getJSONArray("simbolos");

            JSONArray jsonArray = new JSONArray(jsonString);

            if (nivel > jsonArray.length()) {
                guardar();
            }
            JSONObject jsonObject = jsonArray.getJSONObject(nivel);
            nivel++;
            respuesta = (jsonObject.optString("respuesta0").toString());
            String nivelSimbolo = "bs" + jsonObject.optString("pregunta").toString();
            String nivelMuestra = "m" + jsonObject.optString("pregunta").toString();
            int simb = getResources().getIdentifier(nivelSimbolo, "drawable", getPackageName());
            int muest = getResources().getIdentifier(nivelMuestra, "drawable", getPackageName());
            ImageView muestra = (ImageView) findViewById(R.id.muestra);
            ImageView imagesimbolo = (ImageView) findViewById(R.id.imageVSimbolos);
            if (imagesimbolo != null) {
                imagesimbolo.setImageResource(simb);
            }
            if (muestra != null) {
                muestra.setImageResource(muest);
            }
        } catch (JSONException e) {
            //Este metodo se tiene que llamar antes de salir del juego
            guardar();
        }

    }

    private void cargarSimbolosDB() {
        jsonString = DataBase.getJuego("simbolos");
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

