package com.limeri.leon;

import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.limeri.leon.Models.AdministradorJuegos;
import com.limeri.leon.Models.Navegacion;
import com.limeri.leon.common.DragAndDropSource;
import com.limeri.leon.common.DragAndDropTarget;
import com.limeri.leon.common.JSONLoader;

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

    private static final int ULTIMO_NIVEL = 10;
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
    private Map<Integer, Integer> puntos = new HashMap<>();
    private int puntaje = 0;
    private GridLayout gridMatriz;
    private GridLayout gridOpciones;
    private int size;
    private String jsonString;
    private String respuesta;
    private boolean invertido;
    private int nivelErrado;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrices);

        //Busco los GridLayout
        gridMatriz = (GridLayout) findViewById(R.id.matriz);
        gridOpciones = (GridLayout) findViewById(R.id.opciones);

        //Determino el ancho de los dibujos (revisar)
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        this.size = (size.x-100)/5;

        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.action_bar,
                null);


        getSupportActionBar().setCustomView(actionBarLayout);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        Button boton =(Button) getSupportActionBar().getCustomView().findViewById(R.id.boton_actionbar);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Agregar la lógica de cancelar juego en NAVEGACIÓN
               android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MatricesActivity.this);

                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alert_dialog,null);

                builder.setView(dialogView);

                Button btn_positive = (Button) dialogView.findViewById(R.id.dialog_positive_btn);
                Button btn_negative = (Button) dialogView.findViewById(R.id.dialog_negative_btn);
                Button btn_neutral = (Button) dialogView.findViewById(R.id.dialog_neutral_btn);

                final android.app.AlertDialog dialog = builder.create();

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

        inicializarJuego();
    }

    private void inicializarJuego() {
        gridMatriz.removeAllViews();
        gridOpciones.removeAllViews();
        respuesta = null;
        opciones = null;
        matriz.clear();
        mapOpciones.clear();
        target = null;

        leerJson();

        cargarMatriz();
        cargarOpciones();

        //Seteo el metodo Siguiente
        findViewById(R.id.siguiente).setOnClickListener(clickSiguiente());
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
        int puntosNivel = 0;
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
            puntosNivel++;
            puntaje++;
        } else {
            cantCorrectasSeguidas = 0;
            cantIncorrectasSeguidas++;
            if (isMaximoErrores()) {
                guardar();
            } else if (isNivelesIniciales()) {
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
        puntos.put(nivel,puntosNivel);
        if (isUltimoNivel()){
            guardar();
        } else {
            cargarSiguienteNivel();
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

    private void revertir() {
        nivel = nivelErrado + 1;
        invertido = false;
    }

    private boolean isFinInversion() {
        return cantCorrectasSeguidas == FIN_INVERSION;
    }

    private void invertir() {
        nivel = NIVEL_INVERSION;
        invertido = true;
    }

    private boolean isMaximoErrores() {
        return cantIncorrectasSeguidas == MAXIMO_ERRORES;
    }

    private boolean isNivelesIniciales() {
        return NIVELES_INICIALES.contains(nivel);
    }

    private boolean isUltimoNivel() {
        return nivel == ULTIMO_NIVEL;
    }

    private void cargarSiguienteNivel() {
        inicializarJuego();
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
                v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                v.setImageResource(res);
                v.setOnTouchListener(new DragAndDropSource());
                v.setId(res);
                //Creo el linearLayout que contiene el view
                LinearLayout l = new LinearLayout(this);
                l.setLayoutParams(getLayoutParams(row, col, size));
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
        super.onBackPressed();

        guardar();
    }

    private void leerJson() {

        if (nivel == PRIMER_NIVEL) {
            jsonString = JSONLoader.loadJSON(getResources().openRawResource(R.raw.matrices));
        }

        try {
            JSONObject jsonRootObject = new JSONObject(jsonString);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.getJSONArray("matrices");

            //Iterate the jsonArray and print the info of JSONObjects
            JSONObject jsonObject = jsonArray.getJSONObject(nivel);

            //Matriz
            JSONObject jsonMatriz = jsonObject.getJSONObject("matriz");
            JSONArray jsonFila1 = jsonMatriz.getJSONArray("fila1");
            JSONArray jsonFila2 = jsonMatriz.getJSONArray("fila2");
            JSONArray jsonFila3 = jsonMatriz.getJSONArray("fila3");
            Type listType = new TypeToken<List<String>>() {}.getType();
            matriz.add((List<String>) new Gson().fromJson(jsonFila1.toString(), listType));
            if (jsonFila2.length() > 0) matriz.add((List<String>) new Gson().fromJson(jsonFila2.toString(), listType));
            if (jsonFila3.length() > 0) matriz.add((List<String>) new Gson().fromJson(jsonFila3.toString(), listType));

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
