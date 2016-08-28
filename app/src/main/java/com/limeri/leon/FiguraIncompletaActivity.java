package com.limeri.leon;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;

import com.limeri.leon.Models.AdministradorJuegos;
import com.limeri.leon.Models.Navegacion;
import com.limeri.leon.common.JSONLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FiguraIncompletaActivity extends AppCompatActivity {

    public static final int NIVEL_INICIO_RETROGRESION = 2;
    public static final int MAXIMO_ERRORES = 4;
    public static final int FIN_RETROGRESION = 2;
    public static final List<Integer> NIVELES_INICIALES = Arrays.asList(3, 4);
    public static final int PRIMER_NIVEL = 3;
    public static final int TIEMPO_NIVEL = 20;
    private int nivel = PRIMER_NIVEL;
    private int cantCorrectasSeguidas = 0;
    private int cantIncorrectasSeguidas = 0;
    private ImageView imgFigura;
    private ImageView imgMask;
    private boolean retrogresion;
    private int nivelErrado;
    private List<String> listFiguras;
    private int ultimoNivel = 0;
    private List<String> listMasks;
    private Chronometer crono;
    private long tiempo_ejecutado = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_figura_incompleta);

        //Busco los ImageView
        imgFigura = (ImageView) findViewById(R.id.figura);
        imgMask = (ImageView) findViewById(R.id.mask);

        Navegacion.agregarMenuJuego(this);
        AdministradorJuegos.getInstance().inicializarJuego();

        //Seteo el metodo Siguiente
        View frame = findViewById(R.id.contenedor);
        if (frame != null) {
            frame.setOnTouchListener(cargarSiguiente());
        }

        inicializarVariables();
    }

    private View.OnTouchListener cargarSiguiente() {
        return new View.OnTouchListener() {
            public boolean onTouch (View v, MotionEvent ev){
                return guardarRespuesta(ev);
            }
        };
    }

    private boolean guardarRespuesta(MotionEvent ev) {
        final int action = ev.getAction();
        final int evX = (int) ev.getX();
        final int evY = (int) ev.getY();
        if (action == MotionEvent.ACTION_UP) {
            int touchColor = getHotspotColor(evX, evY);
            if (isCorrecta(touchColor)) {
                cantIncorrectasSeguidas = 0;
                cantCorrectasSeguidas++;
                if (!retrogresion) {
                    nivel++;
                } else {
                    nivel--;
                    if (isFinRetrogresion()) {
                        finalizarRetrogresion();
                    }
                }
                sumarPuntos(1);
            } else {
                cantCorrectasSeguidas = 0;
                cantIncorrectasSeguidas++;
                if (isMaximoErrores()) {
                    guardar();
                } else if (isNivelesIniciales()) {
                    nivelErrado = nivel;
                    iniciarRetrogresion();
                } else {
                    if (!retrogresion) {
                        nivel++;
                    } else {
                        nivel--;
                    }
                }
            }
            if (isUltimoNivel()){
                guardar();
            } else {
                cargarSiguienteNivel();
            }
        }
        return true;
    }

    private int getHotspotColor (int x, int y) {
        ImageView img = (ImageView) findViewById (R.id.mask);
        if (img != null) {
            img.setDrawingCacheEnabled(true);
            Bitmap hotspots = Bitmap.createBitmap(img.getDrawingCache());
            img.setDrawingCacheEnabled(false);
            return hotspots.getPixel(x, y);
        }
        return 0;
    }

    private boolean isCorrecta(int touchColor) {
        long tiempo = (SystemClock.elapsedRealtime() - crono.getBase() + tiempo_ejecutado)/1000;
        return Color.BLACK == touchColor && (tiempo <= TIEMPO_NIVEL);
    }

    private void finalizarRetrogresion() {
        nivel = nivelErrado + 1;
        retrogresion = false;
    }

    private boolean isFinRetrogresion() {
        return cantCorrectasSeguidas == FIN_RETROGRESION;
    }

    private void iniciarRetrogresion() {
        nivel = NIVEL_INICIO_RETROGRESION;
        retrogresion = true;
    }

    private boolean isMaximoErrores() {
        return cantIncorrectasSeguidas == MAXIMO_ERRORES;
    }

    private boolean isNivelesIniciales() {
        return NIVELES_INICIALES.contains(nivel);
    }

    private boolean isUltimoNivel() {
        return nivel == ultimoNivel;
    }

    private void cargarSiguienteNivel() {
        inicializarVariables();
    }

    private void inicializarVariables() {
        cargarFigura();
        iniciarCronometro();
    }

    private void iniciarCronometro() {
        crono = (Chronometer) findViewById(R.id.cronometro);
        crono.setBase(SystemClock.elapsedRealtime());
        crono.start();
    }

    private void sumarPuntos(Integer puntos) {
        AdministradorJuegos.getInstance().sumarPuntos(puntos);
    }

    private void guardar() {
        AdministradorJuegos.getInstance().guardarJuego(this);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onResume(){
        super.onResume();
        iniciarCronometro();

    }

    @Override
    public void onPause(){
        super.onPause();
        pararCronometro();
    }

    private void pararCronometro() {
        crono.stop();
        tiempo_ejecutado = tiempo_ejecutado + SystemClock.elapsedRealtime() - crono.getBase();
    }

    private void cargarFigura() {

        if (nivel == PRIMER_NIVEL) {
            try {
                String jsonString = JSONLoader.loadJSON(getResources().openRawResource(R.raw.figurasincompletas));
                JSONObject jsonRootObject = new JSONObject(jsonString);
                JSONArray jsonFigurasArray = jsonRootObject.getJSONArray("figuras");
                listFiguras = new ArrayList<>();
                listMasks = new ArrayList<>();
                if (jsonFigurasArray != null) {
                    int len = jsonFigurasArray.length();
                    for (int i=0;i<len;i++){
                        String figura = jsonFigurasArray.get(i).toString();
                        listFiguras.add(figura);
                        listMasks.add(figura + "_mask");
                    }
                    ultimoNivel = len - 1;
                }
            } catch (JSONException e) {
                guardar();
            }
        }

        //Carga la figura y la solucion
        imgFigura.setImageResource(getResources().getIdentifier(listFiguras.get(nivel), "drawable", this.getPackageName()));
        imgMask.setImageResource(getResources().getIdentifier(listMasks.get(nivel), "drawable", this.getPackageName()));
    }
}
