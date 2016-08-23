package com.limeri.leon;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.limeri.leon.Models.AdministradorJuegos;
import com.limeri.leon.Models.Navegacion;
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
    private int cantIncorrectas = 0;
    private String jsonString;
    private int posSelecc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semejanzas);

        Button siguiente = (Button) findViewById(R.id.siguiente);
        if (siguiente != null) {
            siguiente.setOnClickListener(clickSiguiente());
        }

        palabra = (TextView) findViewById(R.id.palabra);

        Navegacion.agregarMenuJuego(this);
        AdministradorJuegos.getInstance().inicializarJuego();

        //Llamo una funcion que se encarga de leer el archivo JSON
        leerJson();

    }

    private void leerJson() {

        if (nivel == PRIMER_NIVEL) {
            jsonString = JSONLoader.loadJSON(getResources().openRawResource(R.raw.preguntassemejanzas));
        }

        try {
            JSONObject jsonRootObject = new JSONObject(jsonString);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.getJSONArray("semejanzas");

            //Iterate the jsonArray and print the info of JSONObjects
            //for(int i=0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(nivel);

            palabra.setText(jsonObject.getString("pregunta").toString());
            String[] listRespuestas = {(jsonObject.optString("respuesta0").toString()), (jsonObject.optString("respuesta1").toString()),
                    (jsonObject.optString("respuesta2").toString())};
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

    private void sumarPuntos(Integer puntos) {
        AdministradorJuegos.getInstance().sumarPuntos(puntos);
    }

    private void seleccionar(TextView view) {
        view.setTextColor(Color.RED);
    }

    private void blanquear(TextView view) {
        view.setTextColor(Color.BLACK);
    }

    private View.OnClickListener clickSiguiente() {

        return new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (posSelecc == 2){
                    cantIncorrectas++;
                } else if (posSelecc == 1){

                    // Para los dos primeros niveles, no hay tercera opción.
                    // La segunda opción suma 0 puntos pero NO suma respuestas incorrectas.
                    if (!(nivel == 0 || nivel == 1)){
                        sumarPuntos(1);
                    }
                    cantIncorrectas = 0;
                }
                else if (posSelecc == 0) {
                    cantIncorrectas = 0;

                    // Para los dos primeros niveles, no hay tercera opción.
                    // La primera opción suma 1 punto.
                    if (nivel == 0 || nivel == 1) {
                        sumarPuntos(1);
                    } else {
                        sumarPuntos(2);
                    }
                }

                try {
                    guardarRespuesta();
                    seleccion = null;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
    }

    private void guardarRespuesta() {
        //Faltaría guardar la respuesta en la base de datos
        blanquear(seleccion);
        if (cantIncorrectas == 5) {
            guardar();
        } else {
            nivel++;}

        if (nivel == ULTIMO_NIVEL){
            guardar();
        } else {

            try {
                leerJson();
            } catch (Exception ex) {
                guardar();
            }
        }
    }

    @Override
    public void onBackPressed() {
    }

    private void guardar() {
        AdministradorJuegos.getInstance().guardarJuego(this);
    }
}

