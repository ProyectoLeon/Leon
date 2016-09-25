package com.limeri.leon;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import android.view.SurfaceHolder;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
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
import com.limeri.leon.common.JSONLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
    View frame;
    private SurfaceHolder surfaceHolder;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    // private DrawCircle circulo;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            guardarRespuesta(Color.RED);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animales);

        //Busco los ImageView
        imgAnimales = (ImageView) findViewById(R.id.animales);
       //
        imgFront = (ImageView) findViewById(R.id.animales);


        imgMask = (ImageView) findViewById(R.id.mask);
        crono = (Chronometer) findViewById(R.id.cronometro);


        Navegacion.agregarMenuJuego(this);
        AdministradorJuegos.getInstance().inicializarJuego();

        //Seteo el metodo Siguiente
        Button siguiente = (Button) findViewById(R.id.siguiente);
        if (siguiente != null) {
            siguiente.setOnClickListener(clickSiguiente());
        }

        //Seteo el metodo sumar puntaje por cada vez que clickea sobre una figura y dibujo círculo
        frame = findViewById(R.id.contenedor);

        if (frame != null) {
            frame.setOnTouchListener(sumarPuntaje());
        }


        inicializarVariables();

    }


    private View.OnTouchListener sumarPuntaje() {
        return new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent ev) {
                final int action = ev.getAction();
                final int evX = (int) ev.getX();
                final int evY = (int) ev.getY();
                ArrayList<Integer> centerImg;
                if (action == MotionEvent.ACTION_DOWN) {
                    return true;
                }
                if (action == MotionEvent.ACTION_UP) {
                    int touchColor = getHotspotColor(evX, evY);
                    if (isNotPreviouslySelected(evX,evY)) {
                        int resultado = guardarRespuesta(touchColor);
                        if (resultado == ANIMAL) {
                            centerImg = getRegionOfSelected(evX, evY, Color.BLACK);
                            dibujarContornoRespuesta(centerImg);
                        } else if (resultado == OTRA_FIGURA) {
                            centerImg = getRegionOfSelected(evX, evY, -1237980);
                            dibujarContornoRespuesta(centerImg);
                        }
                        return true;
                    }
                }
                return true;
            }
        };
    }

    private void dibujarContornoRespuesta(ArrayList<Integer> centerImg) {
        BitmapFactory.Options myOptions = new BitmapFactory.Options();
        myOptions.inDither = true;
        myOptions.inScaled = false;
        myOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// important
        myOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.animales,myOptions);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);


        Bitmap workingBitmap = Bitmap.createBitmap(bitmap);
        Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);
        canvas = new Canvas(mutableBitmap);
        canvas.drawCircle(centerImg.get(0),centerImg.get(1), 25, paint);
        //canvas.drawLine(centerImg.get(0)-30,centerImg.get(1),centerImg.get(0)+30,centerImg.get(1),paint);
        imgAnimales.setAdjustViewBounds(true);
        imgAnimales.setImageBitmap(mutableBitmap);
    }


    private ArrayList<Integer> getRegionOfSelected (int x, int y, int color) {
            ArrayList<Integer> centro = new ArrayList<>();
            ArrayList<Integer> coords = new ArrayList<>();
            boolean anchoDerecho = false;
            boolean anchoIzquierdo = false;
            boolean altoTecho = false;
            boolean altoPiso = false;
            int relativeXR = x;
            int relativeXL = x;
            int relativeYT = y;
            int relativeYD = y;
            while (anchoDerecho==false && anchoIzquierdo==false && altoTecho == false && altoPiso == false) {
                   if (anchoDerecho==false) {
                       relativeXR =relativeXR +3;
                       int pixel = getHotspotColor(relativeXR,y);
                       if (color != pixel) {
                            anchoDerecho = true;
                       }
                   }
                   if (anchoIzquierdo==false) {
                        relativeXL = relativeXL -3;
                       int pixel = getHotspotColor(relativeXL,y);
                       if (color != pixel) {
                            anchoIzquierdo = true;
                       }
                   }
                   if (altoTecho==false) {
                       relativeYD = relativeYD - 3;
                       int pixel = getHotspotColor(x,relativeYD);
                       if (color != pixel) {
                            altoTecho = true;
                       }
                   }
                   if (altoPiso==false) {
                       relativeYT = relativeYT +3;
                       int pixel = getHotspotColor(x,relativeYT);
                       if (color != pixel) {
                            altoPiso = true;
                       }
                   }
            }
        coords.add(0,relativeXR);
        coords.add(1,relativeXL);
        coords.add(2,relativeYT);
        coords.add(3,relativeYD);
        listaSeleccion.add(coords);
        centro.add((relativeXL+relativeXR)/2);
        centro.add((relativeYD+relativeYT)/2);
        return centro;
    }

    private int getHotspotColor(int x, int y) {
       // ImageView img = (ImageView) findViewById(R.id.mask);
        if (imgMask != null) {
            imgMask.setDrawingCacheEnabled(true);
            hotspots = Bitmap.createBitmap(imgMask.getDrawingCache());
            imgMask.setDrawingCacheEnabled(false);
            return hotspots.getPixel(x, y);
        }
        return -1;
    }

    private boolean isNotPreviouslySelected(int x, int y) {
        boolean nonSelected = true;
        int runnedCoords=0;
        for (ArrayList<Integer> crtSelect: listaSeleccion) {
            if (x > crtSelect.get(0) && x < crtSelect.get(1)) {
                if (y > crtSelect.get(2) && y < crtSelect.get(3))
                    nonSelected = false;
                {
                }
            }
        }
                /*
        if (coordsImg.size()>0) {
            while (coordsImg.size() <= runnedCoords && nonSelected == true) {
                if (x>coordsImg.get(runnedCoords) && x<coordsImg.get(runnedCoords+1)) {
                    if (y>coordsImg.get(runnedCoords+2) && y<coordsImg.get(runnedCoords+3)) {
                        nonSelected = false;
                    } else {
                        runnedCoords= runnedCoords+4;
                    }
                 } else {
                 runnedCoords= runnedCoords+4;
                 }
            }
        }*/
        return nonSelected;
    }

    private boolean isCorrecta(int touchColor) {
        long local = SystemClock.elapsedRealtime();
        return (Color.BLACK == touchColor);
    }

    private boolean isInCorrecta(int touchColor) {
        long local = SystemClock.elapsedRealtime();
        return (-1237980 == touchColor);
    }


    private int guardarRespuesta(int color) {
        handler.removeCallbacks(runnable);
        if (isCorrecta(color)) {
            sumarPuntos(1);
            return(0);
        } else if (isInCorrecta(color)){
            sumarPuntos(-1);
            return(1);
        }
        return 2;
    }

    private View.OnClickListener clickSiguiente() {

        return new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //guardarRespuesta(); según los puntos del nivel, sumar por el tiempo
                if (isUltimoNivel()) {
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
        cargarFigura();

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
        int puntos = obtenerPuntos();
        resultado = (TextView)findViewById(R.id.resultado);
        resultado.setText(puntos);

    }

    private void sumarPuntos(Integer puntos) {
        AdministradorJuegos.getInstance().sumarPuntos(puntos);
    }

    private Integer obtenerPuntos() {
        return AdministradorJuegos.getInstance().obtenerPuntos();
    }

}

