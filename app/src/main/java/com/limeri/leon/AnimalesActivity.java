package com.limeri.leon;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;

import com.limeri.leon.Models.AdministradorJuegos;
import com.limeri.leon.Models.Navegacion;
import com.limeri.leon.common.CanvasView;
import com.limeri.leon.common.ClearCanvas;
import com.limeri.leon.common.JSONLoader;
import com.limeri.leon.common.Renderable;
import com.limeri.leon.common.SquareDrawable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AnimalesActivity extends AppCompatActivity {

    public static final int PRIMER_NIVEL = 0;
    public static final int TIEMPO_NIVEL = 20000; //En milisegundos
    private int nivel = PRIMER_NIVEL;
    private ImageView imgAnimales;
    private ImageView imgMask;
    private List<String> listAnimales;
    private int ultimoNivel = 1;
    private List<String> listMasks;
    private Chronometer crono;
    private long tiempo_ejecutado = 0;
    private long tiempo_inicio;
    private Bitmap hotspots;
    private CanvasView canvasView;
    private int puntos = 0;
    private long tiempoNivel = 0;
    private int puntajePlus = 14;
    private int lado =0;



    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            guardarRespuesta();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animales);

        //Busco los ImageView
        imgAnimales = (ImageView) findViewById(R.id.animales);
        imgMask = (ImageView) findViewById(R.id.mask);
        crono = (Chronometer) findViewById(R.id.cronometro);
        lado = imgAnimales.getDrawable().getIntrinsicWidth()/10;

        Navegacion.agregarMenuJuego(this);
        AdministradorJuegos.getInstance().inicializarJuego();

        //Seteo el metodo Siguiente
        Button siguiente = (Button) findViewById(R.id.siguiente);
        if (siguiente != null) {
            siguiente.setOnClickListener(clickSiguiente());
        }

        //Seteo el metodo para sumar o restar puntaje por cada vez que clickea sobre una figura

        canvasView = (CanvasView)findViewById(R.id.canvas);
        if (canvasView != null) {
            canvasView.setOnTouchListener(new MyTouchListener());
            canvasView.setZOrderOnTop(true);
        }


        inicializarVariables();
    }


    private class MyTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                //Reviso que no haya sido seleccionado anteriormente
                if (noEstaSeleecionado((int)event.getX(), (int) event.getY())){

                    System.out.println("No fue seleccionada");
                    //Busco color del mask
                    int touchColor = getHotspotColor((int) event.getX(), (int) event.getY());

                    if (isCorrecta(touchColor)) {
                        puntos++;
                        marcarDibujo((int) event.getX(), (int) event.getY());

                        //System.out.println("Es correcta");
                    } else {
                        if (isInCorrecta(touchColor)) {
                            puntos--;
                            marcarDibujo((int) event.getX(), (int) event.getY());
                            //System.out.println("Es incorrecta");
                        }

                    }
                }
                else {
                    System.out.println("Fue seleccionada anteriormente");
                }
                return true;
                }
                return false;

            }

    }

    private boolean noEstaSeleecionado(int x, int y) {

        List<Renderable> listaSeleccion = canvasView.getRenderable();

        Boolean noEstaSeleccionada = true;


        if (listaSeleccion !=null && listaSeleccion.size() >0){

            for (Renderable seleccion: listaSeleccion) {
                int top=  seleccion.getBounds().top;
                int bottom =seleccion.getBounds().bottom;
                int right = seleccion.getBounds().right;
                int left = seleccion.getBounds().left;

                if ((y <bottom && y > top && x < right && x > left) && seleccion != listaSeleccion.get(0) )
                {
                    noEstaSeleccionada = false;
                }

            }

        }

        return noEstaSeleccionada;
    }


    private void marcarDibujo(int x, int y) {
        SquareDrawable square = new SquareDrawable("#0000FF");
        square.setBounds(x-lado, y-lado, x + lado,y + lado);
        canvasView.addRenderable(square);
    }



    private int getHotspotColor(int x, int y) {
        if (imgMask != null) {
            imgMask.setDrawingCacheEnabled(true);
            hotspots = Bitmap.createBitmap(imgMask.getDrawingCache());
            imgMask.setDrawingCacheEnabled(false);
            return hotspots.getPixel(x, y);
        }
        return -1;
    }


    private boolean isCorrecta(int touchColor) {
        return (-14503604 == touchColor);
    }

    private boolean isInCorrecta(int touchColor) {
        return (-1237980 == touchColor);
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

        handler.removeCallbacks(runnable);
        long local = SystemClock.elapsedRealtime();
        long tiempo = local - tiempo_inicio + tiempo_ejecutado;
        tiempoNivel = tiempoNivel + tiempo;

        if (isUltimoNivel()) {
            canvasView.pausa();
            guardar();
        } else {
            nivel++;
            cargarSiguienteNivel();
        }

    }

    private void calcularPuntaje() {


        System.out.println("Tiempo: " + tiempoNivel);

        if (puntos>=puntajePlus){
            if(tiempoNivel<=40000) puntos++;
            if (tiempoNivel<=30000) puntos++;
            if(tiempoNivel<=20000) puntos++;
            if(tiempoNivel<=10000) puntos++;
        }

    }


    private boolean isUltimoNivel() {
        return nivel == ultimoNivel;
    }

    private void cargarSiguienteNivel() {
        inicializarVariables();
        //iniciarCronometro();

    }

    private void inicializarVariables() {

            tiempo_ejecutado = 0;
            iniciarCronometro();
            cargarFigura();
            canvasView.inicializar();
            ClearCanvas square = new ClearCanvas();
            square.setBounds(0, 0, canvasView.getWidth(), canvasView.getHeight());
            canvasView.addRenderable(square);

    }

    private void iniciarCronometro() {
        handler.postDelayed(runnable, TIEMPO_NIVEL - tiempo_ejecutado);
        tiempo_inicio = SystemClock.elapsedRealtime();
        crono.setBase(tiempo_inicio - tiempo_ejecutado);
        crono.start();
    }

    private void cargarFigura() {

        if (nivel == PRIMER_NIVEL) {
            try {
                String jsonString = JSONLoader.loadJSON(getResources().openRawResource(R.raw.animales));
                JSONObject jsonRootObject = new JSONObject(jsonString);
                JSONArray jsonFigurasArray = jsonRootObject.getJSONArray("animales");
                listAnimales = new ArrayList<>();
                listMasks = new ArrayList<>();
                if (jsonFigurasArray != null) {
                    int len = jsonFigurasArray.length();
                    for (int i = 0; i < len; i++) {
                        String animales = jsonFigurasArray.get(i).toString();
                        listAnimales.add(animales);
                        listMasks.add(animales + "_mask");
                    }
                    ultimoNivel = len - 1;
                }
            } catch (JSONException e) {
                guardar();
            }
        }

        //Carga la figura y la solucion
        imgAnimales.setImageResource(getResources().getIdentifier(listAnimales.get(nivel), "drawable", this.getPackageName()));
        imgMask.setImageResource(getResources().getIdentifier(listMasks.get(nivel), "drawable", this.getPackageName()));
         }

    private void guardar() {

        calcularPuntaje();
        sumarPuntos(puntos);
        AdministradorJuegos.getInstance().guardarJuego(this);
    }

    private void sumarPuntos(Integer puntos) {
        AdministradorJuegos.getInstance().sumarPuntos(puntos);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void finish() {
        canvasView.pausa();
        super.finish();
        handler.removeCallbacks(runnable);

    }

    @Override
    public void onResume() {
        super.onResume();
        iniciarCronometro();
        canvasView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        canvasView.pausa();
        pararCronometro();

    }

    private void pararCronometro() {
        handler.removeCallbacks(runnable);
        crono.stop();
        long local = SystemClock.elapsedRealtime();
        tiempo_ejecutado = local - tiempo_inicio + tiempo_ejecutado;
    }



}


