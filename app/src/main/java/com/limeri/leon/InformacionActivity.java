package com.limeri.leon;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class InformacionActivity extends AppCompatActivity {

    private Button siguiente;
    private TextView palabra;
    private ListView respuestas;
    private TextView seleccion;
    private String respuestaSeleccionada = "";
    private int nivel = 0;
    private String[] palabras = {"Pie", "Nariz", "Comida", "Orejas", "Años", "Patas", "Jueves", "Monedas", "Marzo", "Hierve"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion);

        siguiente = (Button) findViewById(R.id.siguiente);
        siguiente.setOnClickListener(clickSiguiente());

        palabra = (TextView) findViewById(R.id.palabra);
        palabra.setText(palabras[nivel]);

        String[] listRespuestas = {"Incorrecto (0)", "Correcto (1)"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, listRespuestas);

        respuestas = (ListView) findViewById(R.id.respuestas);
        respuestas.setOnItemClickListener(opcionSeleccionada());
        respuestas.setAdapter(adapter);
    }

    private AdapterView.OnItemClickListener opcionSeleccionada() {

        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(seleccion != null) blanquear(seleccion);
                seleccion = ((TextView) view);
                seleccionar(seleccion);
                respuestaSeleccionada = seleccion.getText().toString();
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
        nivel++;
            if (nivel == 10) {
                return;
            } else {
        palabra.setText(palabras[nivel]);
    }
}}
