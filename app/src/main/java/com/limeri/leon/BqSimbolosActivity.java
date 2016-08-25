package com.limeri.leon;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;

import com.limeri.leon.Models.AdministradorJuegos;
import com.limeri.leon.Models.Navegacion;
import com.limeri.leon.common.JSONLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BqSimbolosActivity extends AppCompatActivity {

    private String respuesta;
    private int nivel = 0;
    private String jsonString;
    private long tiempo_ejecutado = 0;
    private Chronometer crono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bq_simbolos);

        Navegacion.agregarMenuJuego(this);
        AdministradorJuegos.getInstance().inicializarJuego();

        iniciarCronometro();
        //Llamo una funcion que se encarga de leer el archivo JSON
        leerJson();

    }

    private void iniciarCronometro() {
        crono = (Chronometer) findViewById(R.id.cronometro);
        crono.setBase(SystemClock.elapsedRealtime());
        crono.start();
    }

    private void leerJson() {

        if (nivel == 0) {
            jsonString = JSONLoader.loadJSON(getResources().openRawResource(R.raw.busquedasimbolos));
        }

        try {
            JSONObject jsonRootObject = new JSONObject(jsonString);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.getJSONArray("simbolos");

            if (nivel > jsonArray.length()) {
                guardar();
            }
            JSONObject jsonObject = jsonArray.getJSONObject(nivel);
            nivel++;
            respuesta = (jsonObject.optString("respuesta0").toString());
            ImageView muestra = (ImageView) findViewById(R.id.muestra);
            ImageView imagesimbolo = (ImageView) findViewById(R.id.imageVSimbolos);
            String nivelSimbolo = "bs" + nivel;
            final String nivelMuestra = "m" + nivel;
            int simb = getResources().getIdentifier(nivelSimbolo, "drawable", getPackageName());
            int muest = getResources().getIdentifier(nivelMuestra, "drawable", getPackageName());
            if (imagesimbolo != null) {
                imagesimbolo.setImageResource(simb);
            }
            if (muestra != null) {
                muestra.setImageResource(muest);
            }

            Button respuestaSi = (Button) findViewById(R.id.buttonYes);
            if (respuestaSi != null) {
                respuestaSi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        long elapsedMillis = SystemClock.elapsedRealtime() - crono.getBase() + tiempo_ejecutado;
                        if (elapsedMillis / 1000 < 120) {
                            //Está funcionando OK, solo que los primeros 4 niveles son de prueba.
                            if (nivel>4){
                            if (respuesta.equals("true") ) {
                                sumarPuntos(1);
                            } else {
                                sumarPuntos(-1);
                            }}

                            try {
                                leerJson();
                            } catch (Exception ex) {
                                guardar();
                            }
                        } else {
                            guardar();
                        }

                    }
                });
            }
            Button respuestaNo = (Button) findViewById(R.id.buttonNo);
            if (respuestaNo != null) {
                respuestaNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        long elapsedMillis = SystemClock.elapsedRealtime() - crono.getBase() + tiempo_ejecutado;
                        //Está funcionando OK, solo que los primeros 4 niveles son de prueba.
                        if (elapsedMillis / 1000 < 120) {
                            if (nivel>4){
                            if (respuesta.equals("false") ) {
                                sumarPuntos(1);
                            } else {
                                sumarPuntos(-1);
                            }}
                            try {
                                leerJson();
                            } catch (Exception ex) {
                                guardar();
                            }
                        } else {
                            guardar();
                        }

                    }
                });
            }


        } catch (JSONException e) {
            //Este metodo se tiene que llamar antes de salir del juego
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
    }

    @Override
    public void onResume(){
        super.onResume();
        iniciarCronometro();

    }

    @Override
    public void onPause(){
        super.onPause();
        pararCronometro();
    }

    private void pararCronometro() {
        crono.stop();
        tiempo_ejecutado = tiempo_ejecutado + SystemClock.elapsedRealtime() - crono.getBase();
    }
}

