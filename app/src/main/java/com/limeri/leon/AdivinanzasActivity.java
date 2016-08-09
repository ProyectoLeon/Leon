package com.limeri.leon;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class AdivinanzasActivity extends AppCompatActivity {

    private Button siguiente;
    private TextView palabra;
    private ListView respuestas;
    private TextView seleccion;
    private String respuestaSeleccionada = "";
    private int nivel = 0;
    private int cantIncorrectas = 0;
    private int cantConsec = 0;
    private String jsonString;
    private int puntaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adivinanzas);

        siguiente = (Button) findViewById(R.id.siguiente);
        siguiente.setOnClickListener(clickSiguiente());

        palabra = (TextView) findViewById(R.id.palabra);

        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.action_bar,
                null);


        getSupportActionBar().setCustomView(actionBarLayout);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        Button boton =(Button) getSupportActionBar().getCustomView().findViewById(R.id.boton_actionbar);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AdivinanzasActivity.this)
                        .setTitle("Popup")
                        .setMessage("Por favor seleccione opción")
                        .setPositiveButton("Guardar y Finalizar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent mainIntent = new Intent(AdivinanzasActivity.this, ExamenActivity.class);
                                AdivinanzasActivity.this.startActivity(mainIntent);
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
                                return;
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        //Llamo una funcion que se encarga de leer el archivo JSON
        leerJson();

    }

    private void leerJson() {

        Writer writer = new StringWriter();

        if (nivel == 0) {
            InputStream is = getResources().openRawResource(R.raw.preguntasadivinanzas);

            char[] buffer = new char[1024];

            try {
                Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();

            } finally {

                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            jsonString = writer.toString();
        }


        String data = "";
        try {
            JSONObject jsonRootObject = new JSONObject(jsonString);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.getJSONArray("adivinanzas");

            //Iterate the jsonArray and print the info of JSONObjects
            //for(int i=0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(nivel);

            palabra.setText(jsonObject.getString("pregunta").toString());
            String[] listRespuestas = {(jsonObject.optString("respuesta0").toString()), (jsonObject.optString("respuesta1").toString())};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, listRespuestas);

            respuestas = (ListView) findViewById(R.id.respuestas);
            respuestas.setOnItemClickListener(opcionSeleccionada());
            respuestas.setAdapter(adapter);

        } catch (JSONException e) {
            Intent mainIntent = new Intent(AdivinanzasActivity.this, ExamenActivity.class);
            AdivinanzasActivity.this.startActivity(mainIntent);
            AdivinanzasActivity.this.finish();
        }

    }

    private AdapterView.OnItemClickListener opcionSeleccionada() {

        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (seleccion != null) blanquear(seleccion);
                seleccion = ((TextView) view);
                seleccionar(seleccion);
                respuestaSeleccionada = seleccion.getText().toString();
                //Corregir para identificar cuando hacer retroceso o no
                if (position == 1){
                    cantIncorrectas++;
                } else { cantIncorrectas = 0;
                    puntaje++;
                }
            }
        };
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
        //Faltaría guardar la respuesta en la base de datos
        blanquear(seleccion);
        if (cantIncorrectas== 5) {
            Intent mainIntent = new Intent(AdivinanzasActivity.this, ExamenActivity.class);
            AdivinanzasActivity.this.startActivity(mainIntent);
            AdivinanzasActivity.this.finish();
        // } else if (cantIncorrectas==1 & (nivel == 5 | nivel ==6)){
        //    nivel = 4;
        //} else if (cantConsec == 2) {
        //    nivel = 5;
        } else {
            nivel++;
        }
        try {
            leerJson();
        } catch (Exception ex) {
            Intent mainIntent = new Intent(AdivinanzasActivity.this, ExamenActivity.class);
            AdivinanzasActivity.this.startActivity(mainIntent);
            AdivinanzasActivity.this.finish();
        }}

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        guardar();
    }

    private void guardar() {
        Intent mainIntent = new Intent(AdivinanzasActivity.this, InicioJuegoActivity.class);
        AdivinanzasActivity.this.startActivity(mainIntent);
        AdivinanzasActivity.this.finish();
    }


}
