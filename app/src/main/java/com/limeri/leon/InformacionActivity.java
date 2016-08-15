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
import com.limeri.leon.Models.Juego;
import com.limeri.leon.Models.Navegacion;
import com.limeri.leon.Models.Paciente;
import com.limeri.leon.common.JSONLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InformacionActivity extends AppCompatActivity {


    private TextView palabra;
    private TextView seleccion;
    private int longArray;
    private int nivel = 5;
    private int cantIncorrectas = 0;
    private int cantConsec = 0;
    private String jsonString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion);

        Button siguiente = (Button) findViewById(R.id.siguiente);
        if (siguiente != null) {
            siguiente.setOnClickListener(clickSiguiente());
        }

        palabra = (TextView) findViewById(R.id.palabra);

        Navegacion.agregarMenuJuego(this);

        //Llamo una funcion que se encarga de leer el archivo JSON
        leerJson();

    }

    private void leerJson() {
//TODO: Que lo haga solo una vez, no cuando solo es nivel 5.
        if (nivel == 5) {
            jsonString = JSONLoader.loadJSON(getResources().openRawResource(R.raw.preguntasinformacion));
        }

        try {
            JSONObject jsonRootObject = new JSONObject(jsonString);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.getJSONArray("informacion");

            //Iterate the jsonArray and print the info of JSONObjects
            //for(int i=0; i < jsonArray.length(); i++){
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
                if (position == 1){
                    cantIncorrectas++;
                    cantConsec = 0;
                } else { cantIncorrectas = 0;
                    sumarPuntos(1);
                }
            }
        };
    }

    private void sumarPuntos(Integer puntos) {
        Juego juego = Paciente.getSelectedPaciente().getEvaluacionActual().getJuegoActual();
        juego.setPuntosJuego(juego.getPuntosJuego() + puntos);
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
                guardarRespuesta();
            }
        };
    }

    private void guardarRespuesta() {
        blanquear(seleccion);
        if (nivel == longArray){
            guardar();
        }
        if (cantIncorrectas== 5) {
            guardar();
        } else
        if (cantIncorrectas==1 & (nivel == 5 | nivel ==6)){
            nivel = 4;
        } else if (cantIncorrectas == 0 & cantConsec == 2) {
            nivel = 5;
        }else if (cantIncorrectas > 0 & nivel < 5) {
            nivel --;
        }
        else if (cantIncorrectas ==0 & nivel < 5) {
            nivel --;
            cantConsec++;
        }
        else if (nivel > 4){
            nivel++;
        }

        try {
            leerJson();
        } catch (Exception ex) {
            guardar();
        }
    }

    private void guardar() {
        AdministradorJuegos.getInstance().guardarJuego(this);
    }
}
