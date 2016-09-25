package com.limeri.leon;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.speech.RecognizerIntent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.limeri.leon.Models.AdministradorJuegos;
import com.limeri.leon.Models.Navegacion;
import com.limeri.leon.common.DataBase;
import com.limeri.leon.common.JSONLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class AritmeticaActivity extends AppCompatActivity {

    public static final int TIEMPO_NIVEL = 30000; //En milisegundos
    private TextView palabra;
    private int nivel = 2; // Niveles 0 a 4: gráficos
    private int nivelErroneo = 0;
    private int cantIncorrectas = 0;
    private int cantConsec = 0;
    private boolean puntPerfecto = false;
    private boolean jsonLoaded = false;
    private boolean backHecho = false;
    private boolean reconocerAudio = false;
    private boolean respondido;
    private String jsonString;
    private ImageView imagen;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private JSONObject jsonObject;
    private Chronometer crono;
    private long tiempo_ejecutado = 0;
    private long tiempo_inicio;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            respondido = false;
            guardarRespuesta();
//            iniciarCronometro();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aritmetica);

        //Configuro la base de datos
        cargarAritmeticaDB();

        Button reconocer = (Button) findViewById(R.id.reconocer);
        if (reconocer != null) {
            reconocer.setOnClickListener(reconocerAudio());
        }
        palabra = (TextView) findViewById(R.id.palabra);
        imagen = (ImageView) findViewById(R.id.imagen);
        crono = (Chronometer) findViewById(R.id.cronometro);

        Navegacion.agregarMenuJuego(this);
        AdministradorJuegos.getInstance().inicializarJuego();

        //Llamo una funcion que se encarga de leer el archivo JSON
        inicializarVariables();
    }

    private void leerJson() {
//        if ((nivel == 2) & (!jsonLoaded)) {
//            jsonString = JSONLoader.loadJSON(getResources().openRawResource(R.raw.preguntasaritmetica));
//            jsonLoaded = true;
//        }

        try {
//            JSONObject jsonRootObject = new JSONObject(jsonString);
//
//            //Get the instance of JSONArray that contains JSONObjects
//            JSONArray jsonArray = jsonRootObject.getJSONArray("aritmetica");

            JSONArray jsonArray = new JSONArray(jsonString);

            //Iterate the jsonArray and print the info of JSONObjects
            //for(int i=0; i < jsonArray.length(); i++){
            jsonObject = jsonArray.getJSONObject(nivel);

            String pregunta = jsonObject.getString("pregunta").toString();

            if (nivel < 5) {
                String dibujo = jsonObject.getString("dibujo").toString();
                palabra.setVisibility(View.VISIBLE);
                palabra.setText(pregunta);
                imagen.setVisibility(View.VISIBLE);
                imagen.setImageResource(getResources().getIdentifier(dibujo, "drawable", this.getPackageName()));
            } else {
                imagen.setVisibility(View.GONE);
                palabra.setVisibility(View.VISIBLE);
                palabra.setText(pregunta);
            }
        } catch (JSONException e) {
            guardar();
        }
    }

    private void guardarRespuesta() {
        //Faltaría guardar la respuesta en la base de datos
        //blanquear(seleccion);
        if (respondido) {
            cantIncorrectas = 0;
            sumarPuntos(1);
            puntPerfecto = true;
        } else {
            cantIncorrectas++;
            cantConsec = 0;
            puntPerfecto = false;
        }
        if (cantIncorrectas == 4) {
            guardar();
        } else if ((nivel == 2 | nivel == 3) & !puntPerfecto & !backHecho) {
            nivelErroneo = nivel;
            nivel = 1;
            backHecho = true;
        } else if (nivel < 2 & cantIncorrectas > 0) {
            nivel--;
        } else if (nivel < 2 & cantIncorrectas == 0) {
            cantConsec++;
            if (cantConsec == 2) {
                nivel = nivelErroneo + 1;
            } else {
                nivel--;
            }
        } else if (nivel < 0) {
            guardar();
        } else {
            nivel++;
        }
        try {
            cargarSiguienteNivel();
        } catch (Exception ex) {
            guardar();
        }
    }

    private void cargarAritmeticaDB() {
        jsonString = DataBase.cargarJuego("aritmetica");
    }

    private void cargarSiguienteNivel() {
        inicializarVariables();
        iniciarCronometro();
    }

    private void inicializarVariables() {
        tiempo_ejecutado = 0;
        leerJson();
        reconocerAudio = false;
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
        if (!reconocerAudio) {
            iniciarCronometro();
        }
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

    private void sumarPuntos(Integer puntos) {
        AdministradorJuegos.getInstance().sumarPuntos(puntos);
    }

    private void guardar() {
        AdministradorJuegos.getInstance().guardarJuego(this);
    }

    private View.OnClickListener reconocerAudio() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Hable");
                reconocerAudio = true;
                try {
                    startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(),"No",
                    Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Boolean mostrar = true;
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String respuesta = jsonObject.optString("respuesta0").toString();
                    respondido = false;
                    handler.removeCallbacks(runnable);
                    long local = SystemClock.elapsedRealtime();
                    long tiempo = local - tiempo_inicio + tiempo_ejecutado;
                    for (String audio : result){
                        audio = audio.replaceAll(" ","");
                        audio = audio.replaceAll("[^\\.0123456789]","");
                        if (!audio.equals(respuesta) || (tiempo > TIEMPO_NIVEL)){
                            respondido = false;
                            if (mostrar) {
                                Toast.makeText(this,audio,Toast.LENGTH_LONG).show();
                                mostrar = false;
                            }
                        } else {
                            respondido = true;
//                            cantIncorrectas = 0;
//                            sumarPuntos(1);
//                            puntPerfecto = true;
                            Toast.makeText(this,audio,Toast.LENGTH_LONG).show();
                            break;
                        }
                    }
                    if (respondido == false){
//                        cantIncorrectas++;
//                        cantConsec = 0;
//                        puntPerfecto = false;
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    if (respondido) {
                        builder.setTitle("La respuesta se interpretó correcta, ¿está de acuerdo?");
                    } else {
                        builder.setTitle("La respuesta se interpretó incorrecta, ¿está de acuerdo?");
                    }
                    builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            try {
                                guardarRespuesta();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            respondido = !respondido;
                            try {
                                guardarRespuesta();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

//                    try {
//                        guardarRespuesta();
//                        //seleccion = null;
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                    }
                }
            }
        }
    }
}
