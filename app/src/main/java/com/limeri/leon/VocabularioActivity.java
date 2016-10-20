package com.limeri.leon;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.limeri.leon.Models.AdministradorJuegos;
import com.limeri.leon.Models.Navegacion;
import com.limeri.leon.common.DataBase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VocabularioActivity extends AppCompatActivity {

    private TextView palabra;
    private TextView seleccion;
    private int nivel = 4; // Consideramos los niveles 0 a 3 (gráficos)
    private int nivelAnterior = 4;
    private int nivelErroneo = 0;
    private int cantIncorrectas = 0;
    private int cantIncorrectasAnt = 0;
    private int cantConsec = 0;
    private int cantConsecAnt = 0;
    private boolean puntPerfecto = false;
    private boolean jsonLoaded = false;
    private boolean soloImagen = false;
    private boolean backHecho = false;
    private String jsonString;
    private String jsonParciales;
    private TextView parciales;
    private String parcial1;
    private String parcial2;
    private String parcial;
    private ImageView imagen;
    private int posSelecc = -1;
    private int longArray;
    private ProgressBar progBar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulario);

        //Configuro la base de datos
        cargarVocabularioDB();

        Button siguiente = (Button) findViewById(R.id.siguiente);
        if (siguiente != null) {
            siguiente.setOnClickListener(clickSiguiente());
        }

        palabra = (TextView) findViewById(R.id.palabra);
        imagen = (ImageView) findViewById(R.id.imagen);
        parciales = (TextView) findViewById(R.id.parciales);
        progBar = (ProgressBar)findViewById(R.id.progBar);

        Navegacion.agregarMenuJuego(this);
        AdministradorJuegos.getInstance().inicializarJuego();

        //Llamo una funcion que se encarga de leer el archivo JSON
        leerJson();
        leerParciales();
    }

    private void leerParciales() {

//        if (nivel == 4) {
//            jsonParciales = JSONLoader.loadJSON(getResources().openRawResource(R.raw.parciales));
//        }

        try {
//            JSONObject jsonRootObject = new JSONObject(jsonParciales);
//            JSONArray jsonArray = jsonRootObject.getJSONArray("parciales");
            JSONArray jsonArray = new JSONArray(jsonParciales);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            parcial1 = jsonObject.getString("parcial1").toString();
            parcial2 = jsonObject.getString("parcial2").toString();

            parcial = 0 + parcial1 + (longArray - nivel) + parcial2;

            parciales.setText(parcial);

            progBar.setMax(longArray);
            progBar.setProgress(nivel);

        } catch (JSONException e) {
            guardar();
        }
    }

    private void actualizarParciales() {
        parcial = obtenerPuntos() + parcial1 + (longArray - nivel) + parcial2;
        parciales.setText(parcial);

        progBar.setMax(longArray);
        progBar.setProgress(nivel);

        if (soloImagen){
            parciales.setVisibility(View.GONE);
            progBar.setVisibility(View.GONE);
        } else {
            parciales.setVisibility(View.VISIBLE);
            progBar.setVisibility(View.VISIBLE);
        }
    }

    private void leerJson() {
//        if ((nivel == 4) & (!jsonLoaded)) {
//            jsonString = JSONLoader.loadJSON(getResources().openRawResource(R.raw.preguntasvocabulario));
//            jsonLoaded = true;
//        }

        try {
//            JSONObject jsonRootObject = new JSONObject(jsonString);
//
//            //Get the instance of JSONArray that contains JSONObjects
//            JSONArray jsonArray = jsonRootObject.getJSONArray("vocabulario");

            JSONArray jsonArray = new JSONArray(jsonString);

            longArray = jsonArray.length();

            //Iterate the jsonArray and print the info of JSONObjects
            //for(int i=0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(nivel);

            String pregunta = jsonObject.getString("pregunta").toString();

            if (nivel < 4) {
                palabra.setVisibility(View.GONE);
                imagen.setVisibility(View.VISIBLE);
                imagen.setImageResource(getResources().getIdentifier(pregunta, "drawable", this.getPackageName()));
            } else {
                imagen.setVisibility(View.GONE);
                palabra.setVisibility(View.VISIBLE);
                palabra.setText(pregunta);
            }
            if (nivel < 4) {
                if (!soloImagen) {
                    String[] listRespuestas = {(jsonObject.optString("respuesta0").toString()), (jsonObject.optString("respuesta1").toString())};
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listRespuestas);
                    ListView respuestas = (ListView) findViewById(R.id.respuestas);
                    if (respuestas != null) {
                        respuestas.setOnItemClickListener(opcionSeleccionada());
                        respuestas.setAdapter(adapter);
                    }
                } else {
                    String[] listRespuestas = {};
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listRespuestas);
                    ListView respuestas = (ListView) findViewById(R.id.respuestas);
                    if (respuestas != null) {
                        respuestas.setOnItemClickListener(opcionSeleccionada());
                        respuestas.setAdapter(adapter);
                    }
                }
            } else {
                String[] listRespuestas = {(jsonObject.optString("respuesta0").toString()), (jsonObject.optString("respuesta1").toString()),
                        (jsonObject.optString("respuesta2").toString())};
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listRespuestas);
                ListView respuestas = (ListView) findViewById(R.id.respuestas);
                if (respuestas != null) {
                    respuestas.setOnItemClickListener(opcionSeleccionada());
                    respuestas.setAdapter(adapter);
                }
            }
        } catch (JSONException e) {
            guardar();
        }
    }

    private AdapterView.OnItemClickListener opcionSeleccionada() {

        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (seleccion != null) blanquear(seleccion);
                seleccion = ((TextView) view);
                seleccionar(seleccion);

                posSelecc = position;
            }
        };
    }

    private void cargarVocabularioDB() {
        jsonString = DataBase.getJuego("vocabulario");
        jsonParciales = DataBase.getJuego("parciales");
    }

    private void sumarPuntos(Integer puntos) {
        AdministradorJuegos.getInstance().sumarPuntos(puntos);
    }

    private Integer obtenerPuntos() {
        return AdministradorJuegos.getInstance().obtenerPuntos();
    }

    private void restarPuntos(Integer puntos) {
        AdministradorJuegos.getInstance().restarPuntos(puntos);
    }

    private void guardarPuntosNivel(Integer nivel, Integer puntos) {
        AdministradorJuegos.getInstance().guardarPuntosNivel(nivel, puntos);
    }

    private Integer getPuntosNivel(Integer nivel) {
        return AdministradorJuegos.getInstance().getPuntosNivel(nivel);
    }

    private void seleccionar(TextView view) {
        view.setTextColor(Color.RED);
    }

    private void blanquear(TextView view) {
        if (view != null) {
            view.setTextColor(Color.BLACK);
        }
    }

    private View.OnClickListener clickSiguiente() {

        return new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (posSelecc != -1 || soloImagen) {
                    if (!soloImagen) {
                        // Si no obtiene puntuación perfecta en algunos de los primeros dos, sucuencia inversa hasta que acierta 2 seguidos.
                        if (nivel < 4) {
                            if (posSelecc == 1) {
                                cantIncorrectasAnt = cantIncorrectas;
                                cantIncorrectas++;
                                cantConsecAnt = cantConsec;
                                cantConsec = 0;
                                guardarPuntosNivel(nivel, 0);
                                puntPerfecto = false;
                            } else {
                                cantIncorrectasAnt = cantIncorrectas;
                                cantIncorrectas = 0;
                                sumarPuntos(1);
                                guardarPuntosNivel(nivel, 1);
                                puntPerfecto = true;
                            }
                        } else {
                            if (posSelecc == 2) {
                                cantIncorrectasAnt = cantIncorrectas;
                                cantIncorrectas++;
                                guardarPuntosNivel(nivel, 0);
                                puntPerfecto = false;
                            } else if (posSelecc == 1) {
                                cantIncorrectasAnt = cantIncorrectas;
                                cantIncorrectas = 0;
                                sumarPuntos(1);
                                guardarPuntosNivel(nivel, 1);
                                puntPerfecto = false;
                            } else {
                                cantIncorrectasAnt = cantIncorrectas;
                                cantIncorrectas = 0;
                                sumarPuntos(2);
                                guardarPuntosNivel(nivel, 2);
                                puntPerfecto = true;
                            }
                        }
                    }
                    try {
                        guardarRespuesta();
                        seleccion = null;
                        posSelecc = -1;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };
    }

    private void guardarRespuesta() {
        if (nivel < 4 & soloImagen) {
            soloImagen = false;
        } else {
            //Faltaría guardar la respuesta en la base de datos
            blanquear(seleccion);
            if (cantIncorrectas == 5) {
                guardar();
                return;
            } else if ((nivel == 4 | nivel == 5) & !puntPerfecto & !backHecho) {
                nivelErroneo = nivel;
                nivelAnterior = nivel;
                nivel = 3;
                soloImagen = true;
            } else if (nivel < 4 & cantIncorrectas > 0) {
                nivelAnterior = nivel;
                nivel--;
                soloImagen = true;
            } else if (nivel < 4 & cantIncorrectas == 0) {
                cantConsecAnt = cantConsec;
                cantConsec++;
                if (cantConsec == 2) {
                    nivelAnterior = nivel;
                    nivel = nivelErroneo + 1;
                    backHecho = true;
                } else {
                    nivelAnterior = nivel;
                    nivel--;
                    soloImagen = true;
                }
            } else if (nivel < 0) {
                guardar();
            } else {
                nivelAnterior = nivel;
                nivel++;
            }
        }
        try {
            actualizarParciales();
            leerJson();
        } catch (Exception ex) {
            guardar();
        }
    }

    @Override
    public void onBackPressed() {
        volverAtras();
    }

    private void volverAtras() {
        blanquear(seleccion);
        if ((nivel > 0) & (nivel != nivelAnterior) & !soloImagen) {
            nivel = nivelAnterior;
            cantConsec = cantConsecAnt;
            cantIncorrectas = cantIncorrectasAnt;
            restarPuntos(getPuntosNivel(nivel));
            try {
                actualizarParciales();
                leerJson();
            } catch (Exception ex) {
                guardar();
            }
            posSelecc = -1;
        }
    }

    private void guardar() {
        AdministradorJuegos.getInstance().guardarJuego(this);
    }

}
