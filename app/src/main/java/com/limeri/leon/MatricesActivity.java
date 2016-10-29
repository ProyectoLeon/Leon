package com.limeri.leon;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Gravity;
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
import com.limeri.leon.common.DragAndDropSource;
import com.limeri.leon.common.DragAndDropTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatricesActivity extends AppCompatActivity {

    private int ultimoNivel;
    public static final int NIVEL_INVERSION = 2;
    public static final int MAXIMO_ERRORES = 4;
    public static final int FIN_INVERSION = 2;
    public static final List<Integer> NIVELES_INICIALES = Arrays.asList(3, 4);
    public static final int PRIMER_NIVEL = 3;
    private int nivel = PRIMER_NIVEL;
    private int cantCorrectasSeguidas = 0;
    private int cantIncorrectasSeguidas = 0;
    private LinearLayout target;
    private Map<String,Integer> mapOpciones = new HashMap<>();
    private List<List<String>> matriz = new ArrayList<>();
    private List<String> opciones;
    private GridLayout gridMatriz;
    private GridLayout gridOpciones;
    private int size;
    private String jsonString;
    private String respuesta;
    private boolean invertido;
    private int nivelErrado;
    private boolean retrogresion = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrices);

        //Configuro la base de datos
        cargarMatricesDB();

        //Busco los GridLayout
        gridMatriz = (GridLayout) findViewById(R.id.matriz);
        gridOpciones = (GridLayout) findViewById(R.id.opciones);

        //Determino el ancho de los dibujos (revisar)
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        this.size = (size.x-100)/5;

        //Seteo el metodo Siguiente
        View siguiente = findViewById(R.id.siguiente);
        if (siguiente != null) {
            siguiente.setOnClickListener(clickSiguiente());
        }

        Navegacion.agregarMenuJuego(this);
        AdministradorJuegos.getInstance().inicializarJuego();

        inicializarVariables();
    }

    private void cargarMatricesDB() {
        jsonString = DataBase.getJuego("matrices");

        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            ultimoNivel = jsonArray.length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sumarPuntos(Integer puntos) {
        AdministradorJuegos.getInstance().sumarPuntos(puntos);
    }

    private void inicializarVariables() {
        gridMatriz.removeAllViews();
        gridOpciones.removeAllViews();
        respuesta = null;
        opciones = null;
        matriz.clear();
        mapOpciones.clear();
        target = null;

        cargarNivel();
        cargarMatriz();
        cargarOpciones();
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
            cantCorrectasSeguidas++;
            if (!invertido) {
                nivel++;
            } else {
                nivel--;
                if (isFinInversion()) {
                    revertir();
                }
            }
            sumarPuntos(1);
        } else {
            cantCorrectasSeguidas = 0;
            cantIncorrectasSeguidas++;
            if (isMaximoErrores()) {
                guardar();
                return;
            } else if (isNivelesIniciales() && !retrogresion) {
                nivelErrado = nivel;
                invertir();
            } else {
                if (!invertido) {
                    nivel++;
                } else {
                    nivel--;
                }
            }
        }
        if (noHayMasNiveles()){
            guardar();
        } else {
            cargarSiguienteNivel();
        }
    }

    private void guardar() {
        AdministradorJuegos.getInstance().guardarJuego(this);
    }

    private void revertir() {
        nivel = nivelErrado + 1;
        invertido = false;
    }

    private boolean isFinInversion() {
        return cantCorrectasSeguidas == FIN_INVERSION;
    }

    private void invertir() {
        nivel = NIVEL_INVERSION;
        retrogresion = true;
        invertido = true;
    }

    private boolean isMaximoErrores() {
        return cantIncorrectasSeguidas == MAXIMO_ERRORES;
    }

    private boolean isNivelesIniciales() {
        return NIVELES_INICIALES.contains(nivel);
    }

    private boolean noHayMasNiveles() {
        return nivel == ultimoNivel || nivel < 0;
    }

    private void cargarSiguienteNivel() {
        inicializarVariables();
    }

    private boolean isCorrecta() {
        if(target.getChildCount() != 0) {
            ImageView seleccion = (ImageView) target.getChildAt(0);
            if (seleccion.getId() == mapOpciones.get(respuesta))
                return true;
        }
        return false;
    }

    private void cargarOpciones() {
        int row = 0;
        int col = 0;
        if (!opciones.isEmpty()) {
            //Seteo la cantidad de opciones
            gridOpciones.setColumnCount(opciones.size());
            //Recorro la lista de opciones
            for (String opcion : opciones) {
                //Creo view que contiene la imagen
                ImageView v = new ImageView(this);
                int res = getResources().getIdentifier(opcion, "drawable", this.getPackageName());
                v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                v.setImageResource(res);
                v.setOnTouchListener(new DragAndDropSource());
                v.setId(res);
                //Creo el linearLayout que contiene el view
                LinearLayout l = new LinearLayout(this);
                l.setLayoutParams(getLayoutParams(row, col, ViewGroup.LayoutParams.WRAP_CONTENT));
                l.setBackground(getResources().getDrawable(R.drawable.shape));
                //Agrego el view al linearLayout
                l.addView(v);
                //Agrego el linearLayout al gridLayout
                gridOpciones.addView(l);
                mapOpciones.put(opcion, res);
                col++;
            }
        }
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

    private void cargarMatriz() {
        int row = 0;
        int col = 0;
        if (!matriz.isEmpty()) {
            //Seteo la cantidad de filas
            gridMatriz.setRowCount(matriz.size());
            //Recorro cada fila de la matriz
            for (List<String> fila : matriz) {
                if (!fila.isEmpty()) {
                    //Seteo la cantidad de columnas
                    gridMatriz.setColumnCount(fila.size());
                    //Recorro cada columna de la fila
                    for (String celda : fila) {
                        //Creo el linearLayout que va a contener la imagen o la celda Target
                        LinearLayout l = new LinearLayout(this);
                        l.setLayoutParams(getLayoutParams(row, col, (int) getResources().getDimension(R.dimen.shape_size)));
                        if (!celda.equals("")) {
                            //Imagen
                            l.setBackground(getResources().getDrawable(getResources().getIdentifier(celda, "drawable", this.getPackageName())));
                        } else {
                            //Target
                            l.setBackground(getResources().getDrawable(R.drawable.shape));
                            l.setOnDragListener(new DragAndDropTarget(this));
                            target = l;
                        }
                        //Agrego el linearLayout al gridLayout
                        gridMatriz.addView(l);
                        col++;
                    }
                    row++;
                    col = 0;
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
    }

    private void cargarNivel() {

        try {
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

            //Opciones
            JSONArray jsonOpciones = jsonObject.getJSONArray("opciones");
            opciones = (List<String>) new Gson().fromJson(jsonOpciones.toString(), listType);

            //Respuesta
            respuesta = jsonObject.getString("respuesta");

        } catch (JSONException e) {
            guardar();
        }

    }
}

