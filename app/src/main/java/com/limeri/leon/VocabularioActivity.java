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
import com.limeri.leon.Models.Navegacion;
import com.limeri.leon.common.JSONLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VocabularioActivity extends AppCompatActivity {

    private TextView palabra;
    private TextView seleccion;
    private int nivel = 4; // Consideramos los niveles 0 a 3 (gráficos)
    private int nivelErroneo = 0;
    private int cantIncorrectas = 0;
    private int cantConsec = 0;
    private boolean puntPerfecto = false;
    private boolean jsonLoaded = false;
    private boolean soloImagen = true;
    private boolean backHecho = false;
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
        AdministradorJuegos.getInstance().inicializarJuego();

        //Llamo una funcion que se encarga de leer el archivo JSON
        leerJson();

    }

    private void leerJson() {
        if ((nivel == 4) & (!jsonLoaded)) {
            jsonString = JSONLoader.loadJSON(getResources().openRawResource(R.raw.preguntasvocabulario));
            jsonLoaded = true;
        }

        try {
            JSONObject jsonRootObject = new JSONObject(jsonString);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.getJSONArray("vocabulario");

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

                // Si no obtiene puntuación perfecta en algunos de los primeros dos, sucuencia inversa hasta que acierta 2 seguidos.
                if (nivel < 4) {
                    if (position == 1){
                        cantIncorrectas++;
                        cantConsec = 0;
                        puntPerfecto = false;
                    } else {
                        cantIncorrectas = 0;
                        sumarPuntos(1);
                        puntPerfecto = true;
                    }
                } else {
                    if (position == 2){
                        cantIncorrectas++;
                        puntPerfecto = false;
                    } else if (position == 1) {
                        cantIncorrectas = 0;
                        sumarPuntos(1);
                        puntPerfecto = false;
                    } else {
                        cantIncorrectas = 0;
                        sumarPuntos(2);
                        puntPerfecto = true;
                    }
                }
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
        if (nivel < 4 & soloImagen) {
            soloImagen = false;
        } else {
            //Faltaría guardar la respuesta en la base de datos
            blanquear(seleccion);
            if (cantIncorrectas == 5) {
                guardar();
            } else if ((nivel == 4 | nivel == 5) & !puntPerfecto & !backHecho) {
                nivelErroneo = nivel;
                nivel = 3;
                backHecho = true;
            } else if (nivel < 4 & cantIncorrectas > 0) {
                nivel--;
                soloImagen = true;
            } else if (nivel < 4 & cantIncorrectas == 0) {
                cantConsec++;
                if (cantConsec == 2) {
                    nivel = nivelErroneo + 1;
                } else {
                    nivel--;
                    soloImagen = true;
                }
            } else if (nivel < 0) {
                guardar();
            } else {
                nivel++;
            }
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

}
