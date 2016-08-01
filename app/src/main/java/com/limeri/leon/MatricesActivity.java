package com.limeri.leon;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.limeri.leon.common.DragAndDropSource;
import com.limeri.leon.common.DragAndDropTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatricesActivity extends Activity {

    private static final int ULTIMO_NIVEL = 3;
    private int cantIncorrectas = 0;
    private int nivel = 0;
    private LinearLayout target;
    private Map<String,Integer> mapOpciones = new HashMap<String,Integer>();
//    private String[][] matriz = {{"skate","frutilla"},{"skate",""}};
    private List<List<String>> mapMatriz = new ArrayList<List<String>>();
    private List<String> opciones;// = Arrays.asList("skate","cubos","frutilla","redoblante","micro");
//    private String imagenResultado = "frutilla";
    private final LinearLayout.LayoutParams lpv = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    private GridLayout layout;
    private int row;
    private int col;
    private int width;
    private String jsonString;
    private String respuesta;

    /**
     * Called when the activity is first created.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrices);

        //Busco el Grid Layout
        layout = (GridLayout) findViewById(R.id.grid);

        //Determino el ancho de los dibujos (revisar)
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = (size.x-100)/5;

        inicializarJuego();
    }

    private void inicializarJuego() {
        layout.removeAllViews();
        row = 0;
        col = 0;
        respuesta = null;
        opciones = null;
        mapMatriz.clear();
        target = null;

        leerJson();

        cargarMatriz();
        cargarOpciones();

        row++;
        col = 1;
        Button siguiente = new Button(this);
        siguiente.setText("Siguiente");
        siguiente.setOnClickListener(clickSiguiente());
        GridLayout.LayoutParams lpg = new GridLayout.LayoutParams();
        lpg.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        lpg.width = width*2;
        lpg.setGravity(Gravity.CENTER);
        lpg.rowSpec = GridLayout.spec(row);
        lpg.columnSpec = GridLayout.spec(col,2);
        lpg.topMargin = 200;
        siguiente.setLayoutParams(lpg);
        layout.addView(siguiente);
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
        if (isCorrecta()) {
            nivel++;
        }else {
            if (cantIncorrectas++ == 4) {
                Intent mainIntent = new Intent(MatricesActivity.this, ExamenActivity.class);
                MatricesActivity.this.startActivity(mainIntent);
                MatricesActivity.this.finish();
            }else if (cantIncorrectas == 1 & (nivel == 5 | nivel == 6)) {
                nivel = 4;
            }
        }
        if (isUltimoNivel()){
            Intent mainIntent = new Intent(MatricesActivity.this, ExamenActivity.class);
            MatricesActivity.this.startActivity(mainIntent);
            MatricesActivity.this.finish();
        } else {
            cargarSiguiente();
        }
    }

    private boolean isUltimoNivel() {
        return nivel == ULTIMO_NIVEL;
    }

    private void cargarSiguiente() {
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
        col = 0;
        for (String opcion : opciones) {
            ImageView v = new ImageView(this);
            int res = getResources().getIdentifier(opcion, "drawable", this.getPackageName());
            v.setLayoutParams(lpv);
            v.setImageResource(res);
            v.setOnTouchListener(new DragAndDropSource());
            LinearLayout l = new LinearLayout(this);
            GridLayout.LayoutParams lpg = new GridLayout.LayoutParams();
            lpg.height = width;
            lpg.width = width;
            lpg.setGravity(Gravity.CENTER);
            lpg.rowSpec = GridLayout.spec(row);
            lpg.columnSpec = GridLayout.spec(col);
            lpg.topMargin = 100;
            l.setLayoutParams(lpg);
            //l.setOrientation(LinearLayout.HORIZONTAL);
            l.addView(v);
            l.setBackground(getResources().getDrawable(R.drawable.shape));
            layout.addView(l);
            mapOpciones.put(opcion, v.getId());
            col++;
        }
//        findViewById(R.id.imgOpcion1).setOnTouchListener(new DragAndDropSource());
//        mapOpciones.put("Frutilla", R.id.imgOpcion2);
//        findViewById(R.id.imgOpcion2).setOnTouchListener(new DragAndDropSource());
//        mapOpciones.put("Cubos", R.id.imgOpcion3);
//        findViewById(R.id.imgOpcion3).setOnTouchListener(new DragAndDropSource());
//        mapOpciones.put("Redoblante", R.id.imgOpcion4);
//        findViewById(R.id.imgOpcion4).setOnTouchListener(new DragAndDropSource());
    }

    private void cargarMatriz() {
        for (List<String> fila : mapMatriz) {
//        for (int i=0; i<matriz.length; i++){
            for (String celda : fila) {
//            for (int j=0; j<matriz[i].length; j++){
                LinearLayout l = new LinearLayout(this);
                GridLayout.LayoutParams lpg = new GridLayout.LayoutParams();
                lpg.height = width;
                lpg.width = width;
                lpg.setGravity(Gravity.CENTER);
                lpg.rowSpec = GridLayout.spec(row);
                lpg.columnSpec = GridLayout.spec(col);
                l.setLayoutParams(lpg);
                if (celda != "") {
                    l.setBackground(getResources().getDrawable(getResources().getIdentifier(celda, "drawable", this.getPackageName())));
                } else {
                    l.setBackground(getResources().getDrawable(R.drawable.shape));
                    l.setOnDragListener(new DragAndDropTarget(this));
                    target = l;
                }

                layout.addView(l);
                col++;
            }
            row++;
            col = 0;
        }
//        target = (LinearLayout) findViewById(R.id.target);
//        target.setOnDragListener(new DragAndDropTarget(this));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent mainIntent = new Intent(MatricesActivity.this, ExamenActivity.class);
        MatricesActivity.this.startActivity(mainIntent);
        MatricesActivity.this.finish();
    }

    private void leerJson() {

        Writer writer = new StringWriter();

        if (nivel == 0) {
            InputStream is = getResources().openRawResource(R.raw.matrices);

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
            JSONArray jsonArray = jsonRootObject.getJSONArray("matrices");

            //Iterate the jsonArray and print the info of JSONObjects
            JSONObject jsonObject = jsonArray.getJSONObject(nivel);

            //Matriz
            JSONObject jsonMatriz = jsonObject.getJSONObject("matriz");
            JSONArray jsonFila1 = jsonMatriz.getJSONArray("fila1");
            JSONArray jsonFila2 = jsonMatriz.getJSONArray("fila2");
            JSONArray jsonFila3 = jsonMatriz.getJSONArray("fila3");
            Type listType = new TypeToken<List<String>>() {}.getType();
            mapMatriz.add((List<String>) new Gson().fromJson(jsonFila1.toString(), listType));
            if (jsonFila2.length() > 0) mapMatriz.add((List<String>) new Gson().fromJson(jsonFila2.toString(), listType));
            if (jsonFila3.length() > 0) mapMatriz.add((List<String>) new Gson().fromJson(jsonFila3.toString(), listType));

            //Opciones
            JSONArray jsonOpciones = jsonObject.getJSONArray("opciones");
            opciones = (List<String>) new Gson().fromJson(jsonOpciones.toString(), listType);

            //Respuesta
            respuesta = jsonObject.getString("respuesta");

        } catch (JSONException e) {
            Intent mainIntent = new Intent(MatricesActivity.this, ExamenActivity.class);
            MatricesActivity.this.startActivity(mainIntent);
            MatricesActivity.this.finish();
        }

    }
}
