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
import android.widget.TextView;

import com.limeri.leon.Models.AdministradorJuegos;
import com.limeri.leon.Models.Juego;
import com.limeri.leon.Models.Navegacion;
import com.limeri.leon.Models.Paciente;
import com.limeri.leon.common.JSONLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VocabularioActivity extends AppCompatActivity {

    private TextView palabra;
    private TextView seleccion;
    private int nivel = 4; // Consideramos los niveles 0 a 3 (gráficos)
    private int cantIncorrectas = 0;
    private int cantConsec = 0;
    private int puntPerfecto = 0;
    private String jsonString;
    private ImageView imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulario);

        Button siguiente = (Button) findViewById(R.id.siguiente);
        if (siguiente != null) {
            siguiente.setOnClickListener(clickSiguiente());
        }

        palabra = (TextView) findViewById(R.id.palabra);
        imagen = (ImageView) findViewById(R.id.imagen);

        Navegacion.agregarMenuJuego(this);

        //Llamo una funcion que se encarga de leer el archivo JSON
        leerJson();

    }

    private void leerJson() {
        if (nivel == 4) {
            jsonString = JSONLoader.loadJSON(getResources().openRawResource(R.raw.preguntasvocabulario));
        }

        try {
            JSONObject jsonRootObject = new JSONObject(jsonString);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.getJSONArray("vocabulario");

            //Iterate the jsonArray and print the info of JSONObjects
            //for(int i=0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(nivel);

            String pregunta = jsonObject.getString("pregunta").toString();
//TODO: Corregir para cuando debe ser dibujo o pregunta según el nivel.
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
                String[] listRespuestas = {(jsonObject.optString("respuesta0").toString()), (jsonObject.optString("respuesta1").toString())};
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listRespuestas);
                ListView respuestas = (ListView) findViewById(R.id.respuestas);
                if (respuestas != null) {
                    respuestas.setOnItemClickListener(opcionSeleccionada());
                    respuestas.setAdapter(adapter);
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
                //TODO: Corregir para identificar cuando hacer retrogresion.
                // Si no obtiene puntuación perfecta en algunos de los primeros dos, sucuencia inversa hasta que acierta 2 seguidos.
                if (nivel < 4) {
                    if (position == 1){
                        cantIncorrectas++;
                        puntPerfecto = 0;
                    } else {
                        cantIncorrectas = 0;
                        sumarPuntos(1);
                        puntPerfecto = 1;
                    }
                } else {
                    if (position == 2){
                        cantIncorrectas++;
                        puntPerfecto = 0;
                    } else if (position == 1) {
                        cantIncorrectas = 0;
                        sumarPuntos(1);
                        puntPerfecto = 0;
                    } else {
                        cantIncorrectas = 0;
                        sumarPuntos(2);
                        puntPerfecto = 1;
                    }
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
        }  else if ( nivel == 4 & puntPerfecto == 0 & cantConsec == 0 ){
            nivel = 3;
        }  else if ( nivel == 5 & puntPerfecto == 0 & cantConsec == 0 ){
            nivel = 3;
        }  else if ( nivel < 4 & cantIncorrectas > 0 ){
            nivel --;
        }  else if ( nivel < 4 & cantIncorrectas == 0 ){
            cantConsec++;
            if (cantConsec == 2) {
                nivel = 4;
            } else {
                nivel --;
            }
        }  else if ( nivel < 0 ){
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
        super.onBackPressed();

        guardar();
    }

    private void guardar() {
        AdministradorJuegos.getInstance().guardarJuego(this);
    }

}
