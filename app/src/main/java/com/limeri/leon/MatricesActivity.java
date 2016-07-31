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

import com.limeri.leon.common.DragAndDropSource;
import com.limeri.leon.common.DragAndDropTarget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class MatricesActivity extends Activity {

    private int cantIncorrectas = 0;
    private int nivel = 5;
    private LinearLayout target;
    private Map<String,Integer> mapOpciones = new HashMap<String,Integer>();
    private String[][] matriz = {{"skate","frutilla"},{"skate",""}};
    private List<String> opciones = Arrays.asList("skate","cubos","frutilla","redoblante","micro");
    private String imagenResultado = "frutilla";
    private final LinearLayout.LayoutParams lpv = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    private GridLayout layout;
    private int row = 0;
    private int col = 0;
    private int width;

    /**
     * Called when the activity is first created.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrices);
        layout = (GridLayout) findViewById(R.id.grid);
        layout.removeAllViews();

        //Determinar el ancho de los dibujos
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = (size.x-100)/opciones.size();

        //Falta setear cantidad de columnas y filas dinamicamente

        loadMatriz();
        row += 1;
        loadOpciones();

        row += 1;
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

        cargarSiguiente();
    }

    private void cargarSiguiente() {
        reset();
    }

    private void reset() {
        //this.recreate();
    }

    private boolean isCorrecta() {
        if(target.getChildCount() != 0) {
            ImageView seleccion = (ImageView) target.getChildAt(0);
            if (seleccion.getId() == mapOpciones.get(imagenResultado))
                return true;
        }
        return false;
    }


    private void loadOpciones() {
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

    private void loadMatriz() {
        for (int i=0; i<matriz.length; i++){
            row += i;
            col = 1;
            for (int j=0; j<matriz[i].length; j++){
                col += j;
                LinearLayout l = new LinearLayout(this);
                GridLayout.LayoutParams lpg = new GridLayout.LayoutParams();
                lpg.height = width;
                lpg.width = width;
                lpg.setGravity(Gravity.CENTER);
                lpg.rowSpec = GridLayout.spec(row);
                lpg.columnSpec = GridLayout.spec(col);
                l.setLayoutParams(lpg);
                if (matriz[i][j] != "") {
                    l.setBackground(getResources().getDrawable(getResources().getIdentifier(matriz[i][j], "drawable", this.getPackageName())));
                } else {
                    l.setBackground(getResources().getDrawable(R.drawable.shape));
                    l.setOnDragListener(new DragAndDropTarget(this));
                }

                layout.addView(l);
            }
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
}
