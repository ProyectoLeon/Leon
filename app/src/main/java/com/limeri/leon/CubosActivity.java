package com.limeri.leon;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.limeri.leon.Models.AdministradorJuegos;
import com.limeri.leon.Models.Navegacion;
import com.limeri.leon.common.JSONLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.neuroph.contrib.imgrec.ImageRecognitionPlugin;
import org.neuroph.contrib.imgrec.image.Image;
import org.neuroph.contrib.imgrec.image.ImageFactory;
import org.neuroph.core.NeuralNetwork;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CubosActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PRIMER_NIVEL = 0;
    private ImageRecognitionPlugin imageRecognition;
    private File foto;
    private Uri uriImage;
    private String pathFoto;
    private int nivel;
    private int ultimoNivel;
    private List<Cubo> listCubos;
    private ImageView imgCubo;
    private Cubo respuesta;
    private Integer intentos;
    private Chronometer crono;
    private long tiempo_ejecutado = 0;
    private long tiempo_inicio;
    private boolean isFinTiempo;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            isFinTiempo = true;
            guardarRespuesta(null);
            iniciarCronometro();
        }
    };
    private Runnable redNeuronal = new Runnable() {
        public void run() {
            //Abro el archivo de la red neuronal
            InputStream is = getResources().openRawResource(R.raw.red1);
            //Cargo la red a partir del archivo
            NeuralNetwork nnet = NeuralNetwork.load(is);
            //Obtengo el plugin de reconocimiento
            imageRecognition = (ImageRecognitionPlugin) nnet.getPlugin(ImageRecognitionPlugin.class);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cubos);

        //Busco los ImageView
        imgCubo = (ImageView) findViewById(R.id.cubo);
        crono = (Chronometer) findViewById(R.id.cronometro);

        // Agrego el menu y pongo el puntaje en 0
        Navegacion.agregarMenuJuego(this);
        AdministradorJuegos.getInstance().inicializarJuego();

        //Seteo el metodo en el botón Reconocer
        View reconocer = findViewById(R.id.reconocer);
        if (reconocer != null) {
            reconocer.setOnClickListener(clickReconocer());
        }

        //Cargo la red neuronal para el reconocimiento de la imagen
        cargarRedNeuronal();

        inicializarVariables();
    }

    private void cargarRedNeuronal() {
        new Thread(null, redNeuronal, "dataLoader", 32000).start();
    }

    private View.OnClickListener clickReconocer() {

        return new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                abrirCamara();
            }
        };
    }

    private void cargarCubo() {

        if (nivel == PRIMER_NIVEL) {
            try {
                String jsonString = JSONLoader.loadJSON(getResources().openRawResource(R.raw.cubos));
                JSONObject jsonRootObject = new JSONObject(jsonString);
                JSONArray jsonCubosArray = jsonRootObject.getJSONArray("cubos");
                listCubos = new ArrayList<>();
                if (jsonCubosArray != null) {
                    int len = jsonCubosArray.length();
                    for (int i = 0; i < len; i++) {
                        JSONObject jsonCubo = jsonCubosArray.getJSONObject(i);
                        Cubo cubo = new Cubo();
                        cubo.nombre = jsonCubo.getString("nombre");
                        cubo.tiempo = jsonCubo.getInt("tiempo") * 1000;
                        cubo.intentos = jsonCubo.getInt("intentos");
                        cubo.puntos = jsonCubo.getInt("puntos");

                        Type listType = new TypeToken<List<Cubo.Bonificacion>>() {}.getType();
                        JSONArray jsonBonificacion = jsonCubo.getJSONArray("bonificacion");
                        cubo.bonificacion = (List<Cubo.Bonificacion>) new Gson().fromJson(jsonBonificacion.toString(), listType);

                        listCubos.add(cubo);
                    }
                    ultimoNivel = len - 1;
                }
            } catch (JSONException e) {
                guardar();
            }
        }

        //Cargo el cubo al imageView
        respuesta = listCubos.get(nivel);
        imgCubo.setImageResource(getResources().getIdentifier(respuesta.nombre, "drawable", this.getPackageName()));
    }

    private void guardar() {
        AdministradorJuegos.getInstance().guardarJuego(this);
    }

    private void abrirCamara() {
        try {
            //Creo un File para la imagen a capturar
            foto = createImageFile();
            uriImage = Uri.fromFile(foto);
            //Creo el Intent de la cámara
            Intent camaraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (camaraIntent.resolveActivity(getPackageManager()) != null) {
                //Indico que la imagen la guarde en el File creado
                camaraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriImage);
                startActivityForResult(camaraIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file path
        pathFoto = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                //Obtengo la imagen a partir del path guardado
                Image image = ImageFactory.getImage(pathFoto);
                //Muestro el nombre de la imagen reconocida
                String imagen = "cubo" + reconocerImagen(image);
                //Elimino el File donde se guardó la imagen
                foto.delete();

                guardarRespuesta(imagen);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void guardarRespuesta(String imagen) {
        handler.removeCallbacks(runnable);
        Integer puntos = respuesta.puntos;
        Boolean pasaNivel = true;
        if(respuesta.nombre.equals(imagen)) {
            Toast.makeText(this, "Correcto", Toast.LENGTH_SHORT);
            if (masDeUnIntento()) {
                puntos--;
            }
        } else {
            intentos++;
            if (noTieneOtroIntento() || isFinTiempo) {
                puntos = 0;
            } else {
                pasaNivel = false;
            }
        }
        if (pasaNivel) {
            sumarPuntos(puntos);
            nivel++;
            if (isUltimoNivel()) {
                guardar();
            } else {
                cargarSiguienteNivel();
            }
        }
    }

    private void sumarPuntos(Integer puntos) {
        AdministradorJuegos.getInstance().sumarPuntos(puntos);
    }

    private boolean isUltimoNivel() {
        return ultimoNivel == nivel;
    }

    private boolean noTieneOtroIntento() {
        return intentos == respuesta.intentos;
    }

    private boolean masDeUnIntento() {
        return intentos > 1;
    }

    private void cargarSiguienteNivel() {
        inicializarVariables();
        //iniciarCronometro();
    }

    private void inicializarVariables() {
        intentos = 0;
        tiempo_ejecutado = 0;
        isFinTiempo = false;
        cargarCubo();
    }

    private void iniciarCronometro() {
        handler.postDelayed(runnable, respuesta.tiempo - tiempo_ejecutado);
        tiempo_inicio = SystemClock.elapsedRealtime();
        crono.setBase(tiempo_inicio - tiempo_ejecutado);
        crono.start();
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void finish() {
        super.finish();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onResume() {
        super.onResume();
//        if(!isSiguienteNivel) {
            iniciarCronometro();
//        }
//        isSiguienteNivel = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        pararCronometro();
    }

    private void pararCronometro() {
        handler.removeCallbacks(runnable);
        crono.stop();
        long local = SystemClock.elapsedRealtime();
        tiempo_ejecutado = local - tiempo_inicio + tiempo_ejecutado;
    }

    private String reconocerImagen(Image image) {
        //Reconozco la imagen
        HashMap<String, Double> output = imageRecognition.recognizeImage(image);
        //Devuelvo el nombre de la imagen reconocida
        return getAnswer(output);
    }

    private String getAnswer(HashMap<String, Double> output) {
        double highest = 0;
        String answer = "";
        for (Map.Entry<String, Double> entry : output.entrySet()) {
            if (entry.getValue() > highest) {
                highest = entry.getValue();
                answer = entry.getKey();
            }
        }
        return answer;
    }

    class Cubo {
        private String nombre;
        private Integer tiempo;
        private Integer intentos;
        private Integer puntos;
        private List<Bonificacion> bonificacion = new ArrayList<>();

        class Bonificacion {
            private Integer limite;
            private Integer puntos;
        }
    }
}
