package com.limeri.leon;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;

import com.limeri.leon.Models.AdministradorJuegos;
import com.limeri.leon.Models.Juego;
import com.limeri.leon.Models.Navegacion;
import com.limeri.leon.Models.Paciente;
import com.limeri.leon.common.JSONLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BqSimbolosActivity extends AppCompatActivity {


    private String pregunta;
    private String respuesta;
    private ImageView muestra;
    private ImageView imagesimbolo;
    private String respuestaSeleccionada = "";
    private int nivel = 0;
    private String jsonString;
    private int puntaje;
    private long tiempo_detenido;
    private Chronometer crono;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bq_simbolos);

        //BORRAR?   palabra = (TextView) findViewById(R.id.palabra);

        Navegacion.agregarMenuJuego(this);

        //Llamo una funcion que se encarga de leer el archivo JSON
        crono = (Chronometer) findViewById(R.id.cronometro);
        crono.setBase(SystemClock.elapsedRealtime());
        crono.start();

        leerJson();

    }

    private void leerJson() {

        if (nivel == 0) {
            jsonString = JSONLoader.loadJSON(getResources().openRawResource(R.raw.busquedasimbolos));
        nivel++;
        }

        try {
            JSONObject jsonRootObject = new JSONObject(jsonString);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.getJSONArray("simbolos");

            if (nivel > jsonArray.length()){
                guardar();
            }
            JSONObject jsonObject = jsonArray.getJSONObject(nivel);
            pregunta = (jsonObject.getString("pregunta").toString());
            respuesta = (jsonObject.optString("respuesta0").toString());
            muestra = (ImageView) findViewById(R.id.muestra);
            imagesimbolo = (ImageView) findViewById(R.id.imageVSimbolos);
            String nivelSimbolo = "bs"+nivel;
            String nivelMuestra = "m"+nivel;
            int simb = getResources().getIdentifier(nivelSimbolo,"drawable",getPackageName());
            int muest = getResources().getIdentifier(nivelMuestra, "drawable",getPackageName());
            imagesimbolo.setImageResource(simb);
            muestra.setImageResource(muest);

            Button respuestaSi = (Button) findViewById(R.id.buttonYes);
            if (respuestaSi != null) {
                respuestaSi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        long elapsedMillis = SystemClock.elapsedRealtime() - crono.getBase() - tiempo_detenido;
                        if (elapsedMillis/1000 < 120) {
                            if (respuesta.equals("true")){
                                sumarPuntos(1);
                            } else {
                                sumarPuntos(-1);
                            }
                            nivel++;
                            try {
                                leerJson();
                            } catch (Exception ex) {
                                guardar();
                            }
                        }else{
                            guardar();
                        }

                    }});
            }
            Button respuestaNo = (Button) findViewById(R.id.buttonNo);
            if (respuestaNo != null) {
                respuestaNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        long elapsedMillis = SystemClock.elapsedRealtime() - crono.getBase() - tiempo_detenido;
                        if (elapsedMillis/1000 < 120) {
                            if (respuesta.equals("false")) {
                                sumarPuntos(1);
                            } else {
                                sumarPuntos(-1);
                            }
                            nivel++;
                            try {
                                leerJson();
                            } catch (Exception ex) {
                                guardar();
                            }
                        }else {
                            guardar();
                        }

                    }});
            }


        } catch (JSONException e) {
            //Este metodo se tiene que llamar antes de salir del juego
            guardar();
        }

    }

    private void sumarPuntos(Integer puntos) {
        Juego juego = Paciente.getSelectedPaciente().getEvaluacionActual().getJuegoActual();
        juego.setPuntosJuego(juego.getPuntosJuego() + puntos);
    }

    private void guardar() {
        AdministradorJuegos.getInstance().guardarJuego(this);
    }
}

