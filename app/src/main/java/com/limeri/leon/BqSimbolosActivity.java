package com.limeri.leon;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
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

public class BqSimbolosActivity extends AppCompatActivity {


    private Button respuestaSi;
    private Button respuestaNo;
    private String pregunta;
    private String respuesta;
    private ImageView muestra;
    private ImageView imagesimbolo;
    private String respuestaSeleccionada = "";
    private int nivel = 0;
    private int cantIncorrectas = 0;
    private int cantCorrectas = 0;
    private String jsonString;
    private int puntaje;
    private long tiempo_detenido;
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
                //TODO: Agregar la lógica de cancelar juego en NAVEGACIÓN
                crono.stop();
                final long tiempo_ejecutado = SystemClock.elapsedRealtime() - crono.getBase();
                AlertDialog.Builder builder = new AlertDialog.Builder(BqSimbolosActivity.this);

                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alert_dialog,null);

                builder.setView(dialogView);

                Button btn_positive = (Button) dialogView.findViewById(R.id.dialog_positive_btn);
                Button btn_negative = (Button) dialogView.findViewById(R.id.dialog_negative_btn);
                Button btn_neutral = (Button) dialogView.findViewById(R.id.dialog_neutral_btn);

                final AlertDialog dialog = builder.create();

                btn_positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        guardar();
                    }
                });

                btn_negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelar();
                    }
                });

                btn_neutral.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    //TODO: Revisar lógica del crono cuando se pausa el juego y se retoma.
                    tiempo_detenido = SystemClock.elapsedRealtime() - crono.getBase() - tiempo_ejecutado;
                        crono.start();
                        dialog.cancel();
                    }
                });

                // Display the custom alert dialog on interface
                dialog.show();


                /**    .setTitle("Popup")
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
                 */
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

            respuestaSi = (Button) findViewById(R.id.buttonYes);
            respuestaSi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long elapsedMillis = SystemClock.elapsedRealtime() - crono.getBase() - tiempo_detenido;
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
                    long elapsedMillis = SystemClock.elapsedRealtime() - crono.getBase() - tiempo_detenido;
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



        } catch (JSONException e) {
            //Este metodo se tiene que llamar antes de salir del juego
            guardar();
        }

    }

    private void guardar() {
        try {
            AdministradorJuegos.getInstance().guardarJuego(cantCorrectas - cantIncorrectas, this);
            Navegacion.irA(this, InicioJuegoActivity.class);
        } catch (Exception e) {
            Navegacion.irA(this, ExamenActivity.class);
        }
    }

    private void cancelar() {
        try {
            AdministradorJuegos.getInstance().cancelarJuego(this);
            Navegacion.irA(this, InicioJuegoActivity.class);
        } catch (Exception e) {
            Navegacion.irA(this, ExamenActivity.class);
        }
    }
}

