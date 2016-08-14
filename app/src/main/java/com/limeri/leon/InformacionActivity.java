package com.limeri.leon;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class InformacionActivity extends AppCompatActivity {


    private Button siguiente;
    private TextView palabra;
    private ListView respuestas;
    private TextView seleccion;
    private int longArray;
    private String respuestaSeleccionada = "";
    private int nivel = 5;
    private int cantIncorrectas = 0;
    private int cantConsec = 0;
    private String jsonString;
    private int puntaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion);

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
                AlertDialog.Builder builder = new AlertDialog.Builder(InformacionActivity.this);

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
                        dialog.cancel();
                    }
                });

                // Display the custom alert dialog on interface
                dialog.show();
            }
        });

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

            respuestas = (ListView) findViewById(R.id.respuestas);
            respuestas.setOnItemClickListener(opcionSeleccionada());
            respuestas.setAdapter(adapter);

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
                respuestaSeleccionada = seleccion.getText().toString();
                if (position == 1){
                    cantIncorrectas++;
                    cantConsec = 0;
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
        try {
            AdministradorJuegos.getInstance().guardarJuego(puntaje, this);
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
