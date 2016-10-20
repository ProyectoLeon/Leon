package com.limeri.leon;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.limeri.leon.Models.AdministradorJuegos;
import com.limeri.leon.Models.Navegacion;
import com.limeri.leon.common.DataBase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class LetrasYNumerosActivity extends AppCompatActivity {

    private TextView palabra;
    private TextView seleccion;
    private int nivel = 0;
    private int cantIncorrectas = 0;
    private boolean puntPerfecto = false;
    private boolean ordenDirecto = true;
    private boolean respondido;
    private String jsonString;
    private int posSelecc;
    private Button reconocer;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letrasynumeros);

        //Configuro la base de datos
        cargarLetrasNumDB();

        reconocer = (Button) findViewById(R.id.reconocer);
        if (reconocer != null) {
            reconocer.setOnClickListener(reconocerAudio());
        }

        palabra = (TextView) findViewById(R.id.palabra);

        Navegacion.agregarMenuJuego(this);
        AdministradorJuegos.getInstance().inicializarJuego();

        //Llamo una funcion que se encarga de leer el archivo JSON
        leerJson();
    }

    private void leerJson() {
//        if (nivel == 0) {
//            jsonString = JSONLoader.loadJSON(getResources().openRawResource(R.raw.letrasynumeros));
//        }

        try {
//            JSONObject jsonRootObject = new JSONObject(jsonString);
//
//            //Get the instance of JSONArray that contains JSONObjects
//            JSONArray jsonArray = jsonRootObject.getJSONArray("letrasynumeros");

            JSONArray jsonArray = new JSONArray(jsonString);

            //Iterate the jsonArray and print the info of JSONObjects
            //for(int i=0; i < jsonArray.length(); i++){
            jsonObject = jsonArray.getJSONObject(nivel);

            String pregunta = jsonObject.getString("pregunta").toString();

            palabra.setVisibility(View.VISIBLE);
            palabra.setText(pregunta);

        }
        catch (JSONException e) {
            guardar();
        }
    }

    private void cargarLetrasNumDB() {
        jsonString = DataBase.getJuego("letrasynumeros");
    }

    private void sumarPuntos(Integer puntos) {
        AdministradorJuegos.getInstance().sumarPuntos(puntos);
    }

    private void guardarRespuesta() {
        if (respondido) {
            cantIncorrectas = 0;
            if (nivel >= 2) {
                sumarPuntos(1);
            }
            puntPerfecto = true;
        } else {
            cantIncorrectas++;
            puntPerfecto = false;
        }
        if (nivel <= 2 && cantIncorrectas > 0) {
            guardar();
            return;
        } else if ( cantIncorrectas >= 3 ) {
            if (((nivel - 1) % 3) == 0) {
                guardar();
                return;
            } else {
                nivel++;
            }
        } else if (nivel < 0) {
            guardar();
        } else {
            nivel++;
        }
        try {
            leerJson();
        } catch (Exception ex) {
            guardar();
        }
    }

    @Override
    public void onBackPressed() {
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
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String respuesta0 = jsonObject.optString("respuesta0").toString();
                    String respuesta1 = jsonObject.optString("respuesta1").toString();
                    respondido = false;
                    for (String audio : result) {
                        audio = audio.replaceAll(" ","");
                        audio = audio.toLowerCase();
                        audio = audio.replaceAll("[yi]","");
                        if ( !audio.equals(respuesta0) && !audio.equals(respuesta1) ) {
                            respondido = false;
                            if (mostrar) {
                                mostrar = false;
                            }
                        } else {
                            respondido = true;
//                            Toast.makeText(this,audio,Toast.LENGTH_LONG).show();
                            break;
                        }
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
                    dialog.setCancelable(false);

                }

            }
        }

    }

}
