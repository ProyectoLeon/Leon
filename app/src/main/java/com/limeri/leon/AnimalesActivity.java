package com.limeri.leon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.util.Objects;



public class AnimalesActivity extends AppCompatActivity {

    public static final int PRIMER_NIVEL = 0;
    public final int ANIMAL = 0;
    public final int OTRA_FIGURA = 1;
    public static final int TIEMPO_NIVEL = 20000; //En milisegundos
    private int nivel = PRIMER_NIVEL;
    private ImageView imgAnimales;
    private ImageView imgMask;
    private ImageView imgFront;
    private List<String> listAnimales;
    private ArrayList<ArrayList> listaSeleccion = new ArrayList<>();
    private int ultimoNivel = 1;
    private List<String> listMasks;
    private ArrayList<ImageView> listaImg = new ArrayList<>();
    private Chronometer crono;
    private long tiempo_ejecutado = 0;
    private long tiempo_inicio;
    private Button siguiente;
    private TextView resultado;
    private String res = "prueba";
    private Canvas canvas;
    private Bitmap hotspots;
    FrameLayout frame;
    private SurfaceHolder surfaceHolder;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private CanvasView canvasView;
    int puntos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animales);

        //Busco los ImageView
        imgAnimales = (ImageView) findViewById(R.id.animales);
        imgMask = (ImageView) findViewById(R.id.mask);
        crono = (Chronometer) findViewById(R.id.cronometro);


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
                        System.out.println("Es correcta");
                    } else {
                        if (isInCorrecta(touchColor)) {
                            puntos--;
                            marcarDibujo((int) event.getX(), (int) event.getY());
                            System.out.println("Es incorrecta");
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
        square.setBounds(x-50, y-50, x + 50,y + 50);
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
        long local = SystemClock.elapsedRealtime();
        return (-14503604 == touchColor);
    }

    private boolean isInCorrecta(int touchColor) {
        long local = SystemClock.elapsedRealtime();
        return (-1237980 == touchColor);
    }



    private View.OnClickListener clickSiguiente() {

        return new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                sumarPuntos(puntos);
                //guardarRespuesta(); según los puntos del nivel, sumar por el tiempo
                if (isUltimoNivel()) {
                    canvasView.inicializar();
                    guardar();
                } else {
                    nivel++;
                    cargarSiguienteNivel();
                }
            }
        };
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
            puntos = 0;
            cargarFigura();
            canvasView.inicializar();
            ClearCanvas square = new ClearCanvas();
            square.setBounds(0, 0, canvasView.getWidth(), canvasView.getHeight());
            canvasView.addRenderable(square);

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
        AdministradorJuegos.getInstance().guardarJuego(this);
    }

    private void sumarPuntos(Integer puntos) {
        AdministradorJuegos.getInstance().sumarPuntos(puntos);
    }

    private Integer obtenerPuntos() {
        return AdministradorJuegos.getInstance().obtenerPuntos();
    }



}


