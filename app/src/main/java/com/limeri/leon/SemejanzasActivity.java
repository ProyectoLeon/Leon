package com.limeri.leon;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.limeri.leon.Models.AdministradorJuegos;
import com.limeri.leon.Models.Navegacion;
import com.limeri.leon.common.DataBase;
import com.limeri.leon.common.JSONLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SemejanzasActivity extends AppCompatActivity {


    private static final int ULTIMO_NIVEL = 8;
    public static final int PRIMER_NIVEL = 0;
    private TextView palabra;
    private TextView seleccion;
    private int nivel = PRIMER_NIVEL;
    private int nivelAnterior = PRIMER_NIVEL;
    private int cantIncorrectas = 0;
    private int cantIncorrectasAnt = 0;
    private String jsonString;
    private String jsonParciales;
    private TextView parciales;
    private String parcial1;
    private String parcial2;
    private String parcial;
    private int posSelecc = -1;
    private int longArray;
    private ProgressBar progBar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semejanzas);

        //Configuro la base de datos
        cargarSemejanzasDB();

        Button siguiente = (Button) findViewById(R.id.siguiente);
        if (siguiente != null) {
            siguiente.setOnClickListener(clickSiguiente());
        }

        palabra = (TextView) findViewById(R.id.palabra);
        parciales = (TextView) findViewById(R.id.parciales);
        progBar = (ProgressBar)findViewById(R.id.progBar);

        Navegacion.agregarMenuJuego(this);
        AdministradorJuegos.getInstance().inicializarJuego();

        //Llamo una funcion que se encarga de leer el archivo JSON
        leerJson();

        leerParciales();

    }

    private void leerParciales() {

//        if (nivel == PRIMER_NIVEL) {
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
    }

    private void leerJson() {

        String[] listRespuestas;
//        if (nivel == PRIMER_NIVEL) {
//            jsonString = JSONLoader.loadJSON(getResources().openRawResource(R.raw.preguntassemejanzas));
//        }

        try {
//            JSONObject jsonRootObject = new JSONObject(jsonString);
//
//            //Get the instance of JSONArray that contains JSONObjects
//            JSONArray jsonArray = jsonRootObject.getJSONArray("semejanzas");

            JSONArray jsonArray = new JSONArray(jsonString);

            longArray = jsonArray.length();

            //Iterate the jsonArray and print the info of JSONObjects
            //for(int i=0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(nivel);

            palabra.setText(jsonObject.getString("pregunta").toString());
            if (nivel < 3) {
                listRespuestas = new String[]{(jsonObject.optString("respuesta0").toString()), (jsonObject.optString("respuesta1").toString())};
            }else{
                listRespuestas = new String[]{(jsonObject.optString("respuesta0").toString()), (jsonObject.optString("respuesta1").toString()),
                        (jsonObject.optString("respuesta2").toString())};
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, listRespuestas);

            ListView respuestas = (ListView) findViewById(R.id.respuestas);
            if (respuestas != null) {
                respuestas.setOnItemClickListener(opcionSeleccionada());
                respuestas.setAdapter(adapter);
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

    private void cargarSemejanzasDB() {
        jsonString = DataBase.cargarJuego("semejanzas");
        jsonParciales = DataBase.cargarJuego("parciales");
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
                if (posSelecc != -1) {
                    if (posSelecc == 2) {
                        cantIncorrectasAnt = cantIncorrectas;
                        cantIncorrectas++;
                        guardarPuntosNivel(nivel, 0);
                    } else if (posSelecc == 1) {

                        // Para los dos primeros niveles, no hay tercera opción.
                        // La segunda opción suma 0 puntos y suma respuestas incorrectas.
                        if (nivel == 0 || nivel == 1) {
                            cantIncorrectasAnt = cantIncorrectas;
                            cantIncorrectas++;
                            guardarPuntosNivel(nivel, 0);
                        } else {
                            sumarPuntos(1);
                            guardarPuntosNivel(nivel, 1);
                            cantIncorrectasAnt = cantIncorrectas;
                            cantIncorrectas = 0;
                        }

                    } else if (posSelecc == 0) {
                        cantIncorrectasAnt = cantIncorrectas;
                        cantIncorrectas = 0;

                        // Para los dos primeros niveles, no hay tercera opción.
                        // La primera opción suma 1 punto.
                        if (nivel == 0 || nivel == 1) {
                            sumarPuntos(1);
                            guardarPuntosNivel(nivel, 1);
                        } else {
                            sumarPuntos(2);
                            guardarPuntosNivel(nivel, 2);
                        }
                    }

                    try {
                        guardarRespuesta();
                        seleccion = null;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };
    }

    private void guardarRespuesta() {
        //Faltaría guardar la respuesta en la base de datos
        blanquear(seleccion);
        if (cantIncorrectas == 5) {
            guardar();
            return;
        } else {
            nivelAnterior = nivel;
            nivel++;
        }

        if (nivel == longArray){
            guardar();
        } else {

            try {
                actualizarParciales();
                leerJson();
            } catch (Exception ex) {
                guardar();
            }
            posSelecc = -1;
        }
    }

    @Override
    public void onBackPressed() {
        volverAtras();
    }

    private void volverAtras() {
        blanquear(seleccion);
        if ((nivel > 0) & (nivel != nivelAnterior)) {
            nivel = nivelAnterior;
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

