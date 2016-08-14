package com.limeri.leon;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;

import com.limeri.leon.Models.AdministradorJuegos;
import com.limeri.leon.Models.Navegacion;
import com.limeri.leon.common.JSONLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class BqSimbolosActivity extends AppCompatActivity {


    private Button respuestaSi;
    private Button respuestaNo;
    private String pregunta;
    private String respuesta;
    private ImageView imagesimbolo;
    private String respuestaSeleccionada = "";
    private int nivel = 0;
    private int cantIncorrectas = 0;
    private int cantCorrectas = 0;
    private String jsonString;
    private int puntaje;
    private Chronometer crono;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bq_simbolos);

        //BORRAR?   palabra = (TextView) findViewById(R.id.palabra);

//CONFIGURACION DEL BOTON DE CANCELAR JUEGO - Comienza aquí
        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.action_bar,
                null);

        getSupportActionBar().setCustomView(actionBarLayout);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        Button boton =(Button) getSupportActionBar().getCustomView().findViewById(R.id.boton_actionbar);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(BqSimbolosActivity.this)
                        .setTitle("Popup")
                        .setMessage("Por favor seleccione opción")
                        .setPositiveButton("Guardar y Finalizar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                guardar();
                            }
                        })
                        .setNegativeButton("Reiniciar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        })
                        .setNeutralButton("Seleccionar Juego Alternativo", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cancelar();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        //Cancelar juego - Finaliza aqui

        //Llamo una funcion que se encarga de leer el archivo JSON
        crono = (Chronometer) findViewById(R.id.cronometro);
        crono.setBase(SystemClock.elapsedRealtime());
        crono.start();

        leerJson();

    }

    private void leerJson() {

        if (nivel == 0) {
            jsonString = JSONLoader.loadJSON(getResources().openRawResource(R.raw.busquedasimbolos));
        }

        try {
            JSONObject jsonRootObject = new JSONObject(jsonString);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.getJSONArray("simbolos");

            if (nivel > jsonArray.length()){
                guardar();
            }
            //Iterate the jsonArray and print the info of JSONObjects
            //for(int i=0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(nivel);

            pregunta = (jsonObject.getString("pregunta").toString());
            respuesta = (jsonObject.optString("respuesta0").toString());

            imagesimbolo = (ImageView) findViewById(R.id.imageVSimbolos);
            String nivelImagen = "bs"+nivel;
            int simb = getResources().getIdentifier(nivelImagen,"drawable",getPackageName());
            imagesimbolo.setImageResource(simb);

            respuestaSi = (Button) findViewById(R.id.buttonYes);
            respuestaSi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long elapsedMillis = SystemClock.elapsedRealtime() - crono.getBase();
                    if (elapsedMillis/1000 < 120) {
                        if (respuesta.equals("true")){
                            cantCorrectas ++;
                        } else {
                            cantIncorrectas ++;
                        };
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
            respuestaNo = (Button) findViewById(R.id.buttonNo);
            respuestaNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long elapsedMillis = SystemClock.elapsedRealtime() - crono.getBase();
                    if (elapsedMillis/1000 < 120) {
                        if (respuesta.equals("false")) {
                            cantCorrectas++;
                        } else {
                            cantIncorrectas++;
                        }
                        ;
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



            //respuestas = (ListView) findViewById(R.id.respuestas);
            //respuestas.setOnItemClickListener(opcionSeleccionada());
            //respuestas.setAdapter(adapter);

        } catch (JSONException e) {
            //Este metodo se tiene que llamar antes de salir del juego
            guardar();
        }

    }

    private void guardar() {
        try {
            AdministradorJuegos.getInstance().guardarJuego(cantCorrectas - cantIncorrectas, this);
            Navegacion.volver(this, InicioJuegoActivity.class);
        } catch (Exception e) {
            Navegacion.volver(this, ExamenActivity.class);
        }
    }

    private void cancelar() {
        try {
            AdministradorJuegos.getInstance().cancelarJuego(this);
            Navegacion.volver(this, InicioJuegoActivity.class);
        } catch (Exception e) {
            Navegacion.volver(this, ExamenActivity.class);
        }
    }
}

