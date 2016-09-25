package com.limeri.leon;


import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.limeri.leon.Models.AdministradorJuegos;
import com.limeri.leon.Models.Navegacion;
import com.limeri.leon.common.DataBase;
import com.limeri.leon.common.JSONLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConceptosActivity extends AppCompatActivity {

    public static final int ULTIMO_NIVEL = 6;
    public static final int MAXIMO_ERRORES = 3; //es 5
    public static final int PRIMER_NIVEL = 0;
    private int nivel = PRIMER_NIVEL;
    private int cantIncorrectasSeguidas = 0;
    private LinearLayout target;
    private List<List<String>> matriz = new ArrayList<>();
    private ArrayList<ArrayList>  grillaLogica = new ArrayList<>();
    private ArrayList<Boolean> seleccion = new ArrayList<>();
    private GridLayout gridMatriz;
    private String jsonString;
    private String respuesta;
    private Map<String, Integer> mapOpciones = new HashMap<>();
    private List<String> respuestas = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conceptos);

        //Configuro la base de datos
        cargarConceptosDB();

        //Busco los GridLayout
        gridMatriz = (GridLayout) findViewById(R.id.matriz);

        //Seteo el metodo Siguiente
        View siguiente = findViewById(R.id.siguiente);
        if (siguiente != null) {
            siguiente.setOnClickListener(clickSiguiente());
        }

        Navegacion.agregarMenuJuego(this);
        AdministradorJuegos.getInstance().inicializarJuego();


        inicializarVariables();
        leerJson();
        cargarFilas();
    }

    private void cargarConceptosDB() {
        jsonString = DataBase.cargarJuego("conceptos");
    }

    private void sumarPuntos(Integer puntos) {
        AdministradorJuegos.getInstance().sumarPuntos(puntos);
    }

    private void inicializarVariables() {
        gridMatriz.removeAllViews();
        respuesta = null;
        matriz.clear();
        mapOpciones.clear();
        target = null;
        grillaLogica.clear();
        seleccion.clear();

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
       if (isCorrecta()) {
            cantIncorrectasSeguidas = 0;
            //sumarPuntos(1);
        } else {
           cantIncorrectasSeguidas++;
       }

        nivel++;

        if (noHayMasNiveles()||isMaximoErrores()){
            guardar();
        } else {
            cargarSiguienteNivel();
        }

        System.out.println("Cantidad incorrectas" + cantIncorrectasSeguidas);


    }

    private void guardar() {
        AdministradorJuegos.getInstance().guardarJuego(this);
    }


    private boolean isMaximoErrores() {
        return cantIncorrectasSeguidas == MAXIMO_ERRORES;
    }


    private boolean noHayMasNiveles() {
        return nivel == ULTIMO_NIVEL || nivel < 0;
    }

    private void cargarSiguienteNivel() {
        inicializarVariables();
        leerJson();
        cargarFilas();
    }

    private boolean isCorrecta() {

        Boolean result = true;

        for ( Boolean filas: seleccion) {
            if(!filas){
                result = false;
            }
        }
        System.out.println("resultado " + result);
        return result;
    }


    @NonNull
    private GridLayout.LayoutParams getLayoutParams(int row, int col, int size) {
        GridLayout.LayoutParams lpg = new GridLayout.LayoutParams();
        lpg.height = size;
        lpg.width = size;
        lpg.setGravity(Gravity.CENTER);
        lpg.rowSpec = GridLayout.spec(row);
        lpg.columnSpec = GridLayout.spec(col);
        return lpg;
    }

    private void cargarFilas() {
        int row = 0;
        int col = 0;
        if (!matriz.isEmpty()) {
            //Seteo la cantidad de filas
            gridMatriz.setRowCount(matriz.size());
            //Recorro cada fila de la matriz
            for (List<String> fila : matriz) {
                if (!fila.isEmpty()) {
                    ArrayList<LinearLayout> columna = new ArrayList<>();
                    //Inicializo array seleccion
                    seleccion.add(row, false);
                    //Seteo la cantidad de columnas
                    gridMatriz.setColumnCount(fila.size());
                    //Recorro cada columna de la fila

                    for (String celda : fila) {

                        if (!celda.equals("")) {


                            //Creo view que contiene la imagen
                            ImageView v = new ImageView(this);
                            int res = getResources().getIdentifier(celda, "drawable", this.getPackageName());
                            v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            v.setImageResource(res);
                            v.setId(res);
                            //Creo el linearLayout que contiene el view
                            LinearLayout l  = new LinearLayout(this);
                            l.setLayoutParams(getLayoutParams(row, col, (int) getResources().getDimension(R.dimen.shape_size)));
                            //Agrego el view al linearLayout
                            l.addView(v);
                            //evento
                            l.setOnTouchListener(clickOpcion());
                            //Agrego el linearLayout al gridLayout
                            gridMatriz.addView(l);
                            //Cargar la respuesta correcta nada mas
                            mapOpciones.put(celda, res);
                            columna.add(l);

                        }
                        col++;

                    }
                    grillaLogica.add(columna);
                }
                row++;
                col = 0;
            }
        }
    }


    private View.OnTouchListener clickOpcion() {
        return new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent ev) {
                final int action = ev.getAction();
                if (action == MotionEvent.ACTION_UP) {
                    int fila = 0;
                    //recorro la matriz buscando la imagen seleccionada
                    for (ArrayList<LinearLayout> filas: grillaLogica) {
                        int colu = 0;
                          for(LinearLayout linLay : filas) {
                              LinearLayout container = (LinearLayout) v;
                              //liLay contenido de cada columna y container es la vista que me trae en onTouch
                              if (linLay==container) {
                                  borrarYSeleccionar(fila,colu);
                              }
                              colu ++;
                          }
                        fila++;
                    }

                }
                return true;
            }
        };
    }

    private void borrarYSeleccionar(int fila, int colu) {
        //recorro la fila seleccionada
        ArrayList<LinearLayout> crt = grillaLogica.get(fila);
        int cont = 0;
        for (LinearLayout crtLin: crt) {
            //Si la columan es distinta, blanqueo
            if (colu != cont) {
                if(crtLin.getChildCount() != 0){
                    ImageView child = (ImageView) crtLin.getChildAt(0);
                    child.setImageAlpha(70);
                }
            } else { //Resalto respuesta seleccionada
                if(crtLin.getChildCount() != 0){
                    ImageView child = (ImageView) crtLin.getChildAt(0);
                    child.setImageAlpha(250);
                    actualizarSeleccion(fila,child);
            }
            }
            cont++;
        }

    }

    private void actualizarSeleccion(int fila, View vista) {

        System.out.println("respuesta correcta:" + mapOpciones.get(respuestas.get(fila)));
        System.out.println("Id Vista seleccionada:" + vista.getId());


        if (mapOpciones.get(respuestas.get(fila)) == vista.getId()) {
            seleccion.set(fila, true);
            System.out.println("seleccion fila " + fila + " es true");
        }
        else {
            seleccion.set(fila, false);
            System.out.println("seleccion fila " + fila + " false");
        }

        }



    @Override
    public void onBackPressed() {
    }


    private void leerJson() {

//        if (nivel == PRIMER_NIVEL) {
//            jsonString = JSONLoader.loadJSON(getResources().openRawResource(R.raw.conceptos));
//        }

        try {
//            JSONObject jsonRootObject = new JSONObject(jsonString);
//
//            //Get the instance of JSONArray that contains JSONObjects
//            JSONArray jsonArray = jsonRootObject.getJSONArray("conceptos");

            JSONArray jsonArray = new JSONArray(jsonString);

            //Iterate the jsonArray and print the info of JSONObjects
            JSONObject jsonObject = jsonArray.getJSONObject(nivel);

            //Matriz
            JSONObject jsonMatriz = jsonObject.getJSONObject("matriz");
            Type listType = new TypeToken<List<String>>() {}.getType();
            if  (!jsonMatriz.isNull("fila1")) {
                JSONArray jsonFila1 = jsonMatriz.getJSONArray("fila1");
                if (jsonFila1.length() > 0)
                    matriz.add((List<String>) new Gson().fromJson(jsonFila1.toString(), listType));
            }
            if  (!jsonMatriz.isNull("fila2")) {
                JSONArray jsonFila2 = jsonMatriz.getJSONArray("fila2");
                if (jsonFila2.length() > 0)
                    matriz.add((List<String>) new Gson().fromJson(jsonFila2.toString(), listType));
            }
            if  (!jsonMatriz.isNull("fila3")) {
                JSONArray jsonFila3 = jsonMatriz.getJSONArray("fila3");
                if (jsonFila3.length() > 0)
                    matriz.add((List<String>) new Gson().fromJson(jsonFila3.toString(), listType));
            }

            //Respuesta
            JSONArray  jsonRespuesta = jsonObject.getJSONArray("respuesta");
            respuestas = (ArrayList<String>) new Gson().fromJson(jsonRespuesta.toString(), listType);


        } catch (JSONException e) {
            //guardar();
        }

    }
}

