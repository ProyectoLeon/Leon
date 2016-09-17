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
import com.limeri.leon.common.JSONLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdivinanzasActivity extends AppCompatActivity {

    private TextView palabra;
    private TextView seleccion;
    private int nivel = 0;
    private int cantIncorrectas = 0;
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
        setContentView(R.layout.activity_adivinanzas);

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

        if (nivel == 0) {
            jsonParciales = JSONLoader.loadJSON(getResources().openRawResource(R.raw.parciales));
        }

        try {
            JSONObject jsonRootObject = new JSONObject(jsonParciales);
            JSONArray jsonArray = jsonRootObject.getJSONArray("parciales");
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

        if (nivel == 0) {
            jsonString = JSONLoader.loadJSON(getResources().openRawResource(R.raw.preguntasadivinanzas));
        }

        try {
            JSONObject jsonRootObject = new JSONObject(jsonString);

            JSONArray jsonArray = jsonRootObject.getJSONArray("adivinanzas");

            longArray = jsonArray.length();
            JSONObject jsonObject = jsonArray.getJSONObject(nivel);

            palabra.setText(jsonObject.getString("pregunta").toString());
            String[] listRespuestas = {(jsonObject.optString("respuesta0").toString()), (jsonObject.optString("respuesta1").toString())};
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

    private Integer obtenerPuntos() {
        return AdministradorJuegos.getInstance().obtenerPuntos();
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
                    if (posSelecc == 1) {
                        cantIncorrectas++;
                    } else {
                        cantIncorrectas = 0;
                        sumarPuntos(1);
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
        blanquear(seleccion);
        if (cantIncorrectas== 5) {
            guardar();
            // RETROGRESION NO TIENE ESTE JUEGO
        } else {
            nivel++;
        }
        try {
            actualizarParciales();
            leerJson();
        } catch (Exception ex) {
            guardar();
        }

        posSelecc = -1;
    }

    @Override
    public void onBackPressed() {
    }

    private void guardar() {
        AdministradorJuegos.getInstance().guardarJuego(this);
    }
}
